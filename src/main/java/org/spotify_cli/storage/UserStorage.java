package org.spotify_cli.storage;


import org.spotify_cli.models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {

    private static final Path archivo = Path.of("users.csv");

    public List<User> readAll() throws IOException {
        System.out.println("[+] Leyendo lista de usuarios...");
        List<User> res = new ArrayList<>();
        if (!Files.exists(archivo)) {
            throw new RuntimeException("[!] No se encontro el archivo de usuarios");
        }

        Files.readAllLines(archivo).stream()
                .forEach( line -> {
                    User u = parse(line);
                    if (u!=null) {
                        res.add(u);
                    }
                });

        return res;
    };


    private User parse(String line) {
        String[] piezas = line.split(",");

        if (piezas.length!=10) {
            return null;
        }

        try {
            return new User(
                    piezas[0], //username
                    piezas[1], // password
                    piezas[2], // email
                    piezas[6], // last login
                    Integer.parseInt(piezas[7]), // loggin_attemps
                    Boolean.parseBoolean(piezas[8]) // account_locked
            );
        } catch (Exception e) {
            return null;
        }
    }
}
