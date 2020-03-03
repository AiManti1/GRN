package edu.homejava.order.validator;

import edu.homejava.order.domain.Adult;
import edu.homejava.order.domain.Child;
import edu.homejava.order.domain.CityRegistrationResponse;
import edu.homejava.order.domain.Person;
import edu.homejava.order.exception.CityRegistrationException;
import edu.homejava.order.exception.TransportException;

// Идем в ГРН и возвращаем информацию.
public class FakeCityRegistrationChecker implements ICityRegistrationChecker {

    private static final String husbPassSeries= "1000";
    private static final String wifePassSeries = "2000";
    private static final String ERROR_1= "10000";
    private static final String ERROR_2 = "20000";
    private static final String ERROR_T_1 = "10003";
    private static final String ERROR_T_2 = "20003";

    public CityRegistrationResponse checkPerson(Person person) throws CityRegistrationException, TransportException {

        CityRegistrationResponse res = new CityRegistrationResponse();

        if (person instanceof Adult) {
            Adult t = (Adult) person;
            String ps = t.getPassportSeries();
            if (ps.equals(husbPassSeries) || ps.equals(wifePassSeries)) {
                res.setExisting(true);
                res.setTemp(false);
            }
            if (ps.equals(ERROR_1) || ps.equals(ERROR_2)) {
                CityRegistrationException ex = new CityRegistrationException("1", "Fake GRN request" + ps);
                throw ex;
            }
            if (ps.equals(ERROR_T_1) || ps.equals(ERROR_T_2)) {
                TransportException ex = new TransportException("Transport error request" + ps);
                throw ex;
            }
        }

        if (person instanceof Child) {
            res.setExisting(true);
            res.setTemp(true);
        }
        return res;
    }

}
