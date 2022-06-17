package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.shared.kladr.KLADRAddressDict;
import ru.sbsoft.shared.kladr.KLADRItem;
import ru.sbsoft.shared.kladr.SearchModel;

/**
 * @see IKLADRService
 */
public interface IKLADRServiceAsync extends ISBFServiceAsync {

    void search(String query, boolean actualStrict, AsyncCallback<List<SearchModel>> callback);

    void address(int scoreDoc, AsyncCallback<KLADRAddressDict> callback);

    void lookup(String[] codes, boolean actualStrict, AsyncCallback<List<KLADRItem>> callback);

    void postcode(String[] codes, String house, String building, AsyncCallback<String> callback);

}
