USE spotify_client;

CREATE TABLE if not exists logins(
    username varchar(20),
    last_login varchar(40)
);