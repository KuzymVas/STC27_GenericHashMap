package org.innopolis.kuzymvas.original.datastructures;

import org.junit.Assert;
import org.junit.Test;

public class KeyValuePairTest {


    @Test
    public void testToStringTest() {
        KeyValuePair pair = new KeyValuePair("42",null);
        System.out.println("pair = " + pair);
    }

    @Test
    public void testGetValueTest() {
        KeyValuePair pair = new KeyValuePair("a","b");
        Assert.assertEquals("Пара вернуло не то значение, что было ей передано","b",pair.getValue());
    }

    @Test
    public void testGetKeyTest() {
        KeyValuePair pair = new KeyValuePair("a","b");
        Assert.assertEquals("Пара вернула не тот ключ, что был ей передан","a",pair.getKey());
    }

    @Test
    public void testHasKeyTest() {
        KeyValuePair pair = new KeyValuePair("a","b");
        Assert.assertTrue("Пара ключ-значение не распознала свой ключ", pair.hasKey("a"));
        Assert.assertFalse("Пара ключ-значение ошибочно распознала не свой ключ", pair.hasKey("b"));
        Assert.assertFalse("Пара ключ-значение с ключом не-null ошибочно распознала null", pair.hasKey(null));
        KeyValuePair pairNullKey = new KeyValuePair(null,"b");
        Assert.assertTrue("Пара ключ-значение с ключом null не распознала свой ключ", pairNullKey.hasKey(null));
        Assert.assertFalse("Пара ключ-значение с ключом null  ошибочно распознала не свой ключ", pairNullKey.hasKey("b"));

    }

    @Test
    public void testEqualsTest() {
        KeyValuePair pair1 = new KeyValuePair("a","b");
        KeyValuePair pair2 = new KeyValuePair("a","b");
        Assert.assertEquals("Две одинаковые пары ключ-значение не равны",pair1,pair2);
        Assert.assertEquals("Две одинаковые пары ключ-значение имеют разный хэш код",pair1.hashCode(),pair2.hashCode());
    }

    @Test
    public void testNotEqualsTest() {
        KeyValuePair pairSameValue1 = new KeyValuePair("a","b");
        KeyValuePair pairSameValue2 = new KeyValuePair("a","c");
        Assert.assertNotEquals("Две  пары ключ-значение c разными значениями равны",pairSameValue1,pairSameValue2);
        Assert.assertNotEquals("Две  пары ключ-значение c разными значениями имеют одинаковый хэш код",pairSameValue1.hashCode(),pairSameValue2.hashCode());

        KeyValuePair pairSameKey1 = new KeyValuePair("a","b");
        KeyValuePair pairSameKey2 = new KeyValuePair("c","b");
        Assert.assertNotEquals("Две  пары ключ-значение c разными ключами равны",pairSameKey1,pairSameKey2);
        Assert.assertNotEquals("Две  пары ключ-значение c разными ключами имеют одинаковый хэш код",pairSameKey1.hashCode(),pairSameKey2.hashCode());

    }

}