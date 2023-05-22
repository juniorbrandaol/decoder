package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(  @Valid @RequestBody CourseDto courseDto){

        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto,courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseService.save(courseModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse( @PathVariable("courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findByid(courseId);
        if(!courseModelOptional.isPresent()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        courseService.delete(courseModelOptional.get());
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
    public ResponseEntity<List<CourseModel>> findAllCourses(){
       return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll());
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