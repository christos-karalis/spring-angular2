package com.neurocom.crud.search;

import com.neurocom.crud.search.domain.AdvancedSearch;
import com.neurocom.crud.search.util.PredicateUtils;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * Generic Controller that could be used by any entity to do advanced search on the
 * application. It is dependent on spring jpa rest and tries to comply with its
 * responses. It is implemented with com.mysem for the creation of the queries.
 * It supports pagination with {@link org.springframework.data.domain.Pageable} and
 * it support queries on nested entities. The query is retrieved by a json object,
 * where properties should match fields on the entity. Nested objects are joins
 * that match OneToMany associations that are translated to joins and contained properties
 * should match the fields of the matched entity.
 * Created by c.karalis on 11/3/2014.
 */
@RepositoryRestController
public class AdvancedSearchController {

    private static final String BASE_MAPPING = "/{repository}";
    private static Logger logger = LoggerFactory.getLogger(AdvancedSearchController.class);

    @Autowired
    private Repositories repositories;

    @Autowired
    private PagedResourcesAssembler<Object> pagedResourcesAssembler;

    @Autowired
    private EntityManager em;

    /**
     *
     * It manages advanced queries by providing an {@link com.neurocom.crud.search.domain.AdvancedSearch}
     * that could accept nested objects that are joined to the main table that is retrieved by {repository}.
     * It supports Strings that are checked if they are contained on the record (not exact match)
     * @param resourceInformation is resolved by spring argument resolver and its information is retrieved by {repository}
     * @param advancedSearch the object that is matches against the database
     * @param pageable information that holds page, size that are retrieved as parameters
     * @param assembler is resolved by spring argument resolver and its information is retrieved by {repository}
     * @return
     * @throws ReflectiveOperationException
     */
    @RequestMapping(value = BASE_MAPPING+"/search/advanced", method = RequestMethod.POST)
    public HttpEntity<Resources<?>> advancedSearch(
            final RootResourceInformation resourceInformation, @RequestBody AdvancedSearch advancedSearch, DefaultedPageable pageable,
            PersistentEntityResourceAssembler assembler) throws ReflectiveOperationException {

        logger.debug("Advanced Search called");
        Assert.notNull(advancedSearch.getOperator(), "Please check your operators that have valid values OR/AND");
        Assert.notNull(advancedSearch.getOperands(), "You will need one operand at least to be included");

        Class<?> domainType = resourceInformation.getDomainType();
        Page<Object> page = getObjects(advancedSearch, pageable, domainType);

        Resources<?> pagedResources = pagedResourcesAssembler.toResource(page, assembler);
        return new ResponseEntity<Resources<?>>(pagedResources, HttpStatus.OK);
    }

    public Page<Object> getObjects(AdvancedSearch advancedSearch,
                                   DefaultedPageable pageable,
                                   Class<?> domainType) throws ReflectiveOperationException {
        Class predicateClass = PredicateUtils.getPredicateClass(domainType);
        Assert.notNull(predicateClass, "Not supported search for this entity");

        String name = domainType.getSimpleName();
        String pr = name.substring(0, 1).toLowerCase() + name.substring(1);
        EntityPath entityPath = (EntityPath) predicateClass.getConstructor(String.class).newInstance(pr);
        Assert.isInstanceOf(EntityPath.class, entityPath, "Please check that corresponds to entity");

        JPQLQuery query = generateQuery(advancedSearch, entityPath);
        Long total = query.fetchCount();
        Querydsl querydsl = new Querydsl(em, new PathBuilder<Object>(entityPath.getType(), entityPath.getMetadata()));

        JPQLQuery paginatedQuery = querydsl.applyPagination(pageable.getPageable(), query.select(entityPath));

        List<Object> content = total > pageable.getPageable().getOffset() ? paginatedQuery.fetch() : Collections.<Object>emptyList();

        return new PageImpl<Object>(content, pageable.getPageable(), total);
    }

    private JPQLQuery generateQuery(AdvancedSearch advancedSearch, EntityPath entityPath) throws ReflectiveOperationException {
        List<BooleanExpression> expressions = new ArrayList<BooleanExpression>();
        Map<Path, Path> joins = new LinkedHashMap<Path, Path>();
        PredicateUtils predicateUtils = new PredicateUtils(repositories);

        predicateUtils.parseMapForJoins(entityPath, advancedSearch.getOperands(), joins, expressions);

        JPQLQuery query = new JPAQuery(em).from(entityPath);
        for (Map.Entry<Path, Path> join : joins.entrySet()) {
            if (join.getValue() instanceof CollectionExpression) {
                query = query.leftJoin((CollectionExpression) join.getValue(), join.getKey());
            } else if (join.getValue() instanceof  EntityPath) {
                query = query.leftJoin((EntityPath) join.getValue(), join.getKey());
            }
        }

        if (expressions.size()>0) {
            BooleanExpression expression = expressions.get(0);
            if (advancedSearch.getOperator().equals(AdvancedSearch.Operator.AND)) {
                for (int i = 1; i < expressions.size(); i++) {
                    expression = expression.and(expressions.get(i));
                }
            } else {
                for (int i = 1; i < expressions.size(); i++) {
                    expression = expression.or(expressions.get(i));
                }
            }
            query.distinct().where(expression);
        }
        return (JPQLQuery) query.distinct().where();
    }
}