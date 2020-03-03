package edu.homejava.order.dao;

import edu.homejava.order.StudentOrder;
import edu.homejava.order.domain.FamilyOrderDetails;
import edu.homejava.order.exception.DaoException;

import java.util.List;

// Система сохранения данных студенческой заявки.
public interface StudentOrderDao {
    Long saveStudentOrder(FamilyOrderDetails so) throws DaoException;

    List<FamilyOrderDetails> getStudentOrders() throws DaoException;

}
