package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.ActivationToken;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.exception.InvalidRegistrationTokenException;
import com.dyukov.taxi.repository.IActivationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class ActivationTokenRepository implements IActivationTokenRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public ActivationToken persistActivationToken(String value, TpUser user) {
        ActivationToken activationToken = new ActivationToken(value, user);
        entityManager.persist(activationToken);
        entityManager.flush();
        return activationToken;
    }

    @Override
    public ActivationToken findToken(String tokenValue) {
        try {
            String sql = "select e from " + ActivationToken.class.getName() + " e " +
                    "where e.tokenValue = :tokenValue";
            Query query = entityManager.createQuery(sql);
            query.setParameter("tokenValue", tokenValue);
            return (ActivationToken) query.getSingleResult();
        } catch (NoResultException e) {
            throw new InvalidRegistrationTokenException("No token " + tokenValue + " found.", e);
        }
    }

    @Override
    public int deleteToken(String tokenValue) {
        String sql = "delete e from " + ActivationToken.class.getName() + " e " +
                "where e.tokenValue = :tokenValue";
        Query query = entityManager.createQuery(sql);
        query.setParameter("tokenValue", tokenValue);
        return query.executeUpdate();
    }
}
