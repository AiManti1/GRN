package edu.homejava.order.domain;

// Ответ из реестра по одному человеку.
public class RegistrationStatusAuthorityResponseItem {

    // Статус-ответ: есть или нет регистрации. Или же ошибка запроса.
    public enum CityStatus
    {
        YES, NO, ERROR;
    }

    // Текст и код ошибки в ответе из ГРН.
    public static class CityError
    {
        private String code;
        private String text;

        public CityError(String code, String text) {
            this.code = code;
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }

    private CityStatus status;
    private Person person;
    private CityError error;

    public CityStatus getStatus() {
        return status;
    }

    public Person getPerson() {
        return person;
    }

    public CityError getError() {
        return error;
    }

    public RegistrationStatusAuthorityResponseItem(CityStatus status, Person person) {
        this.status = status;
        this.person = person;
    }

    public RegistrationStatusAuthorityResponseItem(CityStatus status, Person person, CityError error) {
        this.status = status;
        this.person = person;
        this.error = error;
    }


}
