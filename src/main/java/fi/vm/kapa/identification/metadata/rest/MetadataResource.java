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
package fi.vm.kapa.identification.metadata.rest;

import fi.vm.kapa.identification.dto.MetadataDTO;
import fi.vm.kapa.identification.dto.MultiLanguageDTO;
import fi.vm.kapa.identification.metadata.model.Metadata;
import fi.vm.kapa.identification.metadata.service.MetadataService;
import fi.vm.kapa.identification.type.EidasSupport;
import fi.vm.kapa.identification.type.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/metadata")
public class MetadataResource {

    private static final Logger logger = LoggerFactory.getLogger(MetadataResource.class);

    @Autowired
    private MetadataService metadataService;

    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getMetadataByType(@QueryParam("type") String providerType) {
        Response response;
        logger.debug("Got request for metadata query by type: {}", (providerType != null ? providerType : "ALL"));
        try {
            ProviderType type = (providerType != null ? ProviderType.valueOf(providerType) : null);
            List<Metadata> queryResults = metadataService.getMetadataByType(type);
            List<MetadataDTO> results = queryResults.stream()
                    .map(result ->
                            createDTO(result, ProviderType.SERVICE_PROVIDER.equals(type))
                    ).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(results)) {
                response = Response.ok().entity(results).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (IllegalArgumentException iae) {
            logger.warn("Bad provider type received");
            response = Response.status(Response.Status.BAD_REQUEST).entity("Invalid provider type").build();
        } catch (Exception e) {
            logger.error("Error getting metadata from database", e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @GET
    @Path("/{entityId}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public MetadataDTO getMetadataByEntityId(@PathParam("entityId") String entityId) {
        logger.debug("Got request for metadata query by entity ID: {}", entityId);
        MetadataDTO metadataDTO = createDTO(metadataService.getMetadataByEntityId(entityId), false);
        if (metadataDTO == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        else {
            return metadataDTO;
        }
    }

    private MetadataDTO createDTO(Metadata metadata, boolean addEidasContactAddress) {
        if (metadata == null) {
            return null;
        }
        else {
            MetadataDTO dto = new MetadataDTO();
            dto.setName(metadata.getName());
            dto.setEntityId(metadata.getEntityid());
            dto.setDnsName(metadata.getDnsName());
            dto.setAcsAddress(metadata.getAcsAddress());
            dto.setProviderType(metadata.getProviderType());
            dto.setLevelOfAssurance(metadata.getLevelOfAssurance());
            dto.setAttributeLevelOfAssurance(metadata.getAttributeLevelOfAssurance());
            dto.setSessionProfile(metadata.getSessionProfile());
            dto.setDisplayName(new MultiLanguageDTO(metadata.getServiceName_fi(),
                    metadata.getServiceName_en(),
                    metadata.getServiceName_sv()));
            dto.setVtjVerificationRequired(metadata.isVtjVerificationRequired());
            dto.setEidasSupport(metadata.getEidasSupport());
            if ( addEidasContactAddress ) {
                dto.setEidasContactAddress(metadata.getEidasContactAddress());
            }
            else {
                dto.setEidasContactAddress("");
            }

            return dto;
        }
    }
}
