package edu.homejava.order.validator;

import edu.homejava.order.domain.CityRegistrationResponse;
import edu.homejava.order.domain.Person;
import edu.homejava.order.exception.CityRegistrationException;
import edu.homejava.order.exception.TransportException;

public interface ICityRegistrationChecker {

    CityRegistrationResponse checkPerson(Person person)
            throws CityRegistrationException, TransportException;

}
