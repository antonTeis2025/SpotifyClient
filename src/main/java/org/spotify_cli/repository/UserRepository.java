package org.spotify_cli.repository;


import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.User;
import org.spotify_cli.storage.UserStorage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private final UserStorage us;
    private final List<User> userList;
    private final DatabaseService db;

    public UserRepository(DatabaseService db) throws IOException, SQLException {
        this.us = new UserStorage();
        userList = us.readAll();
        this.db = db;
    }

    public List<User> findAll() {
        return userList;
    }

    public Optional<User> findByName (String name) {
        return findAll().stream()
                .filter(user ->
                        user.getUsername().equals(name)
                ).findFirst();
    }

    public Optional<User> findByMail(String email){
        return findAll().stream()
                .filter(user ->
                        user.getUsername().equals(email)
                ).findFirst();
    }

    public boolean checkUserPassword(User u, String password) {
        return u.getPassword().equals(password);
    }

    public void saveUser(User u) throws SQLException {
        System.out.println(String.format("[+] Insertando usuario %s...", u.getUsername()));
        db.insert("INSERT INTO logins(username, last_login) VALUES (?, ?)",
                u.getUsername(), u.getLast_login());
    }

}
