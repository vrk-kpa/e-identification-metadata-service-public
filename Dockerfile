# Pull base image
FROM e-identification-docker-virtual.vrk-artifactory-01.eden.csc.fi/e-identification-tomcat-base-image
COPY target/site /site

COPY conf/tomcat/logging.properties /usr/share/tomcat/conf/logging.properties
COPY conf/tomcat/metadata.xml /usr/share/tomcat/conf/Catalina/localhost/
COPY conf/tomcat/catalina.properties /usr/share/tomcat/conf/catalina.properties
COPY target/kapa-metadata-server.war /opt/metadata-server/

# Templates

COPY conf/tomcat/metadata-server.properties.template /data00/templates/store/
COPY conf/tomcat/server.xml.template /data00/templates/store/
COPY conf/tomcat/metadata-setenv.sh.template /data00/templates/store/
COPY conf/logging/log4j.properties.template /data00/templates/store/
COPY conf/ansible /data00/templates/store/ansible

WORKDIR /opt/metadata-server/
RUN jar -xvf kapa-metadata-server.war && \
    mkdir -p /opt/metadata-server-properties  && \
    mkdir -p /usr/share/tomcat/conf/ && \
    mkdir -p /usr/share/tomcat/properties && \
    ln -sf /data00/deploy/metadata-server.properties /opt/metadata-server-properties/metadata-server.properties && \
    ln -sf /data00/deploy/server.xml /usr/share/tomcat/conf/server.xml && \
    ln -sf /data00/deploy/tomcat_keystore /usr/share/tomcat/properties/tomcat_keystore && \
    ln -sf /data00/deploy/metadata-setenv.sh /usr/share/tomcat/bin/setenv.sh && \
    ln -sf /data00/deploy/kapa-ca /opt/kapa-ca && \
    chown -R tomcat:tomcat /usr/share/tomcat && \
    rm -fr /usr/share/tomcat/webapps/* && \
    rm -fr /usr/share/tomcat/server/webapps/* && \
    rm -fr /usr/share/tomcat/conf/Catalina/localhost/host-manager.xml && \
    rm -fr /usr/share/tomcat/conf/Catalina/localhost/manager.xml

CMD \
    mkdir -p /data00/logs && \
    chown -R tomcat:tomcat /data00/logs && \
    sudo -u tomcat /usr/share/tomcat/bin/catalina.sh run

