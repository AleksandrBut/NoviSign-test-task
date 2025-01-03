package com.test.task.novisign.validation;

import com.test.task.novisign.validation.annotation.ImageUrl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import javax.imageio.ImageIO;
import java.net.URI;

public class UrlValidator implements ConstraintValidator<ImageUrl, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return ImageIO.read(URI.create(value).toURL()) != null;
        } catch (Exception ignored) {
            return false;
        }
    }
}
