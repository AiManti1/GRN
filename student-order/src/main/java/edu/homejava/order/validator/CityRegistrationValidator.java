package edu.homejava.order.validator;

import edu.homejava.order.domain.*;
import edu.homejava.order.exception.CityRegistrationException;

import java.util.Iterator;
import java.util.List;

import edu.homejava.order.domain.Person;
import edu.homejava.order.exception.TransportException;

// Отчет по ГРН. Проверка городского реестра населения.
public class CityRegistrationValidator {

    public static final String IN_CODE = "GRN_Request_Failed";

    public String hostName;
    protected int port;
    public String login;
    String password;

    private ICityRegistrationChecker personChecker;

    public CityRegistrationValidator() {
        personChecker = new FakeCityRegistrationChecker();
    }

    // Проверка реестра по каждому человеку / семье.
    public RegistrationStatusAuthorityResponse checkCityRegistration(FamilyOrderDetails familyOrderDetails) {

        RegistrationStatusAuthorityResponse resp = new RegistrationStatusAuthorityResponse();

                resp.addItem(checkPerson(familyOrderDetails.getHusband()));
                resp.addItem(checkPerson(familyOrderDetails.getWife()));
                List<Child> children = familyOrderDetails.getChildren();
                for (Iterator<Child> it = children.iterator(); it.hasNext();) {
                    Child child = it.next();
                    resp.addItem(checkPerson(child));
            }
//                for(int i = 0; i < familyOrderDetails.getChildren().size(); i++) {
//                    CityRegistrationResponse cn = personChecker.checkPerson(children.get(i));
//                }

//                for(Child child : children) {
//                    checkPerson(child);
//                }
        return resp;
    }

    private RegistrationStatusAuthorityResponseItem checkPerson(Person person) {

        RegistrationStatusAuthorityResponseItem.CityStatus cityStatus = null;
        RegistrationStatusAuthorityResponseItem.CityError cityError = null;

        try {
            CityRegistrationResponse tmp = personChecker.checkPerson(person);
            cityStatus = tmp.isExisting() ? RegistrationStatusAuthorityResponseItem.CityStatus.YES : RegistrationStatusAuthorityResponseItem.CityStatus.NO;

        } catch (CityRegistrationException ex) {
            ex.printStackTrace(System.out);
            cityStatus = RegistrationStatusAuthorityResponseItem.CityStatus.ERROR;
            cityError = new RegistrationStatusAuthorityResponseItem.CityError(ex.getCode(), ex.getMessage());
        } catch (TransportException ex) {
            ex.printStackTrace(System.out);
            cityStatus = RegistrationStatusAuthorityResponseItem.CityStatus.ERROR;
            cityError = new RegistrationStatusAuthorityResponseItem.CityError(IN_CODE, ex.getMessage());
        }

        // Формируем ответ по результату проверки челока.
        RegistrationStatusAuthorityResponseItem ans = new RegistrationStatusAuthorityResponseItem(cityStatus, person, cityError);

        return ans;
    }
}
