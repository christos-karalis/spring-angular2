package com.neurocom.crud.search.util;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by c.karalis on 12/2/2014.
 */
public class PredicateUtils {

    public static final String CURRENT_DATE_ACTIVE = "current_date_between";
    public static final String DATE_FROM = "date_from";
    public static final String DATE_TO = "date_to";
    public static final String RANGE_TO = "to";
    public static final String RANGE_FROM = "from";
    public static final String OR = "or";
    public static final String AND = "and";
    public static final String ISNULL = "isNull";
    public static final String ISNOTNULL = "isNotNull";

    private Repositories repositories;

    public PredicateUtils(Repositories repositories) {
        this.repositories = repositories;
    }

    public void parseMapForJoins(EntityPath entityPath, Map<String, Object> searchMap, Map<Path, Path> joins, List<BooleanExpression> expressions) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        for (Map.Entry<String, Object> operand : searchMap.entrySet()) {
            Field field = null;
            try {
                field = entityPath.getClass().getDeclaredField(operand.getKey());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (field instanceof Field) {
                if (operand.getValue() instanceof String) {
                    String value = (String) operand.getValue();
                    if (field.getType().equals(StringPath.class)) {
                        expressions.add(((StringPath) field.get(entityPath)).containsIgnoreCase(value));
                    } else if (field.getType().equals(BooleanPath.class)) {
                        BooleanExpression expression;
                        if (value != null) {
                            expression =
                                    ((BooleanPath) field.get(entityPath)).eq("true".equals(value)?true:false);
                        } else {
                            expression = ((BooleanPath) field.get(entityPath)).isNull();
                        }
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    } else if (field.getType().equals(NumberPath.class)) {
                        BooleanExpression expression = generateExpression((NumberPath) field.get(entityPath), value);
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    } else if (field.getType().equals(DatePath.class)) {
                        BooleanExpression expression = generateExpression((TemporalExpression) field.get(entityPath), value);
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    } else if (field.getType().getGenericSuperclass() instanceof ParameterizedType &&
                            EntityPathBase.class.isAssignableFrom(field.getType())) {
                        BooleanExpression expression = matchToEntityId(entityPath, field, value);
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    }
                } else if (operand.getValue() instanceof Map) {
                    if (field.get(entityPath) instanceof CollectionPathBase) {
                        addCollectionJoinAndParse(entityPath, joins, expressions, operand, field);
                    } else if (EntityPathBase.class.isAssignableFrom(field.getType())) {
                        addEntityJoinAndParse(entityPath, joins, expressions, operand, field);
                    } else if (field.get(entityPath) instanceof NumberPath) {
                        BooleanExpression expression = generateExpression((NumberPath) field.get(entityPath), operand.getValue());
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    } else if (field.get(entityPath) instanceof DatePath) {
                        BooleanExpression expression = generateExpression((DatePath) field.get(entityPath), operand.getValue());
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    } else if (field.get(entityPath) instanceof DateTimePath) {
                        BooleanExpression expression = generateExpression((DateTimePath) field.get(entityPath), operand.getValue());
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    }

                } else if (operand.getValue() instanceof Collection) {
                    Collection values = (Collection) operand.getValue();
                    if (field.getType().getGenericSuperclass() instanceof ParameterizedType &&
                            EntityPathBase.class.isAssignableFrom(field.getType())) {
                        BooleanExpression expression = matchToEntityId(entityPath, field, values);
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    }
                } else if (operand.getValue() instanceof Boolean) {
                    Boolean value = (Boolean) operand.getValue();
                    if (field.getType().equals(BooleanPath.class)) {
                        BooleanExpression expression = null;
                        expression = ((BooleanPath) field.get(entityPath)).eq(value);
                        if (expression!=null) {
                            expressions.add(expression);
                        }
                    }
                }
            } else if (operand.getKey().equals(CURRENT_DATE_ACTIVE)
                    && operand.getValue() instanceof Map) {
                TemporalExpression from = getDatePath(entityPath, (String) ((Map) operand.getValue()).get(DATE_FROM));
                TemporalExpression to = getDatePath(entityPath, (String) ((Map) operand.getValue()).get(DATE_TO));
                Date current = new Date();
                if (from!=null && to!=null) {
                    expressions.add(from.before(current).and(to.after(current)));
                } else if (from!=null) {
                    expressions.add(from.before(current));
                } else if (to!=null) {
                    expressions.add(to.after(current));
                }
            } else if ((operand.getKey().equals(OR) || operand.getKey().equals(AND))
                    && operand.getValue() instanceof Map) {
                List<BooleanExpression> orExpressions = new ArrayList<BooleanExpression>();
                parseMapForJoins(entityPath, (Map<String, Object>) operand.getValue(), joins, orExpressions);
                if (orExpressions.size()>0) {
                    BooleanExpression expression = orExpressions.get(0);
                    for (int i = 1; i < orExpressions.size(); i++) {
                        if (operand.getKey().equals(OR)) {
                            expression = expression.or(orExpressions.get(i));
                        } else {
                            expression = expression.and(orExpressions.get(i));
                        }
                    }
                    expressions.add(expression);
                }
            } else if ((operand.getKey().equals(OR) || operand.getKey().equals(AND))
                    && operand.getValue() instanceof List) {
                for (Map<String, Object> value : (List<Map<String, Object>>) operand.getValue()) {
                    List<BooleanExpression> orExpressions = new ArrayList<BooleanExpression>();
                    parseMapForJoins(entityPath, value, joins, orExpressions);
                    if (orExpressions.size()>0) {
                        BooleanExpression expression = orExpressions.get(0);
                        for (int i = 1; i < orExpressions.size(); i++) {
                            if (operand.getKey().equals(OR)) {
                                expression = expression.or(orExpressions.get(i));
                            } else {
                                expression = expression.and(orExpressions.get(i));
                            }
                        }
                        expressions.add(expression);
                    }
                }
            } else if ( ( operand.getKey().equals(ISNULL) || operand.getKey().equals(ISNOTNULL))
                    && operand.getValue() instanceof String) {
                try {
                    Field nullableField = entityPath.getClass().getDeclaredField((String) operand.getValue());
                    if (operand.getKey().equals(ISNULL)) {
                        expressions.add( ((SimpleExpression) nullableField.get(entityPath)).isNull());
                    } else {
                        expressions.add( ((SimpleExpression) nullableField.get(entityPath)).isNotNull());
                    }
                } catch (NoSuchFieldException e) {
                    //ignore this case
                }
            }

        }

    }


    public BooleanExpression matchToEntityId(EntityPath entityPath, Field field, String value) throws IllegalAccessException, NoSuchFieldException {
        if (((ParameterizedType) field.getType().getGenericSuperclass()).getActualTypeArguments().length > 0) {
            Type clazz = ((ParameterizedType) field.getType().getGenericSuperclass()).getActualTypeArguments()[0];
            String propertyId = repositories.getPersistentEntity((Class) clazz).getIdProperty().getName();

            Object propertyIdField = field.get(entityPath).getClass().getField(propertyId).get(field.get(entityPath));
            SimpleExpression object = null;
            String id  = (value.contains("http")
                    && value.lastIndexOf('/') >= 0)?value.substring(value.lastIndexOf('/') + 1):value;
            Object objectId = null;
            if (propertyIdField instanceof StringPath) {
                object = (StringPath) propertyIdField;
                objectId = id;
            } else if (propertyIdField instanceof NumberPath) {
                object = (NumberPath) propertyIdField;
                objectId = Long.valueOf(id);
            }
            return  object.eq(objectId);
        }
        return null;
    }

    public BooleanExpression matchToEntityId(EntityPath entityPath, Field field, Collection<String> values) throws IllegalAccessException, NoSuchFieldException {
        if (((ParameterizedType) field.getType().getGenericSuperclass()).getActualTypeArguments().length > 0) {
            Type clazz = ((ParameterizedType) field.getType().getGenericSuperclass()).getActualTypeArguments()[0];
            String propertyId = repositories.getPersistentEntity((Class) clazz).getIdProperty().getName();

            Object propertyIdField = field.get(entityPath).getClass().getField(propertyId).get(field.get(entityPath));
            SimpleExpression object = null;
            Collection<Object> matchedIds = new ArrayList<Object>();
            for (String value : values) {
                String id  = (value.contains("http")
                        && value.lastIndexOf('/') >= 0)?value.substring(value.lastIndexOf('/') + 1):value;
                Object objectId = null;
                if (propertyIdField instanceof StringPath) {
                    object = (StringPath) propertyIdField;
                    objectId = id;
                } else if (propertyIdField instanceof NumberPath) {
                    object = (NumberPath) propertyIdField;
                    objectId = Long.valueOf(id);
                }
                matchedIds.add(objectId);
            }
            return  object.in(matchedIds);
        }
        return null;
    }

    public TemporalExpression getDatePath(EntityPath entityPath, String path) throws IllegalAccessException {
        Assert.notNull(entityPath, "Unexpected entityPath : " + entityPath);
        Assert.notNull(path, "Unexpected path : " + path);
        try {
            Object response = entityPath.getClass().getDeclaredField(path).get(entityPath);
            if (response instanceof TemporalExpression) {
                return (TemporalExpression) response;
            } else {
                return null;
            }
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private Number convertTo(Class<Number> clazz, String value) {
        return (clazz.equals(BigDecimal.class) ?
                new BigDecimal(value) :
                Integer.parseInt(value));
    }

    public BooleanExpression generateExpression(final NumberPath matchedPath, Object matcher) throws IllegalAccessException {
        if (matcher instanceof Map) {
            Map map = (Map) matcher;
            Number from = null, to = null;
            try {
                from = map.get(RANGE_FROM) instanceof String ? convertTo(matchedPath.getType(), (String) map.get(RANGE_FROM)) :
                        map.get(RANGE_FROM) instanceof Double? new BigDecimal((Double) map.get(RANGE_FROM)) :
                                (map.get(RANGE_FROM) instanceof Integer? new BigDecimal((Integer) map.get(RANGE_FROM))
                                        : null);
            } catch (NumberFormatException nfe) {}
            try {
                to = map.get(RANGE_TO) instanceof String ? convertTo(matchedPath.getType(), (String) map.get(RANGE_TO)) :
                        map.get(RANGE_TO) instanceof Double? new BigDecimal((Double) map.get(RANGE_TO)) :
                                (map.get(RANGE_TO) instanceof Integer? new BigDecimal((Integer) map.get(RANGE_TO))
                                        : null);
            } catch (NumberFormatException nfe) {}

            if (from!=null || to!=null) {
                return matchedPath.between(from, to);
            }
        } else if  (matcher instanceof String) {
            if (matchedPath.getType().equals(BigDecimal.class)) {
                return matchedPath.eq(new BigDecimal(((String)matcher)));
            } else if (matchedPath.getType().equals(Long.class)) {
                return matchedPath.eq(Long.parseLong(((String) matcher)));
            } else {
                return matchedPath.eq(Integer.parseInt(((String) matcher)));
            }
        }
        return null;
    }

    public BooleanExpression generateExpression(final TemporalExpression matchedPath, Object matcher) throws IllegalAccessException {
        if (matcher instanceof Map) {
            Map map = (Map) matcher;
            Date from = map.get(RANGE_FROM) instanceof  String ?
                    new Date(Long.parseLong((String) map.get(RANGE_FROM))) :
                    map.get(RANGE_FROM) instanceof Long ? new Date((Long) map.get(RANGE_FROM)) : null;
            Date to = map.get(RANGE_TO) instanceof  String ?
                    new Date(Long.parseLong((String) map.get(RANGE_TO))) :
                    map.get(RANGE_TO) instanceof Long ? new Date((Long) map.get(RANGE_TO)) : null;
            if (from!=null || to!=null) {
                return matchedPath.between(from, to);
            }
        } else if  (matcher instanceof String) {

        }
        return null;
    }

    private void addEntityJoinAndParse(EntityPath entityPath, Map<Path, Path> joins, List<BooleanExpression> expressions, Map.Entry<String, Object> operand, Field field) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Class subEntityPathClass = ((EntityPath) field.get(entityPath)).getClass();
        EntityPath subEntityPath = (EntityPath) subEntityPathClass.getConstructor(String.class).newInstance(field.getName());
        joins.put(subEntityPath, (Path) field.get(entityPath));
        parseMapForJoins(subEntityPath, (Map) operand.getValue(), joins, expressions);
    }

    private void addCollectionJoinAndParse(EntityPath entityPath, Map<Path, Path> joins, List<BooleanExpression> expressions, Map.Entry<String, Object> operand, Field field) throws IllegalAccessException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, InstantiationException {
        //TODO a dirty way to get the Type of sub entityPath
        Class subEntityPathClass = ((CollectionPathBase) field.get(entityPath)).any().getClass();
        EntityPath subEntityPath = (EntityPath) subEntityPathClass.getConstructor(String.class).newInstance(field.getName());
        if (CollectionPathBase.class.isAssignableFrom(field.getType())) {
            joins.put(subEntityPath, (Path) field.get(entityPath));
        }
        parseMapForJoins(subEntityPath, (Map) operand.getValue(), joins, expressions);
    }

    public static Class getPredicateClass(Class clazz) throws ClassNotFoundException {
        String name = clazz.getName();
        return Class.forName(new StringBuilder(name).insert(name.lastIndexOf('.')+1, 'Q').toString());
    }

}