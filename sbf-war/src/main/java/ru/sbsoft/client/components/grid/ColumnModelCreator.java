package ru.sbsoft.client.components.grid;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.util.Rectangle;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;
import ru.sbsoft.svc.widget.core.client.grid.HeaderGroupConfig;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.ColumnGroup;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.util.Node;

import java.util.*;

/**
 * @author balandin
 * @since Oct 2, 2014 2:02:23 PM
 */
class ColumnModelCreator {

    static ColumnModel<Row> create(List<ColumnConfig> csrc) {
        final List<ColumnConfig> cdst = new ArrayList<ColumnConfig>(csrc.size());

        LinkedList<ColumnGroup> groups = new LinkedList<ColumnGroup>();
        Map<ColumnGroup, Rectangle> processed = new HashMap<ColumnGroup, Rectangle>();
        while (csrc.size() > 0) {

            ColumnConfig column;
            if (groups.isEmpty()) {
                column = csrc.get(0);
            } else {
                column = findFirstColumnInGroup(csrc, groups.getLast());
                if (column == null) {
                    groups.removeLast();
                    continue;
                }
            }

            if (column instanceof CustomColumnConfig) {
                ColumnGroup group = ((CustomColumnConfig) column).getColumn().getGroup();
                if (group != null) {
                    List<ColumnGroup> tmp = new ArrayList<ColumnGroup>();
                    do {
                        tmp.add(group);
                    } while ((group = group.getParent()) != null);
                    Collections.reverse(tmp);

                    for (ColumnGroup g : tmp) {
                        if (!processed.containsKey(g)) {
                            int row = 0;
                            for (ColumnGroup gIter : groups) {
                                row += gIter.getRows();
                            }
                            groups.addLast(g);
                            processed.put(g, new Rectangle(cdst.size(), row, 0, g.getRows()));
                        }
                    }
                }
            }

            cdst.add(column);
            csrc.remove(column);

            for (ColumnGroup g : groups) {
                final Rectangle r = processed.get(g);
                r.setWidth(r.getWidth() + 1);
            }
        }

        GroupTree groupTree = new GroupTree(processed);
        int groupRowCount = groupTree.getTotalRowCount();
        for (Node<ColumnGroup> n : groupTree.getLeafs()) {
            ColumnGroup g = n.getData();
            Rectangle rt = processed.get(g);
            int rrr = rt.getY() + rt.getHeight();
            if (rrr < groupRowCount) {
                rt.setHeight(groupRowCount - rt.getY());
            }
        }

        final ColumnModel r = new ColumnModel(cdst);
        for (ColumnGroup group : processed.keySet()) {
            final Rectangle rect = processed.get(group);
            r.addHeaderGroup(rect.getY(), rect.getX(), new HeaderGroupConfig(wrapString(I18n.get(group.getTitle())), rect.getHeight(), rect.getWidth()));
        }
        return r;
    }

    private static class GroupTree {

        private final Map<ColumnGroup, Rectangle> processed;
        private final List<Node<ColumnGroup>> roots = new ArrayList<>();

        public GroupTree(Map<ColumnGroup, Rectangle> processed) {
            this.processed = processed;
            if (!processed.isEmpty()) {
                Deque<ColumnGroup> stack = new ArrayDeque<>(processed.keySet());
                while (!stack.isEmpty()) {
                    ColumnGroup g = stack.poll();
                    if (g != null && findNode(g) == null) {
                        if (g.getParent() == null) {
                            roots.add(new Node(g));
                        } else {
                            Node<ColumnGroup> pn = findNode(g.getParent());
                            if (pn != null) {
                                pn.addChild(new Node(g));
                            } else {
                                ColumnGroup pb = g;
                                while (pb != null) {
                                    stack.push(pb);
                                    pb = pb.getParent();
                                }
                            }
                        }
                    }
                }
            }
        }

        public Node<ColumnGroup> findNode(ColumnGroup g) {
            for (Node<ColumnGroup> r : roots) {
                Node<ColumnGroup> res = r.findNode(g);
                if (res != null) {
                    return res;
                }
            }
            return null;
        }

        public Iterable<Node<ColumnGroup>> getLeafs() {
            return () -> new LeafNodeIterator<>(roots);
        }

        public int getTotalRowCount() {
            int rowCount = 0;
            for (Node<ColumnGroup> n : getLeafs()) {
                Rectangle rt = processed.get(n.getData());
                int nodeMaxRow = rt.getY() + rt.getHeight();
                if (nodeMaxRow > rowCount) {
                    rowCount = nodeMaxRow;
                }
            }
            return rowCount;
        }

        private static class NodeIterator<T> implements Iterator<Node<T>> {

            private final List<Node<T>> roots = new ArrayList<>();
            private Iterator<Node<T>> current = null;
            private int rootIndex = 0;

            public NodeIterator(List<Node<T>> roots) {
                if (roots != null && !roots.isEmpty()) {
                    this.roots.addAll(roots);
                    current = roots.get(0).iterator();
                }
            }

            @Override
            public boolean hasNext() {
                if (current == null) {
                    return false;
                }
                if (current.hasNext()) {
                    return true;
                }
                for (++rootIndex; rootIndex < roots.size(); rootIndex++) {
                    Iterator<Node<T>> next = roots.get(rootIndex).iterator();
                    if (next.hasNext()) {
                        current = next;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Node<T> next() {
                if (hasNext()) {
                    return current.next();
                }
                return null;
            }
        }

        private static class LeafNodeIterator<T> implements Iterator<Node<T>> {

            private final Iterator<Node<T>> nodeIter;
            private Node<T> nextLeaf = null;

            public LeafNodeIterator(List<Node<T>> roots) {
                this.nodeIter = new NodeIterator<>(roots);
            }

            @Override
            public boolean hasNext() {
                if (nextLeaf != null) {
                    return true;
                }
                if (!nodeIter.hasNext()) {
                    return false;
                }
                while (nodeIter.hasNext() && nextLeaf == null) {
                    Node<T> n = nodeIter.next();
                    if (n.getChildren().isEmpty()) {
                        nextLeaf = n;
                    }
                }
                return nextLeaf != null;
            }

            @Override
            public Node<T> next() {
                hasNext();
                Node<T> n = nextLeaf;
                nextLeaf = null;
                return n;
            }

        }
    }

    private static SafeHtml wrapString(String value) {

        //res = "<span qtip=\"" + text.replace("\u00A0", "") + "\">" + text + "</span>";
        return SafeHtmlUtils.fromTrustedString("<span qtip=\"" + SafeHtmlUtils.fromTrustedString(value).asString() + "\">" + SafeHtmlUtils.fromTrustedString(value).asString() + "</span>");

        //return SafeHtmlUtils.fromTrustedString("<span style='white-space: normal;'>" + SafeHtmlUtils.fromTrustedString(value).asString() + "</span>");
    }

    private static ColumnConfig findFirstColumnInGroup(List<ColumnConfig> src, ColumnGroup group) {
        for (ColumnConfig c : src) {
            if (c instanceof CustomColumnConfig) {
                CustomColumnConfig customColumnConfig = (CustomColumnConfig) c;
                if (isColumnInGroup(customColumnConfig.getColumn(), group)) {
                    return customColumnConfig;
                }
            }
        }
        return null;
    }

    private static boolean isColumnInGroup(IColumn c, ColumnGroup group) {
        ColumnGroup g = c.getGroup();
        if (g != null) {
            do {
                if (g == group) {
                    return true;
                }
            } while ((g = g.getParent()) != null);
        }
        return false;
    }
}
