package com.ead.course.dtos;

import com.ead.course.enuns.CourseLevel;
import com.ead.course.enuns.CourseStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CourseDto {

    private UUID courseId;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String imageUrl;
    @NotNull
    private CourseStatus courseStatus;
    @NotNull
    private CourseLevel courseLevel;
    @NotNull
    private UUID UserInstructor;
}
