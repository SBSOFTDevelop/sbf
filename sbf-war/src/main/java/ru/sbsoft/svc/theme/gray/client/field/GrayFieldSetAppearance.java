/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.gray.client.field;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.theme.base.client.field.FieldSetDefaultAppearance;

public class GrayFieldSetAppearance extends FieldSetDefaultAppearance {

  public interface GrayFieldSetResources extends FieldSetResources {
    
    @Override
    @Source({"ru/sbsoft/svc/theme/base/client/field/FieldSet.gss", "GrayFieldSet.gss"})
    public GrayFieldSetStyle css();
  }
  
  public interface GrayFieldSetStyle extends FieldSetStyle {
    
  }
  
  
  public GrayFieldSetAppearance() {
    this(GWT.<GrayFieldSetResources>create(GrayFieldSetResources.class));
  }
  
  public GrayFieldSetAppearance(GrayFieldSetResources resources) {
    super(resources);
  }
  
}
