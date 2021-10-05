package ru.sbsoft.shared.model;

import java.util.List;

/**
 *
 * @author vk
 */
public interface IFileLinksModel {

    List<LookupInfoModel> getFileLnks();

    void setFileLnks(List<LookupInfoModel> fileLnks);
}
