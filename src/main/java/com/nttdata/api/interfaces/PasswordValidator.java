package com.nttdata.api.interfaces;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Value("${password.regex}")
    private String passwordRegex;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean tmp;
        if(passwordRegex == null) {         //--> SOLO si no puede leer el archivo de propiedades
            passwordRegex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
            // Ejemplo: mínimo 8 caracteres, al menos una letra mayúscula y un número

        }
        if (password == null) {
            return false;
        }

        tmp = password.matches(passwordRegex);

        return tmp;
    }
}