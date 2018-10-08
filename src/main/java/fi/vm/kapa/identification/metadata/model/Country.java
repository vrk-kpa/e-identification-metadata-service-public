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

@Entity
@Table(name = "country")
@NamedNativeQueries({
        @NamedNativeQuery(name = "Country.getAll",
                query = "SELECT c.* FROM country c",
                resultClass = Country.class)
})
public class Country implements Serializable {

    private static final long serialVersionUID = -3292540716865428568L;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "countryCode", nullable = false)
    private String countryCode;

    @Column(name = "imgSrc", nullable = false)
    private String imgSrc;

    @Column(name = "displayName_fi", nullable = false)
    private String displayName_fi;

    @Column(name = "displayName_sv", nullable = false)
    private String displayName_sv;

    @Column(name = "displayName_en", nullable = false)
    private String displayName_en;

    @Column(name = "levelOfAssurance", nullable = false)
    private String levelOfAssurance;

    @Column(name = "eidasLoginContext", nullable = false)
    private String eidasLoginContext;

    @Column(name = "sortOrder", nullable = false)
    private Long sortOrder;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    // Setters and getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getDisplayName_fi() {
        return displayName_fi;
    }

    public void setDisplayName_fi(String displayName_fi) {
        this.displayName_fi = displayName_fi;
    }

    public String getDisplayName_sv() {
        return displayName_sv;
    }

    public void setDisplayName_sv(String displayName_sv) {
        this.displayName_sv = displayName_sv;
    }

    public String getDisplayName_en() {
        return displayName_en;
    }

    public void setDisplayName_en(String displayName_en) {
        this.displayName_en = displayName_en;
    }

    public String getLevelOfAssurance() {
        return levelOfAssurance;
    }

    public void setLevelOfAssurance(String levelOfAssurance) {
        this.levelOfAssurance = levelOfAssurance;
    }

    public String getEidasLoginContext() {
        return eidasLoginContext;
    }

    public void setEidasLoginContext(String eidasLoginContext) {
        this.eidasLoginContext = eidasLoginContext;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
