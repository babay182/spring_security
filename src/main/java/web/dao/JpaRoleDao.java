package web.dao;

import org.springframework.stereotype.Component;
import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class JpaRoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Role role) {
        entityManager.persist(role);
    }

    public List<Role> getAllRoles() {
        return entityManager.createQuery("select role from Role r").getResultList();
    }

    public Role getRoleByName(String name) {
        TypedQuery<Role> query = entityManager.createQuery("select role from Role role where role.name " +
                "= :name", Role.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findAny().orElse(null);
    }

}
