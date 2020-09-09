package org.innopolis.kuzymvas.hashmap;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.List;

public class SimpleHashMap implements HashMap{

    private class ListNode { // Класс узла списка, хранимого в каждой из корзин

        // Пара ключ и значение
        private Object key;
        private Object value;
        // Ссылки для организации двусвязного списка
        private ListNode next;
        private ListNode prev;

        /**
         * Создает новый узел списка с заданной парой ключ-значение
         * @param key - ключ
         * @param value - значение
         */
        public ListNode(Object key, Object value) {
            this.key = key;
            this.setValue(value);
            this.next = null;
            this.prev = null;
        }

        /**
         * Создает новый узел списка с заданной парой ключ-значение и вставляет его перед данным
         * @param key - ключ
         * @param value - значение
         * @return - вновь созданный узел списка
         */
        public ListNode putInFront(Object key, Object value) {
            ListNode newPrev = new ListNode(key, value);
            newPrev.next = this;
            if (this.prev != null) {
                this.prev.next = newPrev;
            }
            this.prev = newPrev;
            return newPrev;
        }

        /**
         * Односторонний поиск внутри списка  по ключую Возвращает либо узел списка с заданным ключем,
         * либо null, если такого узла не существует
         * @param key - искомый ключ
         * @return - найденный узел или же null, если поиск не удался.
         */
        public ListNode findByKey(Object key) {
            if (this.key == key) {
                return this;
            }
            else if (this.next == null) {
                return null;
            }
            else return this.next.findByKey(key);
        }

        /**
         * Удаляет данный узел из списка, замыкая его предшествующий и последующий узлы друг на друга.
         */
        public void removeSelf() {
            if (this.prev != null) {
                this.prev.next = this.next;
            }
            if (this.next != null) {
                this.next.prev = this.prev;
            }
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Проверяет является ли данный узел первым в списке
         * @return - true, если у узла нет предшественника, иначе false
         */
        public boolean isHead() {
            return this.prev == null;
        }

        public ListNode getNext() {
            return next;
        }


    }

    ListNode[] buckets; // Массив корзин для каждого из возможных значений хэша
    int size; // Число пар ключ-значение в хранилище

    /**
     * Создает хэш таблицу с 1024 корзинами
     */
    public SimpleHashMap() {
        size = 0;
        buckets = new ListNode[1024];
    }

    /**
     * Создаем хэш таблицу с заданным положительным числом корзин
     * @param bucketNumber - число корзин. Должно быть строго больше 0
     * @throws IllegalArgumentException - выбрасывается, если число корзин меньше, либо равно нулю.
     */
    public SimpleHashMap(int bucketNumber) throws IllegalArgumentException {
        size = 0;
        if (bucketNumber <= 0) throw new IllegalArgumentException("Hash map can't have 0 or less buckets");
        buckets = new ListNode[bucketNumber];
    }

    /**
     * Возвращает элемент из хранилища, содержащий пару с заданным ключем, или null, если ключ не найден
     * @param key - искомый ключ
     * @return - элемент, хранящий пару с данным ключем или null, если ключ не найден
     */
    private ListNode findByKey(Object key)  {
        int keyHash = (key == null) ? 0 : key.hashCode() % buckets.length;
        if (buckets[keyHash] == null) {
            return null;
        }
        return buckets[keyHash].findByKey(key);
    }

    @Override
    public void put(Object key, Object value) {
        int keyHash = (key == null) ? 0 : key.hashCode() % buckets.length;
        if (buckets[keyHash] == null) {
            buckets[keyHash] = new ListNode(key, value);
            size++;
        }
        else {
            ListNode target = buckets[keyHash].findByKey(key);
            if (target == null) {
                buckets[keyHash] = buckets[keyHash].putInFront(key, value);
                size++;
            }
            else {
                target.setValue(value);
            }
        }

    }

    @Override
    public void replace(Object key, Object value) throws KeyNotPresentException {
        ListNode target =findByKey(key);
        if (target == null) {
            throw new KeyNotPresentException("Key not found during replace attempt");
        }
        target.setValue(value);
    }

    @Override
    public Object get(Object key) throws KeyNotPresentException {
        ListNode target = findByKey(key);
        if (target == null) {
            throw new KeyNotPresentException("Key not found during get attempt");
        }
        return target.getValue();
    }

    @Override
    public Object remove(Object key) throws KeyNotPresentException {
        int keyHash = (key == null) ? 0 : key.hashCode() % buckets.length;
        ListNode target = (buckets[keyHash] == null) ? null :buckets[keyHash].findByKey(key);
        if (target == null) {
            throw new KeyNotPresentException("Key not found during get attempt");
        }
        if (target.isHead()) {
            buckets[keyHash] = target.getNext();
        }
        target.removeSelf();
        size--;
        return target.getValue();
    }

    @Override
    public boolean containsKey(Object key) {
        return (findByKey(key) != null);
    }

    @Override
    public int size() {
        return size;
    }
}
