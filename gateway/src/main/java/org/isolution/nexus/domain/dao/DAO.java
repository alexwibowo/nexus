package org.isolution.nexus.domain.dao;

import org.isolution.nexus.domain.AbstractModel;

import java.util.List;

/**
 * User: Alex Wibowo
 * Date: 30/12/10
 * Time: 9:32 PM
 */
public interface DAO<M extends AbstractModel> {

    /**
     * @param value persistent (or transient) object to be saved / updated
     * @return saved object
     */
    M save(M value);

    /**
     * @param id id of the persistent object to be loaded
     * @return persisted object matching the given id
     */
    @SuppressWarnings("unchecked")
    M get(Long id);

    /**
     * @param value persistent object to be deleted
     */
    void delete(M value);

    /**
     * Delete all instances of {@link M}
     */
    void deleteAll();

    /**
     * @return all instances of {@link M}
     */
    List<M> loadAll();
}
