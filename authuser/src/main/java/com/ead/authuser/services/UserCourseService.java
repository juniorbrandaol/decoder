package com.ead.authuser.services;

import com.ead.authuser.dtos.CourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserCourseService {
    Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable,String token);
}
