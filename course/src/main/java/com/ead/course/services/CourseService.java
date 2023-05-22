package com.ead.course.services;

import com.ead.course.models.CourseModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);
    CourseModel save(CourseModel courseModel);

    Optional<CourseModel> findByid(UUID  courseId);
    List<CourseModel> findAll();
}
