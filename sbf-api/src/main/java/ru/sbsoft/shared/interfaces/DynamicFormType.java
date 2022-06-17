package ru.sbsoft.shared.interfaces;

import java.math.BigDecimal;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 *
 * @author sokolov
 */
public class DynamicFormType implements IDynamicFormType {

    private ObjectType group;
    private BigDecimal id;
    private ILocalizedString itemName;
    private String securityId;

    public DynamicFormType(ObjectType group, BigDecimal id, String itemName) {
        this(group, id, itemName, () -> extendCode(group.getCode(), id));
    }

    public DynamicFormType(ObjectType group, BigDecimal id, String itemName, SecurityItem secItem) {
        this.group = group;
        this.id = id;
        this.itemName = new NonLocalizedString(itemName);
        this.securityId = secItem.getSecurityId();
    }

    private DynamicFormType() {
    }

    @Override
    public String getGroupCode() {
        return group.getCode();
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public String getRights() {
        return getSecurityId();
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

    private static String extendCode(String groupCode, BigDecimal id) {
        return groupCode + (id == null ? "" : id.longValue());
    }
}
