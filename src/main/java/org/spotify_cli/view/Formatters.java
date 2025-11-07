package org.spotify_cli.view;

import org.spotify_cli.models.Artista;

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
}
