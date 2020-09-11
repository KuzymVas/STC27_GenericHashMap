package org.innopolis.kuzymvas.unicomparable;

import org.junit.Assert;
import org.junit.Test;

public class UniComparableContainerTest {

    @Test
    public void ContainerTest() {
        Object a = "test";
        UniComparableContainer container = new UniComparableContainer(a);
        Assert.assertEquals("Container returned different value from what was put in it",a,container.getValue());
        Assert.assertEquals("Container hash code did not correspond to that of its value",a.hashCode(),container.getValue().hashCode());
    }

    @Test
    public void EqualityTest() {
        Object a = "test";
        Object b = 42;
        UniComparableContainer containerA = new UniComparableContainer(a);
        UniComparableContainer containerA2 = new UniComparableContainer(a);
        UniComparableContainer containerB = new UniComparableContainer(b);
        Assert.assertNotEquals("Containers  of different values were equal",containerA, containerB);
        Assert.assertNotEquals("Different containers of same value were equal",containerA, containerA2);
        Assert.assertEquals("Container wasn't equal to self",containerA, containerA);
    }

    @Test
    public void ComparisonTest() {
        Object a = "test";
        Object b = 42;
        UniComparableContainer containerA = new UniComparableContainer(a);
        UniComparableContainer containerA2 = new UniComparableContainer(a);
        UniComparableContainer containerB = new UniComparableContainer(b);
        UniComparableToken tokenA = containerA.getComparableToken();
        UniComparableToken tokenA2 = containerA2.getComparableToken();
        UniComparableToken tokenACopy = containerA.getComparableToken();
        UniComparableToken tokenB = containerB.getComparableToken();
        Assert.assertFalse("Earlier created container isn't lesser than one created later", tokenA.greaterThan(tokenA2));
        Assert.assertTrue("Later created container isn't greater than one created earlier", tokenA2.greaterThan(tokenACopy));
        Assert.assertFalse("Same container isn't equal to itself in comparison", tokenA.greaterThan(tokenACopy));
        Assert.assertTrue("Later created container isn't greater than one created earlier\"", tokenB.greaterThan(tokenA));
        Assert.assertTrue("Later created container isn't greater than one created earlier\"", tokenB.greaterThan(tokenA2));
    }

}