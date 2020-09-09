package org.innopolis.kuzymvas.hashmap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class SimpleHashMapTest {

    private HashMap hashmap;
    private ArrayList<Object> keys;
    private ArrayList<Object> keysForCollision;
    private ArrayList<Object> values;
    private ArrayList<Integer> shuffle;
    private Object replaceValue;
    private Object invalidKey;

    private static class ZeroHash {

        @Override
        public int hashCode() {
            return 0;
        }
    }

    @Before
    public void setUp() {
        // Создаем хранилище для тестирования
        hashmap = new SimpleHashMap();
        // Подготавливаем массив объектов-ключей
        keys = new ArrayList<>(5);
        keys.add(1);
        keys.add(2L);
        keys.add("Three");
        keys.add(4.0);
        keys.add(null);
        // Подготавливаем массив объектов-ключей, гарантированно вызывающих коллизии.
        keysForCollision = new ArrayList<>(5);
        keysForCollision.add(new ZeroHash());
        keysForCollision.add(new ZeroHash());
        keysForCollision.add(new ZeroHash());
        keysForCollision.add(new ZeroHash());
        keysForCollision.add(new ZeroHash());
        // Подготавливаем массив объектов-значений
        values = new ArrayList<>(5);
        values.add(42);
        values.add(2L);
        values.add(null);
        values.add("");
        values.add(new SimpleHashMap(1)); // Нет запрета, что хэш таблица не может хранить ссылку на хэш таблицу.
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
    public void testConstructorException() {
        try {
            new SimpleHashMap(-1);
            Assert.fail("Was able to create hash map with a negative bucket count");
        }
        catch (IllegalArgumentException ignored) {

        }
        try {
             new SimpleHashMap(0);
            Assert.fail("Was able to create hash map with a zero bucket count");
        }
        catch (IllegalArgumentException ignored) {

        }
    }

    @Test
    public void testToString() {
        try {
            HashMap hm =new SimpleHashMap(3);
            hm.put("key1","val1");
            hm.put("key2","val2");
            hm.put("key3","val3");
            hm.put("key4","val4");
            hm.put("key5","val5");
            System.out.println("hm.toString() = " + hm.toString());
        }
        catch (IllegalArgumentException ignored) {
            Assert.fail("Was unable to create hash map with a given bucker count");
        }
    }



    @Test
    public void testEmptySize() {
        Assert.assertEquals("Empty hash map size is incorrect", 0, hashmap.size());
    }

    @Test
    public void testPutSingle() {
        hashmap.put(keys.get(0), values.get(0));
        Assert.assertEquals("Hash map size is incorrect", 1, hashmap.size());
        Assert.assertTrue("Hash map doesn't contain key, that was put in it", hashmap.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("Hash map returned incorrect value for a given key", values.get(0), hashmap.get(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'get' method threw an exception.");
        }
    }

    @Test
    public void testPutSame() {
        hashmap.put(keys.get(0), values.get(0));
        hashmap.put(keys.get(0), replaceValue);
        Assert.assertEquals("Hash map size is incorrect", 1, hashmap.size());
        Assert.assertTrue("Hash map doesn't contain key, that was put in it", hashmap.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("Hash map returned incorrect value for a given key", replaceValue, hashmap.get(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'get' method threw an exception.");
        }
    }

    @Test
    public void testPutMultiple() {
        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        Assert.assertEquals("Hash map size is incorrect", keys.size(), hashmap.size());
        for (Integer i : shuffle) {
            Assert.assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keys.get(i)));
            try {
                Assert.assertEquals("Hash map returned incorrect value for a given key [" + i + "]", values.get(i), hashmap.get(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
            }
        }

    }

    @Test
    public void testPutCollision() {
        for (int i = 0; i < keysForCollision.size(); i++) {
            hashmap.put(keysForCollision.get(i), values.get(i));
        }
        Assert.assertEquals("Hash map size is incorrect", keysForCollision.size(), hashmap.size());
        for (Integer i : shuffle) {
            Assert.assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keysForCollision.get(i)));
            try {
                Assert.assertEquals("Hash map returned incorrect value for a given key [" + i + "]", values.get(i), hashmap.get(keysForCollision.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
            }
        }

    }

    @Test
    public void testEqualsEmpty() {
        HashMap otherHashMap = new SimpleHashMap();
        Assert.assertEquals("Пустые хэш таблицы не равны между собой", hashmap, otherHashMap);
    }

    @Test
    public void testEqualsSingle() {
        HashMap otherHashMap = new SimpleHashMap();
        hashmap.put(keys.get(0), values.get(0));
        otherHashMap.put(keys.get(0), values.get(0));
        Assert.assertEquals("Хэш таблицы c одним одинаковым элементом не равны между собой", hashmap, otherHashMap);
    }

    @Test
    public void testEqualsMultiple() {
        HashMap otherHashMap = new SimpleHashMap();

        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        for (int i = keys.size() - 1; i > -1; i--) {
            otherHashMap.put(keys.get(i), values.get(i));
        }
        Assert.assertEquals("Заполненные одними элементами в разном порядке хэш таблицы не равны между собой", hashmap, otherHashMap);

    }

    @Test
    public void testEqualsCollision() {
        HashMap otherHashMap = new SimpleHashMap();

        for (int i = 0; i < keysForCollision.size(); i++) {
            hashmap.put(keysForCollision.get(i), values.get(i));
        }
        for (int i = keysForCollision.size() - 1; i > -1; i--) {
            otherHashMap.put(keysForCollision.get(i), values.get(i));
        }
        Assert.assertEquals("Заполненные одними элементами с коллизиями в разном порядке хэш таблицы не равны между собой", hashmap, otherHashMap);

    }

    @Test
    public void testReplaceSingle() {
        hashmap.put(keys.get(0), values.get(0));
        try {
            hashmap.replace(keys.get(0), replaceValue);
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'replace' method threw an exception.");
        }
        Assert.assertEquals("Hash map size is incorrect", 1, hashmap.size());
        Assert.assertTrue("Hash map doesn't contain key, that was put in it", hashmap.containsKey(keys.get(0)));
        try {
            Assert.assertEquals("Hash map returned incorrect value for a given key", replaceValue, hashmap.get(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'get' method threw an exception.");
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
                Assert.fail("Hash map 'replace' method threw an exception.");
            }
            Assert.assertEquals("Hash map size is incorrect after " + i + " replacement", keys.size(), hashmap.size());
            Assert.assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keys.get(i)));
            try {
                Assert.assertEquals("Hash map returned incorrect value for a given key [" + i + "]", replaceValue, hashmap.get(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
            }
        }
    }

    @Test
    public void testReplaceCollision() {
        for (int i = 0; i < keysForCollision.size(); i++) {
            hashmap.put(keysForCollision.get(i), values.get(i));
        }
        for (Integer i : shuffle) {
            try {
                hashmap.replace(keysForCollision.get(i), replaceValue);
            } catch (KeyNotPresentException e) {
                Assert.fail("Hash map 'replace' method threw an exception.");
            }
            Assert.assertEquals("Hash map size is incorrect after " + i + " replacement", keysForCollision.size(), hashmap.size());
            Assert.assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keysForCollision.get(i)));
            try {
                Assert.assertEquals("Hash map returned incorrect value for a given key [" + i + "]", replaceValue, hashmap.get(keysForCollision.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
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
            Assert.fail("Hash map 'replace' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            Assert.assertEquals("Hash map size is incorrect", keys.size(), hashmap.size());
            for (Integer i : shuffle) {
                Assert.assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keys.get(i)));
                try {
                    Assert.assertEquals("Hash map returned incorrect value for a given key [" + i + "]", values.get(i), hashmap.get(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    Assert.fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }

    @Test
    public void testRemoveSingle() {
        hashmap.put(keys.get(0), values.get(0));
        try {
            Assert.assertEquals("Hash map 'remove' returned incorrect value for a given key", values.get(0), hashmap.remove(keys.get(0)));
        } catch (KeyNotPresentException e) {
            Assert.fail("Hash map 'remove' method threw an exception.");
        }
        Assert.assertEquals("Hash map size is incorrect", 0, hashmap.size());
        Assert.assertFalse("Hash map does contain key, that was removed from it", hashmap.containsKey(keys.get(0)));
    }

    @Test
    public void testRemoveMultiple() {
        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        int expectedSize = hashmap.size();
        for (int i = 0; i< keys.size(); i++) { // shuffle содержит повторяющиеся значения, поэтому не используется здесь
            try {
                Assert.assertEquals("Hash map 'remove' returned incorrect value for a given key", values.get(i), hashmap.remove(keys.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Hash map 'remove' method threw an exception.");
            }
            expectedSize--;
            Assert.assertEquals("Hash map size is incorrect after " + i + " removal", expectedSize, hashmap.size());
            Assert.assertFalse("Hash map does contain key, that was removed from it", hashmap.containsKey(keys.get(i)));
        }
    }

    @Test
    public void testRemoveCollision() {
        for (int i = 0; i < keysForCollision.size(); i++) {
            hashmap.put(keysForCollision.get(i), values.get(i));
        }
        int expectedSize = hashmap.size();
        for (int i = 0; i< keysForCollision.size(); i++) { // shuffle содержит повторяющиеся значения, поэтому не используется здесь
            try {
                Assert.assertEquals("Hash map 'remove' returned incorrect value for a given key", values.get(i), hashmap.remove(keysForCollision.get(i)));
            } catch (KeyNotPresentException e) {
                Assert.fail("Hash map 'remove' method threw an exception.");
            }
            expectedSize--;
            Assert.assertEquals("Hash map size is incorrect after " + i + " removal", expectedSize, hashmap.size());
            Assert.assertFalse("Hash map does contain key, that was removed from it", hashmap.containsKey(keysForCollision.get(i)));
        }
    }

    @Test
    public void testRemoveNegative() {
        for (int i = 0; i < keys.size(); i++) {
            hashmap.put(keys.get(i), values.get(i));
        }
        try {
            hashmap.remove(invalidKey);
            Assert.fail("Hash map 'remove' method did NOT throw an exception on invalid key.");
        } catch (KeyNotPresentException e) {
            // Проверяем, что выброс исключения не привел к нарушению состояния хранилища
            Assert.assertEquals("Hash map size is incorrect", keys.size(), hashmap.size());
            for (Integer i : shuffle) {
                Assert.assertTrue("Hash map doesn't contain key[" + i + "], that was put in it", hashmap.containsKey(keys.get(i)));
                try {
                    Assert.assertEquals("Hash map returned incorrect value for a given key [" + i + "]", values.get(i), hashmap.get(keys.get(i)));
                } catch (KeyNotPresentException e2) {
                    Assert.fail("Hash map 'get' method threw an exception at key-value [" + i + "].");
                }
            }
        }
    }
}