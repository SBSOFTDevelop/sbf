package ru.sbsoft.shared.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author vk
 * @param <N> - node type
 * @param <T> - data type
 */
public class TreeIterator<N, T> implements ReusableIterator<T> {

    private final List<N> topNodes;
    private final List<SimpleIterator<N>> nodes = new ArrayList<>();

    private final Function<N, List<N>> childNodesProvider;
    private final Function<N, T> valueProvider;

    private boolean leafsOnly = false;

    public TreeIterator(List<N> topNodes, Function<N, List<N>> childNodesProvider, Function<N, T> valueProvider) {
        this.topNodes = topNodes;
        this.childNodesProvider = childNodesProvider;
        this.valueProvider = valueProvider;
        init();
    }
    
    private void init(){
        nodes.clear();
        nodes.add(new ListIterator<>(topNodes));
    }

    @Override
    public void reset() {
        init();
    }

    public boolean isLeafsOnly() {
        return leafsOnly;
    }

    public TreeIterator setLeafsOnly(boolean leafsOnly) {
        this.leafsOnly = leafsOnly;
        return this;
    }

    @Override
    public T next() {
        if (nodes.isEmpty()) {
            return null;
        }
        SimpleIterator<N> lastNodes = nodes.get(nodes.size() - 1);
        N nextNode = lastNodes.next();
        if (nextNode == null) {
            nodes.remove(nodes.size() - 1);
            return next();
        }
        List<N> children = childNodesProvider.apply(nextNode);
        if (!children.isEmpty()) {
            nodes.add(new ListIterator<>(children));
        }
        if (leafsOnly && !children.isEmpty()) {
            return next();
        } else {
            return valueProvider.apply(nextNode);
        }
    }
}
