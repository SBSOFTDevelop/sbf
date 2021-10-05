package ru.sbsoft.system.dao.common.utils;

/**
 * Тип сохраненного объекта
 *
 * @author balandin
 * @since Aug 6, 2015
 */
public enum StorageObjectType {

    COLUMN('C', StorageObjectKind.COLUMN),
    SYSTEM_FILTER('X', StorageObjectKind.FILTER),
    USER_FILTER('F', StorageObjectKind.FILTER),
    CUSTOM_STATE('S', StorageObjectKind.STATE);
    //
    private final char discriminator;
    private final StorageObjectKind kind;

    private StorageObjectType(char discriminator, StorageObjectKind kind) {
        this.discriminator = discriminator;
        this.kind = kind;
    }

    public StorageObjectKind getKind() {
        return kind;
    }

    public char getDiscriminator() {
        return discriminator;
    }

    public static StorageObjectType[] getTypes(StorageObjectKind kindFrom) {
        int sz = 0;
        for (StorageObjectType t : StorageObjectType.values()) {
            if (t.getKind() == kindFrom) {
                sz++;
            }
        }
        StorageObjectType[] res = new StorageObjectType[sz];
        if (sz > 0) {
            int k = 0;
            for (StorageObjectType t : StorageObjectType.values()) {
                if (t.getKind() == kindFrom) {
                    res[k++] = t;
                }
            }
        }
        return res;
    }
}
