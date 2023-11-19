package ru.clevertec.util.validation;

import ru.clevertec.exception.ValidationException;
import ru.clevertec.util.validation.annotation.Valid;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationCar {

    public static <T> void validate(T car) {
        Field[] fields = car.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Valid.class)) {
                field.setAccessible(true);
                Valid annotation = field.getAnnotation(Valid.class);
                String regex = annotation.regex();
                Pattern pattern = Pattern.compile(regex);
                Object valueField = null;
                try {
                    valueField = field.get(car);
                    Matcher matcher = pattern.matcher(valueField.toString());
                    if (!matcher.find()) {
                        throw new ValidationException("Invalid field " + field.getName());
                    }
                } catch (IllegalAccessException e) {
                    throw new ValidationException("Field " + field.getName() + " not found");
                }
            }
        }
    }
}
