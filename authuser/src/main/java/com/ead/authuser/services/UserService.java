package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAllUsers();

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userId);

    void save(UserModel userModel);

    boolean existByUserName(String userName);

    boolean existByUserEmail(String email);
}
