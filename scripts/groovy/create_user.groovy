import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.sonatype.nexus.security.authz.AuthorizationManager
import org.sonatype.nexus.security.user.UserManager
import org.sonatype.nexus.security.user.UserNotFoundException
import org.sonatype.nexus.security.user.User

// Transform submitted argument to JSon Structure
def userToCreate = new JsonSlurper().parseText(args)

// Script result
Map results = [changed: false, error: false]

// Error details
Map resultDetails = [status: 'created']

// Role Model to persist
def user = null

// Initialize inherited roles
roles = (userToCreate.roles == null ? new ArrayList<String>() : userToCreate.roles)

/**
 * Method used to check if user already exists
 */
private boolean userExists(String userId) {
	
	try {
		
		// Find if user exists
		User user = security.securitySystem.getUser(userId)
		
		// Compute and return result
		return user != null
		
	} catch (UserNotFoundException notFound) {
		
		// Return false
		return false
	}
}


try {
	
	// If the user don't exists
	if(!userExists(userToCreate.id)) {
		
		// Log
		log.info('User Creation [ID : {}, First Name : {}, Last Name : {}, Email: {}, Password : {}, Active : {}]', 
			userToCreate.id, 
			userToCreate.lastName,
			userToCreate.firstName,
			userToCreate.email,
			"*****************",
			userToCreate.enabled)
		
		// Create user
		security.addUser(userToCreate.id, userToCreate.firstName, userToCreate.lastName, userToCreate.email, userToCreate.enabled, userToCreate.password, roles)
		
		// Initialize result details
		resultDetails.put('status', 'created')
        
        // Initialize result
        results['changed'] = true
	}
	
} catch (Exception e) {
	
	// Set result details status
	resultDetails.put('status', 'error')
	
	// Initialize error message
	resultDetails.put('error_msg', e.toString())
	
	// Initialize error attribute
	results['error'] = true
	
	// Log
	log.error('Error when create User [ID : {}, First Name : {}, Last Name : {}, Email: {}, Password : {}, Active : {}, Error : {}]', 
		userToCreate.id, 
		userToCreate.lastName,
		userToCreate.firstName,
		userToCreate.email,
		"*****************",
		userToCreate.enabled,
		e.toString())
}

// Initialize script result details
results.put('action_details', resultDetails)
	
// Return result
return JsonOutput.toJson(results)