package org.spotify_cli.repository;


import java.io.IOException;
import java.util.List;

// CRUD
public interface Repository<T, ID> {
    /**
     * obtiene el objeto en la API para añadir a la DB
     * @param id id en la API
     * @return Objeto parseado
     */
    T add(ID id) throws IOException, InterruptedException;

    /**
     * obtiene todos los objetos de la DB
     * @return Lista de todos los objetos
     */
    List<T> getAll();

    /**
     * obtiene por ID (entre los ya listados en la DB
     * @param id
     * @return Objeto
     */
    T getById(ID id);

    /**
     * actualiza un registro en la DB
     * @param entity nuevo objeto actualizado
     * @param id id del objeto a actualizar
     * @return objeto actualizado
     */
    T update(T entity, ID id);

    /**
     * Borra un registro de la BD
     * @param id ID del objeto a borrar
     * @return objeto borrado
     */
    T delete(ID id);
}
