package edu.homejava.order.dao;

import edu.homejava.order.config.Config;
import edu.homejava.order.domain.*;
import edu.homejava.order.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Класс внесения записей в базу данных.
public class StudentOrderDao_Impl implements StudentOrderDao {

    private static final Logger logger = LoggerFactory.getLogger(StudentOrderDao_Impl.class);

    private static final String INSERT_ORDER_SQL =
            "INSERT INTO j_student_order(" +
                    " student_order_status, student_order_date, h_sur_name, " +
                    " h_given_name, h_patronymic, h_date_of_birth, h_passport_seria, " +
                    " h_passport_number, h_passport_date, h_passport_office_id, h_post_index, " +
                    " h_street_code, h_building, h_extension, h_apartment, h_university_id, h_student_number, " +
                    " w_sur_name, w_given_name, w_patronymic, w_date_of_birth, w_passport_seria, " +
                    " w_passport_number, w_passport_date, w_passport_office_id, w_post_index, " +
                    " w_street_code, w_building, w_extension, w_apartment, w_university_id, w_student_number, " +
                    " certificate_id, register_office_id, marriage_date)" +
                    " VALUES (?, ?, ?, " +
                    " ?, ?, ?, ?, " +
                    " ?, ?, ?, ?, " +
                    " ?, ?, ?, ?, ?, ?, " +
                    " ?, ?, ?, ?, ?, " +
                    " ?, ?, ?, ?, " +
                    " ?, ?, ?, ?, ?, ?, " +
                    " ?, ?, ?);";

    private static final String INSERT_CHILD_SQL =
            "INSERT INTO j_student_child(" +
                    " student_order_id, c_sur_name, c_given_name, " +
                    " c_patronymic, c_date_of_birth, c_certificate_number, c_certificate_date, " +
                    " c_register_office_id, c_post_index, c_street_code, c_building, " +
                    " c_extension, c_apartment)" +
                    " VALUES (?, ?, ?, " +
                    " ?, ?, ?, ?, " +
                    " ?, ?, ?, ?, " +
                    " ?, ?)";

    private static final String SELECT_ORDERS_SQL =
            "SELECT so.*, ro.r_office_area_id, ro.r_office_name, " +
                    "po_h.p_office_area_id as h_p_office_area_id, " +
                    "po_h.p_office_name as h_p_office_name, " +
                    "po_w.p_office_area_id as w_p_office_area_id, " +
                    "po_w.p_office_name as w_p_office_name " +
                    "FROM j_student_order so " +
                    "INNER JOIN j_register_office ro ON ro.r_office_id = so.register_office_id " +
                    "INNER JOIN j_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
                    "INNER JOIN j_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
                    "WHERE student_order_status = ? ORDER BY student_order_date LIMIT ?";

    private static final String SELECT_CHILD_SQL =
            "SELECT soc.*, ro.r_office_area_id, ro.r_office_name " +
                    "FROM j_student_child soc " +
                    "INNER JOIN j_register_office ro ON ro.r_office_id = soc.c_register_office_id " +
                    "WHERE soc.student_order_id IN ";

    private static final String SELECT_ORDERS_FULL_SQL =
            "SELECT so.*, ro.r_office_area_id, ro.r_office_name, " +
                    "po_h.p_office_area_id as h_p_office_area_id, " +
                    "po_h.p_office_name as h_p_office_name, " +
                    "po_w.p_office_area_id as w_p_office_area_id, " +
                    "po_w.p_office_name as w_p_office_name, " +
                    "soc.*, ro_c.r_office_area_id, ro_c.r_office_name " +
                    "FROM j_student_order so " +
                    "INNER JOIN j_register_office ro ON ro.r_office_id = so.register_office_id " +
                    "INNER JOIN j_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
                    "INNER JOIN j_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
                    "INNER JOIN j_student_child soc ON soc.student_order_id = so.student_order_id " +
                    "INNER JOIN j_register_office ro_c ON ro_c.r_office_id = soc.c_register_office_id " +
                    "WHERE student_order_status = ? ORDER BY so.student_order_id LIMIT ?";


    private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();
    }

    @Override
    public Long saveStudentOrder(FamilyOrderDetails order) throws DaoException {
        Long result = -1L;

        logger.debug("Order: {}", order);

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_ORDER_SQL, new String[] {"student_order_id"})) {

            // Берем на себя управление транзакцией.
            con.setAutoCommit(false);
            try {
                // Устанавливаем значение из enum StudentOrderStatus: вставляем заявку со статусом START.
                stmt.setInt(1, StudentOrderStatus.START.ordinal());
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));

                // Данные для мужа и жены.
                setParamsForAdult(stmt, 3, order.getHusband());
                setParamsForAdult(stmt, 18, order.getWife());

                // Данные о браке.
                stmt.setString(33, order.getMarriageCertificateId());
                stmt.setLong(34, order.getMarriageOffice().getOfficeId());
                stmt.setDate(35, java.sql.Date.valueOf(order.getMarriageDate()));

                // Студенческая заявка. Возвращаем количество записей, затронутых данным изменением.
                stmt.executeUpdate();

                // Получаем id из колонки в массив "student_order_id".
                ResultSet gkRs = stmt.getGeneratedKeys();
                if (gkRs.next()) {
                    result = gkRs.getLong(1);
                }

                gkRs.close();

                // Вставляем данные о детях.
                saveChildren(con, order, result);

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }

        } catch(SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    @Override
    public List<FamilyOrderDetails> getStudentOrders() throws DaoException {
        return getStudentOrdersOneSelect();
        //        return getStudentOrdersTwoSelect();
    }

    private List<FamilyOrderDetails> getStudentOrdersOneSelect() throws DaoException {
        List<FamilyOrderDetails> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_ORDERS_FULL_SQL)) {

            Map<Long, FamilyOrderDetails> maps = new HashMap<>();

            stmt.setInt(1, StudentOrderStatus.START.ordinal());
            int limit = Integer.parseInt(Config.getProperty(Config.DB_LIMIT));
            stmt.setInt(2, limit);

            ResultSet rs = stmt.executeQuery();
            int counter = 0;
            while(rs.next()) {
                Long soId = rs.getLong("student_order_id");
                if (!maps.containsKey(soId)) {
                    FamilyOrderDetails fod = getFullStudentOrder(rs);

                    result.add(fod);
                    maps.put(soId, fod);
                }

                FamilyOrderDetails fod = maps.get(soId);
                fod.addChild(fillChild(rs));
                counter++;
            }
            // Количество записей  >= количества ограничений.
            if (counter >= limit) {
                result.remove(result.size() - 1); // удаляем последнюю семью
            }

            rs.close();
        } catch(SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }

        return result;
    }


    private List<FamilyOrderDetails> getStudentOrdersTwoSelect() throws DaoException {
        List<FamilyOrderDetails> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_ORDERS_SQL)) {
                stmt.setInt(1, StudentOrderStatus.START.ordinal());
                stmt.setInt(2, Integer.parseInt(Config.getProperty(Config.DB_LIMIT)));
                 ResultSet rs = stmt.executeQuery();

                 //List<Long> ids = new LinkedList<>();

                 while(rs.next()) {
                     // На основе каждой строки создаем студенческое заявление.
                     FamilyOrderDetails fod = getFullStudentOrder(rs);

                     // Добавляем студ. заявку.
                     result.add(fod);
                     // Список заявлений.
                     //ids.add(fod.getStudentOrderId());
                 }
                 // Формирование параметров IN в запрос SELECT_CHILD_SQL.
                 //StringBuilder sb = new StringBuilder("(");
                 //for(Long id : ids) { sb.append((sb.length() > 1 ? "," : "" ) + String.valueOf(id)); }
                 //sb.append(")");
                findChildren(con, result);

                 rs.close();

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
                 throw new DaoException(ex);
        }

        return result;
    }

    private FamilyOrderDetails getFullStudentOrder(ResultSet rs) throws SQLException {
        FamilyOrderDetails fod = new FamilyOrderDetails();

        // Вытаскиваем данные из таблицы j_student_order
        fillStudentOrder(rs, fod);
        fillMarriage(rs, fod);

        fod.setHusband(fillAdult(rs, "h_"));
        fod.setWife(fillAdult(rs, "w_"));
        return fod;
    }

    // Формирование параметров IN в запрос SELECT_CHILD_SQL.
    private void findChildren(Connection con, List<FamilyOrderDetails> result) throws SQLException {
        String cl = "(" + result.stream().map(fod -> String.valueOf(fod.getStudentOrderId()))
                .collect(Collectors.joining(",")) + ")";

        Map<Long, FamilyOrderDetails> maps = result.stream().collect(Collectors
                .toMap(fod -> fod.getStudentOrderId(), fod -> fod));

        try (PreparedStatement stmt = con.prepareStatement(SELECT_CHILD_SQL + cl)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Child ch = fillChild(rs);
                FamilyOrderDetails fod = maps.get(rs.getLong("student_order_id"));
                fod.addChild(ch);
            }
        }
    }

    private Child fillChild(ResultSet rs) throws SQLException {
        String surName = rs.getString("c_sur_name");
        String givenName = rs.getString("c_given_name");
        String patronymic = rs.getString("c_patronymic");
        LocalDate dateOfBirth = rs.getDate("c_date_of_birth").toLocalDate();

        Child child = new Child(surName,givenName, patronymic, dateOfBirth);

        child.setCertificateOfBirthNumber(rs.getString("c_certificate_number"));
        child.setIssueDate(rs.getDate("c_certificate_date").toLocalDate());

        Long roId = rs.getLong("c_register_office_id");
        String roArea = rs.getString("r_office_area_id");
        String roName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, roArea, roName);
        child.setIssueOffice(ro);

        Address adr = new Address();
        Street st = new Street(rs.getLong("c_street_code"), "");
        adr.setStreet(st);
        adr.setPostCode(rs.getString("c_post_index"));
        adr.setBuilding(rs.getString("c_building"));
        adr.setExtension(rs.getString("c_extension"));
        adr.setApartment(rs.getString("c_apartment"));
        child.setAddress(adr);

        return  child;
    }

    private Adult fillAdult(ResultSet rs, String prefix) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(rs.getString(prefix + "sur_name"));
        adult.setGivenName(rs.getString(prefix + "given_name"));
        adult.setPatronymic(rs.getString(prefix + "patronymic"));
        adult.setDateOfBirth(rs.getDate(prefix + "date_of_birth").toLocalDate());
        adult.setPassportSeries(rs.getString(prefix + "passport_seria"));
        adult.setPassportNumber(rs.getString(prefix + "passport_number"));
        adult.setIssueDate(rs.getDate(prefix + "passport_date").toLocalDate());

        Long poId = rs.getLong(prefix + "passport_office_id");
        String poArea = rs.getString(prefix + "p_office_area_id");
        String poName = rs.getString(prefix + "p_office_name");
        PassportOffice po = new PassportOffice(poId, poArea, poName);
        adult.setIssueOffice(po);
        Address adr = new Address();
        Street st = new Street(rs.getLong(prefix + "street_code"), "");
        adr.setStreet(st);
        adr.setPostCode(rs.getString(prefix + "post_index"));
        adr.setBuilding(rs.getString(prefix + "building"));
        adr.setExtension(rs.getString(prefix + "extension"));
        adr.setApartment(rs.getString(prefix + "apartment"));
        adult.setAddress(adr);

        University uni = new University(rs.getLong(prefix + "university_id"), "");
        adult.setUniversity(uni);
        adult.setStudentId(rs.getString(prefix + "student_number"));

        return adult;
    }


    private void fillStudentOrder(ResultSet rs, FamilyOrderDetails fod) throws SQLException {
        fod.setStudentOrderId(rs.getLong("student_order_id"));
        fod.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());
        fod.setStudentOrderStatus(StudentOrderStatus.fromValue(rs.getInt("student_order_status")));
    }

    private void fillMarriage(ResultSet rs, FamilyOrderDetails fod) throws SQLException {
        fod.setMarriageCertificateId(rs.getString("certificate_id"));
        fod.setMarriageDate(rs.getDate("marriage_date").toLocalDate());

        Long roId = rs.getLong("register_office_id");
        String areaId = rs.getString("r_office_area_id");
        String name = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, areaId, name);
        fod.setMarriageOffice(ro);
    }

    private void saveChildren(Connection con, FamilyOrderDetails order, Long orderId) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_CHILD_SQL)) {
            int counter = 0;
            for (Child child : order.getChildren()) {
                stmt.setLong(1, orderId);
                setParamsForChild(stmt, child);
                stmt.addBatch();
                counter++;
                if (counter > 10000) {
                    stmt.executeBatch();
                    counter = 0;
                }
            }
            if (counter > 0) {
                stmt.executeBatch();
            }
            //stmt.executeUpdate();
        }
    }

    private void setParamsForAdult(PreparedStatement stmt, int i, Adult adult) throws SQLException {
        setParamsForPerson(stmt, i, adult);
        stmt.setString(i + 4, adult.getPassportSeries());
        stmt.setString(i + 5, adult.getPassportNumber());
        stmt.setDate(i + 6, Date.valueOf(adult.getIssueDate()));
        stmt.setLong(i + 7, adult.getIssueOffice().getOfficeId());
        setParamsForAddress(stmt, i + 8, adult);
        stmt.setLong(i + 13, adult.getUniversity().getUniversityId());
        stmt.setString(i + 14, adult.getStudentId());
    }

    private void setParamsForPerson(PreparedStatement stmt, int i, Person person) throws SQLException {
        stmt.setString(i, person.getSurName());
        stmt.setString(i + 1, person.getGivenName());
        stmt.setString(i + 2, person.getPatronymic());
        stmt.setDate(i + 3, java.sql.Date.valueOf(person.getDateOfBirth()));
    }

    private void setParamsForAddress(PreparedStatement stmt, int i, Person person) throws SQLException {
        Address adult_address = person.getAddress();
        stmt.setString(i, adult_address.getPostCode());
        stmt.setLong(i + 1, adult_address.getStreet().getStreetCode());
        stmt.setString(i + 2, adult_address.getBuilding());
        stmt.setString(i + 3, adult_address.getExtension());
        stmt.setString(i + 4, adult_address.getApartment());
    }

    private void setParamsForChild(PreparedStatement stmt, Child child) throws SQLException {
        setParamsForPerson(stmt, 2, child);
        stmt.setString(6, child.getCertificateOfBirthNumber());
        stmt.setDate(7, java.sql.Date.valueOf(child.getIssueDate()));
        stmt.setLong(8, child.getIssueOffice().getOfficeId());
        setParamsForAddress(stmt, 9, child);
    }
}
