package org.spotify_cli.view;

import org.spotify_cli.models.Album;
import org.spotify_cli.models.Artista;
import org.spotify_cli.models.Track;

public class Formatters {
    public static void artistaFormatter(Artista a) {
        System.out.println(String.format("""
                    
                    _________________
                    [------ %s  (ID: %s)
                    | Oyentes: %d
                    | %s                    
                    |________________
                """,
                a.getName(),
                a.getId(),
                a.getListeners(),
                a.getUrl()));
    }

    public static String msToDuration(long ms) {
        if (ms < 0) {
            ms = 0;
        }

        long segundosTotales = ms / 1000;

        long minutos = segundosTotales / 60;
        long segundos = segundosTotales % 60;
        return "" + minutos + ":" + segundos;

    }

    public static void trackFormatter(Track t) {
        System.out.println(String.format("""
                
                %s [ %s ]                
                
                """, t.getTitulo(), Formatters.msToDuration(t.getDuration())
        ));
    }

    public static void artistaShortFormatter(Artista a) {
        System.out.println(String.format("%s -> %s", a.getName(), a.getId()));
    }

    public static void albumShortFormatter(Album a) {
        System.out.println(String.format("%s -> %s", a.getName(), a.getId()));
    }
    public static void trackShortFormatter(Track t) {
        System.out.println(String.format("%s -> %s", t.getTitulo(), t.getId()));
    }

}
