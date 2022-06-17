package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.shared.param.DTO;

/**
 * @see FSSRemoteService
 * @author balandin
 * @since Feb 26, 2015 8:03:03 PM
 */
public interface ISBFServiceAsync {

    void dummy(List<DTO> dto, AsyncCallback<List<DTO>> callback);
}
