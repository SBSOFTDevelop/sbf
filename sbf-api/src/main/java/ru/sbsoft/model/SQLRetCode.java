package ru.sbsoft.model;

import java.util.Objects;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author sychugin
 */
public class SQLRetCode implements DTO {

    private final String SQLstate;
    private final int ErrorCode;

    public SQLRetCode(String SQLstate, int ErrorCode) {
        this.SQLstate = SQLstate;
        this.ErrorCode = ErrorCode;
    }

    @Override
    public int hashCode() {
        return (SQLstate == null) ? ErrorCode : SQLstate.hashCode() + ErrorCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SQLRetCode other = (SQLRetCode) obj;
        if (!Objects.equals(this.SQLstate, other.SQLstate)) {
            return false;
        }
        return this.ErrorCode == other.ErrorCode;
    }

    @Override
    public String toString() {
        return "SQLstate:" + SQLstate + ", ErrorCode:" + ErrorCode;

    }

}
