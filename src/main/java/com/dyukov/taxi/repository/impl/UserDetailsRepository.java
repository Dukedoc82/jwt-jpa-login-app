package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public TpUser findUserAccount(String userName) {
        try {
            String sql = "Select e from " + TpUser.class.getName() + " e " //
                    + " Where e.userName = :userName ";

            Query query = entityManager.createQuery(sql, TpUser.class);
            query.setParameter("userName", userName);

            return (TpUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Collection<TpUser> findAll() {
        try {
            String sql = "Select e from " + TpUser.class.getName() + " e";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public TpUser findUserAccount(Long userId) {
        try {
            String sql = "Select e from " + TpUser.class.getName() + " e " //
                    + " Where e.userId = :userId ";

            Query query = entityManager.createQuery(sql, TpUser.class);
            query.setParameter("userId", userId);

            return (TpUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
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
}
