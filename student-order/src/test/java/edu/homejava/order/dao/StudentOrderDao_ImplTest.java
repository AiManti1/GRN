package edu.homejava.order.dao;

import edu.homejava.order.domain.*;
import edu.homejava.order.exception.DaoException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class StudentOrderDao_ImplTest {

    @BeforeClass
    public static void CreateTables() throws Exception {
        DBInit.CreateTables();
    }

    @Test
    public void saveStudentOrder() throws DaoException {
        FamilyOrderDetails fod = buildStudentOrder(10);
        Long id = new StudentOrderDao_Impl().saveStudentOrder(fod);
    }

    @Test
    public void getStudentOrders() throws DaoException {
        List<FamilyOrderDetails> list = new StudentOrderDao_Impl().getStudentOrders();
    }

    public FamilyOrderDetails buildStudentOrder(long id) {

        FamilyOrderDetails fo = new FamilyOrderDetails();
        fo.setStudentOrderId(id);

        fo.setMarriageCertificateId("" + (123456000 + id));
        fo.setMarriageDate(LocalDate.of(2016, 7, 4));

        RegisterOffice ro = new RegisterOffice(1L, "", "");
        fo.setMarriageOffice(ro);

        Street street = new Street(1L, "");

        Address address = new Address("195000", street, "12", "", "142");

        // Вставка данных в БД.
        // Муж.
        Adult husband = new Adult("Петров", "Виктор", "Сергеевич", LocalDate.of(1997, 8, 24));
        husband.setPassportSeries("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueDate(LocalDate.of(2017, 9, 15));
        PassportOffice po1 = new PassportOffice(1L, "", "");
        husband.setIssueOffice(po1);
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2L, ""));
        husband.setStudentId("HH12345");

        // Жена.
        Adult wife = new Adult("Петрова", "Вероника", "Алекссевна", LocalDate.of(1998, 3, 12));
        wife.setPassportSeries("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2018, 4, 5));
        PassportOffice po2 = new PassportOffice(2L, "", "");
        wife.setIssueOffice(po2);
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(1L, ""));
        wife.setStudentId("WW12345");

        // Ребенок 1.
        Child child1 = new Child("Петрова", "Ирина", "Викторовна", LocalDate.of(2018, 6, 29));
        child1.setCertificateOfBirthNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2018, 6, 11));
        RegisterOffice ro2 = new RegisterOffice(2L, "", "");
        child1.setIssueOffice(ro2);
        child1.setAddress(address);
        // Ребенок 2.
        Child child2 = new Child("Петров", "Евгений", "Викторович", LocalDate.of(2018, 6, 29));
        child2.setCertificateOfBirthNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2018, 7, 19));
        RegisterOffice ro3 = new RegisterOffice(3L, "", "");
        child2.setIssueOffice(ro3);
        child2.setAddress(address);

        fo.setHusband(husband);
        fo.setWife(wife);
        fo.addChild(child1);
        fo.addChild(child2);

        return fo;
    }
}