package ru.sbsoft.shared.grid.style;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.sbsoft.shared.param.DTO;

/**
 * Содержит некоторые стили отображения текста. Для использования с шаблонами
 * браузеров. Заполняется в стиле конструктора. Например:
 * {@code new CStyle().setFontSize(16).setColor(ColorConst.red)}
 *
 * @author Kiselev
 */
public class CStyle implements DTO {

    
    private FontWeight fontWeight = null;
    private FontSizeConst fontSizeConst = null;
    private Integer fontSize = null;
    private FontSizeDim fontSizeDim = null;
    private Integer color = null;
    private Integer backgroundColor = null;
    private TextDecorationLine textDecorationLine;

//aplha canal for backgroundColor
    private float alpha = 0f;

    private TextAlign textAlign = null;

    public CStyle setFontWeight(FontWeight fontWeight) {
        this.fontWeight = fontWeight;
        return this;
    }

    public CStyle setFontSize(FontSizeConst fontSizeConst) {
        this.fontSizeConst = fontSizeConst;
        return this;
    }

    public CStyle setFontSize(Integer fontSize, FontSizeDim fontSizeDim) {
        if (fontSize != null && fontSizeDim == null) {
            throw new IllegalArgumentException("Font size dimension can't be null");
        }
        if (fontSize != null && fontSize < 0) {
            throw new IllegalArgumentException("Font size can't be negative");
        }
        this.fontSize = fontSize;
        this.fontSizeDim = fontSizeDim;
        return this;
    }

    public CStyle setColor(ColorConst color) {
        setColor(color.getCode());
        return this;
    }

    public CStyle setColor(Integer colorCode) {
        this.color = colorCode;
        return this;
    }

    public CStyle setColor(int r, int g, int b) {
        setColor(rgbToCode(r, g, b));
        return this;
    }

    public CStyle setBackgroundColor(ColorConst color) {
        setBackgroundColor(color.getCode());
        alpha = 0;
        return this;
    }

    public CStyle setBackgroundColor(ColorConst color, float alpha) {
        this.alpha = alpha;
        this.backgroundColor = color.getCode();
        return this;
    }

    private String getRgbaColor() {

        StringBuilder buf = new StringBuilder();
        buf.append("rgba(")
                .append(backgroundColor >> 16)
                .append(",")
                .append(backgroundColor >> 8 & 0x0000ff)
                .append(",")
                .append(backgroundColor & 0x0000ff).append(",")
                .append(alpha).append(")");
        return buf.toString();

    }

    public CStyle setBackgroundColor(int r, int g, int b) {
        setBackgroundColor(rgbToCode(r, g, b));
        return this;
    }

    public CStyle setBackgroundColor(Integer colorCode) {
        this.alpha = 0;
        this.backgroundColor = colorCode;
        return this;
    }

    public CStyle setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public FontWeight getFontWeight() {
        return fontWeight;
    }

    public FontSizeConst getFontSizeConst() {
        return fontSizeConst;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public FontSizeDim getFontSizeDim() {
        return fontSizeDim;
    }

    public Integer getColor() {
        return color;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public List<String> getCssStyles() {
        return new CssStylesBuilder().get();
    }

    public TextAlign getTextAlign() {
        return textAlign;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public TextDecorationLine getTextDecorationLine() {
        return textDecorationLine;
    }

    public CStyle setTextDecorationLine(TextDecorationLine textDecorationLine) {
        this.textDecorationLine = textDecorationLine;
        return this;
    }

    private int rgbToCode(int r, int g, int b) {
        checkByte(r, "RED");
        checkByte(g, "GREEN");
        checkByte(b, "BLUE");
        int res = 0;
        res |= r;
        res <<= 8;
        res |= g;
        res <<= 8;
        res |= b;
        return res;
    }

    private void checkByte(int i, String name) {
        if (i > 255 || i < 0) {
            throw new IllegalArgumentException("Invalid value " + i + " of " + name + " code. Only byte range [0-255] is allowed.");
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.fontWeight);
        hash = 31 * hash + Objects.hashCode(this.fontSizeConst);
        hash = 31 * hash + Objects.hashCode(this.fontSize);
        hash = 31 * hash + Objects.hashCode(this.fontSizeDim);
        hash = 31 * hash + Objects.hashCode(this.color);
        hash = 31 * hash + Objects.hashCode(this.backgroundColor);
        hash = 31 * hash + Float.floatToIntBits(this.alpha);
        hash = 31 * hash + Objects.hashCode(this.textAlign);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CStyle other = (CStyle) obj;
        if (Float.floatToIntBits(this.alpha) != Float.floatToIntBits(other.alpha)) {
            return false;
        }
        if (this.fontWeight != other.fontWeight) {
            return false;
        }
        if (this.fontSizeConst != other.fontSizeConst) {
            return false;
        }
        if (!Objects.equals(this.fontSize, other.fontSize)) {
            return false;
        }
        if (this.fontSizeDim != other.fontSizeDim) {
            return false;
        }
        if (!Objects.equals(this.color, other.color)) {
            return false;
        }
        if (!Objects.equals(this.backgroundColor, other.backgroundColor)) {
            return false;
        }
        if (this.textAlign != other.textAlign) {
            return false;
        }
        return true;
    }

    
    
    private class CssStylesBuilder {

        private List<String> res = new ArrayList<String>();

        public List<String> get() {
            if (fontWeight != null) {
                add("font-weight", fontWeight.getCss());
            }
            if (fontSizeConst != null) {
                add("font-size", fontSizeConst.name());
            } else if (fontSize != null && fontSizeDim != null) {
                add("font-size", fontSize.toString(), fontSizeDim.name());
            }
            addColor("color", color);

            if (alpha != 0) {
                add("background", getRgbaColor());
            } else {
                addColor("background", backgroundColor);
            }

            if (textAlign != null) {
                add("text-align", textAlign.name());
            }
            
            if(textDecorationLine != null){
                add("text-decoration-line", textDecorationLine == TextDecorationLine.lineThrough ? "line-through" : textDecorationLine.name());
            }

            return res;
        }

        private void addColor(String name, Integer val) {
            if (val != null) {
                String code = Integer.toHexString(val);
                final int len = 6;
                if (code.length() < len) {
                    int addLen = len - code.length();
                    StringBuilder buf = new StringBuilder();
                    for (int i = 0; i < addLen; i++) {
                        buf.append('0');
                    }
                    buf.append(code);
                    code = buf.toString();
                }
                add(name, "#", code);
            }
        }

        private void add(String name, String val0) {
            add(name, val0, null);
        }

        private void add(String name, String val0, String val1) {
            StringBuilder b = new StringBuilder(name.length() + val0.length() + (val1 != null ? val1.length() : 0) + 1);
            b.append(name).append(':').append(val0);
            if (val1 != null) {
                b.append(val1);
            }
            b.append(';');
            res.add(b.toString());
        }
    
    
    }
}
