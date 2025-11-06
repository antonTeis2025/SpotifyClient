USE spotify_client;

CREATE TABLE IF NOT EXISTS Artista (
    id VARCHAR(40) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    listeners INT DEFAULT 0,
    url VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS Album (
    id VARCHAR(40) PRIMARY KEY,
    artist_id VARCHAR(40) NOT NULL,
    name VARCHAR(200) NOT NULL,
    no_tracks INT NOT NULL,
    release_date DATE,
    FOREIGN KEY (artist_id) REFERENCES Artista(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Track (
    id VARCHAR(40) PRIMARY KEY,
    album_id VARCHAR(40) NOT NULL,
    artist_id VARCHAR(40) NOT NULL,
    duration INT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    FOREIGN KEY (album_id) REFERENCES Album(id) ON DELETE CASCADE,
    FOREIGN KEY (artist_id) REFERENCES Artista(id) ON DELETE CASCADE
);