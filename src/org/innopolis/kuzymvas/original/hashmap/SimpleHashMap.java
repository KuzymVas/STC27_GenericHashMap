package org.innopolis.kuzymvas.original.hashmap;

import org.innopolis.kuzymvas.original.datastructures.KeyValuePair;
import org.innopolis.kuzymvas.original.datastructures.ListNode;
import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;

import java.util.Arrays;

public class SimpleHashMap implements HashMap {

    ListNode[] buckets; // Массив корзин для каждого из возможных значений хэша
    int size; // Число пар ключ-значение в хранилище

    /**
     * Создает хэш таблицу с 1024 корзинами
     */
    public SimpleHashMap() {
        this(1024);
    }

    /**
     * Создаем хэш таблицу с заданным положительным числом корзин
     *
     * @param bucketNumber - число корзин. Должно быть строго больше 0
     * @throws IllegalArgumentException - выбрасывается, если число корзин меньше, либо равно нулю.
     */
    public SimpleHashMap(int bucketNumber) throws IllegalArgumentException {
        size = 0;
        if (bucketNumber <= 0) throw new IllegalArgumentException("Hash map can't have 0 or less buckets");
        buckets = new ListNode[bucketNumber];
    }

    /**
     * Определяет номер корзины, соответствующей ключу
     *
     * @param key - ключ
     * @return номер корзины в массиве коризн
     */
    private int getKeyBucket(Object key) {

        return (key == null) ? 0 : key.hashCode() % buckets.length;
    }


    @Override
    public void put(Object key, Object value) {
        int keyHash = getKeyBucket(key);
        if (buckets[keyHash] == null) { // Если корзина пуста
            buckets[keyHash] = new ListNode(key, value);
            size++;
        } else {
            if (buckets[keyHash].putIntoList(key, value)) {
                size++;
            }
        }

    }

    @Override
    public void replace(Object key, Object value) throws KeyNotPresentException {
        int keyHash = getKeyBucket(key);
        try {
            if (buckets[keyHash] != null) {
                buckets[keyHash].replaceValue(key, value);
            } else {
                throw new KeyNotPresentException("Key not found in hash map during replace attempt. Key value: " + key);
            }
        } catch (KeyNotPresentException e) {
            throw new KeyNotPresentException("Key not found in hash map during replace attempt. Key value: " + key);
        }
    }

    @Override
    public Object get(Object key) throws KeyNotPresentException {
        int keyHash = getKeyBucket(key);
        try {
            if (buckets[keyHash] != null) {
                return buckets[keyHash].getValue(key);
            } else {
                throw new KeyNotPresentException("Key not found in hash map during get attempt. Key value: " + key);
            }
        } catch (KeyNotPresentException e) {
            throw new KeyNotPresentException("Key not found in hash map during get attempt. Key value: " + key);
        }
    }

    @Override
    public Object remove(Object key) throws KeyNotPresentException {
        int keyHash = getKeyBucket(key);
        try {
            if (buckets[keyHash] != null) {
                Object value = buckets[keyHash].getValue(key);
                buckets[keyHash] = buckets[keyHash].removeFromList(key);
                size--;
                return value;
            } else {
                throw new KeyNotPresentException("Key not found in hash map during remove attempt. Key value: " + key);
            }
        } catch (KeyNotPresentException e) {
            throw new KeyNotPresentException("Key not found in hash map during remove attempt. Key value: " + key);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        int keyHash = getKeyBucket(key);
        return buckets[keyHash] != null && buckets[keyHash].containsKey(key);
    }

    @Override
    public boolean containsPair(KeyValuePair pair) {
        int keyHash = getKeyBucket(pair.getKey());
        return buckets[keyHash] != null && buckets[keyHash].containsPair(pair);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder strB = new StringBuilder("SimpleHashMap{ size=").append(size).append(", buckets=");
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                strB.append("bucket[").append(i).append("]{");
                buckets[i].describeList(strB);
                strB.append((i < buckets.length - 1) ? "}," : "}");
            }
        }
        strB.append(" }");
        return strB.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleHashMap that = (SimpleHashMap) o;
        if (this.size != that.size) // разные размеры -> не равны
            return false;
        for (ListNode bucket: buckets) { // Из каждого ведра
            if (bucket != null) {
                KeyValuePair[] pairs = bucket.getKeyValuePairs();
                for (KeyValuePair pair: pairs) { // Каждую пару, что в нем есть
                    if (!that.containsPair(pair)) { // Ищем в другом
                        return false;
                    }
                }
            }
        }
        return true;// Если размеры совпали и каждая наша пара есть в другом -> равны
    }

    @Override
    public int hashCode() {
        int[] nodeHashes = new int[size]; // Массив хэшей по числу элементов
        int currPos = 0;
        for (ListNode bucket : buckets) { // Перебираем ведра
            if (bucket != null) {
                int[] bucketHashes = bucket.getKeyValuePairsHashes(); // Получаем все хэши в ведре
                System.arraycopy(bucketHashes, 0, nodeHashes, currPos, bucketHashes.length); // Пишем их в массив
                currPos += bucketHashes.length;
            }
        }
        Arrays.sort(nodeHashes); // Сортируем хэши, чтобы обеспечить независимость от порядка элементов в ведрах
        return Arrays.hashCode(nodeHashes); // Берем хэш сортированного массива хэшей.
    }
}
