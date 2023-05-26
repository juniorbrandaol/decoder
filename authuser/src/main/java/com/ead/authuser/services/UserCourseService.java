package com.ead.authuser.services;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserCourseService {
    Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable);

    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

    UserCourseModel save(UserCourseModel userCourseModel);

    boolean existsByCourseId(UUID courseId);

    void deleteUserCourseByCourse(UUID courseId);
}
