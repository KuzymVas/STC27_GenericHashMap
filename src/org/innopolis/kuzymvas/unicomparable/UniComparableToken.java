package org.innopolis.kuzymvas.unicomparable;

import java.util.Arrays;

/**
 * Класс токена, обеспечивающее сравнение любых объектов, к которым он привязан
 * Реализует это за счет учета количества созданных токен и назначения каждому порядкового номера
 * Иммутабелен.
 */
public class UniComparableToken implements UniComparable {
    // Номер токена
    private final long[] uid = getUID();
    // Следующий номер свободный для выдачи
    private static long[] nextUID = {0};
    private static int writePos = 0; // Текущая позиция для записи в массиве номера.

    @Override
    public UniComparableToken getComparableToken() {
        return this;
    }

    /**
     * Проверяет ли является ли этот токен большим по отношению к данному
     *
     * @param other - данный токен
     * @return - true, если этот токен больше данного, false в противном случае
     */
    public boolean greaterThan(UniComparableToken other) {
        if (this.equals(other)) {
            return false;
        }
        if (uid.length > other.uid.length) {
            return true;
        }
        if (uid.length < other.uid.length) {
            return false;
        }
        for (int i = 0; i < uid.length; i++) {
            long uidSegment = uid[uid.length - i - 1];
            long uidSegmentOther = other.uid[uid.length - i - 1];
            if (uidSegment > uidSegmentOther) {
                return true;
            }
            if (uidSegment < uidSegmentOther) {
                return false;
            }
        }
        return false;
    }

    /**
     * Выдача порядкового номера токену
     *
     * @return массив, хранящий номер токена. Размер массив меняется при достижении вместимости по номерам
     */
    private static long[] getUID() {
        long[] returnUID = Arrays.copyOf(nextUID, nextUID.length); // Значение для выдачи
        nextUID[writePos]++; // Увеличиваем для следующего
        if (nextUID[nextUID.length - 1] == Long.MAX_VALUE) {  // Достигнут лимит массива
            writePos++; // Пишем в следующий элемент
            if (writePos == nextUID.length) { // Заполнили номером весь массив
                nextUID = new long[nextUID.length + 1]; // Добавляем еще элемент
                // Зануляем массив
                Arrays.fill(nextUID, 0);
                writePos = 0; // Скидываем позицию для записи обратно в начало
            }
        }
        return returnUID;
    }
}
