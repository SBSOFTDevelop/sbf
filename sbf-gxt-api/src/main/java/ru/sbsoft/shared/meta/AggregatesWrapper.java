package ru.sbsoft.shared.meta;

/**
 * @author balandin
 * @since May 16, 2014 4:03:02 PM
 */
public class AggregatesWrapper<N> extends Wrapper<N> {

    private Aggregate aggregates;
    private String format;

    public AggregatesWrapper() {
    }

    public AggregatesWrapper(Aggregate aggregates, String format, N value) {
        super(value);
        this.aggregates = aggregates;
        this.format = format;
    }

    public void setAggregates(Aggregate aggregates) {
        this.aggregates = aggregates;
    }

    public Aggregate getAggregates() {
        return aggregates;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
