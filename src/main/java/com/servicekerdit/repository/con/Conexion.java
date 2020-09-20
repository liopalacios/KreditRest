package com.servicekerdit.repository.con;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class Conexion {
    private static final Log LOG = LogFactory.getLog(Conexion.class);
    private static String user;
    private static String pass;
    private static String url;
    private static String driver;

    static {
        init();
    }

    private static void init() {
        Properties properties = new Properties();
        try {
            Properties tempProperties = new Properties();
            try (InputStream resource = Conexion.class.getResourceAsStream("/jdbc.properties")) {
                tempProperties.load(resource);
                properties.putAll(tempProperties);
            }
            String propertiesFile = System.getProperty("jdbc.properties");
            if (propertiesFile != null) {
                File app = new File(propertiesFile);
                if (app.isFile()) {
                    try (InputStream fileInputStream = new FileInputStream(app)) {
                        tempProperties.load(fileInputStream);
                        properties.putAll(tempProperties);
                    }
                    LOG.info(String.format("External file %s loaded successfully", app));
                } else {
                    LOG.warn(String.format("External file %s not found", app));
                }
            }
            LOG.debug(String.format("properties loaded: %s", properties));
        } catch (IOException ex) {
            LOG.warn("Can not load app.properties", ex);
        }
        driver = properties.getProperty("driverClassName");
        url = properties.getProperty("jdbcUrl");
        user = properties.getProperty("username");
        pass = properties.getProperty("password");
    }

    public static void cerrar(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            LOG.error("no se puede cerrar conexion", e);
        }
    }

    /**
     * Cerramos el statement
     */
    public static void cerrar(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (Exception e) {
            LOG.error("no se puede cerrar statement", e);
        }
    }

    public static Connection conectarPosgressGPS() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, pass);
    }
}
