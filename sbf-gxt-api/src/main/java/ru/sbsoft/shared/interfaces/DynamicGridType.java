package ru.sbsoft.shared.interfaces;

import java.math.BigDecimal;
import java.util.Objects;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 *
 * @author sokolov
 */
public final class DynamicGridType implements IDynamicGridType {

    private ObjectType group;
    private BigDecimal id;
    private ILocalizedString itemName;
    private String securityId;

    public DynamicGridType(ObjectType group, BigDecimal id, String itemName) {
        this(group, group instanceof GroupSecurityItem ? (GroupSecurityItem)group : (GroupSecurityItem)(group::getCode), id, itemName);
    }

    public DynamicGridType(SecureDynamicType group, BigDecimal id, String itemName) {
        this(group, group, id, itemName);
    }

    private DynamicGridType(ObjectType group, final GroupSecurityItem groupSecurity, BigDecimal id, String itemName) {
        this(group, (SecurityItem)(() -> extendCode(groupSecurity.getGroupSecurityId(), id)), id, itemName);
    }
    
    public DynamicGridType(ObjectType group, SecurityItem securityItem, BigDecimal id, String itemName) {
        this.group = group;
        this.securityId = securityItem.getSecurityId();
        this.itemName = new NonLocalizedString(itemName);
        this.id = id;
    }

    private DynamicGridType() {
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public String getCode() {
        return extendCode(getGroupCode(), id);
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
    public String getGroupCode() {
        return group.getCode();
    }

    private static String extendCode(String groupCode, BigDecimal id) {
        return groupCode + (id == null ? "" : id.longValue());
    }

    @Override
    public int hashCode() {
        int hash = 77;
        hash = 85 * hash + Objects.hashCode(this.getGroupCode());
        hash = 85 * hash + Objects.hashCode(this.id);
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
        return Objects.equals(this.id, other.getId()) && Objects.equals(this.getGroupCode(), other.getGroupCode());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' + "group=" + group + ", security=" + securityId + ", itemName=" + itemName + ", id=" + id + '}';
    }
}
