package com.ead.authuser.services.impl;

import com.ead.authuser.Repositories.UserRepository;
import com.ead.authuser.clients.CourseFeignclient;
import com.ead.authuser.clients.CourseRestTemplateClient;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    //caso queira usar o feignclient
    @Autowired
    private CourseFeignclient courseFeignclient;

    //caso opte pelo restTemplate
    @Autowired
    private CourseRestTemplateClient courseRestTemplateClient;

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById( UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    @Override
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public void save(UserModel userModel) {
        userRepository.save(userModel);
    }

    @Override
    public boolean existByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existByUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec,Pageable pageable ) {
        return userRepository.findAll(spec,pageable);
    }
}
