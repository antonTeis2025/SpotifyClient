package org.spotify_cli.models;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Album {
    private String id;
    private String name;
    private LocalDate release_date;
    private List<Track> tracks;

    public Album(String id, String name, LocalDate release_date, List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.release_date = release_date;
        this.tracks = tracks;
    }
}
