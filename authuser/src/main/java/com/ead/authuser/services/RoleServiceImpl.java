package com.ead.authuser.services;

import com.ead.authuser.Repositories.RoleRepository;
import com.ead.authuser.enuns.RoleType;
import com.ead.authuser.models.RoleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Optional<RoleModel> findByRoleName(RoleType roleType) {
    return roleRepository.findByRoleName(roleType);
  }
}
