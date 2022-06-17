/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.svc.themebuilder.base.client.config.ThemeDetails;
import ru.sbsoft.svc.widget.core.client.button.Tools;

/**
 */
public class Css3Tools extends Tools {

  public interface Css3ToolStyle extends ToolStyle {
  }

  public interface Css3ToolResources extends ToolResources, ClientBundle {

    @Source({"ru/sbsoft/svc/theme/base/client/tool/BaseTools.gss", "Css3Tools.gss"})
    Css3ToolStyle style();

    ThemeDetails theme();

    @Override
    ImageResource closeIcon();

    @Override
    ImageResource closeOverIcon();

    @Override
    ImageResource collapseIcon();

    @Override
    ImageResource collapseOverIcon();

    @Override
    ImageResource doubleDownIcon();

    @Override
    ImageResource doubleDownOverIcon();

    @Override
    ImageResource doubleLeftIcon();

    @Override
    ImageResource doubleLeftOverIcon();

    @Override
    ImageResource doubleRightIcon();

    @Override
    ImageResource doubleRightOverIcon();

    @Override
    ImageResource doubleUpIcon();

    @Override
    ImageResource doubleUpOverIcon();

    @Override
    ImageResource downIcon();

    @Override
    ImageResource downOverIcon();

    @Override
    ImageResource expandIcon();

    @Override
    ImageResource expandOverIcon();

    @Override
    ImageResource gearIcon();

    @Override
    ImageResource gearOverIcon();

    @Override
    ImageResource leftIcon();

    @Override
    ImageResource leftOverIcon();

    @Override
    ImageResource maximizeIcon();

    @Override
    ImageResource maximizeOverIcon();

    @Override
    ImageResource minimizeIcon();

    @Override
    ImageResource minimizeOverIcon();

    @Override
    ImageResource minusIcon();

    @Override
    ImageResource minusOverIcon();

    @Override
    ImageResource pinIcon();

    @Override
    ImageResource pinOverIcon();

    @Override
    ImageResource unpinIcon();

    @Override
    ImageResource unpinOverIcon();

    @Override
    ImageResource plusIcon();

    @Override
    ImageResource plusOverIcon();

    @Override
    ImageResource printIcon();

    @Override
    ImageResource printOverIcon();

    @Override
    ImageResource questionIcon();

    @Override
    ImageResource questionOverIcon();

    @Override
    ImageResource refreshIcon();

    @Override
    ImageResource refreshOverIcon();

    @Override
    ImageResource restoreIcon();

    @Override
    ImageResource restoreOverIcon();

    @Override
    ImageResource rightIcon();

    @Override
    ImageResource rightOverIcon();

    @Override
    ImageResource saveIcon();

    @Override
    ImageResource saveOverIcon();

    @Override
    ImageResource searchIcon();

    @Override
    ImageResource searchOverIcon();

    @Override
    ImageResource upIcon();

    @Override
    ImageResource upOverIcon();
  }

  public Css3Tools() {
    this(GWT.<Css3ToolResources>create(Css3ToolResources.class));
  }

  public Css3Tools(Css3ToolResources resources) {
    super(resources);
  }
}
