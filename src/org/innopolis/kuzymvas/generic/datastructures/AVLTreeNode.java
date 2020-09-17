package org.innopolis.kuzymvas.generic.datastructures;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;

import java.util.*;

/**
 * Класс узла АВЛ-дерева, хранящего пару ключ-значение.
 * Имеет возможность сравниваться с другими узлами за счет ограничения
 * типа ключа до сравнимого.
 */
public class AVLTreeNode<K,V> {

    private KeyValuePair<K,V> pair;
    private int height; // Высота узла в дереве (у листьев = 1)
    private AVLTreeNode<K,V> left; // Ссылки на ветви дерева
    private AVLTreeNode<K,V> right;
    private final Comparator<K> comparator;

    /**
     * Создает новый узел дерева (и  корень дерева из одного узла) с заданной парой ключ-значение
     *
     * @param key   - ключ, хранимый в узле дерева
     * @param value - значение, хранимое в узле дерева
     */
    public AVLTreeNode(K key, V value) {
        height = 1;
        left = null;
        right = null;
        pair = new KeyValuePair<>(key, value);
        comparator = null;
    }

    /**
     * Создает новый узел дерева (и  корень дерева из одного узла) с заданной парой ключ-значение
     *
     * @param key   - ключ, хранимый в узле дерева
     * @param value - значение, хранимое в узле дерева
     */
    public AVLTreeNode(K key, V value, Comparator<K> comparator)  {
        height = 1;
        left = null;
        right = null;
        pair = new KeyValuePair<>(key, value);
        this.comparator = comparator;
    }

    /**
     * Возвращает максимальный по модулю показатель сбалансированности среди узлов дерева
     *
     * @return -  максимальный по модулю показатель сбалансированноси в дереве
     */
    public int getMaxAbsTreeBalance() {
        int balance = getBalance();
        if (left != null) {
            final int leftBalance = left.getMaxAbsTreeBalance();
            if (Math.abs(balance) < Math.abs(leftBalance)) {
                balance = leftBalance;
            }
        }
        if (right != null) {
            final int rightBalance = right.getMaxAbsTreeBalance();
            if (Math.abs(balance) < Math.abs(rightBalance)) {
                balance = rightBalance;
            }
        }
        return balance;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Если ключ отсутствует в дереве, начинающемся с данного узла,
     * то вставляет в это дерево новый узел, содержащий заданную пару ключ-значение
     * Если заданный ключ уже есть в дереве, то заменяет соответствуюзеее ему значение.
     *
     * @param key   - ключ, добавляемый в дерево
     * @param value - значение, добавляемое в дерево
     * @return - новый корень дерева
     */
    public AVLTreeNode<K,V> insert(K key, V value) {
        final int comparison = getComparison(this.pair.getKey(), key);
        if (comparison > 0) {
            if (right == null) {
                right = new AVLTreeNode<>(key, value);
            } else {
                right = right.insert(key, value);
            }
        } else if (comparison < 0) {
            if (left == null) {
                left = new AVLTreeNode<>(key, value);
            } else {
                left = left.insert(key, value);
            }
        } else {
            this.pair = new KeyValuePair<>(key, value);
        }
        return rebalance(this);
    }

    /**
     * Удаляет из дерева, начинающегося с данного узла узел с заданным ключем
     *
     * @param key - ключ удаляемого узла
     * @return - новый корень дерева
     * @throws KeyNotPresentException - выбрасывается, если в дереве нет узла с заданным ключем.
     */
    public AVLTreeNode<K,V>  remove(Object key) throws KeyNotPresentException {
        final int comparison = getComparison(this.pair.getKey(), key);
        if (comparison > 0) {
            if (right == null) {
                throw new KeyNotPresentException("Specified to be removed key do not exist on the tree");
            }
            right = right.remove(key);
        } else if (comparison < 0) {
            if (left == null) {
                throw new KeyNotPresentException("Specified to be removed key do not exist on the tree");
            }
            left = left.remove(key);
        } else {
            if (left == null || right == null) {
                return (left == null) ? right : left;
            }
            AVLTreeNode<K,V>  leftmostChildOnRight = right.getLeftmostChild();
            this.pair = leftmostChildOnRight.pair;
            right = right.remove(leftmostChildOnRight.pair.getKey());
        }
        return rebalance(this);
    }

    /**
     * Возвращает значение, хранимое в узле дерева с заданным ключом
     *
     * @param key - ключ искомого узла
     * @throws KeyNotPresentException - выбрасывается, если в списке нет узла с заданным ключем.
     */
    public V getValue(Object key) throws KeyNotPresentException {
        final int comparison = getComparison(this.pair.getKey(), key);
        if (comparison > 0) {
            if (right == null) {
                throw new KeyNotPresentException("Specified to be retrieved key do not exist on the tree");
            }
            return right.getValue(key);
        } else if (comparison < 0) {
            if (left == null) {
                throw new KeyNotPresentException("Specified to be retrieved key do not exist on the tree");
            }
            return left.getValue(key);
        } else {
            return pair.getValue();
        }
    }

    /**
     * Заменяет значение, хранимое в узле дерева с заданным ключом
     *
     * @param key   - ключ изменяемого узла
     * @param value - новое значение
     * @throws KeyNotPresentException - выбрасывается, если в дереве нет узла с заданным ключем.
     */
    public void replaceValue(K key, V value) throws KeyNotPresentException {
        final int comparison = getComparison(this.pair.getKey(), key);
        if (comparison > 0) {
            if (right == null) {
                throw new KeyNotPresentException("Specified for value replacement key do not exist on the tree");
            }
            right.replaceValue(key, value);
        } else if (comparison < 0) {
            if (left == null) {
                throw new KeyNotPresentException("Specified for value replacement key do not exist on the tree");
            }
            left.replaceValue(key, value);
        } else {
            this.pair.setValue(value);
        }
    }

    /**
     * Проверяет наличие заданного ключа в дереве, начинающегося с данного корня.
     *
     * @param key - искомый ключ
     * @return - true, если ключ хранится в одном из узлов дерева, false в противном случае
     */
    public boolean containsKey(Object key) {
        final int comparison = getComparison(this.pair.getKey(), key);
        if (comparison > 0) {
            if (right == null) {
                return false;
            }
            return right.containsKey(key);
        } else if (comparison < 0) {
            if (left == null) {
                return false;
            }
            return left.containsKey(key);
        } else {
            return true;
        }
    }

    /**
     * Проверяет наличие заданной пары ключ-значение в дереве, начинающегося с данного узла.
     *
     * @param pair - искоммая пара ключ-значение
     * @return - true, если пара хранится в одном из узлов дерева, false в противном случае
     */
    public boolean containsPair(KeyValuePair<?,?> pair) {
        final int comparison = getComparison(this.pair.getKey(), (K)pair.getKey());
        if (comparison > 0) {
            if (right == null) {
                return false;
            }
            return right.containsPair(pair);
        } else if (comparison < 0) {
            if (left == null) {
                return false;
            }
            return left.containsPair(pair);
        } else {
            return this.pair.equals(pair);
        }
    }

    /**
     * Возвращает список всех пар ключ-значение, хранимых в дереве, начинающемся с данного узла
     *
     * @return - список пар ключ-значение, минимум 1 пара.
     */
    public List<KeyValuePair<K,V>> getKeyValuePairs() {

        List<KeyValuePair<K,V>> pairs = new ArrayList<>();
        if (left != null) {
            pairs.addAll(left.getKeyValuePairs());
        }
        pairs.add(this.pair);
        if (right != null) {
            pairs.addAll(right.getKeyValuePairs());
        }
        return pairs;
    }

    /**
     * Возвращает массив хэшей всех пар ключ-значение, хранимых в дереве, начинающемся с данного узла
     *
     * @return - массив хэшей пар ключ-значение, минимум 1 хэш.
     */
    public int[] getKeyValuePairsHashes() {
        final List<KeyValuePair<K,V>> pairs = getKeyValuePairs();
        final int[] hashes = new int[pairs.size()];
        for (int i = 0; i < pairs.size(); i++) {
            hashes[i] = pairs.get(i).hashCode();
        }
        return hashes;
    }

    /**
     * Добавляет описание (ключ и значение) этого элемента и всех элементов в его поддеревьях в StringBuilder
     *
     * @param strB - StringBuilder, в который следует добавить описания.
     */
    public void describeTree(StringBuilder strB) {
        if (left != null) {
            left.describeTree(strB);
        }
        pair.describeSelf(strB);
        if (right != null) {
            right.describeTree(strB);
        }
    }

    @Override
    public String toString() {
        final StringBuilder strB = new StringBuilder();
        describeTree(strB);
        return "AVLTree{" + strB + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof  AVLTreeNode<?,?>)) {
            return false;
        }
        final AVLTreeNode<?,?> that = (AVLTreeNode<?,?>) o;
        if (height != that.height) {
            return false;
        }
        return this.isSubTreeOf(that) && that.isSubTreeOf(this);
    }

    @Override
    public int hashCode() {
        final int[] hashes = getKeyValuePairsHashes();
        Arrays.sort(hashes);
        return Arrays.hashCode(hashes);
    }


    private int getComparison(K ourKey, Object otherKey) {
        if (comparator != null) {
            return comparator.compare(ourKey, (K)otherKey);
        }
        else {
            return ((Comparable<K>) ourKey).compareTo((K)otherKey);
        }
    }


    /**
     * Проверяет является ли логически данное дерево подмножеством другого:
     * т. е. входят ли все элементы данного дерева и в другое тоже.
     *
     * @param treeNode - корень другого деревого
     * @return - true, если все элементы данного дерева входят в другое, false в обратном случае
     */
    private boolean isSubTreeOf(AVLTreeNode<?,?> treeNode) {
        final List<KeyValuePair<K,V>> pairs = getKeyValuePairs();
        for (KeyValuePair<K,V> pair : pairs) {
            if (!treeNode.containsPair(pair)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает высоту левого поддерва
     *
     * @return - высота левого поддерева >=0
     */
    private int getLeftHeight() {
        return (left == null) ? 0 : left.getHeight();
    }

    /**
     * Возвращает высоту правого поддерва
     *
     * @return - высота правого поддерева >=0
     */
    private int getRightHeight() {
        return (right == null) ? 0 : right.getHeight();
    }

    /**
     * Выполняет левый поворот дерева, начинающегося с данной вершины.
     *
     * @param node - корень исходного дерева
     * @return - новый корень дерева после поворота
     */
    private static <K,V> AVLTreeNode<K,V> rotateLeft(AVLTreeNode<K,V> node) {
        AVLTreeNode<K,V> rightNode = node.right;
        AVLTreeNode<K,V> leftRightNode = rightNode.left;
        rightNode.left = node;
        node.right = leftRightNode;
        node.updateHeight();
        rightNode.updateHeight();
        return rightNode;
    }

    /**
     * Выполняет правый поворот дерева, начинающегося с данной вершины.
     *
     * @param node - корень исходного дерева
     * @return - новый корень дерева после поворота
     */
    private static <K,V> AVLTreeNode<K,V> rotateRight(AVLTreeNode<K,V> node) {
        AVLTreeNode<K,V> leftNode = node.left;
        AVLTreeNode<K,V> rightLeftNode = leftNode.right;
        leftNode.right = node;
        node.left = rightLeftNode;
        node.updateHeight();
        leftNode.updateHeight();
        return leftNode;
    }

    /**
     * Выполняет ребалансировку дерева, начинающегося с данной вершины.
     *
     * @param node - корень исходного дерева
     * @return - новый корень дерева после реабалансировки
     */
    private static <K,V> AVLTreeNode<K,V> rebalance(AVLTreeNode<K,V> node) {
        node.updateHeight();
        final int balance = node.getBalance();
        if (balance > 1) {
            if (node.right.getRightHeight() <= node.right.getLeftHeight()) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);

        } else if (balance < -1) {
            if (node.left.getLeftHeight() <= node.left.getRightHeight()) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        return node;
    }

    /**
     * Перерасчитывает высоту узла в дереве, исходя из высот его поддеревьев
     */
    private void updateHeight() {
        height = 1 + Math.max(getLeftHeight(), getRightHeight());
    }

    /**
     * Вычисляет сбалансированность узла - разность высот его поддеревьев
     *
     * @return - показатель сбалансированности узла.
     */
    private int getBalance() {
        return getRightHeight() - getLeftHeight();
    }

    /**
     * Возвращает самый левый лист в в дереве, начинающейся с этой вершины
     *
     * @return - крайний левый лист в дереве, начинающемся с этой вершины
     */
    private AVLTreeNode<K,V> getLeftmostChild() {
        if (this.left == null) {
            return this;
        } else {
            return left.getLeftmostChild();
        }
    }
}
