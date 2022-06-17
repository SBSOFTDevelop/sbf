package ru.sbsoft.shared.renderer;

/**
 * @author balandin
 * @since May 12, 2014 5:23:42 PM
 */
public class ResidentRenderer extends Renderer<Long> {

    public ResidentRenderer() {
        init();
    }

    private void init() {
        String[] tmp = new String[]{
            "Нерезидент",
            "Резидент",
            "Высококвалифицированный специалист-нерезидент"
        };
        for (int i = 0; i < tmp.length; i++) {
			addItem((long) i, tmp[i]);
        }
    }
}
