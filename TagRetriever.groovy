package com.morpheus
 
import org.slf4j.*
import groovy.util.logging.Slf4j
import com.morpheus.*
import com.morpheusdata.core.util.HttpApiClient
import com.morpheusdata.core.util.HttpApiClient.RequestOptions
import groovy.json.JsonOutput
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

def logWithTimestamp = { message ->
    def timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    println "${timestamp} - ${message}"
}

if (!instance?.tags) {
    logWithTimestamp("INSTANCE TAGS: No tags found.\r\n")
} else {
    logWithTimestamp("INSTANCE TAGS:\r\n${JsonOutput.prettyPrint(JsonOutput.toJson(instance.tags))}\r\n")
}

def getInstancesWithMissingTags(String missingTag) {
    def morpheusUrl = morpheus.applianceUrl
    def apiToken = morpheus.getApiAccessToken()
 
    HttpApiClient client = new HttpApiClient()
    RequestOptions requestOptions = new RequestOptions()
    
    requestOptions.headers = ["Authorization": "Bearer ${apiToken}"]

    def instanceApi = client.callJsonApi(morpheusUrl, "/api/instances?max=1000", null, null, requestOptions, "GET")
    
    if (instanceApi.success) {
        def instances = instanceApi.data.instances ?: []
        logWithTimestamp("Total instances retrieved: ${instances.size()}")

        def instancesWithoutTag = instances.findAll { instance ->
            def tags = instance.tags?.collect { it.name } ?: []
            return tags.isEmpty() || !tags.contains(missingTag)
        }.collect { it.id } 

        logWithTimestamp("Total instances missing tag '${missingTag}': ${instancesWithoutTag.size()}")
        return instancesWithoutTag
    } else {
        logWithTimestamp("Error fetching instances: ${instanceApi.errors}")
        return []
    }
}

def missingTag = "Uthman"
def instancesToUpdate = getInstancesWithMissingTags(missingTag)
logWithTimestamp("INSTANCES WITHOUT REQUIRED TAG '${missingTag}':\r\n${instancesToUpdate}\r\n")
