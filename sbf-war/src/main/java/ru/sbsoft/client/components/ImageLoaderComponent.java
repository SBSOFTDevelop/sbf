/*
 * Copyright (c) 2021 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.client.components;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.ProgressBar;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.FlowLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.MarginData;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import elemental2.dom.DataTransfer;
import elemental2.dom.DomGlobal;
import elemental2.dom.DragEvent;
import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.File;
import elemental2.dom.FileList;
import elemental2.dom.FileReader;
import elemental2.dom.FormData;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLDocument;
import elemental2.dom.HTMLImageElement;
import elemental2.dom.ProgressEvent;
import elemental2.dom.XMLHttpRequest;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 *
 * @author sokolov
 */
public class ImageLoaderComponent implements IsWidget {

    private final String EMPTY_IMAGE = "data:,";
    private final HTMLAnchorElement anchorElement;
    private final HTMLImageElement imageElement;
    private final FlowLayoutContainer widget;
    private final ProgressBar progressBar;
    private final TextButton clearButton;

    public ImageLoaderComponent(int width, int height) {
        HTMLDocument document = DomGlobal.document;

        imageElement = (HTMLImageElement) document.createElement("img");
        imageElement.setAttribute("style", "width: 100%; height: 100%;");

        Element divElement = document.createElement("div");
        divElement.appendChild(imageElement);
        divElement.setAttribute("style", "border: 1px dashed gray; width: " + width + "px; height: " + height + "px;");
        divElement.addEventListener("dragover", new EventListener() {
            @Override
            public void handleEvent(Event event) {
                event.stopPropagation();
                event.preventDefault();

                DragEvent dragEvent = (DragEvent) event;
                DataTransfer dataTransfer = dragEvent.dataTransfer;
                dataTransfer.dropEffect = "copy";
            }
        });

        divElement.addEventListener("drop", new EventListener() {
            @Override
            public void handleEvent(Event event) {
                event.stopPropagation();
                event.preventDefault();

                DragEvent dragEvent = (DragEvent) event;
                DataTransfer dataTransfer = dragEvent.dataTransfer;
                FileList files = dataTransfer.files;
                for (int i = 0; i < files.length; i++) {
                    File file = files.item(i);
                    upload(file);
                    readAndUploadFile(file);
                }
            }
        });
        /*
            divElement.addEventListener("dragEnd", new EventListener() {
                @Override
                public void handleEvent(Event event) {
                    GWT.log("dragEnd");
                }
            });
         */
        anchorElement = (HTMLAnchorElement) document.createElement("a");

        FlowLayoutContainer flc1 = new FlowLayoutContainer();
        XElement flcEl = flc1.getElement();

        appendTo(flcEl, divElement);

        FlowLayoutContainer flc2 = new FlowLayoutContainer();
        XElement flcEl2 = flc1.getElement();

        appendTo(flcEl2, anchorElement);

        progressBar = new ProgressBar();
        progressBar.setPixelSize(width, 30);
        progressBar.getCell().setProgressText(I18n.get(SBFEditorStr.labelIMLUploaded));

        clearButton = new TextButton(I18n.get(SBFEditorStr.labelIMLClear));
        clearButton.addSelectHandler((SelectEvent event) -> {
            setStringImage(EMPTY_IMAGE);
        });

        widget = new FlowLayoutContainer();
        widget.add(new HTML(I18n.get(SBFEditorStr.labelIMLDragAndDrop)));
        widget.add(flc1);
        widget.add(flc2);
        widget.add(progressBar);
        widget.add(clearButton, new MarginData(0, 0, 0, width / 2 - 35));
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public String getStringImage() {
        return EMPTY_IMAGE.equals(imageElement.src) ? "" : imageElement.src;
    }

    public void setStringImage(String image) {
        if (null == image || image.isEmpty()) {
            imageElement.src = EMPTY_IMAGE;
        } else {
            imageElement.src = image;
        }
        progressBar.setValue(0d);
    }

    protected void readAndUploadFile(final File file) {
        final FileReader reader = new FileReader();
        reader.onloadend = new FileReader.OnloadendFn() {
            @Override
            public Object onInvoke(ProgressEvent progress) {
                FileReader.ResultUnionType result = reader.result;
                String url = result.asString();
                download(file, url);
                return null;
            }
        };
        reader.readAsDataURL(file);
    }

    protected void download(File file, String url) {
        imageElement.src = url;
        /*    
        anchorElement.innerHTML = I18n.get(SBFEditorStr.labelIMLDownload);
        anchorElement.setAttribute("download", file.name);
        anchorElement.setAttribute("href", url);
         */
    }

    private native void appendTo(XElement target, Element el) /*-{
    target.appendChild(el);
  }-*/;

    protected void upload(File file) {

        FormData formData = new FormData();
        formData.append("file", file);

        XMLHttpRequest xhr = new XMLHttpRequest();
        xhr.upload.addEventListener("progress", new EventListener() {
            @Override
            public void handleEvent(Event ev) {
                ProgressEvent event = (ProgressEvent) ev;
                double completed = event.loaded / event.total;
                progressBar.setValue(completed);
            }
        });

        xhr.open("POST", "/");
        xhr.send(formData);
    }

}
