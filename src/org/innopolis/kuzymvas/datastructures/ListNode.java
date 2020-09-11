package org.innopolis.kuzymvas.datastructures;

import org.innopolis.kuzymvas.hashmap.KeyNotPresentException;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс узла списка - обертки вокруг пары ключ-значение.
 */
public class ListNode {
    KeyValuePair pair;
    // Ссылка для организации двусвязного списка
    private ListNode next;

    /**
     * Создает новый узел списка (и тем самым новый список из одного элемента), содержащий заданные ключ и значение.
     * @param key - ключ, хранимый в узле списка
     * @param value - значение, хранимое в узле списка
     */
    public ListNode(Object key, Object value) {
        this.pair = new KeyValuePair(key, value);
    }

    /**
     * Вставляет в список, начинающийся с данного узла, новый узел, содержащий заданную пару ключ-значение
     * @param key - ключ, хранимый в добавляемом узле списка
     * @param value - значение, хранимое в добавляемом узле списка
     * @return - новая голова списка.
     */
    public ListNode insertIntoList(Object key, Object value) {
        ListNode newNode = new ListNode(key,value);
        newNode.next = this;
        return newNode;
    }

    /**
     * Удаляет из списка, начинающегося с данного узла с заданным ключем
     * @param key - ключ удаляемого узла
     * @return - новая голова списка.
     * @throws KeyNotPresentException - выбрасывается, если в списке нет узла с заданным ключем.
     */
    public ListNode removeFromList(Object key) throws KeyNotPresentException {
        if (pair.hasKey(key)) {
            return this.next;
        }
        else {
            if (this.next != null) {
                this.next = this.next.removeFromList(key);
                return this;
            }
            else {
                throw new KeyNotPresentException("Specified to be removed key do not exist on the list");
            }
        }
    }

    /**
     * Проверяет наличие заданного ключа в списке, начинающегося с данного.
     * @param key - искомый ключ
     * @return  - true, если ключ хранится в одном из узлов списка, false в противном случае
     */
    public boolean containsKey(Object key) {
        if (pair.hasKey(key)) {
            return true;
        }
        if (next != null) {
            return next.containsKey(key);
        }
        else {
            return false;
        }
    }

    /**
     * Заменяет значение, хранимое в узле с заданным ключом
     * @param key - ключ изменяемого узла
     * @param value - новое значение
     * @throws KeyNotPresentException - выбрасывается, если в списке нет узла с заданным ключем.
     */
    public void replaceValue(Object key, Object value)  throws KeyNotPresentException {
        if (pair.hasKey(key)) {
            pair = new KeyValuePair(key, value);
            return;
        }
        if (next != null) {
            next.replaceValue(key, value);
        }
        else {
            throw new KeyNotPresentException("Specified for value replacement key do not exist on the list");
        }
    }
    /**
     * Возвращает значение, хранимое в узле с заданным ключом
     * @param key - ключ изменяемого узла
     * @throws KeyNotPresentException - выбрасывается, если в списке нет узла с заданным ключем.
     */
    public Object getValue(Object key) throws KeyNotPresentException {
        if (pair.hasKey(key)) {
            return pair.getValue();
        }
        if (next != null) {
            return next.getValue(key);
        }
        else {
            throw new KeyNotPresentException("Specified to be retrieved key do not exist on the list");
        }
    }

    /**
     * Возвращает массив хэшей всех пар ключ-значение, хранимых в списке, начинающемся с данного узла
     * @return - массив хэшей пар ключ-значение, минимум 1 хэш.
     */
    public int[] getKeyValuePairsHashes() {
        if (next != null) {
            int[] nextHashes = next.getKeyValuePairsHashes();
            int[] hashes = Arrays.copyOf(nextHashes, nextHashes.length + 1);
            hashes[hashes.length - 1] = pair.hashCode();
            return hashes;
        }
        else {
            int[] hashes = new int[1];
            hashes[0] = pair.hashCode();
            return hashes;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListNode listNode = (ListNode) o;
        return pair.equals(listNode.pair) && Objects.equals(this.next, listNode.next);
    }

    @Override
    public int hashCode() {
        int[] hashes = getKeyValuePairsHashes();
        Arrays.sort(hashes);
        return Arrays.hashCode(hashes);
    }
}
