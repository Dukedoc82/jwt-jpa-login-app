package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.TpRole;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserRole;
import com.dyukov.taxi.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

@Repository
@Transactional
public class UserRoleRepository implements IUserRoleRepository {

    @Autowired
    private EntityManager entityManager;

    @Cacheable("roles")
    public Collection getRoleNames(Long userId) {
        String sql = "Select ur.tpRole.roleName from " + UserRole.class.getName() + " ur " //
                + " where ur.tpUser.userId = :userId ";

        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public UserRole saveUserRole(TpUser tpUser) {
       return saveRole(tpUser, "ROLE_USER");
    }

    public UserRole saveAdminRole(TpUser tpUser) {
        return saveRole(tpUser, "ROLE_ADMIN");
    }

    public UserRole saveDriverRole(TpUser tpUser) {
        return saveRole(tpUser, "ROLE_DRIVER");
    }

    private UserRole saveRole(TpUser tpUser, String roleName) {
        String sql = "Select e from " + TpRole.class.getName() + " e " //
                + " Where e.roleName = :roleName ";
        Query query = entityManager.createQuery(sql, TpRole.class);
        query.setParameter("roleName", roleName);
        TpRole appUserRole = (TpRole) query.getSingleResult();
        UserRole userRole = new UserRole();
        userRole.setTpRole(appUserRole);
        userRole.setTpUser(tpUser);
        entityManager.persist(userRole);
        entityManager.flush();
        return userRole;
    }

}
