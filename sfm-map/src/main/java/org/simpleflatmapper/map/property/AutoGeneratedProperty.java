package org.simpleflatmapper.map.property;



public class AutoGeneratedProperty {

    public static final AutoGeneratedProperty DEFAULT = new AutoGeneratedProperty(null);
    
    private final String expression;
    public AutoGeneratedProperty(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AutoGeneratedProperty that = (AutoGeneratedProperty) o;

        return expression != null ? expression.equals(that.expression) : that.expression == null;
    }

    @Override
    public int hashCode() {
        return expression != null ? expression.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AutoGeneratedProperty{" +
                "expression='" + expression + '\'' +
                '}';
    }

    public String getExpression() {
        return expression;
    }

    public static AutoGeneratedProperty of(String expression) {
        return new AutoGeneratedProperty(expression);
    }
}