package org.innopolis.kuzymvas.original.datastructures;

import java.util.Objects;

/**
 * Иммутабельная пара ключа и значения
 */
public class KeyValuePair {

    final private Object key;
    final private Object value;

    /**
     *  Создает новый иммутабельный объект ключ-значение
     * @param key - ключ нового объекта
     * @param value - значение нового объекта
     */
    public KeyValuePair(Object key, Object value) {
        this.key = key;
        this.value = value;
    }


    public Object getKey() {
        return key;
    }
    public Object getValue() {
        return value;
    }

    /**
     *  Проверяет совпадает ли данный ключ с ключом этого объекта
     * @param key - проверяемый ключ
     * @return - true, если ключи совпадают, false в противном случае
     */
    public boolean hasKey(Object key) {
        return Objects.equals(this.key, key);
    }

    /**
     * Загружает описание ключа и значения в объекте в StringBuilder
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
        StringBuilder strB = new StringBuilder();
        describeSelf(strB);
        return "KeyValuePair" + strB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValuePair that = (KeyValuePair) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
