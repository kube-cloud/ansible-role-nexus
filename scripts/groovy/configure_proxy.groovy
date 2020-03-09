import groovy.json.JsonSlurper

// Transform submitted argument to JSon Structure
jsonArguments = new JsonSlurper().parseText(args)

// if HTTP Proxy is enabled on argument call
if (jsonArguments.httpProxyEnabled) {
	
	// Remove Old HTTP Proxy configuration from core config
	core.removeHTTPProxy()
	
	// If proxy username is provided
    if (jsonArguments.httpProxyUsername) {
    	
    	// Initialize HTTP Proxy with basic authentication
        core.httpProxyWithBasicAuth(jsonArguments.httpProxyHost, jsonArguments.httpProxyPort as int, jsonArguments.httpProxyUsername, jsonArguments.httpProxyPassword)
        
    } else {
    
    	// Initialize HTTP Proxy without authentification
        core.httpProxy(jsonArguments.httpProxyHost, jsonArguments.httpProxyPort as int)
    }
}

// if HTTP Proxy is enabled on argument call
if (jsonArguments.httpsProxyEnabled) {
	
	// Remove Old HTTPS Proxy configuration from core config
	core.removeHTTPSProxy()
	
	// If proxy username is provided
    if (jsonArguments.httpsProxyUsername) {
    	
    	// Initialize HTTPS Proxy with basic authentication
        core.httpsProxyWithBasicAuth(jsonArguments.httpsProxyHost, jsonArguments.httpsProxyPort as int, jsonArguments.httpsProxyUsername, jsonArguments.httpsProxyPassword)
        
    } else {
    	
    	// Initialize HTTPS Proxy without authentification
        core.httpsProxy(jsonArguments.httpsProxyHost, jsonArguments.httpsProxyPort as int)
    }
}

// In one of HTTP/HTTPS is enabled
if (jsonArguments.httpProxyEnabled || jsonArguments.httpsProxyEnabled) {
	
	// Delete old non proxy host
    core.nonProxyHosts()
    
    // Reinitialize non proxyhost
    core.nonProxyHosts(jsonArguments.nonProxyHosts as String[])
}