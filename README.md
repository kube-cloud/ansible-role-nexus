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
        nexus_version: "3.21.1-01"
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
        karaf_lock_dir: "/kis/rmg/datas/karaf/lock"
        nexus_karaf_start_location_console: false
        nexus_jvm_minimum_memory: "512m"
        nexus_jvm_maximum_memory: "1024m"
        nexus_jvm_maximum_direct_memory: "1024m"
        nexus_jvm_log_file: "/kis/rmg/logs/jvm.log"
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
        httpProxyEnabled: false
        httpProxyHost: ""
        httpProxyPort: 80
        httpProxyUsername: ""
        httpProxyPassword: ""
        httpsProxyEnabled: false
        httpsProxyHost: ""
        httpsProxyPort: 443
        httpsProxyUsername: ""
        httpsProxyPassword: ""
        nonProxyHosts: ""
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
