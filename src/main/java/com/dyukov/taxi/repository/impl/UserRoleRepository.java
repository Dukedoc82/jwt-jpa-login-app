package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.TpRole;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserRole;
import com.dyukov.taxi.exception.RoleNotFoundException;
import com.dyukov.taxi.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collection;

@Repository
@Transactional
public class UserRoleRepository implements IUserRoleRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Collection getSystemRoles() {
        String sql = "Select r from " + TpRole.class.getName() + " r";
        Query query = entityManager.createQuery(sql);
        return query.getResultList();
    }

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

    @Override
    public TpRole getRoleByName(String roleName) {
        try {
            String sql = "select r from " + TpRole.class.getName() + " r " +
                    "where r.roleName = :roleName";
            Query query = entityManager.createQuery(sql);
            query.setParameter("roleName", roleName);

            return (TpRole) query.getSingleResult();
        } catch (NoResultException e) {
            throw new RoleNotFoundException("Role " + roleName + "is not found in the database.", e);
        }
    }

    @Override
    public TpRole getRoleById(Long id) {
        try {
            String sql = "select r from " + TpRole.class.getName() + " r " +
                    "where r.roleId = : id";
            Query query = entityManager.createQuery(sql);
            query.setParameter("id", id);
            return (TpRole) query.getSingleResult();
        } catch (NoResultException e) {
            throw new RoleNotFoundException("Role #" + id + "is not found in the database.", e);
        }
    }

    @Override
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public UserRole updateRole(TpUser tpUser, Long roleId) {
        String getUserRoleSql = "Select ur from " + UserRole.class.getName() + " ur " //
                + " where ur.tpUser.userId = :userId ";
        Query getUserRoleQuery = entityManager.createQuery(getUserRoleSql);
        getUserRoleQuery.setParameter("userId", tpUser.getUserId());
        UserRole userRole = (UserRole) getUserRoleQuery.getSingleResult();
        TpRole role = getRoleById(roleId);
        userRole.setTpRole(role);
        entityManager.merge(userRole);
        entityManager.flush();
        return userRole;
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
