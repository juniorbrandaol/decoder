package com.ead.course.services;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {
    Optional<LessonModel> findByid(UUID moduleId);

    LessonModel save(LessonModel lessonModel);


    Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);

    void delete(LessonModel lessonModel);

    List<LessonModel> findAllByModel(UUID moduleId);

    Page<LessonModel> findAllByModel(Specification<LessonModel> spec, Pageable pageable);
}
