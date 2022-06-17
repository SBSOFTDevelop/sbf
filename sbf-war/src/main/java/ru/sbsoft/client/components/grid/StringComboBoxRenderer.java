package ru.sbsoft.client.components.grid;

import java.util.List;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.renderer.Renderer;

/**
 *
 * @author sokolov
 */
public class StringComboBoxRenderer extends Renderer<String> {

    public StringComboBoxRenderer(List<String> combo) {
        super();
        combo.forEach(v -> addItem(v, v));
    }

    public ILocalizedString render(String value) {
        return render(value, NonLocalizedString.EMPTY);
    }

}
