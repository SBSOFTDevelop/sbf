package ru.sbsoft.client.components.grid;

import ru.sbsoft.svc.data.shared.SortInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import ru.sbsoft.client.utils.BitCache;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.model.MarkModel;

/**
 * Обеспечивает навигацию по таблице с использованием отмеченных записей.
 * Позволяет выполнять поиск записи для навигации даже если ее нет на текущей странице таблицы.
 * @author balandin
 * @since Feb 1, 2013 3:01:12 PM
 */
public class MarkScanBuffer {

    public static final int SCAN_ROWS_LIMIT = 16000;
    private final MarkSet markModel;
    private int initialSize;
    private List<SortInfo> sortInfo = new ArrayList<SortInfo>();
    private BitCache cache;
    private List<Range> intervals;
    private boolean scanForward = true;
    private int scanIndex = -1;

    public MarkScanBuffer(MarkSet markModel) {
        this.markModel = markModel;
    }

    public boolean isScanForward() {
        return scanForward;
    }

    public int getScanIndex() {
        return scanIndex;
    }

    public boolean find(boolean forward, int index) {
        this.scanForward = forward;
        this.scanIndex = index;
        return find();
    }

    public boolean find() {
        if (scanIndex != -1 && isMarked(scanIndex)) {
            skip();
        }

        final boolean allMarkedLoaded = (cache.getMarkedCount() == markModel.size());
        for (int i = 0; i < initialSize; i++) {
            final Range interval = getInterval(scanIndex);
            if (interval == null && !allMarkedLoaded) {
                return false;
            }
            if (isMarked(scanIndex)) {
                return true;
            }
            skip();
        }
        throw new IllegalStateException("scan error");
    }

    private void skip() {
        if (scanForward) {
            scanIndex++;
            if (scanIndex >= initialSize) {
                scanIndex = 0;
            }
        } else {
            scanIndex--;
            if (scanIndex < 0) {
                scanIndex = initialSize - 1;
            }
        }
    }

    public void addInterval(SBLiveGridView<? extends MarkModel> view) {
        if (caching()) {
            final int start = view.getLiveStoreOffset();
            final int size = view.getCacheStore().size();
            for (int i = 0; i < size; i++) {
                setMarked(start + i, markModel.isMarked(view.getCacheStore().get(i)));
            }
            addInterval(new Range(start, start + size - 1));
        }
    }

    public void addInterval(int start, List<BigDecimal> values) {
        for (int i = 0; i < values.size(); i++) {
            setMarked(start + i, markModel.contains(values.get(i)));
        }
        addInterval(new Range(start, start + values.size() - 1));
    }

    private void addInterval(Range newInterval) {
        List<Range> tmp = null;
        for (Range range : intervals) {
            if (newInterval.contains(range)) {
                if (tmp == null) {
                    tmp = new ArrayList<Range>();
                }
                tmp.add(range);
            }
        }

        if (tmp != null) {
            for (Range range : tmp) {
                intervals.remove(range);
            }
        }

        for (Range range : intervals) {
            if (range.contains(newInterval)) {
                return;
            }
        }

        Range border = getInterval(newInterval.start - 1);
        if (border != null) {
            newInterval.start = border.start;
            intervals.remove(border);
        }

        border = getInterval(newInterval.end + 1);
        if (border != null) {
            newInterval.end = border.end;
            intervals.remove(border);
        }

        intervals.add(newInterval);
    }

    private Range getInterval(int index) {
        for (Range i : intervals) {
            if (i.contains(index)) {
                return i;
            }
        }
        return null;
    }

    private void setMarked(int index, boolean value) {
        cache.set(index, value);
    }

    private boolean isMarked(int index) {
        return cache.get(index);
    }

    public void startCaching(int size, List<SortInfo> initialSortInfo) {
        if (!caching()) {
            init(size, initialSortInfo);
        } else {
            if (initialSize != size || !equal(sortInfo, initialSortInfo)) {
                init(size, initialSortInfo);
            }
        }
    }

    public void setSortInfo(List<SortInfo> sortInfoOther) {
        sortInfo.clear();
        sortInfo.addAll(sortInfoOther);
    }

    private boolean equal(List<SortInfo> sortInfo1, List<SortInfo> sortInfo2) {
        if (sortInfo1 == null && sortInfo2 == null) {
            return true;
        }
        if (sortInfo1 == null || sortInfo2 == null) {
            return false;
        }
        if (sortInfo1.size() != sortInfo2.size()) {
            return false;
        }
        for (int i = 0; i < sortInfo1.size(); i++) {
            final SortInfo s1 = sortInfo1.get(i);
            final SortInfo s2 = sortInfo2.get(i);
            if (s1.getSortDir() != s2.getSortDir()) {
                return false;
            }
            if (!Strings.equals(s1.getSortField(), s2.getSortField())) {
                return false;
            }
        }
        return true;
    }

    private void init(int size, List<SortInfo> sortInfo) {
        setSortInfo(sortInfo);
        initialSize = size;
        cache = new BitCache(size);
        intervals = new ArrayList<Range>();
        scanIndex = -1;
    }

    public void stopCaching(boolean clearMark) {
        if (caching()) {
            initialSize = -1;
            cache = null;
            intervals = null;
        }
        if (clearMark) {
            markModel.clear();
        }
    }

    public int getScanOffset() {
        int offset = scanIndex;
        if (!scanForward) {
            offset -= SCAN_ROWS_LIMIT;
        }
        return Math.max(offset, 0);
    }

    public void inverse(List<BigDecimal> result) {
        if (null == result) {
            return;
        }

        final HashSet<BigDecimal> newMarkModel = new HashSet<BigDecimal>();
        for (final BigDecimal value : result) {
            if (!markModel.contains(value)) {
                newMarkModel.add(value);
            }
        }

        stopCaching(true);

        markModel.reload(newMarkModel);
    }

    public void inverse(int index) {
        if (caching()) {
            cache.invert(index);
        }
    }

    private boolean caching() {
        return cache != null;
    }

    public static class Range {

        private int start;
        private int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        private boolean contains(Range other) {
            return start <= other.start && other.end <= end;
        }

        private boolean contains(int value) {
            return start <= value && value <= end;
        }

        @Override
        public String toString() {
            return "interval [" + start + ", " + end + "]";
        }
    }
}
