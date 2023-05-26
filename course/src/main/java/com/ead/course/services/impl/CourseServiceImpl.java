package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserFeignClient;
import com.ead.course.clients.AuthUserRestTemplateClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseUserRepository courseUserRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;

    //para usar o feignclient
    @Autowired
    private CourseUserService courseUserService;

    //para usar o restTemplate
    @Autowired
    private AuthUserRestTemplateClient authUserRestTemplateClient;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {

        boolean deleteCourseUserInAuthUser = false;
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if(!moduleModelList.isEmpty()){
            for(ModuleModel module: moduleModelList) {
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if(!lessonModelList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }
        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
        if(!courseUserModelList.isEmpty()){
             courseUserRepository.deleteAll(courseUserModelList);
             deleteCourseUserInAuthUser = true;
        }
        courseRepository.delete(courseModel);
        //envia para o service authuser deletar tambem esse curso na sua base
        if(deleteCourseUserInAuthUser){
            courseUserService.deleteCourseInAuthUser(courseModel.getCourseId());
        }
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findByid(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec,pageable);
    }


}
