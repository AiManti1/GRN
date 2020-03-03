package edu.homejava.order;

import edu.homejava.order.dao.StudentOrderDao_Impl;
import edu.homejava.order.domain.*;
import edu.homejava.order.exception.DaoException;
import edu.homejava.order.mail.MailSender;
import edu.homejava.order.validator.MarriageStatusValidator;
import edu.homejava.order.validator.ParentialStatusValidator;
import edu.homejava.order.validator.CityRegistrationValidator;
import edu.homejava.order.validator.StudentStatusValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderValidator {

    private CityRegistrationValidator   registrationStatusVal;
    private MarriageStatusValidator     marriageStatusVal;
    private ParentialStatusValidator    parentalStatusVal;
    private StudentStatusValidator      studentStatusVal;
    private MailSender                  mailSender;

    public StudentOrderValidator() {
        registrationStatusVal   = new CityRegistrationValidator();
        marriageStatusVal       = new MarriageStatusValidator();
        parentalStatusVal = new ParentialStatusValidator();
        studentStatusVal        = new StudentStatusValidator();
        mailSender              = new MailSender();
    }

    public static void main(String[] args) {
        StudentOrderValidator sov = new StudentOrderValidator();
        sov.checkAllStatuses();
    }

    public void checkAllStatuses() {
        try {
            List<FamilyOrderDetails> odList = readOrderDetails();

            for (FamilyOrderDetails item : odList) {
                checkSingleOrder(item);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<FamilyOrderDetails> readOrderDetails() throws DaoException {
        return new StudentOrderDao_Impl().getStudentOrders();
    }

    // Получение данных из ГРН.
    public void checkSingleOrder(FamilyOrderDetails familyOrderDetails) {
        RegistrationStatusAuthorityResponse cityAuthorityResponse = checkCityRegistration(familyOrderDetails);
        MarriageStatusAuthorityResponse     marriageAuthorityResponse = checkMarriageStatus(familyOrderDetails);
        ParentalStatusAuthorityResponse     parentalAuthorityResponse = checkParentalStatus(familyOrderDetails);
        StudentStatusAuthorityResponse      studentAuthorityResponse = checkStudentStatus(familyOrderDetails);

        sendMail(familyOrderDetails);
    }

    public RegistrationStatusAuthorityResponse checkCityRegistration(FamilyOrderDetails orderDetails) {
        return registrationStatusVal.checkCityRegistration(orderDetails);
    }

    public MarriageStatusAuthorityResponse checkMarriageStatus(FamilyOrderDetails orderDetails) {
        return marriageStatusVal.checkMarriageStatus(orderDetails);
    }

    public ParentalStatusAuthorityResponse checkParentalStatus(FamilyOrderDetails orderDetails) {
        return parentalStatusVal.checkParentialStatus(orderDetails);
    }

    public StudentStatusAuthorityResponse checkStudentStatus(FamilyOrderDetails orderDetails) {
        return studentStatusVal.checkStudentStatus(orderDetails);
    }

    public void sendMail(FamilyOrderDetails orderDetails) {
        mailSender.sendMail(orderDetails);
    }
}
