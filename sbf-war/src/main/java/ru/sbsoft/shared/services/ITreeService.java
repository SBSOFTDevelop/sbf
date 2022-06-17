package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.sbsoft.shared.TreeContext;

/**
 * Возвращает иерархическую структуру оъектов заданного типа.
 *
 * @see TreeContext#getTreeType()
 */
@RemoteServiceRelativePath(ServiceConst.TREE_SERVICE_SHORT)
public interface ITreeService extends ITreeServiceBase {
}
