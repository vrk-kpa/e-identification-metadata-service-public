# KaPA Metadata backend service
**KaPA Metadata server**

This component is used to store all metadata related to the whole authentication system.
This is the backend server component which also generates the necessary metadata files for Shibboleth and gives required meta-information for Proxy backend service about the metadata.

**Compiling the component**

This component can be compiled and packaged with Maven tool with the following commands:
```
mvn clean install
mvn assembly:single
```
This produces the required tar ball that contains the necessary binaries and configuration templates.

For local testing with Vagrant environment, there exists a helper script in /script directory which does the necessary work and unpacks the built package ready for Ansible provisioning.
This assumes that there exists environment specific directory structure for Ansible. Here's the required directory structure as an example for local Vagrant environment:
```
/data00/deploy/metadata-server/vagrant
```
**Note!** Copy Ansible user's public key file into this repository's directory /ssl before running vagrant up.

**Works as follows on Ubuntu:**

Edit the hosts file
```
sudo vim /etc/hosts
```
Add the line
```
192.168.10.14	metadata.vagrant.dev
```
Start Vagrant virtual box (could be a while)
```
vagrant up
```
Complete by pushing configurations from your Ansible repository. Example fileset can be found from 'kapa-config' repository.
There's also a Vagrant example SQL initialization file for PostgreSQL for the database. This should be imported before running the Ansible provisioning.

**Errors**

In case of errors, bugs or security issues please contact kapa@vrk.fi.

