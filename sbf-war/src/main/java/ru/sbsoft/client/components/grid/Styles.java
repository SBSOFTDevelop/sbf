package ru.sbsoft.client.components.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Стили колонок для таблицы {@link SystemGrid}
 */
public interface Styles extends CssResource {

    Styles INST = Bundle.INST.css ();

    @ClassName ("gridImageResourceCell")
    String gridImageResourceCell ();

    @ClassName ("sbCell")
    String sbCell ();

    @ClassName ("sbCellLast")
    String sbCellLast ();

    @ClassName ("sbMarkCell")
    String sbMarkCell ();
    
    interface Bundle extends ClientBundle {

        Bundle INST = GWT.create (Bundle.class);

        @Source ("Styles.gss")
        Styles css ();
    }
}