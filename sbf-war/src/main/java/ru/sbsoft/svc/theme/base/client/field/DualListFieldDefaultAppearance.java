/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.button.IconButton.IconConfig;
import ru.sbsoft.svc.widget.core.client.form.DualListField.DualListFieldAppearance;

public class DualListFieldDefaultAppearance implements DualListFieldAppearance {

  public interface DualListFieldResources extends ClientBundle {
    @Source("DualListField.gss")
    DualListFieldStyle css();
    
    @Source("up2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource up();

    @Source("doubleright2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource allRight();
    
    @Source("left2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource left();
    
    @Source("right2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource right();
    
    @Source("doubleleft2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource allLeft();
    
    @Source("down2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource down();
  }
  
  public interface DualListFieldStyle extends CssResource {
    String up();
    
    String allRight();
    
    String right();
    
    String left();
    
    String allLeft();
    
    String down();
  }
  
  private final DualListFieldResources resources;
  private final DualListFieldStyle style;
  
  public DualListFieldDefaultAppearance() {
    this(GWT.<DualListFieldResources>create(DualListFieldResources.class));
  }
  
  public DualListFieldDefaultAppearance(DualListFieldResources resources) {
    this.resources = resources;
    this.style = this.resources.css();
    
    StyleInjectorHelper.ensureInjected(style, true);
  }
  
  @Override
  public IconConfig up() {
    return new IconConfig(style.up());
  }

  @Override
  public IconConfig allRight() {
    return new IconConfig(style.allRight());
  }

  @Override
  public IconConfig right() {
    return new IconConfig(style.right());
  }

  @Override
  public IconConfig left() {
    return new IconConfig(style.left());
  }

  @Override
  public IconConfig allLeft() {
    return new IconConfig(style.allLeft());
  }

  @Override
  public IconConfig down() {
    return new IconConfig(style.down());
  }

}
