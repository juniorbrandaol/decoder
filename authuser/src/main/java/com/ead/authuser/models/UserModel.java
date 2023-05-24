package com.ead.authuser.models;

import com.ead.authuser.enuns.UserStatus;
import com.ead.authuser.enuns.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="TB_USERS")
@JsonInclude(JsonInclude.Include.NON_NULL)//evita de carregar algum atributo cujo valor seja null. Será ocultado
@Data
public class UserModel extends RepresentationModel<UserModel> implements Serializable {
     private static final long serialVersionUID = 1L;

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID userId;
     @Column(nullable = false,unique = true,length = 50)
     private String userName;
     @Column(nullable = false,unique = true,length = 50)
     private String email;
     @JsonIgnore//quando serializado, será ocultado esse dados, oculto.evitando a visualização da senha
     @Column(nullable = false,length = 255)
     private String password;
     @Column(nullable = false,length = 150)
     private String fullName;
     @Column(nullable = false)
     @Enumerated(EnumType.STRING)
     private UserStatus userStatus;
     @Column(nullable = false)
     @Enumerated(EnumType.STRING)
     private UserType userType;
     @Column(length = 20)
     private String phoneNumber;
     @Column(nullable = false,length = 20)
     private String cpf;
     @Column
     private String imageUrl;
     @Column(nullable = false)
     @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
     private LocalDateTime creationDate;
     @Column(nullable = false)
     @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
     private LocalDateTime lastUpdateDate;

     @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)// acesso apenas de escrita
     @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
     private Set<UserCourseModel> usersCourses;


}
