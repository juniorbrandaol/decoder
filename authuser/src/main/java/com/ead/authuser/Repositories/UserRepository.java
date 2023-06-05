package com.ead.authuser.Repositories;

import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

    /*como o model de user a coleção de roles está anotada como fetch tipo lazy, ultilizo
    * essa anotação (EntityGraph, para trazer a coleção de roles junto*/
    @EntityGraph(attributePaths = "roles" , type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserModel> findByUserName(String username);

    @EntityGraph(attributePaths = "roles" , type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserModel> findById(UUID userId);


}
