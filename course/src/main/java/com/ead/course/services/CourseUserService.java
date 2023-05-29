package com.ead.course.services;

import com.ead.course.dtos.UserDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface CourseUserService {
    Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable);
    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);
    CourseUserModel save(CourseUserModel courseUserModel);
    ResponseEntity<UserDto> getOneUserById(UUID userid);
    CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel);
    void deleteCourseInAuthUser(UUID courseId);

    boolean existsByUserId(UUID userId);

    void deleteCourseUserByUser(UUID userId);
}
