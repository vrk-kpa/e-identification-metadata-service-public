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
import fi.vm.kapa.identification.type.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
@Path("/metadata")
public class MetadataResource {

    private static final Logger logger = LoggerFactory.getLogger(MetadataResource.class);
    private static final int HTTP_OK = 200;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    @Named("defaultProps")
    private Properties defaultProps;

    @Value("${aar.base.url}")
    private String aarBaseUrl;

    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getMetadataByType(@QueryParam("type") String providerType) {
        Response response;
        logger.debug("Got request for metadata query by type: {}", (providerType != null ? providerType : "ALL"));
        try {
            ProviderType type = (providerType != null ? ProviderType.valueOf(providerType) : null);
            List<Metadata> queryResults = metadataService.getMetadataByType(type);
            List<MetadataDTO> results = queryResults.stream().map(this::createDTO).collect(Collectors.toList());
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
        MetadataDTO metadataDTO = createDTO(metadataService.getMetadataByEntityId(entityId));
        if (metadataDTO == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        else {
            return metadataDTO;
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response addNewMetadata(MetadataDTO metadataDTO) {
        Response response;
        logger.debug("Received new metadata to be added, entity ID: {}", metadataDTO.getEntityId());
        try {
            metadataService.addMetadata(createFromDTO(metadataDTO));
            response = Response.ok().build();
        } catch (Exception e) {
            logger.error("Error creating new metadata to database", e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    private MetadataDTO createDTO(Metadata metadata) {
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

            return dto;
        }
    }

    private Metadata createFromDTO(MetadataDTO dto) {
        if (dto == null) {
            return null;
        }
        else {
            Metadata metadata = new Metadata();

            metadata.setName(dto.getName());
            metadata.setEntityid(dto.getEntityId());
            metadata.setDnsName(dto.getDnsName());
            metadata.setProviderType(dto.getProviderType());
            metadata.setLevelOfAssurance(dto.getLevelOfAssurance());
            metadata.setSessionProfile(dto.getSessionProfile());
            metadata.setAttributeLevelOfAssurance(dto.getAttributeLevelOfAssurance());
            metadata.setPubKey(dto.getPubKey());
            metadata.setAcsAddress(dto.getAcsAddress());
            metadata.setSloAddress(dto.getSloAddress());
            metadata.setUserAttributes(dto.getUserAttributes());
            metadata.setServiceName_fi(dto.getDisplayName().getFi());
            metadata.setServiceName_sv(dto.getDisplayName().getSv());
            metadata.setServiceName_en(dto.getDisplayName().getEn());
            metadata.setOrganizationName_fi(dto.getOrganizationName().getFi());
            metadata.setOrganizationName_sv(dto.getOrganizationName().getSv());
            metadata.setOrganizationName_en(dto.getOrganizationName().getEn());
            metadata.setAdministrativeContact_name(dto.getAdministrativeContact().getName());
            metadata.setAdministrativeContact_email(dto.getAdministrativeContact().getEmail());
            metadata.setTechnicalContact_name(dto.getTechnicalContact().getName());
            metadata.setTechnicalContact_email(dto.getTechnicalContact().getEmail());
            metadata.setVtjVerificationRequired(dto.isVtjVerificationRequired());
            return metadata;
        }
    }
}
