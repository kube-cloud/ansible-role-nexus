import groovy.json.JsonOutput
import groovy.json.JsonSlurper

// Transform submitted argument to JSon Structure
def blobStoreToCreate = new JsonSlurper().parseText(args)

// Get the blobStore Manager
def blobStoreManager = blobStore.getBlobStoreManager()

// Find if blobStore exists
def blobStoreFounded = blobStoreManager.get(blobStoreToCreate.name)

// Script result
Map results = [changed: false, error: false]

// Error details
Map resultDetails = [status: 'created']

try {
	
	// If the blobStore don't exists
	if(blobStoreFounded == null) {
		
		// Log
		log.info('Store Creation [Name : {}, Type: {}, Path: {}]', 
			blobStoreToCreate.name, 
			blobStoreToCreate.type)
		
		// In case on S3 Store
		if (blobStoreToCreate.type.toUpperCase() == "S3") {
			
			// Log
			log.info('S3 Store [Config : {}]', blobStoreToCreate.config)
			
			// Create S3 BlobStore
			blobStore.createS3BlobStore(blobStoreToCreate.name, blobStoreToCreate.config)
			
			// Log
			log.info('S3 BlobStore Created [Name: {}, Config : {}]', blobStoreToCreate.name, blobStoreToCreate.path)
			
		} else {
			
			// Log
			log.info('File Store [Path : {}]', blobStoreToCreate.path)
			
			// Create File path
			blobStore.createFileBlobStore(blobStoreToCreate.name, blobStoreToCreate.path)
			
			// Log
			log.info('File BlobStore Created [Name: {}, Path : {}]', blobStoreToCreate.name, blobStoreToCreate.path)
		}
		
		// Initialize result details
		resultDetails.put('status', 'created')
        
        // Initialize result
        results['changed'] = true
            
	} else {
		
		// Log
		log.info('Store Already exists [Name : {}]', blobStoreToCreate.name)
		
		// Get blobStore configuration copy
		configuration = blobStoreFounded.configuration.copy()
		
        // Initialize result
        resultDetails.put('status', 'exists')
	}
	
} catch (Exception e) {
	
	// Set result details status
	resultDetails.put('status', 'error')
	
	// Initialize error message
	resultDetails.put('error_msg', e.toString())
	
	// Initialize error attribute
	results['error'] = true
	
	// Log
	log.error('Error when create BlobStore [Name : {}, Type: {}, Error : {}]', 
		blobStoreToCreate.name, blobStoreToCreate.type, e.toString())
}

// Initialize script result details
results.put('action_details', resultDetails)
	
// Return result
return JsonOutput.toJson(results)