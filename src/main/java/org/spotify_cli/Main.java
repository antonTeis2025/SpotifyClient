package org.spotify_cli;

import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // DatabaseService db = new DatabaseService();
        ApiClient api = new ApiClient();
        System.out.println(api.getTokenAPI());
    }
}
