package edu.homejava.order.dao;

import edu.homejava.order.domain.CountryArea;
import edu.homejava.order.domain.PassportOffice;
import edu.homejava.order.domain.Street;
import edu.homejava.order.exception.DaoException;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DbAccessObj_ImplTest {

    private static final Logger logger = LoggerFactory.getLogger(StudentOrderDao_ImplTest.class);

    // Выполняется один раз/
    @BeforeClass
    public static void CreateTables() throws Exception {
        DBInit.CreateTables();
    }

    // Выполняется перед каждым тестом.
    //@Before


    @Test
    public void testStreet() throws DaoException {
        logger.info("TEST");
        List<Street> d = new DbAccessObj_Impl().findStreets("про");
        Assert.assertTrue(d.size() == 0);
    }


    @Test
    public void testPassport() throws DaoException {
        List<PassportOffice> po = new DbAccessObj_Impl().findPassportOffices("010020000000");
        Assert.assertTrue(po.size() == 0);
    }

    @Test
    public void testArea() throws DaoException {
        List<CountryArea> ca3 = new DbAccessObj_Impl().findCountryAreas("020010000000");
        Assert.assertTrue(ca3.size() == 0);


    }

}