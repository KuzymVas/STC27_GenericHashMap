package org.innopolis.kuzymvas.unicomparable;

import org.junit.Assert;
import org.junit.Test;

public class UniComparableTest {

    @Test
    public void ComparisonTest() {
        final UniComparableToken token = new UniComparableToken();
        final UniComparableToken token2 = new UniComparableToken();
        final UniComparableToken token3 = new UniComparableToken();
        Assert.assertFalse("Earlier created token isn't lesser than one created later", token.greaterThan(token2));
        Assert.assertTrue("Later created token isn't greater than one created earlier", token3.greaterThan(token2));
        Assert.assertFalse("Same token isn't equal to itself in comparison", token3.greaterThan(token3));
    }


}