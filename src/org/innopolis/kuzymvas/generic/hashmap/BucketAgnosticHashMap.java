package org.innopolis.kuzymvas.generic.hashmap;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.generic.datastructures.KeyValuePair;

import java.util.*;

/**
 *  Класс хэш таблицы, независимой от реализации своих корзин
 * @param <K> - тип ключа таблицы
 * @param <V> - тип значения таблицы
 */
public class BucketAgnosticHashMap<K, V> implements Map<K, V> {

    private final List<Bucket<K, V>> buckets; // Массив корзин для каждого из возможных значений хэша
    private final EntrySet entrySet;
    private final KeySet keySet;
    private final ValueList valueList;
    private int size; // Число пар ключ-значение в хранилище

    /**
     * Создает хэш таблицу с 1024 корзинами
     *
     * @param factory - фабрика, которая снабдит таблицу нужным типом корзин. Не может быть null
     */
    public BucketAgnosticHashMap(BucketFactory<K, V> factory) {
        this(factory, 1024);
    }

    /**
     * Создаем хэш таблицу с заданным положительным числом корзин
     *
     * @param factory      - фабрика, которая снабдит таблицу нужным типом корзин. Не может быть null
     * @param bucketNumber - число корзин. Должно быть строго больше 0
     * @throws IllegalArgumentException - выбрасывается, если число корзин меньше, либо равно нулю.
     */
    public BucketAgnosticHashMap(BucketFactory<K, V> factory, int bucketNumber) throws IllegalArgumentException {
        size = 0;
        if (factory == null) {
            throw new IllegalArgumentException("Bucket factory can't be null");
        }
        if (bucketNumber <= 0) {
            throw new IllegalArgumentException("Hash map can't have 0 or less buckets");
        }
        buckets = factory.createBuckets(bucketNumber);
        entrySet = new EntrySet();
        keySet = new KeySet();
        valueList = new ValueList();
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        final int keyHash = getKeyBucket(key);
        return buckets.get(keyHash).containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return valueList.contains(value);
    }

    @Override
    public V get(Object key) {
        final int keyHash = getKeyBucket(key);
        try {
            return buckets.get(keyHash).get(key);
        } catch (KeyNotPresentException e) {
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        final int keyHash = getKeyBucket(key);
        V oldValue = null;
        if (buckets.get(keyHash).containsKey(key)) {
            try {
                oldValue = buckets.get(keyHash).get(key);
            } catch (KeyNotPresentException e) {
                e.printStackTrace();
            }
        }
        if (buckets.get(keyHash).put(key, value)) {
            entrySet.addFromMap(buckets.get(keyHash).getEntry(key));
            keySet.addFromMap(key);
            valueList.addFromMap(value);
            size++;
        } else {
            valueList.removeFromMap(oldValue);
            valueList.addFromMap(value);
        }
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        final int keyHash = getKeyBucket(key);
        try {
            Map.Entry<K, V> entry = buckets.get(keyHash).getEntry(key);
            if (entry == null) {
                return null;
            }
            buckets.get(keyHash).remove(key);
            entrySet.removeFromMap(entry);
            keySet.removeFromMap(key);
            valueList.removeFromMap(entry.getValue());
            size--;
            return entry.getValue();
        } catch (KeyNotPresentException e) {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Set<? extends Entry<? extends K, ? extends V>> entries = m.entrySet();
        for (Entry<? extends K, ? extends V> entry : entries) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        size = 0;
        for (Bucket<K, V> bucket : buckets) {
            bucket.clear();
        }
        entrySet.clearFromMap();
        keySet.clearFromMap();
        valueList.clearFromMap();
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public Collection<V> values() {
        return valueList;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entrySet;
    }

    @Override
    public V replace(K key, V value) {
        final int keyHash = getKeyBucket(key);
        try {
            final Entry<K, V> oldEntry = buckets.get(keyHash).getEntry(key);
            if (oldEntry != null) {
                entrySet.removeFromMap(oldEntry);
                V oldValue = oldEntry.getValue();
                buckets.get(keyHash).replace(key, value);
                entrySet.addFromMap(buckets.get(keyHash).getEntry(key));
                valueList.removeFromMap(oldValue);
                valueList.addFromMap(value);
                return oldValue;
            }
            return null;
        } catch (KeyNotPresentException e) {
            return null;
        }
    }

    /**
     * Возвращает хэш код для данной таблицы.
     * Ее хэш код зависит только от содержащихся в ней пар ключ-значение
     * и не зависит от порядка их добавления или типа корзин.
     *
     * @return - хэш код таблицы
     */
    @Override
    public int hashCode() {
        final int[] nodeHashes = new int[size]; // Массив хэшей по числу элементов
        int currPos = 0;
        for (Bucket<K, V> bucket : buckets) { // Перебираем ведра
            if (bucket != null) {
                final int[] bucketHashes = bucket.getKeyValuePairsHashes(); // Получаем все хэши в ведре
                System.arraycopy(bucketHashes, 0, nodeHashes, currPos, bucketHashes.length); // Пишем их в массив
                currPos += bucketHashes.length;
            }
        }
        Arrays.sort(nodeHashes); // Сортируем хэши, чтобы обеспечить независимость от порядка элементов в ведрах
        return Arrays.hashCode(nodeHashes); // Берем хэш сортированного массива хэшей.
    }

    /**
     * Проверяет эквиваленность данной хэш таблицы объекту.
     * Для эквивалентности объект должен реализовывать интерфейс Map.
     * и содержать в точности те же пары ключ-значение, что и эта таблица.
     * Внутренняя структура объекта не важна для эквивалентности.
     *
     * @param o - объект по отношению к которому проверяется эквивалетность
     * @return - true, если таблица и объект эквивалентны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        final Map<?, ?> that = (Map<?, ?>) o;
        if (this.size != that.size()) // разные размеры -> не равны
        {
            return false;
        }
        for (Bucket<K, V> bucket : buckets) { // Из каждого ведра
            List<KeyValuePair<K, V>> pairs = bucket.getKeyValuePairs();
            if (!that.entrySet().containsAll(pairs)) {
                return false;
            }
        }
        return true;// Если размеры совпали и каждая наша пара есть в другом -> равны
    }

    @Override
    public String toString() {
        final StringBuilder strB = new StringBuilder("SimpleHashMap{ size=").append(size).append(", buckets=");
        for (int i = 0; i < buckets.size(); i++) {
            strB.append("bucket[").append(i).append("]{");
            buckets.get(i).describeBucket(strB);
            strB.append((i < buckets.size() - 1) ? "}," : "}");
        }
        strB.append(" }");
        return strB.toString();
    }

    /**
     * Определяет номер корзины, соответствующей ключу
     *
     * @param key - ключ
     * @return номер корзины в массиве коризн
     */
    private int getKeyBucket(Object key) {

        return (key == null) ? 0 : Math.abs(key.hashCode()) % buckets.size();
    }

    /**
     * Класс множества пар ключ-значения, содержащихся в таблице
     * Множество связано с таблицей и удаление пары из него приводит к удалению пары из таблицы
     * Пары не могут быть добавлены в множество.
     */
    final class EntrySet extends HashSet<Map.Entry<K, V>> {

        @Override
        public boolean add(Entry<K, V> kvEntry) {
            throw new UnsupportedOperationException("Add is unsupported");
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            if (BucketAgnosticHashMap.this.containsKey(entry.getKey())) {
                BucketAgnosticHashMap.this.remove(entry.getKey());
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void clear() {
            BucketAgnosticHashMap.this.clear();
        }

        private void removeFromMap(Object o) {
            super.remove(o);
        }

        private void addFromMap(Entry<K, V> kvEntry) {
            super.add(kvEntry);
        }

        private void clearFromMap() {
            super.clear();
        }
    }

    /**
     * Класс множества ключей, содержащихся в таблице
     * Множество связано с таблицей и удаление ключа из него приводит к удалению пары с этим ключом из таблицы
     * Ключи не могут быть добавлены в множество.
     */
    final class KeySet extends HashSet<K> {

        @Override
        public boolean add(K key) {
            throw new UnsupportedOperationException("Add is unsupported");
        }

        @Override
        public boolean remove(Object o) {
            if (BucketAgnosticHashMap.this.containsKey(o)) {
                BucketAgnosticHashMap.this.remove(o);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void clear() {
            BucketAgnosticHashMap.this.clear();
        }

        private void removeFromMap(Object o) {
            super.remove(o);
        }

        private void addFromMap(K key) {
            super.add(key);
        }

        private void clearFromMap() {
            super.clear();
        }
    }

    /**
     * Класс коллекции значений, содержащихся в таблице
     * Коллекция связана с таблицей и его очистка приведет к удалению всех пар ключ-значение из таблицы
     * Значения не могут быть добавлены или удалены из множества.
     */
    final class ValueList extends LinkedList<V> {
        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException("Remove is unsupported");
        }

        @Override
        public boolean add(V value) {
            throw new UnsupportedOperationException("Add is unsupported");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Remove is unsupported");
        }

        @Override
        public boolean addAll(Collection<? extends V> c) {
            throw new UnsupportedOperationException("Add is unsupported");
        }

        @Override
        public void clear() {
            BucketAgnosticHashMap.this.clear();
        }

        private void removeFromMap(Object o) {
            super.remove(o);
        }

        private void addFromMap(V value) {
            super.add(value);
        }

        private void clearFromMap() {
            super.clear();
        }
    }
}
