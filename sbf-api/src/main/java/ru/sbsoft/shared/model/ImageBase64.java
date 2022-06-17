package ru.sbsoft.shared.model;

import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 */
public class ImageBase64 implements DTO {

    private String mimeType;
    private String body64;
    
    private ImageBase64() {
        this(null, null);
    }

    public ImageBase64(String mimeType, String body64) {
        this.mimeType = mimeType;
        this.body64 = body64;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getBody64() {
        return body64;
    }

}
