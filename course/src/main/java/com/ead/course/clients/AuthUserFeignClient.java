package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.UserDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
//"${ead.api.url.authuser}"
@Component
@FeignClient(name="EAD-AUTHUSER-SERVICE",url = "http://localhost:8080/ead-authuser",path = "/users")
public interface AuthUserFeignClient {

    @GetMapping("?courseId={courseId}")
    @ResponseStatus(HttpStatus.OK)
    Page<UserDto> getAllUsersByCourse(@PathVariable(required = false) UUID courseId, Pageable pageable);

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UserDto> getOneUserById(@PathVariable(value = "userId") UUID userid);

    @PostMapping("/{userId}/courses/subscription")
    @ResponseStatus(HttpStatus.CREATED)
    void postSubscriptionUserInCourse( @RequestBody @Valid CourseUserDto userCourseDto, @PathVariable(value = "userId") UUID userId);

    @DeleteMapping("/courses/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    void deleteCourseInAuthUser(  @PathVariable(value = "courseId")  UUID courseId);
}
