package com.ead.authuser.services.impl;

import com.ead.authuser.clients.UserFeignclient;
import com.ead.authuser.configs.FeignclientLoggerConfig;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    private UtilsService utilsService;

    @Autowired
    private UserFeignclient userFeignclient;

    @Autowired
    private FeignclientLoggerConfig feignclientLoggerConfig;

    @Override
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable){
        List<CourseDto> result = null;
        String url = utilsService.createUrl(userId, pageable);
        log.debug("debug url: {} ",url);
        log.info("info url: {} ",url);
        try{
            result =  userFeignclient.getAllCoursesByUser(userId, pageable).getContent();
            log.debug("Response number of elements: {} ",result.size());
        }catch (HttpStatusCodeException e){
            log.error("Error request /courses{}",e);
        }
        log.info("Ending request /courses userId{}",userId);
        return new PageImpl<>(result);
    }
}
