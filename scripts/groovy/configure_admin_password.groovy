import groovy.json.JsonSlurper

// Transform submitted argument to JSon Structure
jsonArguments = new JsonSlurper().parseText(args)

// Initialize Admin Password
security.securitySystem.changePassword(jsonArguments.adminUserName, jsonArguments.newAdminPassword)