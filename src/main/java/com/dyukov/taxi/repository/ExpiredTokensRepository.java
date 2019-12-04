package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.ExpiredToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class ExpiredTokensRepository {

    @Autowired
    private EntityManager entityManager;

    public void invalidateToken(ExpiredToken expiredToken) {
        entityManager.persist(expiredToken);
        entityManager.flush();
    }

    public ExpiredToken getExpiredToken(String token) {
        try {
            String sql = "Select e from " + ExpiredToken.class.getName() + " e " //
                    + " Where e.token = :token ";
            Query query = entityManager.createQuery(sql, ExpiredToken.class);
            query.setParameter("token", token);
            return (ExpiredToken) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
