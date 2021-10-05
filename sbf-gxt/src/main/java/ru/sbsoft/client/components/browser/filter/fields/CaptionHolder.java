package ru.sbsoft.client.components.browser.filter.fields;

/**
 * Заголовок
 * @author balandin
 * @since Nov 6, 2015
 */
public class CaptionHolder extends GroupHolder {

    private final String caption;

    public CaptionHolder(String caption, int level) {
        super(level);
        this.caption = caption;
    }

    @Override
    public String getTitle() {
        return caption;
    }

    @Override
    public String getFullTitle() {
        return caption;
    }

    @Override
    public String getShortTitle() {
        return caption;
    }
}
