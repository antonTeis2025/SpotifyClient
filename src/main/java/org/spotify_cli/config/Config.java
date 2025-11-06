package org.spotify_cli.config;

import lombok.Getter;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    // Valores por defecto
    private static String databaseUrl = "jdbc:mysql:spotify_client.db";
    @Getter
    private static boolean databaseDeleteTables = true;
    private static boolean databaseInitTables = true;
    private static boolean databaseInitData = true;
    private static String storageData = "data";
    private static String databaseUser = "root";
    private static String databasePassword = "admin";

    static {
        try {
            Properties properties = new Properties();
            InputStream input = ClassLoader.getSystemResourceAsStream("config.properties");
            if (input != null) {
                properties.load(input);
                databaseUrl = properties.getProperty("database.url", databaseUrl);
                databaseDeleteTables = Boolean.parseBoolean(properties.getProperty("database.delete.tables", String.valueOf(databaseDeleteTables)));
                databaseInitTables = Boolean.parseBoolean(properties.getProperty("database.init.tables", String.valueOf(databaseInitTables)));
                databaseInitData = Boolean.parseBoolean(properties.getProperty("database.init.data", String.valueOf(databaseInitData)));
                storageData = properties.getProperty("storage.data", storageData);
                databaseUser = properties.getProperty("database.user", databaseUser);
                databasePassword = properties.getProperty("database.password", databasePassword);
                System.out.println("[+] Configuración cargada correctamente");
            } else {
                System.err.println("[!] Archivo de configuración 'config.properties' no encontrado");
                System.err.println("[!] Usando valores por defecto");
            }
        } catch (Exception e) {
            System.err.println("[!] Error cargando configuración: " + e.getMessage());
            System.err.println("[!] Usando valores por defecto");
        }
    }

    public static String getDatabaseUrl() {
        return databaseUrl;
    }

    public static boolean getDatabaseInitTables() {
        return databaseInitTables;
    }

    public static boolean getDatabaseInitData() {
        return databaseInitData;
    }

    public static String getStorageData() {
        return storageData;
    }

    public static String getDatabaseUser() {
        return databaseUser;
    }

    public static String getDatabasePassword() {
        return databasePassword;
    }
}