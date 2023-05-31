package com.ead.course.validation;

import com.ead.course.dtos.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {
    @Autowired
    private Validator validator;
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


      /*  ResponseEntity<UserDto>  responseUserInstructor;
        try {
            responseUserInstructor = authUserFeignClient.getOneUserById(userInstructor);
            if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
               erros.rejectValue("userInstructor", "userInstructorError", "User must be INSTRUCTOR OR ADMIN ");
            }
        }catch (HttpStatusCodeException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
               erros.rejectValue("userInstructor", "userInstructorError", "Instructor not found");
            }
        }
        */
    }

}
