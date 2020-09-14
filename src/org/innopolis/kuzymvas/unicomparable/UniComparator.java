package org.innopolis.kuzymvas.unicomparable;

public class UniComparator {

    /**
     * Сравнивает два объекта, реализующих UniComparable путем сравнения их токенов
     *
     * @param first  - первый объект для сравнения
     * @param second - второй объект для сравнения
     * @return - 1 - если первый объект больше второго, -1 - если второй больше первого,
     * 0 - если ни один из объектов не больше другого
     */
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
