package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.repository.IUserRoleRepository;
import com.dyukov.taxi.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IUserRoleRepository roleRepository;

    @Override
    public Collection getRoles() {
        return roleRepository.getSystemRoles();
    }
}
