package com.ead.authuser.services.impl;

import com.ead.authuser.Repositories.UserCourseRepository;
import com.ead.authuser.clients.CourseFeignclient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

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
    private UserCourseRepository userCourseRepository;

    @Override
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable){
        List<CourseDto> result = null;
        String url =REQUEST_URI+ utilsService.createUrlGetAllCoursesByUser(userId, pageable);
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
}
