package ru.sbsoft.client.utils;

/**
 * Сохраяет позиции строк таблицы в битовых полях.
 * Используется в {@link ru.sbsoft.client.components.grid.MarkScanBuffer}.
 * 
 * @author balandin
 * @since Jan 31, 2013 6:45:49 PM
 */
public class BitCache {

    private final int INT_LENGTH = 32;
    private final int CACHE_PAGE_SIZE = 256000; // ~ 32 Kb
    //
    private final static int[] MASKS = {
        0x80000000, 0x40000000, 0x20000000, 0x10000000,
        0x08000000, 0x04000000, 0x02000000, 0x01000000,
        0x00800000, 0x00400000, 0x00200000, 0x00100000,
        0x00080000, 0x00040000, 0x00020000, 0x00010000,
        0x00008000, 0x00004000, 0x00002000, 0x00001000,
        0x00000800, 0x00000400, 0x00000200, 0x00000100,
        0x00000080, 0x00000040, 0x00000020, 0x00000010,
        0x00000008, 0x00000004, 0x00000002, 0x00000001};
    //
    private final int[][] cache;
    private final int size;
    private int markedCount = 0;

    public BitCache(int size) {
        this.size = size;
        this.cache = new int[getPage(size) + 1][];
    }

    public int getSize() {
        return size;
    }

    public int getMarkedCount() {
        return markedCount;
    }

    public final boolean get(int index) {
        final int page = getPage(index);
        if (cache[page] == null) {
            return false;
        }
        final int intInPage = index % CACHE_PAGE_SIZE;
        return (cache[page][intInPage / INT_LENGTH] & MASKS[intInPage % 32]) != 0;
    }

    public final void set(int index, boolean value) {
        boolean oldValue = get(index);
        if (value == oldValue) {
            return;
        }

        final int page = getPage(index);
        if (cache[page] == null) {
            cache[page] = new int[CACHE_PAGE_SIZE / 32];
        }

        final int intInPage = index % CACHE_PAGE_SIZE;
        if (value) {
            cache[page][intInPage / 32] |= MASKS[intInPage % 32];
            markedCount++;
        } else {
            cache[page][intInPage / 32] &= ~MASKS[intInPage % 32];
            markedCount--;
        }
    }

    public void invert(int index) {
        set(index, !get(index));
    }

    private int getPage(int index) {
        return (index - 1) / CACHE_PAGE_SIZE;
    }
}
