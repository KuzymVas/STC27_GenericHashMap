package org.innopolis.kuzymvas.unicomparable.hashmap;

import org.innopolis.kuzymvas.unicomparable.datastructures.KeyValuePair;
import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;

public interface Bucket {

    /**
     * Помещает заданную пару ключ-знаение в корзину, если такого ключа в ней нет
     * Перезаписывает значение ключа данным, если он уже присустствует в корзине.
     *
     * @param key   - заданный ключ
     * @param value - значение для ключа
     * @return - true, если в корзину была добавлена новая пара, false, если было перезаписано значение.
     */
    boolean put(UniComparable key, Object value);

    /**
     * Заменяет значение, связанное с данным ключом в корзине, на данное.
     * Бросает исключение, если ключа  в корзине нет
     *
     * @param key   - заданный ключ
     * @param value - новое значение для ключа
     * @throws KeyNotPresentException - выбрасывается, если ключа нет в корзине
     */
    void replace(UniComparable key, Object value) throws KeyNotPresentException;

    /**
     * Возвращает значение, связанное с заданным ключом.
     * Бросает исключение, если ключа нет в корзине
     *
     * @param key - заданный ключ
     * @return - значение, связанное с ключом
     * @throws KeyNotPresentException - выбрасывается, если ключа нет в корзине
     */
    Object get(UniComparable key) throws KeyNotPresentException;

    /**
     * Убирает из корзины пару ключ-значение, определяемую заданным ключом
     * Бросает исключение, если ключа нет в корзине
     *
     * @param key - заданный ключ
     * @throws KeyNotPresentException - выбрасывается, если ключа нет в корзине
     */
    void remove(UniComparable key) throws KeyNotPresentException;

    /**
     * Проверяет, содержится ли заданный ключ в корзине
     *
     * @param key - заданный ключ
     * @return - true, если ключ содержится в корзине, false в противном случае
     */
    boolean containsKey(UniComparable key);

    /**
     * Проверяет, содержится ли заданная пара ключ-значение в корзине
     *
     * @param pair - заданная пара ключ-значение
     * @return -  true, если пара содержится в корзине, false в противном случае
     */
    boolean containsPair(KeyValuePair pair);

    /**
     * Добавляет описание всех содержащихся в корзине пар ключ-значение в StringBuilder
     *
     * @param strB - StringBuilder для добавления описаний
     */
    void describeBucket(StringBuilder strB);

    /**
     * Возвращает массив всех содержащихся в корзине пар ключ-значение
     *
     * @return - массив всех содержащихся в корзине пар ключ-значение
     */
    KeyValuePair[] getKeyValuePairs();

    /**
     * Возвращает массив хэшей всех содержащихся в корзине пар ключ-значение
     *
     * @return - массив хэшей всех содержащихся в корзине пар ключ-значение
     */
    int[] getKeyValuePairsHashes();
}
