package org.innopolis.kuzymvas.unicomparable.datastructures;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.UniComparableContainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNull;

public class ListNodeTest {

    private ArrayList<UniComparable> keys;
    private ArrayList<Object> values;
    private ArrayList<Integer> shuffle;
    private  Object replaceValue;
    private  UniComparable invalidKey;



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
        values.add(new org.innopolis.kuzymvas.original.datastructures.ListNode("",""));
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
        final ListNode node = new ListNode(new UniComparableContainer("42"),null);
        System.out.println("Single node = " + node);
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        for (int i = 1; i<keys.size(); i++) {
            head.putIntoList(keys.get(i),values.get(i));
        }
        System.out.println("Entire list = " + head);
    }


    @Test
    public void testCreateSingle() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        Assert.assertTrue("List doesn't contain key, that was put in it", head.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("List returned incorrect value for a given key", values.get(0), head.getValue(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("List 'getValue' method threw an exception.");
        }
        final KeyValuePair pair = new KeyValuePair(keys.get(0), values.get(0));
        Assert.assertTrue("List doesn't contain pair, that was put in it", head.containsPair(pair));
    }

    @Test
    public void testCreateSame() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        Assert.assertFalse("List put a same key into a new node instead of replacing old one during 'put'",head.putIntoList(keys.get(0), values.get(0)));
    }


    @Test
    public void testCreateMultiple() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        for (int i = 1; i<keys.size(); i++) {
            Assert.assertTrue("List didn't  put a unique key into a new node during 'put' for a given key [" + i + "]",head.putIntoList(keys.get(i), values.get(i)));
        }
        for (Integer i : shuffle) {
            Assert.assertTrue("List  doesn't contain key[" + i + "], that was put in it", head.containsKey(keys.get(i)));
            try {
                Assert.assertEquals("List  returned incorrect value for a given key [" + i + "]", values.get(i), head.getValue(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("List 'getValue' method threw an exception at key-value [" + i + "].");
            }
            final KeyValuePair pair = new KeyValuePair(keys.get(i), values.get(i));
            Assert.assertTrue("List doesn't contain pair, that was put in it", head.containsPair(pair));

        }

    }

    @Test
    public void testHashCode() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        final int originalHash = head.hashCode();
        Assert.assertEquals("Hash code changed without changes to the list", originalHash, head.hashCode());
        head.putIntoList(keys.get(0), replaceValue);
        Assert.assertNotEquals("Hash code didn't change after changes to the list", originalHash, head.hashCode());
        head.putIntoList(keys.get(1), values.get(1));
        Assert.assertNotEquals("Hash code didn't change after changes to the list", originalHash, head.hashCode());

    }

    @Test
    public void testEqualsSingle() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        final ListNode otherHead = new ListNode(keys.get(0), values.get(0));
        Assert.assertEquals("Lists with th same one element are not equal", head, otherHead);
        Assert.assertEquals("Equal lists with same one element have a different hashes", head.hashCode(), otherHead.hashCode());
    }

    @Test
    public void testEqualsMultiple() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        final ListNode otherHead = new ListNode(keys.get(0), values.get(0));

        for (int i = 1; i < keys.size(); i++) {
            head.putIntoList(keys.get(i), values.get(i));
        }
        for (int i = keys.size() - 1; i >= 0; i--) {
            otherHead.putIntoList(keys.get(i), values.get(i));
        }
        Assert.assertEquals("Lists with same elements in a different orders are not equal", head, otherHead);
        Assert.assertEquals("Equal lists have a different hashes", head.hashCode(), otherHead.hashCode());
    }

    @Test
    public void testReplaceSingle() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        try {
            head.replaceValue(keys.get(0), replaceValue);
        } catch (KeyNotPresentException e) {
            Assert.fail("List 'replaceValue' method threw an exception.");
        }
        Assert.assertTrue("List doesn't contain key, that was put in it", head.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("List returned incorrect value for a given key", replaceValue, head.getValue(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("List 'getValue' method threw an exception.");
        }
    }

    @Test
    public void testReplaceMultiple() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            head.putIntoList(keys.get(i), values.get(i));
        }
        for (Integer i : shuffle) {
            try {
                head.replaceValue(keys.get(i), replaceValue);
            } catch (KeyNotPresentException e) {
                Assert.fail("List 'replaceValue' method threw an exception.");
            }
            Assert.assertTrue("List doesn't contain key[" + i + "], that was put in it", head.containsKey(keys.get(i)));
            try {
                Assert.assertEquals("List returned incorrect value for a given key [" + i + "]", replaceValue, head.getValue(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("List 'getValue' method threw an exception.");
            }
        }
    }


    @Test
    public void testReplaceNegative() {
        final ListNode head = new ListNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            head.putIntoList(keys.get(i), values.get(i));
        }
        try {
            head.replaceValue(invalidKey, replaceValue);
            Assert.fail("List 'replaceValue' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            for (Integer i : shuffle) {
                Assert.assertTrue("List doesn't contain key[" + i + "], that was put in it", head.containsKey(keys.get(i)));
                try {
                    Assert.assertEquals("List returned incorrect value for a given key [" + i + "]", values.get(i), head.getValue(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    Assert.fail("List 'getValue' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }

    @Test
    public void testRemoveSingle() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
        try {
            head = head.removeFromList(keys.get(0));
            assertNull("List didn't remove itself on the last value removal", head);
        } catch (KeyNotPresentException e) {
            Assert.fail("List 'removeFromList' method threw an exception.");
        }
    }

    @Test
    public void testRemoveMultiple() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            head.putIntoList(keys.get(i), values.get(i));
        }
        for (int i = 0; i < keys.size()-1; i++) { // shuffle содержит повторяющиеся значения, поэтому не используется здесь
            try {
                head = head.removeFromList(keys.get(i));
                Assert.assertNotEquals("List removed itself, while still containing values", null, head);
            } catch (KeyNotPresentException e) {
                Assert.fail("List 'removeFromList' method threw an exception.");
            }
            Assert.assertFalse("List contain key, that was removed from it", head.containsKey(keys.get(i)));
        }
        try {
            head = head.removeFromList(keys.get(keys.size()-1));
            assertNull("List didn't remove itself on the last value removal", head);
        } catch (KeyNotPresentException e) {
            Assert.fail("List 'removeFromList' method threw an exception.");
        }
    }
    @Test
    public void testRemoveNegative() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
        for (int i = 1; i < keys.size(); i++) {
            head.putIntoList(keys.get(i), values.get(i));
        }
        try {
            head = head.removeFromList(invalidKey);
            Assert.fail("List 'removeFromList' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            for (Integer i : shuffle) {
                Assert.assertTrue("List doesn't contain key[" + i + "], that was put in it", head.containsKey(keys.get(i)));
                try {
                    Assert.assertEquals("List returned incorrect value for a given key [" + i + "]", values.get(i), head.getValue(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    Assert.fail("List 'getValue' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }

}