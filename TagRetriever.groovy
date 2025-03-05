package com.morpheus
 
import org.slf4j.*
import groovy.util.logging.Slf4j
import com.morpheus.*
import grails.util.Holders
import groovy.json.JsonOutput
import java.time.LocalDateTime
import java.util.Set
import java.time.format.DateTimeFormatter
 
serverId = server.id ?: instance.container?.serverId
 
def opts = [server: []]
 
def logWithTimestamp = { message ->
    def timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    println "${timestamp} - ${message}"
}

logWithTimestamp("INSTANCE TAGS:\r\n${instance.tags}\r\n")
 
ComputeServer.withNewSession { session ->
    opts.server = ComputeServer.get(serverId)
    def serverTags = opts.server.getTags()
    logWithTimestamp("SERVER TAGS:\r\n${serverTags}\r\n")

  //  def updatedInterfaces = updateResults.toSet()
  //  opts.server.setInterfaces(updatedInterfaces)
 
  //  opts.server.save(flush: true, failOnError: true
  logWithTimestamp("SERVER DUMP:\r\n${opts.server.dump()}")

}
 
@Slf4j
class TagRetriever {
    private grailsApplication
    private mainContext
    private serverService
    private morpheusComputeService
 
    public TagRetriever() {
        this.grailsApplication = Holders.grailsApplication
        this.mainContext = Holders.grailsApplication.mainContext
        this.serverService = this.mainContext["serverService"]
        this.morpheusComputeService = this.mainContext["morpheusComputeService"]
    }
 
    def getServerTags() {
        return null
    }
}
