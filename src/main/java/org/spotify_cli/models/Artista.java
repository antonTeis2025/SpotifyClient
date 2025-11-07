package org.spotify_cli.models;

import lombok.Data;

@Data
public class Artista {

    private String id;
    private String name;
    private int listeners;
    private String url;



    public Artista(String id, String name, int listeners, String url) {
        if (!id.equals("")) {
            this.id = id;
        } else {
            this.id = null;
        }

        this.name = name;
        this.listeners = listeners;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Artista{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", listeners=" + listeners +
                ", url='" + url + '\'' +
                '}';
    }
}
