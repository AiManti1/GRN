package edu.homejava.order.validator;

import edu.homejava.order.domain.FamilyOrderDetails;
import edu.homejava.order.domain.StudentStatusAuthorityResponse;

public class StudentStatusValidator {
    public StudentStatusAuthorityResponse checkStudentStatus(FamilyOrderDetails orderDetails) {
        return new StudentStatusAuthorityResponse();
    }
}
