package org.innopolis.kuzymvas.original.datastructures;

import org.junit.Assert;
import org.junit.Test;

public class KeyValuePairTest {


    @Test
    public void testToStringTest() {
        final KeyValuePair pair = new KeyValuePair("42", null);
        System.out.println("pair = " + pair);
    }

    @Test
    public void testGetValueTest() {
        final KeyValuePair pair = new KeyValuePair("a", "b");
        Assert.assertEquals("Key-value pair returned different value, that was provided to it", "b", pair.getValue());
    }

    @Test
    public void testGetKeyTest() {
        final KeyValuePair pair = new KeyValuePair("a", "b");
        Assert.assertEquals("Key-value pair returned different key, that was provided to it", "a", pair.getKey());
    }

    @Test
    public void testHasKeyTest() {
        final KeyValuePair pair = new KeyValuePair("a", "b");
        Assert.assertTrue("Key-value pair was unable to accept its own key", pair.hasKey("a"));
        Assert.assertFalse("Key-value pair accepted different key", pair.hasKey("b"));
        Assert.assertFalse("Key-value pair with non-null key  accepted null key", pair.hasKey(null));
        final KeyValuePair pairNullKey = new KeyValuePair(null, "b");
        Assert.assertTrue("Key-value pair with null key was unable to accept its own key", pairNullKey.hasKey(null));
        Assert.assertFalse("Key-value pair with null key accepted different non-null key", pairNullKey.hasKey("b"));

    }

    @Test
    public void testEqualsTest() {
        final KeyValuePair pair1 = new KeyValuePair("a", "b");
        final KeyValuePair pair2 = new KeyValuePair("a", "b");
        Assert.assertEquals("Two same key-value pairs are not equal", pair1, pair2);
        Assert.assertEquals("Two same key-value pairs have different hashes", pair1.hashCode(), pair2.hashCode());
    }

    @Test
    public void testNotEqualsTest() {
        final KeyValuePair pairSameValue1 = new KeyValuePair("a", "b");
        final KeyValuePair pairSameValue2 = new KeyValuePair("a", "c");
        Assert.assertNotEquals("Two  key-value pairs with different values are equal", pairSameValue1, pairSameValue2);
        Assert.assertNotEquals("Two  key-value pairs with different values have same hash (possible weak hash function)", pairSameValue1.hashCode(), pairSameValue2.hashCode());

        final KeyValuePair pairSameKey1 = new KeyValuePair("a", "b");
        final KeyValuePair pairSameKey2 = new KeyValuePair("c", "b");
        Assert.assertNotEquals("Two  key-value pairs with different keys are equal", pairSameKey1, pairSameKey2);
        Assert.assertNotEquals("Two  key-value pairs with different keys have same hash (possible weak hash function)Д", pairSameKey1.hashCode(), pairSameKey2.hashCode());

    }

}