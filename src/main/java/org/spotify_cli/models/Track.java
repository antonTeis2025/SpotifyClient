package org.spotify_cli.models;

import lombok.Data;

@Data
public class Track {
    private String id;
    private String album_id;
    private String artist_id;
    private int duration;
    private String titulo;

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", album_id='" + album_id + '\'' +
                ", artist_id='" + artist_id + '\'' +
                ", duration=" + duration +
                ", titulo='" + titulo + '\'' +
                '}';
    }

    public Track(String id, String album_id, String artist_id, int duration, String titulo) {
        this.id = id;
        this.album_id = album_id;
        this.artist_id = artist_id;
        this.duration = duration;
        this.titulo = titulo;
    }
}
