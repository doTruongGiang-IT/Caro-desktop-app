package com.sgu.caro.repository;

import com.sgu.caro.entity.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<User> getById(long id){
        System.out.println(entityManager);
        Query query = entityManager.createNativeQuery("SELECT * FROM users WHERE id = ?", User.class);
        query.setParameter(1, id);

        return query.getResultList();
    }
}
