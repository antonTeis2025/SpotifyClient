package org.spotify_cli.database;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import org.spotify_cli.config.Config;

public class DatabaseService{


    private Connection connection = null;

    public DatabaseService() {
        initConexion();
        if (Config.isDatabaseDeleteTables()) {
            dropsTablas();
        }
        if (Config.getDatabaseInitTables()) {
            initTablas();
        }
        if (Config.getDatabaseInitData()) {
            initData();
        }
    }

    private void initConexion() {
        System.out.println("[!] Iniciando conexión con la base de datos");
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(Config.getDatabaseUrl(), Config.getDatabaseUser(), Config.getDatabasePassword());
            }
            System.out.println("[!] Conexión con la base de datos inicializada");
        } catch (SQLException e) {
            System.err.println("[!] Error al iniciar la conexión: " + e.getMessage());
        }
    }

    public void dropsTablas() {
        try {
            URL resourceUrl = ClassLoader.getSystemResource("sql/01-drops.sql");
            Path path = Paths.get(resourceUrl.toURI());
            String sqlContent = Files.readString(path);
            List<String> statements = Arrays.asList(sqlContent.split(";\\s*"));

            try (Statement stmt = connection.createStatement()) {
                for (String statement : statements) {
                    if (statement.trim().isEmpty()) continue;

                    try {
                        stmt.execute(statement.trim());
                        System.out.println("[+] Sentencia ejecutada: " +
                                statement.trim().substring(0, Math.min(50, statement.trim().length())) + "...");
                    } catch (Exception e) {
                        System.err.println("[!] Error en sentencia: " +
                                statement.trim().substring(0, Math.min(50, statement.trim().length())) + "...");
                        throw e;
                    }
                }
                System.out.println("[+] Todas las tablas borradas");
            }
        } catch (Exception e) {
            System.err.println("[!] Error al borrar las tablas: " + e.getMessage());
            throw new RuntimeException("Error inicializando base de datos", e);
        }
    }

    public void initTablas() {
        try {
            URL resourceUrl = ClassLoader.getSystemResource("sql/02-tables.sql");
            Path path = Paths.get(resourceUrl.toURI());
            String sqlContent = Files.readString(path);
            List<String> statements = Arrays.asList(sqlContent.split(";\\s*"));

            try (Statement stmt = connection.createStatement()) {
                for (String statement : statements) {
                    if (statement.trim().isEmpty()) continue;

                    try {
                        stmt.execute(statement.trim());
                        System.out.println("[+] Sentencia ejecutada: " +
                                statement.trim().substring(0, Math.min(50, statement.trim().length())) + "...");
                    } catch (Exception e) {
                        System.err.println("[!] Error en sentencia: " +
                                statement.trim().substring(0, Math.min(50, statement.trim().length())) + "...");
                        throw e;
                    }
                }
                System.out.println("[+] Todas las tablas creadas correctamente");
            }
        } catch (Exception e) {
            System.err.println("[!] Error al crear las tablas: " + e.getMessage());
            throw new RuntimeException("Error inicializando base de datos", e);
        }
    }

    private void initData() {
        // TODO
    }


    public List<Map<String, Object>> select(String sql, Object... params) {
        System.out.println("[!] Ejecutando consulta: " + sql);
        List<Map<String, Object>> result = new ArrayList<>();
        initConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData meta = resultSet.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnName(i), resultSet.getObject(i));
                }
                result.add(row);
            }
        } catch (SQLException e) {
            System.err.println("[!] Error en la consulta: " + e.getMessage());
        }

        System.out.println("[!] Consulta finalizada con " + result.size() + " resultados");
        return result;
    }

    public int insert(String sql, Object... params) {
        System.out.println("[!] Ejecutando inserción: " + sql);
        int result = 0;
        initConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[!] Error en la inserción: " + e.getMessage());
        }

        System.out.println("[!] Inserción finalizada con " + result + " filas afectadas");
        return result;
    }

    public Object insertAndGetId(String sql, Object... params) {
        System.out.println("[!] Ejecutando inserción y obteniendo ID: " + sql);
        Object result = 0;
        initConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(statement, params);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                result = generatedKeys.getObject(1);
            }
        } catch (SQLException e) {
            System.err.println("[!] Error al insertar y obtener ID: " + e.getMessage());
        }

        System.out.println("[!] Inserción finalizada con ID: " + result);
        return result;
    }


    public int update(String query, Object... params) {
        System.out.println("[!] Ejecutando actualización: " + query);
        int result = 0;
        initConexion();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[!] Error al actualizar: " + e.getMessage());
        }
        System.out.println("[!] Actualización finalizada con " + result + " filas afectadas");
        return result;
    }

    public int delete(String query, Object... params) {
        System.out.println("[!] Ejecutando borrado: " + query);
        int result = 0;
        initConexion();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[!] Error al borrar: " + e.getMessage());
        }
        System.out.println("[!] Borrado finalizado con " + result + " filas afectadas");
        return result;
    }

    private void setParameters(PreparedStatement statement, Object[] params) throws SQLException {
        System.out.println("[!] Pasando parámetros a la consulta: " + Arrays.toString(params));
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}