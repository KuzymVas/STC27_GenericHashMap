package org.innopolis.kuzymvas.unicomparable;


public interface UniComparable {
    /**
     * Хранит объект-токен для сравнения литерала null с другими величинами.
     */
    UniComparableToken NULL_TOKEN = new  UniComparableToken();

    /**
     * Возвращает объект-токен, позволяющий сравнивать между собой любые объекты, реализующие данный интерфейс
     * @return - объект-токен, используемый в сравнении
     */
    UniComparableToken getComparableToken();
}
