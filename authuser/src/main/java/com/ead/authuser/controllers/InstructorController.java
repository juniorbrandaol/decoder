package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.enuns.RoleType;
import com.ead.authuser.enuns.UserType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@CrossOrigin(origins = "*",maxAge = 3600)
@Log4j2
@RestController
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /*Ppor se tratar de um instructor e utilizar de um dto específico, preferi utilizar o post,
     mas poderia ser um put de user como fizemos nos outros casos.*/
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor( @RequestBody @Valid InstructorDto instructorDto){
        Optional<UserModel> userModelOptional = userService.findById(instructorDto.getUserId());
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_INSTRUCTOR)
                    .orElseThrow(()->new RuntimeException("Erro: role is not found"));
            var userModel = userModelOptional.get();
            userModel.setUserType(UserType.INSTRUCTOR);
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userModel.getRoles().add(roleModel);
            userService.updateUser(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }


}
