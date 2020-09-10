package org.innopolis.kuzymvas.hashmap;

import java.util.Arrays;
import java.util.Objects;

public class SimpleHashMap implements HashMap {

    private static class ListNode { // Класс узла списка, хранимого в каждой из корзин

        // Пара ключ и значение
        private final Object key;
        private Object value;
        // Ссылки для организации двусвязного списка
        private ListNode next;
        private ListNode prev;

        /**
         * Создает новый узел списка с заданной парой ключ-значение
         *
         * @param key   - ключ
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
         *
         * @param key   - ключ
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
         *
         * @param key - искомый ключ
         * @return - найденный узел или же null, если поиск не удался.
         */
        public ListNode findByKey(Object key) {
            if (Objects.equals(this.key, key)) {
                return this;
            } else if (this.next == null) {
                return null;
            } else return this.next.findByKey(key);
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
         *
         * @return - true, если у узла нет предшественника, иначе false
         */
        public boolean isHead() {
            return this.prev == null;
        }

        public Object getKey() {
            return key;
        }

        public ListNode getNext() {
            return next;
        }

        @Override
        public String toString() {
            StringBuilder strB = new StringBuilder();
            this.describeSelf(strB);
            return strB.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListNode listNode = (ListNode) o;
            return Objects.equals(key, listNode.key) &&
                    Objects.equals(value, listNode.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        /**
         * Добавляет описание(ключ и значение) этого элемента в StringBuilder
         *
         * @param strB - StringBuilder, в который нужно добавить описание
         */
        public void describeSelf(StringBuilder strB) {
            strB.append("{key=");
            if (key != null) {
                strB.append(key.toString());
            } else {
                strB.append("null");
            }
            strB.append(", value=");
            if (value != null) {
                strB.append(value.toString());
            } else {
                strB.append("null");
            }
            strB.append("}");
        }

        /**
         * Добавляет описание (ключ и значение) этого элемента и всех следующих за ним в StringBuilder
         *
         * @param strB - StringBuilder, в который следует добавить описания.
         */
        public void describeList(StringBuilder strB) {
            describeSelf(strB);
            if (this.next != null) {
                this.next.describeList(strB);
            }
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
     * Возвращает элемент из хранилища, содержащий пару с заданным ключем, или null, если ключ не найден
     *
     * @param key - искомый ключ
     * @return - элемент, хранящий пару с данным ключем или null, если ключ не найден
     */
    private ListNode findByKey(Object key) {
        int keyHash = (key == null) ? 0 : key.hashCode() % buckets.length;
        if (buckets[keyHash] == null) {
            return null;
        }
        return buckets[keyHash].findByKey(key);
    }

    @Override
    public void put(Object key, Object value) {
        int keyHash = (key == null) ? 0 : key.hashCode() % buckets.length;
        if (buckets[keyHash] == null) { // Если корзина пуста
            buckets[keyHash] = new ListNode(key, value);
            size++;
        } else {
            ListNode target = buckets[keyHash].findByKey(key); // Иначе проверяем есть ли в ней данный ключ
            if (target == null) { // Если ключа нет
                buckets[keyHash] = buckets[keyHash].putInFront(key, value);
                size++;
            } else { // Если он есть
                target.setValue(value);
            }
        }

    }

    @Override
    public void replace(Object key, Object value) throws KeyNotPresentException {
        ListNode target = findByKey(key);
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
        ListNode target = (buckets[keyHash] == null) ? null : buckets[keyHash].findByKey(key);
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

    @Override
    public String toString() {
        StringBuilder strB = new StringBuilder("SimpleHashMap{ size=").append(size).append(", buckets=");
        for (int i = 0; i < buckets.length; i++) {
            strB.append("bucket[").append(i).append("]{");
            buckets[i].describeList(strB);
            strB.append((i < buckets.length - 1) ? "}," : "}");
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
        for (ListNode bucket : buckets) { //  Для каждого ведра
            ListNode curr = bucket;
            while (curr != null) { // Пока не дошли до конца ведра
                Object key = curr.getKey(); // Берем ключ пары
                ListNode thatCurr = that.findByKey(key); // Ищем его в другом
                // Если не нашли или значение не совпало - не равны
                if ((thatCurr == null)
                        || (!Objects.equals(curr.getValue(), thatCurr.getValue()))) {
                    return false;
                }
                curr = curr.getNext(); // глубже идем в ведро
            }
        }
        return true; // Если размеры совпали и каждая наша пара есть в другом -> равны
    }

    @Override
    public int hashCode() {
        int[] nodeHashes = new int[size]; // Массив хэшей по числу элементов
        int currElemNum = 0;
        for (ListNode bucket : buckets) { // Перебираем ведра
            ListNode currElem = bucket;
            while (currElem != null) { // Перебираем элементы в ведре (если ведро пустое сразу идем дальше)
                nodeHashes[currElemNum] = currElem.hashCode(); // Пишем хэш элемента в массив
                currElemNum++;
                currElem = currElem.getNext();
            }
        }
        Arrays.sort(nodeHashes); // Сортируем хэши, чтобы обеспечить независимость от порядка элементов в ведрах
        return Arrays.hashCode(nodeHashes); // Берем хэш сортированного массива хэшей.
    }
}
