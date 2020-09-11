package org.innopolis.kuzymvas.unicomparable;

public class UniComparator {

    static int compare(UniComparable first, UniComparable second) {
        UniComparableToken firstToken = first.getComparableToken();
        UniComparableToken secondToken = second.getComparableToken();
        if (firstToken.greaterThan(secondToken)) {
            return 1;
        } else if (secondToken.greaterThan(firstToken)) {
            return -1;
        } else {
            return 0;
        }
    }
}
