package api.dao.jpa.repositories;

import api.dao.jpa.entities.AccountsEntity;
import jakarta.persistence.EntityManager;

public class AccountsRepository extends BaseRepository {

    public AccountsEntity findByAccountId(Integer Id) {

        try (EntityManager em = getEntityManager()) {
            return em.find(AccountsEntity.class, Id);
        }
    }
}
