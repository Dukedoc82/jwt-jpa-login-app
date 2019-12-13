package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserRole;
import com.dyukov.taxi.exception.TaxiServiceException;
import com.dyukov.taxi.exception.UserNotFoundException;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;

@Repository
@Transactional
public class UserDetailsRepository implements IUserDetailsRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Cacheable("users")
    public TpUser findUserAccount(String userName) {
        try {
            String sql = "Select e from " + TpUser.class.getName() + " e " //
                    + " Where e.userName = :userName ";

            Query query = entityManager.createQuery(sql, TpUser.class);
            query.setParameter("userName", userName);

            TpUser user = (TpUser) query.getSingleResult();
            user.setRoleNames(userRoleRepository.getRoleNames(user.getUserId()));

            return user;
        } catch (NoResultException e) {
            throw new UserNotFoundException(String.format(TaxiServiceException.USER_NAME_DOES_NOT_EXIST, userName));
        }
    }

    public Collection findAll() {
        try {
            String sql = "Select e from " + TpUser.class.getName() + " e";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Cacheable("users")
    public TpUser findUserAccount(Long userId) {
        try {
            String sql = "Select e from " + TpUser.class.getName() + " e " //
                    + " Where e.userId = :userId ";

            Query query = entityManager.createQuery(sql, TpUser.class);
            query.setParameter("userId", userId);
            TpUser user = (TpUser) query.getSingleResult();
            user.setRoleNames(userRoleRepository.getRoleNames(userId));
            return user;
        } catch (NoResultException e) {
            throw new UserNotFoundException(userId);
        }
    }

    public TpUser saveUser(TpUser tpUser) {
        entityManager.persist(tpUser);
        userRoleRepository.saveUserRole(tpUser);
        entityManager.flush();
        return tpUser;
    }

    public TpUser saveAdmin(TpUser tpUser) {
        entityManager.persist(tpUser);
        userRoleRepository.saveAdminRole(tpUser);
        entityManager.flush();
        return tpUser;
    }

    public TpUser saveDriver(TpUser tpUser) {
        entityManager.persist(tpUser);
        userRoleRepository.saveDriverRole(tpUser);
        entityManager.flush();
        return tpUser;
    }

    @Override
    public Collection findDrivers() {
        return findUsersByRole("ROLE_DRIVER");
    }

    @Override
    public Collection findAdmins() {
        return findUsersByRole("ROLE_ADMiN");
    }

    @Override
    public Collection findUsers() {
        return findUsersByRole("ROLE_USER");
    }

    private Collection findUsersByRole(String roleName) {
        try {
            String sql = "Select e from " + TpUser.class.getName() + " e, "  + UserRole.class.getName() + " r " +
                    "where e.userId = r.tpUser.userId " +
                    "and r.tpRole.roleName = :roleName";
            Query query = entityManager.createQuery(sql);
            query.setParameter("roleName", roleName);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList();
        }
    }
}
