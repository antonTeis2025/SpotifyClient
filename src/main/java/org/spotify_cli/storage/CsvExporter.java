package org.spotify_cli.storage;

import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.models.Track;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Exporta todos los tipos de dato a CSV
 */
public class CsvExporter {


    public static void exportArtists(List<Artista> artists, String path) throws IOException {
        System.out.println("[+] Exportando artistas al fichero " + path);
        Path archivo = Path.of(path);
        Files.createFile(archivo);
        Files.writeString(archivo, "ID;Nombre;Oyentes Mensuales;URL de Spotify");

        artists.forEach( artista -> {
            try {
                Files.writeString(
                                        archivo,
                                        String.format("\n%s;%s;%d;%s",
                                                artista.getId(),
                                                artista.getName(),
                                                artista.getListeners(),
                                                artista.getUrl()
                                                ),
                        StandardCharsets.UTF_8, StandardOpenOption.APPEND
                                );
            } catch (IOException e) {
                System.err.println("[!] Error: " + e);
                System.err.println("[!] No se pudo exportar "+ artista.toString());
            }
        });

        System.out.println("[+] Artistas exportados correctamente!");
    }

    public static void exportAlbums(List<Album> albums, String path) {

    }

    public static void exportTracks(List<Track> tracks, String path) {

    }
}
