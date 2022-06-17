package ru.sbsoft.client.components.browser.filter.fields;

/**
 *
 * @author Kiselev
 */
public class HoldersListFactory extends AbstractHoldersListFactory<HoldersList> {

    @Override
    protected HoldersList createInstance() {
        return new HoldersList();
    }

}
