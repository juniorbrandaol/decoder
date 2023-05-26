package com.ead.course.validation;

import com.ead.course.clients.AuthUserFeignClient;
import com.ead.course.clients.AuthUserRestTemplateClient;
import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enuns.UserStatus;
import com.ead.course.enuns.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {
    @Autowired
    private Validator validator;
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    //feignClient
    @Autowired
    private AuthUserFeignClient authUserFeignClient;

    //restTemplate
    @Autowired
    private AuthUserRestTemplateClient authUserRestTemplateClient;

    @Override
    public void validate(Object o, Errors errors) {
        CourseDto courseDto = (CourseDto) o;
        validator.validate(courseDto,errors);
        if(!errors.hasErrors()){
            validateUserInstructor(courseDto.getUserInstructor(),errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors erros){

        ResponseEntity<UserDto>  responseUserInstructor;
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
    }

}
