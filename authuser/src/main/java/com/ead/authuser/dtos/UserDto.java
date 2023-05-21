package com.ead.authuser.dtos;

import com.ead.authuser.enuns.UserStatus;
import com.ead.authuser.enuns.UserType;
import com.ead.authuser.validations.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    // uma outra opção de se criar vários dto's para visoes é cruar viwes
    // Serve para criar visões de dto's diferentes, para cada ocasião
    public interface UserView{
        public static interface RegistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }
    private UUID userId;

    //validação customizada
    @UsernameConstraint(groups = UserView.RegistrationPost.class)
    @Size(min = 4,max = 50,message = "Username deve conter no mínmo {min} e no máximo {max} carcateres.",
            groups = UserView.RegistrationPost.class
         )
    @NotBlank(groups = UserView.RegistrationPost.class,message = "Username não pode estar vazio")
    @JsonView(UserView.RegistrationPost.class)
    private String userName;
    @Size(min = 4,max = 50,message = "Email deve conter no mínmo {min} e no máximo {max} carcateres.",
            groups = UserView.RegistrationPost.class
         )
    @NotBlank(groups = UserView.RegistrationPost.class,message = "Email não pode estar vazio")
    @Email(message = "Email inválido")
    @JsonView({UserView.RegistrationPost.class})
    private String email;
    @Size(min = 6,max = 20,message = "Password deve conter no mínmo {min} e no máximo {max} carcateres.",
            groups={ UserView.RegistrationPost.class,UserView.PasswordPut.class}
         )
    @NotBlank(groups = {UserView.RegistrationPost.class,UserView.PasswordPut.class},message = "Password não pode estar vazio")
    @JsonView({UserView.RegistrationPost.class,UserView.PasswordPut.class})
    private String password;
    @Size(min = 6,max = 20,message = "Password deve conter no mínmo {min} e no máximo {max} carcateres.",
            groups =UserView.PasswordPut.class
         )
    @NotBlank(groups =UserView.PasswordPut.class,message = "Oldpassword não pode estar vazio" )
    @JsonView({UserView.PasswordPut.class})
    private String oldPassword;
    @JsonView({UserView.RegistrationPost.class,UserView.UserPut.class})
    private String fullName;
    @JsonView({UserView.RegistrationPost.class,UserView.UserPut.class})
    private String phoneNumber;
    @JsonView({UserView.RegistrationPost.class,UserView.UserPut.class})
    private String cpf;
    @NotBlank(groups = UserView.ImagePut.class,message = "Image não pode estar vazio")
    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
