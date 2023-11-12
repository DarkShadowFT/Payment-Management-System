package com.example.pms.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.IBANValidator;

public class IbanValidator implements ConstraintValidator<ValidIban, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        return IBANValidator.getInstance().isValid(value);
    }
}
