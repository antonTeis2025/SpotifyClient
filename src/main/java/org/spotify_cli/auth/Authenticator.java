package org.spotify_cli.auth;

import org.spotify_cli.database.DatabaseService;
import org.spotify_cli.models.User;
import org.spotify_cli.repository.UserRepository;
import org.spotify_cli.storage.UserStorage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class Authenticator {

    private final DatabaseService db;
    private final Scanner sc = new Scanner(System.in);
    private final UserRepository userRepository;

    public Authenticator(DatabaseService db) throws SQLException, IOException {
        this.db = db;
        this.userRepository = new UserRepository(db);
    }

    /**
     * Esta funcion se encarga de toda la logica tanto del menu como gestion de datos
     * para poder autenticar a los usuarios.
     * Es un void porque si el usuario no es valido, cierra el programa.
     * @throws SQLException
     */
    public void auth() throws SQLException {
        System.out.println("---- Login ------");
        System.out.print("Usuario o email > ");
        String usernameInput = sc.nextLine();
        // ut -> User Temporal (necesario que sea optional)
        Optional<User> ut;
        // u -> User final
        User u = null;

        try {
            if (usernameInput.contains("@")) { // si contiene @, busca por mail
                ut = userRepository.findByMail(usernameInput);
            } else {
                ut = userRepository.findByName(usernameInput);
            }
            // si ut es null es que no encontro ni por email ni por usuario
            if (ut==null) {
                System.out.println("[!] Usuario no valido");
                System.out.println("Saliendo...");
                System.exit(1);
            }

            // hacemos que u no sea optional
            u = ut.get();
        } catch (RuntimeException e) {
            // si hay un excepcion probablemente sea que no lo encontro
            System.out.println("[!] Usuario no valido");
            System.out.println("Saliendo...");
            System.exit(1);
        }

        // Comprobar si el usuario estaba bloqueado
        if (u.isAccount_locked()) {
            System.out.println("[!] Usuario bloqueado");
            System.out.println("Saliendo...");
            System.exit(1);
        }
        // loggin_at -> loggin attemps
        int loggin_at = 0;
        boolean logged = false;
        while (loggin_at < 3) {
            String passwordInput = "";
            System.out.println(String.format("[+] Intento %d", loggin_at+1));
            System.out.print("Contraseña > ");
            passwordInput = sc.nextLine();
            if (userRepository.checkUserPassword(u, passwordInput)) {
                System.out.println("[+] Loggin correcto!");
                logged = true;
                break;
            }
            System.out.println("[!] Intento fallido");
            loggin_at++;
        }
        // si supero los intentos
        if (!logged) {
            System.out.println("[!] Numero maximo de intentos alcanzado");
            System.exit(1);
        }

        System.out.println("[+] Introduciendo usuario en BBDD local");
        userRepository.saveUser(u);
    }
}
