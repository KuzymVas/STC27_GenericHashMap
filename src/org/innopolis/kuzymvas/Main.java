package org.innopolis.kuzymvas;

import org.innopolis.kuzymvas.exceptions.KeyNotPresentException;
import org.innopolis.kuzymvas.unicomparable.UniComparable;
import org.innopolis.kuzymvas.unicomparable.UniComparableContainer;
import org.innopolis.kuzymvas.unicomparable.hashmap.BasicBucketFactory;
import org.innopolis.kuzymvas.unicomparable.hashmap.BucketAgnosticHashMap;
import org.innopolis.kuzymvas.unicomparable.hashmap.HashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Выполняется сравнение реализаций HashMap на односвязном списке и на АВЛ дереве:");
        System.out.println("Будут созданы две хэш таблицы с тремя корзинами каждая для обеспечения коллизий\n" +
                "(без коллизий внутренняя реализация не важна)");
        final HashMap listMap = new BucketAgnosticHashMap(
                new BasicBucketFactory(BasicBucketFactory.BucketType.LIST),
                3);
        final HashMap treeMap = new BucketAgnosticHashMap(
                new BasicBucketFactory(BasicBucketFactory.BucketType.AVL_TREE),
                3);
        System.out.println("В каждую будут добавлены по 10 элементов с ключами числами int 1-10 и \n" +
                "значениями строками 'один'-'десять'. Ключи и значения скомбинированы случайным образом");
        final List<UniComparable> keys = new ArrayList<>();
        keys.add(new UniComparableContainer(1));
        keys.add(new UniComparableContainer(2));
        keys.add(new UniComparableContainer(3));
        keys.add(new UniComparableContainer(4));
        keys.add(new UniComparableContainer(5));
        keys.add(new UniComparableContainer(6));
        keys.add(new UniComparableContainer(7));
        keys.add(new UniComparableContainer(8));
        keys.add(new UniComparableContainer(9));
        keys.add(new UniComparableContainer(10));
        final List<String> values = new ArrayList<>(Arrays.asList("один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять", "десять"));
        Collections.shuffle(keys);
        Collections.shuffle(values);
        for (int i = 0; i < keys.size(); i++) {
            listMap.put(keys.get(i), values.get(i));
            treeMap.put(keys.get(i), values.get(i));
        }
        outputMapsEquality(listMap, treeMap);
        System.out.println("В случайном порядке выберем элементы и выведем их на экран:");
        Collections.shuffle(keys);
        for (UniComparable key : keys) {
            System.out.println("key = " + key);
            try {
                System.out.println("listMap.get(key) = " + listMap.get(key));
                System.out.println("treeMap.get(key) = " + treeMap.get(key));
            } catch (KeyNotPresentException e) {
                System.out.println("Одна из таблиц не нашла ключ при извлечении: " + e.getMessage());
                return;
            }
        }
        System.out.println("Используем операцию замены, чтобы поменять значения друг на друга в обеих таблицах");
        Collections.shuffle(values);
        for (int i = 0; i < keys.size(); i++) {
            try {
                listMap.replace(keys.get(i), values.get(i));
                treeMap.replace(keys.get(i), values.get(i));
            } catch (KeyNotPresentException e) {
                System.out.println("Одна из таблиц не нашла ключ при замене: " + e.getMessage());
                return;
            }
        }
        outputMapsEquality(listMap, treeMap);
        System.out.println("Случайным образом удалим одинаковые пять элементов из таблиц.");
        Collections.shuffle(keys);
        for (int i = 0; i < keys.size() / 2; i++) {
            try {
                listMap.remove(keys.get(i));
                treeMap.remove(keys.get(i));
            } catch (KeyNotPresentException e) {
                System.out.println("Одна из таблиц не нашла ключ при удалении: " + e.getMessage());
                return;
            }
        }
        outputMapsEquality(listMap, treeMap);
        System.out.println("Наконец, покажем, что при изменении только одной таблицы равенства нарушаются");
        try {
            listMap.remove(keys.get(keys.size() / 2 + 1));
        } catch (KeyNotPresentException e) {
            System.out.println("Таблица на списках не нашла ключ при удалении: " + e.getMessage());
            return;
        }
        System.out.println("Выведем обе хэш таблицы:");
        System.out.println("listMap = " + listMap);
        System.out.println("treeMap = " + treeMap);
        System.out.println("Покажем, что они не равны между собой и имеют разный хэш:");
        System.out.println("listMap.equals(treeMap) = " + listMap.equals(treeMap));
        System.out.println("treeMap.equals(listMap) = " + treeMap.equals(listMap));
        System.out.println("(listMap.hashCode() != treeMap.hashCode() = " + (listMap.hashCode() != treeMap.hashCode()));
        System.out.println("listMap.hashCode() = " + listMap.hashCode());
        System.out.println("treeMap.hashCode() = " + treeMap.hashCode());

    }

    private static void outputMapsEquality(HashMap listMap, HashMap treeMap) {
        System.out.println("Выведем обе хэш таблицы:");
        System.out.println("listMap = " + listMap);
        System.out.println("treeMap = " + treeMap);
        System.out.println("Покажем, что они равны между собой и имеют одинаковый хэш:");
        System.out.println("listMap.equals(treeMap) = " + listMap.equals(treeMap));
        System.out.println("treeMap.equals(listMap) = " + treeMap.equals(listMap));
        System.out.println("(listMap.hashCode() == treeMap.hashCode() = " + (listMap.hashCode() == treeMap.hashCode()));
        System.out.println("listMap.hashCode() = " + listMap.hashCode());
        System.out.println("treeMap.hashCode() = " + treeMap.hashCode());
    }
}

