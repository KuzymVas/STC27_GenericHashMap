package org.innopolis.kuzymvas.exceptions;

public class KeyNotPresentException extends  Exception{
    /**
     * Создает исключение по умолчанию
     */
    public KeyNotPresentException() {
        super();
    }

    /**
     * Создает исключение с заданным соообщением
     * @param message - сообщение для передачи внутри исключения
     */
    public KeyNotPresentException(String message) {
        super(message);
    }
}
