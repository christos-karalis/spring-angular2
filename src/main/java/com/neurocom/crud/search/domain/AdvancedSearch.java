package com.neurocom.crud.search.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by c.karalis on 11/3/2014.
 */
public class AdvancedSearch {

    public static enum Operator {
        AND, OR;
    }

    private Operator operator;
    private Map<String, Object> operands;

    public AdvancedSearch() {
        this.operator = Operator.AND;
        this.operands = new HashMap<String, Object>();
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Map<String, Object> getOperands() {
        return operands;
    }

    public void setOperands(Map<String, Object> operands) {
        this.operands = operands;
    }
}
