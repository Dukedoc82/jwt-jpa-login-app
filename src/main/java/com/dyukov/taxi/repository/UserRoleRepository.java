package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.AppRole;
import com.dyukov.taxi.entity.AppUser;
import com.dyukov.taxi.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class UserRoleRepository {

    @Autowired
    private EntityManager entityManager;

    public List<String> getRoleNames(Long userId) {
        String sql = "Select ur.appRole.roleName from " + UserRole.class.getName() + " ur " //
                + " where ur.appUser.userId = :userId ";

        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public UserRole saveUserRole(AppUser appUser) {
       return saveRole(appUser, "ROLE_USER");
    }

    public UserRole saveAdminRole(AppUser appUser) {
        return saveRole(appUser, "ROLE_ADMIN");
    }

    private UserRole saveRole(AppUser appUser, String roleName) {
        String sql = "Select e from " + AppRole.class.getName() + " e " //
                + " Where e.roleName = :roleName ";
        Query query = entityManager.createQuery(sql, AppRole.class);
        query.setParameter("roleName", roleName);
        AppRole appUserRole = (AppRole) query.getSingleResult();
        UserRole userRole = new UserRole();
        userRole.setAppRole(appUserRole);
        userRole.setAppUser(appUser);
        entityManager.persist(userRole);
        entityManager.flush();
        return userRole;
    }



}
