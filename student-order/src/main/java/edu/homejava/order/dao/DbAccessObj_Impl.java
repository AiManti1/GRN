package edu.homejava.order.dao;

import edu.homejava.order.config.Config;
import edu.homejava.order.domain.CountryArea;
import edu.homejava.order.domain.PassportOffice;
import edu.homejava.order.domain.RegisterOffice;
import edu.homejava.order.domain.Street;
import edu.homejava.order.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

// Класс доступа к базе данных.
public class DbAccessObj_Impl implements DbAccessObj {

    private static final Logger logger = LoggerFactory.getLogger(DbAccessObj_Impl.class);

    private static final String GET_STREET_SQL = "SELECT street_code, street_name FROM j_street WHERE UPPER(street_name) LIKE UPPER(?)";
    private static final String GET_PASSPORT_SQL = "SELECT * FROM j_passport_office WHERE p_office_area_id = ?";
    private static final String GET_REGISTER_SQL = "SELECT * FROM j_register_office WHERE r_office_area_id = ?";
    private static final String GET_AREA_SQL = "SELECT * FROM j_country_struct WHERE area_id LIKE ? AND area_id <> ?";

    private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();

    }

    public List<Street> findStreets(String streetNamePattern) throws DaoException {
            List<Street> result = new LinkedList<>();

            try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_STREET_SQL)) {
                stmt.setString(1, "%" + streetNamePattern + "%");

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Street str = new Street(rs.getLong("street_code"), rs.getString("street_name"));
                    result.add(str);
                }
            } catch(SQLException ex) {
                logger.error(ex.getMessage(), ex);
                throw new DaoException(ex);
            }
            return result;
        }

    @Override
    public List<PassportOffice> findPassportOffices(String areaId) throws DaoException {
        List<PassportOffice> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_PASSPORT_SQL)) {
            stmt.setString(1, areaId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PassportOffice po = new PassportOffice(
                        rs.getLong("p_office_id"),
                        rs.getString("p_office_area_id"),
                        rs.getString("p_office_name"));
                result.add(po);
            }
        } catch(SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    @Override
    public List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException {
        List<RegisterOffice> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REGISTER_SQL)) {
            stmt.setString(1, areaId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RegisterOffice po = new RegisterOffice(
                        rs.getLong("r_office_id"),
                        rs.getString("r_office_area_id"),
                        rs.getString("r_office_name"));
                result.add(po);
            }
        } catch(SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    @Override
    public List<CountryArea> findCountryAreas(String areaId) throws DaoException {
        List<CountryArea> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_AREA_SQL)) {
            String param1 = buildParam(areaId); // Строим параметр из переданного id '020000000000'.
            String param2 = areaId;

            stmt.setString(1, param1);
            stmt.setString(2, param2);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CountryArea ca = new CountryArea(
                        rs.getString("area_id"),
                        rs.getString("area_name"));
                result.add(ca);
            }
        } catch(SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    // Корневой элемент. Области (02___0000000). Район(02001___0000). Поселение (02001001____).
    private String buildParam(String areaId) throws SQLException {
        if (areaId == null || areaId.trim().isEmpty()) {
            return "__0000000000";
        } else if (areaId.endsWith("0000000000")) {
            return areaId.substring(0, 2) + "___0000000"; // 02___0000000, 01___0000000 и т.д.
        } else if (areaId.endsWith("0000000")) {
            return areaId.substring(0, 5) + "___0000";
        } else if (areaId.endsWith("0000")) {
            return areaId.substring(0, 8) + "____";
        }
        throw new SQLException("Invalid parameter 'areaId'" + areaId);
    }
}
