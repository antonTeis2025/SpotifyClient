package org.spotify_cli.view;

import org.spotify_cli.models.Artista;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private static Scanner sc = new Scanner(System.in);
    // funcion super palera que "limpia" la pantalla.
    // al menos hace que deje de estar llena de basura y se lea mejor
    public static void cls() {
        for (int i = 0; i<60; i++) {
            System.out.println("\n");
        }
    };

    public static void muestraBanner() {
        System.out.println("""
                                          __  .__  _____          \s
                     ____________   _____/  |_|__|/ ____\\__.__.   \s
                    /  ___/\\____ \\ /  _ \\   __\\  \\   __<   |  |   \s
                    \\___ \\ |  |_> >  <_> )  | |  ||  |  \\___  |   \s
                   /____  >|   __/ \\____/|__| |__||__|  / ____|   \s
                        \\/ |__|                         \\/        \s
                       .__  .__            __                .__  \s
                  ____ |  | |__|         _/  |_  ____   ____ |  | \s
                _/ ___\\|  | |  |  ______ \\   __\\/  _ \\ /  _ \\|  | \s
                \\  \\___|  |_|  | /_____/  |  | (  <_> |  <_> )  |__
                 \\___  >____/__|          |__|  \\____/ \\____/|____/
                     \\/                                           \s
                
                """);
    }

    public static int muestraOpcionesInicio() {

        Menu.muestraBanner();
        int opcion = 0;
        System.out.println("------------------------------------");
        System.out.println(">   Qué deseas hacer? ");
        System.out.println("1. Consultar artistas disponibles localmente");
        System.out.println("2. Añadir artista a la BBDD local");
        System.out.println("3. Eliminar...");
        System.out.println("4. Actualizar...");
        System.out.println("5. Buscar...");
        System.out.println("6. Obtener...");
        System.out.println("0. Salir...");
        System.out.print("\n> ");
        opcion = Integer.parseInt(sc.nextLine());
        return opcion;
    }

    public static void muestraArtistasLocalmente(List<Artista> lista) {
        Menu.cls();
        System.out.println("[+] Los artistas disponibles localmente son: ");
        lista.forEach(Formatters::artistaFormatter);
    }

}
