package edu.homejava.order.domain;

import java.util.ArrayList;
import java.util.List;

// Список всех ответов и ошибок из ГРН по людям.
public class RegistrationStatusAuthorityResponse {
    private List<RegistrationStatusAuthorityResponseItem> items;

    public void addItem(RegistrationStatusAuthorityResponseItem item) {
        if (items == null) {
            items = new ArrayList<>(10);
        }
        items.add(item);
    }

    public List<RegistrationStatusAuthorityResponseItem> getItems() {
        return items;
    }
}

