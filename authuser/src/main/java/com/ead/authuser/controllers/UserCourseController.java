package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseRestTemplateClient;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
@Log4j2
@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class UserCourseController {

    /*caso opte por usar o serviço de restTemplate*/
    @Autowired
    private CourseRestTemplateClient courseRestTemplateClient;

    /* caso opte pelo serviço de feignclient*/
   @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private UserService userService;

  @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("users/{userId}/courses")
    public ResponseEntity<Object> getAllCoursesByUser(
          @PageableDefault(page = 0,size = 10,sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
          @PathVariable(value = "userId") UUID userId,
          @RequestHeader("Authorization") String token
       ){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        }
       return ResponseEntity.status(HttpStatus.OK).body(userCourseService.getAllCoursesByUser(userId,pageable,token));
    }


}
