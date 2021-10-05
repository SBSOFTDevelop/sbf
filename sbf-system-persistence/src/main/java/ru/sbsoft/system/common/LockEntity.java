package ru.sbsoft.system.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import static ru.sbsoft.system.common.LockEntity.*;

@Entity
@Table(name = "SR_LOCKS")
@NamedQueries({
    @NamedQuery(name = LOCK, query = "select lock from LockEntity lock where lock.DESTINATION_TYPE = :dest and lock.ID = :id")
    ,
    @NamedQuery(name = DELETE, query = "delete from LockEntity lock where lock.DESTINATION_TYPE = :dest and lock.ID = :id")
})
public class LockEntity implements Serializable {

    public static final String LOCK = "LockEntity.LOCK";
    public static final String DELETE = "LockEntity.DELETE";

    @Id
    private BigDecimal ID;
    private String DESTINATION_TYPE;

    public String getDestinationType() {
        return DESTINATION_TYPE;
    }

    public void setDestinationType(String DESTINATION_TYPE) {
        this.DESTINATION_TYPE = DESTINATION_TYPE;
    }

    public BigDecimal getId() {
        return ID;
    }

    public void setId(BigDecimal ID) {
        this.ID = ID;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.DESTINATION_TYPE);
        hash = 53 * hash + Objects.hashCode(this.ID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LockEntity other = (LockEntity) obj;
        if (!Objects.equals(this.DESTINATION_TYPE, other.DESTINATION_TYPE)) {
            return false;
        }
        return Objects.equals(this.ID, other.ID);
    }

}
