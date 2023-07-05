package com.ead.authuser.services.impl;

import com.ead.authuser.clients.CourseFeignclient;
import com.ead.authuser.clients.CourseRestTemplateClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;


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


   // @Retry(name = "retryIntance", fallbackMethod ="retryfallback") //circuit break
    @CircuitBreaker(name = "curcuitbreakerInstance")
    @Override
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable,String token)  {
        List<CourseDto> result = null;
        String url =REQUEST_URI+ utilsService.createUrlGetAllCoursesByUser(userId, pageable);

        //pegar o token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);
        HttpEntity<String> requestEntity = new HttpEntity<String>("parameters",headers);

        log.debug("debug url: {} ",url);
        log.info("info url: {} ",url);
        System.out.println("--START REQUEST COURSE MICROSERVICE---");
        result =  userFeignclient.getAllCoursesByUser(userId, pageable).getContent();
        log.debug("Response number of elements: {} ",result.size());

        log.info("Ending request /courses userId{}",userId);
        return new PageImpl<>(result);
    }



}
