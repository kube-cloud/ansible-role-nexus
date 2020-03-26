# Ansible Role for Nexus

[![Travis](https://img.shields.io/travis/com/kube-cloud/ansible-role-nexus/master?style=flat)](https://travis-ci.com/kube-cloud/ansible-role-nexus)
[![GitHub release](https://img.shields.io/github/release/kube-cloud/ansible-role-nexus.svg)](https://github.com/kube-cloud/ansible-role-nexus)
[![GitHub license](https://img.shields.io/github/license/kube-cloud/ansible-role-nexus.svg)](https://github.com/kube-cloud/ansible-role-nexus/blob/master/LICENSE)
[![Ansible Role](https://img.shields.io/ansible/role/d/47147.svg?style=flat)](https://galaxy.ansible.com/kube-cloud/nexus)

Ansible Role for Atlassian Nexus Installation.

<a href="https://www.kube-cloud.com/"><img width="300" src="https://kube-cloud.com/images/branding/logo/kubecloud-logo-single_writing_horizontal_color_300x112px.png" /></a>
<a href="https://www.redhat.com/fr/technologies/management/ansible"><img width="200" src="https://getvectorlogo.com/wp-content/uploads/2019/01/red-hat-ansible-vector-logo.png" /></a>
<a href="https://www.sonatype.com//"><img width="120" src="https://avatars1.githubusercontent.com/u/44938?s=280&v=4" /></a>

# Supported Version

* Sonatype Nexus 3+

# Supported OS

* CentOS 6/7
* RedHat 6/7
* Ubuntu Trusty/Xenial/Bionic
* Debian Jessie/Strech

# Depend On

* jetune.java (Only Java 1.8 accepted - @see official Sonatype Nexus Prerequisites)

# Usage

* Install Role ``` ansible-galaxy install jetune.nexus ```
* use in your playbook
```
---

- hosts: all
  become: true
  tasks:
	
   # Install Open JDK 8
   - name: "({{ ansible_distribution }}) - NEXUS::PREPARE - Install OpenJDK 11"
     include_role:
      name: jetune.java
     vars:
      from_repo: false
      implementation: OPENJDK
      v_major: 8
      v_minor: 40
      build: 25
      os: linux
      arch: x64
      date: 10_feb_2015
      checksum: md5:4980716637f353cfb27467d57f2faf9b
      alternative_priority: 200
      is_default: true

    - name: "Include nexus role"
      import_role:
        name: ansible-role-nexus
      vars:
        nexus_version: "3.21.2-03"
        nexus_checksum: "sha1:f7dd02a755f5a313380dbd18f5c0d64c0b5419fb"
        nexus_owner: "root"
        nexus_group: "root"
        nexus_create_home: false
        nexus_owner_system: true
        nexus_shell: "/usr/sbin/nologin"
        nexus_installdir: "/opt/sonatype/nexus-{{ nexus_version }}"
        nexus_datadir: "/kis/rmg/datas/"
        nexus_logdir: "/kis/rmg/logs/"
        nexus_tempdir: "/kis/rmg/tmp"
        nexus_configdir: "/kis/rmg/configuration"
        nexus_karaf_lock_dir: "/kis/rmg/datas/locks"
        nexus_karaf_start_location_console: false
        nexus_jvm_minimum_memory: "512m"
        nexus_jvm_maximum_memory: "1024m"
        nexus_jvm_maximum_direct_memory: "1024m"
        nexus_jvm_log_file: "{{ nexus_logdir | regex_replace('\\/$', '') }}/jvm.log"
        nexus_prefered_ipv4: true
        nexus_application_host: "0.0.0.0"
        nexus_application_port: 8080
        nexus_application_context_path: "/"
        nexus_edition: "nexus-oss-edition"
        nexus_features: "nexus-oss-feature"
        nexus_hazelcast_discovery: true
        nexus_email_server_host: "ssl0.ovh.net"
        nexus_email_server_port: 465
        nexus_email_server_username: "nexus@dummy.server.com"
        nexus_email_server_password: "nexus.password"
        nexus_email_from_address: "nexus@dummy.server.com"
        nexus_email_subject_prefix: "[Nexus]"
        nexus_email_tls_enabled: true
        nexus_email_tls_required: true
        nexus_email_ssl_on_connect_enabled: true
        nexus_email_ssl_check_server_identity_enabled: true
        nexus_email_trust_store_enabled: true
        nexus_http_proxy_enabled: false
        nexus_http_proxy_host: ""
        nexus_http_proxy_port: 80
        nexus_http_proxy_username: ""
        nexus_http_proxy_password: ""
        nexus_https_proxy_enabled: false
        nexus_https_proxy_host: ""
        nexus_https_proxy_port: 443
        nexus_https_proxy_username: ""
        nexus_https_proxy_password: ""
        nexus_non_proxy_hosts: ""
        nexus_adminitrator_user: "admin"
        nexus_administrator_password: "admin"
        nexus_service_type: "forking"
        nexus_blobstores_clean: true
        nexus_blobstores:
          - name: "bs1"
            type: file
            path: "custom/blobstore1"
          - name: "bs2"
            type: file
            path: "custom/blobstore2"
          - name: "bs3"
            type: file
            path: "custom/blobstore3"
        nexus_repositories:
          - name: "mvn-hosted-repo-snapshot"
            type: "hosted"
            format: "maven2"
            online: true
            store: "default"
            strictContentValidation: true
            writePolicy: "ALLOW_ONCE"
            cleanupPolicies: []
            mavenVersionPolicy: "SNAPSHOT"
            mavenLayoutPolicy: "PERMISSIVE"
          - name: "mvn-hosted-repo-release"
            type: "hosted"
            format: "maven2"
            online: true
            store: "default"
            strictContentValidation: true
            writePolicy: "ALLOW"
            cleanupPolicies: []
            mavenVersionPolicy: "RELEASE"
            mavenLayoutPolicy: "STRICT"
          - name: "mvn-proxy-repo1-central"
            type: "proxy"
            format: "maven2"
            online: true
            store: "default"
            strictContentValidation: true
            writePolicy: "ALLOW"
            cleanupPolicies: []
            proxyUsername: ""
            proxyPassword: ""
            proxyRemoteUrl: "https://repo1.maven.org/maven2/"
            proxyContentMaxAge: -1
            proxyMetadataMaxAge: 1440
            negativeCacheEnabled: true
            negativeCachetimeToLive: 1440
          - name: "docker-hosted-repo-release"
            type: "hosted"
            format: "docker"
            online: true
            store: "default"
            strictContentValidation: true
            writePolicy: "ALLOW"
            cleanupPolicies: []
            dockerForceBasicAuth: false
            dockerV1Enabled: true
            dockerHttpPort: 0
          - name: "docker-proxy-repo-release"
            type: "proxy"
            format: "docker"
            online: true
            store: "default"
            strictContentValidation: true
            writePolicy: "ALLOW"
            cleanupPolicies: []
            dockerProxyIndexType: "REGISTRY"
            dockerProxyUseTrustStoreForIndexAccess: true
            dockerProxyCacheForeignLayers: true
            dockerProxyForeignLayerUrlWhitelist:
              - ".*"
              - ".*\\.docker\\.kube-cloud\\.com"
            proxyUsername: "admin"
            proxyPassword: "admin"
            proxyRemoteUrl: "https://registry-1.docker.io"
            proxyContentMaxAge: -1
            proxyMetadataMaxAge: 1440
            negativeCacheEnabled: true
            negativeCachetimeToLive: 1440
        nexus_roles:
          - id: "kc-role1"
            name: "kc-role1"
            description: "KubeCloud role 1"
            privileges:
              - "nx-repository-view-docker-docker-proxy-repo-release-read"
              - "nx-repository-view-docker-docker-proxy-repo-release-browse"
              - "nx-repository-view-docker-docker-hosted-repo-release-read"
              - "nx-repository-view-docker-docker-hosted-repo-release-browse"
            roles:
              - "nx-anonymous"
          - id: "kc-role2"
            name: "kc-role2"
            description: "KubeCloud role 2"
            privileges:
              - "nx-repository-view-maven2-mvn-proxy-repo1-central-read"
              - "nx-repository-view-maven2-mvn-proxy-repo1-central-browse"
              - "nx-repository-view-maven2-mvn-hosted-repo-snapshot-read"
              - "nx-repository-view-maven2-mvn-hosted-repo-snapshot-browse"
              - "nx-repository-view-maven2-mvn-hosted-repo-release-read"
              - "nx-repository-view-maven2-mvn-hosted-repo-release-browse"
        nexus_users:
          - id: "kc-user1"
            lastName: "Last Name 1"
            firstName: "First Name 1"
            email: "noreply@kube-cloud.com"
            password: "ch@ngeThat"
            roles:
              - "kc-role1"
          - id: "kc-user2"
            lastName: "Last Name 2"
            firstName: "First Name 2"
            email: "noreply@kube-cloud.com"
            password: "ch@ngeThat"
            roles:
              - "kc-role2"
