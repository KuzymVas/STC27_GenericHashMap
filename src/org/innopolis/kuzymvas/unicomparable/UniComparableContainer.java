package org.innopolis.kuzymvas.unicomparable;

import java.util.Objects;

// Иммутабельный контейнер, хранящий ссылку на некий объект и сопоставленный ему токен сравнения
public class UniComparableContainer implements UniComparable{

    private final UniComparableToken token; // хранимый токен
    private final Object value; // исходный объект

    public UniComparableContainer(Object value) {
        token = new UniComparableToken();
        this.value = value;
    }

    public Object getValue() {
        return  value;
    }

    @Override
    public UniComparableToken getComparableToken() {
        return token;
    }

    @Override
    public String toString() {
        return "UCC: " +
                value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniComparableContainer that = (UniComparableContainer) o;
        return token.equals(that.token) &&
                Objects.equals(value, that.value);
    }

    /**
     *  Хэш контейнера всегда совпадает  хэшом объекта внутри
     * @return - хэш объекта внутри контейнера.
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
