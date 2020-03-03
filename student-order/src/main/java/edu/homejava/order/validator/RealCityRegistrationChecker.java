package edu.homejava.order.validator;

import edu.homejava.order.domain.CityRegistrationResponse;
import edu.homejava.order.domain.Person;
import edu.homejava.order.exception.CityRegistrationException;
import edu.homejava.order.exception.TransportException;

// Идем в ГРН и возвращаем информацию.
public class RealCityRegistrationChecker implements ICityRegistrationChecker {

    public CityRegistrationResponse checkPerson(Person person)
            throws CityRegistrationException, TransportException {

        return null;
    }
}
