package com.ead.authuser.services.impl;

import com.ead.authuser.Repositories.UserCourseRepository;
import com.ead.authuser.clients.CourseFeignclient;
import com.ead.authuser.clients.CourseRestTemplateClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class UserCourseServiceImpl implements UserCourseService {

    @Value("${ead.api.url.course}")
    private String REQUEST_URI;
    @Autowired
    private UtilsService utilsService;

    @Autowired
    private CourseFeignclient userFeignclient;

    @Autowired
    private CourseRestTemplateClient courseRestTemplateClient;
    @Autowired
    private UserCourseRepository userCourseRepository;

   // @Retry(name = "retryIntance", fallbackMethod ="retryfallback") //circuit break
    @CircuitBreaker(name = "curcuitbreakerInstance")
    @Override
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable)  {
        List<CourseDto> result = null;
        String url =REQUEST_URI+ utilsService.createUrlGetAllCoursesByUser(userId, pageable);
        log.debug("debug url: {} ",url);
        log.info("info url: {} ",url);
        System.out.println("--START REQUEST COURSE MICROSERVICE---");
        try{
            result =  userFeignclient.getAllCoursesByUser(userId, pageable).getContent();
            log.debug("Response number of elements: {} ",result.size());
        } catch (HttpStatusCodeException e){
            log.error("Error request /courses{}",e);
        }
        log.info("Ending request /courses userId{}",userId);
        return new PageImpl<>(result);
    }

    @Override
    public boolean existsByUserAndCourseId(UserModel userModel, UUID courseId) {
        return userCourseRepository.existsByUserAndCourseId(userModel,courseId);
    }

    @Override
    public UserCourseModel save(UserCourseModel userCourseModel) {
        return userCourseRepository.save(userCourseModel);
    }

    @Override
    public boolean existsByCourseId(UUID courseId) {
        return userCourseRepository.existsByCourseId(courseId);
    }

    @Transactional
    @Override
    public void deleteUserCourseByCourse(UUID courseId) {
        userCourseRepository.deleteAllByCourseId(courseId);
    }

    //método para ser usando apenas se quiser uma rota alternativa , caso dê problema na rota, usando fallbackMethod
    public Page<CourseDto> circuitbreakerfallback(UUID userId, Pageable pageable,Throwable t) {
        log.error("Inside circuitbreaker circuitbraekerfallback cause- {} ",t.toString());
        List<CourseDto> result = new ArrayList<>();
        log.error("========== SISTEMA DE CURSOS FORA DO AR ========= ");
        return new PageImpl<>(result);
    }

    public Page<CourseDto> retryfallback(UUID userId, Pageable pageable,Throwable t) {
      log.error("Inside retry retryfallback cause- {} ",t.toString());
      List<CourseDto> result = new ArrayList<>();
      return new PageImpl<>(result);
    }

}
