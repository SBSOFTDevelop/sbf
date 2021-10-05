package ru.sbsoft.shared.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Kiselev
 */
public class Node<T> implements Iterable<Node<T>> {

    private Node<T> parent = null;
    private List<Node<T>> children = null;
    private final T data;

    public Node(T data) {
        this(null, data);
    }

    public Node(Node<T> parent, T data) {
        this.parent = parent;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void addChild(Node<T> child) {
        if (child != null) {
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(child);
            child.parent = this;
        }
    }

    public Node<T> getParent() {
        return parent;
    }

    public List<Node<T>> getChildren() {
        return children != null ? Collections.unmodifiableList(children) : Collections.<Node<T>>emptyList();
    }

    public Node<T> findNode(T obj) {
        if (Objects.equals(data, obj)) {
            return this;
        }
        if (children != null) {
            for (Node<T> n : children) {
                Node<T> res = n.findNode(obj);
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new NodeIterator<>(this);
    }

    private static class NodeIterator<T> implements Iterator<Node<T>> {

        private final Deque<Node<T>> queue = new ArrayDeque<Node<T>>();

        public NodeIterator(Node<T> root) {
            queue.add(root);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Node<T> next() {
            if (hasNext()) {
                Node<T> n = queue.poll();
                for (Node<T> nn : n.getChildren()) {
                    queue.push(nn);
                }
                return n;
            }
            return null;
        }
    }
}
