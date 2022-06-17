package ru.sbsoft.client.consts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.services.*;

/**
 * Системные сервисы.
 */
public class SBFConst {

    public final static StoredFilterPath DEFAULT_FILTER_PATH = null;

    public final static ISecurityServiceAsync SECURUTY_SERVICE = GWT.create(ISecurityService.class);
    public final static IDBStructServiceAsync DB_STRUCT_SERVICE = GWT.create(IDBStructService.class);
    public final static IGridServiceAsync GRID_SERVICE = GWT.create(IGridService.class);
    public final static IGridListGridServiceAsync GRIDLIST_SERVICE = GWT.create(IGridListGridService.class);
    public final static IFormServiceAsync FORM_SERVICE = GWT.create(IFormService.class);
    public final static ITreeServiceAsync TREE_SERVICE = GWT.create(ITreeService.class);
    public final static IMutableTreeServiceAsync MUTABLE_TREE_SERVICE = GWT.create(IMutableTreeService.class);
    public final static IKLADRServiceAsync KLADR_SERVICE = GWT.create(IKLADRService.class);
    public final static IServiceServiceAsync SERVICE_SERVICE = GWT.create(IServiceService.class);
    public final static IConfigServiceAsync CONFIG_SERVICE = GWT.create(IConfigService.class);
    public final static IMultiOpearationServiceAsync MULTI_OPERATION_SERVICE = GWT.create(IMultiOpearationService.class);
    public final static IEMailServiceAsync EMAIL_SERVICE = GWT.create(IEMailService.class);
    public final static Ii18nServiceAsync I18N_SERVICE = GWT.create(Ii18nService.class);
    public final static IEditableGridUpdateAsync EDITABLE_GRID_UPDATE_SERVICE = GWT.create(IEditableGridUpdate.class);

    static {
        ((ServiceDefTarget) SECURUTY_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.SEC_SERVICE_LONG);
        ((ServiceDefTarget) DB_STRUCT_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.DB_STRUCT_SERVICE_LONG);
        ((ServiceDefTarget) GRID_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.GRID_SERVICE_LONG);
        ((ServiceDefTarget) GRIDLIST_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.GRIDLIST_SERVICE_LONG);
        ((ServiceDefTarget) FORM_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.FORM_SERVICE_LONG);
        ((ServiceDefTarget) TREE_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.TREE_SERVICE_LONG);
        ((ServiceDefTarget) MUTABLE_TREE_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.MUTABLE_TREE_SERVICE_LONG);
        ((ServiceDefTarget) KLADR_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.KLADR_SERVICE_LONG);
        ((ServiceDefTarget) SERVICE_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.SERVICE_SERVICE_LONG);
        ((ServiceDefTarget) CONFIG_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.CONFIG_SERVICE_LONG);
        ((ServiceDefTarget) MULTI_OPERATION_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.MULTI_OPERATION_SERVICE_LONG);
        ((ServiceDefTarget) EMAIL_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.EMAIL_SERVICE_LONG);
        ((ServiceDefTarget) I18N_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.I18N_SERVICE_LONG);
        ((ServiceDefTarget) EDITABLE_GRID_UPDATE_SERVICE).setServiceEntryPoint(getRoot() + ServiceConst.EDITABLE_GRID_UPDATE_SERVICE_LONG);
    }

    private static String getRoot() {
        String url = GWT.getModuleBaseURL();
        int index = url.lastIndexOf("/" + GWT.getModuleName());
        return (index == -1)
                ? url
                : url.substring(0, index);
    }
}
