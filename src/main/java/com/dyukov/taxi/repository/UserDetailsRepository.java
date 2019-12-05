package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.TpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class UserDetailsRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public TpUser findUserAccount(String userName) {
        try {
            String sql = "Select e from " + TpUser.class.getName() + " e " //
                    + " Where e.userName = :userName ";

            Query query = entityManager.createQuery(sql, TpUser.class);
            query.setParameter("userName", userName);

            return (TpUser) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("NULL NULL NULL NULL NULL NULL NULL NULL NULL NULL NULL NULL NULL NULL");
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
        userRoleRepository.saveUserRole(tpUser);
        userRoleRepository.saveAdminRole(tpUser);
        entityManager.flush();
        return tpUser;
    }
}
