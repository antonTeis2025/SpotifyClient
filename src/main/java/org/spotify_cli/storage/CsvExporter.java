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

    public static void exportAlbums(List<Album> albums, String path) throws IOException {
        System.out.println("[+] Exportando albums al fichero " + path);
        Path archivo = Path.of(path);
        Files.createFile(archivo);
        Files.writeString(archivo, "ID;ID del artista;Nombre;Numero de tracks;Fecha de lanzamiento");

        albums.forEach( album -> {
            String release_date = "No hay datos";
            if (album.getRelease_date()!=null) {
                release_date = album.getRelease_date().toString();
            }
            try {
                Files.writeString(
                        archivo,
                        String.format("\n%s;%s;%s;%d;%s",
                                album.getId(),
                                album.getArtist_id(),
                                album.getName(),
                                album.getNo_tracks(),
                                release_date
                        ),
                        StandardCharsets.UTF_8, StandardOpenOption.APPEND
                );
            } catch (IOException e) {
                System.err.println("[!] Error: " + e);
                System.err.println("[!] No se pudo exportar "+ album.toString());
            }
        });

        System.out.println("[+] Albums exportados correctamente!");
    }

    public static void exportTracks(List<Track> tracks, String path) throws IOException {
        System.out.println("[+] Exportando tracks al fichero " + path);
        Path archivo = Path.of(path);
        Files.createFile(archivo);
        Files.writeString(archivo, "ID;ID del album;ID del artista;Duracion;Titulo");

        tracks.forEach( track -> {
            try {
                Files.writeString(
                        archivo,
                        String.format("\n%s;%s;%s;%d;%s",
                                track.getId(),
                                track.getAlbum_id(),
                                track.getArtist_id(),
                                track.getDuration(),
                                track.getTitulo()
                        ),
                        StandardCharsets.UTF_8, StandardOpenOption.APPEND
                );
            } catch (IOException e) {
                System.err.println("[!] Error: " + e);
                System.err.println("[!] No se pudo exportar "+ track.toString());
            }
        });

        System.out.println("[+] Albums exportados correctamente!");
    }
}
