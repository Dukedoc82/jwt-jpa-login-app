package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserRole;

import java.util.Collection;

public interface IUserRoleRepository {

    Collection getRoleNames(Long userId);

    UserRole saveUserRole(TpUser tpUser);

    UserRole saveAdminRole(TpUser tpUser);

    UserRole saveDriverRole(TpUser tpUser);
}
