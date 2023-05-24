package com.ead.course.dtos;

import com.ead.course.enuns.UserStatus;
import com.ead.course.enuns.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
  private String userName;
  private String email;
  private String fullName;
  private UserStatus userStatus;
  private UserType userType;
  private String phoneNumber;
  private String cpf;
  private String imageUrl;
}
