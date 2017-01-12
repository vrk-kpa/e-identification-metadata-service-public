/**
 * The MIT License
 * Copyright (c) 2015 Population Register Centre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vm.kapa.identification.metadata.dao;

import fi.vm.kapa.identification.metadata.model.Metadata;
import fi.vm.kapa.identification.type.ProviderType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class MetadataDAO {

    @PersistenceContext(unitName = "metadataDAO")
    private EntityManager entityManager;

    public void insertMetadata(Metadata metadata) {
        entityManager.persist(metadata);
    }

    public void updateMetadata(Metadata metadata) {
        entityManager.merge(metadata);
    }

    public List<Metadata> getAllMetadata() {
        TypedQuery<Metadata> query = entityManager
                .createNamedQuery("Metadata.getAll", Metadata.class);

        return query.getResultList();
    }

    public List<Metadata> getMetadataByType(ProviderType providerType) {
        TypedQuery<Metadata> query = entityManager
                .createNamedQuery("Metadata.searchByProviderType", Metadata.class)
                .setParameter("providerType", providerType.toString());

        return query.getResultList();
    }

    public List<Metadata> getMetadataByEntityId(String entityId) {
        TypedQuery<Metadata> query = entityManager
                .createNamedQuery("Metadata.searchByEntityId", Metadata.class)
                .setParameter("entityId", entityId);

        return query.getResultList();
    }
}
