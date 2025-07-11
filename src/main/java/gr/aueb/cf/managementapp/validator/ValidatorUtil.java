package gr.aueb.cf.managementapp.validator;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ValidatorUtil {

    private static final Validator validator = initValidator();
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorUtil.class);

    private static Validator initValidator() {
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            return factory.getValidator();
        } catch (Exception e) {
            LOGGER.error("Validator cannot be initialized.", e);
            throw new RuntimeException("Validator init failed", e);
        }
    }

    public ValidatorUtil() {
    }

    public static <T> List<String> validateDTO(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        return violations.stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }
}
