package org.spotify_cli.models;

public class Artista {
    private String id;
    private String name;
    private int listeners;
    private String url;

    public Artista(String id, String name, int listeners, String url) {
        this.id = id;
        this.name = name;
        this.listeners = listeners;
        this.url = url;
    }
}
