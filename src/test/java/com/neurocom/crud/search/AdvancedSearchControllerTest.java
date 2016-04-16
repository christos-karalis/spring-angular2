package com.neurocom.crud.search;

import com.neurocom.crud.config.RestDataConfig;
import com.neurocom.crud.config.WebSecurityConfig;
import com.neurocom.crud.domain.Thread;
import com.neurocom.crud.search.domain.AdvancedSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by c.karalis on 4/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestDataConfig.class, WebSecurityConfig.class})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class AdvancedSearchControllerTest {

    @Autowired
    private AdvancedSearchController advancedSearchController;

    @Test
    public void advancedSearch() throws Exception {
        AdvancedSearch advancedSearch = new AdvancedSearch();
        advancedSearch.setOperator(AdvancedSearch.Operator.AND);
        advancedSearch.setOperands(new HashMap<>());
        advancedSearch.getOperands().put("title", "readResolve");

        DefaultedPageable defaultedPageable = new DefaultedPageable(new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 10;
            }

            @Override
            public int getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        }, false);

//        System.out.println(advancedSearchController.getObjects(advancedSearch, defaultedPageable, Thread.class));
        assertEquals(advancedSearchController.getObjects(advancedSearch, defaultedPageable, Thread.class).getNumberOfElements(), 1);

    }

}