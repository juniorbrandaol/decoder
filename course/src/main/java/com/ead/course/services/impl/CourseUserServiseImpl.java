package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserFeignClient;
import com.ead.course.clients.AuthUserRestTemplateClient;
import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import com.ead.course.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class CourseUserServiseImpl implements CourseUserService {

    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL_AUTHUSER;
    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private UtilsService utilsService;

    // client user restTempalte
    @Autowired
    private AuthUserRestTemplateClient authUserRestTemplateClient;

    //client user feignClient
    @Autowired
    private AuthUserFeignClient authUserFeignClient;

    @CircuitBreaker(name = "curcuitbreakerInstance")
    @Override
    public Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable){
        List<UserDto> result = null;
        String url =REQUEST_URL_AUTHUSER+ utilsService.createUrlGetAllUsersByCourse(courseId, pageable);
        log.debug("debug url: {} ",url);
        log.info("info url: {} ",url);
        try{
            result =  authUserFeignClient.getAllUsersByCourse(courseId, pageable).getContent();
            log.debug("Response number of elements: {} ",result.size());
        }catch (HttpStatusCodeException e){
            log.error("Error request /users {}",e);
        }
        log.info("Ending request /users courseId {}",courseId);
        return new PageImpl<>(result);
    }

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
       return courseUserRepository.existsByCourseAndUserId(courseModel,userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return courseUserRepository.save(courseUserModel);
    }

    @Override
    public ResponseEntity<UserDto> getOneUserById(UUID userid){
        return authUserFeignClient.getOneUserById(userid);
    }

    @Transactional
    @Override
    public CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel) {
        courseUserModel =  courseUserRepository.save(courseUserModel);
        //após salvar, enviar para user service a informação
        var courseUserDto = new CourseUserDto();
        courseUserDto.setUserId(courseUserModel.getUserId());
        courseUserDto.setCourseId(courseUserModel.getCourse().getCourseId());
        authUserFeignClient.postSubscriptionUserInCourse(courseUserDto,courseUserDto.getUserId());
        return courseUserModel;
    }

    @Transactional
    @Override
    public void deleteCourseInAuthUser(UUID courseId) {
        authUserFeignClient.deleteCourseInAuthUser(courseId);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return courseUserRepository.existsByUserId(userId);
    }

    @Transactional
    @Override
    public void deleteCourseUserByUser(UUID userId) {
       courseUserRepository.deleteAllByUserId(userId);
    }


    //método para ser usando apenas se quiser uma rota alternativa , caso dê problema na rota, usando fallbackMethod
    public Page<UserDto> circuitbreakerfallback(UUID courseId, Pageable pageable,Throwable t){
        log.error("Inside circuitbreaker circuitbraekerfallback cause- {} ",t.toString());
        List<UserDto> result = new ArrayList<>();
        log.error("========== SISTEMA DE CURSOS FORA DO AR ========= ");
        return new PageImpl<>(result);
    }

}
