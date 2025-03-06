package com.morpheus

import com.morpheusdata.core.util.HttpApiClient
import com.morpheusdata.core.util.HttpApiClient.RequestOptions

def morpheusUrl = morpheus.applianceUrl
def apiToken = morpheus.getApiAccessToken()

def requiredTags = [[name: 'five', value: 'five'], [name: 'six', value: 'six']] // update me with the tags you want to add
println "EXISTING INSTANCE TAGS: ${instance.metadata}"
 
HttpApiClient client = new HttpApiClient()

RequestOptions requestOptions = new RequestOptions()
requestOptions.headers = ["Authorization": "Bearer ${apiToken}"]
requestOptions.body = [ instance: 
						[
                        	addTags: requiredTags
                        
                        ] 
                      ]

def updateTagsResponse = client.callJsonApi(morpheusUrl, "/api/instances/${instance.id}", null, null, requestOptions, "PUT")
println "REQUEST BODY:\r\n${requestOptions.body}\r\n"
println "UPDATED TAGS SUCCESSFULLY:\r\n${updateTagsResponse.data.instance.tags}\r\n"
