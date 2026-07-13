package api.dao.jpa.repositories;

import api.dao.jpa.entities.TransactionsEntity;
import jakarta.persistence.EntityManager;

public class TransactionsRepository extends BaseRepository {

    public TransactionsEntity findByAccountId(Integer Id) {

        try (EntityManager em = getEntityManager()) {
            return em.find(TransactionsEntity.class, Id);
        }
    }
}
