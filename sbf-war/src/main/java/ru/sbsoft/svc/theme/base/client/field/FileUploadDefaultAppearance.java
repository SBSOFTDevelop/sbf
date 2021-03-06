/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.widget.core.client.form.FileUploadField.FileUploadFieldAppearance;

public class FileUploadDefaultAppearance implements FileUploadFieldAppearance {

  public interface FileUploadResources extends ClientBundle {
    @Source("FileUpload.gss")
    FileUploadStyle css();
  }

  public interface FileUploadStyle extends CssResource {
    String buttonWrap();

    String file();

    String input();

    String wrap();

  }

  public interface FileUploadTemplate extends XTemplates {
    @XTemplate("<div class='{style.wrap}'></div>")
    SafeHtml render(FileUploadStyle style);
  }

  private final FileUploadResources resources;
  private final FileUploadStyle style;
  private final FileUploadTemplate template;

  public FileUploadDefaultAppearance() {
    this(GWT.<FileUploadResources> create(FileUploadResources.class));
  }

  public FileUploadDefaultAppearance(FileUploadResources resources) {
    this.resources = resources;
    this.style = this.resources.css();

    StyleInjectorHelper.ensureInjected(this.style, true);

    this.template = GWT.create(FileUploadTemplate.class);
  }

  @Override
  public String fileInputClass() {
    return style.file();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public String wrapClass() {
    return style.wrap();
  }

  @Override
  public String textFieldClass() {
    return style.input();
  }

  @Override
  public String buttonClass() {
    return style.buttonWrap();
  }

}
