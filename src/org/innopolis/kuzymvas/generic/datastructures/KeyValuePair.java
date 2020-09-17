package org.innopolis.kuzymvas.generic.datastructures;

import java.util.Map;
import java.util.Objects;

/**
 * Иммутабельная пара ключа и значения, позволяющая сравнения между парами
 * за счет ограничения типа ключа до сравнимого
 */
public class KeyValuePair<K, V> implements Map.Entry<K, V> {

    private final K key;
    private V value;

    /**
     * Создает новый иммутабельный объект ключ-значение
     *
     * @param key   - ключ нового объекта
     * @param value - значение нового объекта
     */
    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    /**
     * Проверяет совпадает ли данный ключ с ключом этого объекта
     *
     * @param key - проверяемый ключ
     * @return - true, если ключи совпадают, false в противном случае
     */
    public boolean hasKey(Object key) {
        return Objects.equals(this.key, key);
    }

    /**
     * Загружает описание ключа и значения в объекте в StringBuilder
     *
     * @param strB - StringBuilder, в который следует поместить описание ключа и значения
     */
    public void describeSelf(StringBuilder strB) {
        strB.append("{key=");
        strB.append(key);
        strB.append(", value=");
        strB.append(value);
        strB.append("}");
    }

    @Override
    public String toString() {
        final StringBuilder strB = new StringBuilder();
        describeSelf(strB);
        return "KeyValuePair" + strB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof  Map.Entry)) {
            return false;
        }
        final Map.Entry<?,?> that = (Map.Entry<?,?>)o;
        return (Objects.equals(key, that.getKey()) &&
                Objects.equals(value, that.getValue()));
    }

    @Override
    public int hashCode() {
        return  Objects.hashCode(key) ^ Objects.hashCode(value);
    }
}
