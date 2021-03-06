package ru.sbsoft.client.components;

import com.google.gwt.editor.client.Editor;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
import java.math.BigDecimal;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 */
public interface ILookupInfoModelAccessor extends PropertyAccess<LookupInfoModel> {

    @Editor.Path("ID")
    ModelKeyProvider<LookupInfoModel> key();

    @Editor.Path("semanticName")
    LabelProvider<LookupInfoModel> label();

    ValueProvider<LookupInfoModel, BigDecimal> ID();

    ValueProvider<LookupInfoModel, BigDecimal> semanticID();

    ValueProvider<LookupInfoModel, String> semanticKey();

    ValueProvider<LookupInfoModel, String> semanticName();

    ValueProvider<LookupInfoModel, String> semanticKeyAndName();
    
}
