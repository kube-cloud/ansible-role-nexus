import groovy.json.JsonSlurper
import org.sonatype.nexus.email.EmailManager

// Get the Platform Email Manager
def emailManager = container.lookup(EmailManager.class.getName())

// Get the current platform Email configuration
def emailConfiguration = emailManager.getConfiguration()

// Transform submitted argument to JSon Structure
jsonArguments = new JsonSlurper().parseText(args)

// Update the current configuration with arguments values
emailConfiguration.with {
        enabled = jsonArguments.enabled
        host = jsonArguments.emailServerHost
        port = Integer.valueOf(jsonArguments.port)
        username = jsonArguments.username
        password = jsonArguments.password
        fromAddress = jsonArguments.fromAdress
        subjectPrefix = jsonArguments.subjectPrefix
        startTlsEnabled = jsonArguments.startTlsEnabled
        startTlsRequired = jsonArguments.startTlsRequired
        sslOnConnectEnabled = jsonArguments.sslOnConnectEnabled
        sslCheckServerIdentityEnabled = jsonArguments.sslCheckServerIdentityEnabled
        nexusTrustStoreEnabled = jsonArguments.nexusTrustStoreEnabled
}

// Update the platform email configuration
emailManager.setConfiguration(emailConfiguration)