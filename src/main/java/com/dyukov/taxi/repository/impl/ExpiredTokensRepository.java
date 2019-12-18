package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.ExpiredToken;
import com.dyukov.taxi.repository.IExpiredTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;

@Repository
@Transactional
public class ExpiredTokensRepository implements IExpiredTokensRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public ExpiredToken addToBlackList(String token) {
        ExpiredToken expiredToken = new ExpiredToken();
        expiredToken.setTokenValue(token);
        expiredToken.setExpireDatetime(new Date());
        entityManager.persist(expiredToken);
        entityManager.flush();
        return expiredToken;
    }

    @Override
    public ExpiredToken getFromBlackList(String token) {
        if (token == null || token.isEmpty())
            return null;
        try {
            String sql = "select e from " + ExpiredToken.class.getName() + " e " +
                    "where e.tokenValue = :tokenValue";
            Query query = entityManager.createQuery(sql);
            query.setParameter("tokenValue", token);
            return (ExpiredToken) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
