package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthUserRestTemplateClient {

   @Value("${ead.api.url.authuser}")
   private String REQUEST_URL_AUTHUSER;
    @Autowired
   private UtilsService utilsService;

    @Autowired
    private RestTemplate restTemplate;

    public Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable){
        List<UserDto> result = null;
        String url =REQUEST_URL_AUTHUSER+ utilsService.createUrlGetAllUsersByCourse(courseId, pageable);

        log.debug("debug url: {} ",url);
        log.info("info url: {} ",url);
        try{
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new  ParameterizedTypeReference<ResponsePageDto<UserDto>>(){};
            ResponseEntity<ResponsePageDto<UserDto>> response = restTemplate.exchange(url, HttpMethod.GET,null,responseType);
            result = response.getBody().getContent();
            log.debug("Response number of elements: {} ",result.size());
        }catch (HttpStatusCodeException e){
            log.error("Error request /users {}",e);
        }
        log.info("Ending request /users courseId {}",courseId);
        return new PageImpl<>(result);
    }

    public ResponseEntity<UserDto> getOneUserById(UUID userid){
        String url = REQUEST_URL_AUTHUSER +"/users/"+userid;
        return restTemplate.exchange(url,HttpMethod.GET,null,UserDto.class);
    }

    public void postSubscriptionUserInCourse(CourseUserDto courseUserDto,UUID userCourseId) {
        String url = REQUEST_URL_AUTHUSER +"/users/" + userCourseId+ "/courses/subscription";
        restTemplate.postForObject(url,courseUserDto,String.class);
    }

    public void deleteCourseInAuthUser(UUID courseId) {
        String url = REQUEST_URL_AUTHUSER +"/users/courses/"+courseId;
        restTemplate.exchange(url,HttpMethod.DELETE,null,String.class);
    }
}
