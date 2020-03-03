package edu.homejava.order.validator;

import edu.homejava.order.domain.FamilyOrderDetails;
import edu.homejava.order.domain.ParentalStatusAuthorityResponse;

public class ParentialStatusValidator {
    public ParentalStatusAuthorityResponse checkParentialStatus(FamilyOrderDetails orderDetails) {
        return new ParentalStatusAuthorityResponse();
    }
}
