package ru.lephant.java.rgatu.TestingSystem.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.*;
import java.util.Properties;

public class HibernateUtil {

    private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    private static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    private static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";

    private static final String PROPERTIES_FILE_PATH = "hibernate.properties";
    private static final String HIBERNATE_CFG_XML_PATH = "hibernate.cfg.xml";

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory getSessionFactory(Properties properties) {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = buildSessionFactory(properties);
        }
        return sessionFactory;
    }

    public static boolean checkConnection() {
        try {
            sessionFactory = getSessionFactory();
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    public static boolean checkConnection(Properties properties) {
        try {
            sessionFactory = getSessionFactory(properties);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure(HIBERNATE_CFG_XML_PATH);
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

    private static SessionFactory buildSessionFactory(Properties prop) {
        try {
            Configuration configuration = new Configuration().configure(HIBERNATE_CFG_XML_PATH);
            Properties properties = configuration.getProperties();

            properties.putAll(prop);

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            saveNewProperties(prop);
            return sessionFactory;
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

    private static void loadProperties(Properties prop) {
        InputStream input = null;
        try {
            File file = new File(PROPERTIES_FILE_PATH);
            input = new FileInputStream(file);
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

    private static void saveNewProperties(Properties prop) {
        OutputStream output = null;
        try {
            File file = new File(PROPERTIES_FILE_PATH);
            output = new FileOutputStream(file);
            prop.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}