package org.innopolis.kuzymvas.generic.datastructures;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Класс узла списка - обертки вокруг пары ключ-значение.
 *
 * @param <K> - тип ключа
 * @param <V> - тип значения
 */
public class ListNode<K, V> {

    private final KeyValuePair<K, V> pair;
    private ListNode<K, V> next; // Ссылка для организации односвязного списка
    private long height; // Высота списка

    /**
     * Создает новый узел списка (и тем самым новый список из одного элемента), содержащий заданные ключ и значение.
     *
     * @param key   - ключ, хранимый в узле списка
     * @param value - значение, хранимое в узле списка
     */
    public ListNode(K key, V value) {
        height = 1L;
        this.pair = new KeyValuePair<>(key, value);
    }

    /**
     * Если ключ отсутствует в списке, начинающемся с данного узла,
     * то вставляет в этот список новый узел, содержащий заданную пару ключ-значение
     * Если заданный ключ уже есть в списке, то заменяет соответствуюзеее ему значение.
     *
     * @param key   - ключ, хранимый в добавляемом узле списка
     * @param value - значение, хранимое в добавляемом узле списка
     * @return - true, если в список был добавлен новый узел, false - если было заменено значение
     * и размер списка не изменился.
     */
    public boolean putIntoList(K key, V value) {
        if (pair.hasKey(key)) {
            pair.setValue(value);
            return false;
        }
        if (next != null) {
            boolean res = next.putIntoList(key, value);
            if (res) {
                height++;
            }
            return res;
        } else {
            next = new ListNode<>(key, value);
            height++;
            return true;
        }
    }

    /**
     * Удаляет из списка, начинающегося с данного узла, узелс заданным ключем
     *
     * @param key - ключ удаляемого узла
     * @return - новая голова списка.
     * @throws KeyNotPresentException - выбрасывается, если в списке нет узла с заданным ключем.
     */
    public ListNode<K, V> removeFromList(Object key) throws KeyNotPresentException {
        if (pair.hasKey(key)) {
            height--;
            return this.next;
        } else {
            if (this.next != null) {
                this.next = this.next.removeFromList(key);
                height--;
                return this;
            } else {
                throw new KeyNotPresentException("Specified to be removed key do not exist on the list");
            }
        }
    }

    /**
     * Проверяет наличие заданного ключа в списке, начинающемся с данного узла.
     *
     * @param key - искомый ключ
     * @return - true, если ключ хранится в одном из узлов списка, false в противном случае
     */
    public boolean containsKey(Object key) {
        if (pair.hasKey(key)) {
            return true;
        }
        if (next != null) {
            return next.containsKey(key);
        } else {
            return false;
        }
    }

    /**
     * Проверяет наличие заданной пары ключ-значение в списке, начинающемся с данного узла.
     *
     * @param pair - искоммая пара ключ-значение
     * @return - true, если пара хранится в одном из узлов списка, false в противном случае
     */
    public boolean containsPair(KeyValuePair<?, ?> pair) {
        if (this.pair.equals(pair)) {
            return true;
        }
        if (next != null) {
            return next.containsPair(pair);
        } else {
            return false;
        }
    }

    /**
     * Заменяет значение, хранимое в узле с заданным ключом, если он есть в списке,
     * начинающемся с данного узла
     *
     * @param key   - ключ изменяемого узла
     * @param value - новое значение
     * @throws KeyNotPresentException - выбрасывается, если в списке нет узла с заданным ключем.
     */
    public void replaceValue(K key, V value) throws KeyNotPresentException {
        if (pair.hasKey(key)) {
            pair.setValue(value);
            return;
        }
        if (next != null) {
            next.replaceValue(key, value);
        } else {
            throw new KeyNotPresentException("Specified for value replacement key do not exist on the list");
        }
    }

    /**
     * Возвращает значение, хранимое в узле с заданным ключом, если он есть в списке,
     * начинающемся с данного узла
     *
     * @param key - ключ изменяемого узла
     * @throws KeyNotPresentException - выбрасывается, если в списке нет узла с заданным ключем.
     */
    public V getValue(Object key) throws KeyNotPresentException {
        if (pair.hasKey(key)) {
            return pair.getValue();
        }
        if (next != null) {
            return next.getValue(key);
        } else {
            throw new KeyNotPresentException("Specified to be retrieved key do not exist on the list");
        }
    }

    /**
     * Возвращает пару ключ-значение, которая хранится в узле с заданным ключом
     *
     * @param key - искомый ключ
     * @return - пара ключ-значение
     */
    public Map.Entry<K, V> getEntry(Object key) {
        if (pair.hasKey(key)) {
            return pair;
        }
        if (next != null) {
            return next.getEntry(key);
        } else {
            return null;
        }
    }

    /**
     * Возвращает массив всех пар ключ-значение, хранимых в списке, начинающемся с данного узла
     *
     * @return - массив  пар ключ-значение, минимум 1 пара.
     */
    public List<KeyValuePair<K, V>> getKeyValuePairs() {
        List<KeyValuePair<K, V>> pairs = new ArrayList<>();
        if (next != null) {
            pairs.addAll(next.getKeyValuePairs());
        }
        pairs.add(pair);
        return pairs;
    }

    /**
     * Возвращает массив хэшей всех пар ключ-значение, хранимых в списке, начинающемся с данного узла
     *
     * @return - массив хэшей пар ключ-значение, минимум 1 хэш.
     */
    public int[] getKeyValuePairsHashes() {
        final List<KeyValuePair<K, V>> pairs = getKeyValuePairs();
        final int[] hashes = new int[pairs.size()];
        for (int i = 0; i < pairs.size(); i++) {
            hashes[i] = pairs.get(i).hashCode();
        }
        return hashes;
    }

    /**
     * Добавляет описание (ключ и значение) этого элемента и всех следующих за ним в списке
     * в StringBuilder
     *
     * @param strB - StringBuilder, в который следует добавить описания.
     */
    public void describeList(StringBuilder strB) {
        pair.describeSelf(strB);
        if (this.next != null) {
            strB.append(",");
            this.next.describeList(strB);
        }
    }

    @Override
    public int hashCode() {
        final int[] hashes = getKeyValuePairsHashes();
        Arrays.sort(hashes);
        return Arrays.hashCode(hashes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListNode<?, ?>)) {
            return false;
        }
        final ListNode<?, ?> listNode = (ListNode<?, ?>) o;
        if (this.height != listNode.height) { // Если у списков разная длина - не равны
            return false;
        }
        return isSubListOf(listNode); // Если длина одинакова, то вхождение равносильно эквивалентности.
    }

    @Override
    public String toString() {
        final StringBuilder strB = new StringBuilder();
        describeList(strB);
        return "List{" + strB + "}";
    }

    /**
     * Проверяет является ли логически данный список подмножеством другого:
     * т. е. входят ли все элементы данного списка и в другой тоже.
     *
     * @param listNode - голова другого списка
     * @return - true, если все элементы данного списка входят в другой, false в обратном случае
     */
    private boolean isSubListOf(ListNode<?, ?> listNode) {
        final List<KeyValuePair<K, V>> pairs = getKeyValuePairs();
        for (KeyValuePair<K, V> pair : pairs) {
            if (!listNode.containsPair(pair)) {
                return false;
            }
        }
        return true;
    }
}
