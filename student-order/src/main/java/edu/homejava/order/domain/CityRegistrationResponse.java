package edu.homejava.order.domain;

public class CityRegistrationResponse {

    // Существует ли человек в системе ГРН.
    private boolean existing;
    private Boolean temp = null;

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }

    public Boolean getTemp() {
        return temp;
    }

    public void setTemp(Boolean temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "CityRegistrationResponse{" +
                "existing=" + existing +
                ", temp=" + temp +
                '}';
    }
}
