package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.AuthenticationCurrentUserService;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationCurrentUserService authenticationCurrentUserService;

    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0,size = 10,sort = "creationDate", direction = Sort.Direction.DESC)
            Pageable pageable, Authentication authentication
    ){
        log.info("Authentication {}",authenticationCurrentUserService.getCurrentUser());
        Page<UserModel> userModelPage = userService.findAll(spec,pageable);
        if(!userModelPage.isEmpty()){
            for (UserModel user:userModelPage.toList()){
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable UUID userId ){

        UUID currentUserId =  authenticationCurrentUserService.getCurrentUser().getUserId();
        if(currentUserId.equals(userId)) {
            Optional<UserModel> userModel = userService.findById(userId);
            if (!userModel.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(userModel.get());
            }
        }else{
            throw new AccessDeniedException("Forbidden");
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID userId ){
        Optional<UserModel> userModel = userService.findById(userId);
        log.debug("DELETE deleteUser userId received {} ",userId);
        if(!userModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            userService.deleteUser(userModel.get());
            log.debug("DELETE deleteUser userId deleted {} ",userId);
            log.info("User deleted successfully userId: {} ",userId);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted success");
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable UUID userId,
            @JsonView(UserDto.UserView.UserPut.class)
            @RequestBody @Validated(UserDto.UserView.UserPut.class) UserDto userDto
            ){
        log.debug("PUT updateUser userDto received {} ",userDto.toString());
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            var userModel = userModelOptional.get();
            userModel.setFullName(userDto.getFullName());
            userModel.setPhoneNumber(userDto.getPhoneNumber());
            userModel.setCpf(userDto.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.updateUser(userModel);
            log.debug("PUT updateUser userId updated {} ",userModel.getUserId());
            log.info("User updated successfully userId: {} ",userModel.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(
            @PathVariable UUID userId,
            @JsonView(UserDto.UserView.PasswordPut.class)
            @RequestBody @Validated(UserDto.UserView.PasswordPut.class)UserDto userDto
    ){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if(!userModelOptional.get().getPassword().equals(userDto.getOldPassword())){
            log.warn("Error: Mismatched old password. userId: {} ",userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password ");
        }
        else{
            var userModel = userModelOptional.get();
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.updatePassword(userModel);
            return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(
            @PathVariable UUID userId,
            @JsonView(UserDto.UserView.ImagePut.class)
            @RequestBody @Validated(UserDto.UserView.ImagePut.class) UserDto userDto
    ){
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        else{
            var userModel = userModelOptional.get();
            userModel.setImageUrl(userDto.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.updateUser(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

}
