package com.ead.authuser.dtos;

import com.ead.authuser.enuns.CourseLevel;
import com.ead.authuser.enuns.CourseStatus;
import lombok.Data;
import java.util.UUID;

@Data
public class CourseDto {

    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;
    private CourseStatus courseStatus;
    private CourseLevel courseLevel;
    private UUID UserInstructor;

}
