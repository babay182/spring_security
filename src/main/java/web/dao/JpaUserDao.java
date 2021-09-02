package web.dao;


import org.springframework.stereotype.Component;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional
public class JpaUserDao {

    @PersistenceContext
    private EntityManager em;

    public List<User> index() { return em.createQuery("SELECT u FROM User u", User.class).getResultList(); }

    public User show(int count) { return em.find(User.class, count); }

    public void save(User user) {
        em.persist(user);
    }

    @Transactional
    public void update(User updatedUser) {
        em.merge(updatedUser);
    }

    @Transactional
    public void delete(int count) {
        em.remove(em.find(User.class, count));
    }

    public User getUserById(int count) {
        return em.find(User.class, count);
    }


    public User getUserByUsername(String name) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.name=:name",
                User.class).setParameter("name", name);
        return query.getSingleResult();
    }
}
