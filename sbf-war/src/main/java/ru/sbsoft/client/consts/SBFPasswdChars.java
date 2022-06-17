/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sbsoft.client.consts;

/**
 *
 * @author sychugin
 */
public enum SBFPasswdChars {

    CASE_SENSITIVE_CHARS("a-z, A-Z, a-я, А-Я", "^(?=(.*[a-z,а-я]){1})(?=(.*[A-Z,А-Я]){1})"),
    SPECIAL_CHARS("0-9, !@#$%^&*()_+|~-=\\`{}[]:\";'<>?,./)", 
            "^(?=(.*[0-9]|!|@|#|\\$|%|&|=|%|\\^|&|\\*|_|\\+|~|-|=|\\|`|{|}|\\[|]|:|\"|;|'|<|>|\\?|,|\\.|\\/|\\||\\)|\\(){1})");
//
    private final String title;
    private final String regExp;

    public String getRegExp() {
        return regExp;
    }

    
    public String getTitle() {
        return title;
    }

    SBFPasswdChars(String title, String regExp) {
        this.title = title;
        this.regExp = regExp;
    }

}
