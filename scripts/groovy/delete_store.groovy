import groovy.json.JsonOutput
import groovy.json.JsonSlurper

// Transform submitted argument to JSon Structure
def blobStoreToDelete = new JsonSlurper().parseText(args)

// Get the blobStore Manager
def blobStoreManager = blobStore.getBlobStoreManager()

// Find if blobStore exists
def blobStoreFounded = blobStoreManager.get(blobStoreToDelete.name)

// If the blobStore exists
if(blobStoreFounded != null) {
	
	// Delete the existing Store
	blobStoreManager.delete(blobStoreToDelete.name)
}