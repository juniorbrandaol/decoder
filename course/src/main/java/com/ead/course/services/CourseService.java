package com.ead.course.services;

import com.ead.course.dtos.UserDto;
import com.ead.course.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);
    CourseModel save(CourseModel courseModel);

    Optional<CourseModel> findByid(UUID  courseId);
    Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);

}
