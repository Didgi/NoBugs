package api.dao.jpa.repositories;

import api.dao.jpa.entities.CustomerEntity;
import api.db.jpa.JpaProvider;
import jakarta.persistence.EntityManager;

public class CustomersRepository extends BaseRepository {

    public CustomerEntity findById(Integer id) {

        try (EntityManager em = getEntityManager()) {
            return em.find(CustomerEntity.class, id);
        }
    }
}
