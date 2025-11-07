package org.spotify_cli.repository;

import lombok.Getter;
import org.spotify_cli.api.ApiClient;
import org.spotify_cli.database.DatabaseService;

/**
 * Esta clase almacena las instancias de objetos ApiService y DatabaseService
 * Esto para que los demas repositorios puedan acceder a estas clases sin
 * tener que instanciar propiamente en memoria ambos objetos
 */
public class RepositoryResources {
    @Getter
    private final ApiClient api;
    @Getter
    private final DatabaseService db;

    public RepositoryResources(ApiClient api, DatabaseService db) {
        this.api = api;
        this.db = db;
    }
}
