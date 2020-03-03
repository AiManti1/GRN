package edu.homejava.order.dao;

import edu.homejava.order.domain.CountryArea;
import edu.homejava.order.domain.PassportOffice;
import edu.homejava.order.domain.RegisterOffice;
import edu.homejava.order.domain.Street;
import edu.homejava.order.exception.DaoException;

import java.util.List;

public interface DbAccessObj {
    List<Street> findStreets(String streetName) throws DaoException;
    List<PassportOffice> findPassportOffices(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException;
    List<CountryArea> findCountryAreas(String areaId) throws DaoException;
}
