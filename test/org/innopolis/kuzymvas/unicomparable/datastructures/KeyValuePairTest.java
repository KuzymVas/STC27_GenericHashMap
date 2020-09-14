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
        final KeyValuePair pair = new KeyValuePair(keys.get(0), values.get(0));
        System.out.println("pair = " + pair);
    }

    @Test
    public void testGetValueTest() {
        final KeyValuePair pair = new KeyValuePair(keys.get(1), values.get(1));
        Assert.assertEquals("Pair returned different value compared to what was put into it", values.get(1), pair.getValue());
    }

    @Test
    public void testGetKeyTest() {
        final KeyValuePair pair = new KeyValuePair(keys.get(1), values.get(1));
        Assert.assertEquals("Pair returned different key compared to what was put into it", keys.get(1), pair.getKey());
    }

    @Test
    public void testHasKeyTest() {
        final KeyValuePair pair = new KeyValuePair(keys.get(1), values.get(1));
        Assert.assertTrue("Key-value pair was unable to accept its own key", pair.hasKey(keys.get(1)));
        Assert.assertFalse("Key-value pair accepted different key", pair.hasKey(keys.get(2)));
        Assert.assertFalse("Key-value pair with non-null key  accepted null key", pair.hasKey(null));
        final KeyValuePair pairNullKey = new KeyValuePair(null, values.get(0));
        Assert.assertTrue("Key-value pair with null key was unable to accept its own key", pairNullKey.hasKey(null));
        Assert.assertFalse("Key-value pair with null key accepted different non-null key", pairNullKey.hasKey(keys.get(2)));

    }

    @Test
    public void testEqualsTest() {
        final KeyValuePair pair1 = new KeyValuePair(keys.get(1), values.get(1));
        final KeyValuePair pair2 = new KeyValuePair(keys.get(1), values.get(1));
        Assert.assertEquals("Two same key-value pairs are not equal", pair1, pair2);
        Assert.assertEquals("Two same key-value pairs have different hashes", pair1.hashCode(), pair2.hashCode());
    }

    @Test
    public void testNotEqualsTest() {
        final KeyValuePair pairSameValue1 = new KeyValuePair(keys.get(1), values.get(1));
        final KeyValuePair pairSameValue2 = new KeyValuePair(keys.get(1), values.get(2));
        Assert.assertNotEquals("Two  key-value pairs with different values are equal", pairSameValue1, pairSameValue2);
        Assert.assertNotEquals("Two  key-value pairs with different values have same hash (possible weak hash function)", pairSameValue1.hashCode(), pairSameValue2.hashCode());

        final KeyValuePair pairSameKey1 = new KeyValuePair(keys.get(1), values.get(1));
        final KeyValuePair pairSameKey2 = new KeyValuePair(keys.get(2), values.get(1));
        Assert.assertNotEquals("Two  key-value pairs with different keys are equal", pairSameKey1, pairSameKey2);
        Assert.assertNotEquals("Two  key-value pairs with different keys have same hash (possible weak hash function)Д", pairSameKey1.hashCode(), pairSameKey2.hashCode());

    }

}