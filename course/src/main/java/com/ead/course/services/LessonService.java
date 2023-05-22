package com.ead.course.services;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {
    Optional<LessonModel> findByid(UUID moduleId);

    LessonModel save(LessonModel lessonModel);


    Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);

    void delete(LessonModel lessonModel);

    List<LessonModel> findAllByModel(UUID moduleId);
}
