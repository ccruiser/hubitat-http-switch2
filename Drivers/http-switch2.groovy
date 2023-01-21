/**
 *  HTTP Switch 2
 *
 *  Copyright 2023 Casey Cruise
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Change History:
 *
 *    Date        Who             What
 *    ----        ---             ----
 *    2023-01-20  ccruise         Forked from https://raw.githubusercontent.com/ogiewon/Hubitat/master/Drivers/http-momentary-switch.src/http-momentary-switch.groovy
 *    2023-01-21  ccruise         Modified for use of aqualinkd; added contact sensor; might need a future different device type(s)
 * 
 */

metadata {
    definition (name: "HTTP Switch 2", namespace: "ccruiser", author: "Casey Cruise", importUrl: "https://raw.githubusercontent.com/ccruiser/hubitat-http-switch2/master/Drivers/http-switch2.src/http-switch.groovy2") {
        capability "Switch"
        capability "Contact Sensor"
	}

    preferences {
        input(name: "deviceIP", type: "string", title:"Device IP Address", description: "Enter IP Address of your HTTP server", required: true, displayDuringSetup: true)
        input(name: "devicePort", type: "string", title:"Device Port", description: "Enter Port of your HTTP server (defaults to 80)", defaultValue: "80", required: false, displayDuringSetup: true)
        input(name: "devicePath", type: "string", title:"URL Path", description: "Rest of the URL, include forward slash.", displayDuringSetup: true)
        input(name: "deviceMethod", type: "enum", title: "POST, GET, or PUT", options: ["POST","GET","PUT"], defaultValue: "POST", required: true, displayDuringSetup: true)
        input(name: "deviceContent", type: "enum", title: "Content-Type", options: getCtype(), defaultValue: "application/x-www-form-urlencoded", required: true, displayDuringSetup: true)
        input(name: "deviceHeader", type: "string", title:"Device Header", description: "Extra header", displayDuringSetup: true)
        input(name: "deviceBodyOn", type: "string", title:"Body for On", description: "Body of message for on switch", displayDuringSetup: true)
        input(name: "deviceBodyOff", type: "string", title:"Body for Off", description: "Body of message for off switch", displayDuringSetup: true)
        input name: "isDebugEnabled", type: "bool", title: "Enable debug logging?", defaultValue: false, required: false
    }
}

def parse(String description) {
    logDebug(description)
}

def getCtype() {
    def cType = []
    cType = ["application/x-www-form-urlencoded","application/json"]
}

def push(int state) {
    //set values for events
    def valState = "off"
    def valContact = "closed"
    if (state) {
       valState = "on"
        valContact = "open"
    }
    sendEvent(name: "switch", value: valState, isStateChange: true)
    sendEvent(name: "contact", value: valContact, isStateChange: true)
    runCmd(devicePath, deviceMethod, state)
}

def on() {
	push(1)
}

def off() {
	push(0)
}

def runCmd(String varCommand, String method, int valToSet) {
	def localDevicePort = (devicePort==null) ? "80" : devicePort
	def path = varCommand 
    //set the http body
	def body = ""
    if (valToSet == 1)
    {
      if (deviceBodyOn) {
          body = (deviceBodyOn) ? deviceBodyOn : ""
      }
      else {
          body = (deviceBodyOn) ? deviceBodyOn : "value="+valToSet
      }
    }
    else {
          if (deviceBodyOff) {
            body = (deviceBodyOff) ? deviceBodyOff : ""
          }
          else {
            body = (deviceBodyOff) ? deviceBodyOff : "value="+valToSet
          }
    }

    //set the http headers
	def headers = [:] 
    headers.put("HOST", "${deviceIP}:${localDevicePort}")
    if (deviceHeader) {
        def header = deviceHeader.toString().split(":")
        headers.put(header[0], header[1])
    }
    headers.put("Content-Type", deviceContent)

    try {
        def hubAction = new hubitat.device.HubAction(
            method: method,
            path: path,
            body: body,
            headers: headers
            )
        logDebug(hubAction)
        return hubAction
    }
    catch (Exception e) {
        log.debug "runCmd hit exception ${e} on ${hubAction}"
	}  
}

private logDebug(msg) {
    if (isDebugEnabled == true) {
        if (msg instanceof List && msg.size() > 0) {
            msg = msg.join(", ");
        }
        log.debug "$msg"
    }
}
