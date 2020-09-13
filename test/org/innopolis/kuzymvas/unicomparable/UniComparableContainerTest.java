package org.innopolis.kuzymvas.unicomparable;

import org.junit.Assert;
import org.junit.Test;

public class UniComparableContainerTest {

    @Test
    public void ContainerTest() {
        final Object a = "test";
        final UniComparableContainer container = new UniComparableContainer(a);
        Assert.assertEquals("Container returned different value from what was put in it",a,container.getValue());
        Assert.assertEquals("Container hash code did not correspond to that of its value",a.hashCode(),container.getValue().hashCode());
    }

    @Test
    public void EqualityTest() {
        final Object a = "test";
        final Object b = 42;
        final UniComparableContainer containerA = new UniComparableContainer(a);
        final UniComparableContainer containerA2 = new UniComparableContainer(a);
        final UniComparableContainer containerB = new UniComparableContainer(b);
        Assert.assertNotEquals("Containers  of different values were equal",containerA, containerB);
        Assert.assertNotEquals("Different containers of same value were equal",containerA, containerA2);
        Assert.assertEquals("Container wasn't equal to self",containerA, containerA);
    }

    @Test
    public void ComparisonTest() {
        final Object a = "test";
        final Object b = 42;
        final UniComparableContainer containerA = new UniComparableContainer(a);
        final UniComparableContainer containerA2 = new UniComparableContainer(a);
        final UniComparableContainer containerB = new UniComparableContainer(b);
        final UniComparableToken tokenA = containerA.getComparableToken();
        final UniComparableToken tokenA2 = containerA2.getComparableToken();
        final UniComparableToken tokenACopy = containerA.getComparableToken();
        final UniComparableToken tokenB = containerB.getComparableToken();
        Assert.assertFalse("Earlier created container isn't lesser than one created later", tokenA.greaterThan(tokenA2));
        Assert.assertTrue("Later created container isn't greater than one created earlier", tokenA2.greaterThan(tokenACopy));
        Assert.assertFalse("Same container isn't equal to itself in comparison", tokenA.greaterThan(tokenACopy));
        Assert.assertTrue("Later created container isn't greater than one created earlier\"", tokenB.greaterThan(tokenA));
        Assert.assertTrue("Later created container isn't greater than one created earlier\"", tokenB.greaterThan(tokenA2));
    }

}