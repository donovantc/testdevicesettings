/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
		var oTextarea = new sap.m.TextArea({
			value : "test",
			width : "100%",
			rows : 25
			});
		
		
		window.screenWidth = window.screen.width;
		window.screenHeight = window.screen.height;
		window.devicePixelRatioOri = window.devicePixelRatio;
		
		var oButton = new sap.m.Button("myButtonBarDefault", {
		type: sap.m.ButtonType.Default,
		text: "Detect Device Settings",
		enabled: true,
			press : function() {
				var output = "";
				
				output += "current window.screen.width: " + window.screen.width + "\n";
				output += "current window.screen.height: " + window.screen.height + "\n";
				output += "current window.devicePixelRatio: " + window.devicePixelRatio + "\n";
				
				output += "initial window.screen.width: " + window.screenWidth + "\n";
				output += "initial window.screen.height: " + window.screenHeight + "\n";
				output += "initial window.devicePixelRatio: " + window.devicePixelRatioOri + "\n";
				
				output += 'sap.ui.Device.orientation:\n';
				for (property in sap.ui.Device.orientation) {
					if (!jQuery.isFunction(sap.ui.Device.orientation[property])) {
						output += property + ': ' + sap.ui.Device.orientation[property]+';\n';
					}
				}
				output += 'sap.ui.Device.resize:\n';
				for (property in sap.ui.Device.resize) {
					if (!jQuery.isFunction(sap.ui.Device.resize[property])) {
						output += property + ': ' + sap.ui.Device.resize[property]+';\n';
					}
				}
				output += 'sap.ui.Device.os:\n';
				for (property in sap.ui.Device.os) {
					output += property + ': ' + sap.ui.Device.os[property]+';\n';
				}
				output += 'sap.ui.Device.system:\n';
				for (property in sap.ui.Device.system) {
					output += property + ': ' + sap.ui.Device.system[property]+';\n';
				}
				output += 'sap.ui.Device.media:\n';
				for (property in sap.ui.Device.media) {
					if (!jQuery.isFunction(sap.ui.Device.media[property])) {
						output += property + ': ' + sap.ui.Device.media[property]+';\n';
					}
				}
				output += 'sap.ui.Device.support:\n';
				for (property in sap.ui.Device.support) {
					output += property + ': ' + sap.ui.Device.support[property]+';\n';
				}
				
				oTextarea.setValue(output);
			}
			});
		
		var oApp = new sap.m.App("myApp", {initialPage:"myPage1"});
		
		var oPage1 = new sap.m.Page("myPage1", {
		title: "Tablet test",
			});
		
		oPage1.addContent(oButton);
		oPage1.addContent(oTextarea);
		oApp.addPage(oPage1);
		
		oApp.placeAt("content");
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();