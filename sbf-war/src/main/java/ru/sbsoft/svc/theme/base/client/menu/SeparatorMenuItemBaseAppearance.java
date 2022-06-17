/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.menu;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.menu.SeparatorMenuItem.SeparatorMenuItemAppearance;

public abstract class SeparatorMenuItemBaseAppearance extends ItemBaseAppearance implements SeparatorMenuItemAppearance {

  public interface SeparatorMenuItemResources extends ItemResources {
    @Override
    SeparatorMenuItemStyle style();

  }

  public interface SeparatorMenuItemStyle extends ItemStyle {

    String menuSep();

  }

  public interface SeparatorMenuItemTemplate extends XTemplates {

    @XTemplate(source = "SeparatorMenuItem.html")
    SafeHtml template(SeparatorMenuItemStyle style);

  }

  protected final SeparatorMenuItemResources resources;
  protected SeparatorMenuItemTemplate template;

  private SeparatorMenuItemStyle style;

  public SeparatorMenuItemBaseAppearance(SeparatorMenuItemResources resources,
      SeparatorMenuItemTemplate template) {
    super(resources);
    this.resources = resources;
    this.style = resources.style();
    
    StyleInjectorHelper.ensureInjected(this.style, true);
    
    this.template = template;
  }

  public void render(SafeHtmlBuilder result) {
    result.append(template.template(style));
  }

}
