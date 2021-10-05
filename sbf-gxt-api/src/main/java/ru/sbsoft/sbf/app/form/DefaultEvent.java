package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <OWNER>
 */
public class DefaultEvent<OWNER> implements IEvent<OWNER> {

    private OWNER owner;

    @Override
    public OWNER getOwner() {
        return owner;
    }

    public void setOwner(OWNER owner) {
        this.owner = owner;
    }

}
