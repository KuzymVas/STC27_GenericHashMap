package org.innopolis.kuzymvas.unicomparable.datastructures;

import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.UniComparableContainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class KeyValuePairTest {

    private ArrayList<UniComparable> keys;
    private ArrayList<Object> values;

    @Before
    public void setUp() {
        keys = new ArrayList<>(3);
        keys.add(new UniComparableContainer("42"));
        keys.add(new UniComparableContainer("a"));
        keys.add(new UniComparableContainer(5L));
        values = new ArrayList<>(3);
        values.add(null);
        values.add(4.0);
        values.add("b");
    }

    @Test
    public void testToStringTest() {
        KeyValuePair pair = new KeyValuePair(keys.get(0),values.get(0));
        System.out.println("pair = " + pair);
    }

    @Test
    public void testGetValueTest() {
        KeyValuePair pair = new KeyValuePair(keys.get(1),values.get(1));
        Assert.assertEquals("Пара вернуло не то значение, что было ей передано",values.get(1),pair.getValue());
    }

    @Test
    public void testGetKeyTest() {
        KeyValuePair pair = new KeyValuePair(keys.get(1),values.get(1));
        Assert.assertEquals("Пара вернула не тот ключ, что был ей передан",keys.get(1),pair.getKey());
    }

    @Test
    public void testHasKeyTest() {
        KeyValuePair pair = new KeyValuePair(keys.get(1),values.get(1));
        Assert.assertTrue("Пара ключ-значение не распознала свой ключ", pair.hasKey(keys.get(1)));
        Assert.assertFalse("Пара ключ-значение ошибочно распознала не свой ключ", pair.hasKey(keys.get(2)));
        Assert.assertFalse("Пара ключ-значение с ключом не-null ошибочно распознала null", pair.hasKey(null));
        KeyValuePair pairNullKey = new KeyValuePair(null,values.get(0));
        Assert.assertTrue("Пара ключ-значение с ключом null не распознала свой ключ", pairNullKey.hasKey(null));
        Assert.assertFalse("Пара ключ-значение с ключом null  ошибочно распознала не свой ключ", pairNullKey.hasKey(keys.get(2)));

    }

    @Test
    public void testEqualsTest() {
        KeyValuePair pair1 = new KeyValuePair(keys.get(1),values.get(1));
        KeyValuePair pair2 = new KeyValuePair(keys.get(1),values.get(1));
        Assert.assertEquals("Две одинаковые пары ключ-значение не равны",pair1,pair2);
        Assert.assertEquals("Две одинаковые пары ключ-значение имеют разный хэш код",pair1.hashCode(),pair2.hashCode());
    }

    @Test
    public void testNotEqualsTest() {
        KeyValuePair pairSameValue1 = new KeyValuePair(keys.get(1),values.get(1));
        KeyValuePair pairSameValue2 = new KeyValuePair(keys.get(1),values.get(2));
        Assert.assertNotEquals("Две  пары ключ-значение c разными значениями равны",pairSameValue1,pairSameValue2);
        Assert.assertNotEquals("Две  пары ключ-значение c разными значениями имеют одинаковый хэш код",pairSameValue1.hashCode(),pairSameValue2.hashCode());

        KeyValuePair pairSameKey1 = new KeyValuePair(keys.get(1),values.get(1));
        KeyValuePair pairSameKey2 = new KeyValuePair(keys.get(2),values.get(1));
        Assert.assertNotEquals("Две  пары ключ-значение c разными ключами равны",pairSameKey1,pairSameKey2);
        Assert.assertNotEquals("Две  пары ключ-значение c разными ключами имеют одинаковый хэш код",pairSameKey1.hashCode(),pairSameKey2.hashCode());

    }

}