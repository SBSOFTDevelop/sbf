package ru.sbsoft.client.consts;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.client.icons.application.AppIcons;
import ru.sbsoft.client.icons.browser.BrowserIcons;
import ru.sbsoft.client.icons.editor.EditorIcons;
import ru.sbsoft.client.icons.general.GeneralIcons;
import ru.sbsoft.client.icons.treemenu.TreeMenuIcons;

/**
 * Локализованные ресурсы.
 */
public class SBFResources {

    public final static BrowserIcons BROWSER_ICONS = GWT.create(BrowserIcons.class);
    public final static GeneralIcons GENERAL_ICONS = GWT.create(GeneralIcons.class);
    public final static EditorIcons EDITOR_ICONS = GWT.create(EditorIcons.class);
    public final static TreeMenuIcons TREEMENU_ICONS = GWT.create(TreeMenuIcons.class);
    public final static AppIcons APP_ICONS = GWT.create(AppIcons.class);    
}
