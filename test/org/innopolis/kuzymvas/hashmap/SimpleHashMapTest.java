package org.innopolis.kuzymvas.hashmap;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SimpleHashMapTest extends TestCase {

    private HashMap hashmap;
    private ArrayList<Object> keys;
    private ArrayList<Object> values;
    private ArrayList<Integer> shuffle;
    private Object replaceValue;
    private Object invalidKey;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Создаем хранилище для тестирования
        hashmap = new SimpleHashMap();
        // Подготавливаем массив объектов-ключей
        keys = new ArrayList<>(5);
        keys.add(new Integer(1));
        keys.add(new Long(2L));
        keys.add("Three");
        keys.add(new Double(4.0));
        keys.add(null);
        // Подготавливаем массив объектов-значений
        values = new ArrayList<>(5);
        values.add(new Integer(42));
        values.add(new Long(2L));
        values.add(null);
        values.add("");
        values.add(hashmap); // Нет запрета, что хранилище не может хранить ссылку на себя.
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
        replaceValue = new Integer(0);
        // Ключ, который никогда не будет присутствовать в хранилище
        invalidKey = new Integer(42);

    }



    @Test
    public void testEmptySize() {
        assertEquals("Empty hash map size is incorrect", 0, hashmap.size());
    }

    @Test
    public void testPutSingle() {
        hashmap.put(keys.get(0), values.get(0));
        assertEquals("Hash map size is incorrect", 1, hashmap.size());
        assertTrue("Hash map doesn't contain key, that was put in it", hashmap.containsKey(keys.get(0)));
        try {
            assertEquals("Hash map returned incorrect value for a given key", values.get(0), hashmap.get(keys.get(0)));
        } catch (KeyNotPresentException e) {
            fail("Hash map 'get' method threw an exception.");
        }
    }

    @Test
    public void testPutMultiple() {
        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        assertEquals("Hash map size is incorrect", keys.size(), hashmap.size());
        for (Integer i : shuffle) {
            assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keys.get(i)));
            try {
                assertEquals("Hash map returned incorrect value for a given key [" + i + "]", values.get(i), hashmap.get(keys.get(i)));
            } catch (KeyNotPresentException e) {
                fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
            }
        }

    }

    @Test
    public void testReplaceSingle() {
        hashmap.put(keys.get(0), values.get(0));
        try {
            hashmap.replace(keys.get(0), replaceValue);
        } catch (KeyNotPresentException e) {
            fail("Hash map 'replace' method threw an exception.");
        }
        assertEquals("Hash map size is incorrect", 1, hashmap.size());
        assertTrue("Hash map doesn't contain key, that was put in it", hashmap.containsKey(keys.get(0)));
        try {
            assertEquals("Hash map returned incorrect value for a given key", replaceValue, hashmap.get(keys.get(0)));
        } catch (KeyNotPresentException e) {
            fail("Hash map 'get' method threw an exception.");
        }
    }

    @Test
    public void testReplaceMultiple() {
        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        for (Integer i : shuffle) {
            try {
                hashmap.replace(keys.get(i), replaceValue);
            } catch (KeyNotPresentException e) {
                fail("Hash map 'replace' method threw an exception.");
            }
            assertEquals("Hash map size is incorrect after " + i + " replacement", keys.size(), hashmap.size());
            assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keys.get(i)));
            try {
                assertEquals("Hash map returned incorrect value for a given key [" + i + "]", replaceValue, hashmap.get(keys.get(i)));
            } catch (KeyNotPresentException e) {
                fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
            }
        }
    }

    @Test
    public void testReplaceNegative() {
        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        try {
            hashmap.replace(invalidKey, replaceValue);
            fail("Hash map 'replace' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            assertEquals("Hash map size is incorrect", keys.size(), hashmap.size());
            for (Integer i : shuffle) {
                assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keys.get(i)));
                try {
                    assertEquals("Hash map returned incorrect value for a given key [" + i + "]", values.get(i), hashmap.get(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }

    @Test
    public void testRemoveSingle() {
        hashmap.put(keys.get(0), values.get(0));
        try {
            assertEquals("Hash map 'remove' returned incorrect value for a given key", values.get(0), hashmap.remove(keys.get(0)));
        } catch (KeyNotPresentException e) {
            fail("Hash map 'remove' method threw an exception.");
        }
        assertEquals("Hash map size is incorrect", 0, hashmap.size());
        assertTrue("Hash map does contain key, that was removed from it", !(hashmap.containsKey(keys.get(0))));
    }

    @Test
    public void testRemoveMultiple() {
        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        int expectedSize = hashmap.size();
        for (int i = 0; i< keys.size(); i++) { // shuffle содержит повторяющиеся значения, поэтому не используется здесь
            try {
                assertEquals("Hash map 'remove' returned incorrect value for a given key", values.get(i), hashmap.remove(keys.get(i)));
            } catch (KeyNotPresentException e) {
                fail("Hash map 'remove' method threw an exception.");
            }
            expectedSize--;
            assertEquals("Hash map size is incorrect after " + i + " removal", expectedSize, hashmap.size());
            assertTrue("Hash map does contain key, that was removed from it", !(hashmap.containsKey(keys.get(i))));
        }
    }

    @Test
    public void testRemoveNegative() {
        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        try {
            hashmap.remove(invalidKey);
            fail("Hash map 'remove' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            assertEquals("Hash map size is incorrect", keys.size(), hashmap.size());
            for (Integer i : shuffle) {
                assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keys.get(i)));
                try {
                    assertEquals("Hash map returned incorrect value for a given key [" + i + "]", values.get(i), hashmap.get(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }
}