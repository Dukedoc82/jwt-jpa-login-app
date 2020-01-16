package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.ActivationToken;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.exception.InvalidRegistrationTokenException;
import com.dyukov.taxi.repository.IActivationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;

@Repository
@Transactional
public class ActivationTokenRepository implements IActivationTokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(ActivationTokenRepository.class);

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
        String sql = "delete from " + ActivationToken.class.getName() + " e " +
                "where e.tokenValue = :tokenValue";
        Query query = entityManager.createQuery(sql);
        query.setParameter("tokenValue", tokenValue);
        return query.executeUpdate();
    }

    @Override
    public int deleteOutdatedTokens() {
        String sql = "delete from " + ActivationToken.class.getName() + " e " +
                "where e.expireDatetime < :currentDate";
        Query query = entityManager.createQuery(sql);
        query.setParameter("currentDate", new Date());
        int removedCount = query.executeUpdate();
        logger.info(removedCount + " tokens removed from expired activation tokens.");
        return removedCount;
    }
}
