package edu.homejava.order;

import edu.homejava.order.dao.DbAccessObj_Impl;
import edu.homejava.order.dao.StudentOrderDao;
import edu.homejava.order.dao.StudentOrderDao_Impl;
import edu.homejava.order.domain.*;

import java.time.LocalDate;
import java.util.List;

public class StudentOrder {

    public static void main(String[] args) throws Exception {
/*
       List<Street> d = new DbAccessObj_Impl().findStreets("про");
        for(Street s : d) {
            System.out.println(s.getStreetName());
        }


        List<PassportOffice> po = new DbAccessObj_Impl().findPassportOffices("010020000000");
        for(PassportOffice p : po) {
            System.out.println(p.getOfficeName());
        }

        List<RegisterOffice> ro = new DbAccessObj_Impl().findRegisterOffices("010010000000");
        for(RegisterOffice r : ro) {
            System.out.println(r.getOfficeName());
        }

        List<CountryArea> ca1 = new DbAccessObj_Impl().findCountryAreas("");
        for(CountryArea c : ca1) {
            System.out.println(c.getAreaId() + ":" + c.getAreaName());
        }

        System.out.println("--->");

        List<CountryArea> ca2 = new DbAccessObj_Impl().findCountryAreas("020000000000");
        for(CountryArea c : ca2) {
            System.out.println(c.getAreaId() + ":" + c.getAreaName());
        }

        System.out.println("--->");

        List<CountryArea> ca3 = new DbAccessObj_Impl().findCountryAreas("020010000000");
        for(CountryArea c : ca3) {
            System.out.println(c.getAreaId() + ":" + c.getAreaName());
        }

        System.out.println("--->");

        List<CountryArea> ca4 = new DbAccessObj_Impl().findCountryAreas("020010010000");
        for(CountryArea c : ca4) {
            System.out.println(c.getAreaId() + ":" + c.getAreaName());
        }
*/

        FamilyOrderDetails s = buildStudentOrder(10);
        StudentOrderDao s_dao = new StudentOrderDao_Impl();
        Long id = s_dao.saveStudentOrder(s);
        //System.out.println(id);

        List<FamilyOrderDetails> soList = s_dao.getStudentOrders();
        for (FamilyOrderDetails fod : soList) {
            System.out.println(fod.getStudentOrderId());
        }

        //FamilyOrderDetails so = new FamilyOrderDetails();
        //long ans = StudentOrder(so);

        //System.out.println(ans);

    }

    static long StudentOrder(FamilyOrderDetails fo) {
        System.out.println("saveStudentOrder" );
        long answer = 200;
        return answer;
    }

    public static FamilyOrderDetails buildStudentOrder(long id) {

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
