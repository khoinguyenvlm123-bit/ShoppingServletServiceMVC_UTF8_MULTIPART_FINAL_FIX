package vn.iotstar.connection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public final class JPAConfig {
    private static final EntityManagerFactory FACTORY = createFactory();

    private JPAConfig() {
    }

    private static EntityManagerFactory createFactory() {
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", System.getProperty(
                "shopping.db.url",
                "jdbc:sqlserver://localhost:1433;databaseName=ServletCRUDMVC;encrypt=false;trustServerCertificate=true;sendStringParametersAsUnicode=true"
        ));
        properties.put("jakarta.persistence.jdbc.user", System.getProperty("shopping.db.user", "sa"));
        properties.put("jakarta.persistence.jdbc.password", System.getProperty("shopping.db.password", "123"));
        return Persistence.createEntityManagerFactory("ShoppingServletServiceMVCPU", properties);
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}
