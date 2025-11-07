package org.spotify_cli.view;

import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.models.Track;
import org.spotify_cli.repository.AlbumRepository;
import org.spotify_cli.repository.ArtistRepository;
import org.spotify_cli.repository.TrackRepository;

import java.io.IOException;
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

    public static void pause() {
        System.out.println("\n\n Enter para continuar...");
        sc.nextLine();
    }

    public static void start(ArtistRepository arr, AlbumRepository alr, TrackRepository tr) throws IOException, InterruptedException {

        int opcion = Menu.muestraOpcionesInicio();

        while (opcion != 0) {
            switch (opcion) {
                case 0 -> System.exit(0);
                case 1 ->  Menu.muestraArtistasLocalmente(arr.getAll());
                case 2 ->  arr.add(Menu.muestraMenuAñadirArtista());
                case 4 -> {
                    int opt = Menu.muestraMenuEliminar();
                    switch (opt) {
                        case 0 -> start(arr, alr, tr);
                        case 1 -> arr.delete(Menu.muestraSeleccionarArtista(arr.getAll()));
                        case 2 -> alr.delete(Menu.muestraSeleccionarAlbum(alr.getAll()));
                        case 3 -> tr.delete(Menu.muestraSeleccionarTrack(tr.getAll()));
                    }
                }
                case 5 -> {

                }
                default -> System.err.println("[!] Opcion Invalida");
            }
            opcion = Menu.muestraOpcionesInicio();
        }

    }

    private static void muestraBanner() {
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

    private static int muestraOpcionesInicio() {
        Menu.cls();
        Menu.muestraBanner();
        int opcion = 0;
        System.out.println("------------------------------------");
        System.out.println(">   Qué deseas hacer? ");
        System.out.println("1. Consultar artistas disponibles localmente");
        System.out.println("2. Añadir artista a la BBDD local");
        System.out.println("3. Fuchicar...");
        System.out.println("4. Eliminar...");
        System.out.println("5. Actualizar...");
        System.out.println("6. Buscar...");
        System.out.println("0. Salir...");
        System.out.print("\n> ");
        opcion = Integer.parseInt(sc.nextLine());
        return opcion;
    }
    // OPCION 1
    private static void muestraArtistasLocalmente(List<Artista> lista) {
        Menu.cls();
        System.out.println("[+] Los artistas disponibles localmente son: ");
        lista.forEach(Formatters::artistaFormatter);
        Menu.pause();
    }
    // OPCION 2
    private static String muestraMenuAñadirArtista() {
        Menu.cls();
        System.out.println(" https://spotify.com/artist/xxxyyyzzzz/  ->  el ID es xxxyyyzzzz" );
        System.out.println("[?] Para añadir un artista, introduce su ID");
        System.out.println("Esto añadira también sus 10 ultimos albums con sus respectivos tracks");
        System.out.print("> ");
        String artistId = sc.nextLine();
        return artistId;
    }
    // OPCION 4
    private static int muestraMenuEliminar() {
        Menu.cls();
        System.out.println("""
                [?] Qué tipo de dato deseas eliminar?
                
                1. Artista (Borrara todos sus albumes y tracks en cascada)
                2. Album (Borrara todos sus tracks en cascada)
                3. Track
                0. Atrás
                """
        );
        int opcion = Integer.parseInt(sc.nextLine());
        while (opcion!=1 && opcion!=2 && opcion!=3 && opcion != 0) {
            System.out.println("[!] Opcion no valida");
            opcion = Integer.parseInt(sc.nextLine());
        }
        return opcion;
    }
    // OPCION 5 // TODO
    private static int muestraMenuActualizar() {
        Menu.cls();
        System.out.println("""
                [?] Qué tipo de dato deseas actualizar?
                
                1. Artista 
                2. Album
                3. Track
                0. Atrás
                """
        );
        int opcion = Integer.parseInt(sc.nextLine());
        while (opcion!=1 && opcion!=2 && opcion!=3 && opcion != 0) {
            System.out.println("[!] Opcion no valida");
            opcion = Integer.parseInt(sc.nextLine());
        }
        return opcion;
    }

    private static String muestraSeleccionarArtista(List<Artista> artistaList) {
        Menu.cls();
        System.out.println("[?] Qué artista de la lista deseas eliminar?");
        artistaList.forEach(Formatters::artistaShortFormatter);
        System.out.print("\nID > ");
        return sc.nextLine();
    }

    private static String muestraSeleccionarAlbum(List<Album> albumList) {
        Menu.cls();
        albumList.forEach(Formatters::albumShortFormatter);
        System.out.print("\nID >");
        return sc.nextLine();
    }

    private static String muestraSeleccionarTrack(List<Track> trackList) {
        Menu.cls();
        trackList.forEach(Formatters::trackShortFormatter);
        System.out.print("\nID >");
        return sc.nextLine();
    }



}
