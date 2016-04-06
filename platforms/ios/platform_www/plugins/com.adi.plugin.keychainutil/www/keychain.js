cordova.define("com.adi.plugin.keychainutil.Keychain", function(require, exports, module) {


/*
 Based on code by Shazron Abdullah Copyright 2012

 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

// This is installed as a <js-module /> so it doesn't have a cordova.define wrapper

var exec = require('cordova/exec');

var Keychain = function(encryptionRequired) {
	this.serviceName = "Keychain";
    this.encryptionRequired = encryptionRequired || 'true';
};

Keychain.prototype.getForKey = function(successCallback, failureCallback, key, servicename)
{
	exec(successCallback, failureCallback, this.serviceName, "getForKey", [key, servicename, this.encryptionRequired]);
};

Keychain.prototype.setForKey = function(successCallback, failureCallback, key, servicename, value)
{
	exec(successCallback, failureCallback, this.serviceName, "setForKey", [key, servicename, value, this.encryptionRequired]);
};

Keychain.prototype.removeForKey = function(successCallback, failureCallback, key, servicename)
{
	exec(successCallback, failureCallback, this.serviceName, "removeForKey", [key, servicename, this.encryptionRequired]);
};

module.exports = Keychain;

});
