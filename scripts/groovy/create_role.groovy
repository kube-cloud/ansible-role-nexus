import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.sonatype.nexus.security.authz.AuthorizationManager
import org.sonatype.nexus.security.role.Role
import org.sonatype.nexus.security.user.UserManager
import org.sonatype.nexus.security.role.NoSuchRoleException

// Transform submitted argument to JSon Structure
def roleToCreate = new JsonSlurper().parseText(args)

// Get the role Manager
AuthorizationManager authManager = security.securitySystem.getAuthorizationManager(UserManager.DEFAULT_SOURCE)

// Script result
Map results = [changed: false, error: false]

// Error details
Map resultDetails = [status: 'created']

// Initialize role provileges
privileges = (roleToCreate.privileges == null ? new ArrayList<String>() : roleToCreate.privileges)

// Initialize inherited roles
roles = (roleToCreate.roles == null ? new ArrayList<String>() : roleToCreate.roles)

// Role Model to persist
def role = null

/**
 * Method used to check if role already exists
 */
private boolean roleExists(String roleId, AuthorizationManager authManager) {
	
	try {
		
		// Find if role exists
		role = authManager.getRole(roleId)
		
		// Compute and return result
		return role != null
		
	} catch (NoSuchRoleException notFound) {
		
		// Return false
		return false
	}
}


try {
	
	// If the role don't exists
	if(!roleExists(roleToCreate.id, authManager)) {
		
		// Log
		log.info('Role Creation [ID : {}, Name : {}, Description: {}]', 
			roleToCreate.id, 
			roleToCreate.name,
			roleToCreate.description)
		
		// Create role
		security.addRole(roleToCreate.id, roleToCreate.name, roleToCreate.description, privileges, roles)
		
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
	log.error('Error when create Role [Id : {}, Name : {}, Description: {}, Error : {}]', 
		roleToCreate.id,
		roleToCreate.name,
		roleToCreate.description,
		e.toString())
}

// Initialize script result details
results.put('action_details', resultDetails)
	
// Return result
return JsonOutput.toJson(results)