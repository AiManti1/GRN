package edu.homejava.order.domain;


public enum StudentOrderStatus {
    START, CHECKED;

    // Возвращаем 'StudentOrderStatus' по целому числу.
    public static StudentOrderStatus fromValue(int value) {
        for(StudentOrderStatus sos : StudentOrderStatus.values()) {
            if(sos.ordinal() == value) {
                return sos;
            }
        }
        throw new RuntimeException("Unknown value:" + value);
    }
}
