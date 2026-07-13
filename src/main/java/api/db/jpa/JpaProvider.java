package api.db.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaProvider {

    private static EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("test-db");

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

}