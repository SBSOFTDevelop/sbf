package ru.sbsoft.client.utils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.FormGridView;
import ru.sbsoft.client.schedule.i18n.SBFi18nLocale;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.model.YearMonth;

/**
 *
 * @author Kiselev
 */
public class CliUtil {

    private static final FilterInfoSortComparator FILTER_INFO_SORT_COMPARATOR = new FilterInfoSortComparator();
    
    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static String getSimpleName(Object o) {
        return o != null ? getSimpleName(o.getClass()) : "null";
    }

    public static String getSimpleName(Class clazz) {
        if (clazz != null) {
            String name = clazz.getName();
            int p1 = name.lastIndexOf('.');
            return p1 >= 0 ? name.substring(p1 + 1) : name;
        }
        return "null";
    }

    public static String format(final String format, final Object... args) {
        String[] split = format.split("\\{\\}");
        final StringBuffer msg = new StringBuffer();
        for (int pos = 0; pos < split.length - 1; pos += 1) {
            msg.append(split[pos]);
            msg.append(String.valueOf(args[pos]));
        }
        msg.append(split[split.length - 1]);
        return msg.toString();
    }

    public static int getLastMonthDay(YearMonth ym) {
        return getLastMonthDay(ym.getYear(), ym.getMonth());
    }

    public static int getLastMonthDay(int month, int year) {
        if (year <= 0) {
            throw new ApplicationException(SBFExceptionStr.yearLess, "0");
        }
        if (month < 1 || month > 12) {
            throw new ApplicationException(SBFExceptionStr.monthRange, "1", "12");
        }
        int days = DAYS_IN_MONTH[month - 1];
        if (month == 2 && ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)) {
            days++;
        }
        return days;
    }

    public static int getLastMonthDay(Date d) {
        int month = Integer.valueOf(DateTimeFormat.getFormat("M").format(d));
        int year = Integer.valueOf(DateTimeFormat.getFormat("y").format(d));
        return getLastMonthDay(month, year);
    }

    public static String toDateStr(Date d) {
        return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(d);
    }

    public static boolean isLastMonthDay(Date d) {
        if (d == null) {
            return false;
        }
        int day = Integer.valueOf(DateTimeFormat.getFormat("d").format(d));
        return day == getLastMonthDay(d);
    }

    public static boolean isFirstMonthDay(Date d) {
        if (d == null) {
            return false;
        }
        int day = Integer.valueOf(DateTimeFormat.getFormat("d").format(d));
        return day == 1;
    }

    public static int getNowYear() {
        return Integer.valueOf(DateTimeFormat.getFormat("yyyy").format(new Date()));
    }

    public static Date createDate(int y, int m, int d) {
        StringBuilder buf = new StringBuilder();
        buf.append(y).append('.').append(m).append('.').append(d);
        return DateTimeFormat.getFormat("yyyy.MM.dd").parse(buf.toString());
    }

    public static int getDay(Date d) {
        return Integer.valueOf(DateTimeFormat.getFormat("d").format(d));
    }

    public static int getMonth(Date d) {
        return Integer.valueOf(DateTimeFormat.getFormat("M").format(d));
    }

    public static int getYear(Date d) {
        return Integer.valueOf(DateTimeFormat.getFormat("y").format(d));
    }

    public static YearMonth getYearMonth(Date d) {
        return d != null ? new YearMonth(getYear(d), getMonth(d)) : null;
    }

    public static Date toDate(YearMonth ym) {
        return ym != null ? createDate(ym.getYear(), ym.getMonth(), 1) : null;
    }

    public static void refreshWithParentGrid(BaseForm f) {
        f.refresh();
        if (f.getRecordUQ() != null && f.getOwnerGrid() != null) {
            f.getOwnerGrid().refreshRow(f.getRecordUQ());
        }
    }

    public static boolean instanceOf(Object o, Class baseClazz) {
        if (baseClazz == null || o == null) {
            return false;
        }
        for (Class clazz = o.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            if (baseClazz.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasText(SafeHtml h) {
        if (h != null) {
            String s = h.asString();
            int tagCount = 0;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if ('<' == c) {
                    tagCount++;
                } else if ('>' == c) {
                    tagCount--;
                } else if (tagCount == 0 && !Character.isWhitespace(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void updateHeaderVisibility(final ContentPanel panel) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                SafeHtml h = panel.getHeading();
                boolean inTab = isInTab(panel);
                boolean hasText = CliUtil.hasText(h);
                boolean shouldVisible = hasText && !inTab;
                boolean changed = panel.getHeader().isVisible() ^ shouldVisible;
                panel.setHeaderVisible(shouldVisible);
                if (changed) {
                    panel.getHeader().setVisible(shouldVisible);
                    panel.forceLayout();
                }
            }
        });
    }

    private static boolean isInTab(ContentPanel panel) {
        Widget parent = panel.getParent();
        if (parent instanceof FormGridView) {
            parent = parent.getParent();
        }
        Widget parentParent = parent != null ? parent.getParent() : null;
        return parent instanceof CardLayoutContainer && parentParent instanceof TabPanel;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static String normNull(String s) {
        return s != null && (s = s.trim()).isEmpty() ? null : s;
    }

    public static String normEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    public static void requireClass(Object o, Class clazz, String objectName) {
        if (clazz == null) {
            throw new IllegalArgumentException("Checking class can't be null");
        }
        if (o != null && !clazz.equals(o.getClass())) {
            StringBuilder buf = new StringBuilder();
            buf.append("For '").append(objectName).append("' class must be exactly '").append(clazz.getName()).append("', but '").append(o.getClass().getName()).append("' is found.");
            throw new IllegalArgumentException(buf.toString());
        }
    }

    public static void requireInstanceOf(Object o, Class c, String objectName) {
        requireInstanceOfImpl(o, c, objectName, false);
    }

    public static void requireNotInstanceOf(Object o, Class c, String objectName) {
        requireInstanceOfImpl(o, c, objectName, true);
    }

    private static void requireInstanceOfImpl(Object o, Class c, String objectName, boolean not) {
        if (instanceOf(o, c) == not) {
            StringBuilder buf = new StringBuilder();
            buf.append("'").append(objectName).append("' class must");
            if (not) {
                buf.append(" NOT");
            }
            buf.append(" be instance of '").append(c.getName()).append("'. Its class: '").append(o.getClass().getName()).append("'.");
            throw new IllegalArgumentException(buf.toString());
        }
    }

    //com.google.gwt.dom.client
    public static void sendKey(Component cmp, int keyCode) {
        sendKey(cmp, keyCode, false, false, false, false);
    }

    public static void sendKey(Component cmp, int keyCode, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey) {
        sendKey(cmp.getElement(), keyCode, ctrlKey, altKey, shiftKey, metaKey);
    }

    public static void sendKey(com.google.gwt.dom.client.Element el, int keyCode) {
        sendKey(el, keyCode, false, false, false, false);
    }

    public static void sendKey(com.google.gwt.dom.client.Element el, int keyCode, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey) {
        NativeEvent e = Document.get().createKeyCodeEvent(KeyNav.getKeyEvent() == Event.ONKEYDOWN ? BrowserEvents.KEYDOWN : BrowserEvents.KEYPRESS, ctrlKey, altKey, shiftKey, metaKey, keyCode);
        el.dispatchEvent(e);
    }
    
    public static  String getLang2() {
        String lang = SBFi18nLocale.getLocaleName();
        return lang != null && (lang = lang.trim()).length() > 1 ? lang.substring(0, 2) : null;
    }
    
    public static boolean eq(List<FilterInfo> fs1, List<FilterInfo> fs2) {
        if (fs1 == fs2) {
            return true;
        }
        if (fs1 == null || fs2 == null || fs1.size() != fs2.size()) {
            return false;
        }
        Collections.sort(fs1, FILTER_INFO_SORT_COMPARATOR);
        Collections.sort(fs2, FILTER_INFO_SORT_COMPARATOR);
        for (int i = 0; i < fs1.size(); i++) {
            if (!equals(fs1.get(i), fs2.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private static class FilterInfoSortComparator implements Comparator<FilterInfo> {

        private static final Comparator<FilterInfo>[] FILTER_CMP;

        static {
            List<Comparator<FilterInfo>> cmpBuf = new ArrayList<>();
            cmpBuf.add((o1, o2) -> {
                return compare(o1.getColumnName(), o2.getColumnName());
            });
            cmpBuf.add((o1, o2) -> {
                return compare(o1.getComparison(), o2.getComparison());
            });
            cmpBuf.add((o1, o2) -> {
                return compare(o1.getType(), o2.getType());
            });
            cmpBuf.add((o1, o2) -> {
                Object v1 = o1.getValue();
                Object v2 = o2.getValue();
                Integer oRes = nullCompare(v1, v2);
                if (oRes != null) {
                    return oRes;
                }
                Class c1 = v1.getClass();
                Class c2 = v2.getClass();
                if (c1.equals(c2)) {
                    return (v1 instanceof Comparable) ? ((Comparable) v1).compareTo(v2) : 0;
                } else {
                    return c1.getName().compareTo(c2.getName());
                }
            });
            cmpBuf.add((o1, o2) -> {
                return Boolean.compare(o1.isCaseSensitive(), o2.isCaseSensitive());
            });
            cmpBuf.add((o1, o2) -> {
                return Boolean.compare(o1.isNotExpression(), o2.isNotExpression());
            });
            FILTER_CMP = cmpBuf.toArray(new Comparator[cmpBuf.size()]);
        }

        @Override
        public int compare(FilterInfo o1, FilterInfo o2) {
            Integer oRes = nullCompare(o1, o2);
            if (oRes != null) {
                return oRes;
            }
            for (Comparator<FilterInfo> f : FILTER_CMP) {
                int res = f.compare(o1, o2);
                if (res != 0) {
                    return res;
                }
            }
            return 0;
        }

        private static <T extends Comparable<T>> int compare(T c1, T c2) {
            Integer oRes = nullCompare(c1, c2);
            if (oRes != null) {
                return oRes;
            }
            return c1.compareTo(c2);
        }

        private static Integer nullCompare(Object o1, Object o2) {
            if (o1 == o2) {
                return 0;
            }
            if (o1 == null) {
                return 1;
            }
            if (o2 == null) {
                return -1;
            }
            return null;
        }
    }
}
