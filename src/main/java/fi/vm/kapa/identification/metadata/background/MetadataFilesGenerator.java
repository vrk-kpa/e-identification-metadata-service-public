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
package fi.vm.kapa.identification.metadata.background;

import fi.vm.kapa.identification.metadata.model.Metadata;
import fi.vm.kapa.identification.metadata.service.MetadataService;
import fi.vm.kapa.identification.type.ProviderType;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.*;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.X509Certificate;
import org.opensaml.xml.signature.X509Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.List;

@Service
public class MetadataFilesGenerator {

    private static final Logger logger = LoggerFactory.getLogger(MetadataFilesGenerator.class);
    private static final String NAME_ID_FORMAT = "urn:oasis:names:tc:SAML:2.0:nameid-format:transient";

    @Autowired
    private MetadataService metadataService;

    public void regenerate() {
        long start = System.currentTimeMillis();

        logger.info("Staring metadata files regeneration...");

        try {
            DefaultBootstrap.bootstrap();

            List<Metadata> serviceProviders = metadataService.getMetadataByType(ProviderType.SERVICE_PROVIDER);
            List<Metadata> identityProviders = metadataService.getMetadataByType(ProviderType.IDENTITY_PROVIDER);

            createSPMetadata(serviceProviders);
            createIdPMetadata(identityProviders);
        }
        catch (Exception e) {
            logger.error("Error in regenerating metadata files", e);
        }

        logger.info("Regeneration finished, took {} ms", System.currentTimeMillis() - start);
    }

    private void createSPMetadata(List<Metadata> serviceProviders) throws Exception {
        EntitiesDescriptor entities = createSAMLObject(EntitiesDescriptor.class);

        serviceProviders.forEach(serviceProvider -> {
            try {
                EntityDescriptor entityDescriptor = createSAMLObject(EntityDescriptor.class);
                entityDescriptor.setEntityID(serviceProvider.getEntityid());

                SPSSODescriptor spDescriptor = createSAMLObject(SPSSODescriptor.class);
                spDescriptor.addSupportedProtocol(SAMLConstants.SAML20P_NS);
                spDescriptor.setWantAssertionsSigned(true);

                KeyInfo keyInfo = createSAMLObject(KeyInfo.class);
                X509Data x509Data = createSAMLObject(X509Data.class);
                X509Certificate x509Cert = createSAMLObject(X509Certificate.class);
                x509Cert.setValue("asdf");
                x509Data.getX509Certificates().add(x509Cert);
                keyInfo.getX509Datas().add(x509Data);

                KeyDescriptor keyDescriptor = createSAMLObject(KeyDescriptor.class);
                keyDescriptor.setUse(UsageType.SIGNING);
                keyDescriptor.setKeyInfo(keyInfo);
                spDescriptor.getKeyDescriptors().add(keyDescriptor);

                NameIDFormat nameFormat = createSAMLObject(NameIDFormat.class);
                nameFormat.setFormat(NAME_ID_FORMAT);
                spDescriptor.getNameIDFormats().add(nameFormat);

                AssertionConsumerService consumer = createSAMLObject(AssertionConsumerService.class);
                consumer.setIndex(1);
                consumer.setBinding(SAMLConstants.SAML2_POST_BINDING_URI);
                consumer.setLocation("http://saml.post.url.com/SAML/Post");
                spDescriptor.getAssertionConsumerServices().add(consumer);

                entityDescriptor.getRoleDescriptors().add(spDescriptor);
                entities.getEntityDescriptors().add(entityDescriptor);
            }
            catch (Exception e) {
                logger.error("Error parsing service provider with entity ID: {}", serviceProvider.getEntityid(), e);
            }
        });

        FileOutputStream fos = new FileOutputStream(new File("/tmp/sp_metadata.xml"));
        fos.write(createXmlString(entities).getBytes());
    }

    private void createIdPMetadata(List<Metadata> identityProviders) throws Exception {
        EntitiesDescriptor entities = createSAMLObject(EntitiesDescriptor.class);

        identityProviders.forEach(identityProvider -> {
            try {
                EntityDescriptor entityDescriptor = createSAMLObject(EntityDescriptor.class);
                entityDescriptor.setEntityID(identityProvider.getEntityid());

                IDPSSODescriptor idpDescriptor = createSAMLObject(IDPSSODescriptor.class);
                idpDescriptor.addSupportedProtocol(SAMLConstants.SAML20P_NS);
                idpDescriptor.setWantAuthnRequestsSigned(true);

                KeyInfo keyInfo = createSAMLObject(KeyInfo.class);
                X509Data x509Data = createSAMLObject(X509Data.class);
                X509Certificate x509Cert = createSAMLObject(X509Certificate.class);
                x509Cert.setValue("asdf");
                x509Data.getX509Certificates().add(x509Cert);
                keyInfo.getX509Datas().add(x509Data);

                KeyDescriptor keyDescriptor = createSAMLObject(KeyDescriptor.class);
                keyDescriptor.setUse(UsageType.SIGNING);
                keyDescriptor.setKeyInfo(keyInfo);
                idpDescriptor.getKeyDescriptors().add(keyDescriptor);

                NameIDFormat nameFormat = createSAMLObject(NameIDFormat.class);
                nameFormat.setFormat(NAME_ID_FORMAT);
                idpDescriptor.getNameIDFormats().add(nameFormat);

                SingleSignOnService signOnSrv = createSAMLObject(SingleSignOnService.class);
                signOnSrv.setBinding(SAMLConstants.SAML2_POST_BINDING_URI);
                signOnSrv.setLocation("http://saml.post.url.com/SAML/Post");
                idpDescriptor.getSingleSignOnServices().add(signOnSrv);

                entityDescriptor.getRoleDescriptors().add(idpDescriptor);
                entities.getEntityDescriptors().add(entityDescriptor);
            }
            catch (Exception e) {
                logger.error("Error parsing service provider with entity ID: {}", identityProvider.getEntityid(), e);
            }
        });

        FileOutputStream fos = new FileOutputStream(new File("/tmp/idp_metadata.xml"));
        fos.write(createXmlString(entities).getBytes());
    }

    @SuppressWarnings("unchecked")
    private static <T> T createSAMLObject(final Class<T> object) throws Exception {
        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        QName name = (QName)object.getDeclaredField("DEFAULT_ELEMENT_NAME").get(null);

        return (T)builderFactory.getBuilder(name).buildObject(name);
    }

    private String createXmlString(XMLObject content) throws Exception {
        Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(content);
        marshaller.marshall(content, xmlDocument);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        DOMSource source = new DOMSource(xmlDocument);
        transformer.transform(source, streamResult);
        writer.close();

        return writer.toString();
    }
}
