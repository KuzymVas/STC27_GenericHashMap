package org.innopolis.kuzymvas.unicomparable;

public class UniComparator {

    public static int compare(UniComparable first, UniComparable second) {
        final UniComparableToken firstToken = first.getComparableToken();
        final UniComparableToken secondToken = second.getComparableToken();
        if (firstToken.greaterThan(secondToken)) {
            return 1;
        } else if (secondToken.greaterThan(firstToken)) {
            return -1;
        } else {
            return 0;
        }
    }
}
