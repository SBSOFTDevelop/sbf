package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import java.util.List;
import ru.sbsoft.shared.param.DTO;

/**
 * Базовый сервис для работы с GWT интерфейсом.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface SBFRemoteService extends RemoteService {

    public List<DTO> dummy(List<DTO> dto);
    
}
