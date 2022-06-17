package ru.sbsoft.shared.grid.condition;

/**
 *
 * @author Kiselev
 */
public enum Comparison {

    EQ("="),
    GT(">"),
    GTE(">="),
    NE("!="),
    LT("<"),
    LTE("<=");

    private final String mathSign;

    Comparison(String text) {
        this.mathSign = text;
    }

    public String getMathSign() {
        return this.mathSign;
    }

    @Override
    public String toString() {
        return getMathSign();
    }

    public static Comparison getBySign(String sign) {
        for (Comparison c : values()) {
            if (c.mathSign.equals(sign)) {
                return c;
            }
        }
        if (sign == null) {
            throw new NullPointerException("Sign is null");
        }
        throw new IllegalArgumentException("No enum constant " + Comparison.class.getName() + " with sign " + sign);
    }
}
