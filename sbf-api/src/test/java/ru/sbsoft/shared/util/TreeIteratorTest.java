package ru.sbsoft.shared.util;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vk
 */
public class TreeIteratorTest {

    public TreeIteratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isLeafsOnly method, of class TreeIterator.
     */
    @Test
    public void testIsLeafsOnly() {
        System.out.println("isLeafsOnly");
        testLeafsOnly(null);
        testLeafsOnly(true);
        testLeafsOnly(false);
    }

    private void testLeafsOnly(Boolean expResult) {
        TreeIterator instance = getIter(expResult);
        if (expResult == null) {
            expResult = false;
        }
        boolean result = instance.isLeafsOnly();
        assertEquals(expResult, result);
    }

    /**
     * Test of next method, of class TreeIterator.
     */
    @Test
    public void testNext() {
        System.out.println("next");
        TreeIterator inst = getIter(null);
        for (int i = 0; i < 3; i++) {
            //System.out.println(inst.next());
            assertEquals(inst.next(), "." + i);
            for (int j = 0; j < 3; j++) {
                //System.out.println(inst.next());
                assertEquals(inst.next(), "." + i + "." + j);
                for (int k = 0; k < 3; k++) {
                    //System.out.println(inst.next());
                    assertEquals(inst.next(), "." + i + "." + j + "." + k);
                }
            }
        }
        assertNull(inst.next());
    }
    
    @Test
    public void testNextLeafsOnly() {
        System.out.println("next");
        TreeIterator inst = getIter(true);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    //System.out.println(inst.next());
                    assertEquals(inst.next(), "." + i + "." + j + "." + k);
                }
            }
        }
        assertNull(inst.next());
    }

    private static class Node {

        final String val;
        final List<Node> children = new ArrayList<>();

        public Node(String val) {
            this.val = val;
        }
    }

    TreeIterator<Node, String> getIter(Boolean leafsOnly) {
        List<Node> top = new ArrayList<>();
        fillNodes(top, "", 0);
        TreeIterator<Node, String> res = new TreeIterator<>(top, n -> n.children, n -> n.val);
        if (leafsOnly != null) {
            res.setLeafsOnly(leafsOnly);
        }
        return res;
    }

    void fillNodes(List<Node> l, String parentVal, int level) {
        if (level < 3) {
            for (int i = 0; i < 3; i++) {
                String val = parentVal + '.' + i;
                Node n = new Node(val);
                l.add(n);
                fillNodes(n.children, val, level + 1);
            }
        }
    }
}
