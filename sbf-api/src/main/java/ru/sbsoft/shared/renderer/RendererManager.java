package ru.sbsoft.shared.renderer;

import java.util.HashMap;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.consts.Formats;

/**
 * @see Renderer
 * @author balandin
 * @since May 12, 2014 4:43:34 PM
 */
public class RendererManager implements Formats {

    private final HashMap<String, Renderer> tmp = new HashMap<String, Renderer>();

    public RendererManager() {
    }
    
    public void init() {
        registrRenderer();
    }

    protected void registrRenderer() {
        registr(BOOL_Y, new BooleanRenderer(SBFGeneralStr.rendYes, SBFGeneralStr.rendEmpty));
        registr(BOOL_YN, new BooleanRenderer(SBFGeneralStr.rendYes, SBFGeneralStr.rendNo));
        registr(BOOL_PM, new BooleanRenderer("+", "-"));
        registr(BOOL_GENDER, new BooleanRenderer(SBFGeneralStr.rendFemale, SBFGeneralStr.rendMale));
        //
        registr(MONTHS, new MonthRenderer());
        registr(RESIDENT, new ResidentRenderer());
    }

    protected void registr(String key, Renderer renderer) {
        if (tmp.containsKey(key)) {
            throw new IllegalArgumentException("duplicate keys");
        }
        tmp.put(key, renderer);
    }

    public Renderer getRenderer(String key) {
        return tmp.get(key);
    }
}
