package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import ru.sbsoft.shared.kladr.KLADRAddressDict;
import ru.sbsoft.shared.kladr.KLADRItem;
import ru.sbsoft.shared.kladr.SearchModel;

/**
 * Сервис для работы с КЛАДР
 */
@RemoteServiceRelativePath(ServiceConst.KLADR_SERVICE_SHORT)
public interface IKLADRService extends SBFRemoteService {
    
    public List<SearchModel> search(String query, boolean actualStrict);

    public KLADRAddressDict address(int scoreDoc);

    public List<KLADRItem> lookup(String[] codes, boolean actualStrict);

    public String postcode(String[] codes, String house, String building);
}
