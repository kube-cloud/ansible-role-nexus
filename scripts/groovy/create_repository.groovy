import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.sonatype.nexus.repository.config.Configuration

// Transform submitted argument to JSon Structure
def repoToCreate = new JsonSlurper().parseText(args)

// Get the repostory Manager
def repositoryManager = repository.repositoryManager

// Find if repository exists
def repoFounded = repositoryManager.get(repoToCreate.name)

// Script result
Map results = [changed: false, error: false]

// Error details
Map resultDetails = [status: 'created']

// Repository configuration
Configuration configuration = null

try {
	
	// If the repository don't exists
	if(repoFounded == null) {
		
		// Log
		log.info('Repository Creation [Name : {}, Format: {},  Type: {}, Store : {}, Online : {}, Content Validation : {}]', 
			repoToCreate.name, 
			repoToCreate.format, 
			repoToCreate.type, 
			repoToCreate.store,
			repoToCreate.online,
			repoToCreate.strictContentValidation)
		
		try {
			
			// Instantiate repository configuration (Nexus >= 3.2 compliant)
			configuration = repositoryManager.newConfiguration()
			
		} catch (MissingMethodException) {
			
			// Instantiate repository configuration (Nexus < 3.2 compliant)
			configuration = Configuration.newInstance()
		}
		
		// Initialize general parameters
		configuration.with {
			repositoryName = repoToCreate.name
			recipeName = repoToCreate.format + '-' + repoToCreate.type
			online = repoToCreate.online
			attributes = [
				storage: [
					blobStoreName: repoToCreate.store
				]
			]
		}
		
	} else {
		
		// Log
		log.info('Repository Already exists [Name : {}]', repoToCreate.name)
		
		// Get repository configuration copy
		configuration = repoFounded.configuration.copy()
	}
	
	// Initialize Storage Strict content validation validation
	configuration.attributes['storage']['strictContentTypeValidation'] = Boolean.valueOf(repoToCreate.strictContentValidation)
	
	// In case of group repsitory
	if (repoToCreate.type == 'group') {
		
		// Initialize group members
		configuration.attributes['group'] = [
			memberNames: repoToCreate.members
		]
	}
	
	// In case of hosted repository
	if (repoToCreate.type == 'hosted') {
		
		// Initialize the Storage policy
		configuration.attributes['storage']['writePolicy'] = repoToCreate.writePolicy.toUpperCase()
	}
	
	// In case of yum hosted repository
	if (repoToCreate.type == 'hosted' && repoToCreate.format == 'yum') {
		
		// Initialize Yum repository custom properties
		configuration.attributes['yum'] = [
			
			// Initialize repository Depth
			repodataDepth: repoToCreate.repoDataDepth.toInteger(),
			
			// Initialize repository Layout policy
			layoutPolicy: repoToCreate.layoutPolicy.toUpperCase()
        ]
	}
	
	// in case of proxy apt repository
	if (repoToCreate.type == 'proxy' && repoToCreate.format == 'apt') {
		
		// Initialize APT repository custom properties
		configuration.attributes['apt'] = [
			
			// Initialize distribution
			distribution: repoToCreate.distribution,
			
			// Initialize Flat flag
			flat: Boolean.valueOf(repoToCreate.flat)
        ]
	}
	
	// In case of APT hosted repository
	if (repoToCreate.type == 'hosted' && repoToCreate.format == 'apt') {
		
		// Initialize APT repository distribution property
		configuration.attributes['apt'] = [
			distribution: repoToCreate.distribution
        ]
        
        // Initialize APT repository other properties
        configuration.attributes['aptSigning'] = [
        	
        	// Initialize APT repository keypair property
        	keypair: repoToCreate.keypair,
        	
        	// Initialize APT repository passphrase property
        	passphrase: repoToCreate.passphrase
        ]
	}
	
	// In case of proxy repository
	if (repoToCreate.type == 'proxy') {
		
		// Initialize authentication
		authentication = (repoToCreate.proxyUsername == null || repoToCreate.proxyUsername.trim() == '') ? null : [
			type: 'username',
			username: repoToCreate.proxyUsername,
			password: repoToCreate.proxyPassword
        ]
        
        // Initialize HTTP Client
        configuration.attributes['httpclient'] = [
        	authentication: authentication,
        	blocked: false,
        	autoBlock: true,
        	connection: [
        		useTrustStore: false
            ]
        ]
        
        // Initialize proxy properties
        configuration.attributes['proxy'] = [
        	remoteUrl: repoToCreate.proxyRemoteUrl,
        	contentMaxAge: repoToCreate.get('proxyContentMaxAge', 1440.0) as int,
        	metadataMaxAge: repoToCreate.get('proxyMetadataMaxAge', 1440.0) as int
        ]
        
        // Initialize negate properties
        configuration.attributes['negativeCache'] = [
        	enabled: repoToCreate.get('negativeCacheEnabled', true),
        	timeToLive: repoToCreate.get('negativeCachetimeToLive', 1440.0) as int
        ]
	}
	
	// In case of hosted or proxy
    if (repoToCreate.type == 'proxy' || repoToCreate.type == 'hosted') {
    	
    	// Get cleanup policies
    	def cleanupPolicies = repoToCreate.cleanupPolicies as Set
        
        // If policies are set
        if (cleanupPolicies != null) {
        	
        	// Initialize cleanup attribute
            configuration.attributes['cleanup'] = [
            	policyName: cleanupPolicies
            ]
        }
    }
	
    // In case of docker proxy repository
    if (repoToCreate.type == 'proxy' && repoToCreate.format == 'docker') {
    	
    	// Initialize proxy properties
    	configuration.attributes['dockerProxy'] = [
    		indexType: repoToCreate.dockerProxyIndexType,
    		useTrustStoreForIndexAccess: repoToCreate.dockerProxyUseTrustStoreForIndexAccess,
    		foreignLayerUrlWhitelist: repoToCreate.dockerProxyForeignLayerUrlWhitelist,
    		cacheForeignLayers: repoToCreate.dockerProxyCacheForeignLayers
        ]
    }
	
    // In case of docker repo
    if (repoToCreate.format == 'docker') {
    	
    	// String httpPort
    	String strPort = "" + repoToCreate.get('dockerHttpPort')
    	
    	// Initialize custom docker repository properties
    	configuration.attributes['docker'] = [
    		forceBasicAuth: repoToCreate.dockerForceBasicAuth,
    		v1Enabled: repoToCreate.dockerV1Enabled,
    		httpPort: (strPort != null && strPort.isInteger() && (strPort as int) > 0 ) ? strPort as int : null
        ]
    }
	
    // In case of maven repo proxy or hosted
    if (repoToCreate.type in ['hosted', 'proxy'] && repoToCreate.format == 'maven2') {
    	
    	// Initialize maven properties
    	configuration.attributes['maven'] = [
    		versionPolicy: repoToCreate.mavenVersionPolicy != null ? repoToCreate.mavenVersionPolicy.toUpperCase() : null,
    		layoutPolicy : repoToCreate.mavenLayoutPolicy != null ? repoToCreate.mavenLayoutPolicy.toUpperCase() : null
        ]
    }
	
	// If the repository don't exists
	if (repoFounded == null) {
		
		// Create the new repository
		repositoryManager.create(configuration)
		
		// Set result details status
		resultDetails.put('status', 'created')
		
		// Initialize change attribute
		results['changed'] = true
		
		// Log
		log.info('Reporitory created [Name : {}]', repoToCreate.name)
		
	} else  {
		
		// Update existing repository
		repositoryManager.update(configuration)
		
		// Set result details status
		resultDetails.put('status', 'updated')
		
		// Initialize change attribute
		results['changed'] = true
		
		// Log
		log.info('Reporitory Updated [Name : {}]', repoToCreate.name)
	}
	
} catch (Exception e) {
	
	// Set result details status
	resultDetails.put('status', 'error')
	
	// Initialize error message
	resultDetails.put('error_msg', e.toString())
	
	// Initialize error attribute
	results['error'] = true
	
	// Log
	log.error('Error when create Repository [Name : {}, Format: {},  Type: {}, Store : {}, Error : {}]', repoToCreate.name, repoToCreate.format, repoToCreate.type, repoToCreate.store, e.toString())
}

// Initialize script result details
results.put('action_details', resultDetails)
	
// Return result
return JsonOutput.toJson(results)