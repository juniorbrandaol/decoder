package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "TB_MODULES")
@Entity
public class ModuleModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID moduleId;
    @Column(nullable = false,length = 150)
    private String title;
    @Column(nullable = false,length = 250)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyy HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime criationDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//defique que será um acesso apenas de escrita, será ocultado de visualização
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private CourseModel course;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "module",fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<LessonModel> lessons;

}
