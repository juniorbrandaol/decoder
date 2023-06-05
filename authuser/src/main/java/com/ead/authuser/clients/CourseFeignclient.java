package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Component
@FeignClient(name="EAD-COURSE-SERVICE",url = "http://localhost:8080/ead-course",path = "/courses")
public interface CourseFeignclient {

    @GetMapping("?userId={userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<CourseDto> getAllCoursesByUser(@PathVariable(required = false) UUID userId, Pageable pageable );


}
