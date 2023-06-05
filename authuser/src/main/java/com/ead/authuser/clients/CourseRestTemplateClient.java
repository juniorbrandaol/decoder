package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class CourseRestTemplateClient {

   @Value("${ead.api.url.course}")
   private String REQUEST_URL_COURSE;
   @Autowired
   private RestTemplate restTemplate;

   @Autowired
   private UtilsService utilsService;

   @CircuitBreaker(name = "vircuitBreakerIntance")
   public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable,String token){
      List<CourseDto> result = null;
      String url = REQUEST_URL_COURSE+ utilsService.createUrlGetAllCoursesByUser(userId, pageable);

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization",token);
      HttpEntity<String> requestEntity = new HttpEntity<String>("parameters",headers);

      log.debug("debug url: {} ",url);
      log.info("info url: {} ",url);

      ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType = new  ParameterizedTypeReference<ResponsePageDto<CourseDto>>(){};
      ResponseEntity<ResponsePageDto<CourseDto>> response = restTemplate.exchange(url,HttpMethod.GET,requestEntity,responseType);
      result = response.getBody().getContent();
      log.debug("Response number of elements: {} ",result.size());

      log.info("Ending request /courses userId{}",userId);
      return new PageImpl<>(result);
   }

}
