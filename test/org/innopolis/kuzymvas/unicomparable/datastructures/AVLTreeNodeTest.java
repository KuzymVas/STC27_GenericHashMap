package org.innopolis.kuzymvas.unicomparable.datastructures;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.UniComparableContainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNull;

public class AVLTreeNodeTest {

    private ArrayList<UniComparable> keys;
    private ArrayList<Object> values;
    private ArrayList<Integer> shuffle;
    private Object replaceValue;
    private UniComparable invalidKey;


    @Before
    public void setUp() {
        // Подготавливаем массив объектов-ключей
        keys = new ArrayList<>(5);
        keys.add(new UniComparableContainer("42"));
        keys.add(new UniComparableContainer("a"));
        keys.add(new UniComparableContainer(5L));
        keys.add(new UniComparableContainer(4.0));
        keys.add(new UniComparableContainer(null));
        // Подготавливаем массив объектов-значений
        values = new ArrayList<>(5);
        values.add(42);
        values.add(2L);
        values.add(null);
        values.add("");
        values.add(new Object());
        // "Случайный" порядок для имитации произвольного доступа к элементам в хранилище
        shuffle = new ArrayList<>(7);
        shuffle.add(3);
        shuffle.add(1);
        shuffle.add(0);
        shuffle.add(2);
        shuffle.add(4);
        shuffle.add(4);
        shuffle.add(2);
        // Значение для использованием в замене
        replaceValue = 0;
        // Ключ, который никогда не будет присутствовать в хранилище
        invalidKey = new UniComparableContainer(999);
    }

    @Test
    public void testToStringTest() {
        final AVLTreeNode node = new AVLTreeNode(keys.get(0), values.get(0));
        System.out.println("Single node = " + node);
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            root = root.insert(keys.get(i), values.get(i));
        }
        System.out.println("Entire tree = " + root);
    }


    @Test
    public void testCreateSingle() {
        final AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        Assert.assertTrue("Tree doesn't contain key, that was put in it", root.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("Tree returned incorrect value for a given key", values.get(0), root.getValue(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Tree 'getValue' method threw an exception.");
        }
        final KeyValuePair pair = new KeyValuePair(keys.get(0), values.get(0));
        Assert.assertTrue("Tree doesn't contain pair, that was put in it", root.containsPair(pair));
    }

    @Test
    public void testCreateSame() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        root = root.insert(keys.get(0), values.get(0));
        Assert.assertEquals("Tree put a same key into a new node instead of replacing old one during 'put'", 1, root.getHeight());
    }


    @Test
    public void testCreateMultiple() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            root = root.insert(keys.get(i), values.get(i));
        }
        for (Integer i : shuffle) {
            Assert.assertTrue("List  doesn't contain key[" + i + "], that was put in it", root.containsKey(keys.get(i)));
            try {
                Assert.assertEquals("List  returned incorrect value for a given key [" + i + "]", values.get(i), root.getValue(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("List 'getValue' method threw an exception at key-value [" + i + "].");
            }
            final KeyValuePair pair = new KeyValuePair(keys.get(i), values.get(i));
            Assert.assertTrue("List doesn't contain pair, that was put in it", root.containsPair(pair));

        }

    }

    @Test
    public void testHashCode() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        final int originalHash = root.hashCode();
        Assert.assertEquals("Hash code value changed without change in a tree.", originalHash, root.hashCode());
        root = root.insert(keys.get(0), replaceValue);
        Assert.assertNotEquals("Hash code value didn't change with change in a tree", originalHash, root.hashCode());
        root = root.insert(keys.get(1), values.get(1));
        Assert.assertNotEquals("Hash code value didn't change with change in a tree", originalHash, root.hashCode());

    }

    @Test
    public void testEqualsSingle() {
        final AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        final AVLTreeNode otherRoot = new AVLTreeNode(keys.get(0), values.get(0));
        Assert.assertEquals("Same one node trees are not equal", root, otherRoot);
        Assert.assertEquals("Equal one node trees have a different hash value", root.hashCode(), otherRoot.hashCode());
    }

    @Test
    public void testEqualsMultiple() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        AVLTreeNode otherRoot = new AVLTreeNode(keys.get(0), values.get(0));

        for (int i = 1; i < keys.size(); i++) {
            root = root.insert(keys.get(i), values.get(i));
        }
        for (int i = keys.size() - 1; i >= 0; i--) {
            otherRoot = otherRoot.insert(keys.get(i), values.get(i));
        }
        Assert.assertEquals("Filled with same key-value pairs in different order trees are not equal", root, otherRoot);
        Assert.assertEquals("Equal trees have a different hash values", root.hashCode(), otherRoot.hashCode());
    }

    @Test
    public void testReplaceSingle() {
        final AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        try {
            root.replaceValue(keys.get(0), replaceValue);
        } catch (KeyNotPresentException e) {
            Assert.fail("Tree 'replaceValue' method threw an exception.");
        }
        Assert.assertTrue("Tree doesn't contain key, that was put in it", root.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("Tree returned incorrect value for a given key", replaceValue, root.getValue(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Tree 'getValue' method threw an exception.");
        }
    }

    @Test
    public void testReplaceMultiple() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            root = root.insert(keys.get(i), values.get(i));
        }
        for (Integer i : shuffle) {
            try {
                root.replaceValue(keys.get(i), replaceValue);
            } catch (KeyNotPresentException e) {
                Assert.fail("Tree 'replaceValue' method threw an exception.");
            }
            Assert.assertTrue("Tree doesn't contain key[" + i + "], that was put in it", root.containsKey(keys.get(i)));
            try {
                Assert.assertEquals("Tree returned incorrect value for a given key [" + i + "]", replaceValue, root.getValue(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Tree 'getValue' method threw an exception.");
            }
        }
    }


    @Test
    public void testReplaceNegative() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            root = root.insert(keys.get(i), values.get(i));
        }
        try {
            root.replaceValue(invalidKey, replaceValue);
            Assert.fail("Tree 'replaceValue' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            for (Integer i : shuffle) {
                Assert.assertTrue("Tree doesn't contain key[" + i + "], that was put in it", root.containsKey(keys.get(i)));
                try {
                    Assert.assertEquals("Tree returned incorrect value for a given key [" + i + "]", values.get(i), root.getValue(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    Assert.fail("Tree 'getValue' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }

    @Test
    public void testRemoveSingle() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        try {
            root = root.remove(keys.get(0));
            assertNull("Tree didn't remove itself on the last value removal", root);
        } catch (KeyNotPresentException e) {
            Assert.fail("Tree 'removeFromList' method threw an exception.");
        }
    }

    @Test
    public void testRemoveMultiple() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            root = root.insert(keys.get(i), values.get(i));
        }
        for (int i = 0; i < keys.size() - 1; i++) { // shuffle содержит повторяющиеся значения, поэтому не используется здесь
            try {
                root = root.remove(keys.get(i));
                Assert.assertNotEquals("Tree removed itself, while still containing values", null, root);
            } catch (KeyNotPresentException e) {
                Assert.fail("Tree 'removeFromList' method threw an exception.");
            }
            Assert.assertFalse("Tree contain key, that was removed from it", root.containsKey(keys.get(i)));
        }
        try {
            root = root.remove(keys.get(keys.size() - 1));
            assertNull("Tree didn't remove itself on the last value removal", root);
        } catch (KeyNotPresentException e) {
            Assert.fail("Tree 'removeFromList' method threw an exception.");
        }
    }

    @Test
    public void testRemoveNegative() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            root = root.insert(keys.get(i), values.get(i));
        }
        try {
            root = root.remove(invalidKey);
            Assert.fail("Tree 'removeFromList' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            for (Integer i : shuffle) {
                Assert.assertTrue("Tree doesn't contain key[" + i + "], that was put in it", root.containsKey(keys.get(i)));
                try {
                    Assert.assertEquals("Tree returned incorrect value for a given key [" + i + "]", values.get(i), root.getValue(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    Assert.fail("Tree 'getValue' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }

    @Test
    public void testTreeSelfBalancing() {
        AVLTreeNode root = new AVLTreeNode(keys.get(0), values.get(0));
        Assert.assertEquals("Height of a single node tree isn't equal to 1", 1, root.getHeight());
        Assert.assertEquals("Single node tree is unbalanced", 0, root.getMaxAbsTreeBalance());
        UniComparableContainer[] seqKeys = new UniComparableContainer[1000];
        for (int i = 0; i < 1000; i++) {
            seqKeys[i] = new UniComparableContainer(i);
            root = root.insert(seqKeys[i], null);
            Assert.assertTrue("Tree with " + (i + 2) + " elements and height " + root.getHeight() + " is too high for AVL tree.", root.getHeight() <= 1.45 * Math.log(i + 4) / Math.log(2));
            Assert.assertTrue("Tree with " + (i + 2) + " nodes is unbalanced.", root.getMaxAbsTreeBalance() < 2);
        }
        AVLTreeNode rootReverse = new AVLTreeNode(keys.get(0), values.get(0));
        for (int i = 0; i < 1000; i++) {
            rootReverse = rootReverse.insert(seqKeys[1000 - i - 1], null);
            Assert.assertTrue("Tree with " + (i + 2) + " elements and height " + rootReverse.getHeight() + " is too high for AVL tree.", rootReverse.getHeight() <= 1.45 * Math.log(i + 4) / Math.log(2));
            Assert.assertTrue("Tree with " + (i + 2) + " nodes is unbalanced.", rootReverse.getMaxAbsTreeBalance() < 2);
        }

    }


}