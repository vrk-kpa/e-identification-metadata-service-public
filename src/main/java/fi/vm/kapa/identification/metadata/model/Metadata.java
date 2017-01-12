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
package fi.vm.kapa.identification.metadata.model;

import java.io.Serializable;

import javax.persistence.*;

import fi.vm.kapa.identification.type.ProviderType;
import fi.vm.kapa.identification.type.SessionProfile;

@Entity
@Table(name = "metadata")
@NamedNativeQueries({
        @NamedNativeQuery(name = "Metadata.getAll",
                          query = "SELECT m.* FROM metadata m",
                          resultClass = Metadata.class),
        @NamedNativeQuery(name = "Metadata.searchByProviderType",
                          query = "SELECT m.* FROM metadata m WHERE m.providertype = :providerType",
                          resultClass = Metadata.class),
        @NamedNativeQuery(name = "Metadata.searchByEntityId",
                query = "SELECT m.* FROM metadata m WHERE m.entityid = :entityId",
                resultClass = Metadata.class),
})
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1486430143396044752L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "entityid", nullable = false)
    private String entityid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "providertype", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "dnsname", nullable = false)
    private String dnsName;

    @Column(name = "loa", nullable = false)
    private String levelOfAssurance;

    @Column(name = "attr_loa", nullable = false)
    private String attributeLevelOfAssurance;

    @Column(name = "profile", nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionProfile sessionProfile;

    @Column(name = "acsAddress", nullable = false)
    private String acsAddress;

    @Column(name = "sloAddress", nullable = false)
    private String sloAddress;

    @Column(name = "pubKey", nullable = false)
    private String pubKey;

    @Column(name = "userAttributes", nullable = false)
    private String userAttributes;

    @Column(name = "serviceName_fi", nullable = false)
    private String serviceName_fi;

    @Column(name = "serviceName_sv", nullable = false)
    private String serviceName_sv;

    @Column(name = "serviceName_en", nullable = false)
    private String serviceName_en;

    @Column(name = "organizationName_fi", nullable = false)
    private String organizationName_fi;

    @Column(name = "organizationName_sv", nullable = false)
    private String organizationName_sv;

    @Column(name = "organizationName_en", nullable = false)
    private String organizationName_en;

    @Column(name = "administrativeContact_name", nullable = false)
    private String administrativeContact_name;

    @Column(name = "administrativeContact_email", nullable = false)
    private String administrativeContact_email;

    @Column(name = "technicalContact_name", nullable = false)
    private String technicalContact_name;

    @Column(name = "technicalContact_email", nullable = false)
    private String technicalContact_email;

    @Column(name = "vtjVerificationRequired", nullable = true)
    private boolean vtjVerificationRequired;

    // Setters and getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getEntityid() {
        return entityid;
    }

    public void setEntityid(String entityid) {
        this.entityid = entityid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType type) {
        this.providerType = type;
    }

    public String getDnsName() {
        return dnsName;
    }

    public void setDnsName(String identifier) {
        this.dnsName = identifier;
    }

    public String getLevelOfAssurance() {
        return levelOfAssurance;
    }

    public void setLevelOfAssurance(String levelOfAssurance) {
        this.levelOfAssurance = levelOfAssurance;
    }

    public SessionProfile getSessionProfile() {
        return sessionProfile;
    }

    public void setSessionProfile(SessionProfile sessionProfile) {
        this.sessionProfile = sessionProfile;
    }

    public String getAcsAddress() {
        return acsAddress;
    }

    public void setAcsAddress(String acsAddress) {
        this.acsAddress = acsAddress;
    }

    public String getSloAddress() {
        return sloAddress;
    }

    public void setSloAddress(String sloAddress) {
        this.sloAddress = sloAddress;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(String userAttributes) {
        this.userAttributes = userAttributes;
    }

    public String getServiceName_fi() {
        return serviceName_fi;
    }

    public void setServiceName_fi(String serviceName_fi) {
        this.serviceName_fi = serviceName_fi;
    }

    public String getServiceName_sv() {
        return serviceName_sv;
    }

    public void setServiceName_sv(String serviceName_sv) {
        this.serviceName_sv = serviceName_sv;
    }

    public String getServiceName_en() {
        return serviceName_en;
    }

    public void setServiceName_en(String serviceName_en) {
        this.serviceName_en = serviceName_en;
    }

    public String getOrganizationName_fi() {
        return organizationName_fi;
    }

    public void setOrganizationName_fi(String organizationName_fi) {
        this.organizationName_fi = organizationName_fi;
    }

    public String getOrganizationName_sv() {
        return organizationName_sv;
    }

    public void setOrganizationName_sv(String organizationName_sv) {
        this.organizationName_sv = organizationName_sv;
    }

    public String getOrganizationName_en() {
        return organizationName_en;
    }

    public void setOrganizationName_en(String organizationName_en) {
        this.organizationName_en = organizationName_en;
    }

    public String getAdministrativeContact_name() {
        return administrativeContact_name;
    }

    public void setAdministrativeContact_name(String administrativeContact_name) {
        this.administrativeContact_name = administrativeContact_name;
    }

    public String getAdministrativeContact_email() {
        return administrativeContact_email;
    }

    public void setAdministrativeContact_email(String administrativeContact_email) {
        this.administrativeContact_email = administrativeContact_email;
    }

    public String getTechnicalContact_name() {
        return technicalContact_name;
    }

    public void setTechnicalContact_name(String technicalContact_name) {
        this.technicalContact_name = technicalContact_name;
    }

    public String getTechnicalContact_email() {
        return technicalContact_email;
    }

    public void setTechnicalContact_email(String technicalContact_email) {
        this.technicalContact_email = technicalContact_email;
    }
    public String getAttributeLevelOfAssurance() {
        return attributeLevelOfAssurance;
    }
    public void setAttributeLevelOfAssurance(String attributeLevelOfAssurance) {
        this.attributeLevelOfAssurance = attributeLevelOfAssurance;
    }

    public boolean isVtjVerificationRequired() {
        return vtjVerificationRequired;
    }

    public void setVtjVerificationRequired(boolean vtjVerificationRequired) {
        this.vtjVerificationRequired = vtjVerificationRequired;
    }
}
