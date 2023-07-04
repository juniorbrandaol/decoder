package com.ead.course.validation;

import com.ead.course.configs.security.AuthenticationCurrentUserService;
import com.ead.course.dtos.CourseDto;
import com.ead.course.enuns.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {
    @Autowired
    private Validator validator;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationCurrentUserService authenticationCurrentUserService;
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDto courseDto = (CourseDto) o;
        validator.validate(courseDto,errors);
        if(!errors.hasErrors()){
            validateUserInstructor(courseDto.getUserInstructor(),errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors erros){
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if(currentUserId.equals(userInstructor)) {
            Optional<UserModel> userModelOptional = userService.findById(userInstructor);
            if (!userModelOptional.isPresent()) {
                erros.rejectValue("userInstructor", "userInstructorError", "INSTRUCTOR NOT FOUND ");
            }
            else if (userModelOptional.get().getUserType().equals(UserType.STUDENT.toString()) ||
                    userModelOptional.get().getUserType().equals(UserType.USER.toString())) {
                erros.rejectValue("userInstructor", "userInstructorError", "User must be INSTRUCTOR OR ADMIN ");
            }
        }else{
            throw  new AccessDeniedException("Forbidem");
        }
    }

}
