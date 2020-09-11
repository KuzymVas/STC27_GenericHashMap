package org.innopolis.kuzymvas.datastructures;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNull;

public class ListNodeTest {

    private ArrayList<Object> keys;
    private ArrayList<Object> values;
    private ArrayList<Integer> shuffle;
    private  Object replaceValue;
    private  Object invalidKey;



    @Before
    public void setUp() {
        // Подготавливаем массив объектов-ключей
        keys = new ArrayList<>(5);
        keys.add(1);
        keys.add(2L);
        keys.add("Three");
        keys.add(4.0);
        keys.add(null);
        // Подготавливаем массив объектов-значений
        values = new ArrayList<>(5);
        values.add(42);
        values.add(2L);
        values.add(null);
        values.add("");
        values.add(new ListNode("",""));
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
        invalidKey = 42;

    }

    @Test
    public void testToStringTest() {
        ListNode node = new ListNode("42",null);
        System.out.println("Single node = " + node);
        ListNode head = new ListNode(keys.get(0), values.get(0));
        for (int i = 1; i<keys.size(); i++) {
            head.putIntoList(keys.get(i),values.get(i));
        }
        System.out.println("Entire list = " + head);
    }


    @Test
    public void testCreateSingle() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
        Assert.assertTrue("List doesn't contain key, that was put in it", head.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("List returned incorrect value for a given key", values.get(0), head.getValue(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("List 'getValue' method threw an exception.");
        }
        KeyValuePair pair = new KeyValuePair(keys.get(0), values.get(0));
        Assert.assertTrue("List doesn't contain pair, that was put in it", head.containsPair(pair));
    }

    @Test
    public void testCreateSame() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
        Assert.assertFalse("List put a same key into a new node instead of replacing old one during 'put'",head.putIntoList(keys.get(0), values.get(0)));
    }


    @Test
    public void testCreateMultiple() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
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
            KeyValuePair pair = new KeyValuePair(keys.get(i), values.get(i));
            Assert.assertTrue("List doesn't contain pair, that was put in it", head.containsPair(pair));

        }

    }

    @Test
    public void testHashCode() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
        int originalHash = head.hashCode();
        Assert.assertEquals("Хэш код изменяется без изменений в списке", originalHash, head.hashCode());
        head.putIntoList(keys.get(0), replaceValue);
        Assert.assertNotEquals("Хэш код НЕ изменяется после изменений в списке", originalHash, head.hashCode());
        head.putIntoList(keys.get(1), values.get(1));
        Assert.assertNotEquals("Хэш код НЕ изменяется после изменений в списке", originalHash, head.hashCode());

    }

    @Test
    public void testEqualsSingle() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
        ListNode otherHead = new ListNode(keys.get(0), values.get(0));
        Assert.assertEquals("Списки c одним одинаковым элементом не равны между собой", head, otherHead);
        Assert.assertEquals("Равные между собой списки с одним одинаковым элементом имеют разный хэш", head.hashCode(), otherHead.hashCode());
    }

    @Test
    public void testEqualsMultiple() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
        ListNode otherHead = new ListNode(keys.get(0), values.get(0));

        for (int i = 1; i < keys.size(); i++) {
            head.putIntoList(keys.get(i), values.get(i));
        }
        for (int i = keys.size() - 1; i >= 0; i--) {
            otherHead.putIntoList(keys.get(i), values.get(i));
        }
        Assert.assertEquals("Заполненные одними элементами в разном порядке списки не равны между собой", head, otherHead);
        Assert.assertEquals("Равные между собой списки имеют разный хэш", head.hashCode(), otherHead.hashCode());
    }

    @Test
    public void testReplaceSingle() {
        ListNode head = new ListNode(keys.get(0), values.get(0));
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
        ListNode head = new ListNode(keys.get(0), values.get(0));
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
        ListNode head = new ListNode(keys.get(0), values.get(0));
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