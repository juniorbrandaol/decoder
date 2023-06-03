package com.ead.authuser.dtos;



import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor//cria construtores apenas com campos obrigatórios
public class JwtDto {

  @NonNull
  private String token;
  private String type= "Bearer";

}
