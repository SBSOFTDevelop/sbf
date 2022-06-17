package ru.sbsoft.shared.kladr;

import java.io.Serializable;

/**
 * @author balandin
 * @since Mar 15, 2013 12:43:08 PM
 */
public class SearchModel implements Serializable {
    
    public static SearchModel NOT_READY = new SearchModel(-1, "КЛАДР не загружен");
    public static SearchModel TERMINATOR = new SearchModel(-1, "...");
    public static SearchModel ERROR = new SearchModel(-1, "Ошибка поиска");

    private int scoreDoc;
    private int errors = 0;
    private String address;

    public SearchModel() {
    }

    public int getScoreDoc() {
        return scoreDoc;
    }

    public void setScoreDoc(int scoreDoc) {
        this.scoreDoc = scoreDoc;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SearchModel(Integer scoreDoc, String address) {
        this.scoreDoc = scoreDoc;
        this.address = address;
    }
}
