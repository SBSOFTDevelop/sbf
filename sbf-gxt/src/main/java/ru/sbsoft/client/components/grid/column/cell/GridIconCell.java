package ru.sbsoft.client.components.grid.column.cell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import java.util.Map;
import ru.sbsoft.shared.model.ImageBase64;

/**
 *
 * @author Kiselev
 */
public class GridIconCell extends CustomCell<Object> {

    interface Template extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<img alt=\"{0}\" src=\"data:{1};base64,{2}\" />")
        //@SafeHtmlTemplates.Template("<img style=\"margin: 0px auto; display: block;\" alt=\"{0}\" src=\"data:{1};base64,{2}\" />")
        SafeHtml img(String altText, String imgMime, String imgBase64);
    }

    private static Template template;

    private final Map<?, ImageBase64> imageMap;

    public GridIconCell(Map<?, ImageBase64> imageMap) {
        if (template == null) {
            template = GWT.create(Template.class);
        }
        this.imageMap = imageMap;
    }

    @Override
    public void render(Context context, Object value, SafeHtmlBuilder sb) {
        if (value != null) {
            ImageBase64 img = imageMap.get(value);
            if (img != null) {
                sb.append(template.img(value.toString(), img.getMimeType(), img.getBody64()));
            } else {
                sb.appendEscaped(value.toString());
            }
            applyStyle(context);
        }
    }
}
