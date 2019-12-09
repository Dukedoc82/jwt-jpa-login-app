package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.ActualOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ActualOrderRepository {

    @Autowired
    private EntityManager entityManager;

    public List<ActualOrder> getAll() {
        try {
            String sql = "Select e from " + ActualOrder.class.getName() + " e";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}
