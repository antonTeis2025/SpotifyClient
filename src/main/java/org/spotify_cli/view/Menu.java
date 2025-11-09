package org.spotify_cli.view;

import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.models.Track;
import org.spotify_cli.repository.AlbumRepository;
import org.spotify_cli.repository.ArtistRepository;
import org.spotify_cli.repository.TrackRepository;
import org.spotify_cli.storage.CsvExporter;

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
                case 3 ->  Menu.muestraFuchicar(arr, alr, tr);
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
                    switch (Menu.muestraMenuActualizar()) {
                        case 0 -> start(arr, alr, tr);
                        case 1 -> {
                            Artista a = Menu.muestraActualizarArtista(arr);
                            arr.update(a, a.getId());
                        }
                        case 2 -> {
                            Album a = Menu.muestraActualizarAlbum(alr);
                            alr.update(a, a.getId());
                        }
                        case 3 -> {
                            Track t = Menu.muestraActualizarTrack(tr);
                            tr.update(t, t.getId());
                        }
                    }
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
        System.out.println("3. Fuchicar (mira / exporta los datos)...");
        System.out.println("4. Eliminar...");
        System.out.println("5. Actualizar...");
        System.out.println("6. Busquedas / ordenaciones...");
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

    /**
     * Devuelve el ID del artista de una lista
     * @param artistaList
     * @return
     */
    private static String muestraSeleccionarArtista(List<Artista> artistaList) {
        Menu.cls();
        System.out.println("[?] Qué artista de la lista deseas seleccionar?");
        artistaList.forEach(Formatters::artistaShortFormatter);
        System.out.print("\nID > ");
        return sc.nextLine();
    }

    /**
     * Devuelve el ID del album de una lista
     * @param albumList
     * @return
     */
    private static String muestraSeleccionarAlbum(List<Album> albumList) {
        Menu.cls();
        System.out.println("[?] Qué album de la lista deseas seleccionar?");
        albumList.forEach(Formatters::albumShortFormatter);
        System.out.print("\nID >");
        return sc.nextLine();
    }

    /**
     * Devuelve el ID del track de una lista
     * @param trackList
     * @return
     */
    private static String muestraSeleccionarTrack(List<Track> trackList) {
        Menu.cls();
        System.out.println("[?] Qué track de la lista deseas seleccionar?");
        trackList.forEach(Formatters::trackShortFormatter);
        System.out.print("\nID >");
        return sc.nextLine();
    }

    private static Artista seleccionarArtista(ArtistRepository artistRepository) {
        String id = Menu.muestraSeleccionarArtista(artistRepository.getAll());
        return artistRepository.getById(id);
    }
    private static Album seleccionarAlbum (AlbumRepository albumRepository) {
        String id = Menu.muestraSeleccionarAlbum(albumRepository.getAll());
        return albumRepository.getById(id);
    }
    private static Track seleccionarTrack (TrackRepository trackRepository) {
        String id = Menu.muestraSeleccionarTrack(trackRepository.getAll());
        return trackRepository.getById(id);
    }

    private static String enterOrOld(String nuevo, String old) {
        if (nuevo.equals("")) {
            return old;
        } else {
            return nuevo;
        }
    }

    private static Artista muestraActualizarArtista(ArtistRepository artistRepository) {
        Artista oldArtista = Menu.seleccionarArtista(artistRepository);

        Menu.cls();
        System.out.print(String.format("[?] Nuevo nombre (Enter para mantener %s): ", oldArtista.getName()));
        String nuevoNombre = Menu.enterOrOld(sc.nextLine(), oldArtista.getName());

        System.out.print(String.format("[?] Nuevos oyentes (Enter para mantener %d): ", oldArtista.getListeners()));
        String nuevosOyentes = Menu.enterOrOld(sc.nextLine(), Integer.toString(oldArtista.getListeners()));

        System.out.print(String.format("[?] Nuevo URL (Enter para mantener %s): ", oldArtista.getUrl()));
        String nuevoURL = Menu.enterOrOld(sc.nextLine(), oldArtista.getUrl());

        return new Artista(
                oldArtista.getId(),
                nuevoNombre,
                Integer.parseInt(nuevosOyentes),
                nuevoURL
        );
    }

    private static Album muestraActualizarAlbum(AlbumRepository albumRepository) {
        Album oldAlbum = Menu.seleccionarAlbum(albumRepository);
        Menu.cls();

        System.out.print(String.format("[?] Nuevo nombre (Enter para mantener %s): ", oldAlbum.getName()));
        String nuevoNombre = Menu.enterOrOld(sc.nextLine(), oldAlbum.getName());


        System.out.print(String.format("[?] Nueva fecha de lanzamiento (YYYY-MM-DD) (Enter para mantener %s): ", oldAlbum.getRelease_date().toString()));
        String nuevaFecha = Menu.enterOrOld(sc.nextLine(), oldAlbum.getRelease_date().toString());

        return new Album(
                oldAlbum.getId(),
                oldAlbum.getArtist_id(),
                nuevoNombre,
                nuevaFecha,
                oldAlbum.getNo_tracks()
        );
    }


    private static Track muestraActualizarTrack(TrackRepository tr) {
        Track oldTrack = Menu.seleccionarTrack(tr);
        Menu.cls();

        System.out.print(String.format("[?] Nuevo titulo (Enter para mantener %s): ", oldTrack.getTitulo()));
        String nuevoNombre = Menu.enterOrOld(sc.nextLine(), oldTrack.getTitulo());


        System.out.print(String.format("[?] Nueva duracion (en ms) (Enter para mantener %d): ", oldTrack.getDuration()));
        String nuevaDuracion = Menu.enterOrOld(sc.nextLine(), Integer.toString(oldTrack.getDuration()));

        return new Track(
                oldTrack.getId(),
                oldTrack.getAlbum_id(),
                oldTrack.getArtist_id(),
                Integer.parseInt(nuevaDuracion),
                nuevoNombre
        );
    }

    private static List<Album> albumsDeArtista(Artista a, AlbumRepository albumRepository) {
        return albumRepository.getByArtist(a);
    }

    private static List<Track> tracksDeAlbum(List<Album> albums, AlbumRepository albumRepository, TrackRepository trackRepository) {
        Album a = albumRepository.getById(Menu.muestraSeleccionarAlbum(albums));
        return trackRepository.getByAlbum(a);
    }

    private static int mostrarOexportar(String tipo_dato) {
        Menu.cls();
        System.out.printf("""
                [?] Qué deseas hacer?
                1. Solamente mostrar %s
                2. Exportar %s a CSV y mostrar 
                %n""", tipo_dato, tipo_dato);
        System.out.println();
        System.out.print("> ");
        int opcion = Integer.parseInt(sc.nextLine());
        while (opcion != 1 && opcion != 2) {
            System.out.println("[!] Opcion no valida");
            opcion = Integer.parseInt(sc.nextLine());
        }
        return opcion;
    }

    private static String pideRutaExportacion() {

        System.out.print("[?] Introduce la ruta de guardado (debe ser un fichero CSV) > ");
        return sc.nextLine();
    }

    private static void muestraFuchicar(ArtistRepository artistRepository, AlbumRepository albumRepository, TrackRepository trackRepository) throws IOException {
        List<Artista> artists = artistRepository.getAll();
        Menu.cls();
        int opcionArtistas = Menu.mostrarOexportar("artistas");
        if (opcionArtistas == 2) {
            try {

                CsvExporter.exportArtists(artists, Menu.pideRutaExportacion());
            } catch (IOException e) {
                System.err.println("[!] Error al exportar: " + e);
            }
            Menu.pause();
            Menu.cls();
        }
        Artista a = Menu.seleccionarArtista(artistRepository);
        List<Album> albums = Menu.albumsDeArtista(a, albumRepository);



        int opcionAlbums = Menu.mostrarOexportar("albums");
        if (opcionAlbums == 2) {
            try {
                CsvExporter.exportAlbums(albums, Menu.pideRutaExportacion());
            } catch (Exception e) {
                System.err.println("[!] Error al exportar: " + e);
            }
            Menu.pause();
            Menu.cls();
        }

        List<Track> tracks = Menu.tracksDeAlbum(albums, albumRepository, trackRepository);
        albums.forEach(album -> {
            Formatters.albumShortFormatter(album);
        });
        int opcionTracks = Menu.mostrarOexportar("tracks");

        System.out.println("[+] Tracks del album");
        if (opcionTracks==2) {
            try {
                CsvExporter.exportTracks(tracks, Menu.pideRutaExportacion());
            } catch (Exception e) {
                System.err.println("[!] Error al exportar: " + e);
            }
            Menu.pause();
            Menu.cls();

        }

        tracks.forEach(Formatters::trackFormatter);

        System.out.println("\n");
        Menu.pause();

    };

    public static void muestraMenuBusquedas(ArtistRepository artistRepository, AlbumRepository albumRepository, TrackRepository trackRepository) throws IOException, InterruptedException {
        Menu.cls();
        System.out.println("""
                [?] Sobre qué tipo de dato quieres hacer búsquedas? 
                1. Artistas
                2. Albumes
                3. Tracks
                0. Atras
                """);
        System.out.print("> ");
        String opcion = sc.nextLine();
        while (opcion.isEmpty()
                && Integer.parseInt(opcion) == 1
                && Integer.parseInt(opcion) == 2
                && Integer.parseInt(opcion) == 3
        ) {
            System.err.print("\n[!] Opcion no valida\n> ");
            opcion = sc.nextLine();
        }

        switch (Integer.parseInt(opcion)) {
            case 1 -> Menu.muestraBusquedasArtista(artistRepository);
            case 2 -> Menu.muestraBusquedasAlbum(albumRepository);
            case 3 -> Menu.muestraBusquedasTrack(trackRepository);
            case 0 -> Menu.start(artistRepository, albumRepository, trackRepository);
        }

    }


    private static void muestraBusquedasArtista(ArtistRepository artistRepository) {
        Menu.cls();
        System.out.println("""
                [?] Qué quieres hacer? 
                1. Obtener artistas ordenados por oyentes mensuales
                2. Buscar artista por Nombre
                0. Atrás
                """);

    }

    private static void muestraBusquedasAlbum(AlbumRepository albumRepository) {
        Menu.cls();
        System.out.println("""
                [?] Qué quieres hacer? 
                1. Obtener albumes ordenados por fecha de lanzamiento
                2. Buscar album por nombre
                3. Obtener singles (albumes con un track)
                0. Atrás
                """);
    }


    private static void muestraBusquedasTrack(TrackRepository trackRepository) {
        Menu.cls();
        System.out.println("""
                [?] Qué quieres hacer? 
                1. Obtener tracks ordenados por duracion
                2. Buscar track por nombre
                0. Atrás
                """);
    }
}
