package net.streamarchive.infrastructure;


import net.streamarchive.infrastructure.exceptions.WrongParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.LinkedList;
import java.util.List;

@Component
public class DataValidator {
    @Autowired
    private Validator validator;

    public <T> void validate(T object) throws WrongParamsException {
        List<ConstraintViolation<T>> validations = new LinkedList<>(validator.validate(object));
        if (validations.size() != 0) {
            throw new WrongParamsException(validations.get(0).getMessage());
        }
    }
}
