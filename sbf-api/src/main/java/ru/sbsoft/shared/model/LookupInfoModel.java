package ru.sbsoft.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import ru.sbsoft.common.Defs;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.meta.Row;

/**
 * Описывает структуру данных сущностей для LookupField.
 *
 * @author Sokoloff
 */
public class LookupInfoModel implements Serializable {

    private BigDecimal ID;
    private BigDecimal semanticID;
    private String semanticKey;
    private String semanticName;

    public LookupInfoModel(LookupInfoModel m) {
        ID = m.ID;
        semanticID = m.semanticID;
        semanticKey = m.semanticKey;
        semanticName = m.semanticName;
    }

    public LookupInfoModel() {
    }

    public String getSemanticKeyAndName() {
        return semanticKey == null ? semanticName : Strings.join(".", semanticKey, semanticName);
    }

    public LookupInfoModel(BigDecimal ID, String semanticKey, String semanticName) {
        this.ID = ID;
        this.semanticKey = semanticKey;
        this.semanticName = semanticName;
    }

    public void initialize(Row row) {
    }

    public BigDecimal getID() {
        return ID;
    }

    public void setID(BigDecimal ID) {
        this.ID = ID;
    }

    public BigDecimal getSemanticID() {
        return semanticID;
    }

    public LookupInfoModel setSemanticID(BigDecimal semanticID) {
        this.semanticID = semanticID;
        return this;
    }

    public String getSemanticKey() {
        return semanticKey;
    }

    public void setSemanticKey(String semanticKey) {
        this.semanticKey = semanticKey;
    }

    public String getSemanticName() {
        return semanticName;
    }

    public void setSemanticName(String semanticName) {
        this.semanticName = semanticName;
    }

    @Override
    public String toString() {
        return Strings.join(new Object[]{Defs.coalesce(ID, 0), Defs.coalesce(semanticID, 0), semanticKey, semanticName}, "-");
    }

    public static String listToString(List<LookupInfoModel> models) {
        return models == null ? Strings.EMPTY : Strings.join(models.toArray(), "@@");
    }

    public static boolean isEmpty(final LookupInfoModel model) {
        return model != null && model.getID() != null;
    }

    public boolean isMultiSelectModel() {
        if (semanticKey == null) {
            return true;
        }
        return "...".equals(semanticKey);
    }

    public String getSemanticValue(LookupCellType cellType) {
        if (cellType == LookupCellType.KEY) {
            return semanticKey;
        }
        if (cellType == LookupCellType.NAME) {
            return semanticName;
        }
        throw new IllegalStateException("?");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.ID != null ? this.ID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.ID, ((LookupInfoModel) obj).ID);
    }
}
