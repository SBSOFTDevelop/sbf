package ru.sbsoft.shared.model;

import java.util.Objects;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author sokolov
 */
public class CustomReportFilterInfo implements DTO {

    private String code;
    private ILocalizedString description;

    public CustomReportFilterInfo() {
    }

    public CustomReportFilterInfo(String code, ILocalizedString description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ILocalizedString getDescription() {
        return description;
    }

    public void setDescription(ILocalizedString description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomReportFilterInfo)) {
            return false;
        }
        CustomReportFilterInfo other = (CustomReportFilterInfo)obj;
        return code.equals(other.code) || description.equals(other.description);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.code);
        hash = 47 * hash + Objects.hashCode(this.description);
        return hash;
    }

}
