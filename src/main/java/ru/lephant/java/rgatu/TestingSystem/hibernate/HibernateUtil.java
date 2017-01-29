package ru.lephant.java.rgatu.TestingSystem.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {

    private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    private static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    private static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";

    public static final String PROPERTIES_FILE_PATH = "src/main/resources/hibernate.properties";

    private static final SessionFactory sessionFactory = buildSessionFactory();


    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            Properties properties = configuration.getProperties();

            setChangeableProperties(properties);

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void setChangeableProperties(Properties properties) {
        Properties prop = new Properties();
        loadProperties(prop);

        properties.setProperty(HIBERNATE_CONNECTION_URL, prop.getProperty(HIBERNATE_CONNECTION_URL));
        properties.setProperty(HIBERNATE_CONNECTION_USERNAME, prop.getProperty(HIBERNATE_CONNECTION_USERNAME));
        properties.setProperty(HIBERNATE_CONNECTION_PASSWORD, prop.getProperty(HIBERNATE_CONNECTION_PASSWORD));
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static void loadProperties(Properties prop) {
        InputStream input = null;

        try {
            input = new FileInputStream(PROPERTIES_FILE_PATH);

            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
