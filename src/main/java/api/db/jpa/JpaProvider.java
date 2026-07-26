package api.db.jpa;

import api.config.Config;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JpaProvider {

    private static final EntityManagerFactory EMF;

    static {
        Map<String, Object> properties = new HashMap<>();

        properties.put("jakarta.persistence.jdbc.url",
                Config.getProperty("db_url"));
        properties.put("jakarta.persistence.jdbc.user",
                Config.getProperty("db_username"));
        properties.put("jakarta.persistence.jdbc.password",
                Config.getProperty("db_password"));

        EMF = Persistence.createEntityManagerFactory("test-db", properties);
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

}