package kkmm.back.board.domain.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kkmm.back.board.domain.annotation.NotDefaultCategory;

public class NotDefaultCategoryValidator implements ConstraintValidator<NotDefaultCategory, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null이면 통과 (optional 필드)
        if (value == null) return true;

        // id가 1이면 실패
        return !value.equals("자유게시판");
    }
}
