import groovy.json.JsonSlurper
import org.sonatype.nexus.repository.Repository

// Transform submitted argument to JSon Structure
def parsed_args = new JsonSlurper().parseText(args)

// Get repository by name
Repository repo = repository.repositoryManager.get(parsed_args.name)

// If repository exists
if (repo != null) {
	
	// Delete that repository from manager
    repository.repositoryManager.delete(repo.name)
    
    // destroy repository himself
    repo.destroy()
}
