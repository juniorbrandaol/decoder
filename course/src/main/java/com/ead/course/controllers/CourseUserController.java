package com.ead.course.controllers;

import com.ead.course.clients.AuthUserFeignClient;
import com.ead.course.clients.AuthUserRestTemplateClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enuns.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class CourseUserController {

    /*caso opte por usar o serviço de restTemplate*/
    @Autowired
    private AuthUserRestTemplateClient authUserRestTemplateClient;

    @Autowired
    private CourseService courseService;

    /*serviço de feignclient*/
    @Autowired
    private CourseUserService courseUserService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(
            @PageableDefault(page = 0,size = 10,sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findByid(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseUserService.getAllUsersByCourse(courseId ,pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse( @PathVariable(value = "courseId") UUID courseId,
                                                                @RequestBody  @Valid SubscriptionDto dto){
        ResponseEntity<UserDto> responserUserDto ;
        Optional<CourseModel> courseModelOptional = courseService.findByid(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), dto.getUserId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists.");
        }
        try {
            responserUserDto = courseUserService.getOneUserById(dto.getUserId());
            if(responserUserDto.getBody().getUserStatus().equals(UserStatus.BLOCKED)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User is blocked");
            }
        }catch (HttpStatusCodeException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        }
        //salva e envia para service user a inscrição
        CourseUserModel courseUserModel= courseUserService.saveAndSendSubscriptionUserInCourse(courseModelOptional.get().convertToCourseUserModel(dto.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }

    @DeleteMapping("courses?users/{userId}")
    public ResponseEntity<Object> deleteCourseUserByUser( @PathVariable(value = "userId") UUID userId){
       if(!courseUserService.existsByUserId(userId)){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser not found");
       }
       courseUserService.deleteCourseUserByUser(userId);
       return ResponseEntity.status(HttpStatus.OK).body("CourseUser deleted successfully");
    }

}
