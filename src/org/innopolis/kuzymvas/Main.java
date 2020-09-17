package org.innopolis.kuzymvas;


import org.innopolis.kuzymvas.generic.hashmap.BasicBucketFactory;
import org.innopolis.kuzymvas.generic.hashmap.BucketAgnosticHashMap;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(
                "Выполняется сравнение реализаций HashMap на односвязном списке, на АВЛ дереве и стандартной реализациия из Java.utils:");
        System.out.println("Будут созданы три хэш таблицы, отображающие Integer ключи в String значения");
        final Map<Integer, String> listMap = new BucketAgnosticHashMap<>(
                new BasicBucketFactory<>(BasicBucketFactory.BucketType.LIST),
                3);
        final Map<Integer, String> treeMap = new BucketAgnosticHashMap<>(
                new BasicBucketFactory<>(BasicBucketFactory.BucketType.AVL_TREE),
                3);
        final Map<Integer, String> realHashMap = new HashMap<>();

        System.out.println("В каждую будут добавлены по 10 элементов с ключами числами int 1-10 и \n" +
                                   "значениями строками 'один'-'десять'. Ключи и значения скомбинированы случайным образом");
        final List<Integer> keys = new ArrayList<>();
        keys.add(1);
        keys.add(2);
        keys.add(3);
        keys.add(4);
        keys.add(5);
        keys.add(6);
        keys.add(7);
        keys.add(8);
        keys.add(9);
        keys.add(10);
        final List<String> values = new ArrayList<>(
                Arrays.asList("один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять", "десять"));
        Collections.shuffle(keys);
        Collections.shuffle(values);
        for (int i = 0; i < keys.size(); i++) {
            listMap.put(keys.get(i), values.get(i));
            treeMap.put(keys.get(i), values.get(i));
            realHashMap.put(keys.get(i), values.get(i));
        }
        outputMapsEquality(listMap, treeMap,realHashMap);
        System.out.println("В случайном порядке выберем элементы и выведем их на экран:");
        Collections.shuffle(keys);
        for (Integer key : keys) {
            System.out.println("key = " + key);
            System.out.println("listMap.get(key) = " + listMap.get(key));
            System.out.println("treeMap.get(key) = " + treeMap.get(key));
            System.out.println("realHashMap.get(key) = " + realHashMap.get(key));
        }
        System.out.println("Используем операцию замены, чтобы поменять значения друг на друга в обеих таблицах");
        Collections.shuffle(values);
        for (int i = 0; i < keys.size(); i++) {
            listMap.replace(keys.get(i), values.get(i));
            treeMap.replace(keys.get(i), values.get(i));
            realHashMap.replace(keys.get(i), values.get(i));
        }
        outputMapsEquality(listMap, treeMap,realHashMap);
        System.out.println("Случайным образом удалим одинаковые пять элементов из таблиц.");
        Collections.shuffle(keys);
        Map<Double, Object> map;
        for (int i = 0; i < keys.size() / 2; i++) {
            listMap.remove(keys.get(i));
            treeMap.remove(keys.get(i));
            realHashMap.remove(keys.get(i));
        }
        outputMapsEquality(listMap, treeMap,realHashMap);

    }

    private static void outputMapsEquality(Map<Integer,String> listMap,
                                           Map<Integer,String> treeMap,
                                           Map<Integer,String> realHashMap) {
        System.out.println("Выведем все хэш таблицы:");
        System.out.println("listMap = " + listMap);
        System.out.println("treeMap = " + treeMap);
        System.out.println("realHashMap = " + realHashMap);
        System.out.println("Покажем, что они равны, сравнивая размеры и содержания EntrySet-ов:");
        System.out.println("(listMap.entrySet().size() == realHashMap.entrySet().size()) = " + (listMap.entrySet().size() == realHashMap.entrySet().size()));
        System.out.println("listMap.entrySet().containsAll(realHashMap.entrySet()) = " + listMap.entrySet().containsAll(realHashMap.entrySet()));
        System.out.println("(treeMap.entrySet().size() == realHashMap.entrySet().size()) = " + (treeMap.entrySet().size() == realHashMap.entrySet().size()));
        System.out.println("treeMap.entrySet().containsAll(realHashMap.entrySet()) = " + treeMap.entrySet().containsAll(realHashMap.entrySet()));

    }
}

