package edu.homejava.order.domain;

import java.time.LocalDate;

public class Child extends Person {

    private String certificateOfBirthNumber;
    private LocalDate issueDate;
    private RegisterOffice issueOffice;

    public Child(String surName, String givenName, String patronymic, LocalDate dateOfBirth) {
        super(surName, givenName, patronymic, dateOfBirth);
    }

    public String getCertificateOfBirthNumber() {
        return certificateOfBirthNumber;
    }

    public void setCertificateOfBirthNumber(String certificateOfBirthNumber) {
        this.certificateOfBirthNumber = certificateOfBirthNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public RegisterOffice getIssueOffice() {
        return issueOffice;
    }

    public void setIssueOffice(RegisterOffice issueOffice) {
        this.issueOffice = issueOffice;
    }

    @Override
    public String toString() {
        return "Child{" +
                "certificateOfBirthNumber='" + certificateOfBirthNumber + '\'' +
                ", issueDate=" + issueDate +
                ", issueOffice=" + issueOffice +
                "} " + super.toString();
    }
}
