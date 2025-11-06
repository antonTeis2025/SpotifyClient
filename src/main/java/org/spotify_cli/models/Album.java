package org.spotify_cli.models;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Data
public class Album {

    private String id;
    private String artist_id;
    private String name;
    private LocalDate release_date;
    private List<Track> tracks;

    public Album(String id, String artist_id, String name, String release_date) {
        this.id = id;
        this.artist_id = artist_id;
        this.name = name;
        try {
            this.release_date = LocalDate.parse(release_date);
        } catch (Exception e) {
            this.release_date = null;
        }

    }
}
