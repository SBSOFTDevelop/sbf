package ru.sbsoft.shared.model.user;

import java.util.Date;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author sychugin
 */
public class PasswordPolicy implements DTO {

    private Date dateExpired;
    private int minLength;
    private boolean canChange;
    private boolean expired;
    private boolean caseChar;
    private boolean specChar;

    public final void setPolicy(final int policy) {
//    p_policy = 0bLLLLLLLL00000001 - user can change password 
//    p_policy = 0bLLLLLLLL00000010 - password must contain  chars    a-z, A-Z, a-я, А-Я:
//    p_policy = 0bLLLLLLLL00000100 - password must contain chars 0-9, !@#$%^&*()_+|~-=\`{}[]:";'<>?,./):
//    LLLLLLLL - minimum length of password      
        canChange = (policy & 1) == 1;
        minLength = (policy >> 16) & 0x00ff;
        caseChar = (policy & 2) == 2;
        specChar = (policy & 4) == 4;

    }

    public PasswordPolicy(final int policy) {
        setPolicy(policy);
    }

    public PasswordPolicy() {
        setPolicy(0);
    }

    public static int encodePolicy(int minLength, boolean hasCaseChar, boolean hasSpecChar, boolean isCanChange) {
        return (minLength << 16) | (isCanChange ? 1 : 0) | (hasCaseChar ? 2 : 0) | (hasSpecChar ? 4 : 0);
    }

    public boolean hasCaseChar() {
        return caseChar;
    }

    public boolean hasSpecChar() {
        return specChar;
    }

    public boolean isCanChange() {
        return this.canChange;
    }

    public int getMinLength() {
        return minLength;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean isExpired) {
        this.expired = isExpired;
    }

    public Date getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(Date DateExpired) {
        this.dateExpired = DateExpired;
    }

}
