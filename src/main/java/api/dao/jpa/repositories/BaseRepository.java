package api.dao.jpa.repositories;

import api.db.jpa.JpaProvider;
import jakarta.persistence.EntityManager;

public abstract class BaseRepository {
    protected EntityManager getEntityManager() {
        return JpaProvider.getEntityManager();
    }
}
