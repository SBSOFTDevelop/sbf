package ru.sbsoft.system.dao.common.json;

import java.math.BigDecimal;
import java.util.Objects;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.interfaces.IDynamicGridType;

/**
 *
 * @author vk
 */
public class PlainDynamicGridType implements IDynamicGridType {

    private String groupCode;
    private BigDecimal id;
    private ILocalizedString itemName;
    private String securityId;
    private String code;

    public PlainDynamicGridType(String groupCode, BigDecimal id, String code, ILocalizedString itemName, String securityId) {
        this.groupCode = groupCode;
        this.id = id;
        this.code = code;
        this.itemName = itemName;
        this.securityId = securityId;
    }

    private PlainDynamicGridType() {
    }

    @Override
    public String getGroupCode() {
        return groupCode;
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }

    @Override
    public String getSecurityId() {
        return securityId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.groupCode);
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof IDynamicGridType)) {
            return false;
        }
        final IDynamicGridType other = (IDynamicGridType) obj;
        return Objects.equals(this.id, other.getId()) && Objects.equals(this.groupCode, other.getGroupCode());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' + "groupCode=" + groupCode + ", id=" + id + ", code=" + code + ", itemName=" + itemName + ", securityId=" + securityId + '}';
    }
}
