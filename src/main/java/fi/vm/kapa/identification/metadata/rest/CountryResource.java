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

import fi.vm.kapa.identification.dto.CountryDTO;
import fi.vm.kapa.identification.dto.MultiLanguageDTO;
import fi.vm.kapa.identification.metadata.model.Country;
import fi.vm.kapa.identification.metadata.service.CountryService;
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
@Path("/country")
public class CountryResource {

    private static final Logger logger = LoggerFactory.getLogger(CountryResource.class);

    @Autowired
    private CountryService countryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response getCountries() {
        Response response;
        logger.debug("Got request for countries");
        try {
            List<Country> queryResults = countryService.getCountries();
            List<CountryDTO> results = queryResults.stream().map(this::createDTO).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(results)) {
                response = Response.ok().entity(results).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error("Error getting countries from database", e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    private CountryDTO createDTO(Country country) {
        if (country == null) {
            return null;
        }
        else {
            CountryDTO dto = new CountryDTO();
            dto.setCountryCode(country.getCountryCode());
            dto.setImgSrc(country.getImgSrc());
            dto.setDisplayName(new MultiLanguageDTO(country.getDisplayName_fi(),
                    country.getDisplayName_en(),
                    country.getDisplayName_sv()));
            dto.setLevelOfAssurance(country.getLevelOfAssurance());
            dto.setEidasLoginContext(country.getEidasLoginContext());
            dto.setSortOrder(country.getSortOrder());
            dto.setEnabled(country.isEnabled());
            return dto;
        }
    }

}
