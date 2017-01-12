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
package fi.vm.kapa.identification.metadata.service;

import fi.vm.kapa.identification.metadata.dao.MetadataDAO;
import fi.vm.kapa.identification.metadata.model.Metadata;
import fi.vm.kapa.identification.type.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MetadataService {

    private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);

    @Autowired
    private MetadataDAO metadataDAO;

    public void addMetadata(Metadata metadata) {
        metadataDAO.insertMetadata(metadata);
    }

    public void updateMetadata(Metadata metadata) {
        metadataDAO.updateMetadata(metadata);
    }

    public List<Metadata> getMetadataByType(ProviderType type) {
        List<Metadata> queryResults;

        if (type != null) {
            queryResults = metadataDAO.getMetadataByType(type);
        }
        else {
            queryResults = metadataDAO.getAllMetadata();
        }

        return queryResults;
    }

    public Metadata getMetadataByEntityId(String entityId) {
        List<Metadata> queryResults = metadataDAO.getMetadataByEntityId(entityId);
        if (queryResults.isEmpty()) {
            logger.debug("Metadata search with entity ID {} returned an empty result", entityId);
            return null;
        }
        else {
            return queryResults.get(0);
        }
    }
}
