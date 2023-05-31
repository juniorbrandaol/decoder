package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseValidator courseValidator;
    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDto courseDto, Errors erros){
        log.debug("Post saveCourse courseDto received {} ",courseDto.toString());
        courseValidator.validate(courseDto,erros);
        if(erros.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros.getAllErrors());
        }
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto,courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseService.save(courseModel);
        log.debug("Post saveCourse courseModel saved {} ",courseModel.toString());
        log.info("Course saved successfolly courseId {} ",courseModel.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse( @PathVariable("courseId") UUID courseId){

        log.debug("Delete deleteCourse courseId received {} ",courseId);
        Optional<CourseModel> courseModelOptional = courseService.findByid(courseId);
        if(!courseModelOptional.isPresent()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        courseService.delete(courseModelOptional.get());
        log.debug("Delete deleteCourse courseId deleted {} ",courseId);
        log.info("Course deleted successfully courseId {} ",courseId);
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfuly");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse( @PathVariable("courseId") UUID courseId,
                                                @Valid @RequestBody CourseDto courseDto){
        Optional<CourseModel> courseModelOptional = courseService.findByid(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        var courseModel = courseModelOptional.get();
        courseModel.setName(courseDto.getName());
        courseModel.setDescription(courseDto.getDescription());
        courseModel.setImageUrl(courseDto.getImageUrl());
        courseModel.setCourseStatus(courseDto.getCourseStatus());
        courseModel.setCourseLevel(courseDto.getCourseLevel());
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseService.save(courseModel);
        return  ResponseEntity.status(HttpStatus.OK).body(courseModel);
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                @PageableDefault(page = 0,size = 10,sort = "courseId", direction = Sort.Direction.ASC)
                Pageable pageable, @RequestParam(required = false) UUID userId
           ){
         if(userId!=null){
            return ResponseEntity.status(HttpStatus.OK)
                     .body(courseService.findAll(SpecificationTemplate.CourseUserId(userId).and(spec),pageable));
         }else{
            return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec,pageable));
         }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse( @PathVariable("courseId")UUID courseId ){
        Optional<CourseModel> courseModelOptional = courseService.findByid(courseId);
        if(!courseModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());
    }

}
