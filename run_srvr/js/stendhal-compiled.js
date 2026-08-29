"use strict";
(() => {
  var __create = Object.create;
  var __defProp = Object.defineProperty;
  var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
  var __getOwnPropNames = Object.getOwnPropertyNames;
  var __getProtoOf = Object.getPrototypeOf;
  var __hasOwnProp = Object.prototype.hasOwnProperty;
  var __esm = (fn, res, err) => function __init() {
    if (err) throw err[0];
    try {
      return fn && (res = (0, fn[__getOwnPropNames(fn)[0]])(fn = 0)), res;
    } catch (e) {
      throw err = [e], e;
    }
  };
  var __commonJS = (cb, mod) => function __require() {
    try {
      return mod || (0, cb[__getOwnPropNames(cb)[0]])((mod = { exports: {} }).exports, mod), mod.exports;
    } catch (e) {
      throw mod = 0, e;
    }
  };
  var __export = (target, all) => {
    for (var name in all)
      __defProp(target, name, { get: all[name], enumerable: true });
  };
  var __copyProps = (to, from, except, desc) => {
    if (from && typeof from === "object" || typeof from === "function") {
      for (let key of __getOwnPropNames(from))
        if (!__hasOwnProp.call(to, key) && key !== except)
          __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
    }
    return to;
  };
  var __toESM = (mod, isNodeMode, target) => (target = mod != null ? __create(__getProtoOf(mod)) : {}, __copyProps(
    // If the importer is in node compatibility mode or this is not an ESM
    // file that has been converted to a CommonJS file using a Babel-
    // compatible transform (i.e. "__esModule" has not been set), then set
    // "default" to the CommonJS "module.exports" for node compatibility.
    isNodeMode || !mod || !mod.__esModule ? __defProp(target, "default", { value: mod, enumerable: true }) : target,
    mod
  ));
  var __toCommonJS = (mod) => __copyProps(__defProp({}, "__esModule", { value: true }), mod);

  // ../../../node_modules/marauroa/index.js
  var require_marauroa = __commonJS({
    "../../../node_modules/marauroa/index.js"(exports, module) {
      "use strict";
      var __defProp2 = Object.defineProperty;
      var __getOwnPropDesc2 = Object.getOwnPropertyDescriptor;
      var __getOwnPropNames2 = Object.getOwnPropertyNames;
      var __hasOwnProp2 = Object.prototype.hasOwnProperty;
      var __export2 = (target, all) => {
        for (var name in all)
          __defProp2(target, name, { get: all[name], enumerable: true });
      };
      var __copyProps2 = (to, from, except, desc) => {
        if (from && typeof from === "object" || typeof from === "function") {
          for (let key of __getOwnPropNames2(from))
            if (!__hasOwnProp2.call(to, key) && key !== except)
              __defProp2(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc2(from, key)) || desc.enumerable });
        }
        return to;
      };
      var __toCommonJS2 = (mod) => __copyProps2(__defProp2({}, "__esModule", { value: true }), mod);
      var index_exports = {};
      __export2(index_exports, {
        ClientFramework: () => ClientFramework,
        Deserializer: () => Deserializer3,
        MarauroaUtils: () => MarauroaUtils,
        PerceptionListener: () => PerceptionListener2,
        RPEvent: () => RPEvent21,
        RPEventFactory: () => RPEventFactory,
        RPObject: () => RPObject6,
        RPObjectFactory: () => RPObjectFactory,
        RPSlot: () => RPSlot2,
        RPSlotFactory: () => RPSlotFactory,
        RPZone: () => RPZone4,
        marauroa: () => marauroa64
      });
      module.exports = __toCommonJS2(index_exports);
      var MarauroaUtils = class _MarauroaUtils {
        static isEmpty(obj) {
          for (let i in obj) {
            if (obj.hasOwnProperty(i)) {
              return false;
            }
          }
          return true;
        }
        static isEmptyExceptId(obj) {
          for (let i in obj) {
            if (i !== "id" && obj.hasOwnProperty(i)) {
              return false;
            }
          }
          return true;
        }
        static first(obj) {
          for (var i in obj) {
            if (obj.hasOwnProperty(i)) {
              return obj[i];
            }
          }
        }
        static fromProto(proto, def) {
          let obj;
          if (typeof proto === "function") {
            obj = new proto();
          } else {
            obj = Object.create(proto);
          }
          if (!def) {
            return obj;
          }
          return _MarauroaUtils.merge(obj, def);
        }
        static merge(a, b) {
          for (let key in b) {
            a[key] = b[key];
          }
          return a;
        }
      };
      var MessageDispatcher = class {
        constructor(clientFramework) {
          this.clientFramework = clientFramework;
        }
        dispatchMessage(msg) {
          switch (parseInt(msg.t, 10)) {
            case 14: {
              this.clientFramework.onLoginFailed(msg["reason"], msg["text"]);
              break;
            }
            case 9: {
              this.clientFramework.onAvailableCharacterDetails(msg["characters"]);
              break;
            }
            case 10: {
              console.log("Entering world");
              break;
            }
            case 11: {
              console.log("Character selection rejected");
              this.clientFramework.onChooseCharacterNack();
              break;
            }
            case 13: {
              this.clientFramework.onPreviousLogins(msg["previousLogins"]);
              break;
            }
            case 15: {
              let config2 = {};
              for (let entry of msg["config"]) {
                let pos = entry.indexOf("=");
                config2[entry.substring(0, pos).trim()] = entry.substring(pos + 1).trim();
              }
              this.clientFramework.onLoginRequired(config2);
              break;
            }
            case 19: {
              this.clientFramework.onPerception(msg);
              break;
            }
            case 20: {
              this.clientFramework.onServerInfo(msg["contents"]);
              break;
            }
            case 21: {
              this.clientFramework.onTransfer(msg["contents"]);
              break;
            }
            case 22: {
              this.clientFramework.onTransferREQ(msg["contents"]);
              let contents = {};
              for (let i in msg["contents"]) {
                if (typeof msg["contents"][i]["ack"] != "undefined" && msg["contents"][i]["ack"]) {
                  contents[msg["contents"][i]["name"]] = true;
                } else {
                  contents[msg["contents"][i]["name"]] = false;
                }
              }
              let msg2 = {
                "t": "7",
                "contents": contents
              };
              this.clientFramework.sendMessage(msg2);
              break;
            }
            case 24: {
              this.clientFramework.onCreateAccountAck(msg["username"]);
              break;
            }
            case 25: {
              this.clientFramework.onCreateAccountNack(msg["username"], msg["reason"]);
              break;
            }
            case 27: {
              this.clientFramework.onCreateCharacterAck(msg["charname"], msg["template"]);
              break;
            }
            case 28: {
              this.clientFramework.onCreateCharacterNack(msg["charname"], msg["reason"]);
              break;
            }
            case 35: {
              let msg2 = {
                "t": "36",
                "response": (0, eval)(msg["update"])
              };
              this.clientFramework.sendMessage(msg2);
              break;
            }
            // handle unexpected unknown messages
            default: {
              console.log("Unknown message: " + JSON.stringify(msg));
            }
          }
        }
      };
      var ClientFramework = class {
        clientid = "-1";
        username;
        socket;
        messageDispatcher;
        constructor() {
          this.messageDispatcher = new MessageDispatcher(this);
        }
        /**
         * connect to the server
         *
         * @param host server host name
         * @param port server port number
         */
        connect(host, port, path) {
          var protocol = "ws";
          if (window.location.protocol === "https:") {
            protocol = "wss";
          }
          if (host === null) {
            host = window.location.hostname;
          }
          if (port === null) {
            port = window.location.port;
          }
          if (port != "") {
            port = ":" + port;
          }
          if (path === null) {
            path = "ws/";
          }
          var url = protocol + "://" + host + port + "/" + path;
          let socket = new WebSocket(url);
          socket.addEventListener("message", (e) => this.onMessage(e));
          socket.addEventListener("open", (_e) => {
            setInterval(() => {
              var msg = {
                "t": "8"
              };
              this.sendMessage(msg);
            }, 1e4);
            this.onConnect();
          });
          socket.addEventListener("close", (e) => this.onDisconnect(e.reason, e.code, e.wasClean));
          this.socket = socket;
        }
        onConnect() {
          console.log("connected");
        }
        onDisconnect(reason, code, wasClean) {
          console.log("onDisconnect: " + reason + " code: " + code + " wasClean: " + wasClean);
        }
        onLoginRequired(config2) {
          console.log("Config", config2);
        }
        login(username, password) {
          var msg = {
            "t": "34",
            "u": username,
            "p": password
          };
          this.username = username;
          this.sendMessage(msg);
        }
        onServerInfo(contents) {
        }
        onPreviousLogins(previousLogins) {
          console.log("Previous Logins", previousLogins);
        }
        onLoginFailed(reason, text) {
          console.error("Login failed with reason " + reason + ": " + text);
        }
        onMessage(e) {
          var msg = JSON.parse(e.data);
          if (msg["t"] === "9" || msg["t"] === "15") {
            this.clientid = msg["c"];
          }
          if (typeof msg === "string") {
            console.error("JSON error on message: " + msg);
          } else {
            this.messageDispatcher.dispatchMessage(msg);
          }
        }
        sendMessage(msg) {
          var myMessage = {
            "c": this.clientid,
            "s": "1"
          };
          MarauroaUtils.merge(myMessage, msg);
          this.socket.send(JSON.stringify(myMessage));
        }
        resync() {
        }
        /**
         * After login allows you to choose a character to play
         *
         * @param character
         *            name of the character we want to play with.
         * @return true if choosing character is successful.
         * @throws InvalidVersionException
         *             if we are not using a compatible version
         * @throws TimeoutException
         *             if timeout happens while waiting for the message.
         * @throws BannedAddressException
         */
        chooseCharacter(character) {
          var msg = {
            "t": "1",
            "character": character
          };
          this.sendMessage(msg);
        }
        onChooseCharacterNack() {
          console.error("Server rejected your character.");
        }
        /**
         * Sends a RPAction to server
         *
         * @param action
         *            the action to send to server.
         */
        sendAction(action) {
          var msg = {
            "t": "0",
            "a": action
          };
          this.sendMessage(msg);
        }
        /**
         * Request logout of server
         *
         * @return true if we have successfully logout or false if server rejects to
         *         logout our player and maintain it on game world.
         * @throws InvalidVersionException
         *             if we are not using a compatible version
         * @throws TimeoutException
         *             if timeout happens while waiting for the message.
         * @throws BannedAddressException
         */
        logout() {
          var msg = {
            "t": "5"
          };
          this.sendMessage(msg);
        }
        onLogoutOutAck() {
          console.log("Server accepted logout request");
        }
        onLogoutOutNack() {
          console.log("Server rejected logout request");
        }
        /**
         * Disconnect the socket and finish the network communications.
         */
        close() {
          this.socket?.close();
        }
        /**
         * It is called when a perception arrives so you can choose how to apply the
         * perception.
         *
         * @param perceptionMessage
         *            the perception message itself.
         */
        onPerception(perceptionMessage) {
          marauroa64.perceptionHandler.apply(perceptionMessage);
        }
        /**
         * is called before a content transfer is started.
         *
         * <code> items </code> contains a list of names and timestamp.
         * That information can be used to decide if a transfer from server is needed.
         * By setting attribute ack to true in a TransferContent it will be acknowledged.
         * All acknowledges items in the returned List, will be transfered by server.
         *
         * @param items
         *            in this list by default all items.ack attributes are set to false;
         * @return the list of approved and rejected items.
         */
        onTransferREQ(items) {
          console.log("onTransferREQ: ", items);
        }
        /**
         * It is called when we get a transfer of content
         *
         * @param items
         *            the transfered items.
         */
        onTransfer(items) {
          console.log("onTransfer: ", items);
        }
        /**
         * It is called when we get the list of characters
         *
         * @param characters
         *            the characters we have available at this account.
         */
        onAvailableCharacterDetails(characters) {
          console.log("onAvailableCharacterDetails: ", characters);
          if (MarauroaUtils.isEmpty(characters)) {
            console.log("No character found, creating a character with the username (redefine onAvailableCharacterDetails to prevent this).");
            this.createCharacter(this.username, {});
            return;
          }
          for (var key in characters) {
            if (characters.hasOwnProperty(key)) {
              this.chooseCharacter(key);
            }
          }
        }
        createAccount(username, password, email) {
          var msg = {
            "t": "23",
            "u": username,
            "p": password,
            "e": email
          };
          this.sendMessage(msg);
        }
        onCreateAccountAck(username) {
          console.log('Account "' + username + '" created successfully');
        }
        onCreateAccountNack(username, reason) {
          console.log('Creating Account "' + username + '" failed: ', reason);
          alert(reason.text);
        }
        createCharacter(charname, template) {
          var msg = {
            "t": "26",
            "charname": charname,
            "template": template
          };
          this.sendMessage(msg);
        }
        onCreateCharacterAck(charname, template) {
          console.log('Character "' + charname + '" created successfully', template);
        }
        onCreateCharacterNack(charname, reason) {
          console.log('Creating Character "' + charname + '" failed: ', reason);
          alert(reason.text);
        }
      };
      var RPObject6 = class {
        onEvent(e) {
          var event2 = marauroa64.rpeventFactory.create(e["c"]);
          for (var i in e["a"]) {
            if (e["a"].hasOwnProperty(i)) {
              event2[i] = e["a"][i];
            }
            event2["_rpclass"] = e["c"];
          }
          for (var slot in e["s"]) {
            if (e["s"].hasOwnProperty(slot)) {
              event2[slot] = e["s"][slot];
            }
          }
          event2.execute(this);
        }
        set(key, value) {
          this[key] = value;
        }
        setMapEntry(map, key, value) {
          this[map][key] = value;
        }
        unset(key) {
          delete this[key];
        }
        unsetMapEntry(map, key) {
          delete this[map][key];
        }
        destroy(_parent) {
        }
        createSlot(name) {
          var slot = marauroa64.rpslotFactory.create(name);
          slot._parent = this;
          return slot;
        }
        init() {
        }
        /**
         * Retrieves object width.
         */
        getWidth() {
          return this["width"] || 1;
        }
        /**
         * Retrieves object height.
         */
        getHeight() {
          return this["height"] || 1;
        }
        /**
         * Checks if this object is nested.
         */
        isContained() {
          return this._parent !== void 0;
        }
        /**
         * Retrieves the parent object.
         */
        getContainer() {
          return this._parent;
        }
      };
      var rpClasses = /* @__PURE__ */ new Map();
      rpClasses.set("_default", RPObject6);
      var RPObjectFactory = class {
        register(name, clazz) {
          rpClasses.set(name, clazz);
        }
        create(rpclass) {
          let ctor = rpClasses.get(rpclass);
          if (!ctor) {
            console.log("Unknown RPClass " + rpclass);
            ctor = rpClasses.get("_default");
          }
          let temp = new ctor();
          temp._rpclass = rpclass;
          temp.init();
          return temp;
        }
      };
      var RPSlot2 = class {
        _objects = [];
        add(value) {
          if (value && value["id"]) {
            this._objects.push(value);
          }
        }
        get(key) {
          let idx = this.getIndex(key);
          if (idx > -1) {
            return this._objects[idx];
          }
          return void 0;
        }
        getByIndex(idx) {
          return this._objects[idx];
        }
        count() {
          return this._objects.length;
        }
        getIndex(key) {
          let i;
          let c = this._objects.length;
          for (i = 0; i < c; i++) {
            if (this._objects[i]["id"] === key) {
              return i;
            }
          }
          return -1;
        }
        del(key) {
          let idx = this.getIndex(key);
          if (idx > -1) {
            this._objects.splice(idx, 1);
          }
        }
        first() {
          if (this._objects.length > 0) {
            return this._objects[0];
          }
          return void 0;
        }
      };
      var rpSlots = /* @__PURE__ */ new Map();
      rpSlots.set("_default", RPSlot2);
      var RPSlotFactory = class {
        register(name, clazz) {
          rpSlots.set(name, clazz);
        }
        create(name) {
          let ctor = rpSlots.get(name);
          if (!ctor) {
            ctor = rpSlots.get("_default");
          }
          let slot = new ctor();
          slot._name = name;
          slot._objects = [];
          return slot;
        }
      };
      var RPEvent21 = class {
      };
      var DefaultRPEvent = class extends RPEvent21 {
        execute(rpobject) {
          if (marauroa64.debug.unknownEvents) {
            console.log("Unhandled event: ", this, " on ", rpobject);
          }
        }
      };
      var rpEvents = /* @__PURE__ */ new Map();
      rpEvents.set("_default", DefaultRPEvent);
      var RPEventFactory = class {
        register(name, clazz) {
          rpEvents.set(name, clazz);
        }
        create(rpclass) {
          let ctor = rpEvents.get(rpclass);
          if (!ctor) {
            ctor = rpEvents.get("_default");
          }
          return new ctor();
        }
      };
      var PerceptionListener2 = class {
        /**
         * onAdded is called when an object is added to the world for first time or
         * after a sync perception. Return true to stop further processing.
         *
         * @param object
         *            the added object.
         * @return true to stop further processing
         */
        onAdded(_object) {
          return false;
        }
        /**
         * onModifiedAdded is called when an object is modified by adding or
         * changing one of its attributes. Return true to stop further processing.
         * Note that the method is called *before* modifying the object.
         *
         * @param object
         *            the original object
         * @param changes
         *            the added and modified changes.
         * @return true to stop further processing
         */
        onModifiedAdded(_object, _changes) {
          return false;
        }
        /**
         * onModifiedDeleted is called each time the object has one of its
         * attributes removed. Return true to stop further processing. Note that the
         * method is called *before* modifying the object.
         *
         * @param object
         *            the original object
         * @param changes
         *            the deleted attributes.
         * @return true to stop further processing
         */
        onModifiedDeleted(object, changes) {
          return false;
        }
        /**
         * onDeleted is called when an object is removed of the world Return true to
         * stop further processing.
         *
         * @param object
         *            the original object
         * @return true to stop further processing
         */
        onDeleted(object) {
          return false;
        }
        /**
         * onMyRPObject is called when our rpobject avatar is processed. Return true
         * to stop further processing.
         *
         * @param added
         *            the added and modified attributes and slots
         * @param deleted
         *            the deleted attributes
         * @return true to stop further processing
         */
        onMyRPObject(added, deleted) {
          return false;
        }
        /**
         * onClear is called when the whole world is going to be cleared. It happens
         * on sync perceptions Return true to stop further processing.
         *
         * @return true to stop further processing
         */
        onClear() {
          return false;
        }
        /**
         * onPerceptionBegin is called when the perception is going to be applied
         *
         * @param type
         *            type of the perception: SYNC or DELTA
         * @param timestamp
         *            the timestamp of the perception
         */
        onPerceptionBegin(type, timestamp) {
        }
        /**
         * onPerceptionBegin is called when the perception has been applied
         *
         * @param type
         *            type of the perception: SYNC or DELTA
         * @param timestamp
         *            the timestamp of the perception
         */
        onPerceptionEnd(type, timestamp) {
        }
        /**
         * onException is called when an exception happens
         *
         * @param exception
         *            the exception that happened.
         * @param perception
         *            the message that causes the problem
         */
        onException(exception, perception) {
          console.error(exception, perception);
        }
      };
      var PerceptionHandler = class {
        /**
         * Apply a perception.
         *
         * @param msg
         *            the perception msg
         */
        apply(msg) {
          marauroa64.perceptionListener.onPerceptionBegin(msg["sync"], msg["s"]);
          if (msg["sync"]) {
            if (!marauroa64.perceptionListener.onClear()) {
              marauroa64.currentZone.clear();
              marauroa64.currentZoneName = msg["zoneid"];
            }
          }
          marauroa64.perceptionHandler.applyPerceptionAddedRPObjects(msg);
          marauroa64.perceptionHandler.applyPerceptionModifiedRPObjects(msg);
          marauroa64.perceptionHandler.applyPerceptionDeletedRPObjects(msg);
          marauroa64.perceptionHandler.applyPerceptionMyRPObject(msg);
          marauroa64.perceptionListener.onPerceptionEnd(msg["sync"], msg["s"]);
        }
        /**
         * This method applies perceptions addedto the Map<RPObject::ID,RPObject>
         * passed as argument. It clears the map if this is a sync perception
         *
         * @param msg
         *            the perception message
         */
        applyPerceptionAddedRPObjects(msg) {
          if (msg["aO"]) {
            for (var i in msg["aO"]) {
              if (msg["aO"].hasOwnProperty(i)) {
                if (!marauroa64.perceptionListener.onAdded(msg["aO"][i])) {
                  var o = marauroa64.rpobjectFactory.create(msg["aO"][i]["c"]);
                  marauroa64.perceptionHandler.addChanges(o, msg["aO"][i]);
                  marauroa64.currentZone[msg["aO"][i]["a"]["id"]] = o;
                }
              }
            }
          }
        }
        /**
         * This method applys perceptions deleted to the Map<RPObject::ID,RPObject>
         * passed as argument.
         *
         * @param msg
         *            the perception message
         */
        applyPerceptionDeletedRPObjects(msg) {
          if (msg["dO"]) {
            for (var i in msg["dO"]) {
              if (msg["dO"].hasOwnProperty(i)) {
                var tmp = msg["dO"][i]["a"]["id"];
                if (!marauroa64.perceptionListener.onDeleted(msg["dO"][i])) {
                  marauroa64.currentZone[tmp].destroy(marauroa64.currentZone);
                  delete marauroa64.currentZone[tmp];
                }
              }
            }
          }
        }
        /**
         * This method applies perceptions modified added and modified deleted to the
         * Map<RPObject.ID,RPObject> passed as argument.
         *
         * @param msg
         *            the perception message
         */
        applyPerceptionModifiedRPObjects(msg) {
          if (msg["dA"]) {
            for (var i in msg["dA"]) {
              if (msg["dA"].hasOwnProperty(i)) {
                if (typeof marauroa64.currentZone[msg["dA"][i]["a"]["id"]] !== "undefined") {
                  var o = marauroa64.currentZone[msg["dA"][i]["a"]["id"]];
                  if (!marauroa64.perceptionListener.onModifiedDeleted(msg["dA"][i])) {
                    marauroa64.perceptionHandler.deleteChanges(o, msg["dA"][i]);
                  }
                }
              }
            }
          }
          if (msg["aA"]) {
            for (var i in msg["aA"]) {
              if (msg["aA"].hasOwnProperty(i)) {
                if (typeof marauroa64.currentZone[msg["aA"][i]["a"]["id"]] !== "undefined") {
                  var o = marauroa64.currentZone[msg["aA"][i]["a"]["id"]];
                  if (!marauroa64.perceptionListener.onModifiedAdded(o, msg["aA"][i])) {
                    marauroa64.perceptionHandler.addChanges(o, msg["aA"][i]);
                  }
                }
              }
            }
          }
        }
        /**
         * This method applys perceptions for our RPObject to the Map<RPObject::ID,RPObject>
         * passed as argument.
         *
         * @param msg
         *            the perception message
         */
        applyPerceptionMyRPObject(msg) {
          if (!marauroa64.perceptionListener.onMyRPObject(msg["aM"], msg["dM"])) {
            var id;
            if (typeof msg["aM"] !== "undefined") {
              id = msg["aM"]["a"]["id"];
            }
            if (typeof msg["dM"] !== "undefined") {
              id = msg["dM"]["a"]["id"];
            }
            if (typeof id === "undefined") {
              return;
            }
            marauroa64.perceptionHandler.addMyRPObjectToWorldIfPrivate(id, msg["aM"]);
            var o = marauroa64.currentZone[id];
            marauroa64.me = o;
            marauroa64.perceptionHandler.deleteChanges(o, msg["dM"]);
            marauroa64.perceptionHandler.addChanges(o, msg["aM"]);
          }
        }
        /**
         * adds our RPObject to the world in case it was not already added by the public perception.
         *
         * @param added added changes of my object
         */
        addMyRPObjectToWorldIfPrivate(id, added) {
          if (typeof marauroa64.currentZone[id] !== "undefined") {
            return;
          }
          if (!marauroa64.perceptionListener.onAdded(added)) {
            if (typeof added === "undefined") {
              marauroa64.currentZone[id] = {};
              return;
            }
            var o = marauroa64.rpobjectFactory.create(added["c"]);
            marauroa64.currentZone[id] = o;
            marauroa64.perceptionHandler.addChanges(o, added);
          }
        }
        deleteChanges(object, diff) {
          if (typeof diff === "undefined") {
            return;
          }
          if (typeof diff["a"] !== "undefined") {
            for (var i in diff["a"]) {
              if (diff["a"].hasOwnProperty(i) && i !== "id" && i !== "zoneid") {
                object.unset(i);
              }
            }
          }
          if (typeof diff["s"] !== "undefined") {
            for (var i in diff["s"]) {
              if (!diff["s"].hasOwnProperty(i)) {
                continue;
              }
              if (MarauroaUtils.isEmpty(diff["s"][i])) {
                object.del(diff["s"][i]);
              } else {
                for (var j in diff["s"][i]) {
                  if (diff["s"][i].hasOwnProperty(j)) {
                    object[i].del(diff["s"][i][j]["a"].id);
                  }
                }
              }
            }
          }
          if (typeof diff["m"] !== "undefined") {
            for (var i in diff["m"]) {
              if (!diff["m"].hasOwnProperty(i)) {
                continue;
              }
              if (MarauroaUtils.isEmpty(diff["m"][i]["a"])) {
                object.unset(diff["m"][i]);
              } else {
                for (var j in diff["m"][i]["a"]) {
                  if (diff["m"][i]["a"].hasOwnProperty(j)) {
                    object.unsetMapEntry(i, j);
                  }
                }
              }
            }
          }
        }
        addChanges(object, diff) {
          if (typeof diff === "undefined") {
            return;
          }
          object["_rpclass"] = diff["c"];
          for (var i in diff["a"]) {
            if (diff["a"].hasOwnProperty(i)) {
              if (typeof object.set === "undefined") {
                console.warn("Object missing set(key, value)-function", object, diff["a"]);
                object[i] = diff["a"][i];
              } else {
                object.set(i, diff["a"][i]);
              }
            }
          }
          if (typeof diff["m"] !== "undefined") {
            for (var i in diff["m"]) {
              if (diff["m"].hasOwnProperty(i)) {
                if (typeof object[i] === "undefined") {
                  object[i] = {};
                }
                for (var j in diff["m"][i]["a"]) {
                  if (j !== "zoneid" && j !== "id" && diff["m"][i]["a"].hasOwnProperty(j)) {
                    object.setMapEntry(i, j, diff["m"][i]["a"][j]);
                  }
                }
              }
            }
          }
          if (typeof diff["s"] !== "undefined") {
            for (var i in diff["s"]) {
              if (diff["s"].hasOwnProperty(i)) {
                if (typeof object[i] === "undefined") {
                  object[i] = object.createSlot(i);
                }
                for (var j in diff["s"][i]) {
                  if (diff["s"][i].hasOwnProperty(j)) {
                    var id = diff["s"][i][j]["a"].id;
                    if (typeof object[i].get(id) === "undefined") {
                      var newObject = marauroa64.rpobjectFactory.create(diff["s"][i][j]["c"]);
                      newObject._parent = object[i];
                      newObject["id"] = id;
                      object[i].add(newObject);
                    }
                    marauroa64.perceptionHandler.addChanges(object[i].get(id), diff["s"][i][j]);
                  }
                }
              }
            }
          }
          if (typeof diff["e"] !== "undefined" && typeof object.onEvent !== "undefined") {
            for (var i in diff["e"]) {
              if (diff["e"].hasOwnProperty(i)) {
                object.onEvent(diff["e"][i]);
              }
            }
          }
        }
      };
      var RPZone4 = class {
        clear() {
          for (let i in marauroa64.currentZone) {
            if (marauroa64.currentZone.hasOwnProperty(i) && typeof marauroa64.currentZone[i] !== "function") {
              marauroa64.currentZone[i].destroy(marauroa64.currentZone);
              delete marauroa64.currentZone[i];
            }
          }
        }
      };
      var Marauroa = class {
        debug = {
          messages: false,
          unknownEvents: true
        };
        clientFramework = new ClientFramework();
        currentZoneName;
        currentZone = new RPZone4();
        perceptionHandler = new PerceptionHandler();
        perceptionListener = new PerceptionListener2();
        rpobjectFactory = new RPObjectFactory();
        rpslotFactory = new RPSlotFactory();
        rpeventFactory = new RPEventFactory();
        me;
        // TODO: This should be optional "me?"
      };
      var marauroa64 = new Marauroa();
      var Deserializer3 = class _Deserializer {
        constructor(buffer) {
          this.buffer = buffer;
          this.offset = 0;
          this.view = new DataView(buffer);
        }
        offset = 0;
        view;
        /**
         * This method reads a byte from the Serializer
         *
         * @return the byte serialized
         */
        readByte() {
          this.offset++;
          return this.view.getUint8(this.offset - 1);
        }
        /**
         * This method reads a short from the Serializer
         *
         * @return the short serialized
         */
        readShort() {
          this.offset += 2;
          return this.view.getInt16(this.offset - 2, true);
        }
        /**
         * This method reads a int from the Serializer
         *
         * @return the int serialized
         */
        readInt() {
          this.offset += 4;
          return this.view.getInt32(this.offset - 4, true);
        }
        /**
         * This method reads a byte array from the Serializer
         *
         * @return the byte array serialized
         */
        readByteArray() {
          let size = this.view.getUint32(this.offset, true);
          this.offset += size + 4;
          return new DataView(this.buffer, this.offset - size, size);
        }
        /**
         * This method reads a byte array of a maximum length of 255 entries
         *
         * @return the byte array serialized
         */
        read255LongByteArray() {
          let size = this.view.getUint8(this.offset);
          this.offset += size + 1;
          return new DataView(this.buffer, this.offset - size, size);
        }
        /**
         * This method reads a byte array of a maximum length of 65536 entries
         *
         * @return the byte array serialized
         */
        read65536LongByteArray() {
          let size = this.view.getUint16(this.offset, true);
          this.offset += size + 1;
          return new DataView(this.buffer, this.offset - size, size);
        }
        /**
         * This method reads a float from the Serializer
         *
         * @return the float serialized
         */
        readFloat() {
          this.offset += 4;
          return this.view.getFloat32(this.offset - 4, true);
        }
        /**
         * This method reads a String from the Serializer
         *
         * @return the String serialized
         */
        readString() {
          return new TextDecoder("utf-8").decode(this.readByteArray());
        }
        /**
         * This method reads a short string (whose size is smaller than 255 bytes
         * long)
         *
         * @return the String serialized
         */
        read255LongString() {
          return new TextDecoder("utf-8").decode(this.read255LongByteArray());
        }
        /**
         * This method reads a long string (whose size is smaller than 65536 bytes
         * long)
         *
         * @return the String serialized
         */
        read65536LongString() {
          return new TextDecoder("utf-8").decode(this.read65536LongByteArray());
        }
        /**
         * This method reads a String array from the Serializer
         *
         * @return the String array serialized
         */
        readStringArray() {
          let size = this.readInt();
          let res = [];
          for (let i = 0; i < size; i++) {
            res.push(this.readString());
          }
          return res;
        }
        /**
         * reads an Attributes-object from the stream
         *
         * @param obj object to read into
         * @return obj
         */
        readAttributes(obj) {
          this.readString();
          let size = this.readInt();
          for (let i = 0; i < size; i++) {
            let code = this.readShort();
            if (code !== -1) {
              console.error("RPClass not supported, yet.");
              return obj;
            }
            let key = this.readString();
            let value = this.readString();
            obj[key] = value;
          }
          return obj;
        }
        readRPObject(obj) {
          this.readAttributes(obj);
          return obj;
        }
        static binaryStringToUint(binary) {
          let len = binary.length;
          let bytes = new Uint8Array(len);
          for (let i = 0; i < len; i++) {
            bytes[i] = binary.charCodeAt(i);
          }
          return bytes.buffer;
        }
        /**
         * created a deserializer from a deflated data stream that was encoded using base64.
         *
         * @param base64 base64 string
         * @return Deserializer
         */
        static fromDeflatedBase64(base64) {
          let d = window.atob(base64);
          let buffer = _Deserializer.binaryStringToUint(d.substring(2, d.length - 4));
          let inflate = new window["Zlib"]["RawInflate"](buffer);
          let data = inflate["decompress"]();
          return new _Deserializer(data.buffer);
        }
        /**
         * created a deserializer from a data stream that was encoded using base64.
         *
         * @param base64 base64 string
         * @return Deserializer
         */
        static fromBase64(base64) {
          return _Deserializer.fromBinaryString(atob(base64));
        }
        /**
         * created a deserializer from a data stream
         *
         * @param binary binary string
         * @return Deserializer
         */
        static fromBinaryString(binary) {
          let len = binary.length;
          let bytes = new Uint8Array(len);
          for (let i = 0; i < len; i++) {
            bytes[i] = binary.charCodeAt(i);
          }
          return new _Deserializer(bytes.buffer);
        }
      };
    }
  });

  // ../../../node_modules/marauroa/inflate.js
  var require_inflate = __commonJS({
    "../../../node_modules/marauroa/inflate.js"(exports) {
      (function() {
        "use strict";
        var k = void 0, aa = this;
        function r(c, d) {
          var a = c.split("."), b = aa;
          !(a[0] in b) && b.execScript && b.execScript("var " + a[0]);
          for (var e; a.length && (e = a.shift()); ) !a.length && d !== k ? b[e] = d : b = b[e] ? b[e] : b[e] = {};
        }
        ;
        var t = "undefined" !== typeof Uint8Array && "undefined" !== typeof Uint16Array && "undefined" !== typeof Uint32Array && "undefined" !== typeof DataView;
        function u(c) {
          var d = c.length, a = 0, b = Number.POSITIVE_INFINITY, e, f, g, h, l, n, m, p, s, x;
          for (p = 0; p < d; ++p) c[p] > a && (a = c[p]), c[p] < b && (b = c[p]);
          e = 1 << a;
          f = new (t ? Uint32Array : Array)(e);
          g = 1;
          h = 0;
          for (l = 2; g <= a; ) {
            for (p = 0; p < d; ++p) if (c[p] === g) {
              n = 0;
              m = h;
              for (s = 0; s < g; ++s) n = n << 1 | m & 1, m >>= 1;
              x = g << 16 | p;
              for (s = n; s < e; s += l) f[s] = x;
              ++h;
            }
            ++g;
            h <<= 1;
            l <<= 1;
          }
          return [f, a, b];
        }
        ;
        function w(c, d) {
          this.g = [];
          this.h = 32768;
          this.c = this.f = this.d = this.k = 0;
          this.input = t ? new Uint8Array(c) : c;
          this.l = false;
          this.i = y;
          this.p = false;
          if (d || !(d = {})) d.index && (this.d = d.index), d.bufferSize && (this.h = d.bufferSize), d.bufferType && (this.i = d.bufferType), d.resize && (this.p = d.resize);
          switch (this.i) {
            case A:
              this.a = 32768;
              this.b = new (t ? Uint8Array : Array)(32768 + this.h + 258);
              break;
            case y:
              this.a = 0;
              this.b = new (t ? Uint8Array : Array)(this.h);
              this.e = this.u;
              this.m = this.r;
              this.j = this.s;
              break;
            default:
              throw Error("invalid inflate mode");
          }
        }
        var A = 0, y = 1;
        w.prototype.t = function() {
          for (; !this.l; ) {
            var c = B(this, 3);
            c & 1 && (this.l = true);
            c >>>= 1;
            switch (c) {
              case 0:
                var d = this.input, a = this.d, b = this.b, e = this.a, f = d.length, g = k, h = k, l = b.length, n = k;
                this.c = this.f = 0;
                if (a + 1 >= f) throw Error("invalid uncompressed block header: LEN");
                g = d[a++] | d[a++] << 8;
                if (a + 1 >= f) throw Error("invalid uncompressed block header: NLEN");
                h = d[a++] | d[a++] << 8;
                if (g === ~h) throw Error("invalid uncompressed block header: length verify");
                if (a + g > d.length) throw Error("input buffer is broken");
                switch (this.i) {
                  case A:
                    for (; e + g > b.length; ) {
                      n = l - e;
                      g -= n;
                      if (t) b.set(d.subarray(a, a + n), e), e += n, a += n;
                      else for (; n--; ) b[e++] = d[a++];
                      this.a = e;
                      b = this.e();
                      e = this.a;
                    }
                    break;
                  case y:
                    for (; e + g > b.length; ) b = this.e({ o: 2 });
                    break;
                  default:
                    throw Error("invalid inflate mode");
                }
                if (t) b.set(d.subarray(a, a + g), e), e += g, a += g;
                else for (; g--; ) b[e++] = d[a++];
                this.d = a;
                this.a = e;
                this.b = b;
                break;
              case 1:
                this.j(ba, ca);
                break;
              case 2:
                for (var m = B(this, 5) + 257, p = B(this, 5) + 1, s = B(this, 4) + 4, x = new (t ? Uint8Array : Array)(C.length), Q = k, R = k, S = k, v = k, M = k, F = k, z = k, q = k, T = k, q = 0; q < s; ++q) x[C[q]] = B(this, 3);
                if (!t) {
                  q = s;
                  for (s = x.length; q < s; ++q) x[C[q]] = 0;
                }
                Q = u(x);
                v = new (t ? Uint8Array : Array)(m + p);
                q = 0;
                for (T = m + p; q < T; ) switch (M = D(this, Q), M) {
                  case 16:
                    for (z = 3 + B(this, 2); z--; ) v[q++] = F;
                    break;
                  case 17:
                    for (z = 3 + B(this, 3); z--; ) v[q++] = 0;
                    F = 0;
                    break;
                  case 18:
                    for (z = 11 + B(this, 7); z--; ) v[q++] = 0;
                    F = 0;
                    break;
                  default:
                    F = v[q++] = M;
                }
                R = t ? u(v.subarray(0, m)) : u(v.slice(0, m));
                S = t ? u(v.subarray(m)) : u(v.slice(m));
                this.j(R, S);
                break;
              default:
                throw Error("unknown BTYPE: " + c);
            }
          }
          return this.m();
        };
        var E = [16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15], C = t ? new Uint16Array(E) : E, G = [3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19, 23, 27, 31, 35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195, 227, 258, 258, 258], H = t ? new Uint16Array(G) : G, I = [0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0, 0, 0], J = t ? new Uint8Array(I) : I, K = [1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577], L = t ? new Uint16Array(K) : K, N = [
          0,
          0,
          0,
          0,
          1,
          1,
          2,
          2,
          3,
          3,
          4,
          4,
          5,
          5,
          6,
          6,
          7,
          7,
          8,
          8,
          9,
          9,
          10,
          10,
          11,
          11,
          12,
          12,
          13,
          13
        ], O = t ? new Uint8Array(N) : N, P = new (t ? Uint8Array : Array)(288), U, da;
        U = 0;
        for (da = P.length; U < da; ++U) P[U] = 143 >= U ? 8 : 255 >= U ? 9 : 279 >= U ? 7 : 8;
        var ba = u(P), V = new (t ? Uint8Array : Array)(30), W, ea;
        W = 0;
        for (ea = V.length; W < ea; ++W) V[W] = 5;
        var ca = u(V);
        function B(c, d) {
          for (var a = c.f, b = c.c, e = c.input, f = c.d, g = e.length, h; b < d; ) {
            if (f >= g) throw Error("input buffer is broken");
            a |= e[f++] << b;
            b += 8;
          }
          h = a & (1 << d) - 1;
          c.f = a >>> d;
          c.c = b - d;
          c.d = f;
          return h;
        }
        function D(c, d) {
          for (var a = c.f, b = c.c, e = c.input, f = c.d, g = e.length, h = d[0], l = d[1], n, m; b < l && !(f >= g); ) a |= e[f++] << b, b += 8;
          n = h[a & (1 << l) - 1];
          m = n >>> 16;
          if (m > b) throw Error("invalid code length: " + m);
          c.f = a >> m;
          c.c = b - m;
          c.d = f;
          return n & 65535;
        }
        w.prototype.j = function(c, d) {
          var a = this.b, b = this.a;
          this.n = c;
          for (var e = a.length - 258, f, g, h, l; 256 !== (f = D(this, c)); ) if (256 > f) b >= e && (this.a = b, a = this.e(), b = this.a), a[b++] = f;
          else {
            g = f - 257;
            l = H[g];
            0 < J[g] && (l += B(this, J[g]));
            f = D(this, d);
            h = L[f];
            0 < O[f] && (h += B(this, O[f]));
            b >= e && (this.a = b, a = this.e(), b = this.a);
            for (; l--; ) a[b] = a[b++ - h];
          }
          for (; 8 <= this.c; ) this.c -= 8, this.d--;
          this.a = b;
        };
        w.prototype.s = function(c, d) {
          var a = this.b, b = this.a;
          this.n = c;
          for (var e = a.length, f, g, h, l; 256 !== (f = D(this, c)); ) if (256 > f) b >= e && (a = this.e(), e = a.length), a[b++] = f;
          else {
            g = f - 257;
            l = H[g];
            0 < J[g] && (l += B(this, J[g]));
            f = D(this, d);
            h = L[f];
            0 < O[f] && (h += B(this, O[f]));
            b + l > e && (a = this.e(), e = a.length);
            for (; l--; ) a[b] = a[b++ - h];
          }
          for (; 8 <= this.c; ) this.c -= 8, this.d--;
          this.a = b;
        };
        w.prototype.e = function() {
          var c = new (t ? Uint8Array : Array)(this.a - 32768), d = this.a - 32768, a, b, e = this.b;
          if (t) c.set(e.subarray(32768, c.length));
          else {
            a = 0;
            for (b = c.length; a < b; ++a) c[a] = e[a + 32768];
          }
          this.g.push(c);
          this.k += c.length;
          if (t) e.set(e.subarray(d, d + 32768));
          else for (a = 0; 32768 > a; ++a) e[a] = e[d + a];
          this.a = 32768;
          return e;
        };
        w.prototype.u = function(c) {
          var d, a = this.input.length / this.d + 1 | 0, b, e, f, g = this.input, h = this.b;
          c && ("number" === typeof c.o && (a = c.o), "number" === typeof c.q && (a += c.q));
          2 > a ? (b = (g.length - this.d) / this.n[2], f = 258 * (b / 2) | 0, e = f < h.length ? h.length + f : h.length << 1) : e = h.length * a;
          t ? (d = new Uint8Array(e), d.set(h)) : d = h;
          return this.b = d;
        };
        w.prototype.m = function() {
          var c = 0, d = this.b, a = this.g, b, e = new (t ? Uint8Array : Array)(this.k + (this.a - 32768)), f, g, h, l;
          if (0 === a.length) return t ? this.b.subarray(32768, this.a) : this.b.slice(32768, this.a);
          f = 0;
          for (g = a.length; f < g; ++f) {
            b = a[f];
            h = 0;
            for (l = b.length; h < l; ++h) e[c++] = b[h];
          }
          f = 32768;
          for (g = this.a; f < g; ++f) e[c++] = d[f];
          this.g = [];
          return this.buffer = e;
        };
        w.prototype.r = function() {
          var c, d = this.a;
          t ? this.p ? (c = new Uint8Array(d), c.set(this.b.subarray(0, d))) : c = this.b.subarray(0, d) : (this.b.length > d && (this.b.length = d), c = this.b);
          return this.buffer = c;
        };
        r("Zlib.RawInflate", w);
        r("Zlib.RawInflate.prototype.decompress", w.prototype.t);
        var X = { ADAPTIVE: y, BLOCK: A }, Y, Z, $, fa;
        if (Object.keys) Y = Object.keys(X);
        else for (Z in Y = [], $ = 0, X) Y[$++] = Z;
        $ = 0;
        for (fa = Y.length; $ < fa; ++$) Z = Y[$], r("Zlib.RawInflate.BufferType." + Z, X[Z]);
      }).call(exports);
    }
  });

  // ../../../node_modules/marauroa/build.js
  var build_exports = {};
  __export(build_exports, {
    version: () => version
  });
  function version(marauroa64, stendhal2) {
    stendhal2.data = stendhal2.data || {};
    stendhal2.data.build = {
      version: "1.49.5",
      build: "2026-08-28 23:30:04",
      dist: "webclient"
    };
  }
  var init_build = __esm({
    "../../../node_modules/marauroa/build.js"() {
    }
  });

  // data/sha3.js
  var require_sha3 = __commonJS({
    "data/sha3.js"(exports) {
      "use strict";
      (function(L) {
        function u(d, b, h) {
          var c = 0, a = [], l = 0, e, m, k, f, n, g2, q, v, A = false, p2 = [], u2 = [], w, z2 = false, x = false, t = -1;
          h = h || {};
          e = h.encoding || "UTF8";
          w = h.numRounds || 1;
          if (w !== parseInt(w, 10) || 1 > w) throw Error("numRounds must a integer >= 1");
          if (0 === d.lastIndexOf("SHA3-", 0) || 0 === d.lastIndexOf("SHAKE", 0)) {
            var C = 6;
            g2 = B;
            v = function(c2) {
              var a2 = [], e2;
              for (e2 = 0; 5 > e2; e2 += 1) a2[e2] = c2[e2].slice();
              return a2;
            };
            t = 1;
            if ("SHA3-224" === d) n = 1152, f = 224;
            else if ("SHA3-256" === d) n = 1088, f = 256;
            else if ("SHA3-384" === d) n = 832, f = 384;
            else if ("SHA3-512" === d) n = 576, f = 512;
            else if ("SHAKE128" === d) n = 1344, f = -1, C = 31, x = true;
            else if ("SHAKE256" === d) n = 1088, f = -1, C = 31, x = true;
            else throw Error("Chosen SHA variant is not supported");
            q = function(c2, a2, e2, f2, d2) {
              e2 = n;
              var b2 = C, m2, l2 = [], g3 = e2 >>> 5, k2 = 0, h2 = a2 >>> 5;
              for (m2 = 0; m2 < h2 && a2 >= e2; m2 += g3) f2 = B(c2.slice(m2, m2 + g3), f2), a2 -= e2;
              c2 = c2.slice(m2);
              for (a2 %= e2; c2.length < g3; ) c2.push(0);
              m2 = a2 >>> 3;
              c2[m2 >> 2] ^= b2 << m2 % 4 * 8;
              c2[g3 - 1] ^= 2147483648;
              for (f2 = B(c2, f2); 32 * l2.length < d2; ) {
                c2 = f2[k2 % 5][k2 / 5 | 0];
                l2.push(c2.b);
                if (32 * l2.length >= d2) break;
                l2.push(c2.a);
                k2 += 1;
                0 === 64 * k2 % e2 && B(null, f2);
              }
              return l2;
            };
          } else throw Error("Chosen SHA variant is not supported");
          k = D(b, e, t);
          m = y(d);
          this.setHMACKey = function(a2, b2, l2) {
            var k2;
            if (true === A) throw Error("HMAC key already set");
            if (true === z2) throw Error("Cannot set HMAC key after calling update");
            if (true === x) throw Error("SHAKE is not supported for HMAC");
            e = (l2 || {}).encoding || "UTF8";
            b2 = D(b2, e, t)(a2);
            a2 = b2.binLen;
            b2 = b2.value;
            k2 = n >>> 3;
            l2 = k2 / 4 - 1;
            if (k2 < a2 / 8) {
              for (b2 = q(b2, a2, 0, y(d), f); b2.length <= l2; ) b2.push(0);
              b2[l2] &= 4294967040;
            } else if (k2 > a2 / 8) {
              for (; b2.length <= l2; ) b2.push(0);
              b2[l2] &= 4294967040;
            }
            for (a2 = 0; a2 <= l2; a2 += 1) p2[a2] = b2[a2] ^ 909522486, u2[a2] = b2[a2] ^ 1549556828;
            m = g2(p2, m);
            c = n;
            A = true;
          };
          this.update = function(e2) {
            var b2, f2, d2, h2 = 0, q2 = n >>> 5;
            b2 = k(e2, a, l);
            e2 = b2.binLen;
            f2 = b2.value;
            b2 = e2 >>> 5;
            for (d2 = 0; d2 < b2; d2 += q2) h2 + n <= e2 && (m = g2(f2.slice(d2, d2 + q2), m), h2 += n);
            c += h2;
            a = f2.slice(h2 >>> 5);
            l = e2 % n;
            z2 = true;
          };
          this.getHash = function(e2, b2) {
            var k2, g3, h2, n2;
            if (true === A) throw Error("Cannot call getHash after setting HMAC key");
            h2 = E(b2);
            if (true === x) {
              if (-1 === h2.shakeLen) throw Error("shakeLen must be specified in options");
              f = h2.shakeLen;
            }
            switch (e2) {
              case "HEX":
                k2 = function(a2) {
                  return F(a2, f, t, h2);
                };
                break;
              case "B64":
                k2 = function(a2) {
                  return G(a2, f, t, h2);
                };
                break;
              case "BYTES":
                k2 = function(a2) {
                  return H(a2, f, t);
                };
                break;
              case "ARRAYBUFFER":
                try {
                  g3 = new ArrayBuffer(0);
                } catch (r) {
                  throw Error("ARRAYBUFFER not supported by this environment");
                }
                k2 = function(a2) {
                  return I(a2, f, t);
                };
                break;
              default:
                throw Error("format must be HEX, B64, BYTES, or ARRAYBUFFER");
            }
            n2 = q(a.slice(), l, c, v(m), f);
            for (g3 = 1; g3 < w; g3 += 1) true === x && 0 !== f % 32 && (n2[n2.length - 1] &= 16777215 >>> 24 - f % 32), n2 = q(n2, f, 0, y(d), f);
            return k2(n2);
          };
          this.getHMAC = function(e2, b2) {
            var k2, h2, p3, w2;
            if (false === A) throw Error("Cannot call getHMAC without first setting HMAC key");
            p3 = E(b2);
            switch (e2) {
              case "HEX":
                k2 = function(a2) {
                  return F(a2, f, t, p3);
                };
                break;
              case "B64":
                k2 = function(a2) {
                  return G(a2, f, t, p3);
                };
                break;
              case "BYTES":
                k2 = function(a2) {
                  return H(a2, f, t);
                };
                break;
              case "ARRAYBUFFER":
                try {
                  k2 = new ArrayBuffer(0);
                } catch (M) {
                  throw Error("ARRAYBUFFER not supported by this environment");
                }
                k2 = function(a2) {
                  return I(a2, f, t);
                };
                break;
              default:
                throw Error("outputFormat must be HEX, B64, BYTES, or ARRAYBUFFER");
            }
            h2 = q(a.slice(), l, c, v(m), f);
            w2 = g2(u2, y(d));
            w2 = q(h2, f, n, w2, f);
            return k2(w2);
          };
        }
        function g(d, b) {
          this.a = d;
          this.b = b;
        }
        function F(d, b, h, c) {
          var a = "";
          b /= 8;
          var l, e, m;
          m = -1 === h ? 3 : 0;
          for (l = 0; l < b; l += 1) e = d[l >>> 2] >>> 8 * (m + l % 4 * h), a += "0123456789abcdef".charAt(e >>> 4 & 15) + "0123456789abcdef".charAt(e & 15);
          return c.outputUpper ? a.toUpperCase() : a;
        }
        function G(d, b, h, c) {
          var a = "", l = b / 8, e, m, k, f;
          f = -1 === h ? 3 : 0;
          for (e = 0; e < l; e += 3) for (m = e + 1 < l ? d[e + 1 >>> 2] : 0, k = e + 2 < l ? d[e + 2 >>> 2] : 0, k = (d[e >>> 2] >>> 8 * (f + e % 4 * h) & 255) << 16 | (m >>> 8 * (f + (e + 1) % 4 * h) & 255) << 8 | k >>> 8 * (f + (e + 2) % 4 * h) & 255, m = 0; 4 > m; m += 1) 8 * e + 6 * m <= b ? a += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(k >>> 6 * (3 - m) & 63) : a += c.b64Pad;
          return a;
        }
        function H(d, b, h) {
          var c = "";
          b /= 8;
          var a, l, e;
          e = -1 === h ? 3 : 0;
          for (a = 0; a < b; a += 1) l = d[a >>> 2] >>> 8 * (e + a % 4 * h) & 255, c += String.fromCharCode(l);
          return c;
        }
        function I(d, b, h) {
          b /= 8;
          var c, a = new ArrayBuffer(b), l, e;
          e = new Uint8Array(a);
          l = -1 === h ? 3 : 0;
          for (c = 0; c < b; c += 1) e[c] = d[c >>> 2] >>> 8 * (l + c % 4 * h) & 255;
          return a;
        }
        function E(d) {
          var b = { outputUpper: false, b64Pad: "=", shakeLen: -1 };
          d = d || {};
          b.outputUpper = d.outputUpper || false;
          true === d.hasOwnProperty("b64Pad") && (b.b64Pad = d.b64Pad);
          if (true === d.hasOwnProperty("shakeLen")) {
            if (0 !== d.shakeLen % 8) throw Error("shakeLen must be a multiple of 8");
            b.shakeLen = d.shakeLen;
          }
          if ("boolean" !== typeof b.outputUpper) throw Error("Invalid outputUpper formatting option");
          if ("string" !== typeof b.b64Pad) throw Error("Invalid b64Pad formatting option");
          return b;
        }
        function D(d, b, h) {
          switch (b) {
            case "UTF8":
            case "UTF16BE":
            case "UTF16LE":
              break;
            default:
              throw Error("encoding must be UTF8, UTF16BE, or UTF16LE");
          }
          switch (d) {
            case "HEX":
              d = function(c, a, b2) {
                var e = c.length, d2, k, f, g2, r, q;
                if (0 !== e % 2) throw Error("String of HEX type must be in byte increments");
                a = a || [0];
                b2 = b2 || 0;
                r = b2 >>> 3;
                q = -1 === h ? 3 : 0;
                for (d2 = 0; d2 < e; d2 += 2) {
                  k = parseInt(c.substr(d2, 2), 16);
                  if (isNaN(k)) throw Error("String of HEX type contains invalid characters");
                  g2 = (d2 >>> 1) + r;
                  for (f = g2 >>> 2; a.length <= f; ) a.push(0);
                  a[f] |= k << 8 * (q + g2 % 4 * h);
                }
                return { value: a, binLen: 4 * e + b2 };
              };
              break;
            case "TEXT":
              d = function(c, a, d2) {
                var e, m, k = 0, f, g2, r, q, v, p2;
                a = a || [0];
                d2 = d2 || 0;
                r = d2 >>> 3;
                if ("UTF8" === b) for (p2 = -1 === h ? 3 : 0, f = 0; f < c.length; f += 1) for (e = c.charCodeAt(f), m = [], 128 > e ? m.push(e) : 2048 > e ? (m.push(192 | e >>> 6), m.push(128 | e & 63)) : 55296 > e || 57344 <= e ? m.push(224 | e >>> 12, 128 | e >>> 6 & 63, 128 | e & 63) : (f += 1, e = 65536 + ((e & 1023) << 10 | c.charCodeAt(f) & 1023), m.push(240 | e >>> 18, 128 | e >>> 12 & 63, 128 | e >>> 6 & 63, 128 | e & 63)), g2 = 0; g2 < m.length; g2 += 1) {
                  v = k + r;
                  for (q = v >>> 2; a.length <= q; ) a.push(0);
                  a[q] |= m[g2] << 8 * (p2 + v % 4 * h);
                  k += 1;
                }
                else if ("UTF16BE" === b || "UTF16LE" === b) for (p2 = -1 === h ? 2 : 0, f = 0; f < c.length; f += 1) {
                  e = c.charCodeAt(f);
                  "UTF16LE" === b && (g2 = e & 255, e = g2 << 8 | e >>> 8);
                  v = k + r;
                  for (q = v >>> 2; a.length <= q; ) a.push(0);
                  a[q] |= e << 8 * (p2 + v % 4 * h);
                  k += 2;
                }
                return { value: a, binLen: 8 * k + d2 };
              };
              break;
            case "B64":
              d = function(c, a, b2) {
                var e = 0, d2, g2, f, n, r, q, p2, u2;
                if (-1 === c.search(/^[a-zA-Z0-9=+\/]+$/)) throw Error("Invalid character in base-64 string");
                g2 = c.indexOf("=");
                c = c.replace(/\=/g, "");
                if (-1 !== g2 && g2 < c.length) throw Error("Invalid '=' found in base-64 string");
                a = a || [0];
                b2 = b2 || 0;
                q = b2 >>> 3;
                u2 = -1 === h ? 3 : 0;
                for (g2 = 0; g2 < c.length; g2 += 4) {
                  r = c.substr(g2, 4);
                  for (f = n = 0; f < r.length; f += 1) d2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(r[f]), n |= d2 << 18 - 6 * f;
                  for (f = 0; f < r.length - 1; f += 1) {
                    p2 = e + q;
                    for (d2 = p2 >>> 2; a.length <= d2; ) a.push(0);
                    a[d2] |= (n >>> 16 - 8 * f & 255) << 8 * (u2 + p2 % 4 * h);
                    e += 1;
                  }
                }
                return { value: a, binLen: 8 * e + b2 };
              };
              break;
            case "BYTES":
              d = function(c, a, b2) {
                var e, d2, g2, f, n, p2;
                a = a || [0];
                b2 = b2 || 0;
                g2 = b2 >>> 3;
                p2 = -1 === h ? 3 : 0;
                for (d2 = 0; d2 < c.length; d2 += 1) e = c.charCodeAt(d2), n = d2 + g2, f = n >>> 2, a.length <= f && a.push(0), a[f] |= e << 8 * (p2 + n % 4 * h);
                return { value: a, binLen: 8 * c.length + b2 };
              };
              break;
            case "ARRAYBUFFER":
              try {
                d = new ArrayBuffer(0);
              } catch (c) {
                throw Error("ARRAYBUFFER not supported by this environment");
              }
              d = function(c, a, b2) {
                var d2, g2, k, f, n, p2;
                a = a || [0];
                b2 = b2 || 0;
                g2 = b2 >>> 3;
                n = -1 === h ? 3 : 0;
                p2 = new Uint8Array(c);
                for (d2 = 0; d2 < c.byteLength; d2 += 1) f = d2 + g2, k = f >>> 2, a.length <= k && a.push(0), a[k] |= p2[d2] << 8 * (n + f % 4 * h);
                return { value: a, binLen: 8 * c.byteLength + b2 };
              };
              break;
            default:
              throw Error("format must be HEX, TEXT, B64, BYTES, or ARRAYBUFFER");
          }
          return d;
        }
        function z(d, b) {
          return 32 < b ? (b -= 32, new g(d.b << b | d.a >>> 32 - b, d.a << b | d.b >>> 32 - b)) : 0 !== b ? new g(d.a << b | d.b >>> 32 - b, d.b << b | d.a >>> 32 - b) : d;
        }
        function p(d, b) {
          return new g(d.a ^ b.a, d.b ^ b.b);
        }
        function y(d) {
          var b = [];
          if (0 === d.lastIndexOf("SHA3-", 0) || 0 === d.lastIndexOf("SHAKE", 0)) for (d = 0; 5 > d; d += 1) b[d] = [
            new g(0, 0),
            new g(0, 0),
            new g(0, 0),
            new g(0, 0),
            new g(0, 0)
          ];
          else throw Error("No SHA variants supported");
          return b;
        }
        function B(d, b) {
          var h, c, a, l, e = [], m = [];
          if (null !== d) for (c = 0; c < d.length; c += 2) b[(c >>> 1) % 5][(c >>> 1) / 5 | 0] = p(b[(c >>> 1) % 5][(c >>> 1) / 5 | 0], new g(d[c + 1], d[c]));
          for (h = 0; 24 > h; h += 1) {
            l = y("SHA3-");
            for (c = 0; 5 > c; c += 1) {
              a = b[c][0];
              var k = b[c][1], f = b[c][2], n = b[c][3], r = b[c][4];
              e[c] = new g(a.a ^ k.a ^ f.a ^ n.a ^ r.a, a.b ^ k.b ^ f.b ^ n.b ^ r.b);
            }
            for (c = 0; 5 > c; c += 1) m[c] = p(e[(c + 4) % 5], z(e[(c + 1) % 5], 1));
            for (c = 0; 5 > c; c += 1) for (a = 0; 5 > a; a += 1) b[c][a] = p(b[c][a], m[c]);
            for (c = 0; 5 > c; c += 1) for (a = 0; 5 > a; a += 1) l[a][(2 * c + 3 * a) % 5] = z(b[c][a], J[c][a]);
            for (c = 0; 5 > c; c += 1) for (a = 0; 5 > a; a += 1) b[c][a] = p(l[c][a], new g(~l[(c + 1) % 5][a].a & l[(c + 2) % 5][a].a, ~l[(c + 1) % 5][a].b & l[(c + 2) % 5][a].b));
            b[0][0] = p(b[0][0], K[h]);
          }
          return b;
        }
        var J, K;
        K = [new g(0, 1), new g(0, 32898), new g(2147483648, 32906), new g(2147483648, 2147516416), new g(0, 32907), new g(0, 2147483649), new g(2147483648, 2147516545), new g(2147483648, 32777), new g(0, 138), new g(0, 136), new g(0, 2147516425), new g(0, 2147483658), new g(0, 2147516555), new g(
          2147483648,
          139
        ), new g(2147483648, 32905), new g(2147483648, 32771), new g(2147483648, 32770), new g(2147483648, 128), new g(0, 32778), new g(2147483648, 2147483658), new g(2147483648, 2147516545), new g(2147483648, 32896), new g(0, 2147483649), new g(2147483648, 2147516424)];
        J = [[0, 36, 3, 41, 18], [1, 44, 10, 45, 2], [62, 6, 43, 15, 61], [28, 55, 25, 21, 56], [27, 20, 39, 8, 14]];
        L.jsSHA = u;
      })(exports);
    }
  });

  // main.ts
  var import_marauroa87 = __toESM(require_marauroa());

  // stendhal.ts
  var stendhal = class {
    static config;
    static ui;
    static sound;
    static zone;
    static data;
    static session;
    static main;
    static actions;
    static players;
  };

  // Client.ts
  var import_marauroa86 = __toESM(require_marauroa());

  // ui/UI.ts
  var import_marauroa54 = __toESM(require_marauroa());

  // ui/UIComponentEnum.ts
  var UIComponentEnum = class {
    // this should be an enum, but after TypeScript and closure compiler are done, all
    // enum members resolve to undefined. That is both in JavaScript and TypeScript code
    // Panel
    static TopPanel = 0;
    static LeftPanel = 1;
    static RightPanel = 2;
    static BottomPanel = 3;
    // Components
    static Bag = 101;
    static BuddyList = 102;
    static ChatInput = 103;
    static ChatLog = 104;
    static GroupPanel = 105;
    static PlayerEquipment = 106;
    static Keyring = 107;
    static MiniMap = 108;
    static ZoneInfo = 109;
    static PlayerStats = 110;
    static SocialPanel = 111;
    static StatusesList = 112;
    // Dialog
    static TradeDialog = 201;
    static TravelLogDialog = 202;
    static OutfitDialog = 203;
    // Quick menu
    static QMChat = 301;
    static QMJoystick = 302;
    static QMPathFinding = 303;
    static QMSound = 304;
  };

  // util/Direction.ts
  var Direction = class _Direction {
    constructor(val, dx, dy) {
      this.val = val;
      this.dx = dx;
      this.dy = dy;
    }
    val;
    dx;
    dy;
    static STOP = new _Direction(0, 0, 0);
    static UP = new _Direction(1, 0, -1);
    static RIGHT = new _Direction(2, 1, 0);
    static DOWN = new _Direction(3, 0, 1);
    static LEFT = new _Direction(4, -1, 0);
    static VALUES = [_Direction.STOP, _Direction.UP, _Direction.RIGHT, _Direction.DOWN, _Direction.LEFT];
  };

  // ui/joystick/DPadButton.ts
  var DPadButton = class {
    /** Direction this button represents. */
    direction;
    /** Image element that is added to DOM. */
    element;
    /** Property denoting button's state of engagement. */
    engaged = false;
    /**
     * Creates a new button.
     *
     * @param dir {util.Direction.Direction}
     *   Direction to be represented.
     */
    constructor(dir) {
      this.direction = dir;
      this.element = new Image();
      this.element.classList.add("joystick-button", "hidden");
      this.element.draggable = false;
      this.element.style.position = "absolute";
      this.element.style.touchAction = "none";
      let rotation = 0;
      switch (dir) {
        case Direction.DOWN:
          rotation = 180;
          break;
        case Direction.LEFT:
          rotation = -90;
          break;
        case Direction.RIGHT:
          rotation = 90;
          break;
      }
      if (rotation) {
        this.element.style["transform"] = "rotate(" + rotation + "deg)";
      }
      this.element.addEventListener("pointerdown", (e) => {
        if (e.target) {
          e.target.releasePointerCapture(e.pointerId);
        }
        DirectionPad.get().onButtonEngaged(e, this);
      });
      this.element.addEventListener("touchend", (e) => {
        DirectionPad.get().onButtonDisengaged(e);
      });
      this.element.addEventListener("pointerenter", (e) => {
        DirectionPad.get().onDragWhileEngaged(this);
      });
      this.setEngaged(false);
    }
    /**
     * Updates button positioning.
     *
     * @param radius {number}
     *   Overall radius of direction pad.
     * @param centerX {number}
     *   Overall center position of direction pad on X axis.
     * @param centerY {number}
     *   Overall center position of direction pad on Y axis.
     */
    updateInternal(radius, centerX, centerY) {
      const halfWidth = Math.floor(this.element.width / 2);
      const halfHeight = Math.floor(this.element.height / 2);
      switch (this.direction) {
        case Direction.UP:
          this.element.style.left = centerX - halfWidth + "px";
          this.element.style.top = centerY - radius + "px";
          break;
        case Direction.DOWN:
          this.element.style.left = centerX - halfWidth + "px";
          this.element.style.top = centerY + radius - this.element.height + "px";
          break;
        case Direction.LEFT:
          this.element.style.left = centerX - radius + "px";
          this.element.style.top = centerY - halfHeight + "px";
          break;
        case Direction.RIGHT:
          this.element.style.left = centerX + radius - this.element.width + "px";
          this.element.style.top = centerY - halfHeight + "px";
          break;
      }
    }
    /**
     * Updates button positioning.
     *
     * @param radius {number}
     *   Overall radius of direction pad.
     * @param centerX {number}
     *   Overall center position of direction pad on X axis.
     * @param centerY {number}
     *   Overall center position of direction pad on Y axis.
     */
    update(radius, centerX, centerY) {
      if (this.element.complete) {
        this.updateInternal(radius, centerX, centerY);
        return;
      }
      this.element.onload = () => {
        this.element.onload = null;
        this.updateInternal(radius, centerX, centerY);
      };
    }
    /**
     * Sets engagement property and updates displayed image.
     *
     * @param engaged {boolean}
     *   New state of engagement.
     */
    setEngaged(engaged) {
      this.engaged = engaged;
      this.element.src = DirectionPad.getResource(engaged ? "dpad_button_active" : "dpad_button");
    }
    /**
     * Called when button engagement starts and updates image.
     */
    onEngaged() {
      if (this.engaged) {
        return;
      }
      this.setEngaged(true);
    }
    /**
     * Called when button engagement ends and updates image.
     */
    onDisengaged() {
      if (!this.engaged) {
        return;
      }
      this.setEngaged(false);
    }
  };

  // ui/joystick/JoystickImpl.ts
  var import_marauroa = __toESM(require_marauroa());

  // data/Paths.ts
  var Paths = class _Paths {
    static data = _Paths.extractPath("data-data-path");
    static font = _Paths.data + "/font";
    static gui = _Paths.data + "/gui";
    static music = _Paths.data + "/music";
    static sounds = _Paths.data + "/sounds";
    static sprites = _Paths.data + "/sprites";
    static weather = _Paths.sprites + "/weather";
    static achievements = _Paths.sprites + "/achievements";
    static maps = _Paths.data + "/maps";
    static tileset = _Paths.maps + "/tileset";
    static parallax = _Paths.maps + "/parallax";
    static ws = _Paths.extractPath("data-ws");
    /**
     * Extracts the path information from DOM.
     *
     * @return {string}
     *   Path determining data root.
     */
    static extractPath(ref) {
      let path = document.getElementsByTagName("html")[0].getAttribute(ref);
      if (!path || !path.startsWith("/")) {
        throw new Error("Path reference " + ref + " is not a relative path.");
      }
      return path;
    }
    /**
     * Static members & methods only.
     */
    constructor() {
    }
  };

  // ui/joystick/JoystickImpl.ts
  var JoystickImpl = class _JoystickImpl {
    /** Most recently executed direction change. */
    direction = Direction.STOP;
    /** Pixel radius joystick elements should abide by. */
    radius = 0;
    /** Property denoting joystick is in an engaged state. */
    engaged = false;
    /** ID used to veto buffered time before sending stop event to server. */
    stopTimeoutId = 0;
    /**
     * Determines centering position on the horizontal axis.
     *
     * @return {number}
     *   Joystick center.
     */
    static getCenterX() {
      return stendhal.config.getInt("joystick.center.x", 224);
    }
    /**
     * Determines centering position on the vertical axis.
     *
     * @return {number}
     *   Joystick center.
     */
    static getCenterY() {
      return stendhal.config.getInt("joystick.center.y", 384);
    }
    /**
     * Retrieves joystick image resource path.
     *
     * @param basename {string}
     *   Base filename of image resource.
     * @return {string}
     *   Path to image.
     */
    static getResource(basename) {
      return Paths.gui + "/joystick/" + basename + ".png";
    }
    /**
     * Checks if event can be associated with a joystick action.
     *
     * @param e {Event}
     *   Event to be checked.
     * @return {boolean}
     *   `true` if event can be applied to joystick.
     */
    static checkActionEvent(e) {
      if (e instanceof MouseEvent && e.button == 0) {
        return true;
      }
      return e instanceof TouchEvent;
    }
    /**
     * Called when joystick should be set to its default idle state.
     */
    reset() {
      this.engaged = false;
    }
    /**
     * Called when joystick engagement starts.
     *
     * @param e {Event}
     *   Event to be validated.
     * @return {boolean}
     *   `true` if engagement state set.
     */
    onEngaged(e) {
      if (!_JoystickImpl.checkActionEvent(e)) {
        return false;
      }
      this.engaged = true;
      return this.engaged;
    }
    /**
     * Called when joystick engagement ends and character movement should stop.
     *
     * @param e {Event}
     *   Event to be validated.
     * @return {boolean}
     *   `true` if engagement state unset.
     */
    onDisengaged(e) {
      if (!_JoystickImpl.checkActionEvent(e)) {
        return false;
      }
      this.reset();
      return !this.engaged;
    }
    /**
     * Checks joystick's state of engagement.
     *
     * @return {boolean}
     *   `true` if considered to be engaged.
     */
    isEngaged() {
      return this.engaged;
    }
    /**
     * Checks status of all elements.
     *
     * @return {boolean}
     *   `true` if all image elements are loaded.
     */
    isReady() {
      for (const el of this.getElements()) {
        if (!el.complete) {
          return false;
        }
      }
      return true;
    }
    /**
     * Initializes joystick positioning and makes visible if ready.
     */
    onReady() {
      this.update();
      this.reset();
      if (this.isInUse()) {
        const container = this.getContainer();
        for (let idx = 0; idx < container.children.length; idx++) {
          container.children[idx].classList.remove("hidden");
        }
      }
    }
    /**
     * Adds elements associated with joystick to DOM.
     */
    addToDOM() {
      this.removeFromDOM();
      const container = this.getContainer();
      for (const el of this.getElements()) {
        if (!container.contains(el)) {
          container.appendChild(el);
        }
      }
      if (this.isReady()) {
        this.onReady();
      }
    }
    /**
     * Removes elements associated with joystick from DOM.
     */
    removeFromDOM() {
      const container = this.getContainer();
      for (const el of this.getElements()) {
        if (container.contains(el)) {
          container.removeChild(el);
          el.classList.add("hidden");
        }
      }
    }
    /**
     * Retrieves the parent container where joystick elements are added as children.
     *
     * @return {HTMLDivElement}
     *   Containing div element.
     */
    getContainer() {
      return document.getElementById("joystick-container");
    }
    /**
     * Checks if client is currently using this joystick.
     */
    isInUse() {
      return SoftwareJoystickController.get().getCurrent() === this;
    }
    /**
     * Called when user's character direction should be updated.
     *
     * @param dir {util.Direction}
     *   New direction for character to move or stop.
     */
    onDirectionChange(dir) {
      if (!ui.isDisplayReady() || !import_marauroa.marauroa.me) {
        console.debug("not executing direction change before user ready");
        return;
      }
      this.direction = dir;
      if (this.stopTimeoutId) {
        window.clearTimeout(this.stopTimeoutId);
        this.stopTimeoutId = 0;
      }
      import_marauroa.marauroa.me.setDirection(dir, true);
    }
    /**
     * Called when the user's character should stop moving.
     *
     * Executes a timer buffer before sending stop action to server to allow for subsequent facing
     * direction changes. If the user's facing direction changes before the timer expires it is
     * vetoed and the stop event is not sent.
     */
    queueStop() {
      this.direction = Direction.STOP;
      this.stopTimeoutId = window.setTimeout(() => {
        import_marauroa.marauroa.me.stop();
        this.stopTimeoutId = 0;
      }, 300);
    }
  };

  // ui/joystick/DirectionPad.ts
  var DirectionPad = class _DirectionPad extends JoystickImpl {
    /** Button components associated with this direction pad. */
    buttons;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_DirectionPad.instance) {
        _DirectionPad.instance = new _DirectionPad();
      }
      return _DirectionPad.instance;
    }
    /**
     * Hidden singleton constructor.
     *
     * Creates the HTML elements but does not add them to DOM.
     */
    constructor() {
      super();
      const tmp = new Image();
      tmp.onload = () => {
        this.radius = tmp.width * 1.25;
        this.onReady();
      };
      tmp.src = _DirectionPad.getResource("dpad_button");
      this.buttons = [
        new DPadButton(Direction.UP),
        new DPadButton(Direction.DOWN),
        new DPadButton(Direction.LEFT),
        new DPadButton(Direction.RIGHT)
      ];
    }
    /**
     * Retrieves an array of the elements associated with this direction pad.
     *
     * @return {HTMLImageElement[]}
     *   Image elements representing buttons.
     */
    getElements() {
      const elements = [];
      for (const button of this.buttons) {
        elements.push(button.element);
      }
      return elements;
    }
    /**
     * Updates buttons positioning based on joystick's configured center and radius.
     */
    update() {
      const centerX = _DirectionPad.getCenterX();
      const centerY = _DirectionPad.getCenterY();
      for (const button of this.buttons) {
        button.update(this.radius, centerX, centerY);
      }
    }
    /**
     * Sets all buttons to represent a disengaged state and queues event to stop movement.
     */
    reset() {
      this.disengageAll();
      if (this.engaged) {
        this.queueStop();
      }
      super.reset();
    }
    /**
     * Sets all buttons to represent a disengaged state.
     */
    disengageAll() {
      for (const button of this.buttons) {
        button.onDisengaged();
      }
    }
    /**
     * Called when a button is engaged to determine direction of movement.
     *
     * @param e {Event}
     *   Event to be validated.
     * @param button {ui.joystick.DPadButton.DPadButton}
     *   Button component that was engaged.
     */
    onButtonEngaged(e, button) {
      if (!this.onEngaged(e)) {
        return;
      }
      if (button.direction != this.direction) {
        this.onDirectionChange(button.direction);
      }
      button.onEngaged();
    }
    /**
     * Called when a button is disengaged to stop movement.
     *
     * @param e {Event}
     *   Event to be validated.
     */
    onButtonDisengaged(e) {
      this.onDisengaged(e);
    }
    /**
     * Called when drag gesture is detected to update movement and buttons.
     *
     * This is made public so that `ui.joystick.DPadButton.DPadButton` can access it.
     *
     * @param button {ui.joystick.DPadButton.DPadButton}
     *   Button component over which the drag event was detected.
     */
    onDragWhileEngaged(button) {
      if (!this.engaged) {
        return;
      }
      if (button.direction != this.direction) {
        this.disengageAll();
        button.onEngaged();
        this.onDirectionChange(button.direction);
      }
    }
  };

  // util/Point.ts
  var Point = class {
    /**
     * Creates a new Point.
     *
     * @param x {number}
     *   Coordinate on X axis.
     * @param y {number}
     *   Coordinate on Y axis.
     */
    constructor(x, y) {
      this.x = x;
      this.y = y;
    }
    x;
    y;
    /**
     * Converts to JSON formatted string.
     */
    toString() {
      return JSON.stringify({ x: this.x, y: this.y });
    }
  };

  // util/MathUtil.ts
  var MathUtil = class _MathUtil {
    /** Epsilon value used for coordinate change checks. */
    static EPSILON = 1e-3;
    /**
     * Compares to floating point values.
     *
     * @param d1
     *            first value
     * @param d2
     *            second value
     * @param diff
     *            acceptable diff
     * @return true if they are within diff
     */
    static compareDouble(d1, d2, diff = _MathUtil.EPSILON) {
      return Math.abs(d1 - d2) < diff;
    }
    /**
     * Converts radians to angle of degrees.
     *
     * @param rad {number}
     *   Radians.
     * @return {number}
     *   Angle of degrees.
     */
    static radToDeg(rad) {
      return rad * 180 / Math.PI;
    }
    /**
     * Converts angle of degrees to radians.
     *
     * @param angle {number}
     *   Angle of degrees.
     * @return {number}
     *   Radians.
     */
    static degToRad(angle) {
      return angle * Math.PI / 180;
    }
    static pointToRad(x, y) {
      if (x instanceof Point) {
        y = x.y;
        x = x.x;
      }
      return Math.atan2(y, x);
    }
    static pointToDeg(x, y) {
      if (x instanceof Point) {
        y = x.y;
        x = x.x;
      }
      return _MathUtil.radToDeg(_MathUtil.pointToRad(x, y));
    }
    /**
     * Normalizes number value to positive angle of degrees.
     *
     * @param n {number}
     *   Value to be normalized.
     * @return {number}
     *   Angle of degrees.
     */
    static normDeg(n) {
      n = Number((n % 360).toFixed(2)) || 0;
      return Math.abs(n < 0 ? n + 360 : n);
    }
    /**
     * Converts a number value to hex string.
     *
     * @param n {number}
     *   Number value to be converted.
     * @param pad {boolean}
     *   Left pad with zeros for up to six characters (default: `true`).
     * @param slice {boolean}
     *   If `true`, strip any characters preceding last six (default: `false`).
     * @return {string}
     *   Hexadecimal string representation (excluding "#" prefix).
     */
    static toHex(n, pad = true, slice = false) {
      let h = (n >>> 0).toString(16).toUpperCase();
      if (h.length < 6 && pad) {
        h = ("000000" + h).slice(-6);
      }
      return slice ? h.slice(-6) : h;
    }
  };

  // util/AngleRange.ts
  var AngleRange = class {
    min;
    max;
    /**
     * Creates a new range.
     *
     * @param min {number}
     *   The lowest angle value (inclusive) this range represents.
     * @param max
     *   The highest angle value (inclusive) this range represents.
     */
    constructor(min, max) {
      this.min = MathUtil.normDeg(min);
      this.max = MathUtil.normDeg(max);
    }
    /**
     * Checks if an angle of degrees is within range of angle values.
     *
     * @param angle {number}
     *   Angle to be checked.
     * @return {boolean}
     *   `true` if "angle" is within "min" & "max" properties.
     */
    contains(angle) {
      angle = MathUtil.normDeg(angle);
      if (this.min > this.max) {
        return angle >= this.min || angle <= this.max;
      }
      return angle >= this.min && angle <= this.max;
    }
    /**
     * Retrieves number of degrees between the min & max values.
     */
    getSize() {
      if (this.min > this.max) {
        return 360 - this.min + this.max;
      }
      return this.max - this.min;
    }
  };

  // util/Pair.ts
  var Pair = class {
    constructor(first, second) {
      this.first = first;
      this.second = second;
    }
    first;
    second;
  };

  // ui/HTMLUtil.ts
  var HTMLUtil = class _HTMLUtil {
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    static esc(msg, filter = []) {
      msg = msg.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/\n/g, "<br>");
      for (const tag of filter) {
        msg = msg.replace("&lt;" + tag + "&gt;", "<" + tag + ">").replace("&lt;/" + tag + "&gt;", "</" + tag + ">");
      }
      return msg;
    }
    static niceName(s) {
      if (!s) {
        return "";
      }
      let temp = s.replace(/_/g, " ").trim();
      return temp.charAt(0).toUpperCase() + temp.slice(1);
    }
    /**
     * Retrieves target element from event.
     *
     * @param event {any}
     *   Executed event.
     * @return {EventTarget}
     *   Translated event target.
     */
    static extractTarget(event2) {
      if (event2.changedTouches) {
        const tidx = event2.changedTouches.length - 1;
        if (["touchmove", "touchend"].indexOf(event2.type) > -1) {
          for (const el of document.elementsFromPoint(event2.changedTouches[tidx].pageX, event2.changedTouches[tidx].pageY)) {
            if (!el.classList.contains("notarget")) {
              return el;
            }
          }
        }
        return event2.changedTouches[tidx].target;
      }
      return event2.target;
    }
    /**
     * Normalizes an event object.
     *
     * @param event {any}
     *   Executed event.
     * @return {any}
     *   Normalized event.
     */
    static extractPosition(event2) {
      let pos = event2;
      const target = _HTMLUtil.extractTarget(event2);
      const canvas = target;
      if (event2.changedTouches) {
        const tidx = event2.changedTouches.length - 1;
        pos = {
          pageX: Math.round(event2.changedTouches[tidx].pageX),
          pageY: Math.round(event2.changedTouches[tidx].pageY),
          clientX: Math.round(event2.changedTouches[tidx].clientX),
          clientY: Math.round(event2.changedTouches[tidx].clientY),
          target
        };
        if (["touchmove", "touchend"].indexOf(event2.type) > -1) {
          const rect = canvas.getBoundingClientRect();
          pos.offsetX = pos.pageX - rect.left;
          pos.offsetY = pos.pageY - rect.top;
        } else {
          pos.offsetX = pos.pageX - canvas.offsetLeft;
          pos.offsetY = pos.pageY - canvas.offsetTop;
        }
      }
      pos.canvasRelativeX = Math.round(pos.offsetX * canvas.width / canvas.clientWidth);
      pos.canvasRelativeY = Math.round(pos.offsetY * canvas.height / canvas.clientHeight);
      return pos;
    }
    static formatTallyMarks(line) {
      let tmp = line.split("<tally>");
      const pre = tmp[0];
      tmp = tmp[1].split("</tally>");
      const post = tmp[1];
      const count = parseInt(tmp[0].trim(), 10);
      let tallyString = "";
      if (count > 0) {
        let t = 0;
        for (let idx = 0; idx < count; idx++) {
          t++;
          if (t == 5) {
            tallyString += "5";
            t = 0;
          }
        }
        if (t > 0) {
          tallyString += t;
        }
      } else {
        tallyString = "0";
      }
      const tally = document.createElement("span");
      tally.className = "tally";
      tally.textContent = tallyString;
      return [pre, tally, post];
    }
    /**
     * Extracts slot name from element ID.
     *
     * @param id {string}
     *   Element's ID string.
     * @return {string}
     *   Slot name.
     */
    static parseSlotName(id) {
      if (id.includes("-")) {
        return id.split("-")[0];
      }
      return id.replace(/[0-9]$/, "");
    }
  };

  // ui/joystick/Joystick.ts
  var Joystick = class _Joystick extends JoystickImpl {
    /** Element representing joystick boundaries. */
    outer;
    /** Element used to determine moving direction of character. */
    inner;
    /** Secondary direction of movement. */
    secondaryDir = 0;
    /** Difference in pixels of inner button center relative to outer button center before a move
     *  event will be executed. */
    playThreshold = 24;
    /**
     * Angles representing directions of movement.
     *
     * NOTE: currently supports only 4 sectors but should be updated to represent sectors as laid out
     *       in https://github.com/arianne/stendhal/issues/608#issuecomment-1872485986
     */
    sectors = {
      primary: [
        // NOTE: 0 degrees represents right on joystick plane
        new Pair(Direction.UP.val, new AngleRange(225, 314.9)),
        new Pair(Direction.RIGHT.val, new AngleRange(315, 44.9)),
        new Pair(Direction.DOWN.val, new AngleRange(45, 134.9)),
        new Pair(Direction.LEFT.val, new AngleRange(135, 224.9))
      ],
      secondary: []
    };
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_Joystick.instance) {
        _Joystick.instance = new _Joystick();
      }
      return _Joystick.instance;
    }
    /**
     * Hidden singleton constructor.
     *
     * Creates the HTML elements but does not add them to DOM.
     */
    constructor() {
      super();
      const ssize = this.sectors.primary[0].second.getSize() / 3;
      for (const p of this.sectors.primary) {
        const rangeCC = new AngleRange(p.second.min, p.second.min + ssize);
        const rangeC = new AngleRange(p.second.max - ssize, p.second.max);
        this.sectors.secondary.push(new Pair(p.first + 0.1, rangeCC));
        this.sectors.secondary.push(new Pair(p.first + 0.2, rangeC));
      }
      this.outer = new Image();
      this.inner = new Image();
      for (const jimg of [this.outer, this.inner]) {
        jimg.classList.add("joystick-button", "hidden");
        jimg.style.position = "absolute";
        jimg.draggable = false;
        for (const etype of ["mousedown", "touchstart"]) {
          jimg.addEventListener(etype, (e) => {
            this.onEngaged(e);
          });
        }
        jimg.addEventListener("touchend", (e) => {
          this.onDisengaged(e);
        });
        for (const etype of ["mousemove", "touchmove"]) {
          jimg.addEventListener(etype, (e) => {
            this.onDragWhileEngaged(e);
          });
        }
      }
      this.outer.onload = () => {
        this.outer.onload = null;
        this.radius = Math.floor(this.outer.width / 2);
        this.onOuterReady();
      };
      this.outer.src = _Joystick.getResource("joystick_outer");
    }
    /**
     * Loads inner button image and finalizes initialization.
     */
    onOuterReady() {
      this.inner.onload = () => {
        this.inner.onload = null;
        const rect = this.outer.getBoundingClientRect();
        this.updateInner(rect.left + this.radius, rect.top + this.radius);
        this.onReady();
      };
      this.inner.src = _Joystick.getResource("joystick_inner");
    }
    /**
     * Retrieves an array of the elements associated with this joystick.
     *
     * @return {HTMLImageElement[]}
     *   Image elements representing outer and inner buttons.
     */
    getElements() {
      return [this.outer, this.inner];
    }
    /**
     * Updates joystick positioning based on configured center X & Y coordinates.
     */
    update() {
      const centerX = _Joystick.getCenterX();
      const centerY = _Joystick.getCenterY();
      this.outer.style.left = centerX - this.radius + "px";
      this.outer.style.top = centerY - this.radius + "px";
      this.inner.style.left = centerX - Math.floor(this.inner.width / 2) + "px";
      this.inner.style.top = centerY - Math.floor(this.inner.height / 2) + "px";
    }
    /**
     * Resets inner buttons to disengaged state & stops movement.
     */
    reset() {
      super.reset();
      this.inner.onload = () => {
        this.inner.onload = null;
        const rect = this.outer.getBoundingClientRect();
        this.updateInner(rect.left + this.radius, rect.top + this.radius);
      };
      this.inner.src = _Joystick.getResource("joystick_inner");
      if (this.direction != Direction.STOP) {
        this.onDirectionChange(Direction.STOP);
      }
    }
    /**
     * Updates position of inner button.
     *
     * @param x {number}
     *   Positioning center on X axis.
     * @param y {number}
     *   Positioning center on Y axis.
     */
    updateInner(x, y) {
      const pos = this.keepInside(x, y);
      this.inner.style.left = pos.x - Math.floor(this.inner.width / 2) + "px";
      this.inner.style.top = pos.y - Math.floor(this.inner.height / 2) + "px";
    }
    /**
     * Restricts movement of inner button to joystick radial boundaries.
     *
     * Reference: https://stackoverflow.com/a/8528999/4677917
     *
     * @param x {number}
     *   Absolute coordinate on X axis.
     * @param y {number}
     *   Absolute coordinate on Y axis.
     * @return {util.Point.Point}
     *   Adjusted positioning.
     */
    keepInside(x, y) {
      const bounds = this.outer.getBoundingClientRect();
      const cx = bounds.left + this.radius;
      const cy = bounds.top + this.radius;
      const relX = x - cx;
      const relY = y - cy;
      if (this.outside(relX, relY)) {
        const rad = MathUtil.pointToRad(relX, relY);
        x = Math.cos(rad) * this.radius + cx;
        y = Math.sin(rad) * this.radius + cy;
      }
      return new Point(x, y);
    }
    /**
     * Called when joystick engagement starts in order to determine direction of movement and update
     * inner button.
     *
     * @param e {Event}
     *   Event to be validated.
     */
    onEngaged(e) {
      if (!super.onEngaged(e)) {
        return false;
      }
      this.inner.src = _Joystick.getResource("joystick_inner_active");
      this.onDragWhileEngaged(e);
      return this.engaged;
    }
    /**
     * Called when secondary direction should be updated.
     *
     * @param dir {number}
     *   New secondary direction.
     */
    onSecondaryDirChange(dir) {
      this.secondaryDir = dir;
    }
    /**
     * Called when drag gesture is detected to update movement and buttons.
     *
     * @param e {Event}
     *   Event denoting an update to joystick state.
     */
    onDragWhileEngaged(e) {
      if (!this.engaged) {
        return;
      }
      const pos = HTMLUtil.extractPosition(e);
      this.updateInner(pos.pageX, pos.pageY);
      let sec = this.getPressedDirection();
      const pri = Math.floor(sec);
      if (pri != this.direction.val) {
        if (pri == Direction.STOP.val) {
          this.queueStop();
        } else {
          this.onDirectionChange(Direction.VALUES[pri]);
        }
      }
      sec = sec - pri == 0 ? 0 : sec;
      if (sec != this.secondaryDir) {
        this.onSecondaryDirChange(sec);
      }
    }
    /**
     * Retrieves the interpreted direction of joystick.
     *
     * @return {number}
     *   Direction of movement the joystick's state represents.
     */
    getPressedDirection() {
      const irect = this.inner.getBoundingClientRect();
      const ix = irect.left + Math.floor(this.inner.width / 2) - _Joystick.getCenterX();
      const iy = irect.top + Math.floor(this.inner.height / 2) - _Joystick.getCenterY();
      if (this.inDeadZone(ix, iy)) {
        return Direction.STOP.val;
      }
      const angle = MathUtil.pointToDeg(ix, iy);
      for (const pair of this.sectors.secondary) {
        if (pair.second.contains(angle)) {
          return pair.first;
        }
      }
      for (const pair of this.sectors.primary) {
        if (pair.second.contains(angle)) {
          return pair.first;
        }
      }
      return Direction.STOP.val;
    }
    /**
     * Checks if a position is inside dead zone area.
     *
     * @param relX {number}
     *   Coordinate on X axis relative to joystick center.
     * @param relY {number}
     *   Coordinate on Y axis relative to joystick center.
     * @return {boolean}
     *   `true` if position is not in an area of joystick denoting character movement.
     */
    inDeadZone(relX, relY) {
      return Math.pow(Math.abs(relX), 2) + Math.pow(Math.abs(relY), 2) <= Math.pow(this.playThreshold, 2);
    }
    /**
     * Checks if a position extends beyond radial limit.
     *
     * @param relX {number}
     *   Coordinate on X axis relative to joystick center.
     * @param relY {number}
     *   Coordinate on Y axis relative to joystick center.
     * @return {boolean}
     *   `true` if position is beyond radius.
     */
    outside(relX, relY) {
      return Math.pow(Math.abs(relX), 2) + Math.pow(Math.abs(relY), 2) >= Math.pow(this.radius, 2);
    }
  };

  // util/ConfigManager.ts
  var ConfigManager = class _ConfigManager {
    /**
     * Configuration keys & default values.
     *
     * NOTE: all keys are automatically prefixed with "client." from the set/get methods.
     */
    defaults = {
      "activity-indicator": "true",
      "activity-indicator.animate": "false",
      // TODO: possible change key prefixes pertaining directly to chat panel to "panel.chat."
      "chat.autohide": "false",
      "chat.float": "false",
      "chat.history": "[]",
      "chat.history.index": "0",
      "chat.private.sound": "ui/notify_up",
      "chat.visible": "false",
      "chat-opts.custom": "",
      "click-indicator": "false",
      "effect.blood": "true",
      "effect.entity-overlay": "true",
      "effect.lighting": "true",
      "effect.weather": "true",
      "effect.no-nude": "true",
      "effect.parallax": "true",
      "effect.shadows": "true",
      "emojis.native": "false",
      "font.body": "Carlito",
      "font.chat": "Carlito",
      "font.travel-log": "Black Chancery",
      // NOTE: quick-pickup has precedence over double-click in chests & corpses
      "inventory.double-click": "false",
      "inventory.quick-pickup": "true",
      "joystick": "false",
      "joystick.center.x": "224",
      "joystick.center.y": "384",
      "joystick.style": "joystick",
      "menu.style": "traditional",
      "move.cont": "false",
      "panel.stats.charname": "true",
      "panel.stats.hpbar": "true",
      "pathfinding": "true",
      "pathfinding.minimap": "true",
      "sound": "false",
      "sound.master.volume": "50",
      "sound.ambient.volume": "100",
      "sound.creature.volume": "100",
      "sound.gui.volume": "100",
      "sound.music.volume": "100",
      "sound.sfx.volume": "100",
      "speech.creature": "true",
      "theme": "wood",
      "window.about": "50,20",
      "window.chest": "160,370",
      "window.corpse": "160,370",
      "window.menu": "150,20",
      "window.outfit": "300,50",
      "window.settings": "20,20",
      "window.shortcuts": "20,20",
      "window.trade": "200,100",
      "window.travel-log": "160,50",
      "zoom.touch": "false"
    };
    /**
     * Enumerated options for multiple choice settings.
     */
    opts = {
      "joystick.style": {
        "joystick": "",
        "dpad": "direction pad"
      },
      "menu.style": {
        "traditional": "",
        "floating": ""
      }
    };
    /**
     * Old keys that should be replaced.
     *
     * Format is "old-key": "new-key". If "new-key" is `null` then the old key value is simply removed from storage
     * without updating any new key.
     *
     * NOTE: "new-key" must abide the naming convention by including the "client." prefix. "old-key" must include
     *       prefix only if added after the naming convention was implemented.
     */
    deprecated = {
      "action.inventory.quickpickup": "client.inventory.quick-pickup",
      "action.item.doubleclick": "client.inventory.double-click",
      "chat.custom_keywords": "client.chat-opts.custom",
      "chat.history": "client.chat.history",
      "chat.history.index": "client.chat.history.index",
      "event.pvtmsg.sound": "client.chat.private.sound",
      "gamescreen.blood": "client.effect.blood",
      "gamescreen.lighting": "client.effect.lighting",
      "gamescreen.weather": "client.effect.weather",
      "gamescreen.nonude": "client.effect.no-nude",
      "gamescreen.shadows": "client.effect.shadows",
      "gamescreen.speech.creature": "client.speech.creature",
      "input.click.indicator": "client.click-indicator",
      "input.movecont": "client.move.cont",
      "ui.font.body": "client.font.body",
      "ui.font.chat": "client.font.chat",
      "ui.font.tlog": "client.font.travel-log",
      "ui.joystick": "client.joystick.style",
      "ui.joystick.center.x": "client.joystick.center.x",
      "ui.joystick.center.y": "client.joystick.center.y",
      "ui.sound": "client.sound",
      "ui.sound.master.volume": "client.sound.master.volume",
      "ui.sound.ambient.volume": "client.sound.ambient.volume",
      "ui.sound.creature.volume": "client.sound.creature.volume",
      "ui.sound.gui.volume": "client.sound.gui.volume",
      "ui.sound.music.volume": "client.sound.music.volume",
      "ui.sound.sfx.volume": "client.sound.sfx.volume",
      "ui.stats.charname": "client.panel.stats.charname",
      "ui.stats.hpbar": "client.panel.stats.hpbar",
      "ui.theme": "client.theme",
      "ui.window.chest": "client.window.chest",
      "ui.window.corpse": "client.window.corpse",
      "ui.window.menu": "client.window.menu",
      "ui.window.outfit": "client.window.outfit",
      "ui.window.settings": "client.window.settings",
      "ui.window.shortcuts": "client.window.shortcuts",
      "ui.window.trade": "client.window.trade",
      "ui.window.travellog": "client.window.travel-log"
    };
    /**
     * Themes to apply to panel backgrounds, borders & fonts coloring.
     */
    themes = {
      /**
       * Theme backgrounds indexed by ID.
       */
      map: {
        "aubergine": "panel_aubergine.png",
        "brick": "panel_brick.png",
        "honeycomb": "panel_honeycomb.png",
        "leather": "panel_leather.png",
        "metal": "panel_metal.png",
        "parquet": "panel_wood_parquet.png",
        "stone": "panel_stone.png",
        "tile": "panel_aqua_tile.png",
        "wood": "panel_wood_v.png",
        "wood2": "panel_wood_h.png"
      },
      /**
       * Themes that should use light text.
       */
      dark: {
        "aubergine": true,
        "brick": true,
        "leather": true,
        "metal": true,
        "parquet": true,
        "stone": true,
        "tile": true,
        "wood": true,
        "wood2": true
      }
    };
    /**
     * Available font faces the user can select for different areas of the user interface.
     */
    fonts = {
      "sans-serif": "system default",
      "serif": "system default (serif)",
      "Amaranth": "",
      "Black Chancery": "",
      "Carlito": ""
    };
    /** @deprecated */
    initialized = false;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_ConfigManager.instance) {
        _ConfigManager.instance = new _ConfigManager();
      }
      return _ConfigManager.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
      for (const keyOld in this.deprecated) {
        const keyNew = this.deprecated[keyOld];
        if (keyNew != null) {
          let valueOld = window.localStorage.getItem(keyOld);
          if (window.localStorage.getItem(keyNew) == null && valueOld != null) {
            if (keyOld === "ui.joystick") {
              if (valueOld !== "none" && window.localStorage.getItem("client.joystick") == null) {
                window.localStorage.setItem("client.joystick", "true");
              }
              if (valueOld === "dpad") {
                window.localStorage.setItem(keyNew, valueOld);
              }
            } else {
              window.localStorage.setItem(keyNew, valueOld);
            }
          }
        }
        window.localStorage.removeItem(keyOld);
      }
    }
    /**
     * @deprecated
     *   Not needed anymore as this is now a singleton class & is initialized using the `ConfigManager.get()` method.
     */
    init(args) {
      if (this.initialized) {
        console.warn("tried to re-initialize ConfigManager");
        return;
      }
      this.initialized = true;
    }
    /**
     * Retrieves all available settings keys.
     *
     * NOTE: Keys in the returned list are not prefixed with "client.". Normally it is considered hidden & of no use to
     *       other parts of the client. Thus this method also exludes it. So in cases where methods do need to know of
     *       the prefix, such as the method `ui.SessionManager.init` which manages copies of the keys for the current
     *       session must prepend it itself.
     */
    getKeys() {
      return Object.keys(this.defaults);
    }
    /**
     * Checks if a key has been changed from the default value.
     *
     * @param key {string}
     *   `string` identifier.
     * @return {boolean}
     *   `true` if key is found in local storage.
     */
    isSet(key) {
      return window.localStorage.getItem("client." + key) != null;
    }
    /**
     * Stores a configuration setting.
     *
     * @param key
     *   `string` identifier.
     * @param value
     *   Item to be stored.
     */
    set(key, value) {
      if (typeof value === "object") {
        value = JSON.stringify(value);
      }
      window.localStorage.setItem("client." + key, value);
      stendhal.session.set(key, value);
    }
    /**
     * Retrieves a configuration setting value.
     *
     * @param key {string}
     *   `string` identifier.
     * @return {string|null|undefined}
     *   Stored value identified by key as a `string` or `undefined` if key not found. If value is the string "null" it
     *   is converted to a `null` object.
     */
    get(key) {
      const ret = stendhal.session.get(key) || window.localStorage.getItem("client." + key) || this.defaults[key];
      if (ret === "null") {
        return null;
      }
      return ret;
    }
    /**
     * Retrieves an integer number configuration value.
     *
     * @param key {string}
     *   `string` identifier.
     * @param dval {number}
     *   Default value in case key is not found.
     * @return {number|undefined}
     *   Integer `number` or `undefined`.
     */
    getInt(key, dval) {
      let value = this.getFloat(key);
      if (typeof value === "undefined") {
        if (typeof dval === "undefined") {
          return;
        }
        value = dval;
      }
      return Math.trunc(value);
    }
    /**
     * Retrieves a float number configuration value.
     *
     * @param key {string}
     *   `string` identifier.
     * @param dval {number}
     *   Default value in case key is not found.
     * @return {number|undefined}
     *   Float `number` or `undefined`.
     */
    getFloat(key, dval) {
      let value = Number(this.get(key));
      if (Number.isNaN(value)) {
        if (typeof dval === "undefined") {
          return;
        }
        value = dval;
      }
      return value;
    }
    /**
     * Retrieves a boolean configuration value.
     *
     * @param key {string}
     *   `string` identifier.
     * @return {boolean}
     *   `true` if value is (case-insensitive) string "true", otherwise `false` for any other string value or if key not
     *   found.
     */
    getBoolean(key) {
      const value = this.get(key);
      if (value) {
        return value.toLowerCase() === "true";
      }
      return false;
    }
    /**
     * Retrieves a JSON type object or array configuration value.
     *
     * @param key {string}
     *   `string` identifier.
     * @return {object|string[]}
     *   JavaScript `object`, `string[]` array, or `undefined`.
     */
    getObject(key) {
      let value = this.get(key);
      if (!value) {
        return;
      }
      value = JSON.parse(value);
      if (typeof value === "object") {
        return value;
      }
    }
    /**
     * Retrieves an enumarable object of available options for a configuration key.
     *
     * @param key {string}
     *   `string` identifier.
     * @return {object}
     *   `object` that can be enumerated into a set of key=value pairs in the format of `"id": "label"` where "label" is
     *   displayed in multiple choice settings. If "label" is an empty `string` then "id" is displayed instead.
     */
    getOpts(key) {
      return this.isOptsPairs(key) ? this.opts[key] : {};
    }
    /**
     * Checks if a key represents a set of enumerable configuration key=value pairs.
     *
     * @param key {string}
     *   `string` identifier.
     * @return {boolean}
     *   `true` if "key" has a value in the `ConfigManager.opts` object.
     */
    isOptsPairs(key) {
      return Object.keys(this.opts).indexOf(key) > -1;
    }
    /**
     * Removes a key & its value from storage.
     *
     * @param key {string}
     *   `string` identifier.
     * @todo
     *   Return `true` if successfully removed.
     */
    remove(key) {
      window.localStorage.removeItem("client." + key);
      stendhal.session.remove(key);
    }
    /**
     * Removes all stored configuration values.
     *
     * @todo
     *   Return `true` if all values successfully removed.
     */
    clear() {
      for (const key of Object.keys(this.defaults)) {
        this.remove(key);
      }
    }
    /**
     * Stores attributes for a dialog window.
     *
     * TODO: support resizable windows?
     *
     * @param id {string}
     *   Dialog `string` identifier (excluding "client.window." prefix).
     * @param x {number}
     *   Horizontal position.
     * @param y {number}
     *   Vertical position.
     */
    setWindowState(id, x, y) {
      this.set("window." + id, x + "," + y);
    }
    /**
     * Retrieves stored attributes for a dialog window.
     *
     * @param id {string}
     *   Dialog `string` identifier (excluding "client.window." prefix).
     * @return {util.Point.Point}
     *   Point containing X/Y positioning of window.
     */
    getWindowState(id) {
      let val = this.get("window." + id) || "0,0";
      let tmp = val.split(",");
      let x = parseInt(tmp[0]);
      let y = parseInt(tmp[1]);
      return new Point(x >= 0 ? x : 0, y >= 0 ? y : 0);
    }
    /**
     * Sets the UI theme.
     *
     * @param value {string}
     *   Theme `string` name/identifier.
     */
    setTheme(value) {
      this.set("theme", value);
    }
    /**
     * Retrieves the identifier of current theme.
     *
     * @return {string}
     *   Theme `string` name/identifier.
     */
    getTheme() {
      return this.get("theme") || "wood";
    }
    /**
     * Retrieves image filename of current theme.
     *
     * @return {string}
     *   `string` filename path.
     */
    getThemeBG() {
      return this.themes.map[this.getTheme()] || this.themes.map["wood"];
    }
    /**
     * Applies current theme to an element.
     *
     * @param element {HTMLElement}
     *   Element to be updated.
     * @param children {boolean}
     *   If `true`, theme will be applied to child elements of "element" (default: `false`).
     * @param recurse {boolean}
     *   If `true`, applies to all child elements recursively (default: `false`).
     * @param updateBG {boolean}
     *   If `true`, applies white backgrounds for dark themes & black backgrounds for light themes (default: `false`).
     * @todo
     *   Check that parameters "children", "recurse", & "updateBG" are still necessary as all elements may now have set
     *   the appropriate attribute to denote themable.
     */
    applyTheme(element, children = false, recurse = false, updateBG = false) {
      const current = this.getTheme();
      element.style.setProperty(
        "background",
        "url(" + Paths.gui + "/" + this.themes.map[current] + ")"
      );
      let color = "#000000";
      let colorbg = "#ffffff";
      if (this.themes.dark[current]) {
        color = "#ffffff";
        colorbg = "#000000";
      }
      element.style.setProperty("color", color);
      if (updateBG) {
        element.style.setProperty("background-color", colorbg);
      }
      if (children && element.children) {
        for (let idx = 0; idx < element.children.length; idx++) {
          this.applyTheme(element.children[idx], recurse, recurse);
        }
      }
    }
    /**
     * Refreshes & applies theme for all applicable elements.
     *
     * @param updateBG {boolean}
     *   If `true`, applies white backgrounds for dark themes & black backgrounds for light themes (default: `false`).
     * @todo
     *   Check that parameter "updateBG" is still necessary as all elements may now have set the appropriate attribute
     *   to denote themable.
     */
    refreshTheme(updateBG = false) {
      for (const elem of Array.from(document.querySelectorAll(".background"))) {
        this.applyTheme(elem, void 0, void 0, updateBG);
      }
      const current = this.getTheme();
      let rootStyle = document.documentElement.style;
      rootStyle.setProperty(
        "--background-url",
        "url(" + Paths.gui + "/" + this.themes.map[current] + ")"
      );
      if (this.usingDarkTheme()) {
        rootStyle.setProperty("--text-color", "#fff");
        rootStyle.setProperty("--text-color-inactive", "#aaa");
        rootStyle.setProperty("--text-color-online", "#0a0");
        rootStyle.setProperty("--text-color-offline", "#aaa");
      } else {
        rootStyle.setProperty("--text-color", "#000");
        rootStyle.setProperty("--text-color-inactive", "#555");
        rootStyle.setProperty("--text-color-online", "#070");
        rootStyle.setProperty("--text-color-offline", "#777");
      }
      const buddyList = ui.get(UIComponentEnum.BuddyList);
      if (buddyList) {
        buddyList.update();
      }
    }
    /**
     * Checks if the current theme is configured as "dark".
     *
     * @return {boolean}
     *   `true` if theme is considered dark & should employ a light text foreground color.
     */
    usingDarkTheme() {
      return this.themes.dark[this.getTheme()] == true;
    }
    /**
     * Retreives themes mappings.
     */
    getThemesMap() {
      return this.themes.map;
    }
    /**
     * Retrieves fonts mappings.
     */
    getFontsMap() {
      return this.fonts;
    }
  };

  // util/SessionManager.ts
  var SessionManager = class _SessionManager {
    /** Name of user's character for this client session. */
    charname;
    /** Attribute set to prevent re-initialization. */
    initialized = false;
    /** Attribute determining connection to default server counterpart (test client only). */
    server_default = true;
    /** Session configuration values. */
    states = {};
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_SessionManager.instance) {
        _SessionManager.instance = new _SessionManager();
      }
      return _SessionManager.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Initializes session values.
     */
    init(args) {
      if (this.initialized) {
        console.warn("tried to re-initialize SessionManager");
        return;
      }
      this.initialized = true;
      const charname = args.get("char") || args.get("character") || args.get("name");
      if (charname) {
        this.setCharName(charname);
      }
      const server = args.get("server");
      if (server && this.isTestClient()) {
        this.server_default = server !== "main";
      }
      for (let key of stendhal.config.getKeys()) {
        let value = stendhal.config.get(key);
        if (value == null) {
          value = "null";
        }
        this.set(key, value);
      }
      this.update();
    }
    /**
     * Retrieves a value from session memory.
     *
     * NOTE: Unless specifically needed do not call this directly. Instead `util.ConfigManager.get(key) should be called.
     *
     * @param key {string}
     *   `string` identifier.
     * @return {string|undefined}
     *   `string` value indexed by "key" or `undefined` if key not found.
     * @fixme
     *   Should be allowed to return `null`.
     */
    get(key) {
      return this.states[key];
    }
    /**
     * Stores a value in session memory.
     *
     * NOTE: Unless specifically needed do not call this directly. Instead `util.ConfigManager.set(key, value) should be
     *       called.
     *
     * @param key {string}
     *   `string` identifier.
     * @param value {any}
     *   Value to be stored.
     */
    set(key, value) {
      value = this.toString(value);
      if (value == void 0) {
        this.remove(key);
        return;
      }
      this.states[key] = value;
    }
    /**
     * Removes a key & its value from storage.
     *
     * NOTE: Unless specifically needed do not call this directly. Instead `util.ConfigManager.remove(key) should be
     *       called.
     *
     * @param key {string}
     *   String identifier.
     * @todo
     *   Return `true` if successfully removed.
     */
    remove(key) {
      delete this.states[key];
    }
    /**
     * Converts an object to string equivalent.
     *
     * @param value {any}
     *   Object to be converted.
     * @return {string|undefined}
     *   String representation of "value".
     */
    toString(value) {
      if (value == null) {
        return "null";
      }
      const vtype = typeof value;
      if (vtype === "undefined") {
        return void 0;
      } else if (vtype === "string") {
        return value;
      } else if (vtype === "object") {
        return JSON.stringify(value);
      }
      return "" + value;
    }
    /**
     * Sets active character name for this session.
     *
     * @param charname {string}
     *   Player's character name.
     */
    setCharName(charname) {
      this.charname = charname;
      document.title = "Stendhal - " + this.charname;
      document.getElementById("charname").innerText = this.charname;
    }
    /**
     * Retrieves the active character name for this session.
     *
     * @return {string}
     *   Player's character name or empty string if not set.
     */
    getCharName() {
      return this.charname || "";
    }
    /**
     * Syncs inteface with updated settings.
     */
    update() {
      if (stendhal.config.getBoolean("zoom.touch")) {
        document.documentElement.style.removeProperty("touch-action");
      } else {
        document.documentElement.style.setProperty("touch-action", "pan-x pan-y");
      }
    }
    /**
     * Detects if test client is being used based on data path.
     *
     * @return {boolean}
     *   `true` if data path root is set to "/testdata".
     */
    isTestClient() {
      return Paths.data === "/testdata";
    }
    /**
     * Checks if the client should connect to its default server counterpart.
     *
     * Test client only. Ignored by production client.
     *
     * @return {boolean}
     *   `true` if test client should connect to test server. `false` if it should connect to main production server.
     */
    isServerDefault() {
      return this.server_default;
    }
    /**
     * Checks if joystick/direction pad should be visible.
     *
     * @return {boolean}
     *   `true` if joystick property has been enabled manually or if no pointer device is detected.
     */
    joystickEnabled() {
      if (!stendhal.config.isSet("joystick") && this.touchOnly()) {
        return true;
      }
      return stendhal.config.getBoolean("joystick");
    }
    /**
     * Detectes if system is touch enabled without fine pointing device (mouse).
     *
     * @return {boolean}
     *   `true` if fine pointing device not detected.
     */
    touchOnly() {
      return !window.matchMedia("(pointer: fine)").matches;
    }
  };

  // ui/SoftwareJoystickController.ts
  var SoftwareJoystickController = class _SoftwareJoystickController {
    /** Active on-screen joystck. */
    joystick;
    /** Denotes state of event handlers initialization. */
    static registered = false;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_SoftwareJoystickController.instance) {
        _SoftwareJoystickController.instance = new _SoftwareJoystickController();
      }
      return _SoftwareJoystickController.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Registers handlers for joystick events that occur outside the elements' area.
     *
     * TODO: move registration to when joystick is engaged & de-register when disengaged
     */
    registerGlobalEventHandlers() {
      if (_SoftwareJoystickController.registered) {
        console.warn("Tried to re-register software joystick controller event handlers");
        return;
      }
      _SoftwareJoystickController.registered = true;
      document.body.addEventListener("mousemove", (evt) => {
        if (this.joystick && this.joystick.isEngaged()) {
          if (this.joystick instanceof Joystick) {
            this.joystick.onDragWhileEngaged(evt);
          }
          evt.stopPropagation();
          return;
        }
      });
      document.body.addEventListener("mouseup", (evt) => {
        if (evt.button == 0 && this.joystick && this.joystick.isEngaged()) {
          this.joystick.reset();
          evt.stopPropagation();
        }
      });
    }
    /**
     * Updates the on-screen joystick.
     */
    update() {
      const menuButton = ui.get(UIComponentEnum.QMJoystick);
      if (menuButton) {
        menuButton.update();
      }
      if (!SessionManager.get().joystickEnabled()) {
        this.remove();
        return;
      }
      const newJoystick = ConfigManager.get().get("joystick.style") === "dpad" ? DirectionPad.get() : Joystick.get();
      if (this.joystick && newJoystick === this.joystick) {
        this.joystick.update();
      } else {
        this.remove();
        this.joystick = newJoystick;
        newJoystick.addToDOM();
      }
    }
    /**
     * Removes joystick elements from DOM and unsets `joystick` property.
     */
    remove() {
      if (this.joystick) {
        this.joystick.removeFromDOM();
      }
      this.joystick = void 0;
    }
    /**
     * Checks for an active joystick that is engaged.
     *
     * @return {boolean}
     *   `true` if a joystick is set and is in engaged state.
     */
    isEngaged() {
      return this.joystick != void 0 && this.joystick.isEngaged();
    }
    /**
     * Retrieves the current joystick.
     *
     * @return {ui.joystick.JoystickImpl.JoystickImpl}
     *   Joystick instance or `undefined`.
     */
    getCurrent() {
      return this.joystick;
    }
  };

  // ui/toolkit/Tooltip.ts
  var Tooltip = class extends Pair {
    /** Element for which text is displayed. */
    element;
    /**
     * Creates a new pair of tooltips.
     *
     * @param {HTMLElement} element
     *   Element for which text is displayed.
     * @param {string} first
     *   Text when in primary state.
     * @param {string=} second
     *   Text when in secondary state.
     * @param {boolean} [primary=true]
     *   Initial state. `true` for primary, `false` for secondary. Does nothing if `secondary` is not
     *   defined.
     */
    constructor(element, first, second, primary = true) {
      super(first, second);
      this.element = element;
      this.setState(primary);
    }
    /**
     * Checks if secondary state text is set.
     *
     * @returns {boolean}
     *   `true` for dual state functionality, `false` for single.
     */
    isDualState() {
      return typeof this.second !== "undefined";
    }
    /**
     * Updates element text for state.
     *
     * @param {boolean} primary
     *   New state. `true` for primary, `false` for secondary. Always uses primary if only has single
     *   state functionality.
     */
    setState(primary) {
      if (primary || !this.isDualState()) {
        this.element.title = this.first;
      } else {
        this.element.title = this.second;
      }
    }
    /**
     * Updates element text to inverse state.
     */
    toggle() {
      this.setState(this.element.title !== this.first);
    }
  };

  // ui/toolkit/ComponentBase.ts
  var ComponentBase = class _ComponentBase {
    /** The parent `Component` of this one. Usually a floating window. */
    parentComponent;
    /** Help text. */
    tooltip;
    /**
     * Instructions when component should be refreshed.
     */
    refresh() {
    }
    /**
     * Adds as child to DOM element.
     *
     * @param parent {ui.toolkit.ComponentBase.ComponentBase|HTMLElement}
     *   Component or element to which to add.
     */
    addTo(parent) {
      const isComponent = parent instanceof _ComponentBase;
      this.parentComponent = isComponent ? parent : void 0;
      const parentElement = isComponent && this.parentComponent ? this.parentComponent.componentElement : parent;
      parentElement.appendChild(this.componentElement);
    }
    /**
     * Retrieves a child element.
     *
     * @param selector
     *   Child element's identification string.
     * @return
     *   `HTMLElement` or `undefined` if child not found.
     */
    child(selector) {
      return this.componentElement.querySelector(selector);
    }
    /**
     * Checks if the associated element currently has focus.
     *
     * @return {boolean}
     *   `true` if the document's active element is this component's element.
     */
    hasFocus() {
      return document.activeElement == this.componentElement;
    }
    /**
     * Enables or disables the HTML element.
     *
     * @param {boolean} enabled
     *   Enabled state to be set.
     */
    setEnabled(enabled) {
      this.componentElement.disabled = !enabled;
    }
    /**
     * Sets tooltip text for element.
     *
     * @param {string|Tooltip} tooltip
     *   Primary text or `Tooltip` object.
     * @param {string=} second
     *   Secondary text. Ignored if `tooltip` is `Tooltip` a object.
     * @param {boolean} [primary=true]
     *   Initial state. `true` for primary, `false` for secondary. Ignored if `tooltip` is a `Tooltip`
     *   object.
     */
    setTooltip(tooltip, second, primary = true) {
      if (typeof tooltip === "string") {
        this.tooltip = new Tooltip(this.componentElement, tooltip, second, primary);
      } else {
        this.tooltip = tooltip;
      }
    }
  };

  // ui/toolkit/Component.ts
  var Component = class extends ComponentBase {
    /** The `HTMLElement` associated with this component. */
    componentElement;
    /** @deprecated */
    cid;
    /** Default display type when component is visible.
     *  NOTE: this might not be necessary
     */
    visibleDisplay;
    /**
     * Creates a new component.
     *
     * @param {string|HTMLElement} el
     *   DOM element or ID of HTML element or template.
     * @param {boolean} [themable=false]
     *   Inherits theming visuals.
     */
    constructor(el, themable = false) {
      super();
      let element;
      if (typeof el === "string") {
        const id = el;
        element = document.getElementById(id);
        if (!element) {
          throw new Error("Cannot create component because there is no HTML element with id " + id);
        }
      } else {
        element = el;
      }
      if (element instanceof HTMLTemplateElement) {
        let fragment = element.content.cloneNode(true);
        element = fragment.children[0];
      }
      this.componentElement = element;
      if (themable) {
        this.componentElement.classList.add("background");
      }
      this.visibleDisplay = this.getDisplayProperty();
      if (this.visibleDisplay === "none") {
        this.visibleDisplay = "block";
      }
    }
    /**
     * Called when parent `FloatingWindow` is closed.
     */
    onParentClose() {
    }
    /**
     * Called when parent `FloatingWindow` is moved.
     */
    onMoved() {
    }
    /**
     * Sets value for configuration to identify this component.
     *
     * @param cid
     *     The string identifier.
     * @deprecated
     */
    setConfigId(cid) {
      this.cid = cid;
    }
    /**
     * Retrieves the string identifier for this component. If
     * an identifier is not set, an empty string is returned.
     *
     * @return
     *   String identifier.
     * @deprecated
     */
    getConfigId() {
      return this.cid || "";
    }
    /**
     * Retrieves display type of element.
     *
     * @return
     *   Current display.
     */
    getDisplayProperty() {
      return getComputedStyle(this.componentElement).getPropertyValue("display");
    }
    /**
     * Checks if the element is in a visible state.
     *
     * @return
     *   `true` if the element is visible in the DOM.
     */
    isVisible() {
      return this.getDisplayProperty() !== "none";
    }
    /**
     * Sets the element's visibility.
     *
     * @param visible
     *   `true` if the element should be visible.
     */
    setVisible(visible = true) {
      this.componentElement.style.setProperty("display", visible ? this.visibleDisplay : "none");
    }
    /**
     * Toggles the element's visibility.
     */
    toggleVisibility() {
      this.setVisible(!this.isVisible());
    }
  };

  // ui/toolkit/DialogContentComponent.ts
  var DialogContentComponent = class extends Component {
    frame;
    constructor(id) {
      super(id, true);
      this.componentElement.classList.add("dialogcontents");
    }
    /**
     * Sets the closable dialog frame.
     */
    setFrame(frame) {
      this.frame = frame;
    }
    getFrame() {
      return this.frame;
    }
    /**
     * Closes the containing FloatingWindow.
     */
    close() {
      if (this.frame) {
        this.frame.close();
      }
    }
    /**
     * Adds a dialog button.
     *
     * @param label {string}
     *   Text displayed on button.
     * @param action {Function}
     *   Action to execute when button is pressed (default: `undefined`).
     * @param id {string}
     *   HTML element ID (default: `undefined`).
     * @return {HTMLButtonElement}
     *   The created button.
     */
    addButton(label, action, id) {
      let buttonContainer = this.child(".dialogbuttons");
      if (typeof buttonContainer === "undefined" || buttonContainer == null) {
        buttonContainer = document.createElement("div");
        buttonContainer.classList.add("horizontalgroup dialogbuttons");
        this.componentElement.appendChild(buttonContainer);
      }
      let button = void 0;
      if (id) {
        button = this.child("#" + id);
      }
      if (typeof button === "undefined" || button == null) {
        button = document.createElement("button");
      }
      button.id = id ? id : button.id;
      button.classList.add("dialogbutton");
      button.innerText = label;
      if (action) {
        button.onclick = action;
      }
      buttonContainer.appendChild(button);
      return button;
    }
    /**
     * Adds a dialog button that will close the window.
     *
     * @param id {string}
     *   HTML element ID (default: `undefined`).
     * @return {HTMLButtonElement}
     *   The created button.
     */
    addCloseButton(id) {
      return this.addButton("Close", () => {
        this.close();
      }, id ? id : "button-close");
    }
  };

  // event/EventRegistry.ts
  var import_marauroa44 = __toESM(require_marauroa());

  // event/AttackEvent.ts
  var import_marauroa2 = __toESM(require_marauroa());
  var AttackEvent = class extends import_marauroa2.RPEvent {
    damage;
    type;
    weapon;
    execute(entity) {
      let target = entity.getAttackTarget();
      if (!target) {
        return;
      }
      if (this.hasOwnProperty("hit")) {
        var damage = parseInt(this["damage"], 10);
        if (damage !== 0) {
          target.onDamaged(entity, damage);
        } else {
          target.onBlocked(entity);
        }
      } else {
        target.onMissed(entity);
      }
      entity.onAttackPerformed(parseInt(this["type"], 10), this.hasOwnProperty("ranged"), this["weapon"]);
    }
  };

  // event/BestiaryEvent.ts
  var import_marauroa3 = __toESM(require_marauroa());
  var BestiaryEvent = class extends import_marauroa3.RPEvent {
    enemies;
    execute(_entity) {
      if (!this.hasOwnProperty("enemies")) {
        console.error('Event does not have "enemies" attribute');
        return;
      }
      const header = ["Bestiary:", '"???" = unknown'];
      const hasRare = this["enemies"].includes("(rare)");
      const hasAbnormal = this["enemies"].includes("(abnormal)");
      if (hasRare || hasAbnormal) {
        let subheader = "";
        if (!hasRare) {
          subheader += '"abnormal"';
        } else {
          subheader += '"rare"';
          if (hasAbnormal) {
            subheader += ' and "abnormal"';
          }
        }
        header[1] = subheader + " creatures not required for achievements";
      }
      const content = new class extends DialogContentComponent {
      }("empty-div-template");
      content.setConfigId("bestiary");
      content.componentElement.classList.add("bestiary");
      const layout = document.createElement("div");
      layout.className = "horizontalgroup stretchgroup";
      const col1 = document.createElement("div");
      const col2 = document.createElement("div");
      const col3 = document.createElement("div");
      for (const col of [col1, col2, col3]) {
        col.classList.add("verticalgroup", "stretchgroup");
      }
      const t1 = document.createElement("div");
      const t2 = document.createElement("div");
      const t3 = document.createElement("div");
      for (const t of [t1, t2, t3]) {
        t.classList.add("shopcol", "bestiary-cell");
      }
      t2.classList.add("center-text");
      t3.classList.add("center-text");
      t1.textContent = "Name";
      t2.textContent = "Solo";
      t3.textContent = "Shared";
      col1.appendChild(t1);
      col2.appendChild(t2);
      col3.appendChild(t3);
      for (const e of this["enemies"].split(";")) {
        const info = e.split(",");
        let solo = "";
        let shared = "";
        if (info[1] == "true") {
          solo = "\u2714";
        }
        if (info[2] == "true") {
          shared = "\u2714";
        }
        const l1 = document.createElement("div");
        const l2 = document.createElement("div");
        const l3 = document.createElement("div");
        for (const li of [l1, l2, l3]) {
          li.classList.add("shopcol", "bestiary-cell");
        }
        l2.classList.add("center-text");
        l3.classList.add("center-text");
        l1.textContent = info[0];
        l2.textContent = solo;
        l3.textContent = shared;
        col1.appendChild(l1);
        col2.appendChild(l2);
        col3.appendChild(l3);
      }
      layout.appendChild(col1);
      layout.appendChild(col2);
      layout.appendChild(col3);
      content.componentElement.appendChild(layout);
      stendhal.ui.globalInternalWindow.set(ui.createSingletonFloatingWindow(
        header.join(" "),
        content,
        20,
        20
      ));
    }
  };

  // util/Chat.ts
  var import_marauroa14 = __toESM(require_marauroa());

  // ui/ViewPort.ts
  var import_marauroa13 = __toESM(require_marauroa());

  // ui/HeldObject.ts
  var HeldObjectManager = class _HeldObjectManager {
    /** Singleton instance. */
    static instance;
    /** Image displayed when an object is "held". */
    image;
    heldObject;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_HeldObjectManager.instance) {
        _HeldObjectManager.instance = new _HeldObjectManager();
      }
      return _HeldObjectManager.instance;
    }
    /**
     * Static properties & methods only.
     */
    constructor() {
      this.image = document.getElementById("held-object");
    }
    prepare(draggedEntity, e) {
      let image = draggedEntity?.imageSprite?.imageRef.image;
      if (!draggedEntity || !(draggedEntity.type === "item" || draggedEntity.type === "corpse") || !image) {
        e.preventDefault();
        return false;
      }
      let canvas = document.getElementById("held-object");
      let sprite = draggedEntity.imageSprite;
      canvas.style.display = "block";
      this.setPosition(-1e3, -1e3);
      canvas.width = sprite.width;
      canvas.height = sprite.height;
      let ctx = canvas.getContext("2d");
      ctx.clearRect(0, 0, sprite.width, sprite.height);
      sprite.drawOntoWithZoom(ctx, 0, 0, 32, 32);
      if (e instanceof DragEvent) {
        e.dataTransfer?.setDragImage(canvas, 0, 0);
      }
      return true;
    }
    /**
     * Sets the image's visibility.
     *
     * @param visible
     *   `true` if image should be visible.
     */
    setVisible(visible = true) {
      this.image.style.setProperty("display", visible ? "block" : "none");
    }
    /**
     * Sets global held object & image to display.
     *
     * NOTE: currently this should only be used with touch events
     *
     * @param obj {ui.HeldObject.HeldObject}
     *   Object considered to be "held".
     * @param img {HTMLImageElement|string}
     *   Image element or path to image.
     * @param pos {util.Point.Point}
     *   Initial position of displayed image.
     */
    set(obj, pos) {
      this.heldObject = obj;
      this.onSet(pos);
    }
    /**
     * Called when object is ready to be displayed.
     *
     * @param pos {util.Point.Point}
     *   Initial position of displayed image.
     */
    onSet(pos) {
      if (pos) {
        this.setPosition(pos.x, pos.y);
      } else {
        const rect = ViewPort.get().getElement().getBoundingClientRect();
        this.setPosition(rect.left, rect.top);
      }
      this.setVisible(true);
      document.body.addEventListener("touchmove", this.onDragWhileHeld);
      document.body.addEventListener("touchend", this.onReleaseWhileHeld);
    }
    /**
     * Hides object image.
     */
    onRelease() {
      this.setVisible(false);
    }
    /**
     * Updates position of displayed image.
     *
     * @param x {number}
     *   Absolute positioning on X axis.
     * @param y {number}
     *   Absolute positioning on Y axis.
     */
    setPosition(x, y) {
      this.image.style.left = x + "px";
      this.image.style.top = y + "px";
    }
    /**
     * Handles event to update held object drawing position.
     */
    onDragWhileHeld(e) {
      const pos = HTMLUtil.extractPosition(e);
      _HeldObjectManager.get().setPosition(pos.pageX, pos.pageY);
    }
    /**
     * Handles event to unset held object.
     */
    onReleaseWhileHeld(e) {
      const hom = _HeldObjectManager.get();
      hom.onRelease();
      document.body.removeEventListener("touchmove", hom.onDragWhileHeld);
      document.body.removeEventListener("touchend", hom.onReleaseWhileHeld);
    }
  };

  // ui/dialog/ActionContextMenu.ts
  var import_marauroa4 = __toESM(require_marauroa());
  var ActionContextMenu = class extends Component {
    entity;
    actions;
    appendActions;
    constructor(entity, append = []) {
      super("contextmenu-template", true);
      this.entity = entity;
      this.appendActions = append;
      this.gatherActions();
      var content = '<div class="actionmenu verticalgroup">';
      for (var i = 0; i < this.actions.length; i++) {
        content += '<button class="actionbutton' + (i > 0 ? " sub-actionbutton" : "") + '" id="actionbutton.' + i + '">' + HTMLUtil.esc(this.actions[i].title) + "</button>";
      }
      content += "</div>";
      this.componentElement.innerHTML = content;
      this.componentElement.addEventListener("click", (event2) => {
        this.onClick(event2);
      });
    }
    onClick(event2) {
      let iStr = event2.target.getAttribute("id")?.substring(13);
      if (iStr === void 0 || iStr === "") {
        return;
      }
      let i = parseInt(iStr);
      if (i < 0) {
        return;
      }
      this.componentElement.dispatchEvent(new Event("close"));
      if (i >= this.actions.length) {
        throw new Error("actions index is larger than number of actions");
      }
      if (this.actions[i].action) {
        this.actions[i].action(this.entity);
      } else {
        this.executeFallbackAction(this.actions[i].type);
      }
    }
    executeFallbackAction(type) {
      let action = {
        "type": type,
        "target_path": this.entity.getIdPath(),
        "zone": import_marauroa4.marauroa.currentZoneName
      };
      if ("[" + this.entity.id + "]" === this.entity.getIdPath()) {
        action["target"] = "#" + this.entity.id;
      }
      import_marauroa4.marauroa.clientFramework.sendAction(action);
    }
    gatherActions() {
      let actions = [];
      this.entity.buildActions(actions);
      for (const action of this.appendActions) {
        actions.push(action);
      }
      if (import_marauroa4.marauroa.me["adminlevel"] && import_marauroa4.marauroa.me["adminlevel"] >= 600) {
        actions.push({
          title: "(*) Inspect",
          action: function(entity) {
            const action = { "type": "inspect" };
            if (entity.hasOwnProperty("id")) {
              action["target"] = "#" + entity["id"];
            }
            import_marauroa4.marauroa.clientFramework.sendAction(action);
          }
        });
        actions.push({
          title: "(*) Destroy",
          action: function(entity) {
            var action = {
              "type": "destroy"
            };
            if (entity.isContained()) {
              action["baseobject"] = import_marauroa4.marauroa.me["id"];
              action["baseslot"] = entity.getContainer()._name;
              action["baseitem"] = entity["id"];
            } else {
              action["target"] = "#" + entity["id"];
            }
            import_marauroa4.marauroa.clientFramework.sendAction(action);
          }
        });
        actions.push({
          title: "(*) Alter",
          action: function(entity) {
            ui.get(UIComponentEnum.ChatInput).setText("/alter #" + entity["id"] + " ");
          }
        });
      }
      this.actions = actions;
    }
  };

  // ui/dialog/DropQuantitySelectorDialog.ts
  var import_marauroa5 = __toESM(require_marauroa());
  var DropQuantitySelectorDialog = class extends Component {
    constructor(action, is_touch = false) {
      super("dropquantityselectordialog-template");
      this.action = action;
      this.child(".quantityselectorbutton")?.addEventListener("click", (event2) => {
        this.onDrop(event2);
      });
      if (is_touch) {
        const allButton = document.createElement("button");
        allButton.className = "allselectorbutton";
        allButton.innerText = "All";
        this.componentElement.appendChild(allButton);
        allButton.addEventListener("click", (event2) => {
          this.onDropAll(event2);
        });
      }
      let valueInput = this.child(".quantityselectorvalue");
      queueMicrotask(() => {
        valueInput.select();
        valueInput.focus();
      });
      valueInput.addEventListener("keydown", (event2) => {
        if (event2.key === "Enter") {
          this.onDrop(event2);
        }
      });
    }
    action;
    /**
     * Drops selected quantity from stack.
     */
    onDrop(event2) {
      HeldObjectManager.get().onRelease();
      let quantityStr = this.child(".quantityselectorvalue").value;
      let quantity = parseInt(quantityStr);
      if (quantity > 0) {
        this.action["quantity"] = quantityStr;
        import_marauroa5.marauroa.clientFramework.sendAction(this.action);
      }
      this.componentElement.dispatchEvent(new Event("close"));
      event2.preventDefault();
    }
    /**
     * Drops entire stack.
     */
    onDropAll(event2) {
      HeldObjectManager.get().onRelease();
      import_marauroa5.marauroa.clientFramework.sendAction(this.action);
      this.componentElement.dispatchEvent(new Event("close"));
      event2.preventDefault();
    }
  };

  // sprite/image/ImageFilter.ts
  var ImageFilter = class _ImageFilter {
    /**
     * @param rgb
     */
    splitrgb(rgb) {
      rgb &= 16777215;
      let b = rgb & 255;
      rgb >>>= 8;
      let g = rgb & 255;
      rgb >>>= 8;
      return [rgb, g, b];
    }
    mergergb(rgbArray) {
      const r = rgbArray[0] << 16;
      const g = rgbArray[1] << 8;
      return 16777215 & (r | g | rgbArray[2]);
    }
    /**
     * @param rgb
     */
    rgb2hsl(rgb) {
      let r = rgb[0] / 255;
      let g = rgb[1] / 255;
      let b = rgb[2] / 255;
      let max, min, maxVar;
      if (r > g) {
        max = r;
        min = g;
        maxVar = 0;
      } else {
        max = g;
        min = r;
        maxVar = 1;
      }
      if (b > max) {
        max = b;
        maxVar = 2;
      } else if (b < min) {
        min = b;
      }
      let l = (max + min) / 2;
      let s, h;
      let diff = max - min;
      if (diff < 1e-6) {
        s = 0;
        h = 0;
      } else {
        if (l < 0.5) {
          s = diff / (max + min);
        } else {
          s = diff / (2 - max - min);
        }
        if (maxVar === 0) {
          h = (g - b) / diff;
        } else if (maxVar === 1) {
          h = 2 + (b - r) / diff;
        } else {
          h = 4 + (r - g) / diff;
        }
        h /= 6;
      }
      return [h, s, l];
    }
    /**
     * @param {Array<Number>} hsl
     * @return {Array<Number>}
     */
    hsl2rgb(hsl) {
      let r, g, b;
      let h = hsl[0];
      let s = hsl[1];
      let l = hsl[2];
      if (s < 1e-7) {
        r = g = b = Math.floor(255 * l);
      } else {
        let tmp1, tmp2;
        if (l < 0.5) {
          tmp1 = l * (1 + s);
        } else {
          tmp1 = l + s - l * s;
        }
        tmp2 = 2 * l - tmp1;
        let rf = this.hue2rgb(this.limitHue(h + 1 / 3), tmp2, tmp1);
        let gf = this.hue2rgb(this.limitHue(h), tmp2, tmp1);
        let bf = this.hue2rgb(this.limitHue(h - 1 / 3), tmp2, tmp1);
        r = Math.floor(255 * rf) & 255;
        g = Math.floor(255 * gf) & 255;
        b = Math.floor(255 * bf) & 255;
      }
      return [r, g, b];
    }
    /**
     * @param hue
     * @param val1
     * @param val2
     */
    hue2rgb(hue, val1, val2) {
      let res = hue;
      if (6 * hue < 1) {
        res = val1 + (val2 - val1) * 6 * hue;
      } else if (2 * hue < 1) {
        res = val2;
      } else if (3 * hue < 2) {
        res = val1 + (val2 - val1) * (2 / 3 - hue) * 6;
      } else {
        res = val1;
      }
      return res;
    }
    /**
     * @param hue
     */
    limitHue(hue) {
      let res = hue;
      if (res < 0) {
        res += 1;
      } else if (res > 1) {
        res -= 1;
      }
      return res;
    }
    trueColor(data, color) {
      let hslColor = this.rgb2hsl(this.splitrgb(color));
      let end = data.length;
      for (let i = 0; i < end; i += 4) {
        let rgb = [data[i], data[i + 1], data[i + 2]];
        let hsl = this.rgb2hsl(rgb);
        let adj = hslColor[2] - 0.5;
        let tmp = hsl[2] - 0.5;
        let l = hsl[2] - 2 * adj * (tmp * tmp - 0.25);
        let resultHsl = [hslColor[0], hslColor[1], l];
        let resultRgb = this.hsl2rgb(resultHsl);
        data[i] = resultRgb[0];
        data[i + 1] = resultRgb[1];
        data[i + 2] = resultRgb[2];
      }
    }
    filter(data, filter, param) {
      if (filter === "trueColor") {
        this.trueColor(data.data, param);
      } else {
        console.log("Unknown filter name " + filter);
      }
    }
    filterImage(image, filter, param) {
      const canvas = new OffscreenCanvas(image.width, image.height);
      const ctx = canvas.getContext("2d", { willReadFrequently: true });
      ctx.drawImage(image, 0, 0);
      let imageData = ctx.getImageData(0, 0, image.width, image.height);
      new _ImageFilter().filter(imageData, filter, param);
      this.filter(imageData, filter, param);
      ctx.putImageData(imageData, 0, 0);
      return canvas.transferToImageBitmap();
    }
  };

  // data/HTMLImageStore.ts
  var HTMLImageStore = class {
    knownBrokenUrls = {};
    images = {};
    knownShadows = {
      "24x32": true,
      "32x32": true,
      "32x48": true,
      "32x48_long": true,
      "48x64": true,
      "48x64_float": true,
      "64x48": true,
      "64x64": true,
      "64x85": true,
      "64x96": true,
      "76x64": true,
      "81x96": true,
      "96x96": true,
      "96x128": true,
      "128x96": true,
      "128x170": true,
      "144x128": true,
      "168x224": true,
      "192x192": true,
      "192x192_float": true,
      "192x256": true,
      "320x440": true,
      "ent": true
    };
    // alternatives for known images that may be considered violent or mature
    knownSafeSprites = {
      [Paths.sprites + "/monsters/huge_animal/thing"]: true,
      [Paths.sprites + "/monsters/mutant/imperial_mutant"]: true,
      [Paths.sprites + "/monsters/undead/bloody_zombie"]: true,
      [Paths.sprites + "/npc/deadmannpc"]: true
    };
    // TODO: move to animation.json
    animations = {
      idea: {
        "love": { delay: 100, offsetX: 24, offsetY: -8 }
      }
    };
    get(filename) {
      if (!filename) {
        return {};
      }
      if (filename.indexOf("undefined") > -1) {
        if (!this.knownBrokenUrls[filename]) {
          console.log("Broken image path: ", filename, new Error());
        }
        this.knownBrokenUrls[filename] = true;
        return {};
      }
      if (this.images[filename]) {
        return this.images[filename];
      }
      var temp = new Image();
      temp.onerror = /* @__PURE__ */ (function(t, store) {
        return function() {
          if (t.src && !store.knownBrokenUrls[t.src]) {
            console.log("Broken image path:", t.src, new Error());
            store.knownBrokenUrls[t.src] = true;
          }
          const failsafe = store.getFailsafe();
          if (failsafe.src && t.src !== failsafe.src) {
            t.src = failsafe.src;
          }
        };
      })(temp, this);
      temp.src = filename;
      this.images[filename] = temp;
      return temp;
    }
    /**
     * Rotates an image.
     *
     * @param img
     *   Image to be rotated.
     * @param angle
     *   Angle of rotation.
     */
    rotate(img, angle) {
      const canvas = document.getElementById("drawing-stage");
      const ctx = canvas.getContext("2d");
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      canvas.width = img.width;
      canvas.height = img.height;
      ctx.translate(canvas.width / 2, canvas.height / 2);
      ctx.rotate(angle * Math.PI / 180);
      ctx.translate(-canvas.width / 2, -canvas.height / 2);
      ctx.drawImage(img, 0, 0);
      img.src = canvas.toDataURL("image/png");
    }
    /**
     * Retrieves a rotated image.
     *
     * @param filename
     *   Path to target image file.
     * @param angle
     *   Angle of rotation.
     * @return
     *   HTMLImageElement.
     */
    getRotated(filename, angle) {
      if (angle == 0) {
        return this.get(filename);
      }
      const id = filename + "-rot" + angle;
      if (this.images[id]) {
        return this.get(id);
      }
      const img = new Image();
      img.onload = () => {
        img.onload = null;
        this.rotate(img, angle);
      };
      const baseImg = this.get(filename);
      if (baseImg.complete) {
        img.src = baseImg.src;
      } else {
        baseImg.onload = () => {
          baseImg.onload = null;
          img.src = baseImg.src;
        };
      }
      this.images[id] = img;
      return img;
    }
    /**
     * Adds an image to cache.
     *
     * @param {string} id
     *   Cache identifier.
     * @param {SpriteImage}
     *   Image to be cached.
     */
    cache(id, image) {
      this.images[id] = image;
    }
    /**
     * Used when we only want an image if it was previously cached.
     *
     * @param filename
     *     Full file path.
     * @return
     *     HTMLImageElement or undefined.
     */
    getCached(filename) {
      return this.images[filename];
    }
    /**
     * Retrieves the failsafe sprite.
     *
     * @return
     *     HTMLImageElement with failsafe image data.
     */
    getFailsafe() {
      const filename = Paths.sprites + "/failsafe.png";
      let failsafe = this.images[filename];
      if (failsafe) {
      } else {
        failsafe = new Image();
        failsafe.src = filename;
        this.images[filename] = failsafe;
      }
      return failsafe;
    }
    /**
     * Checks cached images for a valid filename.
     *
     * @param filename
     *     Image filename to be checked.
     * @return
     *     Path to image or failsafe image file.
     */
    checkPath(filename) {
      return this.get(filename).src;
    }
    /**
     * @param {string} fileName
     * @param {string} filter
     * @param {number=} param
     */
    getFiltered(fileName, filter, param) {
      const img = this.get(fileName);
      if (!img.complete || img.width === 0 || img.height === 0) {
        return img;
      }
      const filteredName = fileName + " " + filter + " " + param;
      let filtered = this.images[filteredName];
      if (typeof filtered === "undefined") {
        const canvas = document.createElement("canvas");
        canvas.width = img.width;
        canvas.height = img.height;
        const ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0);
        const imgData = ctx.getImageData(0, 0, img.width, img.height);
        new ImageFilter().filter(imgData, filter, param);
        ctx.putImageData(imgData, 0, 0);
        canvas.complete = true;
        this.images[filteredName] = filtered = canvas;
      }
      return filtered;
    }
    /**
     * Retrieves a shadow sprite if the style is available.
     *
     * @param shadowStyle
     *     Style of shadow to get from cache.
     * @return
     *     Image sprite or <code>undefined</code>.
     */
    getShadow(shadowStyle) {
      if (this.knownShadows[shadowStyle]) {
        const img = new Image();
        img.src = Paths.sprites + "/shadow/" + shadowStyle + ".png";
        return img;
      }
      return void 0;
    }
    /**
     * Checks if there is a "safe" image available for sprite.
     *
     * @param filename
     *     The sprite image base file path.
     * @return
     *     <code>true</code> if a known safe image is available.
     */
    hasSafeImage(filename) {
      return this.knownSafeSprites[filename] == true;
    }
    /**
     * Called at startup to pre-cache certain images.
     */
    startupCache() {
      this.getFailsafe();
      this.get(Paths.sprites + "/npc/floattingladynpc.png");
      this.get(Paths.gui + "/banner_background.png");
      for (const cat of [
        "commerce",
        "deathmatch",
        "experience",
        "fighting",
        "friend",
        "interior_zone",
        "item",
        "obtain",
        "outside_zone",
        "production",
        "quest",
        "quest_ados_items",
        "quest_kill_blordroughs",
        "quest_kirdneh_item",
        "quest_mithrilbourgh_enemy_army",
        "quest_semos_monster",
        "special",
        "underground_zone"
      ]) {
        this.get(Paths.achievements + "/" + cat + ".png");
      }
    }
  };
  var htmlImageStore = /* @__PURE__ */ new HTMLImageStore();

  // util/BackgroundPainter.ts
  var BackgroundPainter = class {
    bg;
    // each slice represents a portion of the image with the upper-left corner at offset x,y
    slices = [];
    tileW;
    tileH;
    warned = false;
    constructor(img) {
      this.bg = img;
      this.tileW = this.bg.width / 3;
      this.tileH = this.bg.height / 3;
      for (let y = 0; y < this.bg.height; y += this.tileH) {
        const row = [];
        for (let x = 0; x < this.bg.width; x += this.tileW) {
          row.push({ x, y });
        }
        this.slices.push(row);
      }
    }
    getTileWidth() {
      return this.tileW;
    }
    getTileHeight() {
      return this.tileH;
    }
    /**
     * Draws the images on the canvas.
     *
     * @param x
     *     Left position where to start drawing image.
     * @param y
     *     Top position where to start drawing image.
     * @param width
     *     Number of horizontal tiles to draw.
     * @param height
     *     Number of vertical tiles to draw.
     */
    paint(ctx, x, y, width, height) {
      if (!this.tileW || !this.tileH) {
        if (!this.warned) {
          console.warn("cannot draw background while tile width or height is 0");
          this.warned = true;
        }
        this.tileW = this.bg.width / 3;
        this.tileH = this.bg.height / 3;
        return;
      }
      let drawY = 0, ir = 0;
      while (drawY < height) {
        let drawX = 0, ic = 0;
        while (drawX < width) {
          const slice = this.slices[ir][ic];
          ctx.drawImage(
            this.bg,
            slice.x,
            slice.y,
            this.tileW,
            this.tileH,
            x + drawX,
            y + drawY,
            this.tileW,
            this.tileH
          );
          drawX += this.tileW;
          drawX = drawX <= width ? drawX : drawX + (width - drawX);
          ic = drawX + this.tileW >= width ? 2 : 1;
        }
        drawY += this.tileH;
        drawY = drawY <= height ? drawY : drawY + (height - drawY);
        ir = drawY + this.tileH >= height ? 2 : 1;
      }
    }
  };

  // data/color/Color.ts
  var ColorType = class {
    /** Alpha level. */
    A;
    /**
     * Creates a new color representation with alpha level.
     *
     * @param A {number}
     *   Alpha level. Float value where 0 represents fully transparent and 1 fully opaque (min: 0,
     *   max: 1, default: `undefined`).
     */
    constructor(A) {
      if (typeof A === "number") {
        this.A = Math.max(Math.min(A, 1), 0);
      }
    }
  };
  var RGBColor = class extends ColorType {
    /** Red level. */
    R;
    /** Green level. */
    G;
    /** Blue level. */
    B;
    /** Alpha level. */
    //~ readonly A?: number;
    /**
     * Creates a new RGB color representation.
     *
     * @param R {number}
     *   Red color level (min: 0, max: 255).
     * @param G {number}
     *   Green color level (min: 0, max: 255).
     * @param B {number}
     *   Blue color level (min: 0, max: 255).
     * @param A {number}
     *   Alpha level. Float value where 0 represents fully transparent and 1 fully opaque (min: 0,
     *   max: 1, default: 1).
     */
    constructor(R, G, B, A) {
      super(A);
      this.R = Math.max(Math.min(R, 255), 0);
      this.G = Math.max(Math.min(G, 255), 0);
      this.B = Math.max(Math.min(B, 255), 0);
    }
    /**
     * Converts RGB color to string representation.
     *
     * @return {string}
     *   RGB or RGBA formatted string. Example: "rgb(255, 255, 255)"
     */
    toString() {
      const rgb = this.R + ", " + this.G + ", " + this.B;
      if (typeof this.A === "number") {
        return "rgba(" + rgb + ", " + this.A + ")";
      }
      return "rgb(" + rgb + ")";
    }
  };
  var HSLColor = class extends ColorType {
    /** Hue degree. */
    H;
    /** Saturation level. */
    S;
    /** Lightness level. */
    L;
    /** Alpha level. */
    //readonly A?: number;
    /**
     * Creates a new HSL color representation.
     *
     * @param H {number}
     *   Hue degree.
     * @param S {number}
     *   Saturation level. Float value representing between 0%-100% (min: 0, max: 1).
     * @param L {number}
     *   Lightness level. Float value representing between 0%-100% (min: 0, max: 1).
     * @param A {number}
     *   Alpha level. Float value where 0 represents fully transparent and 1 fully opaque (min: 0,
     *   max: 1, default: 1).
     */
    constructor(H, S, L, A) {
      super(A);
      this.H = MathUtil.normDeg(H);
      this.S = Math.max(Math.min(S, 1), 0);
      this.L = Math.max(Math.min(L, 1), 0);
    }
    /**
     * Converts HSL color to string representation.
     *
     * @return {string}
     *   HSL or HSLA formatted string. Example: "hsl(300, 100%, 50%)"
     */
    toString() {
      const hsl = this.H + ", " + this.S * 100 + "%, " + this.L * 100 + "%";
      if (typeof this.A === "number") {
        return "hsla(" + hsl + ", " + this.A + ")";
      }
      return "hsl(" + hsl + ")";
    }
  };
  var Color = class _Color {
    static AQUA = "rgb(0, 255, 255)";
    // #00FFFF
    static BLACK = "rgb(0, 0, 0)";
    // #000000
    static BLUE = "rgb(0, 0, 255)";
    // #0000FF
    static CYAN = _Color.AQUA;
    static DARK_GRAY = "rgb(96, 96, 96)";
    // #606060
    static GRAY = "rgb(128, 128, 128)";
    // #808080
    static GREEN = "rgb(0, 128, 0)";
    // #008000
    static LIGHT_GRAY = "rgb(192, 192, 192)";
    // #C0C0C0
    static MAGENTA = "rgb(255, 0, 255)";
    // #FF00FF
    static ORANGE = "rgb(255, 165, 0)";
    // #FFA500
    static PINK = "rgb(255, 192, 203)";
    // #FFC0CB
    static RED = "rgb(255, 0, 0)";
    // #FF0000
    static VIOLET = "rgb(238, 130, 238)";
    // #EE82EE
    static WHITE = "rgb(255, 255, 255)";
    // #FFFFFF
    static YELLOW = "rgb(255, 255, 0)";
    // #FFFF00
    static CREATURE = _Color.YELLOW;
    static DOMESTICANIMAL = "rgb(255, 150, 0)";
    // #FF9600 (orange)
    static GHOST = _Color.GRAY;
    static GROUP = "rgb(99, 61, 139)";
    // #633D8B (dark violet)
    static NPC = "rgb(0, 150, 0)";
    // #009600 (green)
    static PLAYER = _Color.WHITE;
    static USER = _Color.BLUE;
    static BACKGROUND = "rgb(204, 204, 204)";
    // #CCCCCC (light gray)
    static BLOCKED = _Color.RED;
    static COLLISION = _Color.RED;
    static FLYOVER = "rgb(212, 158, 72)";
    // #D49E48 (brown-orange)
    static PROTECTION = "rgb(202, 230, 202)";
    // #CAE6CA (light green)
    static WALKBLOCK = "rgb(209, 144, 224)";
    // #D190E0 (violet)
    static MOVING = "rgb(200, 255, 200)";
    // #C8FFC8 (light green)
    static PORTAL = _Color.WHITE;
    static PORTALOUTLINE = _Color.BLACK;
    static CHEST = _Color.VIOLET;
    static CHAT_CLIENT = _Color.GRAY;
    static CHAT_DETAILED = _Color.BLACK;
    static CHAT_EMOTE = _Color.GROUP;
    static CHAT_ERROR = _Color.RED;
    static CHAT_GROUP = "rgb(00, 00, 160)";
    // #0000A0 (dark blue)
    static CHAT_HEAL = _Color.GREEN;
    static CHAT_HIGHLIGHT = _Color.BLUE;
    static CHAT_INFO = _Color.ORANGE;
    static CHAT_NEGATIVE = _Color.RED;
    static CHAT_NORMAL = _Color.BLACK;
    static CHAT_POISON = _Color.RED;
    static CHAT_POSITIVE = _Color.GREEN;
    static CHAT_PRIVATE = _Color.DARK_GRAY;
    static CHAT_RESPONSE = "rgb(0, 100, 0)";
    // #006400 (dark green)
    static CHAT_SCENE = "rgb(87, 32, 2)";
    // #572002 (brown)
    static CHAT_SERVER = _Color.GRAY;
    static CHAT_SIG_NEGATIVE = _Color.PINK;
    static CHAT_SIG_POSITIVE = "rgb(65, 105, 225)";
    // #4169FF (light blue)
    static CHAT_SUPPORT = "rgb(255, 114, 0)";
    // #FF7200 (orange)
    static CHAT_TUTORIAL = "rgb(172, 0, 172)";
    // #AC00AC (violet)
    static CHAT_WARN = "rgb(160, 0, 0)";
    // #A00000 // (red)
    /**
     * Static members & methods only.
     */
    constructor() {
    }
    static getStatBarColor(ratio) {
      const red = Math.floor(Math.min((1 - ratio) * 2, 1) * 255);
      const green = Math.floor(Math.min(ratio * 2, 1) * 255);
      return "rgb(" + red + ", " + green + ", 0)";
    }
    /**
     * Converts a number value to RGB color.
     *
     * @param n {number}
     *   Value to be converted.
     * @return {data.color.Color.RGBColor}
     *   RGB color representation.
     */
    static numToRGB(n) {
      const r = n >> 16 & 255;
      const g = n >> 8 & 255;
      const b = n & 255;
      return new RGBColor(r, g, b);
    }
    /**
     * Converts a number value to HSL color.
     *
     * @param n {number}
     *   Value to be converted.
     * @return {data.color.Color.HSLColor}
     *   HSL color representation.
     */
    static numToHSL(n) {
      return _Color.RGBToHSL(_Color.numToRGB(n));
    }
    /**
     * Converts hex string to RGB color.
     *
     * FIXME: this should do error checking
     *
     * @param h {string}
     *   Hex value to be converted.
     * @return {data.color.Color.RGBColor}
     *   RGB color representation.
     */
    static hexToRGB(h) {
      h = h.replace(/^#/, "");
      return _Color.numToRGB(parseInt(h, 16));
    }
    /**
     * Converts hex string to HSL color.
     *
     * @param h {string}
     *   Hex value to be converted.
     * @return {data.color.Color.HSLColor}
     *   HSL color representation.
     */
    static hexToHSL(h) {
      return _Color.RGBToHSL(_Color.hexToRGB(h));
    }
    /**
     * Converts RGB string to HSL.
     *
     * https://css-tricks.com/converting-color-spaces-in-javascript/
     *
     * @param rgb {data.color.Color.RGBColor}
     *   RGB color representation.
     * @return {data.color.Color.HSLColor}
     *   HSL color representation.
     */
    static RGBToHSL(rgb) {
      const r = rgb.R / 255;
      const g = rgb.G / 255;
      const b = rgb.B / 255;
      const cmin = Math.min(r, g, b);
      const cmax = Math.max(r, g, b);
      const delta = cmax - cmin;
      let h = 0;
      if (delta == 0) {
        h = 0;
      } else if (cmax == r) {
        h = (g - b) / delta % 6;
      } else if (cmax == g) {
        h = (b - r) / delta + 2;
      } else {
        h = (r - g) / delta + 4;
      }
      h = Math.round(h * 60);
      let l = (cmax + cmin) / 2;
      let s = delta == 0 ? 0 : delta / (1 - Math.abs(2 * l - 1));
      return new HSLColor(h, Number(s.toFixed(3)), Number(l.toFixed(3)));
    }
    /**
     * Parses RBG color values.
     *
     * @param rbg {string}
     *   RGB formatted string.
     * @return {data.color.Color.RGBColor}
     *   Object with R/G/B numerical values.
     */
    static parseRGB(rgb) {
      const tmp = rgb.replace(/^rgb\(/, "").replace(/\)$/, "").split(",");
      return new RGBColor(Number(tmp[0]), Number(tmp[1]), Number(tmp[2]));
    }
    /**
     * Parses HSL color values.
     *
     * @parm hsl (strong}
     *   HSL formatted string.
     * @return {data.color.Color.HSLColor}
     *   Object with H/S/L numerical values.
     */
    static parseHSL(hsl) {
      const tmp = hsl.replace(/^hsl\(/, "").replace(/\)$/, "").split(",");
      const h = Number(tmp[0]);
      const s = parseFloat(tmp[1]) / 100;
      const l = parseFloat(tmp[2]) / 100;
      return new HSLColor(h, s, l);
    }
  };

  // sprite/TextBubble.ts
  var TextBubble = class _TextBubble {
    static STANDARD_DUR = 5e3;
    static adjustY = 15;
    text;
    timeStamp;
    x = -1;
    y = -1;
    width = -1;
    height = -1;
    duration = _TextBubble.STANDARD_DUR;
    onRemovedAction;
    constructor(text) {
      this.text = text;
      this.timeStamp = Date.now();
    }
    getX() {
      return this.x;
    }
    getY() {
      return this.y;
    }
    getWidth() {
      return this.width;
    }
    getHeight() {
      return this.height;
    }
    /**
     * Checks if a point or area clips the boundries of this sprite.
     *
     * @param x1
     *     Left-most position of area to check.
     * @param x2
     *     Right-most position of area to check.
     * @param y1
     *     Top-most position of area to check.
     * @param y2
     *     Bottom-most position of area to check.
     * @return
     *     <code>true</code> if any point is within the area of the
     *     sprite.
     */
    clips(x1, x2, y1, y2) {
      const x = this.getX(), y = this.getY();
      const r = x + this.getWidth();
      const b = y + this.getHeight();
      return x <= x1 && x1 <= r && x <= x2 && x2 <= r && (y <= y1 && y1 <= b && y <= y2 && y2 <= b);
    }
    /**
     * Checks if a point clips the boundaries of this sprite.
     *
     * @param x
     *     Horizonal coordinate.
     * @param y
     *     Vertical coordinate.
     * @return
     *     <code>true</code> if the point is within the area of the
     *     sprite.
     */
    clipsPoint(x, y) {
      return this.clips(x, x, y, y);
    }
    /**
     * Executed when viewport is clicked/tapped to detect text bubble boundaries & remove.
     *
     * @param evt
     *   Mouse or touch event executed on canvas context.
     */
    onClick(evt) {
      let viewPort = ViewPort.get();
      const pos = HTMLUtil.extractPosition(evt);
      const screenRect = document.getElementById("viewport").getBoundingClientRect();
      const pointX = pos.clientX - screenRect.x + viewPort.offsetX;
      const pointY = pos.clientY - screenRect.y + viewPort.offsetY + _TextBubble.adjustY;
      if (this.clipsPoint(pointX, pointY)) {
        evt.stopPropagation();
        viewPort.removeTextBubble(this, pointX, pointY);
      }
    }
    /**
     * Action to execute when sprite is added to viewport.
     *
     * Adds a listener to remove sprite with mouse click or touch.
     *
     * @param ctx
     *   Canvas context on which text bubble is drawn.
     */
    onAdded(ctx) {
      if (!(ctx instanceof CanvasRenderingContext2D)) {
        console.error("Cannot add event listeners because HTMLCanvasElement is required.");
        return;
      }
      if (typeof this.onRemovedAction === "undefined") {
        const listener = (e) => {
          this.onClick(e);
        };
        ctx.canvas.addEventListener("click", listener);
        ctx.canvas.addEventListener("touchend", listener);
        this.onRemovedAction = function() {
          ctx.canvas.removeEventListener("click", listener);
          ctx.canvas.removeEventListener("touchend", listener);
        };
      }
    }
    /**
     * Action to execute when sprite is removed from viewport.
     *
     * Removes the listener added with TextBubble.onAdded.
     */
    onRemoved() {
      if (typeof this.onRemovedAction !== "undefined") {
        this.onRemovedAction();
      }
    }
    /**
     * Sets the duration that the sprite should be displayed on the
     * screen.
     *
     * @param dur
     *     New duration time.
     */
    setDuration(dur) {
      if (dur == 0) {
        this.duration = _TextBubble.STANDARD_DUR;
      } else {
        this.duration = dur;
      }
    }
    /**
     * Checks if the sprite duration time has expired & should be
     * removed from the screen.
     *
     * @return
     *     <code>true</code> if the duration time has been exceeded.
     */
    expired() {
      return Date.now() >= this.timeStamp + this.duration;
    }
    /**
     * Identifies indexes for formatting & removes symbols from text.
     *
     * @param parts {util.Pair.Pair[]}
     *   Array to be populated.
     * @param defaultColor {string}
     *   Unformatted text color (default: `util.Color.Color.CHAT_NORMAL`).
     */
    segregate(parts, defaultColor = Color.CHAT_NORMAL) {
      let fillStyle = defaultColor;
      let inHighlight = false, inHighlightQuote = false;
      let idxStart = 0, idxEnd = 0;
      for (idxEnd; idxEnd < this.text.length; idxEnd++) {
        if (inHighlightQuote && this.text[idxEnd] === "'") {
          inHighlightQuote = false;
          inHighlight = false;
          parts.push(new Pair(fillStyle, this.text.substring(idxStart, idxEnd)));
          idxEnd++;
          idxStart = idxEnd;
          fillStyle = defaultColor;
        } else if (!inHighlightQuote && inHighlight && this.text[idxEnd] === " ") {
          inHighlight = false;
          parts.push(new Pair(fillStyle, this.text.substring(idxStart, idxEnd)));
          idxStart = idxEnd;
          fillStyle = defaultColor;
        } else if (this.text[idxEnd] === "#") {
          inHighlight = true;
          if (idxEnd > idxStart) {
            parts.push(new Pair(fillStyle, this.text.substring(idxStart, idxEnd)));
          }
          fillStyle = Color.CHAT_HIGHLIGHT;
          if (this.text[idxEnd + 1] === "'") {
            inHighlightQuote = true;
            idxEnd++;
          }
          idxStart = idxEnd + 1;
        }
      }
      if (idxEnd > idxStart) {
        parts.push(new Pair(fillStyle, this.text.substring(idxStart, idxEnd)));
      }
      this.text = "";
      for (const p of parts) {
        this.text += p.second;
      }
    }
  };

  // sprite/AchievementBanner.ts
  var AchievementBanner = class extends TextBubble {
    title;
    banner;
    icon;
    font = "normal 14px " + stendhal.config.get("font.travel-log");
    fontT = "normal 20px " + stendhal.config.get("font.travel-log");
    innerWidth = 0;
    innerHeight = 0;
    padding = 32;
    constructor(cat, title, desc) {
      super(desc);
      this.title = title;
      const bg = htmlImageStore.get(Paths.gui + "/banner_background.png");
      this.banner = new BackgroundPainter(bg);
      this.icon = htmlImageStore.get(Paths.achievements + "/" + cat.toLowerCase() + ".png");
      this.duration = TextBubble.STANDARD_DUR * 4;
      const gamewindow = document.getElementById("viewport");
      const td = this.getTextDimensions(gamewindow.getContext("2d"));
      this.innerWidth = td.width + this.padding;
      this.innerHeight = td.height;
      this.width = this.innerWidth + this.padding * 2;
      this.height = bg.height || this.innerHeight;
      this.x = gamewindow.width / 2 - this.width / 2;
      this.y = gamewindow.height - this.height;
    }
    draw(ctx) {
      let viewPort = ViewPort.get();
      const targetX = viewPort.offsetX + this.x;
      const targetY = viewPort.offsetY + this.y;
      const iconX = targetX + this.width / 2 - this.innerWidth / 2;
      const iconY = targetY + this.height / 2 - this.icon.height * 0.75;
      const textX = iconX + this.icon.width + this.padding;
      const textY = targetY + this.height / 2 + 10;
      this.banner.paint(
        ctx,
        targetX,
        targetY,
        this.width,
        this.height
      );
      if (this.icon.height && this.icon.complete) {
        ctx.drawImage(
          this.icon,
          0,
          0,
          this.icon.width,
          this.icon.height,
          iconX,
          iconY,
          this.icon.width,
          this.icon.height
        );
      }
      ctx.fillStyle = "#000000";
      ctx.font = this.fontT;
      ctx.fillText(this.title, textX, textY - 25);
      ctx.font = this.font;
      ctx.fillText(this.text, textX, textY);
      return this.expired();
    }
    getTextDimensions(ctx) {
      const ret = {};
      ctx.font = this.font;
      let m = ctx.measureText(this.text);
      ret.width = m.width;
      ctx.font = this.fontT;
      m = ctx.measureText(this.title);
      ret.width = Math.max(ret.width, m.width);
      ret.height = 96;
      return ret;
    }
    getX() {
      return ViewPort.get().offsetX + this.x;
    }
    getY() {
      return ViewPort.get().offsetY + this.y;
    }
  };

  // util/NotificationType.ts
  var NotificationType = {
    "client": Color.CHAT_CLIENT,
    "detailed": Color.CHAT_DETAILED,
    "emote": Color.CHAT_EMOTE,
    "error": Color.CHAT_ERROR,
    "group": Color.CHAT_GROUP,
    "heal": Color.CHAT_HEAL,
    "information": Color.CHAT_INFO,
    "negative": Color.CHAT_NEGATIVE,
    "normal": Color.CHAT_NORMAL,
    "poison": Color.CHAT_POISON,
    "positive": Color.CHAT_POSITIVE,
    "privmsg": Color.CHAT_PRIVATE,
    "response": Color.CHAT_RESPONSE,
    "scene_setting": Color.CHAT_SCENE,
    "server": Color.CHAT_SERVER,
    "significant_negative": Color.CHAT_SIG_NEGATIVE,
    "significant_positive": Color.CHAT_SIG_POSITIVE,
    "support": Color.CHAT_SUPPORT,
    "tutorial": Color.CHAT_TUTORIAL,
    "warning": Color.CHAT_WARN
  };
  Object.freeze(NotificationType);

  // util/Speech.ts
  var Speech = class {
    // space to be left at the beginning and end of line in pixels
    //private margin_width = 3;
    // the diameter of the arc of the rounded bubble corners
    //private arc_diameter = 2 * this.margin_width + 2;
    /**
     * Draws a rectangle.
     *
     * @param ctx
     * @param x
     * @param y
     * @param width
     * @param height
     */
    static drawBubble(ctx, x, y, width, height, tail = false) {
      ctx.strokeRect(x, y - 15, width, height);
      ctx.fillRect(x, y - 15, width, height);
      ctx.beginPath();
      ctx.moveTo(x, y);
      if (tail) {
        ctx.lineTo(x - 5, y + 8);
        ctx.lineTo(x + 1, y + 5);
      }
      ctx.stroke();
      ctx.closePath();
      ctx.fill();
    }
    /**
     * Draws a rectangle with rounded edges.
     *
     * Source: https://stackoverflow.com/a/3368118/4677917
     *
     * @param ctx
     * @param x
     * @param y
     * @param width
     * @param height
     */
    static drawBubbleRounded(ctx, x, y, width, height) {
      const arc = 3;
      ctx.beginPath();
      ctx.moveTo(x + arc, y);
      ctx.lineTo(x + width - arc, y);
      ctx.quadraticCurveTo(x + width, y, x + width, y + arc);
      ctx.lineTo(x + width, y + height - arc);
      ctx.quadraticCurveTo(x + width, y + height, x + width - arc, y + height);
      ctx.lineTo(x + arc, y + height);
      ctx.quadraticCurveTo(x, y + height, x, y + height - arc);
      ctx.lineTo(x, y + 8);
      ctx.lineTo(x - 8, y + 11);
      ctx.lineTo(x, y + 3);
      ctx.lineTo(x, y + arc);
      ctx.quadraticCurveTo(x, y, x + arc, y);
      ctx.stroke();
      ctx.closePath();
      ctx.fill();
    }
  };

  // sprite/image/HTMLImageElementUtil.ts
  var HTMLImageElementUtil = class {
    /**
     * Get an image element whose image data is an area of a specified image.
     * If the area matches the original image, the image itself is returned.
     * Otherwise <em>a copy</em> of the image data is returned. This is meant
     * to be used for obtaining the drag image for drag and drop.
     *
     * This method intentionally returns an HTMLImageElement because Chrome is
     * not able to use an HTMLCanvasElement as drag image. Firefox does support
     * this. But neither browser is able to use an ImageBitmap.
     *
     * @param image original image
     * @param width width of the area
     * @param height height of the area
     * @param offsetX optional. left x coordinate of the area
     * @param offsetY optional. top y coordinate of the area
     */
    static getAreaOf(image, width, height, offsetX, offsetY) {
      offsetX = offsetX || 0;
      offsetY = offsetY || 0;
      if (image instanceof HTMLImageElement && image.width === width && image.height === height && offsetX === 0 && offsetY === 0) {
        return image;
      }
      let canvas = document.createElement("canvas");
      canvas.width = width;
      canvas.height = height;
      let ctx = canvas.getContext("2d", { willReadFrequently: true });
      try {
        ctx.drawImage(image, offsetX, offsetY, width, height, 0, 0, width, height);
      } catch (err) {
        console.log(err);
      }
      let newImage = new Image();
      newImage.src = canvas.toDataURL("image/png");
      return newImage;
    }
  };

  // sprite/NotificationBubble.ts
  var NotificationBubble = class extends TextBubble {
    mtype;
    lines;
    profile;
    profileName;
    lmargin = 4;
    constructor(mtype, text, profile) {
      super(text);
      this.mtype = mtype;
      this.profileName = profile;
      this.duration = Math.max(
        TextBubble.STANDARD_DUR,
        this.text.length * TextBubble.STANDARD_DUR / 50
      );
      const linewrap = 30;
      const wordbreak = 60;
      this.lines = [];
      let words = text.split("	").join(" ").split(" ");
      let nextline = "";
      for (const w of words) {
        if (nextline) {
          nextline += " ";
        }
        nextline += w;
        if (nextline.length > wordbreak) {
          this.lines.push(nextline.substr(0, wordbreak) + "-");
          nextline = "-" + nextline.substr(wordbreak);
        } else if (nextline.length >= linewrap) {
          this.lines.push(nextline);
          nextline = "";
        }
      }
      if (nextline) {
        this.lines.push(nextline);
      }
      if (profile) {
        this.profile = new Image();
        this.loadProfileSprite();
      }
    }
    draw(ctx) {
      let viewPort = ViewPort.get();
      const screenTop = viewPort.offsetY;
      const screenBottom = screenTop + ctx.canvas.height;
      const screenLeft = viewPort.offsetX;
      const screenCenterX = screenLeft + ctx.canvas.width / 2;
      const fontsize = 14;
      const lheight = fontsize + 6;
      ctx.lineWidth = 2;
      ctx.font = fontsize + "px sans-serif";
      ctx.fillStyle = "#ffffff";
      ctx.strokeStyle = "#000000";
      const lcount = this.lines.length;
      if (this.width < 0 || this.height < 0) {
        let longest = 0;
        for (let li = 0; li < lcount; li++) {
          let measurement = ctx.measureText(this.lines[li]);
          if (measurement.width > longest) {
            longest = measurement.width;
          }
        }
        this.width = longest + this.lmargin * 2;
        this.height = lcount * lheight;
      }
      this.x = screenCenterX - this.width / 2;
      this.y = screenBottom - this.height + TextBubble.adjustY - 1;
      if (this.profile) {
        if (!this.profile.complete || !this.profile.height) {
          this.loadProfileSprite();
        }
        if (this.profile.complete && this.profile.height) {
          ctx.drawImage(this.profile, this.x - 48, this.y - 16);
        }
        Speech.drawBubbleRounded(
          ctx,
          this.x,
          this.y - 15,
          this.width,
          this.height
        );
      } else {
        Speech.drawBubble(
          ctx,
          this.x,
          this.y,
          this.width,
          this.height
        );
      }
      ctx.fillStyle = NotificationType[this.mtype] || Color.BLACK;
      let sy = this.y;
      for (let li = 0; li < lcount; li++) {
        ctx.fillText(this.lines[li], this.x + this.lmargin, sy);
        sy += lheight;
      }
      return this.expired();
    }
    /**
     * Loads a profile image to be drawn with text.
     */
    loadProfileSprite() {
      const img = htmlImageStore.get(Paths.sprites + "/npc/" + this.profileName + ".png");
      if (img.complete && img.height) {
        this.profile = HTMLImageElementUtil.getAreaOf(img, 48, 48, 48, 128);
      }
    }
  };

  // sprite/SpeechBubble.ts
  var SpeechBubble = class extends TextBubble {
    entity;
    offsetY;
    /** Formatted sections. */
    parts;
    constructor(text, entity) {
      let viewPort = ViewPort.get();
      text = text.replace(/\\\\/g, "\\");
      super(text.length > 30 ? text.substring(0, 30) + "..." : text);
      this.entity = entity;
      this.parts = [];
      this.segregate(this.parts);
      this.offsetY = 0;
      const x = this.getX(), y = this.getY();
      for (const bubble of viewPort.textSprites) {
        if (x == bubble.getX() && y + this.offsetY == bubble.getY()) {
          this.offsetY += viewPort.targetTileHeight / 2;
        }
      }
      this.duration = Math.max(
        TextBubble.STANDARD_DUR,
        this.text.length * TextBubble.STANDARD_DUR / 50
      );
    }
    draw(ctx) {
      ctx.lineWidth = 2;
      ctx.font = "14px Arial";
      ctx.fillStyle = Color.WHITE;
      ctx.strokeStyle = Color.BLACK;
      if (this.width < 0 || this.height < 0) {
        this.width = ctx.measureText(this.text).width + 8;
        this.height = 20;
      }
      let x = this.getX(), y = this.getY();
      Speech.drawBubbleRounded(ctx, x, y, this.width, this.height);
      x += 4;
      ctx.save();
      for (const p of this.parts) {
        ctx.fillStyle = p.first;
        ctx.fillText(p.second, x, y + TextBubble.adjustY);
        x += ctx.measureText(p.second).width;
      }
      ctx.restore();
      return this.expired();
    }
    getX() {
      let x = this.entity["_x"] * 32 + 32 * this.entity["width"];
      if (this.entity.inView()) {
        let viewPort = ViewPort.get();
        const overdraw = x + this.width - (viewPort.offsetX + viewPort.width) + 1;
        x = overdraw > 0 ? x - overdraw : x;
      }
      return x;
    }
    getY() {
      let y = this.entity["_y"] * 32 - 16 - 32 * (this.entity["height"] - 1) + this.offsetY - TextBubble.adjustY;
      if (this.entity.inView()) {
        let viewPort = ViewPort.get();
        y = y < viewPort.offsetY + 1 ? viewPort.offsetY + 1 : y;
      }
      return y;
    }
  };

  // data/TileMap.ts
  var import_marauroa6 = __toESM(require_marauroa());

  // landscape/LandscapeRenderingStrategy.ts
  var LandscapeRenderingStrategy = class {
  };

  // landscape/CombinedTileset.ts
  var CombinedTileset = class {
    constructor(numberOfTiles, combinedLayers, tileWidth, tileHeight) {
      this.combinedLayers = combinedLayers;
      this.tilesPerRow = Math.ceil(Math.sqrt(numberOfTiles));
      this.canvas = new OffscreenCanvas(
        tileWidth * this.tilesPerRow,
        tileHeight * this.tilesPerRow
      );
      this.ctx = this.canvas.getContext("2d");
    }
    combinedLayers;
    canvas;
    ctx;
    tilesPerRow;
  };

  // util/MapOfSets.ts
  var MapOfSets = class extends Map {
    /**
     * adds an entry to the map. If the key is already known,
     * the value is added to the associated set. Otherwise
     * a new set containing the value is created.
     *
     * @param key key of the entry
     * @param value value of the entry
     */
    add(key, value) {
      let set = this.get(key);
      if (set === void 0) {
        set = /* @__PURE__ */ new Set();
        this.set(key, set);
      }
      set.add(value);
    }
  };

  // util/Debug.ts
  var Debug = class _Debug {
    /** Properties marked for debugging. */
    static properties = {};
    /**
     * Static class.
     */
    constructor() {
    }
    /**
     * Checks if a property is marked for debugging.
     *
     * @param {string} prop
     *   Property name.
     * @returns {boolean}
     *   `true` if `prop` is contained in active list.
     */
    static isActive(prop) {
      return _Debug.properties[prop] === true;
    }
    /**
     * Marks or unmarks a property for debugging.
     *
     * @param {string} prop
     *   Property name.
     * @param {boolean} active
     *   `true` to mark as active, `false` for inactive.
     */
    static setActive(prop, active) {
      _Debug.properties[prop] = active;
    }
    /**
     * Toggles marking property for debugging.
     *
     * @param {string} prop
     *   Property name.
     * @returns {boolean}
     *   Property debugging state (`true` if active).
     */
    static toggle(prop) {
      _Debug.setActive(prop, !_Debug.isActive(prop));
      return _Debug.isActive(prop);
    }
  };

  // sprite/image/ImageRef.ts
  var ImageRef = class {
    image;
    async waitFor() {
      return new Promise(() => {
      });
    }
    drawOnto(ctx, x, y) {
      if (this.image) {
        ctx.drawImage(this.image, x, y);
      }
    }
  };

  // sprite/image/ImageRefImpl.ts
  var ImageRefImpl = class extends ImageRef {
    constructor(filename) {
      super();
      this.filename = filename;
      this.loaded = new Promise((resolve) => {
        this.promiseResolve = resolve;
      });
    }
    filename;
    refCount = 0;
    lastFreed;
    closed = false;
    loaded;
    promiseResolve;
    async load() {
      let url = this.filename + "?v=" + stendhal.data.build.version;
      let response = await fetch(url);
      if (!response.ok || this.closed) {
        this.promiseResolve(void 0);
        return;
      }
      let blob = await response.blob();
      if (this.closed) {
        this.promiseResolve(void 0);
        return;
      }
      let bitmap = await createImageBitmap(blob);
      if (this.closed) {
        bitmap.close();
        this.promiseResolve(void 0);
        return;
      }
      this.image = bitmap;
      this.promiseResolve(bitmap);
    }
    /**
     * called internally by ImageManager
     */
    use() {
      this.refCount++;
      this.lastFreed = void 0;
    }
    /**
     * called internally by ImageManager
     */
    free() {
      this.refCount--;
      if (this.refCount < 0) {
        console.error("Negative reference count", this);
      }
      if (this.refCount <= 0) {
        this.lastFreed = /* @__PURE__ */ new Date();
      }
    }
    shouldCleanup(olderThan) {
      if (this.refCount > 0 || !this.lastFreed) {
        return false;
      }
      return this.lastFreed < olderThan;
    }
    /**
     * called internally by ImageManager
     */
    close() {
      this.closed = true;
      this.image?.close();
      this.image = void 0;
    }
    async waitFor() {
      return this.loaded;
    }
  };

  // sprite/image/FilteredImageRefImpl.ts
  var FilteredImageRefImpl = class extends ImageRefImpl {
    constructor(filename, filter, param) {
      super(filename);
      this.filter = filter;
      this.param = param;
    }
    filter;
    param;
    async load() {
      let originalImage = images.load(this.filename);
      await originalImage.waitFor();
      if (originalImage.image) {
        this.image = new ImageFilter().filterImage(originalImage.image, this.filter, this.param);
      }
      images.free(this.filename);
      this.promiseResolve();
    }
  };

  // sprite/image/ImageManager.ts
  var ImageManager = class {
    images = /* @__PURE__ */ new Map();
    load(filename, filter, param) {
      if (filename.indexOf("..") > -1) {
        throw new Error("Invalid filename: " + filename);
      }
      let key = filename + "!" + filter + "!" + param;
      let imageRef = this.images.get(key);
      if (!imageRef) {
        if (filter) {
          imageRef = new FilteredImageRefImpl(filename, filter, param);
        } else {
          imageRef = new ImageRefImpl(filename);
        }
        imageRef.load();
        this.images.set(key, imageRef);
      }
      imageRef.use();
      return imageRef;
    }
    free(filename, filter, param) {
      let key = filename + "!" + filter + "!" + param;
      let imageRef = this.images.get(key);
      if (!imageRef) {
        console.error("freeing unknown image: " + key);
        return;
      }
      imageRef.free();
    }
    cleanup(olderThan) {
      for (let [key, imageRef] of this.images) {
        if (imageRef.shouldCleanup(olderThan)) {
          imageRef.close();
          this.images.delete(key);
        }
      }
    }
  };
  var images = /* @__PURE__ */ new ImageManager();

  // landscape/CombinedTilesetImageLoader.ts
  var CombinedTilesetImageLoader = class {
    constructor(map, indexToCombinedTiles, combinedTileset) {
      this.map = map;
      this.indexToCombinedTiles = indexToCombinedTiles;
      this.combinedTileset = combinedTileset;
      this.drawCanvas = new OffscreenCanvas(this.map.tileWidth, this.map.tileHeight);
      this.blendCanvas = new OffscreenCanvas(this.map.tileWidth, this.map.tileHeight);
      this.blendFixCanvas = new OffscreenCanvas(this.map.tileWidth, this.map.tileHeight);
      this.drawCtx = this.drawCanvas.getContext("2d");
      this.blendCtx = this.blendCanvas.getContext("2d");
      this.blendFixCtx = this.blendFixCanvas.getContext("2d");
    }
    map;
    indexToCombinedTiles;
    combinedTileset;
    tileUsedAtIndex;
    tilesetImages = [];
    drawCanvas;
    drawCtx;
    blendCanvas;
    blendCtx;
    blendFixCanvas;
    blendFixCtx;
    loadedTilesetCount = 0;
    usedTilesetCount = 0;
    calculateTileUsedAtIndex() {
      this.tileUsedAtIndex = new MapOfSets();
      for (let [index, combinedTile] of this.indexToCombinedTiles.entries()) {
        for (let tile of combinedTile) {
          let gid = tile & 536870911;
          this.tileUsedAtIndex.add(gid, index);
        }
      }
    }
    calculateUsedTilesets(gids) {
      let usedTilesets = /* @__PURE__ */ new Set();
      for (let gid of gids) {
        usedTilesets.add(this.map.getTilesetForGid(gid));
      }
      return usedTilesets;
    }
    async loadTileset(tileset) {
      const tsname = this.map.tilesetFilenames[tileset];
      if (!tsname) {
        return;
      }
      let imageRef = images.load(tsname);
      await imageRef.waitFor();
      this.tilesetImages[tileset] = imageRef;
      this.loadedTilesetCount++;
      this.drawTileset(tileset);
      if (this.loadedTilesetCount >= this.usedTilesetCount) {
        this.finish();
      }
    }
    finish() {
      for (let imageRef of this.tilesetImages) {
        imageRef.free();
      }
    }
    drawTileset(tileset) {
      let firstGid = this.map.firstgids[tileset];
      let image = this.tilesetImages[tileset].image;
      if (!image) {
        return;
      }
      let numberOfTiles = image.width / this.map.tileWidth * image.height / this.map.tileHeight;
      let lastGid = firstGid + numberOfTiles;
      for (let gid = firstGid; gid <= lastGid; gid++) {
        let indexes = this.tileUsedAtIndex.get(gid);
        if (indexes === void 0) {
          continue;
        }
        for (let index of indexes) {
          this.drawCombinedTileAtIndex(index);
        }
      }
    }
    drawCombinedTileAtIndex(index) {
      let tiles = this.indexToCombinedTiles.get(index);
      let x = index % this.combinedTileset.tilesPerRow;
      let y = Math.floor(index / this.combinedTileset.tilesPerRow);
      const pixelX = x * this.map.tileWidth;
      const pixelY = y * this.map.tileHeight;
      if (Debug.isActive("light") && ViewPort.get().HSLFilter) {
        this.drawCombinedTileWithBlend(tiles, pixelX, pixelY);
      } else {
        this.drawCombinedTileWithoutBlend(this.combinedTileset.ctx, tiles, pixelX, pixelY);
      }
    }
    drawCombinedTileWithoutBlend(ctx, tiles, pixelX, pixelY) {
      ctx.clearRect(pixelX, pixelY, this.map.tileWidth, this.map.tileHeight);
      for (let [i, tile] of tiles.entries()) {
        if (i === 0) {
          continue;
        }
        this.drawTile(ctx, tile, pixelX, pixelY);
      }
    }
    drawCombinedTileWithBlend(tiles, pixelX, pixelY) {
      this.drawCombinedTileWithoutBlend(this.drawCtx, tiles, 0, 0);
      this.blendCtx.globalCompositeOperation = "source-over";
      this.blendCtx.clearRect(0, 0, this.map.tileWidth, this.map.tileHeight);
      this.blendCtx.drawImage(this.drawCanvas, 0, 0);
      this.blendCtx.globalCompositeOperation = "multiply";
      this.blendCtx.fillStyle = ViewPort.get().HSLFilter;
      this.blendCtx.fillRect(0, 0, this.map.tileWidth, this.map.tileHeight);
      let tile = tiles[0];
      this.blendCtx.globalCompositeOperation = "soft-light";
      if (tile > 0) {
        this.blendFixCtx.fillStyle = "black";
        this.blendFixCtx.fillRect(0, 0, this.map.tileWidth, this.map.tileHeight);
        this.drawTile(this.blendFixCtx, tile, 0, 0);
        this.blendCtx.drawImage(this.blendFixCanvas, 0, 0);
      } else {
        this.blendCtx.fillStyle = "black";
        this.blendCtx.fillRect(0, 0, this.map.tileWidth, this.map.tileHeight);
      }
      this.blendCtx.globalCompositeOperation = "destination-in";
      this.blendCtx.drawImage(this.drawCanvas, 0, 0);
      this.combinedTileset.ctx.clearRect(pixelX, pixelY, this.map.tileWidth, this.map.tileHeight);
      this.combinedTileset.ctx.drawImage(this.blendCanvas, pixelX, pixelY);
    }
    drawTile(ctx, tile, pixelX, pixelY) {
      let flip = tile & 3758096384;
      let gid = tile & 536870911;
      let tileset = this.map.getTilesetForGid(gid);
      let image = this.tilesetImages[tileset]?.image;
      if (!image) {
        return;
      }
      let base = this.map.firstgids[tileset];
      let tileIndexInTileset = gid - base;
      this.drawImageTile(ctx, pixelX, pixelY, image, tileIndexInTileset, flip);
    }
    drawImageTile(ctx, pixelX, pixelY, tilesetImage, tileIndexInTileset, flip) {
      const tilesetWidth = tilesetImage.width;
      const tilesPerRow = Math.floor(tilesetWidth / this.map.tileWidth);
      if (flip === 0) {
        ctx.drawImage(
          tilesetImage,
          tileIndexInTileset % tilesPerRow * this.map.tileWidth,
          Math.floor(tileIndexInTileset / tilesPerRow) * this.map.tileHeight,
          this.map.tileWidth,
          this.map.tileHeight,
          pixelX,
          pixelY,
          this.map.tileWidth,
          this.map.tileHeight
        );
      } else {
        ctx.translate(pixelX, pixelY);
        const restore = [[1, 0, 0, 1, -pixelX, -pixelY]];
        if ((flip & 2147483648) !== 0) {
          ctx.transform(-1, 0, 0, 1, 0, 0);
          ctx.translate(-this.map.tileWidth, 0);
          restore.push([-1, 0, 0, 1, 0, 0]);
          restore.push([1, 0, 0, 1, this.map.tileWidth, 0]);
        }
        if ((flip & 1073741824) !== 0) {
          ctx.transform(1, 0, 0, -1, 0, 0);
          ctx.translate(0, -this.map.tileWidth);
          restore.push([1, 0, 0, -1, 0, 0]);
          restore.push([1, 0, 0, 1, 0, this.map.tileHeight]);
        }
        if ((flip & 536870912) !== 0) {
          ctx.transform(0, 1, 1, 0, 0, 0);
          restore.push([0, 1, 1, 0, 0, 0]);
        }
        ctx.drawImage(
          tilesetImage,
          tileIndexInTileset % tilesPerRow * this.map.tileWidth,
          Math.floor(tileIndexInTileset / tilesPerRow) * this.map.tileHeight,
          this.map.tileWidth,
          this.map.tileHeight,
          0,
          0,
          this.map.tileWidth,
          this.map.tileHeight
        );
        restore.reverse();
        for (const args of restore) {
          ctx.transform.apply(ctx, args);
        }
      }
    }
    load() {
      console.log("CombinedTilesetImageLoader.load()");
      this.calculateTileUsedAtIndex();
      let usedTilesets = this.calculateUsedTilesets(this.tileUsedAtIndex.keys());
      this.usedTilesetCount = usedTilesets.size;
      for (let tileset of usedTilesets) {
        this.loadTileset(tileset);
      }
    }
  };

  // landscape/CombinedTilesetFactory.ts
  var CombinedTilesetFactory = class {
    constructor(map) {
      this.map = map;
    }
    map;
    combine() {
      let combinedTilesToIndex = /* @__PURE__ */ new Map();
      let indexToCombinedTiles = /* @__PURE__ */ new Map();
      let combinedLayers = [];
      let index = 0;
      for (let group = 0; group < this.map.layerGroupIndexes.length; group++) {
        combinedLayers[group] = [];
      }
      for (let x = 0; x < this.map.zoneSizeX; x++) {
        for (let y = 0; y < this.map.zoneSizeY; y++) {
          for (let group = 0; group < this.map.layerGroupIndexes.length; group++) {
            let layers = this.map.layerGroupIndexes[group];
            let blendLayer = this.map.layerNames.indexOf(this.map.blendLayers[group]);
            let combinedTile = [];
            if (blendLayer && this.map.layers[blendLayer]) {
              combinedTile.push(this.map.layers[blendLayer][y * this.map.zoneSizeX + x]);
            } else {
              combinedTile.push(0);
            }
            for (let layer of layers) {
              let gid = this.map.layers[layer][y * this.map.zoneSizeX + x];
              if (gid > 0) {
                combinedTile.push(gid);
              }
            }
            if (combinedTile.length <= 1) {
              combinedLayers[group][y * this.map.zoneSizeX + x] = -1;
              continue;
            }
            let key = JSON.stringify(combinedTile);
            let value = combinedTilesToIndex.get(key);
            if (value === void 0) {
              value = index;
              combinedTilesToIndex.set(key, value);
              indexToCombinedTiles.set(value, combinedTile);
              index++;
            }
            combinedLayers[group][y * this.map.zoneSizeX + x] = value;
          }
        }
      }
      let combinedTileset = new CombinedTileset(combinedTilesToIndex.size, combinedLayers, this.map.tileWidth, this.map.tileHeight);
      let ctImageLoader = new CombinedTilesetImageLoader(this.map, indexToCombinedTiles, combinedTileset);
      ctImageLoader.load();
      return combinedTileset;
    }
  };

  // landscape/CombinedTilesetRenderingStrategy.ts
  var CombinedTilesetRenderingStrategy = class extends LandscapeRenderingStrategy {
    combinedTileset;
    map;
    onMapLoaded(map) {
      this.map = map;
      let combinedTilesetFactory = new CombinedTilesetFactory(map);
      this.combinedTileset = combinedTilesetFactory.combine();
    }
    onTilesetLoaded() {
      let body = document.getElementById("body");
      body.style.cursor = "auto";
    }
    render(canvas, gamewindow, tileOffsetX, tileOffsetY, targetTileWidth, targetTileHeight) {
      this.drawLayer(
        canvas,
        this.combinedTileset,
        0,
        tileOffsetX,
        tileOffsetY,
        targetTileWidth,
        targetTileHeight
      );
      gamewindow.drawEntities();
      this.drawLayer(
        canvas,
        this.combinedTileset,
        1,
        tileOffsetX,
        tileOffsetY,
        targetTileWidth,
        targetTileHeight
      );
    }
    drawLayer(canvas, combinedTileset, layerNo, tileOffsetX, tileOffsetY, targetTileWidth, targetTileHeight) {
      if (!combinedTileset) {
        return;
      }
      let ctx = canvas.getContext("2d");
      const layer = combinedTileset.combinedLayers[layerNo];
      const yMax = Math.min(tileOffsetY + canvas.height / targetTileHeight + 1, this.map.zoneSizeY);
      const xMax = Math.min(tileOffsetX + canvas.width / targetTileWidth + 1, this.map.zoneSizeX);
      for (let y = tileOffsetY; y < yMax; y++) {
        for (let x = tileOffsetX; x < xMax; x++) {
          let index = layer[y * this.map.zoneSizeX + x];
          if (index > -1) {
            try {
              const pixelX = x * targetTileWidth;
              const pixelY = y * targetTileHeight;
              ctx.drawImage(
                combinedTileset.canvas,
                index % combinedTileset.tilesPerRow * this.map.tileWidth,
                Math.floor(index / combinedTileset.tilesPerRow) * this.map.tileHeight,
                this.map.tileWidth,
                this.map.tileHeight,
                pixelX,
                pixelY,
                targetTileWidth,
                targetTileHeight
              );
            } catch (e) {
              console.error(e);
            }
          }
        }
      }
    }
  };

  // sprite/image/ImageCache.ts
  var ImageCache = class {
    images = {};
    closed = false;
    load(filenames) {
      for (let filename of filenames) {
        this.loadImage(filename);
      }
    }
    async loadImage(filename) {
      let url = filename + "?v=" + stendhal.data.build.version;
      let response = await fetch(url);
      if (!response.ok || this.closed) {
        return;
      }
      let blob = await response.blob();
      if (this.closed) {
        return;
      }
      let bitmap = await createImageBitmap(blob);
      if (this.closed) {
        bitmap.close();
        return;
      }
      this.images[filename] = bitmap;
    }
    close() {
      this.closed = true;
      for (let image of Object.values(this.images)) {
        image?.close();
      }
    }
  };

  // landscape/IndividualTilesetRenderingStrategy.ts
  var IndividualTilesetRenderingStrategy = class extends LandscapeRenderingStrategy {
    targetTileWidth = 32;
    targetTileHeight = 32;
    imageCache = new ImageCache();
    map;
    constructor() {
      super();
      window.setTimeout(() => {
        Chat.log("client", "Using IndividualTilesetRenderingStrategy");
      }, 1e3);
    }
    onMapLoaded(map) {
      console.log("Using IndividualTilesetRenderingStrategy.");
      this.map = map;
      this.imageCache.close();
      this.imageCache = new ImageCache();
      this.imageCache.load(map.tilesetFilenames);
    }
    onTilesetLoaded() {
      let body = document.getElementById("body");
      body.style.cursor = "auto";
    }
    render(canvas, gamewindow, tileOffsetX, tileOffsetY, targetTileWidth, targetTileHeight) {
      this.targetTileWidth = targetTileWidth;
      this.targetTileHeight = targetTileHeight;
      for (var drawingLayer = 0; drawingLayer < this.map.layers.length; drawingLayer++) {
        var name = this.map.layerNames[drawingLayer];
        if (name !== "protection" && name !== "collision" && name !== "objects" && name !== "blend_ground" && name !== "blend_roof") {
          this.paintLayer(canvas, drawingLayer, tileOffsetX, tileOffsetY);
        }
        if (name === "2_object") {
          gamewindow.drawEntities();
        }
      }
    }
    paintLayer(canvas, drawingLayer, tileOffsetX, tileOffsetY) {
      const layer = this.map.layers[drawingLayer];
      const yMax = Math.min(tileOffsetY + canvas.height / this.targetTileHeight + 1, this.map.zoneSizeY);
      const xMax = Math.min(tileOffsetX + canvas.width / this.targetTileWidth + 1, this.map.zoneSizeX);
      let ctx = canvas.getContext("2d");
      for (let y = tileOffsetY; y < yMax; y++) {
        for (let x = tileOffsetX; x < xMax; x++) {
          let gid = layer[y * this.map.zoneSizeX + x];
          const flip = gid & 3758096384;
          gid &= 536870911;
          if (gid > 0) {
            const tileset = this.map.getTilesetForGid(gid);
            const base = this.map.firstgids[tileset];
            const idx = gid - base;
            try {
              let image = this.imageCache.images[this.map.tilesetFilenames[tileset]];
              if (image) {
                this.drawTile(ctx, image, idx, x, y, flip);
              }
            } catch (e) {
              console.error(e);
            }
          }
        }
      }
    }
    drawTile(ctx, tileset, idx, x, y, flip = 0) {
      const tilesetWidth = tileset.width;
      const tilesPerRow = Math.floor(tilesetWidth / this.map.tileWidth);
      const pixelX = x * this.targetTileWidth;
      const pixelY = y * this.targetTileHeight;
      if (flip === 0) {
        ctx.drawImage(
          tileset,
          idx % tilesPerRow * this.map.tileWidth,
          Math.floor(idx / tilesPerRow) * this.map.tileHeight,
          this.map.tileWidth,
          this.map.tileHeight,
          pixelX,
          pixelY,
          this.targetTileWidth,
          this.targetTileHeight
        );
      } else {
        ctx.translate(pixelX, pixelY);
        const restore = [[1, 0, 0, 1, -pixelX, -pixelY]];
        if ((flip & 2147483648) !== 0) {
          ctx.transform(-1, 0, 0, 1, 0, 0);
          ctx.translate(-this.targetTileWidth, 0);
          restore.push([-1, 0, 0, 1, 0, 0]);
          restore.push([1, 0, 0, 1, this.targetTileWidth, 0]);
        }
        if ((flip & 1073741824) !== 0) {
          ctx.transform(1, 0, 0, -1, 0, 0);
          ctx.translate(0, -this.targetTileWidth);
          restore.push([1, 0, 0, -1, 0, 0]);
          restore.push([1, 0, 0, 1, 0, this.targetTileHeight]);
        }
        if ((flip & 536870912) !== 0) {
          ctx.transform(0, 1, 1, 0, 0, 0);
          restore.push([0, 1, 1, 0, 0, 0]);
        }
        this.drawTile(ctx, tileset, idx, 0, 0);
        restore.reverse();
        for (const args of restore) {
          ctx.transform(args[0], args[1], args[2], args[3], args[4], args[5]);
        }
      }
    }
  };

  // landscape/ParallaxBackground.ts
  var ParallaxBackground = class _ParallaxBackground {
    /** Default scrol rate of parallax background (1/4). */
    static SCROLL = 0.25;
    /** Tiled image to be drawn. */
    imageRef;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_ParallaxBackground.instance) {
        _ParallaxBackground.instance = new _ParallaxBackground();
      }
      return _ParallaxBackground.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Sets image to be drawn.
     *
     * @param {string} name
     *   Relative path (exluding .png filename suffix) to image inside "data/maps/parallax" directory
     *   or `undefined` to unset.
     * @param {number} width
     *   Map pixel width.
     * @param {number} height
     *   Map pixel height.
     */
    setImage(name, width, height) {
      let fullPath = singletons.getPaths().parallax + "/" + name + ".png";
      this.imageRef = images.load(fullPath);
    }
    /**
     * Unsets parallax background image.
     */
    reset() {
      this.imageRef?.free();
      this.imageRef = void 0;
    }
    draw(ctx, offsetX, offsetY) {
      if (!this.imageRef?.image) {
        return;
      }
      let image = this.imageRef.image;
      let dy = offsetY - offsetY / 4 % image.height;
      for (dy; dy < image.height * 100; dy += image.height) {
        let dx = offsetX - offsetX / 4 % image.width;
        for (dx; dx < image.width * 100; dx += image.width) {
          ctx.drawImage(image, dx, dy);
        }
      }
    }
  };

  // data/TileMap.ts
  var TileMap = class _TileMap {
    currentZoneName = "";
    offsetX = 0;
    offsetY = 0;
    zoneSizeX = -1;
    zoneSizeY = -1;
    sizeX = 20;
    sizeY = 15;
    tileWidth = 32;
    tileHeight = 32;
    zoom = 100;
    tilesetFilenames = [];
    aImages = -1;
    layerNames = -1;
    layers = -1;
    firstgids = -1;
    gidsindex = [];
    drawingError = false;
    targetTileWidth = 0;
    targetTileHeight = 0;
    layerGroups = [
      ["0_floor", "1_terrain", "2_object"],
      ["3_roof", "4_roof_add"]
    ];
    blendLayers = ["blend_ground", "blend_roof"];
    layerGroupIndexes;
    strategy;
    protection;
    collisionData;
    // alternatives for known images that may be considered violent or mature
    knownSafeTilesets = [
      Paths.tileset + "/item/armor/bloodied_small_axe",
      Paths.tileset + "/item/blood/floor_stain",
      Paths.tileset + "/item/blood/floor_stains_2",
      Paths.tileset + "/item/blood/nsew_stains",
      Paths.tileset + "/item/blood/small_stains"
    ];
    parallax;
    parallaxImage;
    ignoredTiles;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_TileMap.instance) {
        _TileMap.instance = new _TileMap();
      }
      return _TileMap.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
      if (window.location.search.indexOf("old") > -1) {
        this.strategy = new IndividualTilesetRenderingStrategy();
      } else {
        this.strategy = new CombinedTilesetRenderingStrategy();
      }
      this.parallax = ParallaxBackground.get();
      this.ignoredTiles = [];
    }
    /**
     * Returns the index of the tileset a tile belongs to.
     */
    getTilesetForGid(value) {
      if (value < this.gidsindex.length) {
        return this.gidsindex[value];
      } else {
        return this.gidsindex[this.gidsindex.length - 1] + 1;
      }
    }
    onTransfer(zoneName, content) {
      this.currentZoneName = zoneName;
      this.firstgids = [];
      this.layers = [];
      this.layerNames = [];
      var body = document.getElementById("body");
      body.style.cursor = "wait";
      console.log("load map");
      this.decodeTileset(content, "tilesets");
      this.decodeMapLayer(content, "0_floor");
      this.decodeMapLayer(content, "1_terrain");
      this.decodeMapLayer(content, "2_object");
      this.decodeMapLayer(content, "3_roof");
      this.decodeMapLayer(content, "4_roof_add");
      this.decodeMapLayer(content, "blend_ground");
      this.decodeMapLayer(content, "blend_roof");
      this.protection = this.decodeMapLayer(content, "protection");
      this.collisionData = this.decodeMapLayer(content, "collision");
      this.layerGroupIndexes = this.mapLayerGroup();
      this.strategy.onMapLoaded(this);
      if (this.parallaxImage) {
        this.parallax.setImage(
          this.parallaxImage,
          this.zoneSizeX * this.tileWidth,
          this.zoneSizeY * this.tileHeight
        );
      }
    }
    decodeTileset(content, name) {
      var layerData = content[name];
      var deserializer = import_marauroa6.Deserializer.fromBase64(layerData);
      var amount = deserializer.readInt();
      this.tilesetFilenames = [];
      for (var i = 0; i < amount; i++) {
        var name = deserializer.readString();
        var source = deserializer.readString();
        var firstgid = deserializer.readInt();
        var filename = "/" + source.replace(/\.\.\/\.\.\//g, "");
        const basename = filename.replace(/\.png$/, "").replace(/^\/tileset\//, "");
        const targetname = Paths.tileset + "/" + basename;
        if (this.ignoredTiles.indexOf(basename) > -1) {
          continue;
        }
        if (!stendhal.config.getBoolean("effect.blood") && this.hasSafeTileset(targetname)) {
          this.tilesetFilenames.push(targetname + "-safe.png");
        } else {
          this.tilesetFilenames.push(targetname + ".png");
        }
        this.firstgids.push(firstgid);
      }
      this.gidsindex = [];
      var pos, lastStart = 0, i;
      for (pos = 0; pos < parseInt(this.firstgids.length, 10); pos++) {
        for (i = lastStart; i < this.firstgids[pos]; i++) {
          this.gidsindex.push(pos - 1);
        }
        lastStart = this.firstgids[pos];
      }
      this.strategy.onTilesetLoaded();
    }
    decodeMapLayer(content, name) {
      var layerData;
      if (name === "0_floor" && stendhal.config.getBoolean("effect.parallax")) {
        layerData = content["0_floor_parallax"];
      }
      if (!layerData) {
        layerData = content[name];
      }
      if (!layerData) {
        return;
      }
      var deserializer = import_marauroa6.Deserializer.fromDeflatedBase64(layerData);
      deserializer.readString();
      this.zoneSizeX = deserializer.readInt();
      this.zoneSizeY = deserializer.readInt();
      var layerRaw = deserializer.readByteArray();
      var layer = [];
      for (var i = 0; i < this.zoneSizeX * this.zoneSizeY * 4 - 3; i = i + 4) {
        var tileId = layerRaw.getUint32(i, true);
        layer.push(tileId);
      }
      this.layerNames.push(name);
      this.layers.push(layer);
      return layer;
    }
    collision(x, y) {
      return this.collisionData[y * this.zoneSizeX + x] != 0;
    }
    isProtected(x, y) {
      return this.protection[y * this.zoneSizeX + x] != 0;
    }
    mapLayerGroup() {
      let res = [];
      for (let layers of this.layerGroups) {
        let group = [];
        for (let layer of layers) {
          let index = this.layerNames.indexOf(layer);
          if (index > -1) {
            group.push(index);
          }
        }
        if (group) {
          res.push(group);
        }
      }
      return res;
    }
    /**
     * Checks if there is an alternative "safe" tileset image available.
     *
     * @param filename
     *     The tileset image base file path.
     * @return
     *     <code>true</code> if a known safe image is available.
     */
    hasSafeTileset(filename) {
      return this.knownSafeTilesets.indexOf(filename) > -1;
    }
    /**
     * Sets or unsets parallax image.
     *
     * @param {string=} name
     *   Background image filename.
     */
    setParallax(name) {
      this.parallaxImage = name;
      if (typeof name === "undefined") {
        this.parallax.reset();
      }
    }
    /**
     * Adds tilesets to be ignored when drawing.
     *
     * @param {string=} tilesets
     *   Comma-delimited string. If empty or `undefined`, ignored tilesets list will be reset.
     */
    setIgnoredTiles(tilesets) {
      if (!tilesets) {
        this.ignoredTiles = [];
        return;
      }
      for (let t of tilesets.split(",")) {
        t = t.trim();
        if (t !== "") {
          this.ignoredTiles.push(t);
        }
      }
    }
  };

  // entity/Zone.ts
  var import_marauroa12 = __toESM(require_marauroa());

  // entity/Player.ts
  var import_marauroa10 = __toESM(require_marauroa());

  // entity/RPEntity.ts
  var import_marauroa9 = __toESM(require_marauroa());

  // entity/Entity.ts
  var import_marauroa7 = __toESM(require_marauroa());
  var import_marauroa8 = __toESM(require_marauroa());
  var Entity = class extends import_marauroa7.RPObject {
    minimapShow = false;
    minimapStyle = "rgb(200,255,200)";
    zIndex = 1e4;
    imageSprite;
    set(key, value) {
      super.set(key, value);
      if (key === "name") {
        if (typeof this["title"] === "undefined") {
          this["title"] = value;
        }
        this["_name"] = value;
      } else if (["x", "y", "height", "width"].indexOf(key) > -1) {
        this[key] = parseInt(value, 10);
      } else {
        this[key] = value;
      }
    }
    /**
     * is the other entity next to this entity?
     *
     * @return true, if the other entity is right next to us; false otherwise
     */
    isNextTo(other) {
      if (!other || !this["x"] || !this["y"] || !other["x"] || !other["y"]) {
        return false;
      }
      var nextX = this["x"] + this["width"] >= other["x"] && this["x"] <= other["x"] || other["x"] + other["width"] >= this["x"] && other["x"] <= this["x"];
      if (!nextX) {
        return false;
      }
      var nextY = this["y"] + this["height"] >= other["y"] && this["y"] <= other["y"] || other["y"] + other["height"] >= this["y"] && other["y"] <= this["y"];
      return nextY;
    }
    /**
     * Finds distance to another object on the X axis.
     *
     * @param other
     *     The object to measure against.
     * @return
     *     Distance in steps or -1.
     */
    getXDistanceTo(other) {
      if (other && this["x"] && other["x"]) {
        const tx_right = this["x"] + (this["width"] || 1) - 1;
        const ox_right = other["x"] + (other["width"] || 1) - 1;
        if (this["x"] > ox_right) {
          return Math.abs(this["x"] - ox_right);
        } else if (other["x"] > tx_right) {
          return Math.abs(other["x"] - tx_right);
        }
        return 0;
      }
      return -1;
    }
    /**
     * Finds distance to another object on the Y axis.
     *
     * @param other
     *     The object to measure against.
     * @return
     *     Distance in steps or -1.
     */
    getYDistanceTo(other) {
      if (other && this["y"] && other["y"]) {
        const ty_bottom = this["y"] + (this["height"] || 1) - 1;
        const oy_bottom = other["y"] + (other["height"] || 1) - 1;
        if (this["y"] > oy_bottom) {
          return Math.abs(this["y"] - oy_bottom);
        } else if (other["y"] > ty_bottom) {
          return Math.abs(other["y"] - ty_bottom);
        }
        return 0;
      }
      return -1;
    }
    /**
     * Finds the combined distance on the X & Y axi to
     * another object.
     *
     * @param other
     *     The object to measure against.
     * @return
     *     Distance in steps or -1.
     */
    getDistanceTo(other) {
      let x_dist = this.getXDistanceTo(other);
      let y_dist = this.getYDistanceTo(other);
      if (x_dist < 0 && y_dist < 0) {
        return -1;
      }
      x_dist = x_dist > -1 ? x_dist : 0;
      y_dist = y_dist > -1 ? y_dist : 0;
      return x_dist + y_dist;
    }
    /**
     * is this entity visible to a specific action
     *
     * @param _filter 0: short left click
     * @return true, if the entity is visible, false otherwise
     */
    isVisibleToAction(_filter) {
      return false;
    }
    /**
    	 * Map descriptive command names to the real commands
     */
    actionAliasToAction(actionAlias) {
      var actionAliases = {
        "look_closely": "use",
        "read": "look"
      };
      var actionCommand = "look";
      if (typeof actionAlias === "string") {
        if (actionAliases.hasOwnProperty(actionAlias)) {
          actionCommand = actionAliases[actionAlias];
        } else {
          actionCommand = actionAlias;
        }
      }
      return actionCommand;
    }
    buildActions(list) {
      if (this["menu"]) {
        var pos = this["menu"].indexOf("|");
        list.push({
          title: this["menu"].substring(0, pos),
          type: this["menu"].substring(pos + 1).toLowerCase()
        });
      }
      if (this["action"]) {
        list.push({
          title: HTMLUtil.niceName(this["action"]),
          type: this.actionAliasToAction(this["action"])
        });
      } else {
        list.push({
          title: "Look",
          type: "look"
        });
      }
    }
    onMiniMapDraw() {
    }
    /**
     *  Ensure that the drawing code can rely on _x and _y
     */
    updatePosition(time) {
      this["_y"] = this["y"];
      this["_x"] = this["x"];
    }
    draw(ctx) {
      if (this.sprite || this.imageSprite) {
        this.drawSprite(ctx);
      }
    }
    /**
     * draws a standard sprite
     */
    drawSprite(ctx) {
      this.drawSpriteAt(ctx, this["x"] * 32, this["y"] * 32);
    }
    drawSpriteAt(ctx, x, y) {
      if (this.imageSprite) {
        this.imageSprite.drawOnto(ctx, x, y, this.getWidth() * 32, this.getHeight() * 32);
      } else {
        let image = htmlImageStore.get(this.sprite.filename);
        if (image.height) {
          let offsetX = this.sprite.offsetX || 0;
          let offsetY = this.sprite.offsetY || 0;
          let width = this.sprite.width || image.width;
          let height = this.sprite.height || image.height;
          x += Math.floor((this.getWidth() * 32 - width) / 2);
          y += Math.floor((this.getHeight() * 32 - height) / 2);
          ctx.drawImage(image, offsetX, offsetY, width, height, x, y, width, height);
        }
      }
    }
    /**
     * gets the container path identifying the item
     *
     * @returns
     */
    getIdPath() {
      var object = this;
      var res = "";
      while (object) {
        res = object["id"] + "	" + res;
        var slot = object._parent;
        if (!slot) {
          break;
        }
        res = slot._name + "	" + res;
        object = slot._parent;
      }
      return "[" + res.substr(0, res.length - 1) + "]";
    }
    /**
     * says a text
     *
     * @param text
     *     Message contents.
     * @param rangeSquared
     *     Distance squared within which the entity can be heard (-1
     *     represents entire map).
     */
    say(text, rangeSquared) {
      if (import_marauroa8.marauroa.me && import_marauroa8.marauroa.me.isInHearingRange(this, rangeSquared)) {
        Chat.log("normal", text);
      }
    }
    getCursor(x, y) {
      var cursor = "unknown";
      if (this["cursor"]) {
        cursor = this["cursor"];
      }
      return "url(" + Paths.sprites + "/cursor/" + cursor.toLowerCase().replace("_", "") + ".png) 1 3, auto";
    }
    /**
     * Create the default action for this entity. If the entity specifies a
     * default action description, interpret it as an action command.
     */
    getDefaultAction() {
      return {
        "type": this.actionAliasToAction(this["action"]),
        "target": "#" + this["id"],
        "zone": import_marauroa8.marauroa.currentZoneName
      };
    }
    getResistance() {
      return this["resistance"];
    }
    isObstacle(entity) {
      return entity != this && this.getResistance() * (entity.getResistance() / 100) > 95;
    }
    onclick(x, y) {
      const action = this.getDefaultAction();
      if (action.type === "moveto") {
        import_marauroa8.marauroa.me.moveTo(action);
        return;
      }
      import_marauroa8.marauroa.clientFramework.sendAction(action);
    }
    /**
     * Checks if this is an entity that can be moved via
     * drag-and-drop, such as a corpse or an item.
     */
    isDraggable() {
      return false;
    }
    /**
     * Checks if within vieport area.
     */
    inView() {
      let viewPort = ViewPort.get();
      const view_rect = {};
      view_rect.left = viewPort.offsetX;
      view_rect.right = view_rect.left + viewPort.width;
      view_rect.top = viewPort.offsetY;
      view_rect.bottom = view_rect.top + viewPort.height;
      const pixelX = this["x"] * viewPort.targetTileWidth + Math.floor(viewPort.targetTileWidth / 2);
      const pixelY = this["y"] * viewPort.targetTileHeight + Math.floor(viewPort.targetTileHeight / 2);
      const ent_rect = {};
      ent_rect.left = pixelX - this["drawWidth"] / 2;
      ent_rect.right = pixelX + this["drawWidth"] / 2;
      ent_rect.bottom = pixelY;
      ent_rect.top = ent_rect.bottom - this["drawHeight"];
      return ent_rect.right > view_rect.left && ent_rect.left < view_rect.right && ent_rect.bottom > view_rect.top && ent_rect.top < view_rect.bottom;
    }
    destroy(_parent) {
      this.imageSprite?.free();
    }
  };

  // entity/ActiveEntity.ts
  var ActiveEntity = class extends Entity {
    direction;
    /** The current speed of this entity horizontally (tiles?/sec). */
    dx;
    /** The current speed of this entity vertically (tiles?/sec) . */
    dy;
    /** If <code>true</code>, this entity is not blocked by FlyOverArea */
    flying = false;
    /**
     * Create an active (moving) entity.
     */
    constructor() {
      super();
      this.direction = Direction.DOWN;
      this.dx = 0;
      this.dy = 0;
    }
    set(key, value) {
      if (["dir", "speed", "x", "y"].indexOf(key) > -1) {
        let numberValue = parseFloat(value);
        if (isNaN(this["_" + key])) {
          this["_" + key] = numberValue;
        }
        this.processPositioning(key, numberValue);
      }
      super.set(key, value);
    }
    unset(key) {
      super.unset(key);
      if (["dir", "speed"].indexOf(key) > -1) {
        this.processPositioning(key, 0);
      }
    }
    /**
     * Determine if this entity is not moving.
     *
     * @return <code>true</code> if not moving.
     */
    stopped() {
      return this.dx == 0 && this.dy == 0;
    }
    /**
     * calculates the movement if the server an client are out of sync. for some
     * milliseconds. (server turns are not exactly 300 ms) Most times this will
     * slow down the client movement
     *
     * @param clientPos
     *            the position the client has calculated
     * @param serverPos
     *            the position the server has reported
     * @param delta
     *            the movement based on direction
     * @return the new delta to correct the movement error
     */
    calcDeltaMovement(clientPos, serverPos, delta) {
      let moveErr = clientPos - serverPos;
      let moveCorrection = (delta - moveErr) / delta;
      return (delta + delta * moveCorrection) / 2;
    }
    /**
     * When entity moves, it will be called with the data.
     *
     * @param x new x coordinate
     * @param y new y coordinate
     * @param direction new direction
     * @param speed new speed
     */
    onMove(x, y, direction, speed) {
      let oldx = x;
      let oldy = y;
      this.dx = direction.dx * speed;
      this.dy = direction.dy * speed;
      if (Direction.LEFT == direction || Direction.RIGHT == direction) {
        this["_y"] = y;
        if (MathUtil.compareDouble(this["_x"], x, 1)) {
          this.dx = this.calcDeltaMovement(this["_x"] + this.dx * 0.1, x, direction.dx) * speed || 0;
        } else {
          this["_x"] = x;
        }
        this.dy = 0;
      } else if (Direction.UP == direction || Direction.DOWN == direction) {
        this["_x"] = x;
        if (MathUtil.compareDouble(this["_y"], y, 1)) {
          this.dy = this.calcDeltaMovement(this["_y"] + this.dy * 0.1, y, direction.dy) * speed || 0;
        } else {
          this["_y"] = y;
        }
        this.dx = 0;
      } else {
        this["_x"] = x;
        this["_y"] = y;
      }
      if (!MathUtil.compareDouble(this["_x"], oldx) || !MathUtil.compareDouble(this["_y"], oldy) || !MathUtil.compareDouble(oldx, Math.floor(oldx)) || !MathUtil.compareDouble(oldy, Math.floor(oldy))) {
      }
    }
    /**
     * Update cycle.
     *
     * @param delta
     *            The time (in ms) since last call.
     */
    updatePosition(delta) {
      if (!this.stopped()) {
        let step = delta / 300;
        let oldX = this["_x"];
        let oldY = this["_y"];
        this["_x"] += this.dx * step;
        this["_y"] += this.dy * step;
        if (this.collidesMap() || this.collidesEntities()) {
          this["_x"] = oldX;
          this["_y"] = oldY;
        } else {
        }
      }
    }
    /**
     * Process attribute changes that may affect positioning. This is needed
     * because different entities may want to process coordinate changes more
     * gracefully.
     *
     * @param base
     *            The previous values.
     * @param diff
     *            The changes.
     */
    processPositioning(key, value) {
      let oldx = this["x"];
      let oldy = this["y"];
      let newX = oldx;
      let newY = oldy;
      if (key == "x") {
        newX = value;
      }
      if (key == "y") {
        newY = value;
      }
      if (key == "dir") {
        this.direction = Direction.VALUES[value];
      }
      let speed = this["speed"];
      if (key == "speed") {
        speed = value;
      }
      this.onMove(newX, newY, this.direction, speed);
      if (this.direction == Direction.STOP || speed == 0) {
        this.dx = 0;
        this.dy = 0;
        this["_x"] = newX;
        this["_y"] = newY;
      }
    }
    /**
     * Check if the entity collides with the collision map.
     */
    collidesMap() {
      if (this.ignoresCollision()) {
        return false;
      }
      var startX = Math.floor(this["_x"]);
      var startY = Math.floor(this["_y"]);
      var endX = Math.ceil(this["_x"] + this["width"]);
      var endY = Math.ceil(this["_y"] + this["height"]);
      let map = TileMap.get();
      for (var y = startY; y < endY; y++) {
        for (var x = startX; x < endX; x++) {
          if (map.collision(x, y)) {
            return true;
          }
        }
      }
      return false;
    }
    /**
     * Checks if flag to ignore collision tiles or ghostmode is set.
     */
    ignoresCollision() {
      return typeof this["ignore_collision"] !== "undefined" || typeof this["ghostmode"] !== "undefined";
    }
    /**
     * Check if the entity with another entity;
     */
    collidesEntities() {
      var thisStartX = Math.floor(this["_x"]);
      var thisStartY = Math.floor(this["_y"]);
      var thisEndX = Math.ceil(this["_x"] + this["width"]);
      var thisEndY = Math.ceil(this["_y"] + this["height"]);
      var i;
      for (i in Zone.get().entities) {
        var other = Zone.get().entities[i];
        if (!this.isObstacle(other)) {
          continue;
        }
        var otherStartX = Math.floor(other["_x"]);
        var otherStartY = Math.floor(other["_y"]);
        var otherEndX = Math.ceil(other["_x"] + other["width"]);
        var otherEndY = Math.ceil(other["_y"] + other["height"]);
        if (thisStartX < otherEndX && thisEndX > otherStartX && thisStartY < otherEndY && thisEndY > otherStartY) {
          return true;
        }
      }
      return false;
    }
    /**
     * Retrieves entities direction of movement.
     *
     * Default is `Direction.STOP`.
     *
     * @returns {Direction}
     *   Current direction entity is moving or stopped.
     */
    getWalkDirection() {
      const val = parseInt(this["dir"], 10);
      if (Number.isNaN(val) || !Number.isFinite(val)) {
        return Direction.STOP;
      }
      return Direction.VALUES[Math.max(Direction.STOP.val, Math.min(Direction.LEFT.val, val))];
    }
    /**
     * Retrieves direction entity is facing.
     *
     * If direction cannot be determined (e.g. `Direction.STOP`) defaults to `Direction.DOWN`.
     *
     * @returns {Direction}
     *   Entity's facing direction.
     */
    getFaceDirection() {
      const dir = this.getWalkDirection();
      if (dir === Direction.STOP) {
        return Direction.DOWN;
      }
      return dir;
    }
  };

  // util/Nature.ts
  var Nature = class _Nature {
    constructor(color, elem) {
      this.color = color;
      this.elem = elem;
    }
    color;
    elem;
    /** physical attack */
    static CUT = new _Nature("#c0c0c0");
    /** fire magic */
    static FIRE = new _Nature("#ff6400", "fire");
    /** ice magic */
    static ICE = new _Nature("#8c8cff", "ice");
    /** light magic */
    static LIGHT = new _Nature("#fff08c", "light");
    /** dark magic */
    static DARK = new _Nature("#404040", "dark");
    static VALUES = [_Nature.CUT, _Nature.FIRE, _Nature.ICE, _Nature.LIGHT, _Nature.DARK];
    /**
     * Retrieves image for weapon & element type.
     *
     * @param weapon
     *     Weapon type (blade_strike (default), axe, club, sword).
     * @return
     *     Sprite path.
     */
    getWeaponPath(weapon) {
      let path = Paths.sprites + "/combat/" + weapon;
      if (typeof this.elem !== "undefined") {
        path += "_" + this.elem;
      }
      return path + ".png";
    }
  };

  // sprite/EmojiSprite.ts
  var EmojiSprite = class {
    timeStamp;
    owner;
    sprite;
    constructor(sprite, owner) {
      this.timeStamp = Date.now();
      this.owner = owner;
      this.sprite = sprite;
    }
    draw(ctx) {
      let x = this.owner["_x"] * 32 - 16;
      let y = this.owner["_y"] * 32 - 32;
      if (x < 0) {
        x = 0;
      }
      if (y < 0) {
        y = 0;
      }
      if (this.sprite.height) {
        ctx.drawImage(this.sprite, x, y);
      }
      return Date.now() > this.timeStamp + 5e3;
    }
  };

  // sprite/TextSprite.ts
  var TextSprite = class {
    constructor(text, color, font) {
      this.text = text;
      this.color = color;
      this.font = font;
    }
    text;
    color;
    font;
    textMetrics;
    /*
     * Draws text in specified color with black outline. Setting the font is the
     * caller's responsibility.
     *
     * @param ctx graphics context
     * @param x x coordinate
     * @param y y coordinate
     */
    draw(ctx, x, y) {
      ctx.font = this.font;
      ctx.lineWidth = 2;
      ctx.strokeStyle = "black";
      ctx.fillStyle = this.color;
      ctx.lineJoin = "round";
      ctx.strokeText(this.text, x, y);
      ctx.fillText(this.text, x, y);
    }
    getTextMetrics(ctx) {
      if (!this.textMetrics) {
        ctx.font = this.font;
        this.textMetrics = ctx.measureText(this.text);
      }
      return this.textMetrics;
    }
  };

  // sprite/Floater.ts
  var Floater = class {
    constructor(message, color) {
      this.message = message;
      this.color = color;
      this.initTime = Date.now();
      this.textSprite = new TextSprite(this.message, this.color, "12px sans-serif");
    }
    message;
    color;
    initTime;
    textSprite;
    draw(ctx, x, y) {
      let textOffset = this.textSprite.getTextMetrics(ctx).width / 2;
      var timeDiff = Date.now() - this.initTime;
      this.textSprite.draw(ctx, x - textOffset, y - timeDiff / 50);
      return timeDiff > 2e3;
    }
  };

  // sprite/action/AttackSprite.ts
  var AttackSprite = class {
    initTime;
    constructor() {
      this.initTime = Date.now();
    }
    expired() {
      return Date.now() - this.initTime > 180;
    }
  };

  // sprite/action/BarehandAttackSprite.ts
  var BarehandAttackSprite = class extends AttackSprite {
    dir;
    imageRef;
    constructor(source, imagePath) {
      super();
      this.dir = source["dir"];
      this.imageRef = images.load(imagePath);
    }
    draw(ctx, x, y, entityWidth, entityHeight) {
      let image = this.imageRef.image;
      if (!image) {
        return this.expired();
      }
      let viewPort = ViewPort.get();
      const dtime = Date.now() - this.initTime;
      const frameIndex = Math.floor(Math.min(dtime / 60, 2));
      const drawWidth = image.width / 3;
      const drawHeight = image.height / 4;
      const centerX = x + (entityWidth - drawWidth) / 2;
      const centerY = y + (entityHeight - drawHeight) / 2;
      let sx, sy;
      switch (this.dir + "") {
        case "1":
          sx = centerX + viewPort.targetTileWidth / 2;
          sy = y - viewPort.targetTileHeight * 1.5;
          break;
        case "3":
          sx = centerX;
          sy = y + entityHeight - drawHeight + viewPort.targetTileHeight / 2;
          break;
        case "4":
          sx = x - viewPort.targetTileWidth / 2;
          sy = centerY - viewPort.targetTileHeight / 2;
          break;
        case "2":
          sx = x + entityWidth - drawWidth + viewPort.targetTileWidth / 2;
          sy = centerY;
          break;
        default:
          sx = centerX;
          sy = centerY;
      }
      ctx.drawImage(
        image,
        frameIndex * drawWidth,
        (this.dir - 1) * drawHeight,
        drawWidth,
        drawHeight,
        sx,
        sy,
        drawWidth,
        drawHeight
      );
      return this.expired();
    }
    free() {
      this.imageRef?.free();
    }
  };

  // sprite/action/RangedAttackSprite.ts
  var RangedAttackSprite = class extends AttackSprite {
    dir;
    targetX;
    targetY;
    color;
    imageRef;
    weapon;
    constructor(source, target, color, weapon) {
      super();
      this.imageRef = images.load(Paths.sprites + "/combat/ranged.png");
      this.dir = source["dir"];
      this.targetX = (target.x + target.width / 2) * 32;
      this.targetY = (target.y + target.height / 2) * 32;
      this.color = color;
      this.weapon = weapon;
    }
    draw(ctx, x, y, entityWidth, entityHeight) {
      let viewPort = ViewPort.get();
      var dtime = Date.now() - this.initTime;
      var frame = Math.min(dtime / 60, 4);
      var startX = x + entityWidth / 4;
      var startY = y + entityHeight / 4;
      var yLength = (this.targetY - startY) / 4;
      var xLength = (this.targetX - startX) / 4;
      startY += frame * yLength;
      var endY = startY + yLength;
      startX += frame * xLength;
      var endX = startX + xLength;
      ctx.strokeStyle = this.color;
      ctx.lineWidth = 2;
      ctx.moveTo(startX, startY);
      ctx.lineTo(endX, endY);
      ctx.stroke();
      let image = this.imageRef.image;
      if (this.weapon === "ranged" && image) {
        frame = Math.floor(Math.min(dtime / 60, 3));
        const yRow = this.dir - 1;
        const drawWidth = image.width / 3;
        const drawHeight = image.height / 4;
        const centerX = x + (entityWidth - drawWidth) / 2;
        const centerY = y + (entityHeight - drawHeight) / 2;
        let sx, sy;
        switch (this.dir + "") {
          case "1":
            sx = centerX + viewPort.targetTileWidth / 2;
            sy = y - viewPort.targetTileHeight * 1.5;
            break;
          case "3":
            sx = centerX;
            sy = y + entityHeight - drawHeight + viewPort.targetTileHeight / 2;
            break;
          case "4":
            sx = x - viewPort.targetTileWidth / 2;
            sy = centerY - viewPort.targetTileHeight / 2;
            break;
          case "2":
            sx = x + entityWidth - drawWidth + viewPort.targetTileWidth / 2;
            sy = centerY;
            break;
          default:
            sx = centerX;
            sy = centerY;
        }
        ctx.drawImage(
          image,
          frame * drawWidth,
          yRow * drawHeight,
          drawWidth,
          drawHeight,
          sx,
          sy,
          drawWidth,
          drawHeight
        );
      }
      return this.expired();
    }
    free() {
      this.imageRef.free();
    }
  };

  // util/JSONLoader.ts
  var JSONLoader = class {
    data;
    onDataReady;
    constructor(onDataReady) {
      this.onDataReady = onDataReady;
    }
    /**
     * Loads JSON data from file.
     *
     * @param path
     *     Path to JSON file.
     */
    load(path) {
      fetch(path, {
        headers: { "Content-Type": "application/json" }
      }).then((resp) => resp.json()).then((data) => {
        this.data = data;
        if (typeof this.onDataReady !== "undefined") {
          this.onDataReady();
        }
      });
    }
  };

  // data/OutfitStore.ts
  var OutfitStore = class _OutfitStore {
    // layer names in draw order
    static layers = ["body", "dress", "head", "mouth", "eyes", "mask", "hair", "hat", "detail"];
    static layersLegacy = ["body", "dress", "head", "hair", "detail"];
    // layers using skin coloring
    static skinLayers = ["body", "head"];
    detailRearLayers = [];
    // player pickable layers
    count = {
      "hat": 19,
      "hair": 57,
      "mask": 9,
      "eyes": 28,
      "mouth": 5,
      "head": 4,
      "dress": 65,
      "body": 3
    };
    busty_dress = [
      1,
      4,
      6,
      7,
      10,
      11,
      13,
      16,
      29,
      37,
      40,
      53,
      54,
      56,
      61,
      64,
      967,
      968,
      977,
      980,
      989,
      990,
      999
    ];
    // hair should not be drawn with hat indexes in this list
    hats_no_hair = [
      3,
      4,
      13,
      16,
      992,
      993,
      994,
      996,
      997
    ];
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_OutfitStore.instance) {
        _OutfitStore.instance = new _OutfitStore();
      }
      return _OutfitStore.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    init() {
      const loader = new JSONLoader();
      loader.onDataReady = () => {
        this.detailRearLayers = loader.data["detail"]["rear"];
      };
      loader.load(Paths.sprites + "/outfit/outfits.json");
    }
    /**
     * Retrieves names of applicable outfit layers.
     *
     * @returns {string[]}
     *   Layer names.
     */
    getLayerNames(legacy = false) {
      if (legacy) {
        return [..._OutfitStore.layersLegacy];
      }
      return [..._OutfitStore.layers];
    }
    /**
     * Determines if a layer uses skin coloring.
     *
     * @param name {string}
     *   Layer name.
     * @return {boolean}
     *   `true` if the layer is considered to be a "skin" layer.
     */
    isSkinLayer(name) {
      return _OutfitStore.skinLayers.indexOf(name) > -1;
    }
    /**
     * Determines if hair should be drawn under a determinted hat index.
     *
     * @param hat
     *     Hat index to be checked.
     * @return
     *     <code>true</code> if hair should be drawn, <code>false</code> otherwise.
     */
    drawHair(hat) {
      return this.hats_no_hair.indexOf(hat) < 0;
    }
    /**
     * Determines if a busty alternative dress layer should be drawn.
     *
     * @param dress
     *     Dress index.
     * @param body
     *     Body index.
     * @return
     *     <code>true</code> if body type is busty & a busty dress
     *     alternative is available.
     */
    drawBustyDress(dress, body) {
      return body == 1 && this.busty_dress.indexOf(dress) > -1;
    }
    /**
     * Checks if we can draw a "rear" detail layer.
     *
     * @param detail
     *     Detail index.
     * @return
     *     <code>true</code> if a rear sprite is available.
     */
    detailHasRearLayer(detail) {
      return this.detailRearLayers.indexOf(detail) > -1;
    }
  };

  // sprite/indicator/StatusIconDrawer.ts
  var StatusIconDrawer = class {
    icons = {};
    load(filename) {
      let imageRef = this.icons[filename];
      if (!imageRef) {
        imageRef = images.load(filename);
        this.icons[filename] = imageRef;
      }
      return imageRef;
    }
    drawAnimatedIcon(ctx, iconPath, delay, x, y, fWidth) {
      let icon = this.load(iconPath).image;
      if (!icon) {
        return;
      }
      let dimH = icon.height;
      let dimW = fWidth || dimH;
      let nFrames = icon.width / dimW;
      let frame = Math.floor(Date.now() / delay) % nFrames;
      ctx.drawImage(icon, frame * dimW, 0, dimW, dimH, x, y, dimW, dimH);
    }
    drawAnimatedIconWithFrames(ctx, iconPath, nFrames, delay, x, y) {
      let icon = this.load(iconPath).image;
      if (!icon) {
        return;
      }
      let ydim = icon.height;
      let xdim = icon.width / nFrames;
      let frame = Math.floor(Date.now() / delay) % nFrames;
      ctx.drawImage(icon, frame * xdim, 0, xdim, ydim, x, y, xdim, ydim);
    }
    drawStatusIcons(entity, ctx) {
      let x = entity["_x"] * 32 - 10;
      let y = (entity["_y"] + 1) * 32;
      if (entity.hasOwnProperty("choking")) {
        this.load(Paths.sprites + "/ideas/choking.png").drawOnto(ctx, x, y - 10);
      } else if (entity.hasOwnProperty("eating")) {
        this.load(Paths.sprites + "/ideas/eat.png").drawOnto(ctx, x, y - 10);
      }
      if (entity.hasOwnProperty("idea")) {
        const idea = Paths.sprites + "/ideas/" + entity["idea"] + ".png";
        const ani = htmlImageStore.animations.idea[entity["idea"]];
        if (ani) {
          this.drawAnimatedIcon(
            ctx,
            idea,
            ani.delay,
            x + ani.offsetX * entity["width"],
            y - entity["drawHeight"] + ani.offsetY
          );
        } else {
          this.load(idea).drawOnto(ctx, x + 32 * entity["width"], y - entity["drawHeight"]);
        }
      }
      if (entity.hasOwnProperty("away")) {
        this.drawAnimatedIcon(ctx, Paths.sprites + "/ideas/away.png", 1500, x + 32 * entity["width"], y - entity["drawHeight"]);
      }
      if (entity.hasOwnProperty("grumpy")) {
        this.drawAnimatedIcon(ctx, Paths.sprites + "/ideas/grumpy.png", 1e3, x + 5, y - entity["drawHeight"]);
      }
      if (entity.hasOwnProperty("last_player_kill_time")) {
        this.drawAnimatedIconWithFrames(ctx, Paths.sprites + "/ideas/pk.png", 12, 300, x, y - entity["drawHeight"]);
      }
      if (entity.hasOwnProperty("poisoned")) {
        this.drawAnimatedIcon(ctx, Paths.sprites + "/status/poison.png", 100, x + 32 * entity["width"] - 10, y - entity["drawHeight"]);
      }
      if (entity.hasOwnProperty("status_confuse")) {
        this.drawAnimatedIcon(ctx, Paths.sprites + "/status/confuse.png", 200, x + 32 * entity["width"] - 14, y - entity["drawHeight"] + 16);
      }
      if (entity.hasOwnProperty("status_shock")) {
        this.drawAnimatedIcon(ctx, Paths.sprites + "/status/shock.png", 200, x + 32 * entity["width"] - 25, y - 32, 38);
      }
      let nextX = x;
      if (entity.hasOwnProperty("job_healer")) {
        this.load(Paths.sprites + "/status/healer.png").drawOnto(ctx, nextX, y - 10);
        nextX += 12;
      }
      if (entity.hasOwnProperty("job_merchant")) {
        this.load(Paths.sprites + "/status/merchant.png").drawOnto(ctx, nextX, y - 10);
        nextX += 12;
      }
      if (entity.hasOwnProperty("job_producer")) {
        this.load(Paths.sprites + "/status/producer.png").drawOnto(ctx, nextX, y - 16);
        nextX += 16;
      }
    }
  };
  var statusIconDrawer = /* @__PURE__ */ new StatusIconDrawer();

  // entity/RPEntity.ts
  var HEALTH_BAR_HEIGHT = 6;
  var RPEntity = class extends ActiveEntity {
    zIndex = 8e3;
    drawY = 0;
    spritePath = "";
    titleStyle = "#FFFFFF";
    _target;
    attackSprite;
    attackResult = void 0;
    // TODO
    dir = 3;
    titleTextSprite;
    floaters = [];
    // for keeping hp bar & title on screen
    statusBarYOffset = 0;
    // for adjusting entity hp bar & title vertical position
    titleDrawYOffset = 0;
    // canvas for merging outfit layers to be drawn
    octx;
    attackers = { size: 0 };
    /**
     * Animation drawn over entity sprite.
     *
     * TODO: maybe support multiple overlays
     */
    overlay;
    set(key, value) {
      let oldValue = this[key];
      super.set(key, value);
      if (key == "text") {
        this.say(value);
      } else if (["hp", "base_hp"].indexOf(key) !== -1) {
        this[key] = parseInt(value, 10);
        if (key === "hp" && oldValue != void 0) {
          this.onHPChanged(this[key] - oldValue);
        }
      } else if (key === "id" && !oldValue && this._target) {
        this._target.onTargeted(this);
      } else if (key === "target" && this["id"]) {
        if (this._target) {
          this._target.onAttackStopped(this);
        }
        this._target = import_marauroa9.marauroa.currentZone[value];
        if (this._target) {
          this._target.onTargeted(this);
        }
      } else if (key === "away") {
        this.addFloater("Away", "#ffff00");
      } else if (key === "grumpy") {
        this.addFloater("Grumpy", "#ffff00");
      } else if (key === "xp" && typeof oldValue !== "undefined") {
        this.onXPChanged(this[key] - oldValue);
      } else if (["level", "atk", "ratk", "def"].indexOf(key) > -1 && typeof oldValue !== "undefined") {
        this.onLevelChanged(key, value, oldValue);
      } else if (["title", "name", "class", "type"].indexOf(key) > -1) {
        this.createTitleTextSprite();
      } else if (key === "subclass" && typeof oldValue !== "undefined" && value !== oldValue) {
        this.onTransformed();
      }
    }
    unset(key) {
      if (key === "target" && this._target) {
        this._target.onAttackStopped(this);
        this._target = void 0;
      } else if (key === "away") {
        this.addFloater("Back", "#ffff00");
      } else if (key === "grumpy") {
        this.addFloater("Receptive", "#ffff00");
      }
      super.unset(key);
    }
    isVisibleToAction(_filter) {
      return typeof this["ghostmode"] == "undefined" || import_marauroa9.marauroa.me && import_marauroa9.marauroa.me.isAdmin();
    }
    buildActions(list) {
      super.buildActions(list);
      if (!this["menu"]) {
        if (import_marauroa9.marauroa.me._target === this) {
          list.push({
            title: "Stop attack",
            action: function(_entity) {
              var action = {
                "type": "stop",
                "zone": import_marauroa9.marauroa.currentZoneName,
                "attack": ""
              };
              import_marauroa9.marauroa.clientFramework.sendAction(action);
            }
          });
        } else if (this !== import_marauroa9.marauroa.me) {
          list.push({
            title: "Attack",
            type: "attack"
          });
        }
      }
      if (this != import_marauroa9.marauroa.me) {
        list.push({
          title: "Push",
          type: "push"
        });
      }
    }
    /**
     * retrieves the entity's visible title
     */
    getTitle() {
      return this["title"];
    }
    /**
     * says a text
     *
     * @param text
     *     Message contents.
     * @param rangeSquared
     *     Distance at which message can be heard (-1 represents
     *     entire map).
     */
    say(text, rangeSquared) {
      if (!import_marauroa9.marauroa.me) {
        return;
      }
      if (import_marauroa9.marauroa.me.isInHearingRange(this, rangeSquared)) {
        let emoji = singletons.getEmojiStore().create(text);
        if (emoji && !stendhal.config.getBoolean("emojis.native")) {
          this.addEmoji(emoji);
          Chat.log("emoji", emoji, this.getTitle());
        } else if (text.startsWith("!me")) {
          Chat.log("emote", text.replace(/^!me/, this.getTitle()));
        } else {
          this.addSpeechBubble(text);
          Chat.log("normal", text, this.getTitle());
        }
      }
    }
    /**
     * Displays a speech bubble attached to an entity.
     *
     * @param text
     *     Text to display.
     */
    addSpeechBubble(text) {
      ViewPort.get().addTextSprite(new SpeechBubble(text, this));
    }
    addEmoji(emoji) {
      ViewPort.get().addEmojiSprite(new EmojiSprite(emoji, this));
    }
    /**
     * Checks if the entity should cast a shadow.
     */
    castsShadow() {
      return typeof this["no_shadow"] === "undefined";
    }
    drawMultipartOutfit(ctx) {
      const store = OutfitStore.get();
      var layers = store.getLayerNames();
      var outfit = {};
      if ("outfit_ext" in this) {
        for (const part of this["outfit_ext"].split(",")) {
          if (part.includes("=")) {
            var tmp = part.split("=");
            outfit[tmp[0]] = parseInt(tmp[1], 10);
          }
        }
      } else {
        layers = store.getLayerNames(true);
        outfit["body"] = this["outfit"] % 100;
        outfit["dress"] = Math.floor(this["outfit"] / 100) % 100;
        outfit["head"] = Math.floor(this["outfit"] / 1e4) % 100;
        outfit["hair"] = Math.floor(this["outfit"] / 1e6) % 100;
        outfit["detail"] = Math.floor(this["outfit"] / 1e8) % 100;
      }
      if (stendhal.config.getBoolean("effect.shadows") && this.castsShadow()) {
        const shadow = htmlImageStore.getShadow("48x64");
        if (shadow && shadow.complete && shadow.height) {
          this.drawSpriteImage(ctx, shadow);
        }
      }
      if (this.octx) {
        this.octx.clearRect(0, 0, this.octx.canvas.width, this.octx.canvas.height);
      }
      if (store.detailHasRearLayer(outfit["detail"])) {
        layers.splice(0, 0, "detail-rear");
        outfit["detail-rear"] = outfit["detail"];
      }
      for (const layer of layers) {
        if (layer == "hair" && !store.drawHair(outfit["hat"])) {
          continue;
        }
        const lsprite = this.getOutfitPart(layer, outfit[layer], outfit["body"]);
        if (lsprite && lsprite.complete && lsprite.height) {
          if (!this.octx) {
            let ocanvas = new OffscreenCanvas(lsprite.width, lsprite.height);
            this.octx = ocanvas.getContext("2d");
          }
          this.octx.drawImage(lsprite, 0, 0);
        }
      }
      if (this.octx) {
        this.drawSpriteImage(ctx, this.octx.canvas);
        this.drawOverlayAnimation(ctx);
      }
    }
    /**
     * Get an outfit part (Image or a Promise)
     *
     * @param {string}  part
     * @param {number} index
     * @param {number} body
     */
    getOutfitPart(part, index, body) {
      if (typeof index === "undefined" || index < 0) {
        return null;
      }
      let n = "" + index;
      if (index < 10) {
        n = "00" + index;
      } else if (index < 100) {
        n = "0" + index;
      }
      let store = OutfitStore.get();
      if (part === "body" && index < 3 && stendhal.config.getBoolean("effect.no-nude")) {
        n += "-nonude";
      } else if (part === "dress" && store.drawBustyDress(index, body)) {
        n += "b";
      } else if (part.endsWith("-rear")) {
        n += "-rear";
        part = part.replace(/-rear$/, "");
      }
      const filename = Paths.sprites + "/outfit/" + part + "/" + n + ".png";
      const colors = this["outfit_colors"];
      let colorname;
      if (store.isSkinLayer(part)) {
        colorname = "skin";
      } else {
        colorname = part;
      }
      if (typeof colors !== "undefined" && typeof colors[colorname] !== "undefined") {
        return htmlImageStore.getFiltered(filename, "trueColor", colors[colorname]);
      } else {
        return htmlImageStore.get(filename);
      }
    }
    /**
     * Sets the offset to keep text & health bar on screen when sprite
     * extends past top edge.
     */
    setStatusBarOffset() {
      const screenOffsetY = ViewPort.get().offsetY;
      const entityBottom = this["_y"] * 32 + this["height"] * 32;
      const entityTop = entityBottom - this["drawHeight"] - HEALTH_BAR_HEIGHT - 26;
      if (screenOffsetY > entityTop && screenOffsetY < entityBottom) {
        this.statusBarYOffset = screenOffsetY - entityTop;
      } else {
        this.statusBarYOffset = 0;
      }
    }
    /**
     * draw RPEntities
     */
    draw(ctx) {
      if (typeof this["ghostmode"] != "undefined" && import_marauroa9.marauroa.me && !import_marauroa9.marauroa.me.isAdmin()) {
        return;
      }
      this.drawCombat(ctx);
      this.drawMain(ctx);
      this.drawAttack(ctx);
      statusIconDrawer.drawStatusIcons(this, ctx);
    }
    drawMain(ctx) {
      if (typeof this["outfit"] != "undefined" || typeof this["outfit_ext"] != "undefined") {
        this.drawMultipartOutfit(ctx);
      } else {
        this.drawSingleOutfit(ctx);
      }
    }
    drawSingleOutfit(ctx) {
      let filename = Paths.sprites + "/" + this.spritePath + "/" + this["class"];
      if (typeof this["subclass"] != "undefined") {
        filename = filename + "/" + this["subclass"];
      }
      if (!stendhal.config.getBoolean("effect.blood") && htmlImageStore.hasSafeImage(filename)) {
        filename = filename + "-safe.png";
      } else {
        filename = filename + ".png";
      }
      let image = htmlImageStore.get(filename);
      if (stendhal.config.getBoolean("effect.shadows") && this.castsShadow()) {
        let shadow_style = this["shadow_style"];
        if (typeof shadow_style === "undefined") {
          shadow_style = image.width / 3 + "x" + image.height / 4;
        }
        const shadow = htmlImageStore.getShadow(shadow_style);
        if (typeof shadow !== "undefined") {
          this.drawSpriteImage(ctx, shadow);
        }
      }
      this.drawSpriteImage(ctx, image);
      this.drawOverlayAnimation(ctx);
    }
    /**
     * Draw colored ellipses (or rectangles on browsers that do not support
     * ellipses) when the entity is being attacked, or is attacking the user.
     */
    drawCombat(ctx) {
      if (this.attackers.size > 0) {
        ctx.lineWidth = 1;
        if (ctx.ellipse instanceof Function) {
          var xRad = this["width"] * 16;
          var yRad = this["height"] * 16 / Math.SQRT2;
          var centerX = this["_x"] * 32 + xRad;
          var centerY = (this["_y"] + this["height"]) * 32 - yRad;
          ctx.strokeStyle = "#4a0000";
          ctx.beginPath();
          ctx.ellipse(centerX, centerY, xRad, yRad, 0, 0, Math.PI, false);
          ctx.stroke();
          ctx.strokeStyle = "#e60a0a";
          ctx.beginPath();
          ctx.ellipse(centerX, centerY, xRad, yRad, 0, Math.PI, 2 * Math.PI, false);
          ctx.stroke();
        } else {
          ctx.strokeStyle = "#e60a0a";
          ctx.strokeRect(32 * this["_x"], 32 * this["_y"], 32 * this["width"], 32 * this["height"]);
        }
      }
      if (this.getAttackTarget() === import_marauroa9.marauroa.me) {
        ctx.lineWidth = 1;
        if (ctx.ellipse instanceof Function) {
          var xRad = this["width"] * 16 - 1;
          var yRad = this["height"] * 16 / Math.SQRT2 - 1;
          var centerX = this["_x"] * 32 + xRad + 1;
          var centerY = (this["_y"] + this["height"]) * 32 - yRad - 1;
          ctx.strokeStyle = "#ffc800";
          ctx.beginPath();
          ctx.ellipse(centerX, centerY, xRad, yRad, 0, 0, Math.PI, false);
          ctx.stroke();
          ctx.strokeStyle = "#ffdd0a";
          ctx.beginPath();
          ctx.ellipse(centerX, centerY, xRad, yRad, 0, Math.PI, 2 * Math.PI, false);
          ctx.stroke();
        } else {
          ctx.strokeStyle = "#ffdd0a";
          ctx.strokeRect(32 * this["_x"] + 1, 32 * this["_y"] + 1, 32 * this["width"] - 2, 32 * this["height"] - 2);
        }
      }
      if (this.attackResult) {
        if (this.attackResult.draw(ctx, (this["_x"] + this["width"]) * 32 - 10, (this["_y"] + this["height"]) * 32 - 10)) {
          this.attackResult = void 0;
        }
      }
    }
    /**
     * Draw entities in this.floaters array. Each floater should have
     * draw(ctx, x, y) method, where ctx is the canvas context, and x, y are
     * the coordinates where to start floating. The method should return true
     * when the floater should be removed.
     */
    drawFloaters(ctx) {
      var centerX = (this["_x"] + this["width"] / 2) * 32;
      var topY = (this["_y"] + 1) * 32 - this["drawHeight"];
      var currentFloaters = this.floaters;
      for (var i = 0; i < currentFloaters.length; i++) {
        var floater = currentFloaters[i];
        if (floater.draw(ctx, centerX, topY)) {
          this.floaters = this.floaters.slice();
          this.floaters.splice(this.floaters.indexOf(floater), 1);
        }
      }
    }
    /**
     * @param {RenderingContext2D} ctx
     * @param {Image} image
     */
    drawSpriteImage(ctx, image) {
      var localX = this["_x"] * 32;
      var localY = this["_y"] * 32;
      if (image.height) {
        var nFrames = 3;
        var nDirections = 4;
        const facing = this.getFaceDirection().val;
        var yRow = facing - 1;
        var frame = 1;
        if (this["class"] == "ent") {
          nFrames = 1;
          nDirections = 2;
          yRow = Math.floor((facing - 1) / 2);
          frame = 0;
        }
        this["drawHeight"] = image.height / nDirections;
        this["drawWidth"] = image.width / nFrames;
        var drawX = (this["width"] * 32 - this["drawWidth"]) / 2;
        if ((this["speed"] > 0 || this.hasOwnProperty("active_idle")) && nFrames != 1) {
          var animLength = nFrames * 2 - 2;
          frame = Math.floor(Date.now() / 100) % animLength;
          if (frame >= nFrames) {
            frame = animLength - frame;
          }
        }
        var drawY = this["height"] * 32 - this["drawHeight"];
        let opacity = parseInt(this["visibility"], 10);
        opacity = isNaN(opacity) ? 100 : opacity;
        if (this.hasOwnProperty("ghostmode") && this === import_marauroa9.marauroa.me && opacity > 50) {
          opacity = 50;
        }
        const opacity_orig = ctx.globalAlpha;
        if (opacity < 100) {
          ctx.globalAlpha = opacity * 0.01;
        }
        this["drawOffsetX"] = localX + drawX;
        this["drawOffsetY"] = localY + drawY;
        ctx.drawImage(
          image,
          frame * this["drawWidth"],
          yRow * this["drawHeight"],
          this["drawWidth"],
          this["drawHeight"],
          this["drawOffsetX"],
          this["drawOffsetY"],
          this["drawWidth"],
          this["drawHeight"]
        );
        ctx.globalAlpha = opacity_orig;
      }
    }
    drawTop(ctx) {
      var localX = this["_x"] * 32;
      var localY = this["_y"] * 32;
      this.drawFloaters(ctx);
      this.drawHealthBar(ctx, localX, localY + this.statusBarYOffset);
      this.drawTitle(ctx, localX, localY + this.statusBarYOffset);
    }
    drawHealthBar(ctx, x, y) {
      var drawX = x + (this["width"] * 32 - this["drawWidth"]) / 2;
      var drawY = y + this["height"] * 32 - this["drawHeight"] - HEALTH_BAR_HEIGHT + this.titleDrawYOffset;
      ctx.strokeStyle = "#000000";
      ctx.lineWidth = 2;
      ctx.beginPath();
      ctx.rect(drawX, drawY, this["drawWidth"], HEALTH_BAR_HEIGHT - 2);
      ctx.stroke();
      ctx.fillStyle = "#808080";
      ctx.fillRect(drawX, drawY, this["drawWidth"], HEALTH_BAR_HEIGHT - 2);
      var hpRatio = this["hp"] / this["base_hp"];
      ctx.fillStyle = Color.getStatBarColor(hpRatio);
      ctx.fillRect(drawX, drawY, this["drawWidth"] * hpRatio, HEALTH_BAR_HEIGHT - 2);
    }
    createTitleTextSprite() {
      let title = this.getTitle();
      if (!title) {
        title = this["_name"];
        if (!title) {
          title = this["class"];
          if (!title) {
            title = this["type"];
          }
        }
      }
      if (title) {
        this.titleTextSprite = new TextSprite(title, this.titleStyle, "14px sans-serif");
      }
    }
    drawTitle(ctx, x, y) {
      if (this.titleTextSprite) {
        let textMetrics = this.titleTextSprite.getTextMetrics(ctx);
        if (!textMetrics) {
          throw new Error("textMetrics is undefined");
        }
        var drawY = y + this["height"] * 32 - this["drawHeight"] - HEALTH_BAR_HEIGHT + this.titleDrawYOffset;
        this.titleTextSprite.draw(ctx, x + (this["width"] * 32 - textMetrics.width) / 2, drawY - 5 - HEALTH_BAR_HEIGHT);
      }
    }
    drawAttack(ctx) {
      if (!this.attackSprite) {
        return;
      }
      if (this.attackSprite.expired()) {
        this.attackSprite.free();
        this.attackSprite = void 0;
        return;
      }
      let viewPort = ViewPort.get();
      var localX = this["_x"] * 32;
      var localY = this["_y"] * 32;
      var localW = this["width"] * viewPort.targetTileWidth;
      var localH = this["height"] * viewPort.targetTileHeight;
      this.attackSprite.draw(ctx, localX, localY, localW, localH);
    }
    /**
     * Draws an animation over entity sprite.
     *
     * @param {RenderingContext2D} ctx
     *   Canvas context to draw on.
     */
    drawOverlayAnimation(ctx) {
      if (this.overlay && this.overlay.draw(
        ctx,
        this["drawOffsetX"],
        this["drawOffsetY"],
        this["drawWidth"],
        this["drawHeight"]
      )) {
        this.overlay.free();
        this.overlay = void 0;
      }
    }
    // attack handling
    getAttackTarget() {
      if (!this._target && this["target"]) {
        this._target = import_marauroa9.marauroa.currentZone[this["target"]];
        if (this._target) {
          this._target.onTargeted(this);
        }
      }
      return this._target;
    }
    onDamaged(_source, damage) {
      this.attackResult = this.createResultIcon(Paths.sprites + "/combat/hitted.png");
      var sounds = ["attack-melee-01", "attack-melee-02", "attack-melee-03", "attack-melee-04", "attack-melee-05", "attack-melee-06", "attack-melee-07"];
      var index = Math.floor(Math.random() * Math.floor(sounds.length));
      stendhal.sound.playLocalizedEffect(this["_x"], this["_y"], 20, 3, sounds[index], 1);
    }
    onBlocked(_source) {
      this.attackResult = this.createResultIcon(Paths.sprites + "/combat/blocked.png");
      var sounds = ["clang-metallic-1", "clang-dull-1"];
      var index = Math.floor(Math.random() * Math.floor(sounds.length));
      stendhal.sound.playLocalizedEffect(this["_x"], this["_y"], 20, 3, sounds[index], 1);
    }
    onMissed(_source) {
      this.attackResult = this.createResultIcon(Paths.sprites + "/combat/missed.png");
    }
    onHPChanged(change) {
      if (change > 0) {
        this.addFloater("+" + change, "#00ff00");
      } else if (change < 0) {
        this.addFloater(change.toString(), "#ff0000");
      }
    }
    onXPChanged(change) {
      if (change > 0) {
        this.addFloater("+" + change, "#4169e1");
        Chat.log("significant_positive", this.getTitle() + " earns " + change + " experience points.");
      } else if (change < 0) {
        this.addFloater(change.toString(), "#ff8f8f");
        Chat.log("significant_negative", this.getTitle() + " loses " + Math.abs(change) + " experience points.");
      }
    }
    onLevelChanged(stat, newlevel, oldlevel) {
      if (!import_marauroa9.marauroa.me || newlevel === oldlevel) {
        return;
      }
      if (import_marauroa9.marauroa.me.isInHearingRange(this)) {
        let msg = this.getTitle();
        let msgtype = "significant_positive";
        if (newlevel > oldlevel) {
          msg += " reaches ";
        } else if (newlevel < oldlevel) {
          msg += " drops to ";
          msgtype = "significant_negative";
        }
        msg += stat + " " + newlevel;
        Chat.logH(msgtype, msg);
      }
    }
    addFloater(message, color) {
      this.floaters.push(new Floater(message, color));
    }
    /**
     * Create a closure for drawing attack result icons. The resulting object
     * has a method draw(context, x, y) which returns true when the attack
     * result has expired and draw() should no longer be called.
     *
     * @param imagePath path to the result icon
     * @return object for drawing the icon
     */
    createResultIcon(imagePath) {
      return {
        initTime: Date.now(),
        image: htmlImageStore.get(imagePath),
        draw: function(ctx, x, y) {
          ctx.drawImage(this.image, x, y);
          return Date.now() - this.initTime > 1200;
        }
      };
    }
    onAttackPerformed(nature, ranged, weapon) {
      if (!ranged && weapon === "ranged") {
        weapon = void 0;
      }
      this.attackSprite?.free();
      if (ranged) {
        this.attackSprite = new RangedAttackSprite(
          this,
          this.getAttackTarget(),
          Nature.VALUES[nature].color,
          weapon
        );
      } else {
        weapon = "blade_strike";
        if (nature == 0) {
          weapon += "_cut";
        }
        const imagePath = Nature.VALUES[nature].getWeaponPath(weapon);
        this.attackSprite = new BarehandAttackSprite(this, imagePath);
      }
    }
    /**
     * Called when this entity is selected as the attack target.
     *
     * @param attacked The entity that selected this as the target
     */
    onTargeted(attacker) {
      if (!(attacker["id"] in this.attackers)) {
        this.attackers[attacker["id"]] = true;
        this.attackers.size += 1;
      }
    }
    /**
     * Called when an entity deselects this as its attack target.
     *
     * @param attacker The entity that had this as the attack target, but
     * 	stopped attacking
     */
    onAttackStopped(attacker) {
      if (attacker["id"] in this.attackers) {
        delete this.attackers[attacker["id"]];
        this.attackers.size -= 1;
      }
    }
    destroy(obj) {
      this._target?.onAttackStopped(this);
      this.overlay?.free();
      super.destroy(obj);
    }
    /**
     * Called when entity's subclass is changed.
     *
     * Does nothing in this implementation.
     */
    onTransformed() {
    }
  };

  // entity/Player.ts
  var Player = class extends RPEntity {
    minimapShow = true;
    minimapStyle = Color.PLAYER;
    dir = 3;
    // shift hp bar & title to avoid overlap with other entities
    titleDrawYOffset = 6;
    constructor() {
      super();
      queueMicrotask(() => {
        this.onEnterZone();
      });
    }
    destroy(parent) {
      this.onExitZone();
      super.destroy(parent);
    }
    set(key, value) {
      super.set(key, value);
      if (key === "ghostmode") {
        this.minimapShow = false;
      } else if (["hp", "base_hp"].indexOf(key) !== -1) {
        this.updateGroupStatus(true);
      }
      if (key === "name" && stendhal.players.indexOf(value) < 0) {
        stendhal.players.push(value);
        stendhal.players = stendhal.players.sort();
      }
    }
    createTitleTextSprite() {
      if (this.isAdmin()) {
        this.titleStyle = "#FFFF00";
      }
      super.createTitleTextSprite();
    }
    /**
     * Is this player an admin?
     */
    isAdmin() {
      return typeof this["adminlevel"] !== "undefined" && this["adminlevel"] > 600;
    }
    buildActions(list) {
      super.buildActions(list);
      var playerName = this["_name"];
      var isUnknown = import_marauroa10.marauroa.me !== this && (import_marauroa10.marauroa.me["buddies"] == null || !(playerName in import_marauroa10.marauroa.me["buddies"]));
      if (isUnknown) {
        list.push({
          title: "Add to buddies",
          action: function(_entity) {
            var action = {
              "type": "addbuddy",
              "zone": import_marauroa10.marauroa.currentZoneName,
              "target": playerName
            };
            import_marauroa10.marauroa.clientFramework.sendAction(action);
          }
        });
      }
      if (this.isIgnored()) {
        list.push({
          title: "Remove ignore",
          action: function(_entity) {
            var action = {
              "type": "unignore",
              "zone": import_marauroa10.marauroa.currentZoneName,
              "target": playerName
            };
            import_marauroa10.marauroa.clientFramework.sendAction(action);
          }
        });
      } else if (isUnknown) {
        list.push({
          title: "Ignore",
          action: function(_entity) {
            var action = {
              "type": "ignore",
              "zone": import_marauroa10.marauroa.currentZoneName,
              "target": playerName
            };
            import_marauroa10.marauroa.clientFramework.sendAction(action);
          }
        });
      }
      if (import_marauroa10.marauroa.me !== this) {
        list.push({
          title: "Trade",
          action: function(_entity) {
            var action = {
              "type": "trade",
              "action": "offer_trade",
              "zone": import_marauroa10.marauroa.currentZoneName,
              "target": playerName
            };
            import_marauroa10.marauroa.clientFramework.sendAction(action);
          }
        });
        if (import_marauroa10.marauroa.me.canInviteToGroup()) {
          list.push({
            title: "Invite",
            action: (_entity) => {
              import_marauroa10.marauroa.clientFramework.sendAction({
                "type": "group_management",
                "action": "invite",
                "params": this["name"]
              });
            }
          });
        }
      }
    }
    isIgnored() {
      if (!import_marauroa10.marauroa.me || !import_marauroa10.marauroa.me["!ignore"]) {
        return false;
      }
      var temp = import_marauroa10.marauroa.me["!ignore"]._objects;
      return temp.length > 0 && "_" + this["_name"] in temp[0];
    }
    onMiniMapDraw() {
      if (this === import_marauroa10.marauroa.me) {
        return;
      }
      if (stendhal.data.group.members[this["name"]]) {
        this.minimapStyle = Color.GROUP;
      } else {
        this.minimapStyle = Color.PLAYER;
      }
    }
    draw(ctx) {
      if (this.isIgnored()) {
        return;
      }
      super.draw(ctx);
    }
    getResistance() {
      if (typeof this["ghostmode"] !== "undefined") {
        return 0;
      }
      return this["resistance"];
    }
    /**
     * says a text
     *
     * @param text
     *     Message contents.
     * @param rangeSquared
     *     Distance squared within which the entity can be heard (-1
     *     represents entire map).
     */
    say(text, rangeSquared) {
      if (this.isIgnored()) {
        return;
      }
      super.say(text, rangeSquared);
    }
    /**
     * Actions to execute when player is removed from zone.
     */
    onExitZone() {
      if (this != import_marauroa10.marauroa.me) {
        this.updateGroupStatus(false);
      }
    }
    /**
     * Actions to execute when player is created in zone.
     */
    onEnterZone() {
      this.updateGroupStatus(true);
    }
    /**
     * Updates group membership HP bar.
     *
     * @param visible {boolean}
     *   If `false`, player's group status is not visible to current user.
     */
    updateGroupStatus(visible) {
      const memberComponent = ui.get(UIComponentEnum.GroupPanel).getMemberComponent(this["name"]);
      if (memberComponent) {
        if (visible) {
          memberComponent.updateHP(this["hp"] / this["base_hp"]);
        } else {
          memberComponent.hideStatus();
        }
      }
    }
    getCursor(_x, _y) {
      if (this.isVisibleToAction(false)) {
        return "url(" + Paths.sprites + "/cursor/look.png) 1 3, auto";
      }
      return "url(" + Paths.sprites + "/cursor/walk.png) 1 3, auto";
    }
  };

  // entity/Ground.ts
  var import_marauroa11 = __toESM(require_marauroa());
  var Ground = class {
    isVisibleToAction(filter) {
      return false;
    }
    /**
     * Calculates whether the click was close enough to a zone border to trigger
     * a zone change.
     *
     * @param x x of click point in world coordinates
     * @param y y of click point in world coordinates
     * @return Direction of the zone to change to, <code>null</code> if no zone change should happen
     */
    calculateZoneChangeDirection(x, y) {
      let map = TileMap.get();
      if (x < 15) {
        return "4";
      }
      if (x > map.zoneSizeX * 32 - 15) {
        return "2";
      }
      if (y < 15) {
        return "1";
      }
      if (y > map.zoneSizeY * 32 - 15) {
        return "3";
      }
      return null;
    }
    getCursor(x, y) {
      let map = TileMap.get();
      if (x < 15 || y < 15 || x > map.zoneSizeX * 32 - 15 || y > map.zoneSizeY * 32 - 15) {
        return "url(" + Paths.sprites + "/cursor/walkborder.png) 1 3, auto";
      }
      var worldX = Math.floor(x / 32);
      var worldY = Math.floor(y / 32);
      if (map.collision(worldX, worldY)) {
        return "url(" + Paths.sprites + "/cursor/stop.png) 1 3, auto";
      }
      return "url(" + Paths.sprites + "/cursor/walk.png) 1 3, auto";
    }
    onclick(x, y, dblclick) {
      if (!stendhal.config.getBoolean("pathfinding")) {
        return;
      }
      let viewPort = ViewPort.get();
      var gameX = x + viewPort.offsetX;
      var gameY = y + viewPort.offsetY;
      var action = {
        "type": "moveto",
        "x": "" + Math.floor(gameX / 32),
        "y": "" + Math.floor(gameY / 32)
      };
      if (typeof dblclick == "boolean" && dblclick) {
        action["double_click"] = "";
      }
      var extend = this.calculateZoneChangeDirection(gameX, gameY);
      if (extend) {
        action.extend = extend;
      }
      import_marauroa11.marauroa.me.moveTo(action);
    }
  };

  // entity/Zone.ts
  var Zone = class _Zone {
    /** Entities found in this zone. */
    entities = [];
    /** Singleton instance. */
    static instance;
    ground = new Ground();
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_Zone.instance) {
        _Zone.instance = new _Zone();
      }
      return _Zone.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    getEntitiesAt(x, y, filter) {
      const xGrid = x / 32;
      const yGrid = y / 32;
      const entities = [];
      for (const i in this.entities) {
        const obj = this.entities[i];
        if (obj.isVisibleToAction(filter) && obj["_x"] <= xGrid && obj["_y"] <= yGrid && obj["_x"] + (obj["width"] || 1) >= xGrid && obj["_y"] + (obj["height"] || 1) >= yGrid) {
          entities.push(obj);
        }
      }
      return entities;
    }
    entityAt(x, y, filter) {
      let res = _Zone.get().ground;
      for (const obj of this.getEntitiesAt(x, y, filter)) {
        res = obj;
      }
      if (res != _Zone.get().ground) {
        return res;
      }
      for (var i in this.entities) {
        let obj = this.entities[i];
        if (!obj.isVisibleToAction(filter) || !obj["drawHeight"]) {
          continue;
        }
        let localX = obj["_x"] * 32;
        let localY = obj["_y"] * 32;
        let drawHeight = obj["drawHeight"];
        let drawWidth = obj["drawWidth"];
        var drawX = (obj["width"] * 32 - drawWidth) / 2;
        var drawY = obj["height"] * 32 - drawHeight;
        if (localX + drawX <= x && localX + drawX + drawWidth >= x && localY + drawY <= y && localY + drawY + drawHeight >= y) {
          res = obj;
        }
      }
      return res;
    }
    sortEntities() {
      this.entities = [];
      for (var i in import_marauroa12.marauroa.currentZone) {
        if (import_marauroa12.marauroa.currentZone.hasOwnProperty(i) && typeof import_marauroa12.marauroa.currentZone[i] != "function") {
          this.entities.push(import_marauroa12.marauroa.currentZone[i]);
        }
      }
      this.entities.sort(function(entity1, entity2) {
        var rv = entity1.zIndex - entity2.zIndex;
        if (rv == 0) {
          rv = entity1.y + entity1.height - (entity2.y + entity2.height);
          if (rv == 0) {
            rv = entity1.id - entity2.id;
          }
        }
        return rv;
      });
    }
    /**
     * Finds player instance in zone.
     *
     * @param name {string}
     *   Player name to search for.
     * @return {Player}
     *   Player instance or `undefined`.
     */
    findPlayer(name) {
      for (const ent of this.entities) {
        if (ent instanceof Player && ent["name"] === name) {
          return ent;
        }
      }
    }
  };

  // data/TileStore.ts
  var TileStore = class _TileStore {
    DEFAULT_DELAY = 500;
    landscapeMap;
    weatherMap;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!this.instance) {
        this.instance = new _TileStore();
      }
      return this.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    getLandscapeMap() {
      return this.landscapeMap || {};
    }
    getWeatherMap() {
      return this.weatherMap || {};
    }
    /**
     * Initializes landscape & weather tile animations.
     */
    init() {
      if (this.landscapeMap && this.weatherMap) {
        console.warn("tried to re-initialize tile animations");
        return;
      }
      const loader = new JSONLoader();
      loader.onDataReady = () => {
        this.landscapeMap = this.loadAnimations(
          loader.data["landscape"],
          Paths.tileset + "/"
        );
        this.weatherMap = this.loadAnimations(
          loader.data["weather"],
          Paths.weather + "/"
        );
      };
      loader.load(Paths.tileset + "/animation.json");
    }
    /**
     * Animations are stored using the tileset name as key with value
     * being a map indexed by initial frames. Frame map contains two
     * lists: <code>frames</code> (a list of frames used in animation)
     * & <code>delays</code> (a list of delay values for the
     * corresponding frame of each index).
     *
     * ani = animationMap[tileset_name];
     * frames = ani[0].frames
     * delays = ani[0].delays
     *
     * @param def
     *     Unformatted animations lists indexed by tileset name.
     *     Example: {ts1: [ani1, ani2, ...], ts2: [ani1, ani2, ...], ...}
     * @param prefix
     *     Parent directory containing the target tileset images.
     * @return
     *     Map of animations.
     */
    loadAnimations(def, prefix) {
      const ani = {};
      for (const tsname of Object.keys(def)) {
        const entry = {};
        for (let li of def[tsname]) {
          li = li.trim();
          li = li.replace(/\t/g, " ");
          li = li.replace(/  /g, " ");
          li = li.split(" ");
          if (li.length > 1) {
            let id = li[0];
            let delay = this.DEFAULT_DELAY;
            if (id.includes("@")) {
              const idtemp = id.split("@");
              id = idtemp[0];
              if (idtemp.length > 1) {
                delay = parseInt(idtemp[1], 10);
              } else {
                delay = this.DEFAULT_DELAY;
              }
            }
            const frames = [];
            const delays = [];
            for (let frame of li[1].split(":")) {
              if (frame.includes("@")) {
                const ftemp = frame.split("@");
                delay = parseInt(ftemp[1], 10);
                frame = ftemp[0];
              }
              frame = parseInt(frame, 10);
              frames.push(frame);
              delays.push(delay);
            }
            let first_frame = frames[0];
            if (id !== "*") {
              first_frame = parseInt(id, 10);
            }
            entry[first_frame] = {
              frames,
              delays
            };
          }
        }
        ani[prefix + tsname + ".png"] = entry;
      }
      return ani;
    }
  };

  // util/WeatherRenderer.ts
  var weatherLoops = {
    "rain": true,
    "rain_heavy": true,
    "rain_light": true
  };
  var WeatherRenderer = class _WeatherRenderer {
    enabled = true;
    sprite;
    frameIdx = 0;
    lastUpdate = 0;
    tilesX = 0;
    tilesY = 0;
    audio;
    soundLayer = stendhal.sound.layers.indexOf("ambient");
    weatherName;
    /** Special handling for fog animation. */
    fog;
    heavyFog;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     *
     * @return
     *     WeatherRenderer static instance.
     */
    static get() {
      if (!_WeatherRenderer.instance) {
        _WeatherRenderer.instance = new _WeatherRenderer();
      }
      return _WeatherRenderer.instance;
    }
    /**
     * Hidden singleton constructor.
     *
     * Use <code>WeatherRenderer.get()</code>.
     */
    constructor() {
      this.fog = false;
      this.heavyFog = false;
    }
    /**
     * Called when zone is updated to configure the type of weather.
     *
     * @param weather
     *     Weather type identifier.
     */
    async update(weather) {
      this.enabled = stendhal.config.getBoolean("effect.weather");
      if (!this.enabled) {
        return;
      }
      this.heavyFog = weather === "fog_heavy";
      this.fog = this.heavyFog || weather === "fog";
      if (this.fog) {
        weather = "fog_ani";
      }
      this.weatherName = weather;
      this.frameIdx = 0;
      this.lastUpdate = Date.now();
      if (this.audio) {
        stendhal.sound.stop(this.soundLayer, this.audio);
        this.audio = void 0;
      }
      if (!weather) {
        this.sprite = void 0;
      } else {
        const img = Paths.weather + "/" + weather + ".png";
        let sprite = images.load(img);
        await sprite.waitFor();
        if (weather != this.weatherName || !sprite.image) {
          sprite.free();
          return;
        }
        const animationMap = TileStore.get().getWeatherMap()[img];
        if (animationMap && Object.keys(animationMap).length == 0) {
          console.error("weather animation map for '" + weather + "' is empty");
          return;
        }
        if (animationMap) {
          sprite.frames = animationMap[0].frames;
          sprite.delays = animationMap[0].delays;
        } else {
          sprite.frames = [0];
        }
        let spriteH = sprite.image.height;
        const canvas = document.getElementById("viewport");
        this.tilesX = Math.ceil(canvas.width / spriteH) + 1;
        this.tilesY = Math.ceil(canvas.height / spriteH) + 1;
        if (weatherLoops[weather]) {
          this.audio = stendhal.sound.playGlobalizedLoop("weather/" + weather, this.soundLayer);
        }
        this.sprite = sprite;
      }
    }
    /**
     * Draws the weather animation.
     *
     * @param ctx
     *    Drawing target element.
     */
    draw(ctx) {
      if (!this.enabled || !this.sprite || !this.sprite.frames) {
        return;
      }
      let viewPort = ViewPort.get();
      if (this.weatherName === "clouds") {
        ctx.save();
        ctx.globalAlpha = 0.8;
        this.drawClouds(ctx, viewPort.offsetX, viewPort.offsetY);
        ctx.restore();
      } else if (this.fog) {
        this.drawFog(ctx, viewPort.offsetX, viewPort.offsetY);
      } else {
        this.drawOther(ctx, viewPort.offsetX, viewPort.offsetY);
      }
    }
    /**
     * Draws clouds animation.
     *
     * @param {RenderingContext2D) ctx
     * @param {number} offsetX
     * @param {number} offsetY
     */
    drawClouds(ctx, offsetX, offsetY) {
      let image = this.sprite?.image;
      if (!image) {
        return;
      }
      const drawStart = Date.now();
      const timeDiff = drawStart - this.lastUpdate;
      const dim = { width: image.width, height: image.height };
      let wind = Math.floor(timeDiff / 100);
      if (wind >= dim.width) {
        wind = 0;
        this.lastUpdate = drawStart;
      }
      const driftX = offsetX - Math.floor(offsetX * 0.25);
      const driftY = offsetY - Math.floor(offsetY * 0.25);
      const clipLeft = offsetX - offsetX % dim.width + driftX + wind;
      const clipTop = offsetY - offsetY % dim.height + driftY;
      for (let dy = -clipTop; dy < offsetY + ctx.canvas.height; dy += dim.height) {
        for (let dx = -clipLeft; dx < offsetX + ctx.canvas.width; dx += dim.width) {
          ctx.drawImage(
            image,
            0,
            0,
            dim.width,
            dim.height,
            dx,
            dy,
            dim.width,
            dim.height
          );
        }
      }
    }
    /**
     * Draws fog animation.
     *
     * @param {RenderingContext2D) ctx
     * @param {number} offsetX
     * @param {number} offsetY
     */
    drawFog(ctx, offsetX, offsetY) {
      ctx.save();
      if (!this.heavyFog) {
        ctx.globalAlpha = 0.5;
      }
      this.drawClouds(ctx, offsetX, offsetY);
      ctx.restore();
    }
    /**
     * Draws types of weather other than fog.
     *
     * @param {RenderingContext2D) ctx
     * @param {number} offsetX
     * @param {number} offsetY
     */
    drawOther(ctx, offsetX, offsetY) {
      let image = this.sprite?.image;
      if (!image) {
        return;
      }
      const dim = image.height;
      const clipLeft = offsetX % dim;
      const clipTop = offsetY % dim;
      for (let ix = 0; ix < this.tilesX; ix++) {
        for (let iy = 0; iy < this.tilesY; iy++) {
          ctx.drawImage(
            image,
            this.sprite.frames[this.frameIdx] * dim,
            0,
            dim,
            dim,
            ix * dim + offsetX - clipLeft,
            iy * dim + offsetY - clipTop,
            dim,
            dim
          );
        }
      }
      if (this.sprite.delays) {
        const cycleTime = Date.now();
        const elapsed = cycleTime - this.lastUpdate;
        if (elapsed >= this.sprite.delays[this.frameIdx]) {
          this.lastUpdate = cycleTime;
          this.frameIdx = this.getNextFrame(elapsed);
        }
      }
    }
    /**
     * Calculates the next frame to display incorporating frame skipping.
     *
     * @param elapsed
     *     Amount of time (ms) that has elapsed.
     * @return
     *     The next frame index that should be drawn.
     */
    getNextFrame(elapsed) {
      let delayComb = 0, idx = this.frameIdx;
      for (idx; delayComb < elapsed; idx++) {
        delayComb += this.sprite.delays[idx];
        if (idx + 1 >= this.sprite.delays.length) {
          idx = -1;
        }
      }
      return idx;
    }
  };

  // ui/TouchHandler.ts
  var TouchHandler = class _TouchHandler {
    /** Property denoting a touch is engaged. */
    touchEngaged = false;
    /** Threshold determining if time between touch start & touch end represents a "long" touch. */
    longTouchDuration = 300;
    /** Time at which touch was engaged. */
    timestampTouchStart = 0;
    /** Time at which touch was released. */
    timestampTouchEnd = 0;
    /** Property denoting an object is being "held". */
    held = false;
    /** Position on page when touch event began. */
    origin;
    /** Number of pixels touch can move before being vetoed as a long touch or double tap. */
    moveThreshold = 16;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_TouchHandler.instance) {
        _TouchHandler.instance = new _TouchHandler();
      }
      return _TouchHandler.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Checks for touch event.
     *
     * @param evt {Event}
     *   Event to be checked.
     * @return {boolean}
     *   `true` if "evt" represents a `TouchEvent`.
     */
    isTouchEvent(evt) {
      return window["TouchEvent"] && evt instanceof TouchEvent;
    }
    /**
     * Sets timestamp when touch applied.
     *
     * @param x {number}
     *   Touch position relative to page on X axis.
     * @param y {number}
     *   Touch position relative to page on Y axis.
     */
    onTouchStart(x, y) {
      this.timestampTouchStart = +/* @__PURE__ */ new Date();
      this.touchEngaged = true;
      this.origin = new Point(x, y);
    }
    /**
     * Sets timestamp when touch released.
     */
    onTouchEnd() {
      this.timestampTouchEnd = +/* @__PURE__ */ new Date();
      this.touchEngaged = false;
    }
    /**
     * Can be used to detect if a mouse event was triggered by touch.
     *
     * @return {boolean}
     *   Value of `ui.TouchHandler.TouchHandler.touchEngaged` property.
     */
    isTouchEngaged() {
      return this.touchEngaged;
    }
    /**
     * Checks if a touch event represents a long touch after release.
     *
     * @param evt {Event}
     *   Event object to be checked.
     * @return {boolean}
     *   `true` if touch was released after duration threshold.
     */
    isLongTouch(evt) {
      if (evt && !this.isTouchEvent(evt)) {
        return false;
      }
      const durationMatch = this.timestampTouchEnd - this.timestampTouchStart > this.longTouchDuration;
      let positionMatch = true;
      if (evt && this.origin) {
        const pos = HTMLUtil.extractPosition(evt);
        positionMatch = Math.abs(pos.pageX - this.origin.x) <= this.moveThreshold && Math.abs(pos.pageY - this.origin.y) <= this.moveThreshold;
      }
      return durationMatch && positionMatch;
    }
    /**
     * Unsets `ui.TouchHandler.TouchHandler.origin` property.
     */
    unsetOrigin() {
      this.origin = void 0;
    }
    /**
     * Sets `ui.TouchHandler.TouchHandler.held` property.
     */
    setHolding(held) {
      this.held = held;
    }
    /**
     * Checks if an held object was initiated using touch.
     */
    holding() {
      return this.held;
    }
  };

  // ui/ViewPort.ts
  var ViewPort = class _ViewPort {
    /** Horizontal screen offset in pixels. */
    offsetX = 0;
    /** Vertical screen offset in pixels. */
    offsetY = 0;
    /** Prevents adjusting offset based on player position. */
    freeze = false;
    /** Time of most recent redraw. */
    timeStamp = Date.now();
    // dimensions
    // TODO: remove & use CSS style instead
    width;
    height;
    /** Drawing context. */
    ctx;
    /** Map tile pixel width. */
    targetTileWidth = 32;
    /** Map tile pixel height. */
    targetTileHeight = 32;
    drawingError = false;
    /** Active speech bubbles to draw. */
    textSprites = [];
    /** Active notification bubbles/achievement banners to draw. */
    notifSprites = [];
    /** Active emoji sprites to draw. */
    emojiSprites = [];
    /** Handles drawing weather in viewport. */
    weatherRenderer = WeatherRenderer.get();
    /** Coloring method of current zone. */
    filter;
    // deprecated, use `HSLFilter`
    /** Coloring filter of current zone. */
    HSLFilter;
    colorMethod = "";
    blendMethod = "";
    // FIXME: unused
    map;
    /** Styles to be applied when chat panel is not floating. */
    initialStyle;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_ViewPort.instance) {
        _ViewPort.instance = new _ViewPort();
      }
      return _ViewPort.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
      this.map = TileMap.get();
      const element = this.getElement();
      this.ctx = element.getContext("2d");
      this.width = element.width;
      this.height = element.height;
      this.initialStyle = {};
      this.initialStyle["max-width"] = "calc((100dvh - 5em) * 640 / 480)";
      this.initialStyle["max-height"] = "calc(100dvh - 5em)";
    }
    /**
     * Retrieves the viewport element.
     *
     * @return {HTMLCanvasElement}
     *   Viewport `HTMLElement`.
     */
    getElement() {
      return document.getElementById("viewport");
    }
    /**
     * Draws terrain tiles & entity sprites in the viewport.
     */
    draw() {
      var startTime = (/* @__PURE__ */ new Date()).getTime();
      if (import_marauroa13.marauroa.me && document.visibilityState === "visible") {
        if (import_marauroa13.marauroa.currentZoneName === this.map.currentZoneName || this.map.currentZoneName === "int_vault" || this.map.currentZoneName === "int_adventure_island" || this.map.currentZoneName === "tutorial_island") {
          this.drawingError = false;
          this.ctx.globalAlpha = 1;
          this.adjustView(this.ctx.canvas);
          this.ctx.fillStyle = "black";
          this.ctx.fillRect(0, 0, 1e4, 1e4);
          var tileOffsetX = Math.floor(this.offsetX / this.targetTileWidth);
          var tileOffsetY = Math.floor(this.offsetY / this.targetTileHeight);
          this.map.parallax.draw(this.ctx, this.offsetX, this.offsetY);
          this.map.strategy.render(this.ctx.canvas, this, tileOffsetX, tileOffsetY, this.targetTileWidth, this.targetTileHeight);
          this.weatherRenderer.draw(this.ctx);
          if (!Debug.isActive("light")) {
            this.applyHSLFilter();
          }
          this.drawEntitiesTop();
          this.drawEmojiSprites();
          this.drawTextSprites();
          this.drawTextSprites(this.notifSprites);
          stendhal.ui.equip.update();
          ui.get(UIComponentEnum.PlayerEquipment).update();
        }
      }
      window.setTimeout(() => {
        this.draw();
      }, Math.max(1e3 / 20 - ((/* @__PURE__ */ new Date()).getTime() - startTime), 1));
    }
    /**
     * Adds map's coloring filter to viewport.
     *
     * FIXME:
     * - colors are wrong
     * - doesn't support "blend" layers
     * - very slow
     *
     * @deprecated
     */
    applyFilter() {
      if (this.filter && stendhal.config.getBoolean("effect.lighting")) {
        this.ctx.filter = this.filter;
      }
    }
    /**
     * Removes map's coloring filter from viewport.
     *
     * @deprecated
     */
    removeFilter() {
      this.ctx.filter = "none";
    }
    /**
     * Add coloring filter to viewport.
     */
    applyHSLFilter() {
      if (!this.HSLFilter) {
        return;
      }
      this.ctx.save();
      this.ctx.globalCompositeOperation = this.colorMethod || this.ctx.globalCompositeOperation;
      this.ctx.fillStyle = this.HSLFilter;
      this.ctx.fillRect(this.offsetX, this.offsetY, this.ctx.canvas.width, this.ctx.canvas.height);
      this.ctx.restore();
    }
    /**
     * Sets color method used for current zone.
     *
     * @param method {string}
     *   Color method.
     */
    setColorMethod(method) {
      switch (method) {
        case "softlight":
          method = "soft-light";
          break;
      }
      const known = [
        "source-over",
        "source-in",
        "source-out",
        "source-atop",
        "destination-over",
        "destination-in",
        "destination-out",
        "destination-atop",
        "lighter",
        "copy",
        "xor",
        "multiply",
        "screen",
        "overlay",
        "darken",
        "lighten",
        "color-dodge",
        "color-burn",
        "hard-light",
        "soft-light",
        "difference",
        "exclusion",
        "hue",
        "saturation",
        "color",
        "luminosity"
      ];
      if (known.indexOf(method) < 0) {
        console.warn("Unknown color method:", method);
        return;
      }
      this.colorMethod = method;
    }
    /**
     * Sets blend method used for current zone.
     *
     * @param method {string}
     *   Blend method.
     */
    setBlendMethod(method) {
      this.blendMethod = method;
    }
    /**
     * Draws overall entity sprites.
     */
    drawEntities() {
      let currentTime = (/* @__PURE__ */ new Date()).getTime();
      let time = currentTime - this.timeStamp;
      this.timeStamp = currentTime;
      for (let entity of Zone.get().entities) {
        if (typeof entity.draw != "undefined") {
          entity.updatePosition(time);
          entity.draw(this.ctx);
        }
      }
    }
    /**
     * Draws titles & HP bars associated with entities.
     */
    drawEntitiesTop() {
      for (let entity of Zone.get().entities) {
        if (typeof entity.setStatusBarOffset !== "undefined") {
          entity.setStatusBarOffset();
        }
        if (typeof entity.drawTop != "undefined") {
          entity.drawTop(this.ctx);
        }
      }
    }
    /**
     * Draws active notifications or speech bubbles associated with characters, NPCs, & creatures.
     *
     * @param sgroup {sprite.TextBubble[]}
     *   Sprite group to drawn, either speech bubbles or notifications/achievements (default: speech bubbles).
     */
    drawTextSprites(sgroup = this.textSprites) {
      for (var i = 0; i < sgroup.length; i++) {
        var sprite = sgroup[i];
        var remove = sprite.draw(this.ctx);
        if (remove) {
          sgroup.splice(i, 1);
          sprite.onRemoved();
          i--;
        }
      }
    }
    /**
     * Adds an emoji sprite to viewport.
     *
     * @param sprite {sprite.EmojiSprite}
     *   Sprite definition.
     */
    addEmojiSprite(sprite) {
      this.emojiSprites.push(sprite);
    }
    /**
     * Draws active emoji sprites.
     */
    drawEmojiSprites() {
      for (let i = 0; i < this.emojiSprites.length; i++) {
        const sprite = this.emojiSprites[i];
        const remove = sprite.draw(this.ctx);
        if (remove) {
          this.emojiSprites.splice(i, 1);
          i--;
        }
      }
    }
    /**
     * Updates viewport drawing position of map based on player position.
     *
     * @param canvas {Canvas}
     *   Viewport canvas element.
     */
    adjustView(canvas) {
      this.ctx.setTransform(1, 0, 0, 1, 0, 0);
      let centerX, centerY;
      if (this.freeze) {
        centerX = this.offsetX + this.targetTileWidth / 2;
        centerY = this.offsetY + this.targetTileHeight / 2;
      } else {
        centerX = import_marauroa13.marauroa.me["_x"] * this.targetTileWidth + this.targetTileWidth / 2 - canvas.width / 2;
        centerY = import_marauroa13.marauroa.me["_y"] * this.targetTileHeight + this.targetTileHeight / 2 - canvas.height / 2;
      }
      centerX = Math.min(centerX, this.map.zoneSizeX * this.targetTileWidth - canvas.width);
      centerX = Math.max(centerX, 0);
      centerY = Math.min(centerY, this.map.zoneSizeY * this.targetTileHeight - canvas.height);
      centerY = Math.max(centerY, 0);
      if (this.freeze) {
        this.ctx.translate(-Math.round(centerX), -Math.round(centerY));
        return;
      }
      this.offsetX = Math.round(centerX);
      this.offsetY = Math.round(centerY);
      this.ctx.translate(-this.offsetX, -this.offsetY);
    }
    /**
     * Adds a speech bubble to viewport.
     *
     * @param sprite {sprite.SpeechBubble}
     *   Sprite definition.
     */
    addTextSprite(sprite) {
      this.textSprites.push(sprite);
      sprite.onAdded(this.ctx);
    }
    /**
     * Adds a notification bubble to viewport.
     *
     * @param mtype {string}
     *   Message type.
     * @param text {string}
     *   Text contents.
     * @param profile {string}
     *   Optional entity image filename to show as the speaker.
     */
    addNotifSprite(mtype, text, profile) {
      const bubble = new NotificationBubble(mtype, text, profile);
      this.notifSprites.push(bubble);
      bubble.onAdded(this.ctx);
    }
    /**
     * Adds an achievement banner to viewport.
     *
     * @param cat {string}
     *   Achievement categroy.
     * @param title {string}
     *   Achievement title.
     * @param desc {string}
     *   Achievement description.
     */
    addAchievementNotif(cat, title, desc) {
      const banner = new AchievementBanner(cat, title, desc);
      this.notifSprites.push(banner);
      banner.onAdded(this.ctx);
    }
    /**
     * Removes a speech bubble. Looks for topmost sprite at "x","y". Otherwise removes "sprite".
     *
     * @param sprite {sprite.TextBubble}
     *   Sprite that is to be removed.
     * @param x {number}
     *   X coordinate to check for overlapping sprite.
     * @param y {number}
     *   Y coordinate to check for overlapping sprite.
     */
    removeTextBubble(sprite, x, y) {
      for (let idx = this.notifSprites.length - 1; idx >= 0; idx--) {
        const topSprite = this.notifSprites[idx];
        if (topSprite == sprite || topSprite.clipsPoint(x, y)) {
          this.notifSprites.splice(idx, 1);
          topSprite.onRemoved();
          return;
        }
      }
      for (let idx = this.textSprites.length - 1; idx >= 0; idx--) {
        const topSprite = this.textSprites[idx];
        if (topSprite == sprite || topSprite.clipsPoint(x, y)) {
          this.textSprites.splice(idx, 1);
          topSprite.onRemoved();
          return;
        }
      }
    }
    /**
     * Checks for an active speech bubble.
     *
     * @param x {number}
     *   X coordinate to check.
     * @param y {number}
     *   Y coordinate to check.
     * @return {boolean}
     *   `true` if there is a text bubble at position.
     */
    textBubbleAt(x, y) {
      for (const sprite of this.notifSprites) {
        if (sprite.clipsPoint(x, y)) {
          return true;
        }
      }
      for (const sprite of this.textSprites) {
        if (sprite.clipsPoint(x, y)) {
          return true;
        }
      }
      return false;
    }
    /**
     * Called when `entity.User` instance exits a zone.
     */
    onExitZone() {
      for (const sgroup of [this.textSprites, this.emojiSprites]) {
        for (let idx = sgroup.length - 1; idx >= 0; idx--) {
          const sprite = sgroup[idx];
          sgroup.splice(idx, 1);
          if (sprite instanceof SpeechBubble) {
            sprite.onRemoved();
          }
        }
      }
    }
    /**
     *  Mouse click handling.
     */
    onMouseDown = (function() {
      var entity;
      var startX;
      var startY;
      const mHandle = {};
      mHandle._onMouseDown = function(e) {
        let touchHandler = TouchHandler.get();
        var pos = HTMLUtil.extractPosition(e);
        if (touchHandler.isTouchEvent(e)) {
          if (touchHandler.holding()) {
            return;
          }
          touchHandler.onTouchStart(pos.pageX, pos.pageY);
        }
        if (stendhal.ui.globalpopup) {
          stendhal.ui.globalpopup.close();
        }
        startX = pos.canvasRelativeX;
        startY = pos.canvasRelativeY;
        let viewPort = _ViewPort.get();
        var x = pos.canvasRelativeX + viewPort.offsetX;
        var y = pos.canvasRelativeY + viewPort.offsetY;
        if (viewPort.textBubbleAt(x, y + 15)) {
          return;
        }
        let zone = Zone.get();
        entity = zone.entityAt(x, y);
        stendhal.ui.timestampMouseDown = +/* @__PURE__ */ new Date();
        if (e.type !== "dblclick" && e.target) {
          e.target.addEventListener("mousemove", mHandle.onDrag, { passive: true });
          e.target.addEventListener("mouseup", mHandle.onMouseUp);
          e.target.addEventListener("touchmove", mHandle.onDrag, { passive: true });
          e.target.addEventListener("touchend", mHandle.onMouseUp);
        } else if (entity == zone.ground) {
          entity.onclick(pos.canvasRelativeX, pos.canvasRelativeY, true);
        }
      };
      mHandle.isRightClick = function(e) {
        if (+/* @__PURE__ */ new Date() - stendhal.ui.timestampMouseDown > 300) {
          return true;
        }
        if (e.which) {
          return e.which === 3;
        } else {
          return e.button === 2;
        }
      };
      mHandle.onMouseUp = function(e) {
        let touchHandler = TouchHandler.get();
        const is_touch = touchHandler.isTouchEvent(e);
        if (is_touch) {
          touchHandler.onTouchEnd();
        }
        var pos = HTMLUtil.extractPosition(e);
        const long_touch = is_touch && touchHandler.isLongTouch(e);
        if (e instanceof MouseEvent && mHandle.isRightClick(e) || long_touch) {
          if (entity != Zone.get().ground) {
            const append = [];
            stendhal.ui.actionContextMenu.set(ui.createSingletonFloatingWindow(
              "Action",
              new ActionContextMenu(entity, append),
              pos.pageX - 50,
              pos.pageY - 5
            ));
          }
        } else {
          entity.onclick(pos.canvasRelativeX, pos.canvasRelativeY);
        }
        mHandle.cleanUp(pos);
        pos.target.focus();
        e.preventDefault();
      };
      mHandle.onDrag = function(e) {
        if (TouchHandler.get().isTouchEvent(e)) {
          _ViewPort.get().onDragStart(e);
        }
        var pos = HTMLUtil.extractPosition(e);
        var xDiff = startX - pos.offsetX;
        var yDiff = startY - pos.offsetY;
        if (xDiff * xDiff + yDiff * yDiff > 5) {
          mHandle.cleanUp(e);
        }
      };
      mHandle.cleanUp = function(e) {
        entity = null;
        if (!e.target) {
          return;
        }
        e.target.removeEventListener("mouseup", mHandle.onMouseUp);
        e.target.removeEventListener("mousemove", mHandle.onDrag);
        e.target.removeEventListener("touchend", mHandle.onMouseUp);
        e.target.removeEventListener("touchmove", mHandle.onDrag);
      };
      return mHandle._onMouseDown;
    })();
    /**
     * Updates cursor style when positioned over an element or entity.
     */
    onMouseMove(e) {
      let viewPort = _ViewPort.get();
      var pos = HTMLUtil.extractPosition(e);
      var x = pos.canvasRelativeX + viewPort.offsetX;
      var y = pos.canvasRelativeY + viewPort.offsetY;
      var entity = Zone.get().entityAt(x, y);
      viewPort.getElement().style.cursor = entity.getCursor(x, y);
    }
    /**
     * Changes character facing direction dependent on direction of wheel scroll.
     */
    onMouseWheel(e) {
      if (import_marauroa13.marauroa.me) {
        e.preventDefault();
        const currentDir = parseInt(import_marauroa13.marauroa.me["dir"], 10);
        let newDir = null;
        if (typeof currentDir === "number") {
          if (e.deltaY >= 100) {
            newDir = currentDir + 1;
            if (newDir > 4) {
              newDir = 1;
            }
          } else if (e.deltaY <= -100) {
            newDir = currentDir - 1;
            if (newDir < 1) {
              newDir = 4;
            }
          }
        }
        if (newDir != null) {
          import_marauroa13.marauroa.clientFramework.sendAction({ "type": "face", "dir": "" + newDir });
        }
      }
    }
    /**
     * Handles engaging an item or corpse to be dragged.
     */
    onDragStart(e) {
      var pos = HTMLUtil.extractPosition(e);
      let draggedEntity;
      let viewPort = _ViewPort.get();
      let entries = Zone.get().getEntitiesAt(
        pos.canvasRelativeX + viewPort.offsetX,
        pos.canvasRelativeY + viewPort.offsetY
      );
      for (const obj of entries) {
        if (obj.isDraggable()) {
          draggedEntity = obj;
        }
      }
      if (!draggedEntity || !HeldObjectManager.get().prepare(draggedEntity, e)) {
        e.preventDefault();
        return;
      }
      let heldObject = {
        path: draggedEntity.getIdPath(),
        zone: import_marauroa13.marauroa.currentZoneName,
        quantity: draggedEntity.hasOwnProperty("quantity") ? draggedEntity["quantity"] : 1
      };
      let touchHandler = TouchHandler.get();
      if (touchHandler.isTouchEvent(e)) {
        HeldObjectManager.get().set(heldObject, new Point(pos.pageX, pos.pageY));
        touchHandler.setHolding(true);
      } else {
        HeldObjectManager.get().heldObject = heldObject;
      }
    }
    /**
     * Displays a corpse or item sprite while dragging.
     */
    onDragOver(e) {
      e.preventDefault();
      if (e.dataTransfer) {
        e.dataTransfer.dropEffect = "move";
      }
      return false;
    }
    /**
     * Handles releasing an item or corpse from drag event.
     */
    onDrop(e) {
      let heldObject = HeldObjectManager.get().heldObject;
      if (heldObject) {
        var pos = HTMLUtil.extractPosition(e);
        const targetSlot = HTMLUtil.parseSlotName(pos.target.id);
        const action = {
          "zone": heldObject.zone
        };
        if (targetSlot === "viewport") {
          let viewPort = _ViewPort.get();
          action.x = Math.floor((pos.canvasRelativeX + viewPort.offsetX) / 32).toString();
          action.y = Math.floor((pos.canvasRelativeY + viewPort.offsetY) / 32).toString();
          var id = heldObject.path.substring(1, heldObject.path.length - 1);
          var drop = /\t/.test(id);
          if (drop) {
            action["type"] = "drop";
            action["source_path"] = heldObject.path;
          } else {
            action["type"] = "displace";
            action["baseitem"] = id;
          }
        } else {
          let objectId = import_marauroa13.marauroa.me["id"];
          if (e.type === "touchend" && targetSlot === "content") {
            const container = stendhal.ui.equip.getByElement(HTMLUtil.extractTarget(event).parentElement);
            if (container && container.object) {
              objectId = container.object.id;
            }
          }
          action["type"] = "equip";
          action["source_path"] = heldObject.path;
          action["target_path"] = "[" + objectId + "	" + targetSlot + "]";
        }
        const quantity = heldObject.quantity;
        const sourceSlot = heldObject.slot || "viewport";
        heldObject = void 0;
        const touch_held = TouchHandler.get().holding() && (quantity || 1) > 1;
        if (e.ctrlKey || touch_held && sourceSlot !== targetSlot) {
          ui.createSingletonFloatingWindow("Quantity", new DropQuantitySelectorDialog(action, touch_held), pos.pageX - 50, pos.pageY - 25);
        } else {
          HeldObjectManager.get().onRelease();
          import_marauroa13.marauroa.clientFramework.sendAction(action);
        }
      }
      e.stopPropagation();
      e.preventDefault();
    }
    /**
     * This is a workaround until it's figured out how to make it work using the same methods as mouse event.
     */
    onTouchEnd(e) {
      let touchHandler = TouchHandler.get();
      touchHandler.onTouchEnd();
      _ViewPort.get().onDrop(e);
      if (touchHandler.holding()) {
        touchHandler.setHolding(false);
        touchHandler.unsetOrigin();
      }
      Client.handleClickIndicator(e);
    }
    onContentMenu(e) {
      e.preventDefault();
    }
    /**
     * Updates viewport layout to compensate for chat panel style.
     */
    onChatPanelRefresh(floating) {
      const element = this.getElement();
      for (const prop of Object.keys(this.initialStyle)) {
        if (floating) {
          element.style.removeProperty(prop);
        } else {
          element.style.setProperty(prop, this.initialStyle[prop]);
        }
      }
    }
  };

  // util/Chat.ts
  var Chat = class _Chat {
    static debugLogEnabled = false;
    static clog;
    // available actions from attending NPC
    static attending;
    // initialize with "hello" since we won't have received a ChatOptionsEvent at startup
    static options = ["hello"];
    /**
     * Adds a line to the chat log.
     *
     * @param type
     *     Message type.
     * @param message
     *     Message to log.
     * @param orator
     *     Name of entity making the expression (default: <code>undefined</code>).
     * @param profile
     *     Optional entity image filename to show as the speaker.
     * @param headed
     *     Set to <code>true</code> to show a notification bubble.
     */
    static log(type, message, orator, profile, headed = false) {
      headed = headed || typeof profile !== "undefined";
      if (!_Chat.clog) {
        _Chat.clog = ui.get(UIComponentEnum.ChatLog);
      }
      if (type === "emoji" && message instanceof HTMLImageElement) {
        _Chat.clog.addEmojiLine(message, orator);
      } else {
        if (typeof message === "string") {
          _Chat.clog.addLine(type, message, orator);
        } else if (Object.prototype.toString.call(message) === "[object Array]") {
          _Chat.clog.addLines(type, message, orator);
        }
      }
      if (import_marauroa14.marauroa.me && headed) {
        let messages = [];
        if (typeof message === "string") {
          messages.push(message);
        } else {
          messages = message;
        }
        let viewPort = ViewPort.get();
        for (const m of messages) {
          viewPort.addNotifSprite(type, m, profile);
        }
      }
    }
    /**
     * Adds a line to the chat log & a notification bubble to gamewindow.
     *
     * @param type
     *     Message type.
     * @param message
     *     Message to log.
     * @param orator
     *     Name of entity making the expression (default: <code>undefined</code>).
     * @param profile
     *     Optional entity image filename to show as the speaker.
     */
    static logH(type, message, orator, profile) {
      _Chat.log(type, message, orator, profile, true);
    }
    static debug(message) {
      if (_Chat.debugLogEnabled) {
        _Chat.log("client", message);
      }
    }
    /**
     * Formats displayed text.
     *
     * @param message {string}
     *   Text to parse for special characters.
     * @return {string}
     *   Text formatted with keywords & item names highlighted.
     */
    static formatLogEntry(message) {
      if (!message) {
        return "";
      }
      let res = "";
      let delims = [" ", ",", ".", "!", "?", ":", ";"];
      let length = message.length;
      let inHighlight = false, inUnderline = false, inHighlightQuote = false, inUnderlineQuote = false;
      for (let i = 0; i < length; i++) {
        let c = message[i];
        if (c === "\\") {
          let n = message[i + 1];
          res += n;
          i++;
        } else if (c === "#") {
          if (inHighlight) {
            res += c;
            continue;
          }
          let n = message[i + 1];
          if (n === "#") {
            res += c;
            i++;
            continue;
          }
          if (n === "'") {
            inHighlightQuote = true;
            i++;
          }
          inHighlight = true;
          res += '<span class="logh">';
        } else if (c === "\xA7") {
          if (inUnderline) {
            res += c;
            continue;
          }
          let n = message[i + 1];
          if (n === "\xA7") {
            res += c;
            i++;
            continue;
          }
          if (n === "'") {
            inUnderlineQuote = true;
            i++;
          }
          inUnderline = true;
          res += '<span class="logi">';
        } else if (c === "'") {
          if (inUnderlineQuote) {
            inUnderline = false;
            inUnderlineQuote = false;
            res += "</span>";
            continue;
          }
          if (inHighlightQuote) {
            inHighlight = false;
            inHighlightQuote = false;
            res += "</span>";
            continue;
          }
          res += c;
        } else if (c === "<") {
          res += "&lt;";
        } else if (delims.indexOf(c) > -1) {
          let n = message[i + 1];
          if (c === " " || n === " " || n == void 0) {
            if (inUnderline && !inUnderlineQuote && !inHighlightQuote) {
              inUnderline = false;
              res += "</span>" + c;
              continue;
            }
            if (inHighlight && !inUnderlineQuote && !inHighlightQuote) {
              inHighlight = false;
              res += "</span>" + c;
              continue;
            }
          }
          res += c;
        } else {
          res += c;
        }
      }
      if (inUnderline) {
        res += "</span>";
      }
      if (inHighlight) {
        res += "</span>";
      }
      return res;
    }
  };

  // ui/dialog/ChatOptionsDialog.ts
  var import_marauroa15 = __toESM(require_marauroa());
  var ChatOptionsDialog = class _ChatOptionsDialog extends DialogContentComponent {
    /** Dialog instance that is currently visible. */
    static activeInstance;
    /**
     * Keywords to be replaced by a common alias.
     *
     * Some keywords don't need repeated in NPC options. Others, such as "task" or "favor" which
     * serve as alternatives to "quest", may be highlighted in dialogue so keep those.
     */
    static aliases = {
      "bye": "goodbye",
      "hi": "hello"
    };
    /**
     * Creates a new chat options dialog.
     */
    constructor() {
      super("keywordmap-template");
      const custom_options = [];
      for (let opt of stendhal.config.get("chat-opts.custom").split(",")) {
        opt = opt.trim();
        if (opt) {
          custom_options.push(opt);
        }
      }
      const npc_options = [];
      for (let opt of Chat.options) {
        opt = opt.toLowerCase();
        const original = _ChatOptionsDialog.aliases[opt];
        if (original) {
          opt = original;
        }
        if (custom_options.indexOf(opt) == -1) {
          npc_options.push(opt);
        }
      }
      if (npc_options.length > 0) {
        this.addGroup(!Chat.attending ? void 0 : Chat.attending, npc_options);
      }
      if (custom_options.length > 0) {
        this.addGroup("Custom", custom_options);
      }
    }
    /**
     * Adds a section of grouped keywords to dialog.
     *
     * @param title {string}
     *   Text label for group.
     * @param options {string[]}
     *   Keywords available in group.
     */
    addGroup(title, options) {
      if (title) {
        const titleElement = document.createElement("div");
        titleElement.innerText = title;
        this.componentElement.appendChild(titleElement);
      }
      const row = document.createElement("div");
      this.componentElement.appendChild(row);
      for (let idx = 0; idx < options.length; idx++) {
        const button = document.createElement("button");
        button.className = "shortcut-button";
        button.innerText = options[idx];
        button.onclick = () => {
          this.onButtonPressed(button);
        };
        row.appendChild(button);
      }
    }
    /**
     * Executed when a button representing a keyword is pressed.
     *
     * @param button {HTMLButtonElement}
     *   The button that was pressed.
     */
    onButtonPressed(button) {
      button.style.setProperty("background-color", "#b3b3b3");
      window.setTimeout(function() {
        button.style.removeProperty("background-color");
      }, 600);
      let keyword = button.innerText;
      if (keyword.endsWith("...")) {
        while (keyword.endsWith(".")) {
          keyword = keyword.substring(0, keyword.length - 1);
        }
        singletons.getChatInput().setText(keyword);
      } else {
        const action = {
          "type": "chat",
          "text": keyword
        };
        import_marauroa15.marauroa.clientFramework.sendAction(action);
        singletons.getChatInput().remember(keyword);
      }
    }
    /**
     * Parses options and creates dialog window.
     */
    static createOptions() {
      const wstate = stendhal.config.getWindowState("shortcuts");
      const content = new _ChatOptionsDialog();
      const dialog = ui.createSingletonFloatingWindow("Chat Options", content, wstate.x, wstate.y);
      dialog.setId("shortcuts");
      content.setFrame(dialog);
      _ChatOptionsDialog.activeInstance = content;
    }
    onParentClose() {
      _ChatOptionsDialog.activeInstance = void 0;
    }
    /**
     * Checks for an active chat options dialog window.
     *
     * @return {boolean}
     *   `true` if there is currently a window open.
     */
    static isActive() {
      return typeof _ChatOptionsDialog.activeInstance !== "undefined";
    }
    /**
     * Closes the active dialog window & calls cleanup functions.
     */
    static closeActiveInstance() {
      if (_ChatOptionsDialog.activeInstance) {
        _ChatOptionsDialog.activeInstance.close();
      }
    }
  };

  // event/ChatOptionsEvent.ts
  var import_marauroa16 = __toESM(require_marauroa());
  var import_marauroa17 = __toESM(require_marauroa());
  var ChatOptionsEvent = class extends import_marauroa16.RPEvent {
    npc;
    title;
    options;
    execute(entity) {
      if (entity !== import_marauroa17.marauroa.me) {
        return;
      }
      let optionsList = this["options"].split("	");
      let message = [];
      for (let optionListEntry of optionsList) {
        let option = optionListEntry.split("|~|");
        message.push(option[1]);
      }
      Chat.attending = this["title"] || this["npc"];
      Chat.options = message;
      if (Chat.options.length == 1 && Chat.options[0].toLowerCase() === "hello") {
        Chat.attending = void 0;
      }
      let m = "Chat options for " + this["npc"] + ": " + message.join(", ");
      console.log(m);
      Chat.debug(m);
      if (ChatOptionsDialog.isActive()) {
        if (Chat.attending) {
          ChatOptionsDialog.createOptions();
        } else {
          ChatOptionsDialog.closeActiveInstance();
        }
      }
    }
  };

  // ui/dialog/ImageViewerDialog.ts
  var ImageViewerDialog = class extends Component {
    constructor(caption, imageFilename) {
      super("imageviewer-template");
      this.child("h3").textContent = caption;
      this.componentElement.querySelector("img").src = imageFilename;
    }
  };

  // event/ExamineEvent.ts
  var import_marauroa18 = __toESM(require_marauroa());
  var import_marauroa19 = __toESM(require_marauroa());
  var ExamineEvent = class extends import_marauroa18.RPEvent {
    title;
    caption;
    path;
    execute(entity) {
      if (entity !== import_marauroa19.marauroa.me) {
        return;
      }
      ui.createSingletonFloatingWindow(
        this["title"],
        new ImageViewerDialog(this["caption"], this["path"]),
        100,
        50
      );
    }
  };

  // event/GlobalVisualEffectEvent.ts
  var import_marauroa20 = __toESM(require_marauroa());
  var GlobalVisualEffectEvent = class extends import_marauroa20.RPEvent {
    execute(_entity) {
    }
  };

  // event/GroupChangeEvent.ts
  var import_marauroa21 = __toESM(require_marauroa());
  var import_marauroa22 = __toESM(require_marauroa());
  var GroupChangeEvent = class extends import_marauroa21.RPEvent {
    members;
    leader;
    lootmode;
    execute(entity) {
      if (entity !== import_marauroa22.marauroa.me) {
        return;
      }
      stendhal.data.group.updateGroupStatus(this["members"], this["leader"], this["lootmode"]);
      ui.get(UIComponentEnum.GroupPanel).updateGroupStatus();
    }
  };

  // event/GroupInviteEvent.ts
  var import_marauroa23 = __toESM(require_marauroa());
  var import_marauroa24 = __toESM(require_marauroa());
  var GroupInviteEvent = class extends import_marauroa23.RPEvent {
    expire;
    leader;
    execute(entity) {
      if (entity !== import_marauroa24.marauroa.me) {
        return;
      }
      if (this["expire"]) {
        ui.get(UIComponentEnum.GroupPanel).expiredInvite(this["leader"]);
      } else {
        ui.get(UIComponentEnum.GroupPanel).receivedInvite(this["leader"]);
      }
    }
  };

  // event/ImageEvent.ts
  var import_marauroa25 = __toESM(require_marauroa());
  var ImageEvent = class extends import_marauroa25.RPEvent {
    execute(_entity) {
    }
  };

  // event/PlayerLoggedOnEvent.ts
  var import_marauroa26 = __toESM(require_marauroa());
  var PlayerLoggedOnEvent = class extends import_marauroa26.RPEvent {
    name;
    execute(entity) {
      if (stendhal.players.indexOf(this.name) < 0) {
        stendhal.players.push(this.name);
        stendhal.players = stendhal.players.sort();
      }
    }
  };

  // event/PlayerLoggedOutEvent.ts
  var import_marauroa27 = __toESM(require_marauroa());
  var PlayerLoggedOutEvent = class extends import_marauroa27.RPEvent {
    name;
    execute(entity) {
      stendhal.players.splice(stendhal.players.indexOf(this.name), 1);
    }
  };

  // event/PrivateTextEvent.ts
  var import_marauroa28 = __toESM(require_marauroa());
  var PrivateTextEvent = class extends import_marauroa28.RPEvent {
    texttype;
    text;
    profile;
    soundTextEvents = ["privmsg", "support", "tutorial"];
    execute(_entity) {
      const ttype = this["texttype"].toLowerCase();
      const msg = this["text"].replace(/\\r\\n/g, "\n").replace(/\\r/g, "\n");
      let profile;
      if (this.hasOwnProperty("profile")) {
        profile = this["profile"];
      } else if (ttype === "tutorial") {
        profile = "floattingladynpc";
      }
      if (ttype === "server" && msg.includes("\n")) {
        Chat.log(ttype, msg.split("\n"), void 0, profile);
      } else {
        const headed = ttype !== "scene_setting";
        Chat.log(ttype, msg, void 0, profile, headed);
      }
      const notif = stendhal.config.get("chat.private.sound");
      if (notif && this.soundTextEvents.indexOf(ttype) > -1) {
        stendhal.sound.playGlobalizedEffect(notif);
      }
    }
  };

  // ui/toolkit/FloatingWindow.ts
  var FloatingWindow = class extends Component {
    constructor(title, contentComponent, x, y) {
      super("window-template");
      this.contentComponent = contentComponent;
      this.content = contentComponent;
      this.componentElement.style.position = "absolute";
      this.componentElement.style.left = x + "px";
      this.componentElement.style.top = y + "px";
      const titleBar = this.child(".windowtitlebar");
      stendhal.config.applyTheme(titleBar);
      if (title) {
        this.child(".windowtitle").textContent = title;
      } else {
        titleBar.classList.add("hidden");
      }
      this.child(".windowcontent").append(contentComponent.componentElement);
      titleBar.addEventListener("mousedown", (event2) => {
        this.onMouseDown(event2);
      });
      titleBar.addEventListener("touchstart", (event2) => {
        this.onTouchStart(event2);
      });
      const closeButton = this.child(".windowtitleclose");
      closeButton.addEventListener("click", (event2) => {
        this.onClose(event2);
        stendhal.sound.playGlobalizedEffect(this.closeSound);
      });
      closeButton.addEventListener("touchend", (event2) => {
        this.onClose(event2);
        stendhal.sound.playGlobalizedEffect(this.closeSound);
      });
      this.onMouseMovedDuringDragListener = (event2) => {
        if (event2.type === "mousemove") {
          this.onMouseMovedDuringDrag(event2);
        } else {
          this.onTouchMovedDuringDrag(event2);
        }
      };
      this.onMouseUpDuringDragListener = () => {
        this.onMouseUpDuringDrag();
      };
      contentComponent.componentElement.addEventListener("close", (event2) => {
        this.onClose(event2);
      });
      this.contentComponent.parentComponent = this;
      let popupcontainer = document.getElementById("popupcontainer");
      popupcontainer.appendChild(this.componentElement);
    }
    contentComponent;
    closeSound = "click-1";
    opened = true;
    onMouseMovedDuringDragListener;
    onMouseUpDuringDragListener;
    offsetX = 0;
    offsetY = 0;
    content;
    windowId;
    close() {
      this.componentElement.remove();
      this.contentComponent.onParentClose();
      this.opened = false;
      this.contentComponent.parentComponent = void 0;
    }
    onClose(event2) {
      this.close();
      event2.preventDefault();
    }
    isOpen() {
      return this.opened;
    }
    /**
     * start draging of popup window
     */
    onMouseDown(event2) {
      window.addEventListener("mousemove", this.onMouseMovedDuringDragListener, true);
      window.addEventListener("mouseup", this.onMouseUpDuringDragListener, true);
      window.addEventListener("touchmove", this.onMouseMovedDuringDragListener, true);
      window.addEventListener("touchend", this.onMouseUpDuringDragListener, true);
      event2.preventDefault();
      let box = this.componentElement.getBoundingClientRect();
      this.offsetX = event2.clientX - box.left - window.pageXOffset;
      this.offsetY = event2.clientY - box.top - window.pageYOffset;
    }
    onTouchStart(event2) {
      const firstT = event2.changedTouches[0];
      const simulated = new MouseEvent("mousedown", {
        screenX: firstT.screenX,
        screenY: firstT.screenY,
        clientX: firstT.clientX,
        clientY: firstT.clientY
      });
      firstT.target.dispatchEvent(simulated);
      event2.preventDefault();
    }
    /**
     * updates position of popup window during drag
     */
    onMouseMovedDuringDrag(event2) {
      this.componentElement.style.left = event2.clientX - this.offsetX + "px";
      this.componentElement.style.top = event2.clientY - this.offsetY + "px";
      this.onMoved();
    }
    onTouchMovedDuringDrag(event2) {
      const firstT = event2.changedTouches[0];
      const simulated = new MouseEvent("mousemove", {
        screenX: firstT.screenX,
        screenY: firstT.screenY,
        clientX: firstT.clientX,
        clientY: firstT.clientY
      });
      firstT.target.dispatchEvent(simulated);
    }
    /**
     * deregister global event listeners used for dragging popup window
     */
    onMouseUpDuringDrag() {
      window.removeEventListener("mousemove", this.onMouseMovedDuringDragListener, true);
      window.removeEventListener("mouseup", this.onMouseUpDuringDragListener, true);
      window.removeEventListener("touchmove", this.onMouseMovedDuringDragListener, true);
      window.removeEventListener("touchend", this.onMouseUpDuringDragListener, true);
      this.onMoved();
    }
    /**
     * Keeps dialog window within browser page.
     */
    checkPos() {
      if (this.content) {
        this.content.onMoved();
      }
      const dialogArea = this.componentElement.getBoundingClientRect();
      const clientArea = document.documentElement.getBoundingClientRect();
      let clientAreaHeight = clientArea.height;
      if (clientAreaHeight == 0) {
        clientAreaHeight = window.visualViewport?.height || 200;
      }
      const offset = ui.getPageOffset();
      let newX = dialogArea.x;
      let newY = dialogArea.y;
      if (newX < 0) {
        newX = 0;
        this.componentElement.style.left = offset.x + newX + "px";
      } else if (dialogArea.x + dialogArea.width > clientArea.right + offset.x) {
        newX = clientArea.right - dialogArea.width;
        this.componentElement.style.left = offset.x + newX + "px";
      }
      if (newY < 0) {
        newY = 0;
        this.componentElement.style.top = offset.y + newY + "px";
      } else if (dialogArea.y + dialogArea.height > clientAreaHeight) {
        newY = clientAreaHeight - dialogArea.height;
        this.componentElement.style.top = offset.y + newY + "px";
      }
      return new Point(newX + offset.x, newY + offset.y);
    }
    onMoved() {
      const pos = this.checkPos();
      if (typeof this.windowId !== "undefined") {
        stendhal.config.setWindowState(this.windowId, pos.x, pos.y);
      }
    }
    setId(id) {
      this.windowId = id;
    }
    /**
     * Sets visibility of close button.
     *
     * Default is shown.
     *
     * @param {boolean} enable
     *   Set to `false` to hide.
     */
    enableCloseButton(enable) {
      this.child(".windowtitleclose").style.display = enable ? "" : "none";
    }
  };

  // ui/dialog/TravelLogDialog.ts
  var import_marauroa29 = __toESM(require_marauroa());
  var TravelLogDialog = class extends DialogContentComponent {
    currentProgressType = "";
    repeatable = {};
    constructor(dataItems) {
      super("travellogdialog-template");
      ui.registerComponent(UIComponentEnum.TravelLogDialog, this);
      this.refresh();
      this.child(".travellogitems").innerHTML = '<option value="">(loading)</option>';
      if (dataItems) {
        this.setDataItems(dataItems);
      }
    }
    setDataItems(dataItems) {
      this.createHtml(dataItems);
      this.currentProgressType = dataItems[0];
      var action = {
        "type": "progressstatus",
        "progress_type": this.currentProgressType
      };
      import_marauroa29.marauroa.clientFramework.sendAction(action);
    }
    refresh() {
      this.componentElement.style.setProperty("font-family", stendhal.config.get("font.travel-log"));
    }
    getConfigId() {
      return "travel-log";
    }
    createHtml(dataItems) {
      let buttons = "";
      for (var i = 0; i < dataItems.length; i++) {
        buttons = buttons + '<button id="' + HTMLUtil.esc(dataItems[i]) + '" class="progressTypeButton">' + HTMLUtil.esc(dataItems[i]) + "</button>";
      }
      this.child(".tavellogtabpanel").innerHTML = buttons;
      this.componentElement.querySelectorAll(".progressTypeButton").forEach((button) => {
        button.addEventListener("click", (event2) => {
          this.onProgressTypeButtonClick(event2);
        });
      });
      this.child(".travellogitems").addEventListener("change", (event2) => {
        this.onTravelLogItemsChange(event2);
      });
    }
    updateTabs() {
      document.querySelectorAll(".progressTypeButton").forEach((tab) => {
        const element = document.getElementById(tab.id);
        if (element.id == this.currentProgressType) {
          element.classList.add("active");
        } else {
          element.classList.remove("active");
        }
      });
    }
    onProgressTypeButtonClick(event2) {
      this.refreshDetails();
      this.currentProgressType = event2.target.id;
      this.updateTabs();
      var action = {
        "type": "progressstatus",
        "progress_type": this.currentProgressType
      };
      import_marauroa29.marauroa.clientFramework.sendAction(action);
      if (this.currentProgressType === "Completed Quests") {
        import_marauroa29.marauroa.clientFramework.sendAction({
          "type": "progressstatus",
          "progress_type": "repeatable"
        });
      }
    }
    onTravelLogItemsChange(event2) {
      const value = event2.target.value;
      if (!value) {
        return;
      }
      var action = {
        "type": "progressstatus",
        "progress_type": this.currentProgressType,
        "item": value
      };
      import_marauroa29.marauroa.clientFramework.sendAction(action);
    }
    progressTypeData(progressType, dataItems) {
      if (progressType !== this.currentProgressType) {
        return;
      }
      if (dataItems.length == 1 && dataItems[0] === "") {
        dataItems = [];
      }
      dataItems.sort();
      var html = "";
      for (var i = 0; i < dataItems.length; i++) {
        const currentItem = dataItems[i];
        html += '<option value="' + HTMLUtil.esc(currentItem) + '">' + HTMLUtil.esc(currentItem);
        if (this.repeatable[currentItem]) {
          html += " (R)";
        }
        html += "</option>";
      }
      const itemList = this.child(".travellogitems");
      itemList.innerHTML = html;
      if (dataItems.length > 0) {
        itemList.selectedIndex = 0;
        itemList.dispatchEvent(new Event("change"));
      } else if (stendhal.session.touchOnly()) {
        itemList.innerHTML = '<option value="">(none)</option>';
        itemList.selectedIndex = 0;
      }
    }
    itemData(progressType, selectedItem, description, dataItems) {
      if (progressType !== this.currentProgressType) {
        return;
      }
      const detailsSpan = document.createElement("span");
      detailsSpan.innerHTML = "<h3>" + HTMLUtil.esc(selectedItem) + "</h3>";
      if (this.repeatable[selectedItem]) {
        detailsSpan.innerHTML += '<p id="travellogrepeatable"><img src="' + Paths.gui + '/rp.png" /> <em>I can do this quest again.</em></p>';
      }
      detailsSpan.innerHTML += '<p id="travellogdescription">' + HTMLUtil.esc(description) + "</p>";
      const ul = document.createElement("ul");
      ul.className = "uniform";
      for (var i = 0; i < dataItems.length; i++) {
        let content = [];
        let html = HTMLUtil.esc(dataItems[i], ["em", "tally"]);
        if (html.includes("<tally>") && html.includes("</tally>")) {
          content = HTMLUtil.formatTallyMarks(html);
        } else {
          content.push(html);
        }
        const li = document.createElement("li");
        li.className = "uniform";
        li.innerHTML = content[0];
        if (content[1]) {
          li.appendChild(content[1]);
          if (content[2]) {
            li.innerHTML += content[2];
          }
        }
        ul.appendChild(li);
      }
      detailsSpan.appendChild(ul);
      this.refreshDetails("", detailsSpan);
    }
    refreshDetails(html = "", newDetails) {
      const details = this.child(".travellogdetails");
      details.innerHTML = html;
      if (newDetails) {
        details.appendChild(newDetails);
      }
    }
    onParentClose() {
      ui.unregisterComponent(this);
    }
    setRepeatable(dataItems) {
      const repeatable = {};
      for (const item of dataItems) {
        repeatable[item] = true;
      }
      this.repeatable = repeatable;
    }
  };

  // event/ProgressStatusEvent.ts
  var import_marauroa30 = __toESM(require_marauroa());
  var ProgressStatusEvent = class extends import_marauroa30.RPEvent {
    progress_type;
    data;
    item;
    description;
    execute(_entity) {
      let progressType = this["progress_type"];
      let dataItems = this["data"].substring(1, this["data"].length - 1).split(/\t/);
      let travelLogDialog = ui.get(UIComponentEnum.TravelLogDialog);
      if (!this["progress_type"]) {
        if (!travelLogDialog) {
          const dstate = stendhal.config.getWindowState("travel-log");
          travelLogDialog = new TravelLogDialog(dataItems);
          new FloatingWindow("Travel Log", travelLogDialog, dstate.x, dstate.y).setId("travel-log");
        } else {
          travelLogDialog.setDataItems(dataItems);
        }
        travelLogDialog.updateTabs();
        return;
      }
      if (!travelLogDialog) {
        return;
      }
      travelLogDialog.updateTabs();
      if (!this["item"]) {
        if (progressType === "repeatable") {
          travelLogDialog.setRepeatable(dataItems);
        } else {
          travelLogDialog.progressTypeData(progressType, dataItems);
        }
      } else {
        travelLogDialog.itemData(progressType, this["item"], this["description"], dataItems);
      }
    }
  };

  // event/ReachedAchievementEvent.ts
  var import_marauroa31 = __toESM(require_marauroa());
  var ReachedAchievementEvent = class extends import_marauroa31.RPEvent {
    category;
    title;
    description;
    execute(_entity) {
      ViewPort.get().addAchievementNotif(this["category"], this["title"], this["description"]);
    }
  };

  // event/ShowItemListEvent.ts
  var import_marauroa32 = __toESM(require_marauroa());
  var ShowItemListEvent = class extends import_marauroa32.RPEvent {
    title;
    caption;
    content;
    execute(_entity) {
      let title = "Items";
      let caption = "";
      let items = [];
      if (this.hasOwnProperty("title")) {
        title = this["title"];
      }
      if (this.hasOwnProperty("caption")) {
        caption = this["caption"];
      }
      if (this.hasOwnProperty("content")) {
        for (let obj in this["content"]) {
          if (this["content"].hasOwnProperty(obj)) {
            let data = this["content"][obj]["a"];
            let item = {
              clazz: data["class"],
              subclass: data["subclass"],
              img: data["class"] + "/" + data["subclass"] + ".png",
              price: data["price"],
              desc: data["description_info"]
            };
            if (item.price.startsWith("-")) {
              item.price = item.price.substr(1);
            }
            items.push(item);
          }
        }
      }
      const content = new class extends DialogContentComponent {
      }("empty-div-template");
      content.componentElement.classList.add("shopsign");
      const table = document.createElement("table");
      table.className = "shoptable";
      content.componentElement.appendChild(table);
      const tableCaption = document.createElement("caption");
      tableCaption.className = "shopcaption";
      tableCaption.textContent = caption;
      table.appendChild(tableCaption);
      const thead = document.createElement("thead");
      const headerRow = document.createElement("tr");
      const headers = ["Item", "Price", "Description"];
      for (const text of headers) {
        const th = document.createElement("th");
        th.textContent = text;
        headerRow.appendChild(th);
      }
      thead.appendChild(headerRow);
      table.appendChild(thead);
      const tbody = document.createElement("tbody");
      for (const i of items) {
        const row = document.createElement("tr");
        const itemCell = document.createElement("td");
        itemCell.appendChild(
          htmlImageStore.get(
            Paths.sprites + "/items/" + i.img
          )
        );
        row.appendChild(itemCell);
        const priceCell = document.createElement("td");
        priceCell.className = "number";
        priceCell.textContent = i.price;
        row.appendChild(priceCell);
        const descCell = document.createElement("td");
        descCell.innerHTML = Chat.formatLogEntry(i.desc);
        row.appendChild(descCell);
        tbody.appendChild(row);
      }
      table.appendChild(tbody);
      stendhal.ui.globalInternalWindow.set(
        ui.createSingletonFloatingWindow(title, content, 20, 20)
      );
    }
  };

  // event/ShowOutfitListEvent.ts
  var import_marauroa33 = __toESM(require_marauroa());
  var ShowOutfitListEvent = class _ShowOutfitListEvent extends import_marauroa33.RPEvent {
    title;
    caption;
    outfits;
    execute(_entity) {
      let title = "Outfits";
      let caption = "";
      let outfits = [];
      if (this.hasOwnProperty("title")) {
        title = this["title"];
      }
      if (this.hasOwnProperty("caption")) {
        caption = this["caption"];
      }
      if (this.hasOwnProperty("outfits")) {
        for (let o of this["outfits"].split(":")) {
          let parts = o.split(";");
          if (parts.length > 2) {
            outfits.push({
              name: parts[0],
              outfit: parts[1],
              price: parts[2]
            });
          }
        }
      }
      if (this.hasOwnProperty("show_base")) {
      }
      const content = new class extends DialogContentComponent {
      }("empty-div-template");
      content.componentElement.classList.add("shopsign");
      const table = document.createElement("table");
      table.className = "shoptable";
      content.componentElement.appendChild(table);
      const tableCaption = document.createElement("caption");
      tableCaption.className = "shopcaption";
      tableCaption.textContent = caption;
      table.appendChild(tableCaption);
      const thead = document.createElement("thead");
      const headerRow = document.createElement("tr");
      const headers = ["Name", "Outfit", "Price"];
      for (const text of headers) {
        const th = document.createElement("th");
        th.textContent = text;
        headerRow.appendChild(th);
      }
      thead.appendChild(headerRow);
      table.appendChild(thead);
      const tbody = document.createElement("tbody");
      for (const outfit of outfits) {
        const row = document.createElement("tr");
        const nameCell = document.createElement("td");
        nameCell.textContent = outfit.name;
        row.appendChild(nameCell);
        const imageCell = document.createElement("td");
        let url = _ShowOutfitListEvent.outfitToUrl(outfit.outfit);
        const img1 = document.createElement("img");
        const img2 = document.createElement("img");
        img1.src = url;
        img2.src = url + "&offset=0";
        imageCell.append(img1);
        imageCell.append(img2);
        row.appendChild(imageCell);
        const priceCell = document.createElement("td");
        priceCell.className = "number";
        priceCell.textContent = outfit.price;
        row.appendChild(priceCell);
        tbody.appendChild(row);
      }
      table.appendChild(tbody);
      stendhal.ui.globalInternalWindow.set(
        ui.createSingletonFloatingWindow(title, content, 20, 20)
      );
    }
    static outfitToUrl(outfit) {
      if (!outfit) {
        return "";
      }
      const order = ["body", "dress", "head", "mouth", "eyes", "mask", "hair", "hat", "detail"];
      let values = {};
      for (let part of outfit.split(",")) {
        let [key, value] = part.split("=");
        values[key] = Number(value);
      }
      let parts = order.map((key) => {
        let value = values[key] ?? 0;
        return `${key}-${value}-0`;
      });
      let url = "https://stendhalgame.org/createoutfit.php?rewritten=true&dialog=shop&outfit=" + parts.join("_");
      return url;
    }
  };

  // event/SoundEvent.ts
  var import_marauroa35 = __toESM(require_marauroa());

  // data/sound/SoundID.ts
  var SoundID = {
    "level_up": "player/tadaa",
    "level_down": void 0,
    "stat_up": "player/stat_up-01",
    "stat_down": void 0,
    "achievement": "player/yay",
    "commerce": "coins-01",
    "commerce2": "cha-ching",
    "heal": "heal-01"
  };

  // data/sound/SoundManager.ts
  var import_marauroa34 = __toESM(require_marauroa());

  // data/sound/SoundFactory.ts
  var SoundFactory = class {
    /**
     * Hidden constructor (use `SoundFactory.create`).
     */
    constructor() {
    }
    /**
     * Creates a new sound object.
     *
     * @param src {string}
     *   Sound filename path (default: `undefined`).
     */
    static create(src) {
      const sound = new Audio(src);
      sound.basevolume = sound.volume;
      return sound;
    }
  };

  // data/enum/AbstractEnum.ts
  var AbstractEnum = class _AbstractEnum {
    /**
     * Creates a new enumeration instance.
     *
     * @param value {T}
     *   Value representation of this instance.
     */
    constructor(value) {
      this.value = value;
    }
    value;
    /**
     * Checks for equality between two objects.
     *
     * @param obj {any}
     *   The other object to compare against this instance.
     * @return {boolean}
     *   `true` if `obj` is instance of same type & their values are the same.
     */
    equals(obj) {
      if (!(obj instanceof _AbstractEnum)) {
        return false;
      }
      return this.value === obj.value;
    }
  };

  // data/sound/SoundLayer.ts
  var SoundLayer = class _SoundLayer extends AbstractEnum {
    /** Reference to layers. */
    static layers = [];
    static MUSIC = new _SoundLayer("music");
    static AMBIENT = new _SoundLayer("ambient");
    static CREATURE = new _SoundLayer("creature");
    static SFX = new _SoundLayer("sfx");
    static GUI = new _SoundLayer("gui");
    constructor(value) {
      super(value);
      _SoundLayer.layers.push(this);
    }
    /**
     * Retrieves all layer names.
     *
     * @return {string[]}
     *   Names of available layers.
     */
    static names() {
      const names = [];
      for (const layer of _SoundLayer.layers) {
        names.push(layer.value);
      }
      return names;
    }
    /**
     * Retrieves sound layer corresponding to layer name.
     *
     * @param name {string}
     *   Layer name.
     * @return {data.sound.SoundLayer.SoundLayer}
     *   Sound layer matching name or `undefined`.
     */
    static checkLayerName(name) {
      for (const layer of _SoundLayer.layers) {
        if (layer.value === name) {
          return layer;
        }
      }
      return void 0;
    }
  };

  // data/sound/SoundManager.ts
  var SoundManager = class _SoundManager {
    /** Distance at which entity sounds can be heard. */
    static DEFAULT_RADIUS = 23;
    /** Layer names & ordering. */
    layers;
    /** Session cache. */
    cacheGlobal;
    /** Cache for current map. */
    cache;
    /** Actively playing sounds. */
    active;
    /** Music instance played globally. */
    globalMusic;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_SoundManager.instance) {
        _SoundManager.instance = new _SoundManager();
      }
      return _SoundManager.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
      this.layers = SoundLayer.names();
      this.cacheGlobal = {};
      this.cache = {};
      this.active = {};
      for (const layerName of this.layers) {
        this.active[layerName] = [];
      }
    }
    /**
     * Retrieves active sounds.
     *
     * @param includeGui {boolean}
     *   Will include sounds from the gui layer (default: false).
     * @return {data.SoundFactory.SoundObject[]}
     *   Array of active sounds.
     */
    getActive(includeGui = false) {
      const active = [];
      for (const layerName of this.layers) {
        if (layerName === "gui" && !includeGui) {
          continue;
        }
        for (const snd of this.active[layerName]) {
          active.push(snd);
        }
      }
      return active;
    }
    /**
     * Retrieves a single active sound.
     *
     * @param soundName {string}
     *   Basename of sound file.
     * @param includeGui {boolean}
     *   Will include sounds from the gui layer (default: false).
     * @return {data.SoundFactory.SoundObject}
     *   Active sound instance or `undefined`.
     */
    getActiveByName(soundName, includeGui = false) {
      for (const sound of this.getActive(includeGui)) {
        if (soundName === sound.basename) {
          return sound;
        }
      }
    }
    /**
     * Initializes an audio object & loads into the cache.
     *
     * @param id {string}
     *   Identifier string used to retrieve from cache.
     * @param filename {string}
     *   Path to sound file.
     * @param global {boolean}
     *   Store in session cache instead of map (default: false).
     * @return {data.SoundFactory.SoundObject}
     *   New audio object.
     */
    load(id, filename, global = false) {
      const snd = SoundFactory.create(filename);
      snd.autoplay = false;
      if (global) {
        this.cacheGlobal[id] = snd;
      } else {
        this.cache[id] = snd;
      }
      return snd;
    }
    /**
     * Retrieves list of valid layer string identifiers.
     *
     * @return {string[]}
     *   Array of layer names.
     */
    getLayerNames() {
      return this.layers;
    }
    /**
     * Checks for valid layer.
     *
     * @param layer {any}
     *   Layer name or index.
     * @return {string}
     *   Layer name or `undefined`.
     */
    checkLayer(layer) {
      let layerIndex = -1;
      const ltype = typeof layer;
      if (ltype === "number") {
        layerIndex = layer;
      } else if (ltype === "string") {
        if (this.layers.indexOf(layer) > -1) {
          return layer;
        }
        if (!Number.isNaN(layer)) {
          layerIndex = parseInt(layer, 10);
        }
      }
      let layerName = this.layers[layerIndex];
      if (!layerName) {
        console.error("invalid sound layer:", layer, new Error());
      }
      return layerName;
    }
    /**
     * Retrieves array index of layer name.
     *
     * @param layerName {string}
     *   Name of layer or index representation as string.
     * @return {number}
     *   Index or -1 if not found.
     */
    getLayerIndex(layerName) {
      let layerIndex = parseInt(layerName, 10);
      if (Number.isNaN(layerIndex)) {
        layerIndex = this.layers.indexOf(layerName);
      }
      if (layerIndex < 0 || layerIndex >= this.layers.length) {
        console.error("invalid sound layer:", layerName, new Error());
        layerIndex = -1;
      }
      return layerIndex;
    }
    /**
     * Sets event handlers for when sound finishes.
     *
     * @param layerName {string}
     *   Name of layer sound will play on.
     * @param sound {data.SoundFactory.SoundObject}
     *   The playing sound.
     */
    onSoundAdded(layerName, sound) {
      sound.onended = (e) => {
        const idx = this.active[layerName].indexOf(sound);
        if (idx > -1) {
          this.active[layerName].splice(idx, 1);
        }
      };
      this.active[layerName].push(sound);
    }
    /**
     * Plays a sound.
     *
     * @param soundName {string}
     *   Sound file basename.
     * @param layerName {string}
     *   Name of layer sound will play on.
     * @param volume {number}
     *   Volume level between 0.0 and 1.0 (default 1.0).
     * @param loop {boolean}
     *   Whether or not sound should be looped (default: false).
     * @return {data.SoundFactory.SoundObject}
     *   The new sound instance or `undefined`.
     */
    playEffect(soundName, layerName, volume = 1, loop = false) {
      const muted = !stendhal.config.getBoolean("sound");
      if (muted && !loop) {
        return;
      }
      volume = this.normVolume(volume);
      const volActual = this.getAdjustedVolume(layerName, volume);
      let snd = this.cache[soundName] || this.cacheGlobal[soundName];
      if (!snd) {
        snd = this.load(soundName, Paths.sounds + "/" + soundName + ".ogg");
      }
      if (!this.cache[soundName]) {
        this.cache[soundName] = snd;
      }
      if (layerName === "gui" && !this.cacheGlobal[soundName]) {
        this.cacheGlobal[soundName] = snd;
      }
      const scopy = snd.cloneNode();
      scopy.autoplay = true;
      scopy.basevolume = volume;
      scopy.volume = Math.min(volActual, volume);
      scopy.loop = loop;
      scopy.muted = muted;
      scopy.basename = soundName;
      this.onSoundAdded(layerName, scopy);
      return scopy;
    }
    /**
     * Plays a sound with volume relative to distance.
     *
     * TODO: re-order parameters to put sound name first
     *
     * @param x {number}
     *   X coordinate of sound source.
     * @param y {number}
     *   Y coordinate of sound source.
     * @param radius {radius}
     *   Radius at which sound can be heard.
     * @param layer {string|number}
     *   Channel name or index sound will play on.
     * @param soundName {string}
     *   Sound file basename.
     * @param volume {number}
     *   Volume level between 0.0 and 1.0 (default: 1.0).
     * @param loop {boolean}
     *   Whether or not sound should be looped (default: false).
     * @return {data.SoundFactory.SoundObject}
     *   The new sound instance or `undefined`.
     */
    playLocalizedEffect(x, y, radius, layer, soundName, volume = 1, loop = false) {
      const layerName = this.checkLayer(layer);
      if (!layerName) {
        return;
      }
      const snd = this.playEffect(soundName, layerName, volume, loop);
      if (!snd) {
        return;
      }
      if (radius) {
        if (!import_marauroa34.marauroa.me || !x) {
          snd.volume = 0;
        } else {
          this.adjustForDistance(layerName, snd, radius, x, y, import_marauroa34.marauroa.me["_x"], import_marauroa34.marauroa.me["_y"]);
        }
      }
      snd.radius = radius;
      snd.x = x;
      snd.y = y;
      return snd;
    }
    /**
     * Plays a sound with uniform volume.
     *
     * @param soundName {string}
     *   Sound file basename.
     * @param layer {string|number}
     *   Channel name or index sound will play on (default: "gui").
     * @param volume {number}
     *   Volume level between 0.0 and 1.0 (default: 1.0).
     * @param loop {boolean}
     *   Whether or not sound should be looped (default: false).
     * @return {data.SoundFactory.SoundObject}
     *   The new sound instance or `undefined`.
     */
    playGlobalizedEffect(soundName, layer = "gui", volume = 1, loop = false) {
      const layerName = this.checkLayer(layer);
      if (!layerName) {
        return;
      }
      return this.playEffect(soundName, layerName, volume, loop);
    }
    /**
     * Loops a sound with volume relative to distance.
     *
     * TODO: re-order parameters to put sound name first
     *
     * @param x {number}
     *   X coordinate of sound source.
     * @param y {number}
     *   Y coordinate of sound source.
     * @param radius {number}
     *   Radius at which sound can be heard.
     * @param layer {string|number}
     *   Channel name or index sound will play on.
     * @param soundName {string}
     *   Sound file basename.
     * @param volume {number}
     *   Volume level between 0.0 and 1.0.
     * @return {data.SoundFactory.SoundObject}
     *   The new sound instance or `undefind`.
     */
    playLocalizedLoop(x, y, radius, layer, soundName, volume = 1) {
      return this.playLocalizedEffect(x, y, radius, layer, soundName, volume, true);
    }
    /**
     * Loops a sound with uniform volume.
     *
     * @param soundName {string}
     *   Sound file basename.
     * @param layer {string|number}
     *   Channel name or index sound will play on (default: index of "gui").
     * @param volume {number}
     *   Volume level between 0.0 and 1.0 (default: 1.0).
     * @return {data.SoundFactory.SoundObject}
     *   The new sound instance or `undefined`.
     */
    playGlobalizedLoop(soundName, layer, volume = 1) {
      return this.playGlobalizedEffect(soundName, layer, volume, true);
    }
    /**
     * Loops a sound with volume relative to distance.
     *
     * TODO: re-order parameters to put music name first
     *
     * @param x {number}
     *   X coordinate of sound source.
     * @param y {number}
     *   Y coordinate of sound source.
     * @param radius {number}
     *   Radius at which sound can be heard.
     * @param layer {string|number}
     *   Channel on which to be played.
     * @param musicName {string}
     *   Sound file basename.
     * @param volume {number}
     *   Volume level between 0.0 and 1.0.
     * @return {data.SoundFactory.SoundObject}
     *   The new sound instance or `undefined`.
     */
    playLocalizedMusic(x, y, radius, layer, musicName, volume = 1) {
      if (!this.cache[musicName]) {
        this.load(musicName, Paths.music + "/" + musicName + ".ogg");
      }
      return this.playLocalizedLoop(x, y, radius, layer, musicName, volume);
    }
    /**
     * Loops a sound with uniform volume.
     *
     * @param musicName {string}
     *   Sound file basename.
     * @param volume {number}
     *   Volume level between 0.0 and 1.0 (default: 1.0).
     * @return {data.SoundFactory.SoundObject}
     *   The new sound instance or `undefined`.
     */
    playGlobalizedMusic(musicName, volume = 1) {
      if (!this.cache[musicName]) {
        this.load(musicName, Paths.music + "/" + musicName + ".ogg");
      }
      return this.playGlobalizedLoop(musicName, "music", volume);
    }
    /**
     * Loops a sound with uniform volume that continues playing accross zone changes.
     *
     * Any currently playing instance will be stopped before starting a new one. Also, calling without
     * parameters will stop current instance if playing.
     *
     * @param musicName {string}
     *   Sound file basename. If value `undefined`, `null`, or empty string music will be stopped.
     * @param volume {number}
     *   Volume level between 0.0 and 1.0.
     */
    playSingleGlobalizedMusic(musicName, volume = 1) {
      if (this.globalMusic && musicName === this.globalMusic.basename) {
        return;
      }
      if (this.globalMusic) {
        this.stop("music", this.globalMusic);
        if (!musicName) {
          this.globalMusic = void 0;
          return;
        }
      }
      if (musicName) {
        this.globalMusic = this.playGlobalizedMusic(musicName, volume);
        if (!this.globalMusic) {
          console.error("failed to play global music:", musicName, new Error());
          return;
        }
        this.globalMusic.onended = null;
        const activeIndex = this.active["music"].indexOf(this.globalMusic);
        if (activeIndex > -1) {
          this.active["music"].splice(activeIndex, 1);
        }
        if (this.active["music"].indexOf(this.globalMusic) > -1) {
          console.warn("failed to remove global music from active list, will be stopped when player changes zones");
        }
      }
    }
    /**
     * Stops a sound & removes it from active group.
     *
     * @param layer {string|number}
     *   Channel name or index sound is playing on.
     * @param sound {string|data.SoundFactory.SoundObject}
     *   Sound name or instance to be stopped.
     * @return {boolean}
     *   `true` if succeeded.
     */
    stop(layer, sound) {
      const layerName = this.checkLayer(layer);
      if (this.getLayerIndex(layerName) < 0) {
        return false;
      }
      const isString = typeof sound === "string";
      const sSound = !isString ? sound : this.getActiveByName(sound);
      if (!sSound) {
        console.error(
          "cannot stop unknown sound:",
          isString ? sound : sound.basename,
          new Error()
        );
        return false;
      }
      if (this.active[layerName].indexOf(sSound) > -1 || sSound == this.globalMusic) {
        sSound.pause();
        sSound.currentTime = 0;
        if (sSound.onended) {
          sSound.onended(new Event("stopsound"));
        }
      }
      return this.active[layerName].indexOf(sSound) < 0;
    }
    /**
     * Stops all currently playing sounds.
     *
     * NOTE: this if for map changes & should not include single global music instance
     *
     * @param includeGui {boolean}
     *   If `true`, sounds on the gui layer will stopped as well (default: false).
     * @return {boolean}
     *   `true` if all sounds were aborted or paused.
     */
    stopAll(includeGui = false) {
      let stopped = true;
      for (const layerName of this.layers) {
        if (layerName === "gui" && !includeGui) {
          continue;
        }
        const curLayer = this.active[layerName];
        while (curLayer.length > 0) {
          this.stop(layerName, curLayer[0]);
        }
        stopped = stopped && this.active[layerName].length == 0;
      }
      return stopped;
    }
    /**
     * Mutes all currently playing sounds.
     *
     * @return {boolean}
     *   `true` if all sounds were muted.
     */
    muteAll() {
      let muted = true;
      for (const layerName of this.layers) {
        for (const snd of this.active[layerName]) {
          snd.muted = true;
          muted = muted && snd.muted;
        }
      }
      if (this.globalMusic) {
        this.globalMusic.muted = true;
        muted = muted && this.globalMusic.muted;
      }
      return muted;
    }
    /**
     * Unmutes all currently playing sounds.
     *
     * @return {boolean}
     *   `true` if all sounds were unmuted.
     */
    unmuteAll() {
      let unmuted = true;
      for (const layerName of this.layers) {
        for (const snd of this.active[layerName]) {
          snd.muted = false;
          unmuted = unmuted && !snd.paused && !snd.muted;
        }
      }
      if (this.globalMusic) {
        this.globalMusic.muted = false;
        unmuted = unmuted && !this.globalMusic.paused && !this.globalMusic.muted;
      }
      return unmuted;
    }
    /**
     * Adjusts volume level relative to distance.
     *
     * FIXME: hearing distance is slightly further than in Java client
     *        See: games.stendhal.client.sound.facade.Audible*Area
     *
     * @param layerName {string}
     *   Name of layer this sound is played on.
     * @param snd {data.SoundFactory.SoundObject}
     *   The sound to be adjusted.
     * @param radius {number}
     *   Radius at which sound can be heard.
     * @param sx {number}
     *   X coordinate of sound entity.
     * @param sy {number}
     *   Y coordinate of sound entity.
     * @param ex {number}
     *   X coordinate of listening entity.
     * @param ey {number}
     *   Y coordinate of listening entity.
     */
    adjustForDistance(layerName, snd, radius, sx, sy, ex, ey) {
      const xdist = ex - sx;
      const ydist = ey - sy;
      const dist2 = xdist * xdist + ydist * ydist;
      const rad2 = radius * radius;
      if (dist2 > rad2) {
        snd.volume = 0;
      } else {
        const volMax = this.getAdjustedVolume(layerName, snd.basevolume);
        snd.volume = this.normVolume(Math.min(rad2 / (dist2 * 20), volMax));
      }
    }
    /**
     * Normalizes volume level.
     *
     * @param vol {number}
     *   The input volume.
     * @return {number}
     *   Normalized volume level between 0.0 and 1.0.
     */
    normVolume(vol) {
      if (isNaN(vol) || !isFinite(vol)) {
        console.warn("Tried to set invalid volume level: " + vol, new Error());
        vol = 1;
      }
      return vol < 0 ? 0 : vol > 1 ? 1 : vol;
    }
    /**
     * Calculates actual volume against layer volume levels.
     *
     * @param layerName {string}
     *   String identifier of layer sound is playing on.
     * @param volBase {number}
     *   The sounds base volume level.
     * @return {number}
     *   Volume level adjusted with "master" & associated layer.
     */
    getAdjustedVolume(layerName, volBase) {
      let volActual = stendhal.config.getInt("sound.master.volume");
      if (typeof volActual === "number") {
        volActual /= 100;
      } else {
        volActual = 1;
      }
      let lvol = stendhal.config.getInt("sound." + layerName + ".volume");
      if (typeof lvol === "number") {
        lvol /= 100;
      } else {
        console.warn('cannot adjust volume for layer "' + layerName + '"');
        return volActual;
      }
      return volActual * lvol;
    }
    /**
     * Applies the adjusted volume level to a sound.
     *
     * @param layerName {string}
     *   String identifier of layer sound is playing on.
     * @param snd {data.SoundFactory.SoundObject}
     *   The sound to be adjusted.
     */
    applyAdjustedVolume(layerName, snd) {
      snd.volume = this.normVolume(this.getAdjustedVolume(layerName, snd.basevolume));
    }
    /**
     * Sets layer volume level.
     *
     * TODO: return numeric value for determining what went wrong
     *
     * @param layerName {string}
     *   Name of layer being adjusted.
     * @param vol {number}
     *   Volume level between 0.0 and 1.0.
     * @return
     *   `true` if volume level was set.
     */
    setVolume(layerName, vol) {
      let volOld = stendhal.config.getInt("sound." + layerName + ".volume");
      if (typeof volOld === "number") {
        volOld /= 100;
      } else {
        return false;
      }
      stendhal.config.set("sound." + layerName + ".volume", Math.floor(this.normVolume(vol) * 100));
      const layerSet = layerName === "master" ? this.layers : [layerName];
      for (const l of layerSet) {
        const layerSounds = this.active[l];
        if (typeof layerSounds === "undefined") {
          continue;
        }
        for (const snd of layerSounds) {
          if (typeof snd.radius === "number" && typeof snd.x === "number" && typeof snd.y === "number") {
            this.adjustForDistance(
              layerName,
              snd,
              snd.radius,
              snd.x,
              snd.y,
              import_marauroa34.marauroa.me["_x"],
              import_marauroa34.marauroa.me["_y"]
            );
          } else {
            this.applyAdjustedVolume(layerName, snd);
          }
        }
      }
      if (this.globalMusic && (layerName === "music" || layerName === "master")) {
        this.applyAdjustedVolume(layerName, this.globalMusic);
      }
      return true;
    }
    /**
     * Retrieves layer volume level.
     *
     * @param layerName {string}
     *   Layer string identifier (default: "master").
     * @return {number}
     *   Current volume level of layer (returns 1 on error).
     */
    getVolume(layerName = "master") {
      let vol = stendhal.config.getInt("sound." + layerName + ".volume");
      if (typeof vol === "undefined" || isNaN(vol) || !isFinite(vol)) {
        console.warn('could not get volume for channel "' + layerName + '"');
        return 1;
      }
      return this.normVolume(vol / 100);
    }
    /**
     * Toggles muted state of sound system.
     */
    toggleSound() {
      stendhal.config.set("sound", !stendhal.config.getBoolean("sound"));
      this.onStateChanged();
    }
    /**
     * Called when sound enabled/disabled state is changed.
     */
    onStateChanged() {
      if (stendhal.config.getBoolean("sound")) {
        if (!this.unmuteAll()) {
          let errmsg = "Failed to unmute sounds:";
          for (const snd of this.getActive()) {
            if (snd && snd.src && snd.muted) {
              errmsg += "\n- " + snd.src;
            }
          }
          console.warn(errmsg);
        }
      } else {
        if (!this.muteAll()) {
          let errmsg = "Failed to mute sounds:";
          for (const snd of this.getActive()) {
            if (snd && snd.src && !snd.muted) {
              errmsg += "\n- " + snd.src;
            }
          }
          console.warn(errmsg);
        }
      }
      ui.onSoundUpdate();
    }
    /**
     * Can be called when configuration values change.
     */
    onConfigUpdate() {
      for (const layerName of ["master", ...this.layers]) {
        let vol = stendhal.config.getInt("sound." + layerName + ".volume");
        if (typeof vol === "number") {
          vol /= 100;
        } else {
          console.warn('Unrecognized volume value for layer "' + layerName + '":', vol);
          vol = 1;
        }
        this.setVolume(layerName, vol);
      }
    }
    /**
     * Called at startup to pre-cache certain sounds.
     */
    startupCache() {
      this.load(
        "ui/login",
        Paths.sounds + "/ui/login.ogg",
        true
      );
    }
  };

  // event/SoundEvent.ts
  var import_marauroa36 = __toESM(require_marauroa());
  var SoundEvent = class extends import_marauroa35.RPEvent {
    sound;
    sound_id;
    volume;
    radius;
    layer;
    execute(entity) {
      if (!import_marauroa36.marauroa.me) {
        return;
      }
      let radius = SoundManager.DEFAULT_RADIUS;
      if (typeof this["radius"] === "number") {
        radius = this["radius"];
      }
      if (!import_marauroa36.marauroa.me.isInSoundRange(radius, entity)) {
        return;
      }
      let volume = 1;
      if (this.hasOwnProperty("volume")) {
        volume *= this["volume"] / 100;
      }
      let sound = this["sound"];
      if (this["sound_id"]) {
        sound = SoundID[this["sound_id"]];
      }
      stendhal.sound.playLocalizedEffect(
        entity["_x"],
        entity["_y"],
        radius,
        this["layer"],
        sound,
        volume
      );
    }
  };

  // event/TextEvent.ts
  var import_marauroa37 = __toESM(require_marauroa());
  var TextEvent = class extends import_marauroa37.RPEvent {
    range;
    text;
    execute(entity) {
      if (this["range"]) {
        entity.say(this["text"], parseInt(this["range"], 10));
      } else {
        entity.say(this["text"]);
      }
    }
  };

  // ui/component/ItemContainerImplementation.ts
  var import_marauroa38 = __toESM(require_marauroa());
  var ItemContainerImplementation = class {
    // TODO: replace usage of global document.getElementById()
    /**
     * slot name, slot size, object (a corpse or chest) or null for marauroa.me,
     * which changes on zone change.
     */
    constructor(parentElement, slot, size, object, suffix, quickPickup, defaultImage) {
      this.parentElement = parentElement;
      this.slot = slot;
      this.size = size;
      this.object = object;
      this.suffix = suffix;
      this.quickPickup = quickPickup;
      this.defaultImage = defaultImage;
      this.init(size);
    }
    parentElement;
    slot;
    size;
    object;
    suffix;
    quickPickup;
    defaultImage;
    rightClickDuration = 300;
    timestampMouseDown = 0;
    timestampMouseDownPrev = 0;
    lastClickedId = "";
    // marked for updating certain attributes
    dirty = false;
    touchHandler = TouchHandler.get();
    init(size) {
      this.size = size;
      for (let i = 0; i < size; i++) {
        let e = this.parentElement.querySelector("#" + this.slot + this.suffix + i);
        e.setAttribute("draggable", "true");
        e.addEventListener("dragstart", (event2) => {
          this.onDragStart(event2);
        });
        e.addEventListener("dragover", (event2) => {
          this.onDragOver(event2);
        });
        e.addEventListener("touchmove", (event2) => {
          this.onTouchMove(event2);
        }, { passive: true });
        e.addEventListener("drop", (event2) => {
          this.onDrop(event2);
        });
        e.addEventListener("mousedown", (event2) => {
          this.onMouseDown(event2);
        });
        e.addEventListener("mouseup", (event2) => {
          this.onMouseUp(event2);
        });
        e.addEventListener("touchstart", (event2) => {
          this.onTouchStart(event2);
        }, { passive: true });
        e.addEventListener("touchend", (event2) => {
          this.onTouchEnd(event2);
        });
        e.addEventListener("contextmenu", (event2) => {
          this.onContextMenu(event2);
        });
        e.addEventListener("mouseenter", (event2) => {
          this.onMouseEnter(event2);
        });
        e.addEventListener("mouseleave", (event2) => {
          this.onMouseLeave(event2);
        });
      }
      this.update();
    }
    /**
     * Retrieves the inventory component element associated with this container.
     *
     * @return {HTMLElement}
     *   Parent element.
     */
    getParentElement() {
      return this.parentElement;
    }
    /**
     * Marks items to update cursors & tooltips.
     */
    markDirty() {
      this.dirty = true;
    }
    update() {
      this.render();
    }
    render() {
      let myobject = this.object || import_marauroa38.marauroa.me;
      let cnt = 0;
      if (myobject && myobject[this.slot]) {
        for (let i = 0; i < myobject[this.slot].count(); i++) {
          let o = myobject[this.slot].getByIndex(i);
          let e = this.parentElement.querySelector("#" + this.slot + this.suffix + cnt);
          if (!e) {
            continue;
          }
          this.dirty = this.dirty || o !== e.dataItem;
          const item = o;
          let xOffset = 0;
          let yOffset = (item["state"] || 0) * -32;
          if (item.isAnimated()) {
            item.stepAnimation();
            xOffset = -(item.getXFrameIndex() * 32);
          }
          e.style.backgroundImage = "url(" + htmlImageStore.checkPath(Paths.sprites + "/items/" + o["class"] + "/" + o["subclass"] + ".png") + ")";
          e.style.backgroundPosition = xOffset + 1 + "px " + (yOffset + 1) + "px";
          e.textContent = o.formatQuantity();
          if (this.dirty) {
            this.updateCursor(e, item);
            this.updateToolTip(e, item);
          }
          e.dataItem = o;
          cnt++;
        }
      }
      for (let i = cnt; i < this.size; i++) {
        let e = this.parentElement.querySelector("#" + this.slot + this.suffix + i);
        if (this.defaultImage) {
          e.style.backgroundImage = "url(" + Paths.gui + "/" + this.defaultImage + ")";
        } else {
          e.style.backgroundImage = "none";
        }
        e.textContent = "";
        if (this.dirty) {
          this.updateCursor(e);
          this.updateToolTip(e);
        }
        e.dataItem = void 0;
      }
      this.dirty = false;
    }
    onDragStart(event2) {
      let myobject = this.object || import_marauroa38.marauroa.me;
      if (!myobject[this.slot] || event2.type === "dragstart" && this.touchHandler.isTouchEngaged()) {
        event2.preventDefault();
        return;
      }
      let target;
      if (event2 instanceof DragEvent) {
        target = event2.target;
      } else {
        const touch = event2.touches[0] || event2.targetTouches[0] || event2.changedTouches[0];
        target = touch.target;
      }
      const slotNumber = target.id.slice(this.slot.length + this.suffix.length);
      let item = myobject[this.slot].getByIndex(slotNumber);
      if (item) {
        if (!HeldObjectManager.get().prepare(item, event2)) {
          return;
        }
        const heldObject = {
          path: item.getIdPath(),
          zone: import_marauroa38.marauroa.currentZoneName,
          slot: this.slot,
          quantity: item.hasOwnProperty("quantity") ? item["quantity"] : 1
        };
        if (event2 instanceof DragEvent && event2.dataTransfer) {
          HeldObjectManager.get().heldObject = heldObject;
        } else if (this.touchHandler.isTouchEvent(event2)) {
          this.touchHandler.setHolding(true);
          const pos = HTMLUtil.extractPosition(event2);
          HeldObjectManager.get().set(heldObject, new Point(pos.pageX, pos.pageY));
        }
      } else {
        event2.preventDefault();
      }
    }
    /**
     * Handles displaying an icon for objects dragged with touch.
     */
    onTouchMove(event2) {
      if (HeldObjectManager.get().heldObject) {
        return;
      }
      this.onDragStart(event2);
    }
    onDragOver(event2) {
      event2.preventDefault();
      if (event2 instanceof DragEvent && event2.dataTransfer) {
        event2.dataTransfer.dropEffect = "move";
      }
      return false;
    }
    /**
     * Extracts slot index from element ID.
     */
    parseIndex(id) {
      if (id.includes("-")) {
        const tmp = id.split("-");
        const idx = tmp[tmp.length - 1];
        if (!isNaN(parseInt(idx, 10))) {
          return idx;
        }
      }
    }
    onDrop(event2) {
      let heldObject = HeldObjectManager.get().heldObject;
      const myobject = this.object || import_marauroa38.marauroa.me;
      if (heldObject) {
        const pos = HTMLUtil.extractPosition(event2);
        const id = pos.target.id;
        const targetSlot = HTMLUtil.parseSlotName(id);
        if (event2.type === "touchend" && id === "viewport") {
          ViewPort.get().onDrop(event2);
          event2.stopPropagation();
          event2.preventDefault();
          return;
        }
        let objectId = myobject["id"];
        if (event2.type === "touchend") {
          if (targetSlot === "content") {
            const container = stendhal.ui.equip.getByElement(HTMLUtil.extractTarget(event2).parentElement);
            if (container && container.object) {
              objectId = container.object.id;
            }
          } else {
            objectId = import_marauroa38.marauroa.me["id"];
          }
        }
        const action = {
          "source_path": heldObject.path
        };
        const sameSlot = heldObject.slot === targetSlot;
        if (sameSlot) {
          action["type"] = "reorder";
          action["new_position"] = this.parseIndex(id) || "" + (this.size - 1);
        } else {
          action["type"] = "equip";
          action["target_path"] = "[" + objectId + "	" + targetSlot + "]";
          action["zone"] = heldObject.zone;
        }
        const quantity = heldObject.quantity || 1;
        HeldObjectManager.get().heldObject = void 0;
        const touch_held = this.touchHandler.holding() && quantity > 1;
        const split_action = !sameSlot && (event2 instanceof DragEvent && event2.ctrlKey || touch_held);
        if (split_action) {
          const pos2 = HTMLUtil.extractPosition(event2);
          ui.createSingletonFloatingWindow(
            "Quantity",
            new DropQuantitySelectorDialog(action, touch_held),
            pos2.pageX - 50,
            pos2.pageY - 25
          );
        } else {
          HeldObjectManager.get().onRelease();
          import_marauroa38.marauroa.clientFramework.sendAction(action);
        }
      }
      event2.stopPropagation();
      event2.preventDefault();
    }
    onContextMenu(event2) {
      event2.preventDefault();
    }
    isRightClick(event2) {
      if (event2.which) {
        return event2.which === 3;
      } else {
        return event2.button === 2;
      }
    }
    isDoubleClick(evt) {
      if (this.timestampMouseDown - this.timestampMouseDownPrev <= this.rightClickDuration) {
        this.timestampMouseDown = 0;
        return HTMLUtil.extractTarget(evt).id === this.lastClickedId;
      }
      this.lastClickedId = HTMLUtil.extractTarget(evt).id;
      return false;
    }
    onMouseDown(evt) {
      this.timestampMouseDownPrev = this.timestampMouseDown;
      this.timestampMouseDown = +/* @__PURE__ */ new Date();
    }
    onMouseUp(evt) {
      if (evt instanceof MouseEvent) {
        evt.preventDefault();
      }
      if (singletons.getJoystickController().isEngaged()) {
        return;
      }
      let event2 = HTMLUtil.extractPosition(evt);
      if (event2.target.dataItem) {
        const long_touch = this.touchHandler.isLongTouch(evt);
        const context_action = evt instanceof MouseEvent && this.isRightClick(evt) || long_touch;
        if (this.quickPickup && !context_action) {
          import_marauroa38.marauroa.clientFramework.sendAction({
            type: "equip",
            "source_path": event2.target.dataItem.getIdPath(),
            "target_path": "[" + import_marauroa38.marauroa.me["id"] + "	bag]",
            "clicked": "",
            // useful for changing default target in equip action
            "zone": import_marauroa38.marauroa.currentZoneName
          });
          return;
        }
        if (this.isRightClick(event2) || long_touch) {
          const append = [];
          if (long_touch) {
            const tmp = this;
            append.push({
              title: "Hold",
              action: function(entity) {
                tmp.onDragStart(evt);
              }
            });
          }
          stendhal.ui.actionContextMenu.set(ui.createSingletonFloatingWindow(
            "Action",
            new ActionContextMenu(event2.target.dataItem, append),
            event2.pageX - 50,
            event2.pageY - 5
          ));
        } else if (!HeldObjectManager.get().heldObject) {
          if (!stendhal.config.getBoolean("inventory.double-click") || this.isDoubleClick(event2)) {
            import_marauroa38.marauroa.clientFramework.sendAction({
              type: "use",
              "target_path": event2.target.dataItem.getIdPath(),
              "zone": import_marauroa38.marauroa.currentZoneName
            });
          }
        }
      }
      this.touchHandler.setHolding(false);
      document.getElementById("viewport").focus();
    }
    onMouseEnter(evt) {
    }
    onMouseLeave(evt) {
    }
    onTouchStart(evt) {
      const pos = HTMLUtil.extractPosition(evt);
      this.touchHandler.onTouchStart(pos.pageX, pos.pageY);
    }
    onTouchEnd(evt) {
      this.touchHandler.onTouchEnd();
      if (this.touchHandler.isLongTouch(evt) && !this.touchHandler.holding()) {
        this.onMouseUp(evt);
      } else if (this.touchHandler.holding()) {
        evt.preventDefault();
        this.onDrop(evt);
        this.touchHandler.setHolding(false);
      }
      this.touchHandler.unsetOrigin();
    }
    /**
     * Updates cursor to display for targeted item.
     *
     * @param target
     *     HTMLElement representing item.
     * @param item
     *     Object containing item information.
     */
    updateCursor(target, item) {
      if (item) {
        if (this.slot === "content" && stendhal.config.getBoolean("inventory.quick-pickup")) {
          target.style.cursor = "url(" + Paths.sprites + "/cursor/itempickupfromslot.png) 1 3, auto";
          return;
        }
        target.style.cursor = item.getCursor(0, 0);
        return;
      }
      target.style.cursor = "url(" + Paths.sprites + "/cursor/normal.png) 1 3, auto";
    }
    /**
     * Sets tooltip to be shown for item.
     *
     * @param target
     *     HTMLElement representing item.
     * @param item
     *     Object containing item information.
     */
    updateToolTip(target, item) {
      target.title = typeof item !== "undefined" ? item.getToolTip() : "";
    }
  };

  // ui/component/ItemInventoryComponent.ts
  var ItemInventoryComponent = class _ItemInventoryComponent extends Component {
    constructor(object, slot, sizeX, sizeY, quickPickup, defaultImage) {
      super("iteminventory-template", true);
      this.slot = slot;
      _ItemInventoryComponent.counter++;
      this.suffix = "-" + _ItemInventoryComponent.counter + "-";
      this.componentElement.classList.add("inventorypopup_" + sizeX);
      if (quickPickup) {
        this.componentElement.classList.add("quickPickup");
      }
      this.setSize(sizeX, sizeY);
      this.itemContainerImplementation = new ItemContainerImplementation(
        this.componentElement,
        slot,
        sizeX * sizeY,
        object,
        this.suffix,
        quickPickup,
        defaultImage
      );
      stendhal.ui.equip.add(this.itemContainerImplementation);
    }
    slot;
    static counter = 0;
    itemContainerImplementation;
    suffix;
    oldSizeX = 0;
    setObject(object) {
      this.itemContainerImplementation.object = object;
    }
    setSize(sizeX, sizeY) {
      this.componentElement.classList.remove("inventorypopup_" + this.oldSizeX);
      this.componentElement.classList.add("inventorypopup_" + sizeX);
      this.oldSizeX = sizeX;
      let html = "";
      for (let i = 0; i < sizeX * sizeY; i++) {
        html += "<div id='" + this.slot + this.suffix + i + "' class='itemSlot'></div>";
      }
      this.componentElement.innerHTML = html;
    }
    update() {
      this.itemContainerImplementation.update();
    }
    markDirty() {
      this.itemContainerImplementation.markDirty();
    }
    onParentClose() {
      let idx = stendhal.ui.equip.indexOf(this.itemContainerImplementation);
      if (idx < 0) {
        console.log("Cannot cleanup unknown itemContainerImplementation");
        return;
      }
      stendhal.ui.equip.removeIndex(idx);
    }
  };

  // ui/dialog/TradeDialog.ts
  var import_marauroa39 = __toESM(require_marauroa());
  var TradeDialog = class _TradeDialog extends DialogContentComponent {
    static empty = {};
    otherItemsComponent;
    myItemsComponent;
    constructor() {
      super("tradedialog-template");
      ui.registerComponent(UIComponentEnum.TradeDialog, this);
      this.refresh();
      this.otherItemsComponent = new ItemInventoryComponent(
        _TradeDialog.empty,
        "trade",
        2,
        2,
        false,
        void 0
      );
      this.myItemsComponent = new ItemInventoryComponent(
        _TradeDialog.empty,
        "trade",
        2,
        2,
        false,
        void 0
      );
      this.createHtml();
    }
    showState(element, state) {
      element.className = "trade-" + state;
      switch (state) {
        case "NO_ACTIVE_TRADE":
          element.innerText = "Inactive";
          break;
        case "MAKING_OFFERS":
          element.innerText = "Changing";
          break;
        case "LOCKED":
          element.innerText = "Offered";
          break;
        case "DEAL_WAITING_FOR_OTHER_DEAL":
          element.innerText = "ACCEPTED";
          break;
      }
    }
    updateTradeState(me, partner, myState, partnerState) {
      if (partner) {
        this.child("#trade-partner-name").innerText = partner["_name"];
      }
      this.showState(this.child("#trade-partner-status"), partnerState);
      this.showState(this.child("#trade-my-status"), myState);
      this.child("#trade-offer").disabled = !(myState === "MAKING_OFFERS");
      this.child("#trade-accept").disabled = !(myState === "LOCKED" && (partnerState === "LOCKED" || partnerState === "DEAL_WAITING_FOR_OTHER_DEAL"));
      this.otherItemsComponent.setObject(partner ? partner : _TradeDialog.empty);
      this.myItemsComponent.setObject(me);
      if (myState === "TRADE_COMPLETED") {
        this.componentElement.dispatchEvent(new Event("close"));
      }
    }
    getConfigId() {
      return "trade";
    }
    createHtml() {
      this.child("#trade-partner-items").append(this.otherItemsComponent.componentElement);
      this.child("#trade-my-items").append(this.myItemsComponent.componentElement);
      this.child("#trade-accept").addEventListener("click", () => {
        this.onAcceptClicked();
      });
      this.child("#trade-offer").addEventListener("click", () => {
        this.onOfferClicked();
      });
      this.child("#trade-cancel").addEventListener("click", () => {
        this.onCancelClicked();
      });
    }
    onAcceptClicked() {
      let action = {
        "type": "trade",
        "action": "deal",
        "zone": import_marauroa39.marauroa.currentZoneName
      };
      import_marauroa39.marauroa.clientFramework.sendAction(action);
    }
    onOfferClicked() {
      let action = {
        "type": "trade",
        "action": "lock",
        "zone": import_marauroa39.marauroa.currentZoneName
      };
      import_marauroa39.marauroa.clientFramework.sendAction(action);
    }
    onCancelClicked() {
      this.componentElement.dispatchEvent(new Event("close"));
    }
    onParentClose() {
      this.otherItemsComponent.onParentClose();
      this.myItemsComponent.onParentClose();
      ui.unregisterComponent(this);
      let action = {
        "type": "trade",
        "action": "cancel",
        "zone": import_marauroa39.marauroa.currentZoneName
      };
      import_marauroa39.marauroa.clientFramework.sendAction(action);
    }
  };

  // event/TradeEvent.ts
  var import_marauroa40 = __toESM(require_marauroa());
  var import_marauroa41 = __toESM(require_marauroa());
  var TradeEvent = class extends import_marauroa40.RPEvent {
    partner_id;
    user_trade_state;
    partner_trade_state;
    execute(entity) {
      let dialog = ui.get(UIComponentEnum.TradeDialog);
      if (this["user_trade_state"] !== "NO_ACTIVE_TRADE" && !dialog) {
        const dstate = stendhal.config.getWindowState("trade");
        dialog = new TradeDialog();
        new FloatingWindow("Trade", dialog, dstate.x, dstate.y).setId("trade");
      }
      if (dialog) {
        let partner = import_marauroa41.marauroa.currentZone[this["partner_id"]];
        dialog.updateTradeState(import_marauroa41.marauroa.me, partner, this["user_trade_state"], this["partner_trade_state"]);
      }
    }
  };

  // event/TransitionGraphEvent.ts
  var import_marauroa42 = __toESM(require_marauroa());
  var TransitionGraphEvent = class extends import_marauroa42.RPEvent {
    execute(_entity) {
    }
  };

  // event/ViewChangeEvent.ts
  var import_marauroa43 = __toESM(require_marauroa());
  var ViewChangeEvent = class extends import_marauroa43.RPEvent {
    x;
    y;
    execute(_entity) {
      let viewport = ViewPort.get();
      const canvas = viewport.getElement();
      queueMicrotask(() => {
        viewport.freeze = true;
        viewport.offsetX = this.x * viewport.targetTileWidth - Math.round(canvas.width / 2);
        viewport.offsetY = this.y * viewport.targetTileHeight - Math.round(canvas.height / 2);
      });
    }
  };

  // event/EventRegistry.ts
  var EventRegistry = class _EventRegistry {
    initialized = false;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_EventRegistry.instance) {
        _EventRegistry.instance = new _EventRegistry();
      }
      return _EventRegistry.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Registers standard events.
     */
    init() {
      if (this.initialized) {
        console.warn("tried to re-initialize EventRegistry");
        return;
      }
      this.initialized = true;
      import_marauroa44.marauroa.rpeventFactory.register("attack", AttackEvent);
      import_marauroa44.marauroa.rpeventFactory.register("bestiary", BestiaryEvent);
      import_marauroa44.marauroa.rpeventFactory.register("chat_options", ChatOptionsEvent);
      import_marauroa44.marauroa.rpeventFactory.register("examine", ExamineEvent);
      import_marauroa44.marauroa.rpeventFactory.register("global_visual_effect", GlobalVisualEffectEvent);
      import_marauroa44.marauroa.rpeventFactory.register("group_change_event", GroupChangeEvent);
      import_marauroa44.marauroa.rpeventFactory.register("group_invite_event", GroupInviteEvent);
      import_marauroa44.marauroa.rpeventFactory.register("image_event", ImageEvent);
      import_marauroa44.marauroa.rpeventFactory.register("player_logged_on", PlayerLoggedOnEvent);
      import_marauroa44.marauroa.rpeventFactory.register("player_logged_out", PlayerLoggedOutEvent);
      import_marauroa44.marauroa.rpeventFactory.register("private_text", PrivateTextEvent);
      import_marauroa44.marauroa.rpeventFactory.register("progress_status_event", ProgressStatusEvent);
      import_marauroa44.marauroa.rpeventFactory.register("reached_achievement", ReachedAchievementEvent);
      import_marauroa44.marauroa.rpeventFactory.register("show_item_list", ShowItemListEvent);
      import_marauroa44.marauroa.rpeventFactory.register("show_outfit_list", ShowOutfitListEvent);
      import_marauroa44.marauroa.rpeventFactory.register("sound_event", SoundEvent);
      import_marauroa44.marauroa.rpeventFactory.register("text", TextEvent);
      import_marauroa44.marauroa.rpeventFactory.register("trade_state_change_event", TradeEvent);
      import_marauroa44.marauroa.rpeventFactory.register("transition_graph", TransitionGraphEvent);
      import_marauroa44.marauroa.rpeventFactory.register("view_change", ViewChangeEvent);
    }
  };

  // SlashActionRepo.ts
  var import_marauroa48 = __toESM(require_marauroa());

  // action/SlashAction.ts
  var import_marauroa45 = __toESM(require_marauroa());

  // action/ParamList.ts
  var ParamList = class _ParamList {
    /** Parameters associated with an action/command. */
    params;
    /**
     * Creates a new parameter list.
     *
     * @param params {action.ParamList.ParamList|util.Pair.Pair|string}
     *   Parameters to be copied to this instance (default: []).
     */
    constructor(params = []) {
      if (typeof params === "string") {
        const names = params.split(" ");
        params = [];
        for (const name of names) {
          if (name) {
            params.push(new Pair(name, ""));
          }
        }
      } else if (params instanceof _ParamList) {
        params = params.params;
      }
      this.params = [...params];
    }
    /**
     * Adds a parameter to list.
     *
     * @param name {string}
     *   Parameter name/ID such as "foo" & "&lt;foo&gt;".
     * @param desc {string}
     *   Parameter description info {default: empty string}.
     */
    add(name, desc = "") {
      this.params.push(new Pair(name, desc));
    }
    /**
     * Formats parameter names to single string.
     *
     * @param namesOnly {boolean}
     *   Format result in single line using parameter names only.
     * @return {string}
     *   Parameter formatted string.
     */
    toString(namesOnly) {
      if (namesOnly) {
        let sparams2 = "";
        for (const pair of this.params) {
          sparams2 = sparams2.length == 0 ? pair.first : sparams2 + " " + pair.first;
        }
        return sparams2;
      }
      let sparams = "";
      for (const pair of this.params) {
        if (sparams.length > 0) {
          sparams += "\n";
        }
        let tmp = pair.first;
        while (tmp.startsWith("[") || tmp.startsWith("<")) {
          tmp = tmp.substring(1);
        }
        while (tmp.endsWith("]") || tmp.endsWith(">")) {
          tmp = tmp.substring(0, tmp.length - 1);
        }
        sparams += "    " + tmp;
        if (pair.second) {
          sparams += ": " + pair.second;
        }
      }
      return sparams;
    }
  };

  // action/SlashAction.ts
  var SlashActionImpl = class {
    /** Parameter definitions for help information. */
    params;
    /** Description for help information. */
    desc;
    /** Alternative aliases. */
    aliases;
  };
  var SlashAction = class _SlashAction extends SlashActionImpl {
    /**
     * Retrieves help information.
     *
     * @param params {action.ParamList.ParamList|util.Pair.Pair[]}
     *   Optional parameters info.
     * @return {string[]}
     *   Help info for this action.
     */
    getHelp(params) {
      const p = params ? new ParamList(params) : this.params;
      const result = [];
      const sparams = p ? _SlashAction.formatParams(p) : "";
      if (this.desc) {
        result.push(sparams);
        result.push(this.desc);
      } else if (sparams) {
        result.push(sparams);
      }
      return result;
    }
    /**
     * Formats parameter list information.
     *
     * @param params {any}
     *   Parameters info.
     * @param namesOnly {boolean}
     *   Format result in single line using parameter names only (default: `true`).
     * @return {string}
     *   Parameter formatted string.
     */
    static formatParams(params, namesOnly = true) {
      if (params instanceof _SlashAction) {
        params = params.params;
      }
      if (typeof params === "string") {
        return params;
      }
      return new ParamList(params).toString(namesOnly);
    }
    /**
     * Forwards action information to server.
     *
     * @param action {object}
     *   Action object.
     */
    send(action) {
      import_marauroa45.marauroa.clientFramework.sendAction(action);
    }
  };

  // ui/toolkit/Panel.ts
  var Panel = class extends Component {
    containerElement;
    children = [];
    /**
     * Creates a new panel.
     *
     * @param el
     *   DOM element or ID of HTML element or template.
     * @param themable
     *   Inherits theming visuals (default: `false`).
     */
    constructor(el, themable = false) {
      super(el, themable);
      this.containerElement = this.componentElement;
    }
    /**
     * adds a component to this panel
     *
     * @param child Component to add to this panel
     */
    add(child) {
      if (this.children.indexOf(child) > -1) {
        return;
      }
      this.children.push(child);
      if (!this.containerElement.contains(child.componentElement)) {
        this.containerElement.append(child.componentElement);
      }
    }
    /**
     * removes a component from this panel
     *
     * @param child Component to remove from this panel
     */
    remove(child) {
      let index = this.children.indexOf(child);
      if (index < 0) {
        return;
      }
      this.children.splice(index, 1);
      if (this.containerElement.contains(child.componentElement)) {
        child.componentElement.remove();
      }
    }
    /**
     * removes all children
     */
    clear() {
      this.children = [];
      this.containerElement.innerHTML = "";
    }
  };

  // ui/toolkit/TabPanelComponent.ts
  var TabPanelComponent = class extends Panel {
    /** Index value of currently visible tab. */
    currentIndex = 0;
    /** Button representation for each tab. */
    buttons = [];
    /** Optional instructions to execute when active tab changes. */
    onTabChanged;
    /**
     * Creates a new tabbed panel.
     *
     * @param {Layout} layout
     *   Positioning of tabs relative to contents.
     */
    constructor(layout) {
      super("tabpanel-template");
      this.containerElement = this.child(".tabpanel-content");
      const tabs = this.child(".tabpanel-tabs");
      if (1 /* TOP */ === layout) {
        tabs.classList.add("tabpanel-tabs-top");
      } else if (2 /* BOTTOM */ === layout) {
        tabs.classList.add("tabpanel-tabs-bottom");
        this.componentElement.appendChild(tabs);
      } else if (3 /* LEFT */ === layout) {
        this.componentElement.className = "tabpanel-h";
        tabs.classList.add("tabpanel-tabs-h");
        tabs.classList.add("tabpanel-tabs-left");
      } else if (4 /* RIGHT */ === layout) {
        this.componentElement.className = "tabpanel-h";
        tabs.classList.add("tabpanel-tabs-h");
        tabs.classList.add("tabpanel-tabs-right");
        this.componentElement.appendChild(tabs);
      } else {
        console.warn("unsupported layout:", layout);
      }
    }
    add(child) {
      if (this.children.length != this.currentIndex) {
        child.componentElement.style.display = "none";
      }
      super.add(child);
    }
    /**
     * Adds a new tab.
     *
     * @param {string} label
     *   Tab button label.
     * @param {Component=} content
     *   Contents to display when this tab is active.
     */
    addTab(label, content) {
      let button = document.createElement("button");
      button.innerText = label;
      button.dataset.index = "" + this.child(".tabpanel-tabs").children.length;
      button.addEventListener("click", (e) => {
        this.onTabClick(e);
      });
      if (this.buttons.length === 0) {
        button.classList.add("active");
      }
      this.buttons.push(button);
      this.child(".tabpanel-tabs").append(button);
      if (content) {
        this.add(content);
      }
    }
    /**
     * Sets the visible child panel.
     *
     * @param {number} idx
     *   Index of child panel to show.
     */
    setCurrentTab(idx) {
      if (idx == this.currentIndex) {
        return;
      }
      this.children[this.currentIndex].componentElement.style.display = "none";
      this.buttons[this.currentIndex].classList.remove("active");
      this.currentIndex = idx;
      this.children[this.currentIndex].componentElement.style.display = "";
      this.buttons[this.currentIndex].classList.add("active");
      if (this.onTabChanged) {
        this.onTabChanged();
      }
    }
    /**
     * Retrieves index value of current tab.
     *
     * @returns {number}
     *   Indexed number value.
     */
    getCurrentIndex() {
      return this.currentIndex;
    }
    onTabClick(event2) {
      this.setCurrentTab(Number.parseInt(event2.target.dataset.index, 10));
    }
  };

  // ui/toolkit/TabDialogContentComponent.ts
  var TabDialogContentComponent = class extends DialogContentComponent {
    /** Tabs interface. */
    tabs;
    /**
     * Creates a new tabbed content component.
     *
     * @param id {string}
     *   HTML component element ID.
     * @param layout {util.Layout.Layout}
     *   Positioning of tabs relative to contents (default: `util.Layout.Layout.BOTTOM`).
     * @param tabsParent {string}
     *   Selector that should contain the tabs instead of main component (default: `undefined`).
     */
    constructor(id, layout = 2 /* BOTTOM */, tabsParent) {
      super(id);
      this.tabs = new TabPanelComponent(layout);
      if (tabsParent) {
        this.child(tabsParent).appendChild(this.tabs.componentElement);
      } else {
        this.componentElement.appendChild(this.tabs.componentElement);
      }
    }
    /**
     * Adds a new tab.
     *
     * @param label {string}
     *   Tab button label.
     * @param content {ui.toolkit.Component.Component}
     *   Contents to display when this tab is active (default: `undefined`).
     */
    addTab(label, content) {
      this.tabs.addTab(label, content);
    }
  };

  // ui/dialog/AboutDialog.ts
  var AboutDialog = class extends TabDialogContentComponent {
    constructor() {
      super("aboutdialog-template", 1 /* TOP */, "#aboutdialog-content");
      this.addTab("License", new Panel(this.child("#license")));
      this.addTab("Contributors", new Panel(this.child("#contributors")));
      this.addCloseButton();
      this.child("#aboutdialog-content").addEventListener("contextmenu", (evt) => {
        evt.stopPropagation();
      });
      this.load();
    }
    async load() {
      let response = await fetch("LICENSE.html");
      let content = await response.text();
      this.child("#license").innerHTML = content;
      let response2 = await fetch("contributors.html");
      let content2 = await response2.text();
      this.child("#contributors").innerHTML = content2;
    }
    getConfigId() {
      return "about";
    }
  };

  // action/AboutAction.ts
  var AboutAction = class extends SlashAction {
    minParams = 0;
    maxParams = 0;
    execute(_type, _params, _remainder) {
      const wstate = stendhal.config.getWindowState("about");
      const offset = ui.getPageOffset();
      const content = new AboutDialog();
      const dialog = ui.createSingletonFloatingWindow(
        "About",
        content,
        wstate.x - offset.x,
        wstate.y - offset.y
      );
      dialog.setId("about");
      content.setFrame(dialog);
      return true;
    }
    getHelp(params) {
      return ["", "Show dialog with information about project."];
    }
  };

  // ui/component/ShowFloatingWindowComponent.ts
  var ShowFloatingWindowComponent = class extends Component {
    constructor(uiComponentEnum, title, x, y) {
      super("showfloatingwindow-template");
      this.uiComponentEnum = uiComponentEnum;
      this.title = title;
      this.x = x;
      this.y = y;
      this.componentElement.innerText = title;
      this.componentElement.addEventListener("click", () => {
        this.onClick();
      });
    }
    uiComponentEnum;
    title;
    x;
    y;
    onClick() {
      let component = ui.get(this.uiComponentEnum);
      if (!component) {
        return;
      }
      if (component.parentComponent?.isOpen()) {
        component.parentComponent?.close();
      } else {
        new FloatingWindow(this.title, component, this.x, this.y);
      }
    }
  };

  // action/DebugAction.ts
  var import_marauroa46 = __toESM(require_marauroa());
  var DebugAction = class extends SlashAction {
    minParams = 0;
    maxParams = 0;
    uiPopped = false;
    execute(_type, params, _remainder) {
      if (params.length < 1) {
        Chat.logH("error", "Expected parameters");
        this.showUsage();
      } else if (["help", "?"].indexOf(params[0]) > -1) {
        this.showUsage();
      } else if (params[0] === "ui") {
        this.uiAction(params);
      } else if (params[0] === "weather") {
        this.debugWeather(params[1]);
      } else if (params[0] === "log") {
        Chat.debugLogEnabled = true;
      } else if (params[0] === "settings") {
        Chat.log("client", "Experimental settings " + (Debug.toggle("settings") ? "enabled" : "disabled"));
      } else if (params[0] === "touch") {
        Chat.log("client", "Touch debugging " + (Debug.toggle("touch") ? "enabled" : "disabled"));
      } else if (params[0] === "screencap") {
        Debug.setActive("screencap", !Debug.isActive("screencap"));
        Chat.log("client", "Screen capture debugging " + (Debug.isActive("screencap") ? "enabled" : "disabled"));
      } else if (params[0] === "light") {
        Debug.toggle("light");
      }
      return true;
    }
    showUsage() {
      const usage = [
        "Usage:",
        "  /debug log",
        "  /debug ui [pop]",
        "  /debug weather [<name>]",
        "  /debug settings",
        "  /debug touch",
        "  /debug light"
      ];
      Chat.log("client", usage);
    }
    uiAction(params) {
      console.log(UIComponentEnum);
      for (let i in UIComponentEnum) {
        console.log("in", i);
      }
      if (params[1] === "pop") {
        this.uiPop();
      }
    }
    uiPop() {
      this.uiFloatComponent(UIComponentEnum.LeftPanel, "Left panel", 10, 10);
      this.uiFloatComponent(UIComponentEnum.RightPanel, "Right panel", 500, 10);
      this.uiFloatComponent(UIComponentEnum.TopPanel, "Top panel", 200, 10);
      this.uiFloatComponent(UIComponentEnum.BottomPanel, "Bottom panel", 10, 500);
      this.uiFloatComponent(UIComponentEnum.MiniMap, "Map", 10, 50);
      this.uiFloatComponent(UIComponentEnum.PlayerStats, "Stats", 10, 190);
      this.uiFloatComponent(UIComponentEnum.BuddyList, "Buddies", 10, 250);
      this.uiFloatComponent(UIComponentEnum.PlayerEquipment, import_marauroa46.marauroa.me["_name"], 500, 50);
      this.uiFloatComponent(UIComponentEnum.Bag, "Bag", 500, 200);
      this.uiFloatComponent(UIComponentEnum.Keyring, "Keyring", 500, 300);
      this.uiFloatComponent(UIComponentEnum.ChatInput, "Chat", 100, 500);
      this.uiFloatComponent(UIComponentEnum.ChatLog, "Chat log", 100, 560);
      this.uiPopped = true;
    }
    uiFloatComponent(uiComponentEnum, title, x, y) {
      let component = ui.get(uiComponentEnum);
      if (!component) {
        return;
      }
      component.componentElement.dispatchEvent(new Event("close"));
      component.componentElement.remove();
      new FloatingWindow(title, component, x, y);
      if (!this.uiPopped) {
        let bottomPannel = ui.get(UIComponentEnum.BottomPanel);
        bottomPannel.add(new ShowFloatingWindowComponent(uiComponentEnum, title, x, y));
      }
    }
    /**
     * Sets weather animation for debugging.
     *
     * @param weather
     *     Name of weather to be loaded. <code>undefined</code> turns
     *     weather animation off.
     */
    debugWeather(weather) {
      const usage = ["Usage:", "  /debug weather [<name>]"];
      if (weather && ["help", "?"].indexOf(weather) > -1) {
        Chat.log("client", usage);
        return;
      }
      if (!stendhal.config.getBoolean("effect.weather")) {
        Chat.logH("warning", "Weather is disabled.");
      }
      if (weather) {
        weather = weather.replace(/ /g, "_");
        WeatherRenderer.get().update(weather);
      }
    }
  };

  // action/OpenWebsiteAction.ts
  var OpenWebsiteAction = class extends SlashAction {
    /**
     * creates a OpenWebsiteAction
     *
     * @param url website to open
     */
    constructor(url) {
      super();
      this.url = url;
    }
    url;
    minParams = 0;
    maxParams = 0;
    execute(_type, _params, _remainder) {
      window.location.href = this.url;
      return true;
    }
  };

  // action/ProgressStatusAction.ts
  var ProgressStatusAction = class extends SlashAction {
    minParams = 0;
    maxParams = 0;
    execute(_type, _params, _remainder) {
      let travelLogDialog = ui.get(UIComponentEnum.TravelLogDialog);
      if (!travelLogDialog) {
        const dstate = ConfigManager.get().getWindowState("travel-log");
        travelLogDialog = new TravelLogDialog();
        new FloatingWindow("Travel Log", travelLogDialog, dstate.x, dstate.y).setId("travel-log");
      }
      const action = { "type": _type };
      if (_remainder.length > 0) {
        if (_remainder.indexOf("Open Quests") > -1) {
          action["progress_type"] = "Open Quests";
          _remainder = _remainder.substring(12);
        } else if (_remainder.indexOf("Completed Quests") > -1) {
          action["progress_type"] = "Completed Quests";
          _remainder = _remainder.substring(17);
        } else if (_remainder.indexOf("Production") > -1) {
          action["progress_type"] = "Production";
          _remainder = _remainder.substring(11);
        } else {
        }
        if (_remainder) {
          action["item"] = _remainder;
        }
      }
      this.send(action);
      return true;
    }
    getHelp(params) {
      return ["", "Open travel log dialog window."];
    }
  };

  // action/TellAction.ts
  var TellAction = class _TellAction extends SlashAction {
    minParams;
    maxParams;
    aliases = ["msg"];
    static lastPlayerTell;
    /**
     * Creates a new tell action.
     *
     * NOTE: only inheriting classes should call constructor with min/max params
     *
     * @param {number=} minParams
     *   Minimum required number of parameters.
     * @param {number=} maxParams
     *   Maximum allowed number of parameters.
     */
    constructor(minParams, maxParams) {
      super();
      this.minParams = typeof minParams !== "undefined" ? minParams : 1;
      this.maxParams = typeof maxParams !== "undefined" ? maxParams : 1;
    }
    execute(_type, params, remainder) {
      const target = params[0];
      const action = {
        "type": "tell",
        "target": target,
        "text": remainder
      };
      this.send(action);
      _TellAction.lastPlayerTell = target;
      return true;
    }
    getHelp(params) {
      return ["<player> <message>", "Send a private message to #player."];
    }
    /**
     * Retrieves name of player most recently messaged.
     *
     * @return {string|undefined}
     *   Player name or `undefined` if never private messaged.
     */
    static getLastPlayerTell() {
      return _TellAction.lastPlayerTell;
    }
  };

  // action/ReTellAction.ts
  var ReTellAction = class _ReTellAction extends TellAction {
    constructor() {
      super(0, 0);
    }
    execute(_type, params, remainder) {
      const target = _ReTellAction.getLastPlayerTell();
      if (typeof target !== "undefined") {
        return super.execute(_type, [target], remainder);
      }
      return true;
    }
    getHelp(params) {
      return ["<message>", "Send a private message to the last player you sent a message to."];
    }
  };

  // util/DownloadObject.ts
  var DownloadObject = class {
    /** DOM anchor element download link. */
    anchor;
    /**
     * Creates a new downloadable object.
     *
     * @param filename {string}
     *   Default filename.
     * @param data {string}
     *   Data contents of download.
     */
    constructor(filename, data) {
      this.anchor = document.createElement("a");
      this.anchor.download = filename;
      this.anchor.target = "_blank";
      this.anchor.href = data;
    }
    /**
     * Simulates click on anchor element to start download.
     *
     * @return {boolean}
     *   `true` if the download was created.
     */
    execute() {
      this.anchor.click();
      return true;
    }
  };

  // util/StringUtil.ts
  var StringUtil;
  ((StringUtil2) => {
    function isEmpty(st) {
      if (!st) {
        return true;
      }
      return st.trim() === "";
    }
    StringUtil2.isEmpty = isEmpty;
    ;
    function padLeft(st, c, len) {
      if (st.length >= len) {
        return st;
      }
      while (st.length < len) {
        st = c + st;
      }
      return st.slice(-len);
    }
    StringUtil2.padLeft = padLeft;
    ;
    function toDataURL(text, mime = "text/plain", charset = "utf-8") {
      return "data:" + mime + ";charset=" + charset + "," + window.encodeURIComponent(text);
    }
    StringUtil2.toDataURL = toDataURL;
    ;
  })(StringUtil || (StringUtil = {}));

  // util/DownloadUtil.ts
  var DownloadUtil = class _DownloadUtil {
    /**
     * Creates a timestamp.
     *
     * @return {string}
     *   Timestamp formatted string (yyyy-mm-dd_HH.MM.SS).
     */
    static timestamp() {
      const d = /* @__PURE__ */ new Date();
      const ts = {
        yyyy: "" + d.getFullYear(),
        mm: ("00" + (d.getMonth() + 1)).slice(-2),
        dd: ("00" + d.getDate()).slice(-2),
        HH: ("00" + d.getHours()).slice(-2),
        MM: ("00" + d.getMinutes()).slice(-2),
        SS: ("00" + d.getSeconds()).slice(-2),
        ms: "" + d.getMilliseconds()
      };
      while (ts.ms.length < 3) {
        ts.ms = "0" + ts.ms;
      }
      return ts.yyyy + "-" + ts.mm + "-" + ts.dd + "_" + ts.HH + "." + ts.MM + "." + ts.SS;
    }
    /**
     * Creates a downloadable object.
     *
     * @param filename {string}
     *   Default filename.
     * @param uri {string}
     *   Data URI of download.
     * @return {util.DownloadObject.DownloadObject}
     *   Object to be handled by browser for download.
     */
    static build(filename, uri) {
      return new DownloadObject(filename, uri);
    }
    /**
     * Creates an image download from a canvas element.
     *
     * @param filename {string}
     *   Default filename.
     * @param canvas {Canvas}
     *   Canvas containing data to convert to image.
     * @return {util.DownloadObject.DownloadObject}
     *   Object to be handled by browser for image download.
     */
    static buildImage(filename, canvas) {
      return _DownloadUtil.build(filename, canvas.toDataURL("image/png"));
    }
    /**
     * Creates a plain text file download.
     *
     * @param filename {string}
     *   Default filename.
     * @param text {string}
     *   Text contents.
     * @return {util.DownloadObject.DownloadObject}
     *   Object to be handled by browser for text download.
     */
    static buildText(filename, text) {
      return _DownloadUtil.build(filename, StringUtil.toDataURL(text));
    }
    /**
     * Creates an image download from a canvas element.
     *
     * @param canvas {Canvas}
     *   Canvas containing data to convert to image.
     * @return {util.DownloadObject.DownloadObject}
     *   Object to be handled by browser for image download.
     */
    static buildScreenshot() {
      Chat.log("client", "Creating screenshot download ...");
      return _DownloadUtil.buildImage(
        "stendhal_" + _DownloadUtil.timestamp() + ".png",
        ViewPort.get().getElement()
      );
    }
    /**
     * Creates a plain text download of chat log.
     *
     * @param text {string}
     *   Chat log text contents.
     * @return {util.DownloadObject.DownloadObject}
     *   Object to be handled by browser for text download.
     */
    static buildChatLog(text) {
      Chat.log("client", "Creating chat log download ...");
      const name = SessionManager.get().getCharName();
      return _DownloadUtil.buildText("stendhal_chat_" + (!name ? "" : name + "_") + _DownloadUtil.timestamp() + ".txt", text);
    }
  };

  // util/ScreenCapture.ts
  var ScreenCapture = class _ScreenCapture {
    /** Property denoting state of activity. */
    static active = false;
    /** Video recorder. */
    capture;
    /** Recorded data. */
    chunks;
    /** Video capture definition. */
    def;
    constructor() {
      this.chunks = [];
      this.def = this.getDefinition();
    }
    /**
     * Begins recording.
     *
     * @param {Canvas} canvas
     *   Canvas element to record.
     * @param {AudioContext=} audio
     *   Sound manager from which to record audio.
     */
    start(canvas, audio) {
      if (!Debug.isActive("screencap")) {
        Chat.log("client", "Screen capture debugging is disabled");
        return;
      }
      if (!this.def) {
        Chat.log("error", "No suitable video codec available");
        return;
      }
      if (!this.def.acodec) {
        Chat.log("warning", "Audio capture not supported");
      }
      Chat.log("client", "Starting video capture ...");
      Chat.log("client", "&nbsp;&nbsp;MIME type: " + this.def.mime);
      Chat.log("client", "&nbsp;&nbsp;framerate: " + this.def.framerate.toFixed(2) + " frames/sec");
      if (this.def.codec) {
        Chat.log("client", "&nbsp;&nbsp;video encoder: " + this.def.codec + " (" + this.def.bitrate + " bits/sec)");
        if (this.def.acodec) {
          Chat.log("client", "&nbsp;&nbsp;audio encoder: " + this.def.acodec + " (" + this.def.abitrate + " bits/sec)");
        }
      }
      this.capture = new MediaRecorder(canvas.captureStream(this.def.framerate), {
        mimeType: this.getCodec(this.def),
        videoBitsPerSecond: this.def.bitrate,
        audioBitsPerSecond: this.def.abitrate
      });
      this.capture.ondataavailable = (e) => {
        this.chunks.push(e.data);
      };
      this.capture.onstop = () => {
        this.createDownload();
      };
      _ScreenCapture.active = true;
      this.capture.start();
    }
    /**
     * Stops recording & creates data download.
     */
    stop() {
      _ScreenCapture.active = false;
      if (!this.capture) {
        return;
      }
      this.capture.stop();
    }
    /**
     * Creates a video definition based on available media containers & codecs.
     *
     * @returns {VideoDefinition|undefined}
     *   Supported video definiton or `undefined`.
     */
    getDefinition() {
      const containers = ["mp4", "webm", "ogg"];
      const codecs = ["avc1", "h264", "av1", "vp9", "vp8"];
      const acodecs = ["aac", "vorbis", "mpeg"];
      for (const container of containers) {
        for (const codec of codecs) {
          for (const acodec of acodecs) {
            const def = this.buildDefinition(container, codec, acodec);
            if (this.checkDefinition(def)) {
              return def;
            }
          }
        }
      }
      for (const container of containers) {
        for (const codec of codecs) {
          const def = this.buildDefinition(container, codec);
          if (this.checkDefinition(def)) {
            return def;
          }
        }
      }
      for (const container of containers) {
        const def = this.buildDefinition(container);
        if (this.checkDefinition(def)) {
          return def;
        }
      }
      return void 0;
    }
    /**
     * Builds a video definition based on parameters.
     *
     * @param {string} container
     *   Container type (mp4, webm, or ogg).
     * @param {string=} codec
     *   Video codec (avc1, h264, vp9, etc.).
     * @param {string=} acodec
     *   Audio codec (aac, vorbis, mpeg, etc.).
     */
    buildDefinition(container, codec, acodec) {
      const def = {
        mime: "video/" + container,
        container,
        framerate: 3e4 / 1001,
        // NTSC standard (29.97)
        bitrate: 15e5
        // 1.5 Mbit/sec
      };
      if (codec) {
        def.codec = codec;
        if (acodec) {
          def.acodec = acodec;
          def.abitrate = 96e3;
        }
      }
      return def;
    }
    /**
     * Checks if a video definition is supported by Web API & browser.
     *
     * @param {VideoDefinition} def
     *   Video definition with MIME, container, codec, etc. information.
     * @returns {boolean}
     *   `true` if the browser supports encoding with definition's codecs.
     */
    checkDefinition(def) {
      return MediaRecorder.isTypeSupported(this.getCodec(def));
    }
    /**
     * Converts video definition to codec string formatted for use with Web API.
     *
     * @param {VideoDefinition} def
     *   Video definition with MIME, container, codec, etc. information.
     * @returns {string}
     *   String formatted as "<mimetype>[;codecs=<video-encoder>[,<audio-encoder]]".
     */
    getCodec(def) {
      if (def.codec) {
        if (def.acodec) {
          return def.mime + ';codecs="' + def.codec + "," + def.acodec + '"';
        }
        return def.mime + ";codecs=" + def.codec;
      }
      return def.mime;
    }
    /**
     * Gets a URL from recorded data.
     *
     * @returns {string}
     *   Video data URL.
     */
    toDataURL() {
      let blob;
      if (this.def) {
        blob = new Blob(this.chunks, { type: this.def.mime });
      } else {
        blob = new Blob(this.chunks);
      }
      return URL.createObjectURL(blob);
    }
    /**
     * Prepares video data for download.
     */
    createDownload() {
      Chat.log("client", "Creating video download ...");
      const filename = "stendhal_" + DownloadUtil.timestamp() + "." + this.def.container;
      DownloadUtil.build(filename, this.toDataURL()).execute();
    }
    /**
     * Retrieves current activity state.
     *
     * @returns {boolean}
     *   `true` if currently recording.
     */
    static isActive() {
      return _ScreenCapture.active;
    }
  };

  // action/ScreenCaptureAction.ts
  var ScreenCaptureAction = class extends SlashAction {
    minParams = 0;
    maxParams = 0;
    desc = "Start or stop capturing video of the client viewport";
    recorder;
    execute(type, params, remainder) {
      if (this.recorder && ScreenCapture.isActive()) {
        this.recorder.stop();
        this.recorder = void 0;
        return true;
      }
      this.recorder = new ScreenCapture();
      this.recorder.start(ViewPort.get().getElement());
      return true;
    }
  };

  // ui/dialog/settings/GeneralTab.ts
  var import_marauroa47 = __toESM(require_marauroa());

  // ui/dialog/settings/AbstractSettingsTab.ts
  var AbstractSettingsTab = class extends Component {
    constructor(element) {
      super(element, true);
    }
  };

  // util/Globals.ts
  var Globals;
  ((Globals2) => {
    const config2 = ConfigManager.get();
    const session = SessionManager.get();
    function getMenuStyle() {
      return !config2.isSet("menu.style") && session.touchOnly() ? "floating" : config2.get("menu.style") || "traditional";
    }
    Globals2.getMenuStyle = getMenuStyle;
    ;
  })(Globals || (Globals = {}));

  // ui/dialog/settings/GeneralTab.ts
  var GeneralTab = class extends AbstractSettingsTab {
    constructor(parent, element) {
      super(element);
      const config2 = singletons.getConfigManager();
      const chatLog = ui.get(UIComponentEnum.ChatLog);
      parent.createCheckBox(
        "chk_speechcr",
        "speech.creature",
        "Creature speech bubbles are enabled",
        "Creature speech bubbles are disabled"
      );
      const player_stats = ui.get(UIComponentEnum.PlayerStats);
      const chk_charname = parent.createCheckBox(
        "chk_charname",
        "panel.stats.charname",
        void 0,
        void 0,
        function() {
          player_stats.enableCharName(chk_charname.checked);
        }
      );
      const chk_hpbar = parent.createCheckBox(
        "chk_hpbar",
        "panel.stats.hpbar",
        void 0,
        void 0,
        function() {
          player_stats.enableBar("hp", chk_hpbar.checked);
        }
      );
      const chk_floatchat = parent.createCheckBox(
        "chk_floatchat",
        "chat.float",
        void 0,
        void 0,
        function() {
          ui.get(UIComponentEnum.BottomPanel).setFloating(chk_floatchat.checked);
        }
      );
      parent.createCheckBox(
        "chk_hidechat",
        "chat.autohide",
        "Chat panel will be hidden after sending text",
        "Chat panel will remain on-screen"
      );
      parent.createCheckBox(
        "chk_zoom",
        "zoom.touch",
        "Touch zooming enabled (may not work with all browsers)",
        "Touch zooming disabled (may not work with all browsers)",
        function() {
          singletons.getSessionManager().update();
        }
      );
      parent.createCheckBox(
        "chk_dblclick",
        "inventory.double-click",
        "Items are used/consumed with double click/touch",
        "Items are used/consumed with single click/touch",
        function() {
          ui.get(UIComponentEnum.PlayerEquipment).markDirty();
          for (const cid of [UIComponentEnum.Bag, UIComponentEnum.Keyring]) {
            ui.get(cid).markDirty();
          }
        }
      );
      parent.createCheckBox(
        "chk_chestqp",
        "inventory.quick-pickup",
        "Click tranfers items from chests and corpses to player inventory",
        "Click executes default action on items in chests and corpses"
      );
      const chk_movecont = parent.createCheckBox(
        "chk_movecont",
        "move.cont",
        "Player will continue to walk after changing areas",
        "Player will stop after changing areas",
        function() {
          const action = { "type": "move.continuous" };
          if (chk_movecont.checked) {
            action["move.continuous"] = "";
          }
          import_marauroa47.marauroa.clientFramework.sendAction(action);
        }
      );
      const chk_pvtsnd = parent.createCheckBox(
        "chk_pvtsnd",
        "chat.private.sound",
        "Private message audio notifications enabled",
        "Private message audio notifications disabled",
        void 0,
        "ui/notify_up",
        "null"
      );
      chk_pvtsnd.checked = config2.get("chat.private.sound") === "ui/notify_up";
      parent.createCheckBox(
        "chk_nativeemojis",
        "emojis.native",
        "Using native emojis",
        "Using built-in emojis",
        function() {
          singletons.getChatInput().refresh();
        }
      );
      const themes = {};
      for (const t of Object.keys(config2.getThemesMap())) {
        if (t === "wood") {
          themes[t] = t + " (default)";
        } else {
          themes[t] = t;
        }
      }
      const sel_theme = parent.createSelect(
        "selecttheme",
        themes,
        Object.keys(themes).indexOf(config2.getTheme())
      );
      sel_theme.addEventListener("change", (o) => {
        config2.setTheme(Object.keys(themes)[sel_theme.selectedIndex]);
        config2.refreshTheme();
      });
      const fonts = Object.keys(config2.getFontsMap());
      const sel_fontbody = parent.createFontSelect(
        "selfontbody",
        fonts.indexOf(config2.get("font.body"))
      );
      sel_fontbody.addEventListener("change", (e) => {
        const new_font = fonts[sel_fontbody.selectedIndex];
        config2.set("font.body", new_font);
        document.body.style.setProperty("font-family", new_font);
      });
      const sel_fontchat = parent.createFontSelect(
        "selfontchat",
        fonts.indexOf(config2.get("font.chat"))
      );
      sel_fontchat.addEventListener("change", (e) => {
        config2.set("font.chat", fonts[sel_fontchat.selectedIndex]);
        if (chatLog) {
          chatLog.refresh();
        }
      });
      const sel_fonttlog = parent.createFontSelect(
        "selfonttlog",
        fonts.indexOf(config2.get("font.travel-log"))
      );
      sel_fonttlog.addEventListener("change", (e) => {
        config2.set("font.travel-log", fonts[sel_fonttlog.selectedIndex]);
        const tlog = ui.get(UIComponentEnum.TravelLogDialog);
        if (tlog) {
          tlog.refresh();
        }
      });
      parent.createSelectFromConfig(
        "selmenustyle",
        "menu.style",
        void 0,
        function(e) {
          ui.onMenuUpdate();
        }
      );
      const menuStyleElement = this.child("#selmenustyle");
      menuStyleElement.selectedIndex = Globals.getMenuStyle() === "traditional" ? 0 : 1;
      const txt_chatopts = parent.createTextInput(
        "txtchatopts",
        config2.get("chat-opts.custom"),
        "Comma-separated list accessible from the chat options dialog"
      );
      txt_chatopts.addEventListener("change", (e) => {
        config2.set("chat-opts.custom", txt_chatopts.value);
      });
    }
  };

  // ui/dialog/settings/InputTab.ts
  var InputTab = class extends AbstractSettingsTab {
    constructor(parent, element) {
      super(element);
      parent.createCheckBox(
        "chk_pathfinding",
        "pathfinding",
        "Click/Tap ground to walk",
        "Ground pathfinding disabled",
        function(e) {
          ui.get(UIComponentEnum.QMPathFinding).update();
        }
      );
      parent.createCheckBox(
        "chk_pathfindingmm",
        "pathfinding.minimap",
        "Click/Tap minimap to walk",
        "Minimap pathfinding disabled"
      );
      const sel_joystick = parent.createSelectFromConfig(
        "seljoystick",
        "joystick.style",
        void 0,
        function(e) {
          singletons.getJoystickController().update();
        }
      );
      const chk_joystick = parent.createCheckBox(
        "chk_joystick",
        "joystick",
        void 0,
        void 0,
        function(e) {
          sel_joystick.disabled = !chk_joystick.checked;
          singletons.getJoystickController().update();
        }
      );
      chk_joystick.checked = singletons.getSessionManager().joystickEnabled();
      sel_joystick.disabled = !chk_joystick.checked;
      for (const o of ["x", "y"]) {
        const orienter = parent.createNumberInput(
          "txtjoystick" + o,
          parseInt(parent.storedStates["txtjoystick" + o], 10),
          "Joystick position on " + o.toUpperCase() + " axis"
        );
        orienter.addEventListener("input", (e) => {
          singletons.getConfigManager().set("joystick.center." + o, orienter.value || 0);
          singletons.getJoystickController().update();
        });
      }
    }
  };

  // ui/toolkit/WidgetComponent.ts
  var WidgetComponent = class extends ComponentBase {
    /** Setting type. */
    type;
    /** Called when the settings state or value changes. */
    changeListeners;
    initialized = false;
    /**
     * Creates a widget component.
     *
     * @param {WidgetType} type
     *   Widget type.
     */
    constructor(type) {
      super();
      this.type = type;
      this.changeListeners = [];
    }
    /**
     * Inheriting classes constructors should call this method after the component element is created.
     */
    initChangeListener() {
      if (this.initialized) {
        console.warn("Tried to re-initialize widged component");
        return;
      }
      if (!this.componentElement) {
        throw new Error("WidgetComponent.componentElement not defined");
      }
      this.initialized = true;
      this.componentElement.addEventListener("change", (evt) => {
        for (const listener of this.changeListeners) {
          listener(evt);
        }
        this.refresh();
      });
    }
    /**
     * Adds listener for change event.
     *
     * @param {Function} listener
     *   Function to call when change event occurs.
     * @return {Function}
     */
    addListener(listener) {
      this.changeListeners.push(listener);
      return listener;
    }
    /**
     * Adds listener for change event.
     *
     * @param {number} idx
     *   Insertion index in list of listeners.
     * @param {Function} listener
     *   Function to call when change event occurs.
     * @return {Function}
     */
    insertListener(idx, listener) {
      this.changeListeners.splice(idx, 0, listener);
      return listener;
    }
    /**
     * Removes change event listener.
     *
     * @param {number|Function} listener
     *   Listener function or index.
     */
    removeListener(listener) {
      let idx;
      if (typeof listener === "number") {
        idx = listener;
      } else {
        idx = this.changeListeners.indexOf(listener);
      }
      if (idx > -1 && idx < this.changeListeners.length) {
        this.changeListeners.splice(idx, 1);
      }
    }
    /**
     * Adds as child to DOM element.
     *
     * @param {ComponentBase|HTMLElement} parent
     *   Component or element to which to add.
     */
    addTo(parent) {
      const isComponent = parent instanceof ComponentBase;
      this.parentComponent = isComponent ? parent : void 0;
      const parentElement = isComponent ? this.parentComponent.componentElement : parent;
      parentElement.appendChild(this.labelElement);
      if ("checkbox" /* CHECK */ !== this.type) {
        parentElement.appendChild(this.componentElement);
      }
    }
    /**
     * Updates value from configuration ID & adds listener to update configuration when value changes.
     *
     * @param {string} cid
     *   Configuration ID.
     */
    setConfigId(cid) {
      this.setValue(ConfigManager.get().get(cid));
      this.setConfigListener(cid);
    }
    /**
     * Adds listener to update configuration when value changes.
     *
     * @param {string} cid
     *   Configuration ID.
     */
    setConfigListener(cid) {
      this.insertListener(0, (evt) => {
        ConfigManager.get().set(cid, this.getValue());
      });
    }
  };

  // ui/toolkit/SettingsComponent.ts
  var SettingsComponent = class extends WidgetComponent {
    /** The `HTMLElement` associated with this component. */
    componentElement;
    /** Text description. */
    labelElement;
    /**
     * Creates a new settings component.
     *
     * @param {string} id
     *   DOM element ID.
     * @param {string} label
     *   Text label to display.
     * @param {WidgetType} [type=WidgetType.CHECK]
     *   The type of this component. E.g. check box, text input, etc.
     * @param {OptionsEnum} [options={}]
     *   Options for multi-select enumeration.
     * @param {boolean} [experimental=false]
     *   Marks this element to be hidden unless settings debugging is enabled.
     */
    constructor(id, label, type = "checkbox" /* CHECK */, options = {}, experimental = false) {
      super(type);
      this.labelElement = document.createElement("label");
      if ("select" /* SELECT */ === type) {
        this.componentElement = this.initSelect(id, label, options);
      } else {
        this.componentElement = this.initInput(id, label);
      }
      this.initChangeListener();
      if (experimental) {
        this.componentElement.classList.add("experimental");
      }
    }
    /**
     * Initializes main component element as input type.
     *
     * @param {string} id
     *   DOM element ID.
     * @param {string} label
     *   Text label to display.
     * @returns {HTMLElement}
     *   Main component element.
     */
    initInput(id, label) {
      let componentElement;
      switch (this.type) {
        case "checkbox" /* CHECK */:
          this.labelElement.classList.add("checksetting");
          const inputHTML = '<input type="' + this.type + '" id="' + id + '">';
          this.labelElement.innerHTML = inputHTML + label;
          componentElement = this.labelElement.querySelector("#" + id);
          break;
        default:
          this.labelElement.htmlFor = id;
          this.labelElement.innerText = label;
          componentElement = document.createElement("input");
          const inputElement = componentElement;
          inputElement.type = this.type;
          inputElement.id = id;
      }
      return componentElement;
    }
    /**
     * Initializes main component element as select type.
     *
     * @param {string} id
     *   DOM element ID.
     * @param {string} label
     *   Text label to display.
     * @param {OptionsEnum} [options={}]
     *   Options for multi-select enumeration.
     * @returns {HTMLElement}
     *   Main component element.
     */
    initSelect(id, label, options = {}) {
      this.labelElement.htmlFor = id;
      this.labelElement.innerText = label;
      const componentElement = document.createElement("select");
      componentElement.id = id;
      for (const ol in options) {
        this.addOption(ol, options[ol]);
      }
      this.setSelected(0);
      return componentElement;
    }
    refresh() {
      if ("checkbox" /* CHECK */ === this.type && this.tooltip) {
        this.tooltip.setState(this.getValue());
      }
    }
    /**
     * Sets selected index.
     *
     * @param {number} idx
     *   Index to set as selected.
     */
    setSelected(idx) {
      if ("select" /* SELECT */ !== this.type) {
        throw new Error('Settings component of type "' + this.type + '" does not support index selection');
      }
      const selectElement = this.componentElement;
      if (selectElement.options.length <= idx) {
        console.warn("Invalid index of HTMLSelectElement");
        return;
      }
      if (idx > -1) {
        selectElement.selectedIndex = idx;
      }
    }
    /**
     * Sets value of component element.
     *
     * @param {string|number|boolean} value
     *   New value to be used.
     */
    setValue(value) {
      switch (this.type) {
        case "select" /* SELECT */:
          const options = Array.from(this.componentElement.options).map((o) => o.value);
          const idx = typeof value === "number" ? value : options.indexOf(value);
          this.setSelected(idx);
          break;
        case "checkbox" /* CHECK */:
          this.componentElement.checked = value;
          break;
        default:
          this.componentElement.value = value;
      }
      this.refresh();
    }
    /**
     * Retrieves current value of component.
     *
     * Returns the following for each component type:
     * - select: (`number`) selected index
     * - check box: (`boolean`) checked state
     * - default: (`string`) elements text value
     *
     * @returns {string|number|boolean}
     *   Component value.
     */
    getValue() {
      switch (this.type) {
        case "select" /* SELECT */:
          return this.componentElement.selectedIndex;
          break;
        case "checkbox" /* CHECK */:
          return this.componentElement.checked;
          break;
        default:
          return this.componentElement.value;
      }
    }
    setConfigId(cid) {
      switch (this.type) {
        case "select" /* SELECT */:
          this.setValue(ConfigManager.get().getInt(cid));
          break;
        case "checkbox" /* CHECK */:
          this.setValue(ConfigManager.get().getBoolean(cid));
          break;
        default:
          this.setValue(ConfigManager.get().get(cid));
      }
      this.setConfigListener(cid);
    }
    /**
     * Adds a selectable option.
     *
     * @param {string} label
     *   Displayed text.
     * @param {string} [value=""]
     *   Value the option represents.
     */
    addOption(label, value = "") {
      if ("select" /* SELECT */ !== this.type) {
        throw new Error('Settings component of type "' + this.type + '" does not support adding selection options');
      }
      if (!value) {
        value = label;
      }
      const selectElement = this.componentElement;
      const opt = document.createElement("option");
      opt.value = value;
      opt.textContent = label;
      selectElement.appendChild(opt);
    }
    /**
     * Applies tooltip text to HTML element.
     *
     * @param {string|Tooltip} tooltip
     *   Tooltip object or primary text.
     * @param {string=} second
     *   Optional secondary text.
     */
    setTooltip(tooltip, second) {
      if (typeof tooltip === "string") {
        let element = this.componentElement;
        if ("select" /* SELECT */ !== this.type) {
          element = this.labelElement;
        }
        let primary = true;
        if ("checkbox" /* CHECK */ === this.type) {
          primary = this.getValue();
        }
        this.tooltip = new Tooltip(element, tooltip, second, primary);
      } else {
        this.tooltip = tooltip;
      }
    }
  };

  // ui/toolkit/SliderComponent.ts
  var SliderComponent = class extends WidgetComponent {
    /** The `HTMLElement` associated with this component. */
    componentElement;
    /** Text description. */
    labelElement;
    /**
     * Creates a new slider settings component.
     *
     * @param {string} id
     *   DOM element ID.
     * @param {string} label
     *   Text label to display.
     * @param {number} min
     *   Lowest possible value this component can represent.
     * @param {number} max
     *   Highest possible value this component can represent.
     * @param {boolean} [experimental=false]
     *   Marks this element to be hidden unless settings debugging is enabled.
     */
    constructor(id, label, min, max, experimental = false) {
      super("range" /* SLIDER */);
      this.labelElement = document.createElement("label");
      this.labelElement.htmlFor = id;
      this.labelElement.innerText = label;
      const componentElement = document.createElement("input");
      componentElement.type = this.type;
      componentElement.id = id;
      componentElement.min = "" + min;
      componentElement.max = "" + max;
      this.componentElement = componentElement;
      this.initChangeListener();
      if (experimental) {
        this.componentElement.classList.add("experimental");
      }
    }
    /**
     * Sets component's current value representation.
     *
     * @param {number} value
     *   Current value.
     */
    setValue(value) {
      this.componentElement.value = "" + value;
    }
    /**
     * Retrieves component's current value representation.
     *
     * @returns {number}
     *   Current value.
     */
    getValue() {
      return parseInt(this.componentElement.value, 10);
    }
    setConfigId(cid) {
      this.setValue(ConfigManager.get().getInt(cid));
      this.setConfigListener(cid);
    }
  };

  // ui/dialog/settings/SoundTab.ts
  var SoundTab = class extends AbstractSettingsTab {
    sliders;
    constructor(parent, element) {
      super(element);
      const config2 = singletons.getConfigManager();
      const sound = singletons.getSoundManager();
      this.sliders = [];
      const col1 = this.child("#col1");
      let soundEnabled = config2.getBoolean("sound");
      const chkSound = new SettingsComponent("chk_sound", "Enable sound");
      chkSound.setConfigId("sound");
      chkSound.addListener((evt) => {
        sound.onStateChanged();
        this.setSlidersEnabled(config2.getBoolean("sound"));
      });
      chkSound.addTo(col1);
      const layers = [
        ["master", "Master"],
        ["gui", "GUI"],
        ["sfx", "Effects"],
        ["creature", "Creatures"],
        ["ambient", "Ambient"],
        ["music", "Music"]
      ];
      for (const group of layers) {
        const layer = group[0];
        const label = group[1];
        const slider = new SliderComponent("setting-vol-" + layer, label, 0, 100);
        slider.setValue(sound.getVolume(layer) * 100);
        slider.addListener(function(evt) {
          sound.setVolume(layer, slider.getValue() / 100);
        });
        slider.addTo(col1);
        this.sliders.push(slider);
      }
      this.setSlidersEnabled(soundEnabled);
    }
    setSlidersEnabled(enabled) {
      for (const slider of this.sliders) {
        slider.setEnabled(enabled);
      }
    }
  };

  // util/StandardMessages.ts
  var StandardMessages = class {
    /**
     * Static class.
     */
    constructor() {
    }
    /**
     * Message when a change is made to the client that won't take effect until after player changes
     * maps or the page is reloaded.
     */
    static changeNeedsRefresh() {
      Chat.log("client", "Changes will take effect after changing maps or reloading page.");
    }
  };

  // ui/dialog/settings/VisualsTab.ts
  var VisualsTab = class extends AbstractSettingsTab {
    constructor(parent, element) {
      super(element);
      const config2 = singletons.getConfigManager();
      const col1 = this.child("#col1");
      parent.createCheckBox(
        "chk_light",
        "effect.lighting",
        "Lighting effects are enabled",
        "Lighting effects are disabled",
        function() {
          StandardMessages.changeNeedsRefresh();
        }
      );
      parent.createCheckBox(
        "chk_weather",
        "effect.weather",
        "Weather is enabled",
        "Weather is disabled",
        function() {
          StandardMessages.changeNeedsRefresh();
        }
      );
      parent.createCheckBox(
        "chk_blood",
        "effect.blood",
        "Gory images are enabled",
        "Gory images are disabled"
      );
      parent.createCheckBox(
        "chk_nonude",
        "effect.no-nude",
        "Naked entities have undergarments",
        "Naked entities are not covered"
      );
      parent.createCheckBox(
        "chk_shadows",
        "effect.shadows",
        "Shadows are enabled",
        "Shadows are disabled"
      );
      parent.createCheckBox(
        "chk_clickindicator",
        "click-indicator",
        "Displaying clicks",
        "Not displaying clicks"
      );
      const chkAnimate = new SettingsComponent("chk_animate", "Animate");
      chkAnimate.setConfigId("activity-indicator.animate");
      chkAnimate.setEnabled(config2.getBoolean("activity-indicator"));
      chkAnimate.addListener((evt) => {
        StandardMessages.changeNeedsRefresh();
        parent.refresh();
      });
      const chkActivityInd = new SettingsComponent("chk_activityindicator", "Object activity indicator");
      chkActivityInd.setConfigId("activity-indicator");
      chkActivityInd.setTooltip("Display an indictor over certain interactive objects and corpses that aren't empty");
      chkActivityInd.addListener((evt) => {
        chkAnimate.setEnabled(chkActivityInd.getValue());
        StandardMessages.changeNeedsRefresh();
        parent.refresh();
      });
      chkActivityInd.addTo(col1);
      chkAnimate.addTo(col1);
      chkAnimate.componentElement.classList.add("indented");
      const chkParallax = new SettingsComponent("chk_parallax", "Parallax scrolling backgrounds");
      chkParallax.setTooltip("Parallax scrolling enabled", "Parallax scrolling disabled");
      chkParallax.setConfigId("effect.parallax");
      chkParallax.addListener((evt) => {
        StandardMessages.changeNeedsRefresh();
        parent.refresh();
      });
      chkParallax.addTo(col1);
      const chkEntityOverlay = new SettingsComponent(
        "chk_entity_overlay",
        "Entity overlay effects"
      );
      chkEntityOverlay.setValue(config2.getBoolean("effect.entity-overlay"));
      chkEntityOverlay.addListener((evt) => {
        config2.set("effect.entity-overlay", chkEntityOverlay.getValue());
        StandardMessages.changeNeedsRefresh();
        parent.refresh();
      });
      chkEntityOverlay.addTo(col1);
    }
  };

  // ui/dialog/SettingsDialog.ts
  var SettingsDialog = class extends TabDialogContentComponent {
    storedStates;
    initialStates;
    constructor() {
      super("settingsdialog-template", 1 /* TOP */, "#settings-tabs");
      this.storedStates = {
        "txtjoystickx": stendhal.config.get("joystick.center.x"),
        "txtjoysticky": stendhal.config.get("joystick.center.y")
      };
      this.initialStates = {
        "activity-indicator": stendhal.config.get("activity-indicator"),
        "activity-indicator.animate": stendhal.config.get("activity-indicator.animate"),
        "effect.blood": stendhal.config.get("effect.blood"),
        "effect.entity-overlay": stendhal.config.get("effect.entity-overlay"),
        "effect.lighting": stendhal.config.get("effect.lighting"),
        "effect.parallax": stendhal.config.get("effect.parallax"),
        "effect.weather": stendhal.config.get("effect.weather")
      };
      this.addTab("General", new GeneralTab(this, this.child("#settings-general")));
      this.addTab("Visuals", new VisualsTab(this, this.child("#settings-visuals")));
      this.addTab("Sound", new SoundTab(this, this.child("#settings-sound")));
      this.addTab("Input", new InputTab(this, this.child("#settings-input")));
      const btn_reload = this.addButton("Reload", () => {
        this.close();
        location.reload();
      }, "button-reload");
      btn_reload.title = "Reloads page if required by changes";
      btn_reload.disabled = true;
      const btn_close = this.addCloseButton();
      btn_close.title = "Close this dialog without reloading page";
    }
    getConfigId() {
      return "settings";
    }
    /**
     * Updates state of reload button.
     */
    refresh() {
      let reloadRequired = false;
      for (const key of Object.keys(this.initialStates)) {
        if (stendhal.config.get(key) !== this.initialStates[key]) {
          reloadRequired = true;
          break;
        }
      }
      this.child("#button-reload").disabled = !reloadRequired;
    }
    /**
     * Creates a checkbox element.
     *
     * @param id
     *     Identifier of element to retrieve.
     * @param tooltip
     *     Optional popup tooltip text.
     * @return
     *     HTMLInputElement.
     */
    createCheckBoxSkel(id, tooltip) {
      const checkbox = this.child("input[type=checkbox][id=" + id + "]");
      if (tooltip) {
        checkbox.title = tooltip;
      }
      if (checkbox.classList.contains("experimental")) {
        const label = checkbox.parentElement;
        const text = document.createElement("span");
        text.innerText = label.innerText + " (experimental)";
        label.innerHTML = "";
        label.appendChild(checkbox);
        label.appendChild(text);
        if (!Debug.isActive("settings")) {
          checkbox.disabled = true;
          checkbox.style.setProperty("display", "none");
          label.style.setProperty("display", "none");
        }
      }
      return checkbox;
    }
    /**
     * Creates a fully function checkbox element.
     *
     * @param id
     *     Identifier of element to retrieve.
     * @param setid
     *     Identifier of associated configuration setting.
     * @param ttpos
     *     Tooltip to display when setting is enabled.
     * @param ttneg
     *     Tooltip to display when setting is disabled.
     * @param action
     *     Action to execute when state changed.
     * @param von
     *     Optional value to set when enabled (<code>null</code> can be
     *     used).
     * @param voff
     *     Optional value to set when disabled (<code>null</code> can be
     *     used).
     * @return
     *     HTMLInputElement.
     */
    createCheckBox(id, setid, ttpos = "", ttneg = "", action, von, voff) {
      const chk = this.createCheckBoxSkel(id);
      chk.checked = stendhal.config.getBoolean(setid);
      let tt;
      if (ttpos !== "") {
        let element = chk;
        if (chk.parentElement) {
          element = chk.parentElement;
        }
        tt = new Tooltip(element, ttpos, ttneg !== "" ? ttneg : void 0, chk.checked);
      }
      if (!chk.disabled) {
        chk.addEventListener("change", (e) => {
          if (chk.checked && typeof von !== "undefined") {
            stendhal.config.set(setid, von);
          } else if (!chk.checked && typeof voff !== "undefined") {
            stendhal.config.set(setid, voff);
          } else {
            stendhal.config.set(setid, chk.checked);
          }
          if (tt) {
            tt.setState(chk.checked);
          }
          if (action) {
            action();
          }
          this.refresh();
        });
      }
      return chk;
    }
    /**
     * Creates a button element.
     *
     * @param id
     *     Identifier of element to retrieve.
     * @param tooltip
     *     Optional popup tooltip text.
     * @return
     *     HTMLButtonElement.
     */
    createButton(id, tooltip) {
      const button = this.child(
        "button[id=" + id + "]"
      );
      if (tooltip) {
        button.title = tooltip;
      }
      return button;
    }
    /**
     * Creates a select element.
     *
     * @param id
     *   Identifier of element to retrieve.
     * @param opts
     *   A {string: string} (identifier: visible text) object for available options.
     * @param idx
     *   The index to set as selected on construction.
     * @param tooltip
     *   Optional popup tooltip text.
     * @param action
     *   Action to execute when state changed.
     * @return
     *   `HTMLSelectElement`.
     */
    createSelect(id, opts, idx, tooltip, action) {
      if (Object.keys(opts).length == 0) {
        console.warn('initializing empty values for selector ID "' + id + '"');
      }
      const sel = this.child("select[id=" + id + "]");
      sel.style.setProperty("width", "9em");
      sel.parentElement.style.setProperty("margin-right", "0");
      sel.parentElement.style.setProperty("margin-left", "auto");
      sel.parentElement.style.setProperty("padding-bottom", "5px");
      for (const key of Object.keys(opts)) {
        if (!opts[key]) {
          opts[key] = key;
        }
        const opt = document.createElement("option");
        opt.value = key;
        opt.textContent = opts[key];
        sel.appendChild(opt);
      }
      if (idx > -1 && idx < Object.keys(opts).length) {
        sel.selectedIndex = idx;
      }
      if (tooltip) {
        sel.title = tooltip;
      }
      if (action) {
        sel.addEventListener("change", function(e) {
          action(e);
        });
      }
      return sel;
    }
    /**
     * Creates a select element.
     *
     * @param {string} id
     *   Identifier of element to retrieve.
     * @param {string} ckey
     *   Configuration key associated with element.
     * @param {string=} tooltip
     *   Optional popup tooltip text.
     * @param {Function=} action
     *   Action to execute when state changed.
     * @returns {HTMLSelectElement}
     *   Select element.
     */
    createSelectFromConfig(id, ckey, tooltip, action) {
      const cvalue = stendhal.config.get(ckey);
      const opts = stendhal.config.getOpts(ckey);
      let idx = Object.keys(opts).indexOf(cvalue);
      if (typeof cvalue === "undefined" || idx < 0) {
        console.error('invalid value "' + cvalue + '" for configuration key "' + ckey + '". options are: ' + Object.keys(opts).join(", "));
      }
      const actionTemp = function(e) {
        if (e.target) {
          const select = e.target;
          stendhal.config.set(ckey, Object.keys(opts)[select.selectedIndex]);
        }
        if (action) {
          action(e);
        }
      };
      return this.createSelect(id, opts, idx, tooltip, actionTemp);
    }
    /**
     * Creates a select element for registered fonts.
     *
     * @param id
     *   Identifier of element to retrieve.
     * @param idx
     *   The index to set as selected on construction.
     * @param tooltip
     *   Optional popup tooltip text.
     * @return
     *   `HTMLSelectElement`.
     */
    createFontSelect(id, idx, tooltip) {
      return this.createSelect(id, Object.assign({}, stendhal.config.fonts), idx, tooltip);
    }
    /**
     * Creates a text input element.
     *
     * @param id
     *     Identifier of element to retrieve.
     * @param value
     *     Default content.
     * @param tooltip
     *     Optional popup tooltip text.
     * @param type
     *     Input type.
     * @return
     *     HTMLInputElement
     */
    createTextInput(id, value = "", tooltip, type = "text") {
      const input = this.child("input[type=" + type + "][id=" + id + "]");
      input.style.setProperty("width", "9em");
      input.parentElement.style.setProperty("margin-right", "0");
      input.parentElement.style.setProperty("margin-left", "auto");
      input.parentElement.style.setProperty("padding-bottom", "5px");
      input.value = value;
      if (tooltip) {
        input.title = tooltip;
      }
      return input;
    }
    /**
     * Creates a text input element that accepts only number values.
     *
     * @param id
     *     Identifier of element to retrieve.
     * @param value
     *     Default content.
     * @param tooltip
     *     Optional popup tooltip text.
     * @return
     *     HTMLInputElement
     */
    createNumberInput(id, value = 0, tooltip) {
      const input = this.createTextInput(id, "" + value, tooltip, "number");
      input.addEventListener("input", (e) => {
        const new_char = e.data;
        const new_digit = Number(new_char);
        if (new_char != void 0 && new_char.replace(/ |\t/g, "") === "" || Number.isNaN(new_digit)) {
          input.value = this.storedStates[input.id];
          return;
        }
        input.value = "" + parseInt(input.value.replace(/ |\t/g, ""), 10);
        this.storedStates[input.id] = input.value;
      });
      return input;
    }
  };

  // action/SettingsAction.ts
  var SettingsAction = class extends SlashAction {
    minParams = 0;
    maxParams = 0;
    desc = "Opens settings dialog.";
    execute(_type, _params, _remainder) {
      const wstate = stendhal.config.getWindowState("menu");
      const offset = ui.getPageOffset();
      const content = new SettingsDialog();
      const dialog = ui.createSingletonFloatingWindow(
        "Settings",
        content,
        wstate.x - offset.x,
        wstate.y - offset.y
      );
      dialog.setId("menu");
      content.setFrame(dialog);
      return true;
    }
  };

  // SlashActionRepo.ts
  var SlashActionRepo = class _SlashActionRepo {
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_SlashActionRepo.instance) {
        _SlashActionRepo.instance = new _SlashActionRepo();
      }
      return _SlashActionRepo.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Forwards action information to server.
     *
     * @param action {SlashActionRepo.Action}
     *   Action object.
     */
    sendAction(action) {
      import_marauroa48.marauroa.clientFramework.sendAction(action);
    }
    /**
     * Retrieves registered types excluding generic defaults & aliases.
     *
     * FIXME: not detecting some property names such as "manual"
     *
     * @return {string[]}
     *   List of unique type names.
     */
    getTypes() {
      const types = [];
      const actions = [this["_default"], this["debug"]];
      for (const t of Object.keys(this)) {
        const action = this[t];
        if (actions.indexOf(action) > -1) {
          continue;
        }
        actions.push(action);
        types.push(t);
      }
      return types;
    }
    /**
     * Retrieves help data for standard user actions.
     *
     * @return {any}
     */
    getUserHelpData() {
      const grouping = {
        "CHATTING": [
          "chat",
          "me",
          "tell",
          "answer",
          "/",
          "p",
          "storemessage",
          {
            type: "who",
            getHelp: function() {
              return ["", "List all players currently online."];
            }
          },
          {
            type: "where",
            getHelp: function() {
              return ["[<player>]", "Show the current location of #player."];
            }
          },
          "sentence"
        ],
        "TOOLS": [
          "progressstatus",
          "screenshot",
          //"screencap",
          "atlas",
          "beginnersguide"
        ],
        "SUPPORT": [
          "support",
          {
            type: "faq",
            getHelp: function() {
              return ["", "Open Stendhal FAQs wiki page in browser."];
            }
          }
        ],
        "ITEM MANIPULATION": [
          "drop",
          {
            type: "markscroll",
            getHelp: function() {
              return ["<text>", "Mark your empty scroll and add a #text label."];
            }
          }
        ],
        "BUDDIES AND ENEMIES": [
          "add",
          "remove",
          { type: "ignore", sparams: "<player> [minutes|*|- [reason...]]" },
          "ignore",
          {
            type: "unignore",
            getHelp: function() {
              return ["<player>", "Remove #player from your ignore list."];
            }
          }
        ],
        "STATUS": [
          { type: "away", sparams: "<message>" },
          "away",
          { type: "grumpy", sparams: "<message>" },
          "grumpy",
          {
            type: "name",
            getHelp: function() {
              return ["<pet> <name>", "Give a name to your pet."];
            }
          },
          "profile"
        ],
        "PLAYER CONTROL": [
          //"clickmode", // Switches between single click mode and double click mode.
          "walk",
          "stopwalk",
          "movecont"
        ],
        "CLIENT SETTINGS": [
          "settings",
          "mute",
          { type: "volume", sparams: "<layer> <value>" },
          "volume"
        ],
        "MISC": [
          "about",
          {
            type: "info",
            getHelp: function() {
              return ["", "Find out what the current server time is."];
            }
          },
          "clear",
          "help",
          {
            type: "removedetail",
            getHelp: function() {
              return [
                "",
                "Remove the detail layer (e.g. balloon, umbrella, etc.) from character. #WARNING: Cannot be undone."
              ];
            }
          },
          "emojilist",
          { type: "group", sparams: "invite <player>" },
          { type: "group", sparams: "join <player" },
          { type: "group", sparams: "leader <player>" },
          { type: "group", sparams: "lootmode single|shared" },
          { type: "group", sparams: "kick <player>" },
          { type: "group", sparams: "part" },
          { type: "group", sparams: "status" }
        ]
      };
      if (Debug.isActive("screencap")) {
        grouping["TOOLS"].push("screencap");
      }
      return {
        info: [
          "For a detailed reference, visit #https://stendhalgame.org/wiki/Stendhal_Manual",
          "Here are the most-used commands:"
        ],
        grouping
      };
    }
    /**
     * Retrieves help data for game master actions.
     *
     * @return {any}
     */
    getGMHelpData() {
      const grouping = {
        "GENERAL": [
          { type: "gmhelp", sparams: "[alter|script|support]" },
          "gmhelp",
          "adminnote",
          {
            type: "inspect",
            getHelp: function() {
              return ["<player>", "Show complete details of #player."];
            }
          },
          "inspectkill",
          "inspectquest",
          {
            type: "script",
            getHelp: function() {
              return [
                "<scriptname>",
                "Load (or reload) a script on the server. See #/gmhelp #script for details."
              ];
            }
          }
        ],
        "CHATTING": [
          "supportanswer",
          "tellall"
        ],
        "PLAYER CONTROL": [
          "teleportto",
          {
            type: "teleclickmode",
            getHelp: function() {
              return ["", "Makes you teleport to the location you double click."];
            }
          },
          {
            type: "ghostmode",
            getHelp: function() {
              return ["", "Makes yourself invisible and intangible."];
            }
          },
          {
            type: "invisible",
            getHelp: function() {
              return ["", "Toggles whether or not you are invisible to creatures."];
            }
          }
        ],
        "ENTITY MANIPULATION": [
          "adminlevel",
          "jail",
          "gag",
          "ban",
          "teleport",
          "alter",
          "altercreature",
          "alterkill",
          "alterquest",
          { type: "summon", sparams: "<creature>|<item> [<x> <y>]" },
          { type: "summon", sparams: "<stackable_item> [<x> <y>] [quantity]" },
          "summonat",
          {
            type: "destroy",
            getHelp: function() {
              return ["<entity>", "Destroy an entity completely."];
            }
          }
        ],
        "MISC": [
          {
            type: "jailreport",
            getHelp: function() {
              return ["[<player>]", "List the jailed players and their sentences."];
            }
          }
        ]
      };
      return {
        info: [
          "For a detailed reference, visit #https://stendhalgame.org/wiki/Stendhal:Administration",
          "Here are the most-used GM commands:"
        ],
        grouping
      };
    }
    /**
     * Compiles help information for registered actions.
     *
     * TODO: cache compiled help info
     *
     * @param gm {boolean}
     *   Pull info for GM help commands instead of standard user help (default: `false`).
     * @return {string[]}
     *   Help info string array.
     */
    getHelp(gm = false) {
      let help;
      let stripHelp;
      if (gm) {
        help = this.getGMHelpData();
        stripHelp = this.getUserHelpData();
      } else {
        help = this.getUserHelpData();
        stripHelp = this.getGMHelpData();
      }
      const types = this.getTypes();
      for (const gname in stripHelp.grouping) {
        for (let t of stripHelp.grouping[gname]) {
          if (typeof t !== "string") {
            t = t.type;
          }
          types.splice(types.indexOf(t), 1);
        }
      }
      const unavailable = [];
      for (const gname in help.grouping) {
        help.info.push("&nbsp;");
        help.info.push(gname + ":");
        for (let t of help.grouping[gname]) {
          let actionHelp = ["- \xA7'/"];
          let action;
          if (typeof t === "string") {
            actionHelp[0] += t;
            action = this[t];
          } else {
            actionHelp[0] += t.type;
            action = t || this[t.type];
            action.getHelp = action.getHelp || this[t.type].getHelp;
            t = t.type;
          }
          const typeIndex = types.indexOf(t);
          if (!action || !action.getHelp) {
            unavailable.push(t);
            actionHelp[0] += "'";
            help.info.push(actionHelp[0]);
            if (typeIndex > -1) {
              types.splice(typeIndex, 1);
            }
            continue;
          }
          const helpTemp = action.getHelp(action.sparams);
          if (helpTemp && helpTemp.length) {
            if (helpTemp[0]) {
              actionHelp[0] += " " + helpTemp[0];
            }
            helpTemp.splice(0, 1);
            for (const li of helpTemp) {
              actionHelp.push("&nbsp;&nbsp;" + li);
            }
          } else {
            unavailable.push(t);
          }
          if (action.aliases) {
            actionHelp.push("&nbsp;&nbsp;Aliases: " + action.aliases.join(", "));
          }
          actionHelp[0] += "'";
          help.info = [...help.info, ...actionHelp];
          if (typeIndex > -1) {
            types.splice(typeIndex, 1);
          }
        }
      }
      for (const t of types) {
        if (unavailable.indexOf(t) < 0) {
          unavailable.push(t);
        }
      }
      if (unavailable.length) {
        console.warn("help information is unavailable for actions:", unavailable.join(", "));
      }
      return help.info;
    }
    "help" = {
      execute: (type, params, remainder) => {
        const msg = this.getHelp();
        for (var i = 0; i < msg.length; i++) {
          Chat.log("info", msg[i]);
        }
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "Show this help message."];
      }
    };
    "gmhelp" = {
      execute: (type, params, remainder) => {
        var msg = null;
        if (params[0] == null) {
          msg = this.getHelp(true);
        } else if (params.length == 1 && params[0] != null) {
          if ("alter" == params[0]) {
            msg = [
              "/alter <player> <attrib> <mode> <value>",
              "		Alter stat <attrib> of <player> by the given amount; <mode> can be ADD, SUB, SET or UNSET.",
              "		- Examples of <attrib>: atk, def, base_hp, hp, atk_xp, def_xp, xp, outfit",
              "		- When modifying 'outfit', you should use SET mode and provide an 8-digit number; the first 2 digits are the 'hair' setting, then 'head', 'outfit', then 'body'",
              "		  For example: #'/alter testplayer outfit set 12109901'",
              "		  This will make <testplayer> look like danter"
            ];
          } else if ("script" == params[0]) {
            msg = [
              "usage: /script [-list|-load|-unload|-execute] [params]",
              `	-list : shows available scripts. In this mode can be given one optional parameter for filenames filtering, with using well-known wildcards for filenames ('*' and '?', for example "*.class" for java-only scripts).`,
              "	-load : load script with first parameter's filename.",
              "	-unload : unload script with first parameter's filename from server",
              "	-execute : run selected script.",
              "",
              "All scripts are ran using: /script scriptname [params]. After running a script you can remove any traces of it with /script -unload scriptname, this would remove any summoned creatures, for example. It's good practise to do this after summoning creatures for a raid using scripts.",
              "#/script #AdminMaker.class : For test servers only, summons an adminmaker to aid testing.",
              "#/script #AdminSign.class #zone #x #y #text : Makes an AdminSign in zone at (x,y) with text. To put it next to you do /script AdminSign.class - - - text.",
              "#/script #AlterQuest.class #player #questname #state : Update the quest for a player to be in a certain state. Omit #state to remove the quest.",
              "#/script #DeepInspect.class #player : Deep inspects a player and all his/her items.",
              "#/script #DropPlayerItems.class #player #[amount] #item : Drop the specified amount of items from the player if they are equipped in the bag or body.",
              "#/script #EntitySearch.class #nonrespawn : Shows the locations of all creatures that don't respawn, for example creatures that were summoned by a GM, deathmatch creatures, etc.",
              "#/script #FixDM.class #player : sets a player's DeathMatch slot to victory status.",
              "#/script #ListNPCs.class : lists all npcs and their position.",
              "#/script #LogoutPlayer.class #player : kicks a player from the game.",
              "#/script #NPCShout.class #npc #text : NPC shouts text.",
              "#/script #NPCShoutZone.class #npc #zone #text : NPC shouts text to players in given zone. Use - in place of zone to make it your current zone.",
              "#/script #Plague.class #1 #creature : summon a plague of raid creatures around you.",
              "#/script #WhereWho.class : Lists where all the online players are",
              "#/script #Maria.class : Summons Maria, who sells food&drinks. Don't forget to -unload her after you're done.",
              "#/script #ServerReset.class : use only in a real emergency to shut down server. If possible please warn the players to logout and give them some time. It kills the server the hard way.",
              "#/script #ResetSlot.class #player #slot : Resets the named slot such as !kills or !quests. Useful for debugging."
            ];
          } else {
            return false;
          }
        } else {
          return false;
        }
        for (var i = 0; i < msg.length; i++) {
          Chat.log("info", msg[i]);
        }
        return true;
      },
      minParams: 0,
      maxParams: 1,
      getHelp: function(sparams) {
        if (sparams) {
          return [sparams, "For more info about alter, script or the supportanswer shortcuts."];
        }
        return ["", "Show this help message."];
      }
    };
    "about" = new AboutAction();
    "add" = {
      execute: (type, params, remainder) => {
        if (params == null) {
          return false;
        }
        ;
        const action = {
          "type": "addbuddy",
          "target": params[0]
        };
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 1,
      getHelp: function() {
        return ["<player>", "Add #player to your buddy list."];
      }
    };
    "adminnote" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": type,
          "target": params[0],
          "note": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 1,
      getHelp: function() {
        return ["<player> <note>", "Logs a note about #player."];
      }
    };
    "adminlevel" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": type,
          "target": params[0]
        };
        if (params.length >= 2) {
          action["newlevel"] = params[1];
        }
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 2,
      getHelp: function() {
        return ["<player> [<newlevel>]", "Display or set the adminlevel of the specified #player."];
      }
    };
    "alter" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": type,
          "target": params[0],
          "stat": params[1],
          "mode": params[2],
          "value": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 3,
      maxParams: 3,
      getHelp: function() {
        return [
          "<player> <attrib> <mode> <value>",
          "Alter stat #attrib of #player by the given amount; #mode can be ADD, SUB, SET or UNSET. See #/gmhelp #alter for details."
        ];
      }
    };
    "altercreature" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "altercreature",
          "target": params[0],
          "text": params[1]
        };
        this.sendAction(action);
        return true;
      },
      minParams: 2,
      maxParams: 2,
      getHelp: function() {
        return [
          "<id> set|unset|add|sub <attribute> [<value>]",
          "Change values of the creature. Use #- as a placeholder to keep default value. Useful in raids."
        ];
      }
    };
    "alterkill" = {
      execute: (type, params, remainder) => {
        const target = params[0];
        const killtype = params[1];
        const count = params[2];
        var creature = null;
        if (remainder != null && remainder != "") {
          creature = remainder;
        }
        const action = {
          "type": "alterkill",
          "target": target,
          "killtype": killtype,
          "count": count
        };
        if (creature != null) {
          action["creature"] = creature;
        }
        this.sendAction(action);
        return true;
      },
      minParams: 3,
      maxParams: 3,
      getHelp: function() {
        return [
          "<player> <type> <count> <creature>",
          'Change number of #creature killed #type ("solo" or "shared") to #count for #player.'
        ];
      }
    };
    "alterquest" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "alterquest",
          "target": params[0],
          "name": params[1]
        };
        if (params[2] != null) {
          action["state"] = this.checkQuoted(params[2], remainder);
        }
        this.sendAction(action);
        return true;
      },
      minParams: 2,
      maxParams: 3,
      getHelp: function() {
        return ["<player> <questslot> <value>", "Update the #questslot for #player to be #value."];
      }
    };
    "answer" = {
      execute: (type, params, remainder) => {
        if (remainder == null || remainder == "") {
          return false;
        }
        ;
        const action = {
          "type": "answer",
          "text": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 0,
      getHelp: function() {
        return ["<message>", "Send a private message to the last player who sent a message to you."];
      }
    };
    "away" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "away"
        };
        if (remainder.length != 0) {
          action["message"] = remainder;
        }
        ;
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function(sparams) {
        if (sparams === "<message>") {
          return [sparams, "Set an away message."];
        }
        return ["", "Remove away status."];
      }
    };
    "ban" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "ban",
          "target": params[0],
          "hours": params[1],
          "reason": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 2,
      maxParams: 2,
      getHelp: function() {
        return [
          "<character> <hours> <reason>",
          "Bans the account of the character from logging onto the game server or website for the specified amount of hours (-1 till end of time)."
        ];
      }
    };
    "chat" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": type,
          "text": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return [
          "<message>",
          "Send a public chat message (same as sending text without prefixed chat command)."
        ];
      }
    };
    "clear" = {
      execute: (type, params, remainder) => {
        ui.get(UIComponentEnum.ChatLog).clear();
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "Clear chat log."];
      }
    };
    /*
    	"clickmode": SlashActionImpl = {
    		execute: (type: string, params: string[], remainder: string): boolean => {
    			const newMode = !stendhal.config.getBoolean("input.doubleclick");
    			stendhal.config.set("input.doubleclick", newMode);
    			ViewPort.get().updateClickMode();
    
    			if (newMode) {
    				Chat.log("info", "Click mode is now set to double click.");
    			} else {
    				Chat.log("info", "Click mode is now set to single click.");
    			}
    			return true;
    		},
    		minParams: 0,
    		maxParams: 0
    	};
    	*/
    "debug" = new DebugAction();
    "drop" = {
      execute: (type, params, remainder) => {
        console.log(type, params, remainder);
        let name = remainder;
        let quantity = parseInt(params[0], 10);
        console.log(name, quantity);
        if (isNaN(quantity)) {
          name = (params[0] + " " + remainder).trim();
          quantity = 0;
        }
        console.log(name, quantity);
        const action = {
          "type": "drop",
          "source_name": name,
          "quantity": "" + quantity,
          "x": "" + import_marauroa48.marauroa.me.x,
          "y": "" + import_marauroa48.marauroa.me.y
        };
        console.log(action);
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 1,
      getHelp: function() {
        return ["[<quantity>] <item_name>", "Drops items from inventory where you stand."];
      }
    };
    "emojilist" = {
      execute: (type, params, remainder) => {
        const emojilist = singletons.getEmojiStore().getEmojiList().sort();
        for (const idx in emojilist) {
          emojilist[idx] = "&nbsp;&nbsp;- :" + emojilist[idx] + ":";
        }
        emojilist.splice(0, 0, emojilist.length + " emojis available:");
        Chat.log("client", emojilist);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "List available emojis."];
      }
    };
    "gag" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "gag",
          "target": params[0],
          "minutes": params[1],
          "reason": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 2,
      maxParams: 2,
      getHelp: function() {
        return [
          "<player> <minutes> <reason>",
          "Gags #player for a given length of time (player is unable to send messages to anyone)."
        ];
      }
    };
    "group" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "group_management",
          "action": params[0],
          "params": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 1,
      getHelp: function(sparams) {
        const desc = [];
        if (sparams === "invite <player>") {
          desc.push("Invite a player to join your group.");
        } else if (sparams === "join <player") {
          desc.push("Accept invite to join #player's group.");
        } else if (sparams === "leader <player>") {
          desc.push("Make #player leader of group.");
        } else if (sparams === "lootmode single|shared") {
          desc.push("Set looting mode for group.");
          desc.push("&nbsp;&nbsp;single: Only group leader can loot.");
          desc.push("&nbsp;&nbsp;shared: Any group member can loot.");
        } else if (sparams === "kick <player>") {
          desc.push("Kick #player from group.");
        } else if (sparams === "part") {
          desc.push("Leave group.");
        } else if (sparams === "status") {
          desc.push("Broken?");
        }
        return [sparams, ...desc];
      }
    };
    "grumpy" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "grumpy"
        };
        if (remainder.length != 0) {
          action["reason"] = remainder;
        }
        ;
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function(sparams) {
        if (sparams === "<message>") {
          return [sparams, "Set a message to ignore all non-buddies."];
        }
        return ["", "Remove grumpy status."];
      }
    };
    "ignore" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "ignore"
        };
        if (params[0] == null) {
          action["list"] = "1";
        } else {
          action["target"] = params[0];
          var duration = params[1];
          if (duration != null) {
            if (duration != "*" && duration != "-") {
              if (isNaN(parseInt(duration, 10))) {
                return false;
              }
              action["duration"] = duration;
            }
          }
          if (remainder.length != 0) {
            action["reason"] = remainder;
          }
        }
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 2,
      getHelp: function(sparams) {
        if (sparams) {
          return [sparams, "Add #player to your ignore list."];
        }
        return ["", "Find out who is on your ignore list."];
      }
    };
    "inspectkill" = {
      execute: (type, params, remainder) => {
        const target = params[0];
        var creature = null;
        if (remainder != null && remainder != "") {
          creature = remainder;
        }
        const action = {
          "type": "inspectkill",
          "target": target
        };
        if (creature != null) {
          action["creature"] = creature;
        }
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 1,
      getHelp: function() {
        return ["<player> <creature>", "Show creature kill counts of #player for #creature."];
      }
    };
    "inspectquest" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "inspectquest",
          "target": params[0]
        };
        if (params.length > 1) {
          action["quest_slot"] = params[1];
        }
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 2,
      getHelp: function() {
        return ["<player> [<quest_slot>]", "Show the state of quest for #player."];
      }
    };
    "jail" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "jail",
          "target": params[0],
          "minutes": params[1],
          "reason": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 2,
      maxParams: 2,
      getHelp: function() {
        return ["<player> <minutes> <reason>", "Imprisons #player for a given length of time."];
      }
    };
    "me" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "emote",
          "text": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["<action>", "Show a message about what you are doing."];
      }
    };
    "movecont" = {
      execute: (type, params, remainder) => {
        const enable = !stendhal.config.getBoolean("move.cont");
        const action = {
          "type": "move.continuous"
        };
        if (enable) {
          action["move.continuous"] = "";
        }
        this.sendAction(action);
        stendhal.config.set("move.cont", enable);
        Chat.log("info", "Continuous movement " + (enable ? "enabled" : "disabled") + ".");
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return [
          "",
          "Toggle continuous movement (allows players to continue walking after map change or teleport without releasing direction key)."
        ];
      }
    };
    "tell" = new TellAction();
    "msg" = this["tell"];
    "/" = new ReTellAction();
    "mute" = {
      execute: (type, params, remainder) => {
        stendhal.sound.toggleSound();
        if (stendhal.config.getBoolean("sound")) {
          Chat.log("info", "Sounds are now on.");
        } else {
          Chat.log("info", "Sounds are now off.");
        }
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "Mute or unmute the sounds."];
      }
    };
    "p" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "group_message",
          "text": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["<message>", "Send a message to group members."];
      }
    };
    "progressstatus" = new ProgressStatusAction();
    "remove" = {
      execute: (type, params, remainder) => {
        if (params == null) {
          return false;
        }
        const action = {
          "type": "removebuddy",
          "target": params[0]
        };
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 1,
      getHelp: function() {
        return ["<player>", "Remove #player from your buddy list."];
      }
    };
    "screenshot" = {
      execute: (type, params, remainder) => {
        return singletons.getDownloadUtil().buildScreenshot().execute();
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "Capture a screenshot of the viewport area."];
      }
    };
    "screencap" = new ScreenCaptureAction();
    "sentence" = {
      execute: (type, params, remainder) => {
        if (params == null) {
          return false;
        }
        ;
        const action = {
          "type": "sentence",
          "value": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return [
          "<text>",
          "Set message on stendhalgame.org profile page and what players see when using #Look."
        ];
      }
    };
    "settings" = new SettingsAction();
    "stopwalk" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "walk",
          "mode": "stop"
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "Turns autowalk off."];
      }
    };
    "volume" = {
      execute: (type, params, remainder) => {
        const layername = params[0];
        let vol = params[1];
        if (typeof layername === "undefined") {
          const layers = ["master", ...stendhal.sound.getLayerNames()];
          Chat.log("info", "Please use /volume <layer> <value> to adjust the volume.");
          Chat.log("client", '<layer> is one of "' + layers.join('", "') + '"');
          Chat.log("client", "<value> is a number in the range 0 to 100.");
          Chat.log("client", "Current volume levels:");
          for (const l of layers) {
            Chat.log("client", "&nbsp;&nbsp;- " + l + " -> " + stendhal.sound.getVolume(l) * 100);
          }
        } else if (typeof vol !== "undefined") {
          if (!/^\d+$/.test(vol)) {
            Chat.log("error", "Value must be a number.");
            return true;
          }
          if (stendhal.sound.setVolume(layername, parseInt(vol, 10) / 100)) {
            Chat.log("client", 'Channel "' + layername + '" volume set to ' + stendhal.sound.getVolume(layername) * 100 + ".");
          } else {
            Chat.log("error", 'Unknown layer "' + layername + '".');
          }
        } else {
          Chat.log("error", "Please use /volume for help.");
        }
        return true;
      },
      minParams: 0,
      maxParams: 2,
      getHelp: function(sparams) {
        if (sparams) {
          return [sparams, "Lists or sets the volume for sound and music."];
        }
        return ["", "Shows current volume levels."];
      }
    };
    "summon" = {
      execute: (type, params, remainder) => {
        var x = null;
        var y = null;
        var quantity = null;
        var nameBuilder = [];
        for (var idx = 0; idx < params.length; idx++) {
          const str = params[idx];
          if (str.match("[0-9].*")) {
            if (x == null) {
              x = str;
            } else if (y == null) {
              y = str;
            } else if (quantity == null) {
              quantity = str;
            } else {
              nameBuilder.push(str);
            }
          } else {
            nameBuilder.push(str);
          }
        }
        if (quantity == null && y == null && x != null) {
          quantity = x;
          x = null;
        }
        var creature = nameBuilder.join(" ");
        if (x == null || y == null) {
          x = import_marauroa48.marauroa.me.x.toString();
          y = import_marauroa48.marauroa.me.y.toString();
        }
        const action = {
          "type": type,
          "creature": creature,
          "x": x,
          "y": y
        };
        if (quantity != null) {
          action["quantity"] = quantity;
        }
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: -1,
      // XXX: is this the proper way to allow an unlimited number of arguments?
      getHelp: function(sparams) {
        let desc;
        if (sparams === "<creature>|<item> [<x> <y>]") {
          desc = "Summon a creature.";
        } else if (sparams === "<stackable_item> [<x> <y>] [quantity]") {
          desc = "Summon the specified item or creature at co-ordinates #x, #y in the current zone.";
        }
        return [sparams, desc];
      }
    };
    "summonat" = {
      execute: (type, params, remainder) => {
        var amount = params[2];
        if (isNaN(parseInt(amount, 10))) {
          if (remainder) {
            remainder = amount + " " + remainder;
          } else {
            remainder = amount;
          }
          amount = "1";
        }
        const action = {
          "type": type,
          "target": params[0],
          "slot": params[1],
          "amount": amount,
          "item": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 3,
      maxParams: 3,
      getHelp: function() {
        return [
          "<player> <slot> [amount] <item>",
          "Summon the specified item into the specified slot of <player>; <amount> defaults to 1 if not specified."
        ];
      }
    };
    "support" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "support",
          "text": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["<message>", "Ask an administrator for help."];
      }
    };
    "supportanswer" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "supportanswer",
          "target": params[0],
          "text": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 1,
      aliases: ["supporta"],
      getHelp: function() {
        return [
          "<player> <message>",
          "Replies to a support question. Replace #message with $faq, $faqsocial, $ignore, $faqpvp, $wiki, $knownbug, $bugstracker, $rules, $notsupport or $spam shortcuts if desired."
        ];
      }
    };
    "supporta" = this["supportanswer"];
    "teleport" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "teleport",
          "target": params[0],
          "zone": params[1],
          "x": params[2],
          "y": params[3]
        };
        this.sendAction(action);
        return true;
      },
      minParams: 4,
      maxParams: 4,
      getHelp: function() {
        return ["<player> <zone> <x> <y>", "Teleport #player to the given location."];
      }
    };
    "teleportto" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "teleportto",
          "target": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["<name>", "Teleport yourself near the specified player or NPC."];
      }
    };
    "tellall" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "tellall",
          "text": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["<message>", "Send a private message to all logged-in players."];
      }
    };
    "walk" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "walk"
        };
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "Toggles autowalk on/off."];
      }
    };
    "atlas" = {
      execute: (type, params, remainder) => {
        window.location.href = "https://stendhalgame.org/world/atlas.html?me=" + import_marauroa48.marauroa.currentZoneName + "." + import_marauroa48.marauroa.me.x + "." + import_marauroa48.marauroa.me.y;
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "Opens atlas page on stendhalgame.org."];
      }
    };
    "beginnersguide" = {
      execute: (type, params, remainder) => {
        window.location.href = "https://stendhalgame.org/wiki/Stendhal_Beginner's_Guide";
        return true;
      },
      minParams: 0,
      maxParams: 0,
      getHelp: function() {
        return ["", "Opens beginner's guide wiki page on stendhalgame.org."];
      }
    };
    "characterselector" = new OpenWebsiteAction("https://stendhalgame.org/account/mycharacters.html");
    "faq" = new OpenWebsiteAction("https://stendhalgame.org/wiki/Stendhal_FAQ");
    "manual" = new OpenWebsiteAction("https://stendhalgame.org/wiki/Stendhal_Manual/Controls_and_Game_Settings");
    "profile" = {
      execute: (type, params, remainder) => {
        var url = "https://stendhalgame.org/character/";
        var name = import_marauroa48.marauroa.me["_name"] || singletons.getSessionManager().getCharName();
        if (params.length > 0 && params[0] != null) {
          name = params[0];
        }
        if (!name) {
          console.warn("failed to get default character name!");
          return true;
        }
        url += name + ".html";
        Chat.log("info", "Trying to open #" + url + " in your browser.");
        window.location.href = url;
        return true;
      },
      minParams: 0,
      maxParams: 1,
      getHelp: function() {
        return ["[<name>]", "Opens a player profile page on stendhalgame.org."];
      }
    };
    "rules" = new OpenWebsiteAction("https://stendhalgame.org/wiki/Stendhal_Rules");
    "changepassword" = new OpenWebsiteAction("https://stendhalgame.org/account/change-password.html");
    "loginhistory" = new OpenWebsiteAction("https://stendhalgame.org/account/history.html");
    "logout" = new OpenWebsiteAction("/account/logout.html");
    "halloffame" = new OpenWebsiteAction("https://stendhalgame.org/world/hall-of-fame/active_overview.html");
    "storemessage" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": "storemessage",
          "target": params[0],
          "text": remainder
        };
        this.sendAction(action);
        return true;
      },
      minParams: 1,
      maxParams: 1,
      getHelp: function() {
        return ["<player> <message>", "Store a private message to deliver for an offline #player."];
      }
    };
    /** Default action executed if a type is not registered. */
    "_default" = {
      execute: (type, params, remainder) => {
        const action = {
          "type": type
        };
        if (typeof (params[0] != "undefined")) {
          action["target"] = params[0];
          if (remainder != "") {
            action["args"] = remainder;
          }
        }
        this.sendAction(action);
        return true;
      },
      minParams: 0,
      maxParams: 1
    };
    /**
     * Parses a slash action formatted string & executes the registered action.
     *
     * @param line {string}
     *   Complete slash action line including parameters.
     * @return {boolean}
     *   `true` to represent successful execution.
     */
    execute(line) {
      line = line.trim();
      if (line.startsWith("//") && !line.startsWith("// ")) {
        line = "// " + line.substring(2);
      }
      var array = line.split(" ");
      for (var el in array) {
        array[el] = array[el].trim();
      }
      array = array.filter(Boolean);
      if (array.length == 0) {
        return false;
      }
      var name = array[0];
      if (name[0] != "/") {
        name = "/chat";
      } else {
        array.shift();
      }
      name = name.substr(1);
      var action;
      if (typeof this[name] == "undefined") {
        action = this["_default"];
      } else {
        action = this[name];
      }
      if (name == "where" && array.length == 0) {
        array[0] = import_marauroa48.marauroa.me["_name"];
      }
      if (action.minParams <= array.length) {
        var remainder = "";
        for (var i = action.maxParams; i < array.length; i++) {
          remainder = remainder + array[i] + " ";
        }
        array.slice(action.maxParams);
        return action.execute(name, array, remainder.trim());
      } else {
        Chat.log("error", "Missing arguments. Try /help");
        return false;
      }
      return true;
    }
    /**
     * Checks for quoted whitepace to be included in parameter.
     *
     * @param p {string}
     *   The parameter to be amended.
     * @param remainder {string}
     *   String to be checked for quoted whitespace.
     * @return {string}
     *   Amended parameter.
     */
    checkQuoted(p, remainder) {
      if (p.includes('"') && remainder.includes('"')) {
        let endQuote = false;
        let paramEnd = 0;
        const arr = Array.from(remainder);
        for (const c of arr) {
          if (c === " " && endQuote) {
            break;
          } else if (c === '"') {
            endQuote = !endQuote;
          }
          paramEnd++;
        }
        p = (p + " " + remainder.substring(0, paramEnd + 1)).replace(/\"/g, "").trim();
      }
      return p;
    }
  };

  // util/FacingHandler.ts
  var import_marauroa49 = __toESM(require_marauroa());
  var Rotation = class _Rotation {
    constructor(val) {
      this.val = val;
    }
    val;
    static CLOCKWISE = new _Rotation(1);
    static COUNTERCLOCKWISE = new _Rotation(-1);
  };
  var FacingHandler = class _FacingHandler {
    static UP = Direction.UP.val;
    static DOWN = Direction.DOWN.val;
    static LEFT = Direction.LEFT.val;
    static RIGHT = Direction.RIGHT.val;
    static LOWEST = _FacingHandler.UP;
    static HIGHEST = _FacingHandler.LEFT;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_FacingHandler.instance) {
        _FacingHandler.instance = new _FacingHandler();
      }
      return _FacingHandler.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Retrieves user's direction.
     *
     * @return
     *   Current facing `Direction`.
     */
    getDirection() {
      let dir = _FacingHandler.DOWN;
      if (import_marauroa49.marauroa.me) {
        dir = import_marauroa49.marauroa.me.getFaceDirection().val;
        dir = dir >= _FacingHandler.LOWEST && dir <= _FacingHandler.HIGHEST ? dir : _FacingHandler.DOWN;
      }
      return Direction.VALUES[dir];
    }
    /**
     * Determines next direction user should face.
     *
     * @param rot
     *   Whether user should turn clockwise or counterclockwise.
     * @return
     *   Next facing `Direction`.
     */
    getNextDir(rot) {
      let nextDir = this.getDirection().val + rot.val;
      if (nextDir < _FacingHandler.LOWEST) {
        nextDir = _FacingHandler.HIGHEST;
      } else if (nextDir > _FacingHandler.HIGHEST) {
        nextDir = _FacingHandler.LOWEST;
      }
      return Direction.VALUES[nextDir];
    }
    /**
     * Sets user's facing direction.
     *
     * @param dir
     *   `Direction` to face towards.
     */
    faceTo(dir) {
      import_marauroa49.marauroa.clientFramework.sendAction({
        "type": "face",
        "dir": "" + dir.val
      });
    }
    /**
     * Turns user clockwise.
     */
    turnClockwise() {
      if (!import_marauroa49.marauroa.me) {
        return;
      }
      this.faceTo(this.getNextDir(Rotation.CLOCKWISE));
    }
    /**
     * Turns user counterclockwise.
     */
    turnCounterClockwise() {
      if (!import_marauroa49.marauroa.me) {
        return;
      }
      this.faceTo(this.getNextDir(Rotation.COUNTERCLOCKWISE));
    }
  };

  // util/KeyHandler.ts
  var import_marauroa51 = __toESM(require_marauroa());

  // util/ChatCompletionHelper.ts
  var import_marauroa50 = __toESM(require_marauroa());
  var ChatCompletionHelper = class _ChatCompletionHelper {
    /** Chat commands relating to players (tab completes player names). */
    playerCommands = [
      "add",
      "adminlevel",
      "alter",
      "alterkill",
      "alterquest",
      "ban",
      "gag",
      "ignore",
      "inspect",
      "inspectkill",
      "inspectquest",
      "jail",
      "msg",
      "profile",
      "remove",
      "summonat",
      "supporta",
      "supportanswer",
      "teleport",
      "teleportto",
      "tell",
      "unignore",
      "where"
    ];
    /** Chat commands available only for admins/GMs. */
    adminCommands = [
      "adminnote",
      "alter",
      "altercreature",
      "alterkill",
      "alterquest",
      "ban",
      "destroy",
      "gag",
      "ghostmode",
      "inspect",
      "inspectkill",
      "inspectquest",
      "invisible",
      "jail",
      "jailreport",
      "script",
      "summon",
      "summonat",
      "supporta",
      "supportanswer",
      "teleclickmode",
      "teleport",
      "teleportto",
      "tellall"
    ];
    /** Chat commands not explicitly registered in SlashActionRepo. */
    unlistedCommands = [
      "destroy",
      "ghostmode",
      "info",
      "inspect",
      "invisible",
      "jailreport",
      "markscroll",
      "name",
      //"removedetail", // don't include this to prevent accidental loss of detail layer
      "script",
      "teleclickmode",
      "unignore",
      "where",
      "who"
    ];
    /** Available chat commands. */
    chatCommands = [];
    commandPrefix;
    commandIndex = -1;
    playerPrefix;
    playerIndex = -1;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves the singleton instance.
     */
    static get() {
      if (!_ChatCompletionHelper.instance) {
        _ChatCompletionHelper.instance = new _ChatCompletionHelper();
      }
      return _ChatCompletionHelper.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Called when tab key is pressed while chat input has focus.
     *
     * FIXME: Sometimes fails to detect connected players. See FIXME note in
     *        `event.PlayerLoggedOnEvent`. `entity.Player.Player` will add to list when created but
     *        this is only a partial rememedy as only players on the same map as `entity.User.User`
     *        instance will be added.
     * NOTE:  So far have only noticed players on same map as user at login don't always register
     *        a login event.
     */
    onTabKey() {
      const parts = [];
      const chatInput = ui.get(UIComponentEnum.ChatInput);
      let text = chatInput.getText();
      let iterFailsafe = 0;
      while (text.indexOf("  ") > -1) {
        text = text.replace(/  /, " ");
        iterFailsafe++;
        if (iterFailsafe > 999) {
          break;
        }
      }
      for (const p of text.split(" ")) {
        parts.push(p);
      }
      if (parts.length == 0 || !parts[0].startsWith("/")) {
        return;
      }
      this.parseChatCommands();
      if (this.commandPrefix == void 0) {
        this.commandPrefix = parts[0].substring(1, parts[0].length);
      }
      this.commandIndex++;
      if (parts.length < 2) {
        this.cycleNextCommand(parts);
        if (this.commandIndex < 0 && this.chatCommands.length > 1) {
          this.commandIndex = 0;
          this.cycleNextCommand(parts);
        }
        chatInput.setText("/" + parts[0]);
        return;
      }
      if (!stendhal.players || parts.length > 2 || this.playerCommands.indexOf(this.commandPrefix) < 0) {
        return;
      }
      if (this.playerPrefix == void 0) {
        this.playerPrefix = parts[1].toLowerCase();
      }
      this.playerIndex++;
      this.cycleNextPlayer(parts);
      if (this.playerIndex < 0 && stendhal.players.length > 1) {
        this.playerIndex = 0;
        this.cycleNextPlayer(parts);
      }
      chatInput.setText(parts.join(" "));
    }
    /**
     * Iterates slash action commands for next matching instance.
     *
     * @param parts {string[]}
     *   Content to update chat input.
     */
    cycleNextCommand(parts) {
      for (this.commandIndex; this.commandIndex < this.chatCommands.length + 1; this.commandIndex++) {
        if (this.commandIndex >= this.chatCommands.length) {
          this.commandIndex = -1;
          break;
        }
        const cmd = this.chatCommands[this.commandIndex];
        if (cmd.startsWith(this.commandPrefix)) {
          parts[0] = cmd;
          break;
        }
      }
    }
    /**
     * Iterates player names for next matching instance.
     *
     * @param parts {string[]}
     *   Content to update chat input.
     */
    cycleNextPlayer(parts) {
      if (!stendhal.players.length) {
        console.error("failed to detect available players");
        this.playerIndex--;
        return;
      }
      for (this.playerIndex; this.playerIndex < stendhal.players.length + 1; this.playerIndex++) {
        if (this.playerIndex >= stendhal.players.length) {
          this.playerIndex = -1;
          break;
        }
        const name = stendhal.players[this.playerIndex];
        if (name.toLowerCase().startsWith(this.playerPrefix)) {
          parts[parts.length - 1] = name;
          break;
        }
      }
    }
    /**
     * Extracts usable chat commands from slash action repository.
     */
    parseChatCommands() {
      if (this.chatCommands.length > 0) {
        return;
      }
      const excludes = ["/", "_default"];
      const admin = import_marauroa50.marauroa.me && import_marauroa50.marauroa.me.isAdmin();
      for (const cmd of [...Object.getOwnPropertyNames(SlashActionRepo.get()), ...this.unlistedCommands].sort()) {
        if (excludes.indexOf(cmd) > -1 || this.adminCommands.indexOf(cmd) > -1 && !admin) {
          continue;
        }
        this.chatCommands.push(cmd);
      }
    }
    /**
     * Resets command & player prefixes to default values.
     */
    reset() {
      this.commandPrefix = void 0;
      this.commandIndex = -1;
      this.playerPrefix = void 0;
      this.playerIndex = -1;
    }
  };

  // util/KeyCode.ts
  var KeyCode = class {
    static TAB = 9;
    static ENTER = 13;
    static ARROW_LEFT = 37;
    static ARROW_UP = 38;
    static ARROW_RIGHT = 39;
    static ARROW_DOWN = 40;
    // for direction control with asdw keys
    static CHAR_A = 65;
    // left
    static CHAR_W = 87;
    // up
    static CHAR_D = 68;
    // right
    static CHAR_S = 83;
    // down
    constructor() {
    }
    /**
     * Extracts key code from keyboard event.
     *
     * @param e
     *   Keyboard event.
     * @return
     *   Integer representation of key.
     */
    static extract(e) {
      if (e.which) {
        return e.which;
      }
      return e.keyCode;
    }
  };

  // util/KeyHandler.ts
  var KeyHandler = class _KeyHandler {
    static dirCodes = {
      left: KeyCode.ARROW_LEFT,
      up: KeyCode.ARROW_UP,
      right: KeyCode.ARROW_RIGHT,
      down: KeyCode.ARROW_DOWN
    };
    static translations = {
      ["" + KeyCode.CHAR_A]: "left",
      ["" + KeyCode.CHAR_W]: "up",
      ["" + KeyCode.CHAR_D]: "right",
      ["" + KeyCode.CHAR_S]: "down"
    };
    /** List of currently pressed direction keys. */
    static pressedKeys = [];
    static asdwEnabled = false;
    /**
     * Static members & methods only.
     */
    constructor() {
    }
    /**
     * Checks if any direction key is currently pressed.
     *
     * @return
     *   `true` if direction keycode found in pressed keys list.
     */
    static isDirPressed() {
      for (const dir of Object.keys(_KeyHandler.dirCodes).map((key) => _KeyHandler.dirCodes[key])) {
        if (_KeyHandler.pressedKeys.indexOf(dir) > -1) {
          return true;
        }
      }
      return false;
    }
    /**
     * Checks keyboard event & determines resulting action.
     *
     * @param event:
     *   Keyboard event.
     * @return {number}
     *   Ternary representation of action to be executed:
     *   -  0: no action
     *   - -1: face to action
     *   -  1: move/walk action
     */
    static checkActionEvent(event2) {
      if (event2.shiftKey) {
        return 0;
      }
      if (event2.ctrlKey) {
        return -1;
      }
      return 1;
    }
    /**
     * Converts keyboard code to `Direction`.
     *
     * @param code
     *   Code of pressed key.
     * @return
     *   Direction representation.
     */
    static extractDirectionFromKeyCode(code) {
      let dir = code - KeyCode.ARROW_LEFT;
      if (dir < Direction.UP.val) {
        dir = Direction.LEFT.val;
      }
      return Direction.VALUES[dir];
    }
    /**
     * Translates asdw key code to standard direction.
     *
     * @param code
     *   Code of pressed key.
     * @return
     *   Translated code.
     */
    static translate(code) {
      if (_KeyHandler.asdwEnabled) {
        const translated = _KeyHandler.dirCodes[_KeyHandler.translations["" + code]];
        if (typeof translated !== "undefined") {
          return translated;
        }
      }
      return code;
    }
    /**
     * Action when a key is pressed.
     *
     * @param e
     *   Keyboard event.
     */
    static onKeyDown(e) {
      var event2 = e;
      if (!event2) {
        event2 = window.event;
      }
      if (!event2) {
        return;
      }
      var code = _KeyHandler.translate(KeyCode.extract(event2));
      if (code == KeyCode.TAB) {
        event2.preventDefault();
        ChatCompletionHelper.get().onTabKey();
        return;
      }
      ChatCompletionHelper.get().reset();
      if (code == KeyCode.ENTER && !singletons.getChatInput().hasFocus()) {
        ui.get(UIComponentEnum.BottomPanel).onEnterPressed();
        return;
      }
      if (code >= KeyCode.ARROW_LEFT && code <= KeyCode.ARROW_DOWN) {
        const target = event2.target;
        if (target.tagName === "BODY" || target.tagName === "CANVAS") {
          event2.preventDefault();
        }
        if (!import_marauroa51.marauroa.me) {
          return;
        }
        if (_KeyHandler.pressedKeys.indexOf(code) > -1) {
          return;
        }
        _KeyHandler.pressedKeys.push(code);
        const actionType = _KeyHandler.checkActionEvent(event2);
        if (!actionType) {
          return;
        }
        const dir = _KeyHandler.extractDirectionFromKeyCode(code);
        if (actionType < 0) {
          import_marauroa51.marauroa.me.faceTo(dir);
        } else {
          import_marauroa51.marauroa.me.setDirection(dir, true);
        }
      } else {
        if (!event2.altKey && !event2.metaKey && !event2.ctrlKey && event2.key !== "Control") {
          if (document.activeElement && document.activeElement.localName !== "input") {
            document.getElementById("chatinput").focus();
          }
        }
      }
    }
    /**
     * Action when a key is released.
     *
     * @param e
     *   Keyboard event.
     */
    static onKeyUp(e) {
      var event2 = e;
      if (!event2) {
        event2 = window.event;
      }
      if (!event2) {
        return;
      }
      var code = _KeyHandler.translate(KeyCode.extract(event2));
      if (code >= KeyCode.ARROW_LEFT && code <= KeyCode.ARROW_DOWN) {
        if (!import_marauroa51.marauroa.me) {
          return;
        }
        const i = _KeyHandler.pressedKeys.indexOf(code);
        if (i > -1) {
          _KeyHandler.pressedKeys.splice(i, 1);
        }
        if (!_KeyHandler.isDirPressed()) {
          import_marauroa51.marauroa.me.stop();
          return;
        }
        code = _KeyHandler.pressedKeys[0];
        const actionType = _KeyHandler.checkActionEvent(event2);
        if (!actionType) {
          return;
        }
        const dir = _KeyHandler.extractDirectionFromKeyCode(code);
        if (actionType < 0) {
          import_marauroa51.marauroa.me.faceTo(dir);
        } else {
          import_marauroa51.marauroa.me.setDirection(dir);
        }
      }
    }
  };

  // data/CacheManager.ts
  var CacheManager = class _CacheManager {
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_CacheManager.instance) {
        _CacheManager.instance = new _CacheManager();
      }
      return _CacheManager.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Initializes cache.
     */
    init() {
      function requestAllItems(storeName, callback) {
        var tx = stendhal.data.cache.db.transaction(storeName, "readonly");
        var store = tx.objectStore(storeName);
        var items = [];
        tx.oncomplete = function(evt) {
          callback(items);
        };
        var cursorRequest = store.openCursor();
        cursorRequest.onerror = function(error) {
          console.log(error);
        };
        cursorRequest.onsuccess = function(evt) {
          var cursor = evt.target.result;
          if (cursor) {
            items.push(cursor.value);
            cursor.continue();
          }
        };
      }
      let cacheId = null;
      try {
        cacheId = localStorage.getItem("cache.cid");
      } catch (e) {
      }
      stendhal.data.cache.sync = {
        "cid": cacheId
      };
      if (cacheId || !window.indexedDB) {
        return;
      }
      var open = indexedDB.open("stendhal", 1);
      open.onupgradeneeded = function() {
        var db = open.result;
        db.createObjectStore("cache", { keyPath: "key" });
      };
      open.onsuccess = function() {
        stendhal.data.cache.db = open.result;
        requestAllItems("cache", function(items) {
          var len = items.length;
          for (var i = 0; i < len; i += 1) {
            stendhal.data.cache.sync[items[i].key] = items[i].data;
            try {
              cacheId = localStorage.setItem("cache.cid", items[i].data);
            } catch (e) {
            }
          }
          if (!stendhal.data.cache.sync["cid"]) {
            stendhal.data.cache.put("cid", (Math.random() * 1e48).toString(36));
          }
        });
      };
      open.onerror = function() {
        stendhal.data.cache.put("cid", (Math.random() * 1e48).toString(36));
      };
    }
    get(key) {
      return stendhal.data.cache.sync[key];
    }
    put(key, value) {
      if (stendhal.data.cache.sync[key] === value) {
        return;
      }
      stendhal.data.cache.sync[key] = value;
      try {
        localStorage.setItem("cache." + key, value);
      } catch (e) {
        console.log(e);
      }
    }
  };

  // data/CStatus.ts
  var import_marauroa52 = __toESM(require_marauroa());
  var CStatus = class _CStatus {
    initialized = false;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_CStatus.instance) {
        _CStatus.instance = new _CStatus();
      }
      return _CStatus.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    init() {
      if (this.initialized) {
        console.warn("tried to re-initialize CStatus");
        return;
      }
      this.initialized = true;
      this.send();
    }
    send() {
      if (!import_marauroa52.marauroa.me) {
        window.setTimeout(stendhal.data.cstatus.send, 1e3);
        return;
      }
      var action = {
        "type": "cstatus",
        // the client version, the server will deactivate certain features that
        // are incompatible with old clients. E. g. changing of light and dark
        // in the current zone is implemented by retransmitting the tileset
        // information. an old client would mistake that for a zone change and
        // hang.
        "version": stendhal.data.build.version,
        // The build number is especially helpful for pre releases
        "build": stendhal.data.build.build,
        // so that we can ask bug reporters to try again with the official
        // client, if they are using an unofficial one.
        "dist": stendhal.data.build.dist,
        // a client id to help with the investigation of hacked accounts
        // especially in the common "angry sibling" case.
        "cid": stendhal.data.cache.get("cid")
      };
      import_marauroa52.marauroa.clientFramework.sendAction(action);
    }
  };

  // data/EmojiStore.ts
  var EmojiStore = class _EmojiStore {
    emojilist = [];
    emojimap = {};
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     *
     * @return
     *     EmojiStore static instance.
     */
    static get() {
      if (!_EmojiStore.instance) {
        _EmojiStore.instance = new _EmojiStore();
      }
      return _EmojiStore.instance;
    }
    /**
     * Hidden singleton constructor.
     *
     * Use <code>EmojiStore.get()</code>.
     */
    constructor() {
    }
    /**
     * Loads emoji data from JSON.
     */
    init() {
      const loader = new JSONLoader();
      loader.onDataReady = () => {
        this.emojilist = loader.data["emojilist"];
        this.emojimap = loader.data["emojimap"];
      };
      loader.load(Paths.sprites + "/emoji/emojis.json");
    }
    /**
     * Creates an emoji sprite.
     *
     * @param text
     *     Text representing emoji.
     * @return
     *     <code>Image</code> or <code>undefined</code> if emoji isn't available.
     */
    create(text) {
      const filename = this.absPath(text);
      if (!filename) {
        return;
      }
      return htmlImageStore.get(filename);
    }
    /**
     * Checks if text represents an emoji.
     *
     * @param text
     *     Text to be checked.
     * @return
     *     String representing emoji sprite filename or <code>undefined</code>.
     */
    check(text) {
      text = text.replace(/\\\\/g, "\\");
      let name = this.emojimap[text];
      if (!name && (text.startsWith(":") && text.endsWith(":"))) {
        text = text.substr(0, text.length - 1).substr(1);
        if (this.isAvailable(text)) {
          name = text;
        }
      }
      return name;
    }
    /**
     * Retrieves full path to an emoji image.
     *
     * @param name
     *     Text representing emoji image filename.
     * @return
     *     String path to emoji image.
     */
    absPath(name) {
      const checked = this.check(name);
      if (checked) {
        return Paths.sprites + "/emoji/" + checked + ".png";
      }
    }
    /**
     * Checks if an emoji is registered.
     *
     * @param name
     *     Text representing emoji image filename.
     * @return
     *     <code>true</code> if name is registered.
     */
    isAvailable(name) {
      if (name.startsWith(":") && name.endsWith(":")) {
        name = name.substr(1, name.length - 2);
      }
      return this.emojilist.indexOf(name) > -1;
    }
    /**
     * Get a list of available emojis.
     *
     * @return
     *     A copy of the emoji list.
     */
    getEmojiList() {
      return [...this.emojilist];
    }
  };

  // data/GroupManager.ts
  var GroupManager = class _GroupManager {
    members = [];
    lootmode = "";
    leader = "";
    count = 0;
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_GroupManager.instance) {
        _GroupManager.instance = new _GroupManager();
      }
      return _GroupManager.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    updateGroupStatus(members, leader, lootmode) {
      this.count = 0;
      if (members) {
        var memberArray = members.substring(1, members.length - 1).split("	");
        stendhal.data.group.members = {};
        for (var i = 0; i < memberArray.length; i++) {
          stendhal.data.group.members[memberArray[i]] = true;
          this.count++;
        }
        stendhal.data.group.leader = leader;
        stendhal.data.group.lootmode = lootmode;
      } else {
        stendhal.data.group.members = [];
        stendhal.data.group.leader = "";
        stendhal.data.group.lootmode = "";
      }
    }
    /**
     * Retrieves the name of the leader of this group or an empty string.
     */
    getLeader() {
      return this.leader;
    }
    /**
     * Retrieves number of members in group.
     */
    getMemberCount() {
      return this.count;
    }
    /**
     * Requests a group management event from server to refresh status in client.
     */
    refresh() {
      SlashActionRepo.get().execute("/group status");
    }
  };

  // data/sound/LoopedSoundSourceManager.ts
  var import_marauroa53 = __toESM(require_marauroa());

  // entity/InvisibleEntity.ts
  var InvisibleEntity = class extends Entity {
    isVisibleToAction(_filter) {
      return false;
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/walk.png) 1 3, auto";
    }
  };

  // entity/LoopedSoundSource.ts
  var LoopedSoundSource = class extends InvisibleEntity {
    /**
     * Checks if a looped sound source has been loaded with this
     * entity's ID.
     *
     * @return
     *     <code>true</code> if the ID is found in the sources list.
     */
    isLoaded() {
      return typeof LoopedSoundSourceManager.get().getSources()[this["id"]] !== "undefined";
    }
  };

  // data/sound/LoopedSoundSourceManager.ts
  var sfxLoops = {
    "clock-1": true,
    "fire-1": true,
    "sleep-1": true
  };
  var LoopedSoundSourceManager = class _LoopedSoundSourceManager {
    /** Detected sound sources in current zone. */
    sources = {};
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     *
     * @return
     *     LoopedSoundSourceManager.
     */
    static get() {
      if (!_LoopedSoundSourceManager.instance) {
        _LoopedSoundSourceManager.instance = new _LoopedSoundSourceManager();
      }
      return _LoopedSoundSourceManager.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Determines whether sound file should be opened from music or sounds directory.
     *
     * @param path {string}
     *   Path sto sound file.
     * @return {boolean}
     *   `true` if "path" denotes a music sound file.
     */
    isMusic(path) {
      return !path.startsWith("loop/") && !path.startsWith("weather/") && !sfxLoops[path];
    }
    /**
     * Retrieves the looped sound sources for the current zone.
     *
     * @return {object}
     *   Sound sources detected in current zone.
     */
    getSources() {
      return this.sources;
    }
    /**
     * Adds a new looped sound source to be played.
     *
     * @param source {entity.LoopedSoundSource.LoopedSoundSource}
     *   Sound source.
     * @return {boolean}
     *   `true` if addition succeeded.
     */
    addSource(source) {
      const id = source["id"];
      if (!import_marauroa53.marauroa.me) {
        console.warn("tried to add looped sound source with ID '" + id + "' before player was ready");
        return false;
      }
      if (this.sources[id]) {
        console.warn("tried to add looped sound source with existing ID '" + id + "'");
        return true;
      }
      let snd;
      const layer = source["layer"];
      if (this.isMusic(source["sound"])) {
        snd = stendhal.sound.playLocalizedMusic(
          source["x"],
          source["y"],
          source["radius"],
          layer,
          source["sound"],
          source["volume"]
        );
      } else {
        snd = stendhal.sound.playLocalizedLoop(
          source["x"],
          source["y"],
          source["radius"],
          layer,
          source["sound"],
          source["volume"]
        );
      }
      if (!snd) {
        console.error("failed to add looped sound source with ID '" + id + "'");
        return false;
      }
      this.sources[id] = { layer, sound: snd };
      return true;
    }
    /**
     * Removes a currently playing sound source.
     *
     * @param id {string}
     *   Sound source identifier.
     * @return {boolean}
     *   `true` if removal succeeded.
     */
    removeSource(id) {
      const source = this.sources[id];
      if (typeof source === "undefined") {
        console.warn("tried to remove unknown looped sound source with ID '" + id + "'");
        return true;
      }
      delete this.sources[id];
      const errmsg = [];
      if (!stendhal.sound.stop(source.layer, source.sound)) {
        errmsg.push("failed to stop looped sound source with ID '" + id + "' (" + source.sound.src + ")");
      }
      if (this.sources[id]) {
        errmsg.push("failed to remove looped sound source with ID '" + id + "' (" + source.sound.src + ")");
      }
      if (errmsg.length > 0) {
        for (const msg of errmsg) {
          console.error(msg);
        }
        return false;
      }
      return true;
    }
    /**
     * Stops all playing looped sounds.
     *
     * FIXME: not all sounds stopped/removed
     *
     * @return {boolean}
     *   `true` if the sources list is empty & all removed sources returned successful.
     */
    removeAll() {
      let removed = true;
      for (const id in this.sources) {
        removed = removed && this.removeSource(id);
      }
      if (!removed) {
        this.sources = {};
      }
      return Object.keys(this.sources).length == 0;
    }
    /**
     * Retrieves a list of looped sound sources.
     *
     * @return {entity.LoopedSoundSource.LoopedSoundSource[]}
     *   All sound source entities in current zone.
     */
    getZoneEntities() {
      const ents = [];
      if (Zone.get().entities) {
        for (const ent of Zone.get().entities) {
          if (ent instanceof LoopedSoundSource) {
            ents.push(ent);
          }
        }
      }
      return ents;
    }
    /**
     * Called after zone is created to make sure looped sound sources are added properly.
     */
    onZoneReady() {
      for (const ent of this.getZoneEntities()) {
        if (!ent.isLoaded()) {
          this.addSource(ent);
        }
      }
    }
    /**
     * Adjusts volume level for each looped sound source in current zone.
     *
     * @param x {number}
     *   The new X coordinate of listening entity.
     * @param y {number}
     *   The new Y coordinate of listening entity.
     */
    onDistanceChanged(x, y) {
      for (const ent of this.getZoneEntities()) {
        if (ent.isLoaded()) {
          const layerName = stendhal.sound.checkLayer(ent["layer"]);
          const snd = this.sources[ent["id"]].sound;
          stendhal.sound.adjustForDistance(
            layerName,
            snd,
            ent["radius"],
            ent["x"],
            ent["y"],
            x,
            y
          );
        }
      }
    }
  };

  // ui/Inventory.ts
  var Inventory = class _Inventory {
    inventory = [];
    /** Singleton instance. */
    static instance;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_Inventory.instance) {
        _Inventory.instance = new _Inventory();
      }
      return _Inventory.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    update() {
      for (var i in this.inventory) {
        this.inventory[i].update();
      }
    }
    getInventory() {
      return this.inventory;
    }
    /**
     * Retrieves an inventory item container.
     *
     * @param element {string}
     *   HTML element associated with container.
     * @return {ItemContainerImplementation}
     *   Container with matching element or `undefined`.
     */
    getByElement(element) {
      for (const container of this.inventory) {
        if (element === container.getParentElement()) {
          return container;
        }
      }
    }
    add(comp) {
      this.inventory.push(comp);
    }
    remove(comp) {
      const idx = this.inventory.indexOf(comp);
      if (idx > -1) {
        this.inventory.splice(idx, 1);
      }
    }
    removeIndex(idx) {
      if (idx > -1 && idx < this.inventory.length) {
        this.inventory.splice(idx, 1);
      }
    }
    indexOf(comp) {
      return this.inventory.indexOf(comp);
    }
  };

  // ui/UIUpdateObserver.ts
  var UIUpdateObserver = class _UIUpdateObserver {
    vpRectObserver;
    vpScaleObserver;
    static instance;
    /**
     * Retrieves the singleton instance.
     */
    static get() {
      if (!_UIUpdateObserver.instance) {
        _UIUpdateObserver.instance = new _UIUpdateObserver();
      }
      return _UIUpdateObserver.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Initializes observers for display state & changes to viewport attributes.
     */
    init() {
      if (this.vpRectObserver) {
        console.warn("cannot re-initialize UIUpdateObserver");
        return;
      }
      this.vpRectObserver = new MutationObserver((mutations) => {
        this.onViewPortUpdate();
      });
      this.vpRectObserver.observe(
        document.getElementById("viewport"),
        { attributes: true, attributeFilter: ["left", "right", "top", "bottom", "width", "height"] }
      );
      this.vpScaleObserver = new ResizeObserver((entries, observer) => {
        this.onViewPortUpdate();
      });
      this.vpScaleObserver.observe(document.getElementById("viewport"));
      screen.orientation.addEventListener("change", (e) => {
        this.onOrientationUpdate();
      });
    }
    /**
     * Retrieves the display style of the client.
     *
     * @return
     *   String representing the client's display state.
     */
    getClientDisplay() {
      return document.getElementById("client").style["display"] || "none";
    }
    /**
     * Checks if the client's display state has changed.
     *
     * @param oldDisplay
     *   The previous display state of the client.
     * @return
     *   `true` if new state is not "none".
     */
    isClientDisplayChanged(oldDisplay) {
      return this.getClientDisplay() !== oldDisplay;
    }
    /**
     * Called when the client's "display" attribute changes.
     */
    onClientDisplayUpdate() {
    }
    /**
     * Called when an attribute of the viewport has changed.
     */
    onViewPortUpdate() {
      ui.onDisplayUpdate();
    }
    /**
     * Called when screen orientation changes.
     */
    onOrientationUpdate() {
      ui.onDisplayUpdate();
    }
  };

  // SingletonRepo.ts
  var SingletonRepo = class {
    static getCStatus() {
      return CStatus.get();
    }
    static getCacheManager() {
      return CacheManager.get();
    }
    static getChatInput() {
      return ui.get(UIComponentEnum.ChatInput);
    }
    static getClient() {
      return Client.get();
    }
    static getConfigManager() {
      return ConfigManager.get();
    }
    static getDownloadUtil() {
      return DownloadUtil;
    }
    static getEmojiStore() {
      return EmojiStore.get();
    }
    static getEventRegistry() {
      return EventRegistry.get();
    }
    static getFacingHandler() {
      return FacingHandler.get();
    }
    static getGroupManager() {
      return GroupManager.get();
    }
    static getInventory() {
      return Inventory.get();
    }
    static getJoystickController() {
      return SoftwareJoystickController.get();
    }
    static getKeyHandler() {
      return KeyHandler;
    }
    static getLoopedSoundSourceManager() {
      return LoopedSoundSourceManager.get();
    }
    static getPaths() {
      return Paths;
    }
    static getSessionManager() {
      return SessionManager.get();
    }
    static getSlashActionRepo() {
      return SlashActionRepo.get();
    }
    static getSoundManager() {
      return SoundManager.get();
    }
    static getUIUpdateObserver() {
      return UIUpdateObserver.get();
    }
  };
  var singletons = SingletonRepo;

  // ui/dialog/ApplicationMenuDialog.ts
  var ApplicationMenuDialog = class extends DialogContentComponent {
    actions = [
      {
        title: "Account",
        children: [
          {
            title: "Select character",
            action: "characterselector"
          },
          {
            title: "Login History",
            action: "loginhistory"
          },
          {
            title: "Change Password",
            action: "changepassword"
          },
          {
            title: "Logout",
            action: "logout"
          }
        ]
      },
      {
        title: "Tools",
        children: [
          {
            title: "Take Screenshot",
            action: "screenshot"
          },
          /*
          {
          	title: "Capture Video",
          	alt: "Stop Capture",
          	condition: ScreenCapture.isActive,
          	action: "screencap"
          },
          */
          {
            title: "Settings",
            action: "settings"
          }
        ]
      },
      {
        title: "Commands",
        children: [
          {
            title: "Atlas",
            action: "atlas"
          },
          {
            title: "Online Players",
            action: "who"
          },
          {
            title: "Hall of Fame",
            action: "halloffame"
          },
          {
            title: "Travel Log",
            action: "progressstatus"
          }
        ]
      },
      {
        title: "Help",
        children: [
          {
            title: "Manual",
            action: "manual"
          },
          {
            title: "FAQ",
            action: "faq"
          },
          {
            title: "Beginners Guide",
            action: "beginnersguide"
          },
          {
            title: "Commands",
            action: "help"
          },
          {
            title: "Rules",
            action: "rules"
          },
          {
            title: "About",
            action: "about"
          }
        ]
      }
    ];
    constructor() {
      super("applicationmenudialog-template");
      if (Debug.isActive("screencap")) {
        this.actions[1].children.push({
          title: "Capture Video",
          alt: "Stop Capture",
          condition: ScreenCapture.isActive,
          action: "screencap"
        });
      }
      var content = "";
      for (var i = 0; i < this.actions.length; i++) {
        content += '<div class="inlineblock buttonColumn"><h4 class="menugroup">' + HTMLUtil.esc(this.actions[i].title) + "</h4>";
        for (var j = 0; j < this.actions[i].children.length; j++) {
          const action = this.actions[i].children[j];
          let title = action.title;
          if (action.alt && action.condition && action.condition()) {
            title = action.alt;
          }
          content += '<button id="menubutton.' + action.action + '" class="menubutton">' + HTMLUtil.esc(title) + "</button><br>";
        }
        content += "</div>";
      }
      this.componentElement.innerHTML = content;
      this.componentElement.addEventListener("click", (event2) => {
        this.onClick(event2);
      });
    }
    getConfigId() {
      return "menu";
    }
    onClick(event2) {
      var cmd = event2.target.id?.substring(11);
      if (cmd) {
        singletons.getSlashActionRepo().execute("/" + cmd);
        this.componentElement.dispatchEvent(new Event("close"));
      }
      event2.preventDefault();
    }
  };

  // util/ElementClickListener.ts
  var ElementClickListener = class {
    /** Function executed when click is detected. */
    onClick;
    /** Property denoting button was pressed with mouse click. */
    clickEngaged = false;
    /** Property denoting button was pressed with tap/touch. */
    touchEngaged = 0;
    constructor(element) {
      element.addEventListener("mousedown", (evt) => {
        evt.preventDefault();
        if (evt.button == 0) {
          this.clickEngaged = true;
        }
      });
      element.addEventListener("touchstart", (evt) => {
        evt.preventDefault();
        this.touchEngaged = evt.changedTouches.length;
      }, { passive: true });
      element.addEventListener("mouseup", (evt) => {
        evt.preventDefault();
        if (this.clickEngaged && evt.button == 0 && this.onClick) {
          this.onClick(evt);
        }
        this.clickEngaged = false;
      });
      element.addEventListener("touchend", (evt) => {
        evt.preventDefault();
        const target = HTMLUtil.extractTarget(evt);
        if (this.touchEngaged == evt.changedTouches.length && target == element && this.onClick) {
          this.onClick(evt);
        }
        this.touchEngaged = 0;
      });
    }
  };

  // ui/quickmenu/QuickMenuButton.ts
  var QuickMenuButton = class extends Component {
    /** Property to determine if button is visible/disabled. */
    enabled = true;
    /** Property denoting button was pressed with mouse click. */
    clickEngaged = false;
    /** Property denoting button was pressed with tap/touch. */
    touchEngaged = 0;
    /**
     * Creates a new quick menu button.
     *
     * @param id {string}
     *   Basename of DOM element ID.
     * @param cid {ui.UIComponentEnum.UIComponentEnum}
     *   ID to optionally register component.
     */
    constructor(id, cid) {
      super("qm-" + id);
      if (typeof cid !== "undefined") {
        ui.registerComponent(cid, this);
      }
      this.componentElement.src = Paths.gui + "/quickmenu/" + id + ".png";
      this.componentElement.style["cursor"] = "url(" + Paths.sprites + "/cursor/highlight.png) 1 3, auto";
      this.componentElement.draggable = false;
      new ElementClickListener(this.componentElement).onClick = (evt) => {
        this.onClick(evt);
      };
      this.update();
    }
    /**
     * Sets button's position relative to page.
     *
     * @param x {number}
     *   X coordinate pixel position.
     * @param y {number}F
     *   Y coordinate pixel position.
     */
    setPos(x, y) {
      this.componentElement.style["left"] = x + "px";
      this.componentElement.style["top"] = y + "px";
    }
    /**
     * Sets image to be used for button.
     *
     * @param basename {string}
     *   Base filename of PNG image located in &gt;data&lt;/gui/quickmenu/ directory.
     */
    setImageBasename(basename) {
      this.componentElement.src = Paths.gui + "/quickmenu/" + basename + ".png";
    }
    /**
     * Action(s) to execute when button state is changed.
     *
     * NOTE: maybe it would be better to use `ui.toolkit.Component.refresh` instead of creating new method?
     */
    update() {
    }
  };

  // ui/quickmenu/ChatButton.ts
  var ChatButton = class extends QuickMenuButton {
    constructor() {
      super("chat", UIComponentEnum.QMChat);
    }
    /**
     * Updates button image.
     */
    update() {
      const chatPanel = ui.get(UIComponentEnum.BottomPanel);
      if (chatPanel) {
        this.setImageBasename(chatPanel.isVisible() ? "chat" : "chat-disabled");
      }
    }
    /**
     * Toggles chat panel visibility when clicked/tapped.
     */
    onClick(evt) {
      const chatPanel = ui.get(UIComponentEnum.BottomPanel);
      if (chatPanel) {
        chatPanel.setVisible(!chatPanel.isVisible());
      }
    }
  };

  // ui/quickmenu/ChatOptsButton.ts
  var ChatOptsButton = class extends QuickMenuButton {
    constructor() {
      super("chatopts");
    }
    /**
     * Opens chat options window when clicked/tapped.
     */
    onClick(e) {
      ChatOptionsDialog.createOptions();
    }
  };

  // ui/quickmenu/JoystickButton.ts
  var JoystickButton = class extends QuickMenuButton {
    constructor() {
      super("joystick", UIComponentEnum.QMJoystick);
    }
    /**
     * Updates button image.
     */
    update() {
      this.setImageBasename(stendhal.config.getBoolean("joystick") ? "joystick" : "joystick-disabled");
    }
    /**
     * Toggles joystick visibility when clicked/tapped.
     */
    onClick(evt) {
      const newState = !stendhal.config.getBoolean("joystick");
      stendhal.config.set("joystick", newState);
      SoftwareJoystickController.get().update();
    }
  };

  // ui/quickmenu/TravelLogButton.ts
  var TravelLogButton = class extends QuickMenuButton {
    constructor() {
      super("log");
    }
    /**
     * Opens travel log window when clicked/tapped.
     */
    onClick(evt) {
      SlashActionRepo.get().execute("/progressstatus");
    }
  };

  // ui/quickmenu/MenuButton.ts
  var MenuButton = class extends QuickMenuButton {
    constructor() {
      super("menu");
    }
    /**
     * Opens application menu window when clicked/tapped.
     */
    onClick(evt) {
      ui.showApplicationMenu();
    }
  };

  // ui/quickmenu/PathFindingButton.ts
  var PathFindingButton = class extends QuickMenuButton {
    constructor() {
      super("pathfinding", UIComponentEnum.QMPathFinding);
    }
    /**
     * Updates button image.
     */
    update() {
      this.setImageBasename(stendhal.config.getBoolean("pathfinding") ? "pathfinding" : "pathfinding-disabled");
    }
    /**
     * Toggles pathfinding when clicked/tapped.
     */
    onClick(evt) {
      stendhal.config.set("pathfinding", !stendhal.config.getBoolean("pathfinding"));
      this.update();
    }
  };

  // ui/quickmenu/RotateLButton.ts
  var RotateLButton = class extends QuickMenuButton {
    constructor() {
      super("rotate-l");
    }
    /**
     * Rotates character counter-clockwise when clicked/tapped.
     */
    onClick(evt) {
      FacingHandler.get().turnCounterClockwise();
    }
  };

  // ui/quickmenu/RotateRButton.ts
  var RotateRButton = class extends QuickMenuButton {
    constructor() {
      super("rotate-r");
    }
    /**
     * Rotates character clockwise when clicked/tapped.
     */
    onClick(evt) {
      FacingHandler.get().turnClockwise();
    }
  };

  // ui/quickmenu/SettingsButton.ts
  var SettingsButton = class extends QuickMenuButton {
    constructor() {
      super("settings");
    }
    /**
     * Opens settings window when clicked/tapped.
     */
    onClick(evt) {
      singletons.getSlashActionRepo().execute("/settings");
    }
  };

  // ui/quickmenu/SoundButton.ts
  var SoundButton = class extends QuickMenuButton {
    constructor() {
      super("sound", UIComponentEnum.QMSound);
    }
    /**
     * Updates button image.
     */
    update() {
      this.setImageBasename(stendhal.config.getBoolean("sound") ? "sound" : "sound-disabled");
    }
    /**
     * Toggles sound muted state when clicked/tapped.
     */
    onClick(evt) {
      stendhal.sound.toggleSound();
    }
  };

  // ui/quickmenu/QuickMenu.ts
  var QuickMenu = class _QuickMenu {
    /** Property set to prevent re-initialization. */
    static initialized = false;
    /** Determines if sub-buttons are visible. */
    static expanded = false;
    /** Horizontally grouped sub-buttons. */
    static buttonListX = [];
    /** Vertially grouped sub-buttons. */
    static buttonListY = [];
    /**
     * Static properties & methods only.
     *
     * NOTE: should this be singleton?
     */
    constructor() {
    }
    /**
     * Initializes quick menu & all sub-buttons.
     */
    static init() {
      if (_QuickMenu.initialized) {
        console.warn("WARNING: attempted to re-initialize quick menu buttons");
        return;
      }
      _QuickMenu.initialized = true;
      const btn_main = _QuickMenu.getElement();
      btn_main.style["display"] = "block";
      btn_main.style["cursor"] = "url(" + Paths.sprites + "/cursor/highlight.png) 1 3, auto";
      btn_main.draggable = false;
      _QuickMenu.buttonListX.push(new JoystickButton());
      _QuickMenu.buttonListX.push(new PathFindingButton());
      _QuickMenu.buttonListX.push(new SoundButton());
      _QuickMenu.buttonListX.push(new TravelLogButton());
      _QuickMenu.buttonListX.push(new SettingsButton());
      _QuickMenu.buttonListX.push(new MenuButton());
      _QuickMenu.buttonListY.push(new ChatButton());
      _QuickMenu.buttonListY.push(new ChatOptsButton());
      _QuickMenu.buttonListY.push(new RotateLButton());
      _QuickMenu.buttonListY.push(new RotateRButton());
      btn_main.onload = () => {
        btn_main.onload = null;
        this.refresh();
      };
      btn_main.src = Paths.gui + "/quickmenu/main.png";
      new ElementClickListener(btn_main).onClick = function(evt) {
        _QuickMenu.toggle();
      };
      _QuickMenu.update();
    }
    /**
     * Enables or disables a sub-button.
     *
     * @param id {number}
     *   Registered `ui.UIComponentEnum` ID.
     */
    static setButtonEnabled(id, enabled) {
      const button = ui.get(id);
      if (button) {
        button.enabled = enabled;
        _QuickMenu.refresh();
        _QuickMenu.update();
      }
    }
    /**
     * Updates menu positioning relative to upper-right corner of viewport.
     */
    static refresh() {
      const btn_main = _QuickMenu.getElement();
      const rect = document.getElementById("viewport").getBoundingClientRect();
      let drawLeft = rect.right - btn_main.width;
      let drawTop = rect.top;
      btn_main.style["left"] = drawLeft + "px";
      btn_main.style["top"] = drawTop + "px";
      for (const btn of _QuickMenu.buttonListX) {
        if (!btn.enabled) {
          continue;
        }
        drawLeft -= btn_main.width;
        btn.setPos(drawLeft, drawTop);
      }
      drawLeft = rect.right - btn_main.width;
      for (const btn of _QuickMenu.buttonListY) {
        if (!btn.enabled) {
          continue;
        }
        drawTop += btn_main.height;
        btn.setPos(drawLeft, drawTop);
      }
    }
    /**
     * Toggles expanded state.
     */
    static toggle() {
      _QuickMenu.expanded = !_QuickMenu.expanded;
      _QuickMenu.update();
    }
    /**
     * Updates main button image & shows or hides sub-buttons depending on expanded state.
     */
    static update() {
      if (!_QuickMenu.isVisible()) {
        return;
      }
      _QuickMenu.getElement().style["transform"] = "rotate(" + (_QuickMenu.expanded ? 90 : 0) + "deg)";
      for (const btn of [..._QuickMenu.buttonListX, ..._QuickMenu.buttonListY]) {
        btn.setVisible(_QuickMenu.expanded && btn.enabled);
      }
    }
    /**
     * Determines enabled (visible) state of menu.
     *
     * @param visible {boolean}
     *   Whether quick menu should be shown (default: `true`).
     */
    static setVisible(visible = true) {
      if (_QuickMenu.isVisible() == visible) {
        return;
      }
      _QuickMenu.expanded = false;
      _QuickMenu.update();
      _QuickMenu.getElement().style["display"] = visible ? "block" : "none";
      if (visible) {
        _QuickMenu.refresh();
      }
    }
    /**
     * Checks if menu is in enabled (visible) state.
     *
     * @return {boolean}
     *   `true` if main button element's display style is not set to "none".
     */
    static isVisible() {
      return _QuickMenu.getElement().style["display"] !== "none";
    }
    /**
     * Retrieves the master element associated with the menu.
     *
     * @return
     *   Main `HTMLImageElement`.
     */
    static getElement() {
      return document.getElementById("qm-main");
    }
  };

  // ui/toolkit/SingletonFloatingWindow.ts
  var SingletonFloatingWindow = class _SingletonFloatingWindow extends FloatingWindow {
    static visiblePopup;
    /**
     * creates and shows a SingletonFloatingWindow
     *
     * @param title title of the window
     * @param contentComponent Component that is shown inside of the window
     * @param x x-location on screen
     * @param y y-location on screen
     */
    constructor(title, contentComponent, x, y) {
      super(title, contentComponent, x, y);
      if (_SingletonFloatingWindow.visiblePopup) {
        _SingletonFloatingWindow.visiblePopup.close();
      }
      _SingletonFloatingWindow.visiblePopup = this;
    }
    close() {
      super.close();
      if (_SingletonFloatingWindow.visiblePopup === this) {
        _SingletonFloatingWindow.visiblePopup = void 0;
      }
    }
    static closeAll() {
      if (_SingletonFloatingWindow.visiblePopup) {
        _SingletonFloatingWindow.visiblePopup.close();
      }
    }
  };

  // ui/UI.ts
  var UI = class _UI {
    /** Attribute denoting readiness of display. */
    static displayReady = false;
    /** Attribute denoting readiness of user. */
    static userReady = false;
    /** List of registered components. */
    wellKnownComponents = /* @__PURE__ */ new Map();
    /**
     * Builds and displays a dialog window that is automatically closed if another is opened.
     *
     * @param title {string}
     *   Text displayed in window title bar.
     * @param contentComponent {ui.toolkit.Component.Component}
     *   Component containing contents of dialog window.
     * @param x {number}
     *   Window positioning on X axis.
     * @param y {number}
     *   Window positioning on Y axis.
     * @return {ui.toolkit.SingletonFloatingWindow.SingletonFloatingWindow}
     *   New dialog window.
     */
    createSingletonFloatingWindow(title, contentComponent, x, y) {
      return new SingletonFloatingWindow(title, contentComponent, x, y);
    }
    /**
     * Adds a component to list of known components.
     *
     * @param key {ui.UIComponentEnum.UIComponentEnum}
     *   The ID component will be indexed under.
     * @param component {ui.toolkit.Component.Component}
     *   The component to be registered.
     */
    registerComponent(key, component) {
      this.wellKnownComponents.set(key, component);
    }
    /**
     * Removes a component from list of known components.
     *
     * @param component {ui.toolkit.Component.Component}
     *   The component to be unregistered.
     */
    unregisterComponent(component) {
      for (let entry of this.wellKnownComponents.entries()) {
        if (entry[1] === component) {
          this.wellKnownComponents.delete(entry[0]);
          return;
        }
      }
    }
    /**
     * Retrieves a registered component.
     *
     * @param key {ui.UIComponentEnum.UIComponentEnum}
     *   Component ID.
     * @return {ui.toolkit.Component.Component}
     *   Component instance or `undefined` if ID isn't registered.
     */
    get(key) {
      return this.wellKnownComponents.get(key);
    }
    getPageOffset() {
      const body = document.body;
      const delem = document.documentElement;
      const offsetX = window.pageXOffset || delem.scrollLeft || body.scrollLeft;
      const offsetY = window.pageYOffset || delem.scrollTop || body.scrollTop;
      return { x: offsetX, y: offsetY };
    }
    /**
     * Creates and displays application menu window.
     */
    showApplicationMenu() {
      const dialogState = stendhal.config.getWindowState("menu");
      const menuContent = new ApplicationMenuDialog();
      const menuFrame = ui.createSingletonFloatingWindow("Menu", menuContent, dialogState.x, dialogState.y);
      menuFrame.setId("menu");
      menuContent.setFrame(menuFrame);
    }
    /**
     * Checks for attribute denoting display has been initialized.
     *
     * @return
     *   `true` if display is initialized and ready.
     */
    isDisplayReady() {
      return _UI.displayReady;
    }
    /**
     * Instructions to execute when display initialized.
     */
    onDisplayReady() {
      if (_UI.displayReady) {
        console.warn('display state was previously set to "ready"');
        return;
      }
      _UI.displayReady = true;
      const chatPanel = this.get(UIComponentEnum.BottomPanel);
      chatPanel.refresh();
      chatPanel.setVisible(stendhal.config.getBoolean("chat.visible"));
      SoftwareJoystickController.get().update();
      QuickMenu.init();
      this.onSoundUpdate();
      this.onMenuUpdate();
    }
    /**
     * Instructions to execute when display attributes are updated.
     */
    onDisplayUpdate() {
      if (!_UI.displayReady) {
        console.debug('display not in "ready" state');
        return;
      }
      this.get(UIComponentEnum.BottomPanel).refresh();
      QuickMenu.refresh();
    }
    /**
     * Instructions to execute when sound muted state changes.
     */
    onSoundUpdate() {
      this.get(UIComponentEnum.QMSound).update();
      document.getElementById("soundbutton").textContent = stendhal.config.getBoolean("sound") ? "\u{1F50A}" : "\u{1F507}";
    }
    /**
     * Instructions to execute when menu style changes.
     */
    onMenuUpdate() {
      switch (stendhal.ui.getMenuStyle()) {
        case "traditional":
          document.getElementById("menupanel").style["display"] = "";
          QuickMenu.setVisible(false);
          break;
        case "floating":
          document.getElementById("menupanel").style["display"] = "none";
          QuickMenu.setVisible(true);
          break;
      }
    }
    /**
     * Called when the initial `User` instance is constructed.
     */
    onUserReady() {
      if (_UI.userReady) {
        return;
      }
      _UI.userReady = true;
      if (stendhal.config.getBoolean("move.cont")) {
        import_marauroa54.marauroa.clientFramework.sendAction({
          "type": "move.continuous",
          "move.continuous": ""
        });
      }
    }
  };
  var ui = new UI();

  // PerceptionListener.ts
  var import_marauroa55 = __toESM(require_marauroa());
  var StendhalPerceptionListener = class extends import_marauroa55.PerceptionListener {
    /**
     * Action when an object is added to the world.
     *
     * @param obj {entity.RPObject}
     *   Object to be added.
     * @return {boolean}
     *   `true` if object was added.
     * @see marauroa.perceptionListener.onAdded
     */
    onAdded(obj) {
      if (obj["c"] === "player") {
        if (obj.hasOwnProperty("a")) {
          if (obj["a"]["name"] === stendhal.session.getCharName()) {
            obj["c"] = "user";
          }
        }
      }
      return super.onAdded(obj);
    }
    onPerceptionEnd(_type, _timestamp) {
      Zone.get().sortEntities();
      ui.get(UIComponentEnum.MiniMap).draw();
      ui.get(UIComponentEnum.BuddyList).update();
      stendhal.ui.equip.update();
      ui.get(UIComponentEnum.PlayerEquipment).update();
      if (!Client.get().loaded) {
        Client.get().loaded = true;
        window.setTimeout(function() {
          let body = document.getElementById("body");
          body.style.cursor = "auto";
          document.getElementById("client").style.display = "block";
          document.getElementById("loginpopup").style.display = "none";
          singletons.getUIUpdateObserver().init();
          ui.onDisplayReady();
        }, 300);
      }
    }
  };

  // entity/EntityRegistry.ts
  var import_marauroa72 = __toESM(require_marauroa());

  // sprite/image/ImageSprite.ts
  var ImageSprite = class {
    constructor(imageRef, offsetX, offsetY, width, height) {
      this.imageRef = imageRef;
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      this.width = width;
      this.height = height;
    }
    imageRef;
    offsetX;
    offsetY;
    width;
    height;
    drawOnto(ctx, x, y, entityWidth, entityHeight) {
      let image = this.imageRef.image;
      if (!image) {
        return;
      }
      let offsetX = this.offsetX || 0;
      let offsetY = this.offsetY || 0;
      let width = this.width || image.width;
      let height = this.height || image.height;
      x += Math.floor((entityWidth - width) / 2);
      y += Math.floor((entityHeight - height) / 2);
      ctx.drawImage(image, offsetX, offsetY, width, height, x, y, width, height);
    }
    drawOntoWithZoom(ctx, targetX, targetY, targetWidth, targetHeight) {
      let image = this.imageRef.image;
      if (!image) {
        return;
      }
      let offsetX = this.offsetX || 0;
      let offsetY = this.offsetY || 0;
      let width = this.width || image.width;
      let height = this.height || image.height;
      ctx.drawImage(image, offsetX, offsetY, width, height, targetX, targetY, targetWidth, targetHeight);
    }
    free() {
      this.imageRef.free();
    }
  };

  // entity/Blood.ts
  var Blood = class extends Entity {
    minimapShow = false;
    zIndex = 2e3;
    constructor() {
      super();
      this.imageSprite = new ImageSprite(
        images.load(Paths.sprites + "/combat/blood_red.png"),
        0,
        0,
        32,
        32
      );
    }
    set(key, value) {
      super.set(key, value);
      if (key === "amount") {
        this.imageSprite.offsetY = parseInt(value, 10) * 32;
      } else if (key === "class") {
        this.imageSprite?.free();
        this.imageSprite = new ImageSprite(
          images.load(Paths.sprites + "/combat/blood_" + value + ".png"),
          0,
          parseInt(this["amount"], 10) * 32,
          32,
          32
        );
      }
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/walk.png) 1 3, auto";
    }
    drawSpriteAt(ctx, x, y) {
      if (!stendhal.config.getBoolean("effect.blood")) {
        return;
      }
      super.drawSpriteAt(ctx, x, y);
    }
  };

  // entity/PopupInventory.ts
  var import_marauroa56 = __toESM(require_marauroa());
  var PopupInventory = class extends Entity {
    maxDistToView = 4;
    draw(ctx) {
      super.draw(ctx);
      if (this.inventory && this.inventory.isOpen() && !this.canViewContents()) {
        this.closeInventoryWindow();
      }
    }
    closeInventoryWindow() {
    }
    destroy(parent) {
      this.closeInventoryWindow();
      super.destroy(parent);
    }
    /**
     * Checks if player is close enough to view contents.
     */
    canViewContents() {
      if (!import_marauroa56.marauroa.me) {
        return false;
      }
      return this.getDistanceTo(import_marauroa56.marauroa.me) <= this.maxDistToView;
    }
  };

  // entity/Chest.ts
  var import_marauroa57 = __toESM(require_marauroa());
  var Chest = class extends PopupInventory {
    minimapShow = true;
    minimapStyle = Color.CHEST;
    zIndex = 5e3;
    open = false;
    constructor() {
      super();
      this.imageSprite = new ImageSprite(
        images.load(Paths.sprites + "/chest.png"),
        0,
        0,
        32,
        32
      );
    }
    set(key, value) {
      super.set(key, value);
      if (key === "open") {
        this.imageSprite.offsetY = 32;
        this.open = true;
      }
      if (this.isNextTo(import_marauroa57.marauroa.me)) {
        this.openInventoryWindow();
      }
    }
    unset(key) {
      super.unset(key);
      if (key === "open") {
        this.imageSprite.offsetY = 0;
        this.open = false;
        if (this.inventory && this.inventory.isOpen()) {
          this.inventory.close();
          this.inventory = void 0;
        }
      }
    }
    buildActions(list) {
      super.buildActions(list);
      const that = this;
      if (this.open) {
        list.push({
          title: "Inspect",
          action: function(_entity) {
            that.checkOpenInventoryWindow();
          }
        });
      }
      list.push({
        title: that.open ? "Close" : "Open",
        action: function(_entity) {
          that.onUsed();
        }
      });
    }
    isVisibleToAction(_filter) {
      return true;
    }
    onclick(_x, _y) {
      if (import_marauroa57.marauroa.me.isNextTo(this)) {
        if (!this.open) {
          this.onUsed();
        } else {
          this.openInventoryWindow();
        }
      } else {
        if (this.open) {
          this.checkOpenInventoryWindow();
        }
      }
    }
    onUsed() {
      var action = {
        "type": "use",
        "target": "#" + this["id"],
        "zone": import_marauroa57.marauroa.currentZoneName
      };
      import_marauroa57.marauroa.clientFramework.sendAction(action);
    }
    openInventoryWindow() {
      if (!this.inventory || !this.inventory.isOpen()) {
        const invComponent = new ItemInventoryComponent(
          this,
          "content",
          5,
          6,
          stendhal.config.getBoolean("inventory.quick-pickup"),
          void 0
        );
        invComponent.setConfigId("chest");
        const dstate = stendhal.config.getWindowState("chest");
        this.inventory = new FloatingWindow(
          "Chest",
          invComponent,
          dstate.x,
          dstate.y
        );
        this.inventory.setId("chest");
      }
    }
    /**
     * Opens inventory window if player is within range.
     */
    checkOpenInventoryWindow() {
      if (this.canViewContents()) {
        this.openInventoryWindow();
      } else {
        Chat.log("client", "The chest is too far away.");
      }
    }
    closeInventoryWindow() {
      if (this.inventory && this.inventory.isOpen()) {
        this.inventory.close();
        this.inventory = void 0;
      }
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/bag.png) 1 3, auto";
    }
  };

  // entity/CorpseSlot.ts
  var import_marauroa58 = __toESM(require_marauroa());
  var CorpseSlot = class extends import_marauroa58.RPSlot {
    _name;
    _parent;
    add(object) {
      super.add(object);
      if (this._objects.length > 0) {
        this._parent?.autoOpenIfDesired();
      }
    }
    del(key) {
      super.del(key);
      if (this._objects.length == 0) {
        this._parent?.closeCorpseInventory();
      }
    }
  };

  // sprite/ActivityIndicatorSprite.ts
  var ActivityIndicatorSprite = class {
    imageRef;
    frameIdx = 0;
    lastFrameUpdate = 0;
    animate;
    constructor() {
      this.animate = stendhal.config.getBoolean("activity-indicator.animate");
      this.imageRef = images.load(Paths.sprites + "/ideas/activity.png");
    }
    /**
     * Draws an indicator.
     *
     * @param dx
     *   Pixel position where parent object is drawn on X axis.
     * @param dy
     *   Pixel position where parent object is drawn on Y axis.
     * @param width
     *   Pixel width of parent object.
     */
    draw(ctx, dx, dy, width) {
      let image = this.imageRef.image;
      if (!image) {
        return;
      }
      let dim = image.height;
      if (this.animate) {
        let cycleStart = Date.now();
        if (this.lastFrameUpdate == 0) {
          this.lastFrameUpdate = cycleStart;
        }
        if (cycleStart - this.lastFrameUpdate > 150) {
          this.frameIdx++;
          if ((this.frameIdx + 1) * dim >= image.width) {
            this.frameIdx = 0;
          }
          this.lastFrameUpdate = cycleStart;
        }
      }
      ctx.drawImage(image, dim * this.frameIdx, 0, dim, dim, dx + width - dim, dy, dim, dim);
    }
    free() {
      this.imageRef.free();
    }
  };

  // entity/Corpse.ts
  var import_marauroa59 = __toESM(require_marauroa());
  var Corpse = class extends PopupInventory {
    minimapShow = false;
    zIndex = 5500;
    autoOpenedAlready = false;
    indicator;
    constructor() {
      super();
      if (stendhal.config.getBoolean("activity-indicator")) {
        this.indicator = new ActivityIndicatorSprite();
      }
    }
    set(key, value) {
      super.set(key, value);
      let bloodEnabled = stendhal.config.getBoolean("effect.blood");
      if (bloodEnabled && key === "image") {
        this.imageSprite?.free();
        this.imageSprite = new ImageSprite(
          images.load(Paths.sprites + "/corpse/" + value + ".png")
        );
      } else if (!bloodEnabled && key === "harmless_image") {
        this.imageSprite?.free();
        this.imageSprite = new ImageSprite(
          images.load(Paths.sprites + "/corpse/" + value + ".png")
        );
      }
    }
    draw(ctx) {
      let image = this.imageSprite?.imageRef?.image;
      if (!image) {
        return;
      }
      super.draw(ctx);
      if (this.indicator && !this.isEmpty()) {
        let viewPort = ViewPort.get();
        const tileW = viewPort.targetTileWidth;
        const tileH = viewPort.targetTileHeight;
        let width = image.width < tileW ? tileW : image.width;
        let height = image.height < tileH ? tileH : image.height;
        let offsetX = Math.floor((this["width"] * tileW - width) / 2);
        let offsetY = Math.floor((this["height"] * tileH - height) / 2);
        let dx = this["x"] * tileW + offsetX;
        let dy = this["y"] * tileH + offsetY;
        this.indicator.draw(ctx, dx, dy, width);
      }
    }
    createSlot(name) {
      let slot = new CorpseSlot();
      slot._name = name;
      slot._parent = this;
      return slot;
    }
    isVisibleToAction(_filter) {
      return true;
    }
    closeCorpseInventory() {
      if (this.inventory && this.inventory.isOpen()) {
        this.inventory.close();
        this.inventory = void 0;
      }
    }
    openCorpseInventory() {
      if (!this.inventory || !this.inventory.isOpen()) {
        let content_row = 2;
        const content_col = 2;
        if (this["content"]) {
          if (this["content"]._objects.length > 4) {
            content_row = 3;
          }
        }
        const invComponent = new ItemInventoryComponent(
          this,
          "content",
          content_row,
          content_col,
          stendhal.config.getBoolean("inventory.quick-pickup"),
          void 0
        );
        invComponent.setConfigId("corpse");
        const dstate = stendhal.config.getWindowState("corpse");
        this.inventory = new FloatingWindow(
          "Corpse",
          invComponent,
          dstate.x,
          dstate.y
        );
        this.inventory.setId("corpse");
      }
    }
    /**
     * Opens inventory window if player is within range.
     */
    checkOpenCorpseInventory() {
      if (this.canViewContents()) {
        this.openCorpseInventory();
      } else {
        Chat.log("client", "The corpse is too far away.");
      }
    }
    autoOpenIfDesired() {
      if (!this.autoOpenedAlready) {
        this.autoOpenedAlready = true;
        if (import_marauroa59.marauroa.me && this["corpse_owner"] == import_marauroa59.marauroa.me["_name"]) {
          var that = this;
          window.setTimeout(function() {
            that.checkOpenCorpseInventory();
          }, 1);
        }
      }
    }
    closeInventoryWindow() {
      this.closeCorpseInventory();
    }
    onclick(_x, _y) {
      this.checkOpenCorpseInventory();
    }
    getCursor(_x, _y) {
      if (this.isEmpty()) {
        return "url(" + Paths.sprites + "/cursor/emptybag.png) 1 3, auto";
      }
      if (!this["corpse_owner"] || this["corpse_owner"] == import_marauroa59.marauroa.me["_name"]) {
        return "url(" + Paths.sprites + "/cursor/bag.png) 1 3, auto";
      }
      if (stendhal.data.group.lootmode === "shared" && stendhal.data.group.members[this["corpse_owner"]]) {
        return "url(" + Paths.sprites + "/cursor/bag.png) 1 3, auto";
      }
      return "url(" + Paths.sprites + "/cursor/lockedbag.png) 1 3, auto";
    }
    isDraggable() {
      return true;
    }
    isEmpty() {
      return !this["content"] || this["content"]._objects.length === 0;
    }
    destroy(parent) {
      this.indicator?.free();
      super.destroy(parent);
    }
  };

  // sprite/action/SkillEffect.ts
  var SkillEffect = class {
    /** Image to be drawn on canvas. */
    imageRef;
    /** Time for which each frame should be displayed. */
    delay;
    /** Timestamp at which this sprite should expire. */
    expires;
    /** Timestamp at which frame cycling begins. */
    cycleTime;
    /** Timestamp of most recent draw. */
    drawTime;
    /**
     * Creates a new skill effect sprite.
     *
     * @param {string} name
     *   Image filename (excluding .png suffix).
     * @param {number} [delay=100]
     *   Time, in milliseconds, for which each frame should be displayed. Any value less than 1
     *   results in using a default of 100ms.
     * @param {number} [duration=0]
     *   Time, in milliseconds, sprite drawing should persist. Any value less than 1 signifies that
     *   sprite should not expire.
     */
    constructor(name, delay = 100, duration = 0) {
      this.imageRef = images.load(singletons.getPaths().sprites + "/entity/overlay/" + name + ".png");
      this.cycleTime = Date.now();
      this.drawTime = 0;
      this.delay = delay > 0 ? delay : 100;
      this.expires = duration > 0 ? this.cycleTime + duration : 0;
    }
    /**
     * Draws sprite to canvas.
     *
     * @param {RenderingContext2D} ctx
     *   Canvas drawing context.
     * @param {number} colIdx
     *   Horizontal frame index.
     * @param {number} x
     *   Horizonal pixel position of where to draw on canvas.
     * @param {number} y
     *   Vertical pixel position of where to draw on canvas.
     * @param {number} drawWidth
     *   Width of each frame to draw.
     * @param {number} drawHeight
     *   Height of each frame to draw.
     */
    drawInternal(ctx, colIdx, x, y, drawWidth, drawHeight) {
      ctx.drawImage(
        this.imageRef.image,
        colIdx * drawWidth,
        0,
        drawWidth,
        drawHeight,
        x,
        y,
        drawWidth,
        drawHeight
      );
    }
    draw(ctx, x = 0, y = 0, drawWidth = 48, drawHeight = 64) {
      this.drawTime = Date.now();
      if (!this.imageRef.image) {
        return this.expired();
      }
      const timeDiff = this.drawTime - this.cycleTime;
      const fcount = Math.floor(this.imageRef.image?.width / drawWidth);
      let frame = Math.floor(timeDiff / this.delay);
      if (frame >= fcount) {
        frame = 0;
        this.cycleTime = this.drawTime;
      }
      this.drawInternal(ctx, frame, x, y, drawWidth, drawHeight);
      return this.expired();
    }
    expired() {
      return this.expires > 0 && this.drawTime > this.expires;
    }
    free() {
      this.imageRef.free();
    }
  };

  // sprite/action/DirectionalSkillEffect.ts
  var DirectionalSkillEffect = class extends SkillEffect {
    entity;
    /**
     * Creates a new skill effect sprite.
     *
     * @param {Entity} entity
     *   Entity associated with this effect.
     * @param {string} name
     *   Image filename (excluding .png suffix).
     * @param {number} [delay=100]
     *   Time, in milliseconds, for which each frame should be displayed. Any value less than 1
     *   results in using a default of 100ms.
     * @param {number} [duration=0]
     *   Time, in milliseconds, sprite drawing should persist. Any value less than 1 signifies that
     *   sprite should not expire.
     */
    constructor(entity, name, delay = 100, duration = 0) {
      super(name, delay, duration);
      this.entity = entity;
    }
    drawInternal(ctx, colIdx, x, y, drawWidth, drawHeight) {
      const dir = Direction.VALUES[parseInt(this.entity["dir"], 10)];
      let rowIdx = dir.val - 1;
      if (rowIdx < 0) {
        rowIdx = Direction.DOWN.val - 1;
      }
      ctx.drawImage(
        this.imageRef.image,
        colIdx * drawWidth,
        rowIdx * drawHeight,
        drawWidth,
        drawHeight,
        x,
        y,
        drawWidth,
        drawHeight
      );
    }
  };

  // data/EntityOverlayRegistry.ts
  var EntityOverlayRegistry;
  ((EntityOverlayRegistry2) => {
    const AnimationTable = {
      "NPC": {
        "Aenihata": ["electricity_blue", 50],
        "Tywysoga": "magic_sparkles"
      },
      "creature": {
        "albino elf magician": "beams",
        "archrat": "flame",
        "dark elf master": ["electricity_violet", 50],
        "dark elf viceroy": ["electricity_blue", 50],
        "emperor dalmung": "flame_blue",
        "ice giant": "ice_sparkles_2x",
        "ice golem": "ice_sparkles",
        "imperial demon lord": ["electricity_red", 50],
        "necrosophia": "necro_flames"
      }
    };
    const directional = [
      "necrosophia"
    ];
    function get(type, entity) {
      if (!stendhal.config.getBoolean("effect.entity-overlay")) {
        return void 0;
      }
      const group = AnimationTable[type];
      if (!group) {
        return void 0;
      }
      const name = entity["name"];
      let aniDef = group[name];
      if (!aniDef) {
        return void 0;
      }
      if (typeof aniDef === "string") {
        aniDef = [aniDef, 0];
      }
      if (directional.indexOf(name) > -1) {
        return new DirectionalSkillEffect(entity, aniDef[0], aniDef[1]);
      }
      return new SkillEffect(aniDef[0], aniDef[1]);
    }
    EntityOverlayRegistry2.get = get;
  })(EntityOverlayRegistry || (EntityOverlayRegistry = {}));

  // entity/Creature.ts
  var import_marauroa60 = __toESM(require_marauroa());
  var Creature = class extends RPEntity {
    minimapStyle = Color.CREATURE;
    spritePath = "monsters";
    titleStyle = "#ffc8c8";
    set(key, value) {
      super.set(key, value);
      if (key === "name") {
        this.overlay = EntityOverlayRegistry.get("creature", this);
      }
    }
    onclick(_x, _y) {
      var action = {
        "type": "attack",
        "target": "#" + this["id"]
      };
      import_marauroa60.marauroa.clientFramework.sendAction(action);
    }
    // Overrides the one in creature
    say(text) {
      if (stendhal.config.getBoolean("speech.creature")) {
        this.addSpeechBubble(text);
      }
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/attack.png) 1 3, auto";
    }
    /**
     * Shows a temporary animation overlay for certain entities.
     *
     * FIXME: does not restore previous overlay
     */
    onTransformed() {
      if (!this["name"].startsWith("vampire")) {
        return;
      }
      const delay = 100;
      const frames = 5;
      this.overlay = new SkillEffect("transform", delay, delay * frames);
    }
  };

  // entity/DomesticAnimal.ts
  var import_marauroa61 = __toESM(require_marauroa());
  var DomesticAnimal = class extends RPEntity {
    minimapStyle = Color.DOMESTICANIMAL;
    init() {
      this.imageSprite = new ImageSprite(
        images.load(Paths.sprites + "/" + this["_rpclass"] + ".png")
      );
      this["largeWeight"] = this["largeWeight"] || 20;
      if (this["_rpclass"] == "sheep") {
        this["largeWeight"] = 60;
      }
    }
    drawMain(ctx) {
      let image = this.imageSprite?.imageRef?.image;
      if (!image) {
        return;
      }
      let localX = this["_x"] * 32;
      let localY = this["_y"] * 32;
      let nFrames = 3;
      let nDirections = 4;
      let yRow = this["dir"] - 1;
      if (this["weight"] >= this["largeWeight"]) {
        yRow += 4;
      }
      this["drawHeight"] = image.height / nDirections / 2;
      this["drawWidth"] = image.width / nFrames;
      let drawX = (this["width"] * 32 - this["drawWidth"]) / 2;
      let frame = 0;
      if (this["speed"] > 0) {
        frame = Math.floor(Date.now() / 100) % nFrames;
      }
      let drawY = this["height"] * 32 - this["drawHeight"];
      ctx.drawImage(image, frame * this["drawWidth"], yRow * this["drawHeight"], this["drawWidth"], this["drawHeight"], localX + drawX, localY + drawY, this["drawWidth"], this["drawHeight"]);
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/look.png) 1 3, auto";
    }
    buildActions(list) {
      let species = "pet";
      if (this["_rpclass"] === "sheep") {
        species = "sheep";
      }
      let playerOwned = import_marauroa61.marauroa.me[species];
      if (!playerOwned) {
        list.push({
          title: "Own",
          action: function(_entity) {
            let action = {
              "type": "own",
              "zone": import_marauroa61.marauroa.currentZoneName,
              "target": "#" + _entity["id"]
            };
            import_marauroa61.marauroa.clientFramework.sendAction(action);
          }
        });
      }
      if (playerOwned === this["id"]) {
        list.push({
          title: "Leave",
          action: function(_entity) {
            let action = {
              "type": "forsake",
              "zone": import_marauroa61.marauroa.currentZoneName,
              "species": species,
              "target": "#" + _entity["id"]
            };
            import_marauroa61.marauroa.clientFramework.sendAction(action);
          }
        });
      }
      super.buildActions(list);
    }
  };

  // entity/Portal.ts
  var import_marauroa62 = __toESM(require_marauroa());
  var Portal = class extends Entity {
    minimapShow = true;
    minimapStyle = Color.BLACK;
    zIndex = 5e3;
    buildActions(list) {
      super.buildActions(list);
      if (this["_rpclass"] == "house_portal") {
        list.push({
          title: "Use",
          type: "use"
        });
        list.push({
          title: "Knock",
          type: "knock"
        });
      } else {
        list.splice(list.indexOf({ title: "Look", type: "look" }), 1);
        list.push({
          title: "Use",
          type: "use"
        });
      }
    }
    isVisibleToAction(_filter) {
      return true;
    }
    /**
     * Create the default action for this entity. If the entity specifies a
     * default action description, interpret it as an action command.
     */
    getDefaultAction() {
      let map = TileMap.get();
      if (map.collision(this["x"], this["y"])) {
        return {
          "type": "use",
          "target": "#" + this["id"],
          "zone": import_marauroa62.marauroa.currentZoneName
        };
      }
      return {
        "type": "moveto",
        "x": "" + this["x"],
        "y": "" + this["y"],
        "zone": import_marauroa62.marauroa.currentZoneName
      };
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/portal.png) 1 3, auto";
    }
  };

  // entity/Door.ts
  var Door = class extends Portal {
    zIndex = 5e3;
    set(key, value) {
      super.set(key, value);
      if (key === "class") {
        if (this.imageSprite) {
          this.imageSprite?.free();
          this.imageSprite = new ImageSprite(
            images.load(Paths.sprites + "/doors/" + value + ".png"),
            0,
            0,
            0,
            0
          );
        }
      }
    }
    draw(ctx) {
      let image = this.imageSprite?.imageRef?.image;
      if (!image) {
        return;
      }
      let height = image.height / 2;
      let x = this["x"] * 32 - (image.width - 32) / 2;
      let y = this["y"] * 32 - (height - 32) / 2;
      let offsetY = height;
      if (this["open"] === "") {
        offsetY = 0;
      }
      ctx.drawImage(image, 0, offsetY, image.width, height, x, y, image.width, height);
    }
    buildActions(list) {
      list.push({
        title: "Look",
        type: "look"
      });
      list.push({
        title: "Use",
        type: "use"
      });
    }
    isVisibleToAction(_filter) {
      return true;
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/portal.png) 1 3, auto";
    }
  };

  // entity/Food.ts
  var import_marauroa63 = __toESM(require_marauroa());
  var Food = class extends Entity {
    zIndex = 5e3;
    init() {
      this.imageSprite = new ImageSprite(
        images.load(Paths.sprites + "/food.png"),
        0,
        this["amount"] * 32,
        32,
        32
      );
    }
    set(key, value) {
      super.set(key, value);
      if (key === "amount") {
        if (this.imageSprite) {
          this.imageSprite.offsetY = this["amount"] * 32;
        }
      }
    }
    onclick(_x, _y) {
      var action = {
        "type": "look",
        "target": "#" + this["id"]
      };
      import_marauroa63.marauroa.clientFramework.sendAction(action);
    }
    isVisibleToAction(_filter) {
      return true;
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/look.png) 1 3, auto";
    }
  };

  // entity/GameBoard.ts
  var GameBoard = class extends Entity {
    minimapShow = false;
    zIndex = 100;
    set(key, value) {
      super.set(key, value);
      if (key === "class") {
        let filename = Paths.sprites + "/gameboard/" + this["class"] + ".png";
        this.imageSprite?.free();
        this.imageSprite = new ImageSprite(images.load(filename), 0, 0, 3 * 32, 3 * 32);
      }
    }
    isVisibleToAction(_filter) {
      return false;
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/walk.png) 1 3, auto";
    }
  };

  // entity/Gate.ts
  var import_marauroa64 = __toESM(require_marauroa());
  var Gate = class extends Entity {
    zIndex = 5e3;
    locked = false;
    init() {
      this.imageSprite?.free();
      this.imageSprite = new ImageSprite(
        images.load(Paths.sprites + "/doors/" + this["image"] + "_" + this["orientation"] + ".png"),
        0,
        0,
        0,
        0
      );
    }
    set(key, value) {
      super.set(key, value);
      if (key === "resistance") {
        this.locked = parseInt(value, 10) !== 0;
      } else if (key === "image" || key === "orientation") {
        if (this.imageSprite) {
          this.imageSprite?.free();
          this.imageSprite = new ImageSprite(
            images.load(Paths.sprites + "/doors/" + this["image"] + "_" + this["orientation"] + ".png"),
            0,
            0,
            0,
            0
          );
        }
      }
    }
    buildActions(list) {
      var id = this["id"];
      list.push({
        title: this.locked ? "Open" : "Close",
        action: function(_entity) {
          var action = {
            "type": "use",
            "target": "#" + id,
            "zone": import_marauroa64.marauroa.currentZoneName
          };
          import_marauroa64.marauroa.clientFramework.sendAction(action);
        }
      });
    }
    draw(ctx) {
      let image = this.imageSprite?.imageRef?.image;
      if (!image) {
        return;
      }
      let xOffset = -32 * Math.floor(image.width / 32 / 2);
      let height = image.height / 2;
      let yOffset = -32 * Math.floor(height / 32 / 2);
      let localX = this["_x"] * 32 + xOffset;
      let localY = this["_y"] * 32 + yOffset;
      let yStart = this.locked ? height : 0;
      ctx.drawImage(image, 0, yStart, image.width, height, localX, localY, image.width, height);
    }
    isVisibleToAction(_filter) {
      return true;
    }
    onclick(_x, _y) {
      var action = {
        "type": "use",
        "target": "#" + this["id"],
        "zone": import_marauroa64.marauroa.currentZoneName
      };
      import_marauroa64.marauroa.clientFramework.sendAction(action);
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/activity.png) 1 3, auto";
    }
  };

  // entity/GrowingEntitySpawner.ts
  var import_marauroa65 = __toESM(require_marauroa());
  var GrowingEntitySpawner = class extends Entity {
    zIndex = 3e3;
    set(key, value) {
      super.set(key, value);
      if (key === "class") {
        this.imageSprite?.free();
        let className = value.replace(" ", "_");
        this.imageSprite = new ImageSprite(
          images.load(Paths.sprites + "/" + className + ".png"),
          0,
          0,
          0,
          0
        );
      }
    }
    /**
     * is this entity visible to a specific action
     *
     * @param filter 0: short left click
     * @return true, if the entity is visible, false otherwise
     */
    isVisibleToAction(_filter) {
      return true;
    }
    buildActions(list) {
      if (!this["menu"]) {
        list.push({
          title: "Harvest",
          type: "use"
        });
      }
      super.buildActions(list);
    }
    onclick(_x, _y) {
      var action = {
        "type": "use",
        "target": "#" + this["id"],
        "zone": import_marauroa65.marauroa.currentZoneName
      };
      import_marauroa65.marauroa.clientFramework.sendAction(action);
    }
    draw(ctx) {
      let localX = this["x"] * 32;
      let localY = this["y"] * 32;
      let image = this.imageSprite?.imageRef?.image;
      if (!image) {
        return;
      }
      let count = parseInt(this["max_ripeness"], 10) + 1;
      let drawHeight = image.height / count;
      let yRow = this["ripeness"];
      ctx.drawImage(
        image,
        0,
        yRow * drawHeight,
        image.width,
        drawHeight,
        localX,
        localY - drawHeight + 32,
        image.width,
        drawHeight
      );
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/harvest.png) 1 3, auto";
    }
  };

  // entity/ItemMap.ts
  var import_marauroa66 = __toESM(require_marauroa());
  var config = singletons.getConfigManager();
  var defaultUse = {
    title: "Use",
    type: "use",
    index: 0
  };
  var ItemMap = {
    ["class"]: {
      ["box"]: {
        cursor: "bag",
        actions: [
          {
            title: "Open",
            type: "use",
            index: 0
          }
        ]
      },
      ["drink"]: {
        cursor: "itemuse"
      },
      ["food"]: {
        cursor: "itemuse"
      },
      ["scroll"]: {
        cursor: "itemuse"
      }
    },
    ["name"]: {
      ["ashen holy water"]: {
        actions: [defaultUse]
      },
      ["bestiary"]: {
        cursor: "itemuse"
      },
      ["bulb"]: {
        cursor: "itemuse"
      },
      ["empty scroll"]: {
        actions: function(e) {
          const count = parseInt(e["quantity"], 10);
          if (count > 1 && e._parent) {
            return [{
              title: "Mark all",
              index: 1,
              action: function(entity) {
                import_marauroa66.marauroa.clientFramework.sendAction(
                  {
                    "type": "markscroll",
                    "quantity": "" + count
                  }
                );
              }
            }];
          }
        }
      },
      ["food mill"]: {
        cursor: "itemuse"
      },
      ["metal detector"]: {
        cursor: "itemuse"
      },
      ["picture in wooden frame"]: {
        cursor: "itemuse"
      },
      ["rotary cutter"]: {
        cursor: "itemuse"
      },
      ["scroll eraser"]: {
        cursor: "itemuse"
      },
      ["seed"]: {
        cursor: "itemuse"
      },
      ["snowglobe"]: {
        cursor: "itemuse"
      },
      ["sugar mill"]: {
        cursor: "itemuse"
      },
      ["teddy"]: {
        cursor: "itemuse"
      },
      ["wedding ring"]: {
        cursor: "itemuse",
        actions: [defaultUse]
      }
    },
    /**
     * Retrieves cursor registered for item.
     *
     * @param clazz
     *     Item class.
     * @param name
     *     Item name.
     * @return
     *     Cursor name.
     */
    getCursor: function(clazz, name) {
      let cursor = "normal";
      for (const imap of [ItemMap["class"][clazz], ItemMap["name"][name]]) {
        if (imap && imap.cursor) {
          cursor = imap.cursor;
        }
      }
      if (cursor === "itemuse" && !config.getBoolean("inventory.double-click")) {
        cursor = "activity";
      }
      return cursor;
    },
    /**
     * Retrieves list of menu actions defined for item.
     *
     * @param item
     *     Object containing item defintion.
     * @return
     *     Actions.
     */
    getActions: function(item) {
      let actions = [];
      if (!item) {
        return actions;
      }
      for (const imap of [ItemMap["class"][item["class"]], ItemMap["name"][item["name"]]]) {
        if (imap && imap.actions) {
          let a;
          if (typeof imap.actions === "function") {
            a = imap.actions(item) || [];
          } else {
            a = imap.actions;
          }
          actions = actions.concat(a);
        }
      }
      return actions;
    }
  };

  // entity/Item.ts
  var import_marauroa67 = __toESM(require_marauroa());
  var Item = class extends Entity {
    minimapShow = false;
    minimapStyle = "rgb(0,255,0)";
    zIndex = 7e3;
    quantityTextSprite;
    // animation
    frameTimeStamp = 0;
    animated = null;
    constructor() {
      super();
      this.quantityTextSprite = new TextSprite("", "white", "10px sans-serif");
    }
    isVisibleToAction(_filter) {
      return true;
    }
    buildActions(list) {
      super.buildActions(list);
      for (const mi of ItemMap.getActions(this)) {
        if (typeof mi.index === "number") {
          list.splice(mi.index, 0, mi);
        } else {
          list.push(mi);
        }
      }
    }
    // default action for items on the ground is to pick them up
    // do not require players to use drag & drop (as the classic client did)
    // because drag & drop is difficult on mobile (especially if the page is zoomed)
    getDefaultAction() {
      return {
        type: "equip",
        "source_path": this.getIdPath(),
        "target_path": "[" + import_marauroa67.marauroa.me["id"] + "	bag]",
        "clicked": "",
        // useful for changing default target in equip action
        "zone": import_marauroa67.marauroa.currentZoneName
      };
    }
    set(key, value) {
      super.set(key, value);
      if (key === "class" || key === "subclass") {
        if (this["class"] && this["subclass"]) {
          this.imageSprite?.free();
          this.imageSprite = new ImageSprite(
            images.load(Paths.sprites + "/items/" + this["class"] + "/" + this["subclass"] + ".png"),
            0,
            (this["state"] || 0) * 32,
            32,
            32
          );
        }
      } else if (key === "state") {
        if (this.imageSprite) {
          this.imageSprite.offsetY = (this["state"] || 0) * 32;
        }
      } else if (key === "quantity") {
        this.quantityTextSprite = new TextSprite(this.formatQuantity(), "white", "10px sans-serif");
      }
    }
    draw(ctx) {
      this.stepAnimation();
      this.drawAt(ctx, this["x"] * 32, this["y"] * 32);
    }
    drawAt(ctx, x, y) {
      let image = this.imageSprite?.imageRef?.image;
      if (!image) {
        return;
      }
      this.drawSpriteAt(ctx, x, y);
      let textMetrics = this.quantityTextSprite.getTextMetrics(ctx);
      if (!textMetrics) {
        throw new Error("textMetrics is undefined");
      }
      this.quantityTextSprite.draw(ctx, x + (32 - textMetrics.width), y + 6);
    }
    stepAnimation() {
      let image = this.imageSprite?.imageRef?.image;
      if (!image) {
        return;
      }
      const currentTimeStamp = +/* @__PURE__ */ new Date();
      if (currentTimeStamp - this.frameTimeStamp >= 100) {
        this.setXFrameIndex(this.getXFrameIndex() + 1);
        this.frameTimeStamp = currentTimeStamp;
      }
    }
    formatQuantity() {
      if (!this["quantity"] || this["quantity"] === "1") {
        return "";
      }
      if (this["quantity"] > 1e7) {
        return Math.floor(this["quantity"] / 1e6) + "m";
      }
      if (this["quantity"] > 1e4) {
        return Math.floor(this["quantity"] / 1e3) + "k";
      }
      return this["quantity"];
    }
    getCursor(_x, _y) {
      let cursor;
      if (!this._parent) {
        cursor = "itempickupfromslot";
      } else {
        cursor = ItemMap.getCursor(this["class"], this["name"]);
      }
      return "url(" + Paths.sprites + "/cursor/" + cursor + ".png) 1 3, auto";
    }
    getToolTip() {
      if (this["class"] === "scroll" && this["dest"]) {
        const dest = this["dest"].split(",");
        if (dest.length > 2) {
          return dest[0] + " " + dest[1] + "," + dest[2];
        }
      }
      return "";
    }
    isAnimated() {
      if (this.animated === null) {
        let image = this.imageSprite?.imageRef?.image;
        if (image) {
          this.animated = image.width / 32 > 1;
        }
      }
      return this.animated || false;
    }
    setXFrameIndex(idx) {
      let image = this.imageSprite?.imageRef.image;
      if (!image) {
        return;
      }
      let frames = image.width / 32;
      if (idx >= frames) {
        idx = 0;
      }
      this.imageSprite.offsetX = idx * 32;
    }
    getXFrameIndex() {
      return (this.imageSprite?.offsetX || 0) / 32;
    }
    isDraggable() {
      return true;
    }
  };

  // entity/NPC.ts
  var NPC = class extends RPEntity {
    minimapStyle = Color.NPC;
    spritePath = "npc";
    titleStyle = "#c8c8ff";
    constructor() {
      super();
      this["hp"] = 100;
      this["base_hp"] = 100;
    }
    set(key, value) {
      super.set(key, value);
      if (key === "name") {
        this.overlay = EntityOverlayRegistry.get("NPC", this);
        if (value.startsWith("Zekiel")) {
          this.titleDrawYOffset = -32;
        }
      }
    }
    drawTop(ctx) {
      var localX = this["_x"] * 32;
      var localY = this["_y"] * 32;
      if (typeof this["no_hpbar"] == "undefined") {
        this.drawHealthBar(ctx, localX, localY + this.statusBarYOffset);
      }
      if (typeof this["unnamed"] == "undefined") {
        this.drawTitle(ctx, localX, localY + this.statusBarYOffset);
      }
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/look.png) 1 3, auto";
    }
  };

  // entity/Sign.ts
  var Sign = class extends Entity {
    zIndex = 5e3;
    indicator;
    init() {
      super.init();
      if (!this.imageSprite) {
        this.imageSprite = new ImageSprite(
          images.load(Paths.sprites + "/signs/default.png"),
          0,
          0,
          32,
          32
        );
      }
    }
    set(key, value) {
      super.set(key, value);
      if (key === "activity-indicator" && stendhal.config.getBoolean("activity-indicator")) {
        this.indicator = new ActivityIndicatorSprite();
      } else if (key === "class") {
        this.imageSprite = new ImageSprite(
          images.load(Paths.sprites + "/signs/" + this["class"] + ".png"),
          0,
          0,
          32,
          32
        );
      }
    }
    draw(ctx) {
      let image = this.imageSprite?.imageRef?.image;
      let localX = this["x"] * 32;
      let localY = this["y"] * 32;
      if (image) {
        ctx.drawImage(image, localX, localY);
        if (this.indicator) {
          this.indicator.draw(ctx, localX, localY, image.width);
        }
      }
    }
    isVisibleToAction(_filter) {
      return true;
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/look.png) 1 3, auto";
    }
    destroy(parent) {
      this.indicator?.free();
      super.destroy(parent);
    }
  };

  // entity/TrainingDummy.ts
  var import_marauroa68 = __toESM(require_marauroa());
  var TrainingDummy = class extends NPC {
    /**
     * Default action when clicked.
     */
    onclick(_x, _y) {
      var action = {
        "type": "attack",
        "target": "#" + this["id"]
      };
      import_marauroa68.marauroa.clientFramework.sendAction(action);
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/attack.png) 1 3, auto";
    }
  };

  // entity/UnknownEntity.ts
  var import_marauroa69 = __toESM(require_marauroa());
  var UnknownEntity = class extends Entity {
    zIndex = 1;
    constructor() {
      super();
      window.setTimeout(() => {
        if (this["_rpclass"]) {
          console.log("Unknown entity", this["_rpclass"], "at", import_marauroa69.marauroa.currentZoneName, this["x"], this["y"], "is", this);
        }
      }, 1);
    }
    isVisibleToAction(_filter) {
      return import_marauroa69.marauroa.me["adminlevel"] && import_marauroa69.marauroa.me["adminlevel"] >= 600;
    }
  };

  // entity/UseableEntity.ts
  var UseableEntity = class extends Entity {
    zIndex = 3e3;
    action = "use";
    set(key, value) {
      super.set(key, value);
      if (key === "class" || key === "name") {
        let filename = Paths.sprites + "/" + this["class"] + "/" + this["_name"] + ".png";
        let state = this["state"] * 32 || 0;
        this.imageSprite?.free();
        this.imageSprite = new ImageSprite(images.load(filename), 0, state, 32, 32);
      } else if (key === "state" && this.imageSprite) {
        this.imageSprite.offsetY = this["state"] * 32;
      }
    }
    isVisibleToAction(_filter) {
      return true;
    }
    buildActions(list) {
      super.buildActions(list);
      const lookItem = { title: "Look", type: "look" };
      if (list.indexOf(lookItem) < 0) {
        list.push(lookItem);
      }
    }
  };

  // entity/User.ts
  var import_marauroa71 = __toESM(require_marauroa());

  // ui/dialog/outfit/OutfitPartSelector.ts
  var OutfitPartSelector = class {
    part;
    onPartChanged;
    _index;
    minIndex;
    maxIndex;
    ctx;
    width;
    height;
    _color;
    imageRef;
    constructor(part, initialIndex, minIndex, maxIndex, onPartChanged) {
      this.part = part;
      this.onPartChanged = onPartChanged;
      this._index = parseInt(initialIndex, 10);
      this.minIndex = minIndex;
      this.maxIndex = maxIndex;
      const canvas = document.getElementById("setoutfit" + part + "canvas");
      canvas.style.margin = "5px";
      this.ctx = canvas.getContext("2d");
      this.width = canvas.width;
      this.height = canvas.height;
      this._color = void 0;
      this.loadPartSprite();
    }
    indexString(index) {
      if (index > -1 && index < 100) {
        if (index < 10) {
          return "00" + index;
        } else {
          return "0" + index;
        }
      }
      return "" + index;
    }
    async draw() {
      this.ctx.fillStyle = "white";
      this.ctx.fillRect(0, 0, this.width, this.height);
      let currentImage = this.imageRef;
      await currentImage.waitFor();
      if (currentImage === this.imageRef && currentImage.image) {
        this.ctx.drawImage(currentImage.image, -48, -128);
      }
    }
    get index() {
      return this.indexString(this._index);
    }
    set index(newIndex) {
      this._index = parseInt(newIndex, 10);
      this.loadPartSprite();
      this.draw();
    }
    set color(newColor) {
      this._color = newColor;
      this.loadPartSprite();
      this.onPartChanged();
      this.draw();
    }
    previous() {
      this._index--;
      if (this._index < this.minIndex) {
        this._index = this.maxIndex;
      }
      this.loadPartSprite();
      this.onPartChanged();
      this.draw();
    }
    next() {
      this._index++;
      if (this._index > this.maxIndex) {
        this._index = this.minIndex;
      }
      this.loadPartSprite();
      this.onPartChanged();
      this.draw();
    }
    loadPartSprite() {
      this.imageRef?.free();
      let suffix = ".png";
      if (this.part === "body" && stendhal.config.getBoolean("effect.no-nude")) {
        suffix = "-nonude.png";
      }
      let fname = Paths.sprites + "/outfit/" + this.part + "/" + this.indexString(this._index) + suffix;
      if (this._color != null) {
        this.imageRef = images.load(fname, "trueColor", this._color);
      } else {
        this.imageRef = images.load(fname);
      }
    }
    close() {
      this.imageRef?.free();
    }
  };

  // ui/dialog/outfit/OutfitColorSelector.ts
  var OutfitColorSelector = class {
    ctx;
    gradCtx;
    baseImage;
    onColorChanged;
    _enabled;
    _x;
    _y;
    hX;
    constructor(canvas, gradientCanvas, onColorChanged) {
      this.ctx = canvas.getContext("2d");
      this.gradCtx = gradientCanvas.getContext("2d");
      this.baseImage = this._createBaseImage(canvas.width, canvas.height);
      this.onColorChanged = onColorChanged;
      this._enabled = false;
      this._x = this.baseImage.width / 2;
      this._y = this.baseImage.height / 2;
      this.hX = this._x;
      canvas.addEventListener("mousedown", (e) => this._onMouseDown(e));
      canvas.addEventListener("mousemove", (e) => this._onMouseMove(e));
      gradientCanvas.addEventListener("mousedown", (e) => this._onMouseDownGrad(e));
      gradientCanvas.addEventListener("mousemove", (e) => this._onMouseMoveGrad(e));
      gradientCanvas.style.margin = "5px 0px 0px 0px";
    }
    set enabled(value) {
      this._enabled = value ? true : false;
      this.draw();
      this.onColorChanged(this.color);
    }
    get enabled() {
      return this._enabled;
    }
    get color() {
      if (this.enabled) {
        let filter = new ImageFilter();
        const hsl = [
          this._x / this.baseImage.width,
          1 - this._y / this.baseImage.height,
          this.hX / this.baseImage.width
        ];
        const rgbArray = filter.hsl2rgb(hsl);
        return filter.mergergb(rgbArray);
      }
      return null;
    }
    set color(rgb) {
      if (rgb != null) {
        let filter = new ImageFilter();
        const hsl = filter.rgb2hsl(filter.splitrgb(rgb));
        this._x = hsl[0] * this.baseImage.width;
        this._y = (1 - hsl[1]) * this.baseImage.height;
        this.hX = hsl[2] * this.baseImage.width;
        this.enabled = true;
      } else {
        this.enabled = false;
      }
    }
    _createBaseImage(width, height) {
      const img = document.createElement("canvas");
      img.width = width;
      img.height = height;
      const ctx = img.getContext("2d");
      let filter = new ImageFilter();
      for (let x = 0; x < width; x++) {
        for (let y = 0; y < height; y++) {
          const hsl = [x / width, 1 - y / height, 0.5];
          const rgb = filter.hsl2rgb(hsl);
          ctx.fillStyle = this._rgbToCssString(rgb);
          ctx.fillRect(x, y, 1, 1);
        }
      }
      return img;
    }
    _rgbToCssString(rgb) {
      return "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
    }
    draw() {
      if (this.enabled) {
        this.ctx.drawImage(this.baseImage, 0, 0);
        this._drawSelection();
      } else {
        this.ctx.fillStyle = "gray";
        this.ctx.fillRect(0, 0, this.baseImage.width, this.baseImage.height);
      }
      this._drawGradientPart();
    }
    _drawSelection() {
      this.ctx.strokeStyle = "black";
      this.ctx.beginPath();
      this.ctx.moveTo(this._x, 0);
      this.ctx.lineTo(this._x, this.baseImage.height);
      this.ctx.moveTo(0, this._y);
      this.ctx.lineTo(this.baseImage.width, this._y);
      this.ctx.stroke();
    }
    _drawGradientPart() {
      if (this.enabled) {
        const gradient = this.gradCtx.createLinearGradient(0, 0, this.baseImage.width, 0);
        const stops = this._calculateGradientStops();
        gradient.addColorStop(0, this._rgbToCssString(stops[0]));
        gradient.addColorStop(0.5, this._rgbToCssString(stops[1]));
        gradient.addColorStop(1, this._rgbToCssString(stops[2]));
        const ctx = this.gradCtx;
        ctx.fillStyle = gradient;
        ctx.fillRect(0, 0, this.baseImage.width, 10);
        ctx.fillStyle = "black";
        ctx.beginPath();
        ctx.moveTo(this.hX, 0);
        ctx.lineTo(this.hX, 10);
        ctx.stroke();
      } else {
        this.gradCtx.fillStyle = "gray";
        this.gradCtx.fillRect(0, 0, this.baseImage.width, 10);
      }
    }
    _calculateGradientStops() {
      let filter = new ImageFilter();
      const width = this.baseImage.width;
      const height = this.baseImage.height;
      const hslLeft = [this._x / width, 1 - this._y / height, 0.08];
      const hslMiddle = [this._x / width, 1 - this._y / height, 0.5];
      const hslRight = [this._x / width, 1 - this._y / height, 0.92];
      const rgbLeft = filter.hsl2rgb(hslLeft);
      const rgbMiddle = filter.hsl2rgb(hslMiddle);
      const rgbRight = filter.hsl2rgb(hslRight);
      return [rgbLeft, rgbMiddle, rgbRight];
    }
    _onMouseDown(event2) {
      if (!this.enabled) {
        return;
      }
      this._x = event2.offsetX;
      this._y = event2.offsetY;
      this.draw();
      this.onColorChanged(this.color);
    }
    _onMouseMove(event2) {
      if (event2.buttons) {
        this._onMouseDown(event2);
      }
    }
    _onMouseDownGrad(event2) {
      if (!this.enabled) {
        return;
      }
      this.hX = event2.offsetX;
      this.draw();
      this.onColorChanged(this.color);
    }
    _onMouseMoveGrad(event2) {
      if (event2.buttons) {
        this._onMouseDownGrad(event2);
      }
    }
  };

  // ui/dialog/outfit/OutfitPaletteColorSelector.ts
  var OutfitPaletteColorSelector = class extends OutfitColorSelector {
    _blockWidth;
    _blockHeight;
    _colorMap;
    _hsMap;
    constructor(canvas, gradientCanvas, onColorChanged) {
      super(canvas, gradientCanvas, onColorChanged);
      this._blockWidth = this.baseImage.width / 4;
      this._blockHeight = this.baseImage.height / 4;
      this._x = 0;
      this._y = 0;
      this.hX = this.baseImage.width / 2;
      this._createColorMap();
    }
    get color() {
      if (this.enabled) {
        let filter = new ImageFilter();
        const hs = this._hsMap[Math.floor(this._x / this._blockWidth)][Math.floor(this._y / this._blockHeight)];
        const hsl = [hs[0], hs[1], this.hX / this.baseImage.width];
        const rgbArray = filter.hsl2rgb(hsl);
        return filter.mergergb(rgbArray);
      }
      return null;
    }
    set color(rgb) {
      if (rgb != null) {
        this.enabled = true;
        let filter = new ImageFilter();
        const hsl = filter.rgb2hsl(filter.splitrgb(rgb));
        this.hX = hsl[2] * this.baseImage.width;
        let bestDelta = Number.MAX_VALUE;
        for (let i = 0; i < 4; i++) {
          for (let j = 0; j < 4; j++) {
            const hs = this._hsMap[i][j];
            const hueDelta = hs[0] - hsl[0];
            const satDelta = hs[1] - hsl[1];
            const delta = hueDelta * hueDelta + satDelta * satDelta;
            if (delta < bestDelta) {
              bestDelta = delta;
              this._x = i * this._blockWidth;
              this._y = j * this._blockHeight;
            }
          }
        }
      } else {
        this.enabled = false;
      }
    }
    _createColorMap() {
      const hues = [0.05, 0.07, 0.09, 0.11];
      const saturations = [0.7, 0.55, 0.4, 0.25];
      const hsMap = [[], [], [], []];
      const colors = [[], [], [], []];
      let filter = new ImageFilter();
      for (let i = 0; i < 4; i++) {
        for (let j = 0; j < 4; j++) {
          const hue = hues[j];
          const sat = saturations[i];
          hsMap[i].push([hue, sat]);
          const color = filter.hsl2rgb([hue, sat, 0.5]);
          colors[i].push(color);
        }
      }
      this._hsMap = hsMap;
      this._colorMap = colors;
    }
    _createBaseImage(width, height) {
      this._createColorMap();
      const img = document.createElement("canvas");
      img.width = width;
      img.height = height;
      const ctx = img.getContext("2d");
      const blockWidth = width / 4;
      const blockHeight = height / 4;
      for (let x = 0; x < 4; x++) {
        for (let y = 0; y < 4; y++) {
          const rgb = this._colorMap[x][y];
          ctx.fillStyle = this._rgbToCssString(rgb);
          ctx.fillRect(x * blockWidth, y * blockHeight, blockWidth, blockHeight);
        }
      }
      return img;
    }
    _calculateGradientStops() {
      let filter = new ImageFilter();
      let hs = this._hsMap[Math.floor(this._x / this._blockWidth)][Math.floor(this._y / this._blockHeight)];
      const hslLeft = [hs[0], hs[1], 0.08];
      const hslMiddle = [hs[0], hs[1], 0.5];
      const hslRight = [hs[0], hs[1], 0.92];
      const rgbLeft = filter.hsl2rgb(hslLeft);
      const rgbMiddle = filter.hsl2rgb(hslMiddle);
      const rgbRight = filter.hsl2rgb(hslRight);
      return [rgbLeft, rgbMiddle, rgbRight];
    }
    _drawSelection() {
      let x = Math.floor(this._x / this._blockWidth);
      let y = Math.floor(this._y / this._blockHeight);
      this.ctx.strokeStyle = "white";
      this.ctx.strokeRect(x * this._blockWidth, y * this._blockHeight, this._blockWidth, this._blockHeight);
    }
  };

  // sprite/image/ImageMetadata.ts
  var ImageMetadata = class {
    constructor(filename, filter, color) {
      this.filename = filename;
      this.filter = filter;
      this.color = color;
    }
    filename;
    filter;
    color;
  };

  // sprite/image/MultilayerImageLoader.ts
  var MultilayerImageLoader = class {
    constructor(imageMetadatas) {
      this.imageMetadatas = imageMetadatas;
    }
    imageMetadatas;
    imageRefs = [];
    async load() {
      for (let imageMetadata of this.imageMetadatas) {
        this.imageRefs.push(images.load(imageMetadata.filename, imageMetadata.filter, imageMetadata.color));
      }
      for (let imageRef of this.imageRefs) {
        await imageRef.waitFor();
      }
      let canvas = void 0;
      let ctx = void 0;
      for (let imageRef of this.imageRefs) {
        if (imageRef.image) {
          if (!canvas) {
            canvas = new OffscreenCanvas(imageRef.image.width, imageRef.image.height);
            ctx = canvas.getContext("2d");
          }
          ctx?.drawImage(imageRef.image, 0, 0);
          imageRef.free();
        }
      }
      if (!canvas) {
        canvas = new OffscreenCanvas(0, 0);
      }
      return canvas.transferToImageBitmap();
    }
  };

  // data/Outfit.ts
  var Outfit = class _Outfit {
    /** Sprite layers. */
    layers;
    /** Layer coloring values. */
    coloring;
    /**
     * Creates a new outfit.
     *
     * @param layers
     *   Sprite layers.
     */
    constructor(layers = {}) {
      this.layers = layers;
    }
    /**
     * Builds an outfit from string values.
     *
     * @param {string} layers
     *   Outfit layers definition in "layer=index,..." format.
     * @param {string=} coloring
     *   Outfit colors definition in "layer=color,..." format.
     * @returns {Outfit}
     *   New outfit.
     */
    static build(layers, coloring) {
      const olayers = {};
      for (const l of layers.split(",")) {
        if (l.includes("=")) {
          const temp = l.split("=");
          olayers[temp[0]] = Number.parseInt(temp[1], 10);
        }
      }
      const outfit = new _Outfit(olayers);
      if (coloring) {
        const ocoloring = {};
        for (const c of coloring.split(",")) {
          if (c.includes("=")) {
            const temp = c.split("=");
            ocoloring[temp[0]] = Number.parseInt(temp[1], 10);
          }
        }
        outfit.setColoring(ocoloring);
      }
      return outfit;
    }
    /**
     * Sets a layer's sprite index.
     *
     * @param name
     *   Layer name.
     * @param index
     *   Sprite image index.
     */
    setLayer(name, index) {
      this.layers[name] = index;
    }
    /**
     * Unsets a layer's sprite index.
     *
     * @param name
     *   Layer name.
     */
    unsetLayer(name) {
      delete this.layers[name];
    }
    /**
     * Retrieves the index value of a specified layer.
     *
     * @param name
     *   Layer name.
     * @return
     *   Sprite image index.
     */
    getLayerIndex(name) {
      return this.layers[name];
    }
    /**
     * Retrieves sorted layer info.
     *
     * @return {util.Pair.Pair<string, number>[]}
     *   Layers info.
     */
    getLayers() {
      const layers = [];
      for (const name of OutfitStore.get().getLayerNames()) {
        let index = this.layers[name];
        if (index != void 0) {
          layers.push(new Pair(name, index));
        }
      }
      return layers;
    }
    /**
     * Sets coloring info for this outfit.
     *
     * @param coloring {object}
     *   Color values indexed by layer name.
     */
    setColoring(coloring) {
      this.coloring = coloring;
    }
    /**
     * Retrieves coloring info for this outfit.
     *
     * @return {object}
     *   Color values indexed by layer name or `undefined`.
     */
    getColoring() {
      return this.coloring;
    }
    /**
     * Retrieves coloring for a single layer.
     *
     * @param name {string}
     *   Layer name.
     * @return {number}
     *   Color value or `undefined`.
     */
    getLayerColor(name) {
      if (OutfitStore.get().isSkinLayer(name)) {
        name = "skin";
      }
      return this.coloring && name in this.coloring ? this.coloring[name] : void 0;
    }
    /**
     * Retrieves signature identifying this outfit.
     */
    getSignature() {
      const lsig = [];
      const csig = [];
      for (const layer of this.getLayers()) {
        lsig.push(layer.first + "=" + layer.second);
      }
      if (this.coloring) {
        for (const name of Object.keys(this.coloring)) {
          csig.push(name + "=" + this.coloring[name]);
        }
      }
      return "outfit(" + lsig.join(",") + ")" + (csig ? " colors(" + csig.join(",") + ")" : "") + (stendhal.config.getBoolean("effect.no-nude") ? " no-nude" : "");
    }
    /**
     * Compares outfit signatures for equality.
     *
     * @param {any} obj
     *   The object to compare against this outfit.
     */
    equals(obj) {
      if (!(obj instanceof _Outfit)) {
        return false;
      }
      return obj.getSignature() === this.getSignature();
    }
    /**
     * Creates a sprite sheet from layer information.
     *
     * TODO:
     *   - use dress layer based on body type
     */
    async toImage() {
      let parts = [];
      let outfitLayers = this.getLayers();
      const detailIndex = this.getLayerIndex("detail") || 0;
      if (OutfitStore.get().detailHasRearLayer(detailIndex)) {
        outfitLayers = [new Pair("detail-rear", detailIndex), ...outfitLayers];
      }
      for (const l of outfitLayers) {
        let layerName = l.first;
        let suffix = "";
        if (layerName.endsWith("-rear")) {
          suffix = "-rear";
          layerName = layerName.replace(/-rear$/, "");
        }
        const layerIndex = l.second;
        let layerPath = Paths.sprites + "/outfit/" + layerName + "/" + StringUtil.padLeft("" + layerIndex, "0", 3) + suffix;
        if (layerName === "body" && stendhal.config.getBoolean("effect.no-nude")) {
          layerPath += "-nonude";
        }
        layerPath += ".png";
        let filter = void 0;
        let coloring = this.getLayerColor(layerName);
        if (coloring) {
          filter = "trueColor";
        }
        parts.push(new ImageMetadata(layerPath, filter, coloring));
      }
      return await new MultilayerImageLoader(parts).load();
    }
  };

  // ui/component/OutfitPreviewComponent.ts
  var OutfitPreviewComponent = class extends Component {
    dir;
    index;
    image;
    bgColor;
    constructor() {
      super("outfit-preview-template");
      this.dir = Direction.DOWN;
      this.index = 1;
    }
    /**
     * Sets current outfit for this preview & updates drawing.
     *
     * @param {string} outfit
     *   Outfit formatted string ("body=0,head=2,eyes=1,...).
     * @param {string=} coloring
     *   Outfit coloring.
     */
    setOutfit(outfit, coloring) {
      const otemp = Outfit.build(outfit, coloring);
      otemp.toImage().then((image) => {
        this.image?.close();
        this.image = image;
        this.update();
      });
    }
    /**
     * Sets facing direction to draw.
     *
     * Default is down.
     *
     * @param {Direction} dir
     *   Facing direction.
     */
    setDirection(dir) {
      this.dir = dir.val < 1 ? Direction.VALUES[1] : dir.val > 4 ? Direction.VALUES[4] : dir;
      this.update();
    }
    /**
     * Increments facing direction to draw clockwise.
     */
    nextDirection() {
      let dir = Direction.VALUES[this.dir.val + 1];
      if (typeof dir === "undefined") {
        dir = Direction.VALUES[1];
      }
      this.setDirection(dir);
    }
    /**
     * Increments facing direction to draw counter-clockwise.
     */
    prevDirection() {
      let dir = Direction.VALUES[this.dir.val - 1];
      if (typeof dir === "undefined" || dir.val === 0) {
        dir = Direction.VALUES[4];
      }
      this.setDirection(dir);
    }
    /**
     * Sets frame index to draw.
     *
     * Default is 1 (center frame).
     *
     * @param {number} index
     *   Frame index.
     */
    setFrame(index) {
      this.index = index < 0 ? 0 : index > 2 ? 2 : index;
      this.update();
    }
    /**
     * Sets or unsets background color to fill before drawing outfit.
     *
     * @param {string|undefined|null} color
     *   Background color or `undefined|null` or empty string to unset.
     */
    setBGColor(color) {
      if (!color || StringUtil.isEmpty(color)) {
        color = void 0;
      }
      this.bgColor = color;
    }
    /**
     * Draws outfit in preview area.
     */
    update() {
      if (!this.image || !this.image.height) {
        console.warn("outfit preview not ready");
        return;
      }
      const w = this.image.width / 3;
      const h = this.image.height / 4;
      const ctx = this.componentElement.getContext("2d");
      if (w !== ctx.canvas.width || h !== ctx.canvas.height) {
        console.warn("Image dimensions do not match preview canvas: " + w + "x" + h + " != " + ctx.canvas.width + "x" + ctx.canvas.height);
      }
      if (this.bgColor) {
        ctx.fillStyle = this.bgColor;
        ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);
      } else {
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
      }
      ctx.drawImage(this.image, this.index * w, (this.dir.val - 1) * h, w, h, 0, 0, w, h);
    }
    close() {
      this.image?.close();
    }
  };

  // ui/dialog/outfit/OutfitDialog.ts
  var import_marauroa70 = __toESM(require_marauroa());
  var OutfitDialog = class extends DialogContentComponent {
    hatSelector;
    hairSelector;
    maskSelector;
    eyesSelector;
    mouthSelector;
    headSelector;
    bodySelector;
    dressSelector;
    hairColorSelector;
    eyesColorSelector;
    dressColorSelector;
    skinColorSelector;
    /** Composite preview. */
    outfitPreview;
    constructor() {
      super("outfitdialog-template");
      ui.registerComponent(UIComponentEnum.OutfitDialog, this);
      this.outfitPreview = new OutfitPreviewComponent();
      this.outfitPreview.componentElement.id = "setoutfitcompositecanvas";
      this.outfitPreview.setBGColor("white");
      this.child("#outfit-preview-composite").appendChild(this.outfitPreview.componentElement);
      this.child("#rotate-c").addEventListener("click", (e) => {
        this.outfitPreview.nextDirection();
      });
      this.child("#rotate-cc").addEventListener("click", (e) => {
        this.outfitPreview.prevDirection();
      });
      queueMicrotask(() => {
        this.createDialog();
      });
    }
    getConfigId() {
      return "outfit";
    }
    createDialog() {
      let outfit = import_marauroa70.marauroa.me["outfit_ext_orig"];
      if (outfit === void 0) {
        outfit = import_marauroa70.marauroa.me["outfit_ext"];
      }
      let entries = outfit.split(",");
      let currentOutfit = {};
      for (let i = 0; i < entries.length; i++) {
        let entry = entries[i].split("=");
        currentOutfit[entry[0]] = entry[1];
      }
      this.hatSelector = this.makeSelector("hat", currentOutfit["hat"]);
      this.hairSelector = this.makeSelector("hair", currentOutfit["hair"]);
      this.maskSelector = this.makeSelector("mask", currentOutfit["mask"]);
      this.eyesSelector = this.makeSelector("eyes", currentOutfit["eyes"]);
      this.mouthSelector = this.makeSelector("mouth", currentOutfit["mouth"]);
      this.headSelector = this.makeSelector("head", currentOutfit["head"]);
      this.bodySelector = this.makeSelector("body", currentOutfit["body"]);
      this.dressSelector = this.makeSelector("dress", currentOutfit["dress"]);
      this.hairColorSelector = this.createColorSelector(OutfitColorSelector, "hair", this.hairSelector);
      this.eyesColorSelector = this.createColorSelector(OutfitColorSelector, "eyes", this.eyesSelector);
      this.dressColorSelector = this.createColorSelector(OutfitColorSelector, "dress", this.dressSelector);
      this.skinColorSelector = this.createColorSelector(OutfitPaletteColorSelector, "skin", this.headSelector, this.bodySelector);
      this.drawComposite();
      this.child("#setoutfitcancel").addEventListener("click", (event2) => {
        this.onCancel(event2);
      });
      this.child("#setoutfitapply").addEventListener("click", (event2) => {
        this.onApply(event2);
      });
    }
    onCancel(event2) {
      this.componentElement.dispatchEvent(new Event("close"));
      event2.preventDefault();
    }
    /**
     * Creates an outfit string from individual parts selectors.
     *
     * @returns {string}
     *   Outfit formatted string ("body=0,head=2,eyes=1,...).
     */
    buildOutfitString() {
      return "body=" + this.bodySelector.index.toString() + ",dress=" + this.dressSelector.index.toString() + ",head=" + this.headSelector.index.toString() + ",mouth=" + this.mouthSelector.index.toString() + ",eyes=" + this.eyesSelector.index.toString() + ",mask=" + this.maskSelector.index.toString() + ",hair=" + this.hairSelector.index.toString() + ",hat=" + this.hatSelector.index.toString();
    }
    /**
     * Creates an outfit coloring string from individual parts selectors.
     *
     * @returns {string}
     *   Outfit coloring formatted string ("skin=14179110,hair=16446211,...").
     */
    buildColoringString() {
      const colors = [];
      let color = this.hairColorSelector.color;
      if (color != null) {
        colors.push("hair=" + color.toString());
      }
      color = this.eyesColorSelector.color;
      if (color != null) {
        colors.push("eyes=" + color.toString());
      }
      color = this.dressColorSelector.color;
      if (color != null) {
        colors.push("dress=" + color.toString());
      }
      color = this.skinColorSelector.color;
      if (color != null) {
        colors.push("skin=" + color.toString());
      }
      return colors.join(",");
    }
    onApply(event2) {
      const action = {
        "type": "outfit_ext",
        "zone": import_marauroa70.marauroa.currentZoneName,
        "value": this.buildOutfitString()
      };
      let color = this.hairColorSelector.color;
      if (color != null) {
        action["hair"] = color.toString();
      }
      color = this.eyesColorSelector.color;
      if (color != null) {
        action["eyes"] = color.toString();
      }
      color = this.dressColorSelector.color;
      if (color != null) {
        action["dress"] = color.toString();
      }
      color = this.skinColorSelector.color;
      if (color != null) {
        action["skin"] = color.toString();
      }
      import_marauroa70.marauroa.clientFramework.sendAction(action);
      event2.preventDefault();
      this.componentElement.dispatchEvent(new Event("close"));
    }
    makeSelector(part, index) {
      let minIndex = 0;
      if (part === "dress" && stendhal.config.getBoolean("effect.no-nude")) {
        minIndex = 1;
      }
      if (index < minIndex || index === void 0) {
        index = minIndex;
      }
      const selector = new OutfitPartSelector(part, index, minIndex, OutfitStore.get().count[part] - 1, () => {
        this.drawComposite();
      });
      document.getElementById("setoutfitprev" + part).addEventListener("click", function(_e) {
        selector.previous();
      });
      document.getElementById("setoutfitnext" + part).addEventListener("click", function(_e) {
        selector.next();
      });
      selector.draw();
      return selector;
    }
    drawComposite() {
      this.outfitPreview.setOutfit(this.buildOutfitString(), this.buildColoringString());
    }
    initialColorValue(part) {
      const colors = import_marauroa70.marauroa.me["outfit_colors"];
      if (colors != null) {
        const colorName = OutfitStore.get().isSkinLayer(part) ? "skin" : part;
        let layer_color = colors[colorName + "_orig"];
        if (layer_color === void 0) {
          layer_color = colors[colorName];
        }
        return layer_color;
      }
      return null;
    }
    createColorSelector(classObject, part, ...partSelectors) {
      const toggle = document.getElementById("setoutfit" + part + "colortoggle");
      const canvas = document.getElementById("setoutfit" + part + "colorcanvas");
      const gradientCanvas = document.getElementById("setoutfit" + part + "colorgradient");
      const selector = new classObject(canvas, gradientCanvas, (color) => {
        for (const partSelector of partSelectors) {
          partSelector.color = color;
        }
      });
      const initialColor = this.initialColorValue(part);
      if (initialColor != null) {
        toggle.checked = true;
        selector.color = initialColor;
      }
      toggle.addEventListener("change", function(_e) {
        selector.enabled = toggle.checked;
      });
      selector.draw();
      return selector;
    }
    onParentClose() {
      this.outfitPreview.close();
      this.hatSelector.close();
      this.hairSelector.close();
      this.maskSelector.close();
      this.eyesSelector.close();
      this.mouthSelector.close();
      this.headSelector.close();
      this.bodySelector.close();
      this.dressSelector.close();
      ui.unregisterComponent(this);
    }
  };

  // entity/User.ts
  var User = class extends Player {
    minimapStyle = Color.USER;
    constructor() {
      super();
      ui.onUserReady();
    }
    set(key, value) {
      const oldX = this["x"];
      const oldY = this["y"];
      super.set(key, value);
      if ((key === "x" || key === "y") && this["x"] && this["y"] && (this["x"] !== oldX || this["y"] !== oldY)) {
        singletons.getLoopedSoundSourceManager().onDistanceChanged(this["x"], this["y"]);
      }
      queueMicrotask(() => {
        ui.get(UIComponentEnum.PlayerStats).update(key);
        ui.get(UIComponentEnum.Bag).update();
        ui.get(UIComponentEnum.Keyring).update();
        ui.get(UIComponentEnum.StatusesList).update(this);
      });
    }
    unset(key) {
      super.unset(key);
      queueMicrotask(() => {
        ui.get(UIComponentEnum.StatusesList).update(this);
      });
    }
    buildActions(list) {
      super.buildActions(list);
      const charname = this["_name"];
      list.push({
        title: this.stopped() ? "Walk" : "Stop",
        action: function(_entity) {
          import_marauroa71.marauroa.clientFramework.sendAction({ "type": "walk" });
        }
      });
      list.push({
        title: "Set outfit",
        action: function(_entity) {
          let outfitDialog = ui.get(UIComponentEnum.OutfitDialog);
          if (!outfitDialog) {
            const dstate = stendhal.config.getWindowState("outfit");
            outfitDialog = new OutfitDialog();
            new FloatingWindow("Choose outfit", outfitDialog, dstate.x, dstate.y).setId("outfit");
          }
        }
      });
      list.push({
        title: "Where",
        action: function(_entity) {
          var action = {
            "type": "where",
            "target": charname
          };
          import_marauroa71.marauroa.clientFramework.sendAction(action);
        }
      });
    }
    //~ override onMiniMapDraw() {
    //~ }
    /**
     * Can the player hear this chat message?
     *
     * @param entity
     *     The speaking entity.
     * @param rangeSquared
     *     Distance squared within which the entity can be heard (-1
     *     represents entire map).
     */
    isInHearingRange(entity, rangeSquared) {
      if (entity === import_marauroa71.marauroa.me || this.isAdmin()) {
        return true;
      }
      let hearingRange = 15;
      if (typeof rangeSquared !== "undefined") {
        if (rangeSquared < 0) {
          hearingRange = -1;
        } else {
          hearingRange = Math.sqrt(rangeSquared);
        }
      }
      return hearingRange < 0 || Math.abs(this["x"] - entity["x"]) < hearingRange && Math.abs(this["y"] - entity["y"]) < hearingRange;
    }
    /**
     * Checks if player is within range to hear a sound event.
     *
     * @param {number} radius
     *   Distance at which sound can be heard.
     * @param {Entity} entity
     *   Entity emitting sound event.
     * @returns {boolean}
     *   `true` if sound should be loaded (when event radius is more than -1 & user position is within
     *   radial distance from event origin).
     */
    isInSoundRange(radius, entity) {
      if (entity === this) {
        return true;
      }
      if (radius < 0) {
        return false;
      }
      return Math.abs(this["x"] - entity["x"]) + Math.abs(this["y"] - entity["y"]) <= radius;
    }
    /**
     * Actions when player leaves a zone.
     */
    onExitZone() {
      super.onExitZone();
      ViewPort.get().onExitZone();
      const msgs = [];
      const lssm = singletons.getLoopedSoundSourceManager();
      if (!lssm.removeAll()) {
        let tmp = "LoopedSoundSourceManager reported not all sources stopped on zone change:";
        const loopSources = lssm.getSources();
        for (const id in loopSources) {
          const snd = loopSources[id].sound;
          tmp += "\n- ID: " + id + " (" + snd.src + ")";
        }
        msgs.push(tmp);
      }
      if (!stendhal.sound.stopAll()) {
        let tmp = "SoundManager reported not all sounds stopped on zone change:";
        for (const snd of stendhal.sound.getActive()) {
          tmp += "\n- " + snd.src;
          if (snd.loop) {
            tmp += " (loop)";
          }
        }
        msgs.push(tmp);
      }
      for (const msg of msgs) {
        console.warn(msg);
      }
    }
    /**
     * Actions when player enters a zone.
     */
    onEnterZone() {
      super.onEnterZone();
      singletons.getLoopedSoundSourceManager().onZoneReady();
    }
    /**
     * Checks if player has autowalk set.
     */
    autoWalkEnabled() {
      return typeof this["autowalk"] !== "undefined";
    }
    /**
     * Checks if player meets requirements to invite others into a group.
     *
     * @return
     *     <code>true</code> if player is not in a group or is leader of a group.
     */
    canInviteToGroup() {
      const gman = singletons.getGroupManager();
      return gman.getMemberCount() == 0 || gman.getLeader() === this["name"];
    }
    /**
     * Sends event to server to set direction user should face.
     *
     * @param dir {util.Direction.Direction}
     *   New direction to face.
     */
    faceTo(dir) {
      import_marauroa71.marauroa.clientFramework.sendAction({ type: "face", dir: "" + dir.val });
    }
    /**
     * Sends event to server to set or stop player's walking direction.
     *
     * @param dir {util.Direction.Direction}
     *   Direction for character to move or stop.
     * @param cancelAutoWalk {boolean}
     *   If `true` stops movement if auto-walk is active & "dir" matches user's current direction of
     *   movement.
     */
    setDirection(dir, cancelAutoWalk = false) {
      if (dir == Direction.STOP) {
        this.stop();
        return;
      }
      ViewPort.get().freeze = false;
      if (cancelAutoWalk && this.autoWalkEnabled() && this.getWalkDirection() == dir) {
        import_marauroa71.marauroa.clientFramework.sendAction({ type: "walk" });
        return;
      }
      import_marauroa71.marauroa.clientFramework.sendAction({ type: "move", dir: "" + dir.val });
    }
    /**
     * Sends action to server to stop player's walking direction.
     */
    stop() {
      import_marauroa71.marauroa.clientFramework.sendAction({ type: "stop" });
    }
    moveTo(p1, p2, p3) {
      ViewPort.get().freeze = false;
      let action = {};
      if (typeof p1 === "object") {
        action = p1;
      } else {
        action.x = p1.toString();
        action.y = p2.toString();
        if (p3) {
          action.zone = p3;
        }
      }
      action.type = "moveto";
      import_marauroa71.marauroa.clientFramework.sendAction(action);
    }
  };

  // entity/VisibleEntity.ts
  var VisibleEntity = class extends Entity {
    zIndex = 1;
    constructor() {
      super();
    }
    init() {
      let filename = Paths.sprites + "/" + (this["class"] || "") + "/" + (this["subclass"] || "") + "/" + (this["_name"] || "") + ".png";
      let state = this["state"] * 32 || 0;
      this.imageSprite = new ImageSprite(images.load(filename), 0, state, 32, 32);
    }
    set(key, value) {
      super.set(key, value);
      if (key === "class" || key === "subclass" || key === "_name") {
        if (this.imageSprite) {
          this.imageSprite.free();
          this.init();
        }
      } else if (key === "state") {
        if (this.imageSprite) {
          this.imageSprite.offsetY = value * 32;
        }
      }
    }
    isVisibleToAction(_filter) {
      return true;
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/look.png) 1 3, auto";
    }
  };

  // entity/WalkBlocker.ts
  var WalkBlocker = class extends Entity {
    zIndex = 3e3;
    isVisibleToAction(_filter) {
      return true;
    }
    getCursor(_x, _y) {
      return "url(" + Paths.sprites + "/cursor/stop.png) 1 3, auto";
    }
  };

  // entity/EntityRegistry.ts
  var EntityRegistry = class {
    init() {
      import_marauroa72.marauroa.rpobjectFactory.register("area", InvisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("baby_dragon", DomesticAnimal);
      import_marauroa72.marauroa.rpobjectFactory.register("blackboard", Sign);
      import_marauroa72.marauroa.rpobjectFactory.register("blocktarget", InvisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("block", VisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("blood", Blood);
      import_marauroa72.marauroa.rpobjectFactory.register("cat", DomesticAnimal);
      import_marauroa72.marauroa.rpobjectFactory.register("chest", Chest);
      import_marauroa72.marauroa.rpobjectFactory.register("corpse", Corpse);
      import_marauroa72.marauroa.rpobjectFactory.register("creature", Creature);
      import_marauroa72.marauroa.rpobjectFactory.register("_default", UnknownEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("domesticanimal", DomesticAnimal);
      import_marauroa72.marauroa.rpobjectFactory.register("door", Door);
      import_marauroa72.marauroa.rpobjectFactory.register("flyover", InvisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("food", Food);
      import_marauroa72.marauroa.rpobjectFactory.register("game_board", GameBoard);
      import_marauroa72.marauroa.rpobjectFactory.register("gate", Gate);
      import_marauroa72.marauroa.rpobjectFactory.register("growing_entity_spawner", GrowingEntitySpawner);
      import_marauroa72.marauroa.rpobjectFactory.register("house_portal", Portal);
      import_marauroa72.marauroa.rpobjectFactory.register("invisible_entity", InvisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("item", Item);
      import_marauroa72.marauroa.rpobjectFactory.register("looped_sound_source", LoopedSoundSource);
      import_marauroa72.marauroa.rpobjectFactory.register("npc", NPC);
      import_marauroa72.marauroa.rpobjectFactory.register("plant_grower", VisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("player", Player);
      import_marauroa72.marauroa.rpobjectFactory.register("portal", Portal);
      import_marauroa72.marauroa.rpobjectFactory.register("rented_sign", Sign);
      import_marauroa72.marauroa.rpobjectFactory.register("sheep", DomesticAnimal);
      import_marauroa72.marauroa.rpobjectFactory.register("sign", Sign);
      import_marauroa72.marauroa.rpobjectFactory.register("tiled_entity", InvisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("training_dummy", TrainingDummy);
      import_marauroa72.marauroa.rpobjectFactory.register("unknown", UnknownEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("useable_entity", UseableEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("user", User);
      import_marauroa72.marauroa.rpobjectFactory.register("visible_entity", VisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("walkblocker", WalkBlocker);
      import_marauroa72.marauroa.rpobjectFactory.register("wall", InvisibleEntity);
      import_marauroa72.marauroa.rpobjectFactory.register("weather_entity", InvisibleEntity);
    }
  };

  // ui/dialog/ChooseCharacterDialog.ts
  var ChooseCharacterDialog = class extends DialogContentComponent {
    constructor(characters) {
      super("choose-character-template");
      const characterList = this.child("#characters");
      for (var i in characters) {
        if (characters.hasOwnProperty(i)) {
          let name = characters[i]["a"]["name"];
          let button = document.createElement("button");
          button.classList.add("menubutton");
          button.innerText = name;
          button.addEventListener("click", () => {
            this.componentElement.dispatchEvent(new Event("close"));
            Client.get().chooseCharacter(name);
          });
          characterList.append(button);
        }
      }
      this.child("#logout").addEventListener("click", (e) => {
        this.componentElement.dispatchEvent(new Event("close"));
        this.onLogout();
      });
    }
    onLogout() {
      queueMicrotask(() => {
        SlashActionRepo.get().execute("/logout");
      });
    }
  };

  // ui/dialog/CreateAccountDialog.ts
  var import_marauroa73 = __toESM(require_marauroa());
  var CreateAccountDialog = class extends DialogContentComponent {
    constructor() {
      super("createaccountdialog-template");
      this.child("input[type=submit]").addEventListener("click", (event2) => {
        event2.preventDefault();
        let username = this.child("#username").value;
        let password = this.child("#password").value;
        let passwordRepeat = this.child("#passwordrepeat").value;
        let email = this.child("#email").value;
        if (!username || !password || !passwordRepeat) {
          alert("Please fill in all required fields.");
          return;
        }
        if (password != passwordRepeat) {
          alert("Password and password repetition do not match.");
          return;
        }
        import_marauroa73.marauroa.clientFramework.createAccount(username, password, email);
      });
      this.child("a").addEventListener("click", (event2) => {
        event2.preventDefault();
        this.close();
        ui.createSingletonFloatingWindow(
          "Login",
          new LoginDialog(),
          100,
          50
        );
      });
    }
  };

  // ui/dialog/LoginDialog.ts
  var import_marauroa74 = __toESM(require_marauroa());
  var LoginDialog = class extends DialogContentComponent {
    constructor() {
      super("logindialog-template");
      this.componentElement.addEventListener("keydown", (e) => {
        e.stopPropagation();
      });
      this.componentElement.addEventListener("keyup", (e) => {
        e.stopPropagation();
      });
      this.child("button").addEventListener("click", (event2) => {
        event2.preventDefault();
        let username = this.child("#username").value;
        let password = this.child("#password").value;
        Client.get().username = username;
        import_marauroa74.marauroa.clientFramework.login(username, password);
        this.close();
      });
      this.child("a").addEventListener("click", (event2) => {
        event2.preventDefault();
        this.close();
        ui.createSingletonFloatingWindow(
          "Create Account",
          new CreateAccountDialog(),
          100,
          50
        );
      });
    }
  };

  // ui/ChatPanel.ts
  var ChatPanel = class extends Panel {
    constructor() {
      super("bottomPanel");
      this.setFloating(singletons.getConfigManager().getBoolean("chat.float"));
    }
    /**
     * Updates chat panel to float or be positioned statically.
     *
     * @param floating
     *   `true` if panel should float.
     */
    setFloating(floating) {
      singletons.getConfigManager().set("chat.float", floating);
      let propPosition = "static";
      let propOpacity = "1.0";
      if (floating) {
        propPosition = "absolute";
        propOpacity = "0.5";
      } else {
        this.setVisible(true);
      }
      this.componentElement.style.setProperty("position", propPosition);
      this.componentElement.style.setProperty("opacity", propOpacity);
      this.refresh();
    }
    /**
     * Checks if panel is in floating state.
     *
     * @return
     *   `true` if "position" style property is set to "absolute".
     */
    isFloating() {
      return getComputedStyle(this.componentElement).getPropertyValue("position") === "absolute";
    }
    /**
     * Updates element using viewport attributes.
     */
    refresh() {
      const floating = this.isFloating();
      if (floating) {
        const rect = ViewPort.get().getElement().getBoundingClientRect();
        const halfHeight = Math.abs(rect.height / 2);
        this.componentElement.style["width"] = rect.width + "px";
        this.componentElement.style["height"] = halfHeight + "px";
        this.componentElement.style["left"] = rect.left + "px";
        this.componentElement.style["top"] = rect.top + halfHeight + "px";
        this.componentElement.classList.remove("background");
      } else {
        for (const prop of ["width", "height", "left", "top"]) {
          this.componentElement.style.removeProperty(prop);
        }
        this.componentElement.classList.add("background");
      }
      QuickMenu.setButtonEnabled(UIComponentEnum.QMChat, floating);
      ViewPort.get().onChatPanelRefresh(floating);
    }
    /**
     * Shows chat panel when enter key is pressed.
     */
    onEnterPressed() {
      if (this.isFloating()) {
        this.setVisible(!this.isVisible());
      }
    }
    /**
     * Hides chat panel after sending message if auto-hiding enabled.
     */
    onMessageSent() {
      if (this.isFloating() && this.isVisible() && singletons.getConfigManager().getBoolean("chat.autohide")) {
        this.setVisible(false);
      }
    }
    setVisible(visible = true) {
      const stateChanged = visible != this.isVisible();
      super.setVisible(visible);
      singletons.getConfigManager().set("chat.visible", visible);
      const chatButton = ui.get(UIComponentEnum.QMChat);
      if (chatButton) {
        chatButton.update();
      }
      const chatLog = ui.get(UIComponentEnum.ChatLog);
      if (stateChanged && chatLog) {
        this.isVisible() ? chatLog.onUnhide() : chatLog.onHide();
      }
    }
  };

  // ui/component/BagComponent.ts
  var import_marauroa75 = __toESM(require_marauroa());
  var BagComponent = class extends ItemInventoryComponent {
    slotSize = "3 4";
    constructor(object, slot, sizeX, sizeY, quickPickup, defaultImage) {
      super(object, slot, sizeX, sizeY, quickPickup, defaultImage);
    }
    update() {
      let features = null;
      if (import_marauroa75.marauroa.me != null) {
        features = import_marauroa75.marauroa.me["features"];
      }
      if (features == null) {
        return;
      }
      let size = features["bag"];
      if (size) {
        if (this.slotSize != size) {
          this.slotSize = size;
          let sizeArray = size.split(" ");
          let sizeX = parseInt(sizeArray[0], 10);
          let sizeY = parseInt(sizeArray[1], 10);
          this.resize(sizeX, sizeY);
        }
      }
      super.update();
    }
    resize(sizeX, sizeY) {
      super.setSize(sizeX, sizeY);
      this.itemContainerImplementation.init(sizeX * sizeY);
    }
  };

  // ui/component/BuddyListComponent.ts
  var import_marauroa76 = __toESM(require_marauroa());
  var BuddyListComponent = class extends Component {
    buddies = [];
    current;
    lastHtml;
    constructor() {
      super("buddyList");
      this.componentElement.addEventListener("mouseup", (event2) => {
        this.onMouseUp(event2);
      });
      this.componentElement.addEventListener("contextmenu", (event2) => {
        event2.preventDefault();
      });
    }
    // TODO: don't rebuilt the buddylist completely on every turn,
    //       but implement an observer
    update() {
      let data = import_marauroa76.marauroa.me["buddies"];
      let buddies = [];
      for (let buddy in data) {
        if (data.hasOwnProperty(buddy)) {
          let entry = { "name": buddy };
          if (data[buddy] == "true") {
            entry.isOnline = true;
            entry.status = "online";
          } else {
            entry.isOnline = false;
            entry.status = "offline";
          }
          buddies.push(entry);
        }
      }
      this.sort(buddies);
      let html = "";
      for (let i = 0; i < buddies.length; i++) {
        html += "<li class=" + buddies[i].status + '><img src="';
        if (buddies[i].status == "online") {
          html += Paths.gui + "/buddy_online.png";
        } else {
          html += Paths.gui + "/buddy_offline.png";
        }
        html += '"> ' + HTMLUtil.esc(buddies[i].name) + "</li>";
      }
      if (this.lastHtml !== html) {
        let buddyListUL = this.child("#buddyListUL");
        buddyListUL.innerHTML = html;
        this.lastHtml = html;
      }
      this.buddies = buddies;
    }
    /**
     * sorts the buddy list
     */
    sort(buddies) {
      buddies.sort(function compare(a, b) {
        if (a.isOnline) {
          if (!b.isOnline) {
            return -1;
          }
        } else {
          if (b.isOnline) {
            return 1;
          }
        }
        if (a.name < b.name) {
          return -1;
        }
        if (a.name > b.name) {
          return 1;
        }
        return 0;
      });
    }
    setBuddyStatus(buddy, status) {
      this.removeBuddy(buddy);
      let newEntry = { "name": buddy, "status": status };
      this.buddies.push(newEntry);
      this.sort(this.buddies);
    }
    hasBuddy(buddy) {
      let arrayLength = this.buddies.length;
      for (let i = 0; i < arrayLength; i++) {
        if (this.buddies[i].name === buddy) {
          return true;
        }
      }
      return false;
    }
    removeBuddy(buddy) {
      let arrayLength = this.buddies.length;
      for (let i = 0; i < arrayLength; i++) {
        if (this.buddies[i].name === buddy) {
          this.buddies.splice(i, 1);
          return;
        }
      }
    }
    buildActions(actions) {
      if (!this.current) {
        return;
      }
      if (this.current.className === "online") {
        actions.push({
          title: "Talk",
          action: function(buddyListComponent) {
            ui.get(UIComponentEnum.ChatInput).setText("/msg " + buddyListComponent.current.textContent.trim() + " ");
          }
        });
        actions.push({
          title: "Where",
          action: function(buddyListComponent) {
            let action = {
              "type": "where",
              "target": buddyListComponent.current.textContent.trim(),
              "zone": import_marauroa76.marauroa.currentZoneName
            };
            import_marauroa76.marauroa.clientFramework.sendAction(action);
          }
        });
      } else {
        actions.push({
          title: "Leave Message",
          action: function(buddyListComponent) {
            ui.get(UIComponentEnum.ChatInput).setText("/storemessage " + buddyListComponent.current.textContent.trim() + " ");
          }
        });
      }
      actions.push({
        title: "Remove",
        action: function(buddyListComponent) {
          let action = {
            "type": "removebuddy",
            "target": buddyListComponent.current.textContent.trim(),
            "zone": import_marauroa76.marauroa.currentZoneName
          };
          import_marauroa76.marauroa.clientFramework.sendAction(action);
          buddyListComponent.removeBuddy(buddyListComponent.current.textContent.trim());
        }
      });
    }
    onMouseUp(event2) {
      this.current = void 0;
      let target = event2.target;
      if (target.tagName === "LI") {
        this.current = target;
      } else if (target.tagName === "IMG") {
        this.current = target.parentElement;
      }
      if (this.current) {
        stendhal.ui.actionContextMenu.set(ui.createSingletonFloatingWindow(
          "Action",
          new ActionContextMenu(this),
          Math.max(10, event2.pageX - 50),
          event2.pageY - 5
        ));
      }
    }
  };

  // ui/component/ChatInputComponent.ts
  var import_marauroa78 = __toESM(require_marauroa());

  // ui/dialog/EmojiMapDialog.ts
  var import_marauroa77 = __toESM(require_marauroa());
  var EmojiMapDialog = class _EmojiMapDialog extends DialogContentComponent {
    static glyphs = [
      "\u263A",
      "\u263B",
      "\u{1F642}",
      "\u{1F643}",
      "\u{1F60A}",
      "\u{1F609}",
      "\u{1F61B}",
      "\u{1F61C}",
      "\u{1F60B}",
      "\u{1F600}",
      "\u{1F603}",
      "\u2639",
      "\u{1F641}",
      "\u{1F622}",
      "\u{1F972}",
      "\u{1F602}",
      "\u{1F615}",
      "\u{1F644}",
      "\u{1F610}",
      "\u{1F611}",
      "\u{1F636}",
      "\u{1F632}",
      "\u{1F62E}",
      "\u{1F62F}",
      "\u{1F616}",
      "\u{1F623}",
      "\u{1F913}",
      "\u{1F60E}",
      "\u{1F607}",
      "\u{1F47C}",
      "\u2764",
      "\u{1F499}",
      "\u{1F49A}",
      "\u{1F49C}",
      "\u{1F49B}",
      "\u{1F498}",
      "\u{1F494}",
      "\u{1F4A2}",
      "\u{1F5E2}",
      "\u{1F444}",
      "\u{1F48B}",
      "\u{1F4A7}",
      "\u2669",
      "\u{1D15F}",
      "\u266A",
      "\u{1D160}",
      "\u{1F3B5}",
      "\u266C",
      "\u{1F39C}",
      "\u266B",
      "\u{1F39D}",
      "\u{1F44D}",
      "\u{1F44E}",
      "\u{1F44B}"
    ];
    /** Property determining if dialog will be loaded with built-in or native emojis. */
    sysEmojis;
    constructor() {
      super("emojimap-template");
      this.sysEmojis = stendhal.config.getBoolean("emojis.native");
      const emojiStore = singletons.getEmojiStore();
      let emojiList = _EmojiMapDialog.glyphs;
      if (!this.sysEmojis) {
        emojiList = emojiStore.getEmojiList();
      }
      const row = document.createElement("div");
      this.componentElement.appendChild(row);
      for (const emoji of emojiList) {
        const button = document.createElement("button");
        button.classList.add("shortcut-button", "emoji-text");
        if (!this.sysEmojis) {
          button.appendChild(htmlImageStore.get(Paths.sprites + "/emoji/" + emoji + ".png").cloneNode());
        } else {
          button.innerText = emoji;
        }
        button.addEventListener("click", (evt) => {
          this.onButtonPressed(emoji);
        });
        row.appendChild(button);
      }
    }
    onButtonPressed(emoji) {
      if (!this.sysEmojis) {
        const action = {
          "type": "chat",
          "text": ":" + emoji + ":"
        };
        import_marauroa77.marauroa.clientFramework.sendAction(action);
        this.close();
        return;
      }
      singletons.getChatInput().appendText(emoji);
    }
  };

  // ui/component/ChatInputComponent.ts
  var ChatInputComponent = class extends Component {
    /** Remembered input values for quick lookup. */
    history;
    /** Selected index in history entries. */
    historyIndex;
    /** DOM element. */
    inputElement;
    constructor() {
      super("chatinput");
      this.inputElement = this.componentElement;
      this.componentElement.addEventListener("keydown", (event2) => {
        this.onKeyDown(event2);
      });
      this.componentElement.addEventListener("keypress", (event2) => {
        this.onKeyPress(event2);
      });
      this.history = stendhal.config.getObject("chat.history") || [];
      this.historyIndex = stendhal.config.getInt("chat.history.index", 0);
      const btn_send = document.getElementById("send-button");
      btn_send.addEventListener("click", (e) => {
        this.send();
      });
      const btn_keyword = document.getElementById("keywords-button");
      btn_keyword.addEventListener("click", (e) => {
        ChatOptionsDialog.createOptions();
      });
      const btn_emoji = document.getElementById("emojis-button");
      btn_emoji.addEventListener("click", (e) => {
        this.buildEmojiMap();
      });
      this.refresh();
    }
    /**
     * Updates attributes of DOM element & shortcuts buttons.
     */
    refresh() {
      super.refresh();
      const btn_emoji = document.getElementById("emojis-button");
      ;
      while (btn_emoji.lastChild != null) {
        btn_emoji.removeChild(btn_emoji.lastChild);
      }
      if (stendhal.config.getBoolean("emojis.native")) {
        btn_emoji.innerText = "\u263A";
      } else {
        btn_emoji.appendChild(htmlImageStore.get(Paths.sprites + "/emoji/smile.png").cloneNode());
      }
    }
    /**
     * Removes all text from input.
     */
    clear() {
      this.inputElement.value = "";
      ChatCompletionHelper.get().reset();
    }
    /**
     * Sets text value of input.
     *
     * @param text {string}
     *   Text to be set.
     */
    setText(text) {
      const chatPanel = ui.get(UIComponentEnum.BottomPanel);
      if (!chatPanel.isVisible()) {
        chatPanel.setVisible(true);
      }
      this.inputElement.value = text;
      this.inputElement.focus();
    }
    /**
     * Appends to current value of input.
     *
     * @param text {string}
     *   Text to be added to end of value.
     */
    appendText(text) {
      this.setText(this.inputElement.value + text);
    }
    /**
     * Retrieves the current text value.
     */
    getText() {
      return this.inputElement.value;
    }
    /**
     * Sets text value from history.
     *
     * @param i {number}
     *   History index to use.
     */
    fromHistory(i) {
      this.historyIndex = this.historyIndex + i;
      if (this.historyIndex < 0) {
        this.historyIndex = 0;
      }
      if (this.historyIndex >= this.history.length) {
        this.historyIndex = this.history.length;
        this.clear();
      } else {
        this.inputElement.value = this.history[this.historyIndex];
      }
    }
    /**
     * Handles shift+arrow keypress events to cycle through history entries.
     */
    onKeyDown(event2) {
      let code = KeyCode.extract(event2);
      if (event2.shiftKey) {
        if (code === KeyCode.ARROW_UP) {
          event2.preventDefault();
          this.fromHistory(-1);
        } else if (code === KeyCode.ARROW_DOWN) {
          event2.preventDefault();
          this.fromHistory(1);
        }
      }
    }
    /**
     * Executes send action when enter key is pressed.
     */
    onKeyPress(event2) {
      if (event2.keyCode === KeyCode.ENTER) {
        this.send();
        event2.preventDefault();
      }
    }
    /**
     * Adds line of text to history.
     *
     * After reaching maximum of 100 entries oldest entry is overwritten in favor of new text.
     *
     * NOTE: Would it be beneficial at all to allow configuring history lines buffer in settings?
     *
     * @param text {string}
     *   Text to be added. Usually value of the text input.
     */
    remember(text) {
      if (this.history.length > 0 && text === this.history[this.history.length - 1]) {
        this.historyIndex = this.history.length;
        stendhal.config.set("chat.history.index", this.historyIndex);
        return;
      }
      if (this.history.length > 100) {
        this.history.shift();
      }
      this.history[this.history.length] = text;
      this.historyIndex = this.history.length;
      stendhal.config.set("chat.history", this.history);
      stendhal.config.set("chat.history.index", this.historyIndex);
    }
    /**
     * Processes current text value & executes appropriate action.
     */
    send() {
      let val = this.inputElement.value;
      let array = val.split(" ");
      if (array[0] === "/choosecharacter") {
        import_marauroa78.marauroa.clientFramework.chooseCharacter(array[1]);
      } else if (val === "/close") {
        import_marauroa78.marauroa.clientFramework.close();
      } else {
        if (stendhal.actions.execute(val)) {
          this.remember(val);
        }
      }
      this.clear();
      if (this.hasFocus()) {
        this.inputElement.blur();
      }
      ui.get(UIComponentEnum.BottomPanel).onMessageSent();
    }
    /**
     * Creates & shows the emoji map dialog.
     */
    buildEmojiMap() {
      const wstate = stendhal.config.getWindowState("shortcuts");
      const content = new EmojiMapDialog();
      const dialog = ui.createSingletonFloatingWindow("Emojis", content, wstate.x, wstate.y);
      dialog.setId("shortcuts");
      content.setFrame(dialog);
    }
  };

  // action/MenuItem.ts
  var MenuItem = class {
    title;
    type;
    action;
    index;
    constructor(title, action) {
      this.title = title;
      this.action = action;
    }
  };

  // ui/component/ChatLogComponent.ts
  var ChatLogComponent = class extends Component {
    /** Most recent state of chat log scrolling. */
    scrollStateBottom;
    /** Event listener for managing scroll position when component properties change such as changes to visibility. */
    scrollListener;
    constructor() {
      super("chat");
      this.refresh();
      this.scrollStateBottom = this.isAtBottom();
      let touchHandler = TouchHandler.get();
      this.componentElement.addEventListener("mouseup", (evt) => {
        this.onContextMenu(evt);
      });
      this.componentElement.addEventListener("touchstart", (evt) => {
        const pos = HTMLUtil.extractPosition(evt);
        touchHandler.onTouchStart(pos.pageX, pos.pageY);
      }, { passive: true });
      this.componentElement.addEventListener("touchend", (evt) => {
        touchHandler.onTouchEnd();
        this.onContextMenu(evt);
        touchHandler.unsetOrigin();
      });
      this.scrollListener = (evt) => {
        this.onScroll();
      };
      if (this.isVisible()) {
        this.componentElement.addEventListener("scroll", this.scrollListener);
      }
    }
    /**
     * Updates font from config.
     */
    refresh() {
      this.componentElement.style.setProperty("font-family", stendhal.config.get("font.chat"));
    }
    /**
     * Generates a timestamp.
     *
     * @return {HTMLSpanElement}
     *   `HTMLSpanElement` element with inner text formatted with timestamp.
     */
    createTimestamp() {
      const date = /* @__PURE__ */ new Date();
      let time = "" + date.getHours() + ":";
      if (date.getHours() < 10) {
        time = "0" + time;
      }
      if (date.getMinutes() < 10) {
        time = time + "0";
      }
      time = time + date.getMinutes();
      const timestamp = document.createElement("span");
      timestamp.className = "logtimestamp";
      timestamp.innerHTML = "[" + time + "]";
      return timestamp;
    }
    /**
     * Adds a line of text.
     *
     * @param row {HTMLDivElement}
     *   Div containing text to be added.
     */
    add(row) {
      const wasAtBottom = this.isAtBottom();
      this.componentElement.appendChild(row);
      if (wasAtBottom) {
        this.scrollToBottom();
      }
    }
    /**
     * Adds a line of text.
     *
     * @param type
     *   Message type.
     * @param message
     *   Text to be added.
     * @param orator
     *   Name of entity making the expression (default: `undefined`).
     * @param timestamp
     *   If `false`, suppresses prepending message with timestamp (default: `true`).
     */
    addLine(type, message, orator, timestamp = true) {
      if (orator) {
        message = orator + ": " + message;
      }
      const lcol = document.createElement("div");
      lcol.className = "logcolL";
      if (timestamp) {
        lcol.appendChild(this.createTimestamp());
      } else {
        lcol.innerHTML = " ";
      }
      const rcol = document.createElement("div");
      rcol.className = "logcolR log" + type;
      rcol.innerHTML += Chat.formatLogEntry(message);
      const row = document.createElement("div");
      row.className = "logrow";
      row.appendChild(lcol);
      row.appendChild(rcol);
      this.add(row);
    }
    /**
     * Adds multiple lines of text.
     *
     * @param type {string}
     *   Message type.
     * @param messages {string[]}
     *   Array of texts to be added.
     * @param orator
     *   Name of entity making the expression (default: `undefined`).
     */
    addLines(type, messages, orator) {
      let stamped = false;
      for (const line of messages) {
        if (!stamped) {
          this.addLine(type, line, orator);
          stamped = true;
        } else {
          this.addLine(type, line, orator, false);
        }
      }
    }
    /**
     * Adds a line displaying an emoji image.
     *
     * @param emoji {HTMLImageElement}
     *   Emoji image sprite.
     * @param orator {string}
     *   Name of entity making the expression (default: `undefined`).
     */
    addEmojiLine(emoji, orator) {
      const lcol = document.createElement("div");
      lcol.className = "logcolL";
      lcol.appendChild(this.createTimestamp());
      const rcol = document.createElement("div");
      rcol.className = "logcolR lognormal";
      if (orator) {
        rcol.innerHTML += orator + ": ";
      }
      rcol.appendChild(emoji.cloneNode());
      const row = document.createElement("div");
      row.className = "logrow";
      row.appendChild(lcol);
      row.appendChild(rcol);
      this.add(row);
    }
    /**
     * Removes all text from log.
     */
    clear() {
      this.componentElement.innerHTML = "";
    }
    /**
     * Sets scrolled position.
     *
     * @param scroll {number}
     *   New scrolled position.
     */
    setScroll(scroll) {
      this.componentElement.scrollTop = scroll;
    }
    /**
     * Sets scrolled position to end of log.
     */
    scrollToBottom() {
      this.setScroll(this.componentElement.scrollHeight);
    }
    /**
     * Checks if scrolled state represents end of log.
     *
     * @return
     *   `true` if recent state is less than 0 or scroll position is same as element height.
     */
    isAtBottom() {
      return this.componentElement.scrollHeight - this.componentElement.clientHeight <= this.componentElement.scrollTop + 5;
    }
    /**
     * Called when a scroll event occurs.
     */
    onScroll() {
      this.scrollStateBottom = this.isAtBottom();
    }
    /**
     * Doesn't listen for scroll events while chat panel/log is hidden.
     */
    onHide() {
      this.componentElement.removeEventListener("scroll", this.scrollListener);
    }
    /**
     * Re-activates scroll event listener when chat panel/log visibility is restored.
     */
    onUnhide() {
      if (this.scrollStateBottom) {
        this.scrollToBottom();
      }
      this.componentElement.addEventListener("scroll", this.scrollListener);
    }
    /**
     * Copies log text to clipboard or exports to file.
     *
     * @param clipboard {boolean}
     *   If `true` text is copied to clipboard.
     * @fixme
     *   File export currently not supported.
     */
    exportContents(clipboard = true) {
      if (clipboard && (!navigator || !navigator.clipboard)) {
        console.warn("copying to clipboard not supported by this browser");
        return;
      }
      const lines = [];
      if (window.getSelection) {
        const sel = window.getSelection();
        let value;
        if (sel && sel.type == "Range") {
          if (sel.rangeCount == 1) {
            value = sel.toString();
          }
        }
        if (value && value !== "") {
          lines.push(value);
        }
      }
      if (lines.length == 0) {
        const children = this.componentElement.children;
        for (let idx = 0; idx < children.length; idx++) {
          const row = children[idx];
          let text = row.children[0].innerHTML.trim() + " ";
          if (text.trim() === "") {
            text = "    ";
          }
          text = this.plainText(
            text + row.children[1].innerHTML,
            ["span", "div"]
          );
          lines.push(text.replace("&lt;", "<").replace("&gt;", ">"));
        }
      }
      if (lines.length > 0) {
        if (clipboard) {
          navigator.clipboard.writeText(lines.join("\n"));
          return;
        }
        singletons.getDownloadUtil().buildChatLog(lines.join("\n")).execute();
      }
    }
    /**
     * Removes HTML tag formatting from a string.
     *
     * @param msg {string}
     *   Message to format.
     * @param tags {string[]}
     *   List of tags to filter for removal (default: []).
     * @return {string}
     *   Plain text formatted message.
     */
    plainText(msg, tags) {
      if (!tags) {
        msg = msg.replace(/<.*?>/g, "");
      } else {
        for (const tag of tags) {
          msg = msg.replace(new RegExp("<" + tag + ".*?>", "g"), "").replace(new RegExp("</" + tag + ">", "g"), "");
        }
      }
      return msg;
    }
    /**
     * Handles creating a custom context menu with options "Clear" & "Copy".
     *
     * FIXME: text highlighting interferes with opening context menu with touch
     *
     * @param evt {any}
     *   Mouse or touch event.
     */
    onContextMenu(evt) {
      if (!evt || stendhal.ui.actionContextMenu.isOpen()) {
        return;
      }
      if (evt.type === "mouseup" && evt.button != 2) {
        return;
      }
      if (evt.type === "touchend" && !TouchHandler.get().isLongTouch(evt)) {
        evt.preventDefault();
        return;
      }
      const log = ui.get(UIComponentEnum.ChatLog);
      const options = [
        new MenuItem("Save", function() {
          log.exportContents(false);
        }),
        new MenuItem("Clear", function() {
          log.clear();
        })
      ];
      if (navigator && navigator.clipboard) {
        options.unshift(new MenuItem("Copy", function() {
          log.exportContents();
        }));
      }
      const pos = HTMLUtil.extractPosition(evt);
      stendhal.ui.actionContextMenu.set(ui.createSingletonFloatingWindow(
        "Action",
        new LogContextMenu(options),
        pos.pageX - 50,
        pos.pageY - 5
      ));
      evt.preventDefault();
      evt.stopPropagation();
    }
  };
  var LogContextMenu = class extends Component {
    /** Available options. */
    options;
    constructor(options) {
      super("contextmenu-template", true);
      this.options = options;
      let content = '<div class="actionmenu verticalgroup">';
      for (let i = 0; i < this.options.length; i++) {
        content += '<button class="actionbutton" id="actionbutton.' + i + '">' + HTMLUtil.esc(this.options[i].title) + "</button>";
      }
      content += "</div>";
      this.componentElement.innerHTML = content;
      this.componentElement.addEventListener("click", (evt) => {
        this.onClick(evt);
      });
    }
    /**
     * Handles executing commands when an option is clicked/tapped.
     */
    onClick(evt) {
      let iStr = evt.target.getAttribute("id")?.substring(13);
      if (iStr === void 0 || iStr === "") {
        return;
      }
      let i = parseInt(iStr);
      if (i < 0) {
        return;
      }
      this.componentElement.dispatchEvent(new Event("close"));
      if (i >= this.options.length) {
        throw new Error("actions index is larger than number of actions");
      }
      const action = this.options[i].action;
      if (action) {
        action();
      } else {
        console.error("chat log context menu action failed");
      }
    }
  };

  // ui/component/GroupPanelComponent.ts
  var import_marauroa80 = __toESM(require_marauroa());

  // ui/component/GroupMemberComponent.ts
  var import_marauroa79 = __toESM(require_marauroa());

  // ui/component/StatBarComponent.ts
  var StatBarComponent = class extends Component {
    canvas;
    ctx;
    constructor(id) {
      if (!id) {
        id = "statbar-template";
      }
      super(id);
      this.canvas = this.componentElement;
      this.ctx = this.canvas.getContext("2d");
    }
    drawBase() {
      this.ctx.beginPath();
      this.ctx.fillStyle = Color.GRAY;
      this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
    }
    draw(ratio) {
      this.ctx.beginPath();
      this.ctx.fillStyle = Color.BLACK;
      this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
      this.ctx.fillStyle = Color.getStatBarColor(ratio);
      this.ctx.fillRect(0, 0, this.canvas.width * ratio, this.canvas.height);
    }
  };

  // ui/component/GroupMemberComponent.ts
  var GroupMemberComponent = class extends Component {
    constructor(memberName, isUserLeader) {
      super("group-member-template");
      this.memberName = memberName;
      this.isUserLeader = isUserLeader;
      this.child(".group-member-name").innerText = memberName;
      this.componentElement.addEventListener("mouseup", (event2) => {
        this.onMouseUp(event2);
      });
      this.hpBar = new StatBarComponent();
      this.hpBar.componentElement.classList.add("group-member-hpbar");
      const player = Zone.get().findPlayer(memberName);
      if (player) {
        this.hpBar.draw(player["hp"] / player["base_hp"]);
      } else {
        this.hpBar.drawBase();
      }
      this.componentElement.appendChild(this.hpBar.componentElement);
    }
    memberName;
    isUserLeader;
    /** HP bar associated with this component. */
    hpBar;
    /**
     * Retrieves player name.
     *
     * @return {string}
     *   Name of player associated with this component.
     */
    getMemberName() {
      return this.child(".group-member-name").innerText;
    }
    buildActions(actions) {
      let playerName = this.memberName;
      actions.push({
        title: "Talk",
        action: function(_groupMemberComponent) {
          ui.get(UIComponentEnum.ChatInput).setText("/msg " + playerName + " ");
        }
      });
      actions.push({
        title: "Where",
        action: function(_groupMemberComponent) {
          let action = {
            "type": "where",
            "target": playerName,
            "zone": import_marauroa79.marauroa.currentZoneName
          };
          import_marauroa79.marauroa.clientFramework.sendAction(action);
        }
      });
      if (this.isUserLeader && (!import_marauroa79.marauroa.me || this.memberName != import_marauroa79.marauroa.me["_name"])) {
        actions.push({
          title: "Kick",
          action: function(_groupMemberComponent) {
            let action = {
              "type": "group_management",
              "action": "kick",
              "params": playerName,
              "zone": import_marauroa79.marauroa.currentZoneName
            };
            import_marauroa79.marauroa.clientFramework.sendAction(action);
          }
        });
        actions.push({
          title: "Make leader",
          action: function(_groupMemberComponent) {
            let action = {
              "type": "group_management",
              "action": "leader",
              "params": playerName,
              "zone": import_marauroa79.marauroa.currentZoneName
            };
            import_marauroa79.marauroa.clientFramework.sendAction(action);
          }
        });
      }
    }
    onMouseUp(event2) {
      stendhal.ui.actionContextMenu.set(ui.createSingletonFloatingWindow(
        "Action",
        new ActionContextMenu(this),
        Math.max(10, event2.pageX - 50),
        event2.pageY - 5
      ));
    }
    /**
     * Updates & redraws member HP bar.
     *
     * @param ratio {number}
     *   Percentage value of player's current HP.
     */
    updateHP(ratio) {
      this.hpBar.draw(ratio);
    }
    /**
     * Makes member's HP status invisible to current user.
     */
    hideStatus() {
      this.hpBar.drawBase();
    }
  };

  // ui/component/GroupPanelComponent.ts
  var GroupPanelComponent = class extends Panel {
    invites = {};
    /** Registered members for faster lookup. */
    members = /* @__PURE__ */ new Map();
    constructor() {
      super("group-panel");
      this.containerElement = this.child(".group-members");
      this.child(".group-lootmode").addEventListener("click", () => {
        this.onLootmodeClick();
      });
      this.child(".group-chat").addEventListener("click", () => {
        this.onGroupChatButtonClick();
      });
      this.child(".group-invite").addEventListener("click", () => {
        this.onGroupInviteButtonClick();
      });
      this.child(".group-part").addEventListener("click", () => {
        this.onGroupPartButtonClick();
      });
    }
    receivedInvite(leader) {
      if (this.invites[leader]) {
        return;
      }
      let button = document.createElement("button");
      button.innerText = "Join " + leader;
      button.title = "Join the group led by " + leader;
      button.addEventListener("click", () => {
        this.onJoinClicked(leader);
      });
      this.invites[leader] = button;
      this.child(".group-invites").append(button);
      Chat.log("client", "You received an invite to join a group. Please use the group panel to accept the invite.");
      ui.get(UIComponentEnum.SocialPanel).setCurrentTab(1);
    }
    onJoinClicked(leader) {
      let action = {
        "type": "group_management",
        "action": "join",
        "params": leader,
        "zone": import_marauroa80.marauroa.currentZoneName
      };
      import_marauroa80.marauroa.clientFramework.sendAction(action);
    }
    expiredInvite(leader) {
      let button = this.invites[leader];
      if (button) {
        button.remove();
        delete this.invites[leader];
      }
    }
    isInGroup() {
      return stendhal.data.group.members && Object.keys(stendhal.data.group.members).length > 0;
    }
    updateGroupStatus() {
      if (!this.isInGroup()) {
        this.child(".group-nogroup").classList.remove("hidden");
        this.child(".group-group").classList.add("hidden");
        return;
      }
      this.invites = {};
      this.child(".group-invites").innerHTML = "";
      this.child(".group-nogroup").classList.add("hidden");
      this.child(".group-group").classList.remove("hidden");
      this.child(".group-lootmode").innerText = stendhal.data.group.lootmode;
      this.child(".group-leader").innerText = stendhal.data.group.leader;
      this.renderGroupMembers();
    }
    add(child) {
      super.add(child);
      if (child instanceof GroupMemberComponent) {
        const memberComponent = child;
        this.members.set(memberComponent.getMemberName(), memberComponent);
      }
    }
    clear() {
      super.clear();
      for (const name of this.members.keys()) {
        this.members.delete(name);
      }
    }
    renderGroupMembers() {
      this.clear();
      for (let member of Object.keys(stendhal.data.group.members)) {
        this.add(new GroupMemberComponent(member, stendhal.data.group.leader === import_marauroa80.marauroa.me["_name"]));
      }
    }
    onLootmodeClick() {
      let newMode = "shared";
      if (stendhal.data.group.lootmode === "shared") {
        newMode = "single";
      }
      let action = {
        "type": "group_management",
        "action": "lootmode",
        "params": newMode,
        "zone": import_marauroa80.marauroa.currentZoneName
      };
      import_marauroa80.marauroa.clientFramework.sendAction(action);
    }
    onGroupChatButtonClick() {
      if (!this.isInGroup()) {
        Chat.log("error", "Please invite someone into a group before trying to send group messages.");
        return;
      }
      ui.get(UIComponentEnum.ChatInput).setText("/p ");
    }
    onGroupInviteButtonClick() {
      if (this.isInGroup() && stendhal.data.group.leader !== import_marauroa80.marauroa.me["_name"]) {
        Chat.log("error", "Only the leader may invite people into the group.");
        return;
      }
      Chat.log("client", "Please Fill in the name of the player you want to invite");
      ui.get(UIComponentEnum.ChatInput).setText("/group invite ");
    }
    onGroupPartButtonClick() {
      if (!this.isInGroup()) {
        Chat.log("error", "You cannot leave a group because your are not a member of a group");
        return;
      }
      const action = {
        "type": "group_management",
        "action": "part",
        "params": "",
        "zone": import_marauroa80.marauroa.currentZoneName
      };
      import_marauroa80.marauroa.clientFramework.sendAction(action);
    }
    /**
     * Retrieves membership component.
     *
     * @param name {string}
     *   Name of player.
     * @return {ui.component.GroupMemberComponent.GroupMemberComponent}
     *   The component associated with player or `undefined`.
     */
    getMemberComponent(name) {
      if (!this.isInGroup()) {
        return void 0;
      }
      for (const entry of this.members.entries()) {
        if (entry[0] === name) {
          return entry[1];
        }
      }
    }
  };

  // ui/component/KeyringComponent.ts
  var import_marauroa81 = __toESM(require_marauroa());
  var KeyringComponent = class extends ItemInventoryComponent {
    slotSize = "2 4";
    constructor(object, slot, sizeX, sizeY, quickPickup, defaultImage) {
      super(object, slot, sizeX, sizeY, quickPickup, defaultImage);
    }
    update() {
      let features = null;
      if (import_marauroa81.marauroa.me != null) {
        features = import_marauroa81.marauroa.me["features"];
      }
      if (features == null) {
        return;
      }
      let size = features["keyring"];
      if (size) {
        if (!this.isVisible()) {
          this.setVisible(true);
        }
        if (this.slotSize != size) {
          this.slotSize = size;
          let sizeArray = size.split(" ");
          let sizeX = parseInt(sizeArray[0], 10);
          let sizeY = parseInt(sizeArray[1], 10);
          this.resize(sizeX, sizeY);
        }
      } else if (this.isVisible()) {
        this.setVisible(false);
      }
      super.update();
    }
    resize(sizeX, sizeY) {
      super.setSize(sizeX, sizeY);
      this.itemContainerImplementation.init(sizeX * sizeY);
    }
  };

  // ui/component/MiniMapComponent.ts
  var import_marauroa82 = __toESM(require_marauroa());
  var MiniMapComponent = class _MiniMapComponent extends Component {
    map;
    width = 128;
    height = 128;
    minimumScale = 2;
    xOffset = 1;
    yOffset = 1;
    mapWidth = 1;
    mapHeight = 1;
    scale = 1;
    bgImage;
    lastZone;
    // ground/collision colors
    static COLOR_COLLISION = Color.parseRGB(Color.COLLISION);
    // red
    static COLOR_GROUND = Color.parseRGB(Color.BACKGROUND);
    // light gray
    static COLOR_PROTECTION = Color.parseRGB(Color.PROTECTION);
    // green
    constructor() {
      super("minimap");
      this.map = TileMap.get();
      this.componentElement.addEventListener("click", (event2) => {
        this.onClick(event2);
      });
      this.componentElement.addEventListener("dblclick", (event2) => {
        this.onClick(event2);
      });
    }
    zoneChange() {
      this.mapWidth = this.map.zoneSizeX;
      this.mapHeight = this.map.zoneSizeY;
      this.scale = Math.max(this.minimumScale, Math.min(this.height / this.mapHeight, this.width / this.mapWidth));
      this.createBackgroundImage();
    }
    updateBasePosition() {
      if (!import_marauroa82.marauroa.me) {
        return;
      }
      this.xOffset = 0;
      this.yOffset = 0;
      let imageWidth = this.mapWidth * this.scale;
      let imageHeight = this.mapHeight * this.scale;
      let xpos = Math.round(import_marauroa82.marauroa.me["x"] * this.scale + 0.5) - this.width / 2;
      let ypos = Math.round(import_marauroa82.marauroa.me["y"] * this.scale + 0.5) - this.width / 2;
      if (imageWidth > this.width) {
        if (xpos + this.width > imageWidth) {
          this.xOffset = imageWidth - this.width;
        } else if (xpos > 0) {
          this.xOffset = xpos;
        }
      }
      if (imageHeight > this.height) {
        if (ypos + this.height > imageHeight) {
          this.yOffset = imageHeight - this.height;
        } else if (ypos > 0) {
          this.yOffset = ypos;
        }
      }
    }
    draw() {
      if (import_marauroa82.marauroa.currentZoneName === this.map.currentZoneName || this.map.currentZoneName === "int_vault" || this.map.currentZoneName === "int_adventure_island" || this.map.currentZoneName === "tutorial_island") {
        this.scale = 10;
        this.zoneChange();
        this.updateBasePosition();
        let ctx = this.componentElement.getContext("2d");
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.fillStyle = Color.DARK_GRAY;
        ctx.fillRect(0, 0, this.width, this.height);
        ctx.translate(Math.round(-this.xOffset), Math.round(-this.yOffset));
        this.drawBackground(ctx);
        this.drawEntities(ctx);
      }
    }
    drawBackground(ctx) {
      ctx.save();
      ctx.imageSmoothingEnabled = false;
      ctx.scale(this.scale, this.scale);
      if (this.bgImage) {
        ctx.drawImage(this.bgImage, 0, 0);
      }
      ctx.restore();
    }
    createBackgroundImage() {
      let width = this.mapWidth;
      let height = this.mapHeight;
      if (width <= 0 || height <= 0) {
        return;
      }
      if (this.map.collisionData !== this.lastZone) {
        this.lastZone = this.map.collisionData;
        let canvas = new OffscreenCanvas(width, height);
        let ctx = canvas.getContext("2d");
        let imgData = ctx.createImageData(width, height);
        for (let y = 0; y < height; y++) {
          for (let x = 0; x < width; x++) {
            let color = _MiniMapComponent.COLOR_GROUND;
            let pos = 4 * (y * width + x);
            if (this.map.collision(x, y)) {
              color = _MiniMapComponent.COLOR_COLLISION;
            } else if (this.map.isProtected(x, y)) {
              color = _MiniMapComponent.COLOR_PROTECTION;
            }
            imgData.data[pos] = color.R;
            imgData.data[pos + 1] = color.G;
            imgData.data[pos + 2] = color.B;
            imgData.data[pos + 3] = 255;
          }
        }
        ctx.putImageData(imgData, 0, 0);
        if (this.bgImage) {
          this.bgImage.close();
        }
        this.bgImage = canvas.transferToImageBitmap();
      }
    }
    drawEntities(ctx) {
      ctx.fillStyle = Color.RED;
      ctx.strokeStyle = Color.BLACK;
      let isAdmin = import_marauroa82.marauroa.me["adminlevel"] && import_marauroa82.marauroa.me["adminlevel"] >= 600;
      for (let i in import_marauroa82.marauroa.currentZone) {
        let o = import_marauroa82.marauroa.currentZone[i];
        if (typeof o["x"] != "undefined" && typeof o["y"] != "undefined" && (o.minimapShow || isAdmin)) {
          o.onMiniMapDraw();
          if (o.minimapStyle) {
            ctx.strokeStyle = o.minimapStyle;
          } else {
            ctx.strokeStyle = Color.GRAY;
          }
          if (o instanceof Player) {
            let adj_scale = this.scale;
            if (adj_scale < 6) {
              adj_scale = 6;
            }
            let ho = o["width"] * adj_scale / 2;
            let vo = o["height"] * adj_scale / 2;
            const hc = o["x"] * this.scale + ho;
            const vc = o["y"] * this.scale + vo;
            ctx.beginPath();
            ctx.moveTo(hc - ho, vc);
            ctx.lineTo(hc + ho, vc);
            ctx.moveTo(hc, vc - vo);
            ctx.lineTo(hc, vc + vo);
            ctx.stroke();
            ctx.closePath();
          } else {
            ctx.strokeRect(o["x"] * this.scale, o["y"] * this.scale, o["width"] * this.scale, o["height"] * this.scale);
          }
        }
      }
    }
    onClick(event2) {
      if (!stendhal.config.getBoolean("pathfinding.minimap")) {
        return;
      }
      let pos = HTMLUtil.extractPosition(event2);
      let x = Math.floor((pos.canvasRelativeX + this.xOffset) / this.scale);
      let y = Math.floor((pos.canvasRelativeY + this.yOffset) / this.scale);
      if (!this.map.collision(x, y)) {
        let action = {
          type: "moveto",
          x: x.toString(),
          y: y.toString()
        };
        if ("type" in event2 && event2["type"] === "dblclick") {
          action["double_click"] = "";
        }
        import_marauroa82.marauroa.me.moveTo(action);
      }
    }
  };

  // ui/component/ZoneInfoComponent.ts
  var import_marauroa83 = __toESM(require_marauroa());
  var ZoneInfoComponent = class extends Component {
    DANGER_LEVEL_DESCRIPTIONS = [
      "The area feels safe.",
      "The area feels relatively safe.",
      "The area feels somewhat dangerous.",
      "The area feels dangerous.",
      "The area feels very dangerous!",
      "The area feels extremely dangerous. Run away!"
    ];
    constructor() {
      super("zoneinfo");
    }
    zoneChange(zoneinfo) {
      document.getElementById("zonename").textContent = zoneinfo["readable_name"];
      if (import_marauroa83.marauroa.me) {
        let dangerLevel = Number.parseFloat(zoneinfo["danger_level"]);
        let skulls = Math.min(5, Math.round(2 * dangerLevel / (Number.parseInt(import_marauroa83.marauroa.me.level, 10) + 3)));
        let div = document.getElementById("skulls");
        if (skulls === 0) {
          div.style.height = "0";
        } else {
          div.style.height = "16px";
        }
        div.style.width = skulls * 20 + "px";
        this.componentElement.title = this.DANGER_LEVEL_DESCRIPTIONS[skulls];
      }
    }
  };

  // ui/component/PlayerEquipmentComponent.ts
  var import_marauroa84 = __toESM(require_marauroa());
  var PlayerEquipmentComponent = class extends Component {
    slotNames = ["head", "lhand", "rhand", "finger", "armor", "cloak", "legs", "feet", "pouch"];
    slotSizes = [1, 1, 1, 1, 1, 1, 1, 1, 1];
    slotImages = [
      "slot-helmet.png",
      "slot-shield.png",
      "slot-weapon.png",
      "slot-ring.png",
      "slot-armor.png",
      "slot-cloak.png",
      "slot-legs.png",
      "slot-boots.png",
      "slot-pouch.png"
    ];
    inventory = [];
    pouchVisible = false;
    constructor() {
      super("equipment");
      this.inventory = [];
      for (var i in this.slotNames) {
        this.inventory.push(
          new ItemContainerImplementation(
            document,
            this.slotNames[i],
            this.slotSizes[i],
            null,
            "",
            false,
            this.slotImages[i]
          )
        );
      }
      this.showPouch(false);
    }
    update() {
      for (var i in this.inventory) {
        this.inventory[i].update();
      }
      if (!this.pouchVisible) {
        var features = null;
        if (import_marauroa84.marauroa.me != null) {
          features = import_marauroa84.marauroa.me["features"];
        }
        if (features != null) {
          if (features["pouch"] != null) {
            this.showPouch(true);
          }
        }
      }
    }
    markDirty() {
      for (const inv of this.inventory) {
        inv.markDirty();
      }
    }
    showSlot(id, show) {
      var slot = document.getElementById(id);
      var prevState = slot.style.display;
      if (show === true) {
        slot.style.display = "block";
      } else {
        slot.style.display = "none";
      }
      return prevState != slot.style.display;
    }
    showPouch(show) {
      if (this.showSlot("pouch0", show)) {
        var equip = document.getElementById("equipment");
        if (show) {
          equip.style.height = "200px";
        } else {
          equip.style.height = "160px";
        }
        this.pouchVisible = show;
      }
    }
  };

  // ui/component/StatusesListComponent.ts
  var StatusesListComponent = class _StatusesListComponent extends Component {
    static iconPaths = {
      "choking": "/ideas/choking.png",
      "eating": "/ideas/eat.png"
    };
    // list of active statuse IDs
    active;
    constructor() {
      super("statuses-list");
      this.active = [];
    }
    update(user) {
      const changes = [];
      for (const key of Object.keys(user)) {
        if (Object.keys(_StatusesListComponent.iconPaths).indexOf(key) !== -1) {
          changes.push(key);
        } else if (key === "poisoned") {
          changes.push("poison");
        } else if (key.startsWith("status_")) {
          const id = key.substring(key.indexOf("_") + 1);
          if (id !== "") {
            changes.push(id);
          }
        }
      }
      if (this.changed(changes)) {
        this.active = changes;
        this.updateInternal();
      }
    }
    updateInternal() {
      for (const icon of Array.from(this.componentElement.children)) {
        this.componentElement.removeChild(icon);
      }
      const choking = this.active.indexOf("choking") !== -1;
      for (const id of this.active) {
        if (id === "eating" && choking) {
          continue;
        }
        let iconPath;
        if (Object.keys(_StatusesListComponent.iconPaths).indexOf(id) !== -1) {
          iconPath = Paths.sprites + _StatusesListComponent.iconPaths[id];
        } else {
          iconPath = Paths.sprites + "/status/panel/" + id + ".png";
        }
        const icon = htmlImageStore.get(iconPath);
        icon.className = "status-icon";
        this.componentElement.appendChild(icon);
      }
    }
    changed(changes) {
      if (changes.length != this.active.length) {
        return true;
      }
      for (let idx = 0; idx < changes.length; idx++) {
        if (changes[idx] !== this.active[idx]) {
          return true;
        }
      }
      return false;
    }
  };

  // ui/component/KarmaBarComponent.ts
  var KarmaBarComponent = class extends StatBarComponent {
    karma = 0;
    lastChange = 0;
    // value represented in bar
    repValue = 0.5;
    // prevents bar from lighting up when first created
    initialized = false;
    constructor() {
      super("karmabar");
    }
    draw(newKarma) {
      const cycleTime = Date.now();
      if (this.initialized) {
        if (newKarma != this.karma) {
          this.canvas.style.setProperty("outline", "1px solid #ffffff");
          this.karma = newKarma;
          this.canvas.title = this.describeKarma(newKarma);
          this.calculateRepresentation(newKarma);
          this.lastChange = cycleTime;
        } else if (cycleTime - this.lastChange >= 1e3) {
          this.canvas.style.setProperty("outline", "1px solid #000000");
        }
      } else {
        this.karma = newKarma;
        this.canvas.title = this.describeKarma(newKarma);
        this.calculateRepresentation(newKarma);
        this.initialized = true;
      }
      this.ctx.beginPath();
      this.ctx.fillStyle = "#000000";
      this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
      const grad = this.ctx.createLinearGradient(0, 0, this.canvas.width, 0);
      grad.addColorStop(0, "#ff0000");
      grad.addColorStop(0.5, "#ffffff");
      grad.addColorStop(1, "#0000ff");
      this.ctx.fillStyle = grad;
      this.ctx.fillRect(0, 0, this.repValue, this.canvas.height);
    }
    /**
     * Get textual description of karma value.
     *
     * @param karma
     *     karma value.
     * @return
     *     Karma description.
     */
    describeKarma(karma) {
      if (karma > 499) {
        return "You have unusually good karma";
      } else if (karma > 99) {
        return "You have great karma";
      } else if (karma > 5) {
        return "You have good karma";
      } else if (karma > -5) {
        return "You have average karma";
      } else if (karma > -99) {
        return "You have bad karma";
      } else if (karma > -499) {
        return "You have terrible karma";
      }
      return "You have disastrously bad karma";
    }
    /**
     * Calculates the value to be represented in bar.
     */
    calculateRepresentation(karma) {
      const normalized = 0.5 + Math.atan(0.02 * karma) / Math.PI;
      this.repValue = this.canvas.width * Math.max(Math.min(normalized, 1), 0);
    }
  };

  // ui/component/PlayerStatsComponent.ts
  var import_marauroa85 = __toESM(require_marauroa());
  var PlayerStatsComponent = class extends Component {
    keys = ["hp", "base_hp", "atk", "atk_item", "atk_xp", "def", "def_item", "def_xp", "xp", "level"];
    LEVELS = 598;
    MONEY_SLOTS = ["pouch", "bag", "lhand", "rhand"];
    xp;
    hpText;
    otherText;
    bars = {};
    constructor() {
      super("stats");
      this.xp = [this.LEVELS + 1];
      this.xp[0] = 0;
      this.xp[1] = 50;
      this.xp[2] = 100;
      this.xp[3] = 200;
      this.xp[4] = 400;
      this.xp[5] = 800;
      for (let i = 5; i < this.LEVELS; i++) {
        const exp = Math.floor((i * 16 + i * i * 5 + i * i * i * 10 + 300) / 100) * 100;
        this.xp[i + 1] = exp;
      }
      const statuses = new StatusesListComponent();
      ui.registerComponent(UIComponentEnum.StatusesList, statuses);
      this.enableCharName(stendhal.config.getBoolean("panel.stats.charname"));
      this.hpText = this.child("#hptext");
      this.otherText = this.child("#otherstats");
      this.bars["karma"] = new KarmaBarComponent();
      this.enableBar("karma", false);
      this.bars["hp"] = new StatBarComponent("hpbar");
      this.enableBar("hp", singletons.getConfigManager().getBoolean("panel.stats.hpbar"));
    }
    update(key) {
      if (this.keys.indexOf(key) < -1) {
        return;
      }
      const object = import_marauroa85.marauroa.me;
      this.updateKarma(object["karma"]);
      this.updateHp(object["hp"], object["base_hp"]);
      this.updateOther(object);
      if (!this.isBarEnabled("karma") && object) {
        const features = object["features"];
        if (features && features["karma_indicator"] != null) {
          this.enableBar("karma");
        }
      }
    }
    /**
     * Updates karma bar.
     *
     * @param karma
     *     New karma value.
     */
    updateKarma(karma) {
      if (this.isBarEnabled("karma")) {
        this.bars["karma"].draw(karma);
      }
    }
    /**
     * Updates HP value & draws bar.
     *
     * @param hp
     *     Player's actual HP.
     * @param base_hp
     *     Player's potential max HP.
     */
    updateHp(hp, base_hp) {
      this.hpText.innerText = "HP: " + hp + " / " + base_hp;
      if (this.isBarEnabled("hp")) {
        this.bars["hp"].draw(hp / base_hp);
      }
    }
    /**
     * Updates all other stat values.
     *
     * @param object
     *     Owner of stats.
     */
    updateOther(object) {
      const atk = object["atk"];
      const atkXP = object["atk_xp"];
      const def = object["def"];
      const defXP = object["def_xp"];
      const atkTNL = this.getAtkDefTNL(atk, atkXP);
      const defTNL = this.getAtkDefTNL(def, defXP);
      const lvl = parseInt(object["level"], 10);
      const xp = object["xp"];
      let xpTNL = lvl < this.getMaxLevel() ? this.getTNL(lvl, xp) : "-";
      this.otherText.innerText = "ATK: " + atk + " x " + object["atk_item"] + "\r\n  (" + atkTNL + ")\r\nDEF: " + def + " x " + object["def_item"] + "\r\n  (" + defTNL + ")\r\nXP: " + xp + "\r\nLevel: " + lvl + "\r\n  (" + xpTNL + ")\r\nMoney: " + this.calculateMoney();
    }
    /**
     * Retrieves amount of experience required to reach a level.
     *
     * @param lvl
     *     Desired level.
     * @param xp
     *     Current XP.
     * @return
     *     Remaining XP.
     */
    getTNL(lvl, xp) {
      return this.getReqXP(lvl + 1) - xp;
    }
    /**
     * Retrieves amount of experience required to reach an ATK/DEF level.
     *
     * @param lvl
     *     Desired level.
     * @param xp
     *     Current XP.
     * @return
     *     Remaining XP.
     */
    getAtkDefTNL(lvl, xp) {
      return this.getReqXP(lvl - 9) - xp;
    }
    /**
     * Retrieves the amount of XP required to reach a level.
     *
     * @param lvl
     *     The respective level.
     * @return
     *     Amount of expericence.
     */
    getReqXP(lvl) {
      if (lvl >= 0 && lvl < this.xp.length) {
        return this.xp[lvl];
      }
      return -1;
    }
    /**
     * Retrieves highest possible player level.
     */
    getMaxLevel() {
      return this.LEVELS - 1;
    }
    /**
     * Retrieves amount of money being carried by player.
     *
     * @return
     *     Total money from relevant slots.
     */
    calculateMoney() {
      let mo = 0;
      if (import_marauroa85.marauroa.me) {
        for (const sname of this.MONEY_SLOTS) {
          const slot = import_marauroa85.marauroa.me[sname];
          if (slot) {
            for (let idx = 0; idx < slot.count(); idx++) {
              const o = slot.getByIndex(idx);
              if (o instanceof Item) {
                const i = o;
                if (i["name"] === "money") {
                  mo += parseInt(i["quantity"]);
                }
              }
            }
          }
        }
      }
      return mo;
    }
    /**
     * Sets visibility of character name in status panel.
     */
    enableCharName(visible = true) {
      const charname = document.getElementById("charname");
      if (visible) {
        charname.style["display"] = "block";
      } else {
        charname.style["display"] = "none";
      }
    }
    /**
     * Enables or disables drawing of stat bars.
     *
     * @param
     *     Bar identifier string.
     * @param visible
     *     If true, bar will be drawn.
     */
    enableBar(id, visible = true) {
      const bar = this.bars[id];
      if (bar) {
        bar.setVisible(visible);
      }
    }
    isBarEnabled(id) {
      const bar = this.bars[id];
      if (bar) {
        return bar.isVisible();
      }
      return false;
    }
  };

  // ui/factory/DesktopUserInterfaceFactory.ts
  var DesktopUserInterfaceFactory = class {
    /** Property to help workaround issue with group event not always received at login. */
    checkedGroupAfterLogin = false;
    create() {
      let topPanel = new Panel("topPanel");
      ui.registerComponent(UIComponentEnum.LeftPanel, topPanel);
      let leftPanel = new Panel("leftColumn");
      ui.registerComponent(UIComponentEnum.LeftPanel, leftPanel);
      let socialPanel = new TabPanelComponent(2 /* BOTTOM */);
      ui.registerComponent(UIComponentEnum.SocialPanel, socialPanel);
      this.add(leftPanel, UIComponentEnum.MiniMap, new MiniMapComponent());
      this.add(leftPanel, UIComponentEnum.ZoneInfo, new ZoneInfoComponent());
      this.add(leftPanel, UIComponentEnum.PlayerStats, new PlayerStatsComponent());
      leftPanel.add(socialPanel);
      this.add(socialPanel, UIComponentEnum.BuddyList, new BuddyListComponent());
      this.add(socialPanel, UIComponentEnum.GroupPanel, new GroupPanelComponent());
      socialPanel.addTab("Friends");
      socialPanel.addTab("Group");
      const that = this;
      socialPanel.onTabChanged = function() {
        if (this.getCurrentIndex() == 1) {
          if (that.checkedGroupAfterLogin) {
            return;
          }
          that.checkedGroupAfterLogin = true;
          if (stendhal.data.group.getMemberCount() == 0) {
            console.debug("group not recognized after login, contacting server for refresh ...");
            stendhal.data.group.refresh();
          }
        }
      };
      socialPanel.onTabChanged();
      let rightPanel = new Panel("rightColumn");
      ui.registerComponent(UIComponentEnum.RightPanel, rightPanel);
      this.add(rightPanel, UIComponentEnum.PlayerEquipment, new PlayerEquipmentComponent());
      this.add(
        rightPanel,
        UIComponentEnum.Bag,
        new BagComponent(void 0, "bag", 3, 4, false, void 0)
      );
      const keyring = new KeyringComponent(void 0, "keyring", 2, 4, false, "slot-key.png");
      keyring.setVisible(false);
      this.add(rightPanel, UIComponentEnum.Keyring, keyring);
      const bottomPanel = new ChatPanel();
      ui.registerComponent(UIComponentEnum.BottomPanel, bottomPanel);
      this.add(bottomPanel, UIComponentEnum.ChatInput, new ChatInputComponent());
      this.add(bottomPanel, UIComponentEnum.ChatLog, new ChatLogComponent());
    }
    add(panel, uiComponentEnum, component) {
      panel.add(component);
      ui.registerComponent(uiComponentEnum, component);
    }
  };

  // util/DialogHandler.ts
  var DialogHandler = class {
    content;
    /**
     * Sets the dialog window content.
     *
     * @param c
     *     The FloatingWindow instance to be set.
     */
    set(c) {
      if (this.isOpen()) {
        this.close();
      }
      this.content = c;
      this.content.componentElement.addEventListener("mousedown", function(e) {
        e.preventDefault();
        e.stopPropagation();
      });
    }
    /**
     * Sets the content to <code>null</code>.
     */
    unset() {
      this.content = void 0;
    }
    /**
     * Retrieves the window.
     *
     * @return
     *     FloatingWindow instance or <code>null</code> if not set.
     */
    get() {
      return this.content;
    }
    /**
     * Checks if the action context menu is open.
     *
     * @return
     *     The FloatingWindow open state.
     */
    isOpen() {
      return typeof this.content !== "undefined" && this.content.isOpen();
    }
    /**
     * Closes the dialog window.
     *
     * @param unset
     *     If <code>true</code>, resets content to <code>null</code>.
     */
    close(unset = false) {
      if (!this.content) {
        return;
      }
      this.content.close();
      if (unset) {
        this.unset();
      }
    }
  };

  // Client.ts
  var Client = class _Client {
    /** Property set to prevent re-initialization. */
    initialized = false;
    errorCounter = 0;
    unloading = false;
    /** User's character name.
     *
     * NOTE: can we replace references to this with value now stored in `util.SessionManager`?
     */
    username;
    /** ID for vetoing click indicator timeout (experimental setting not enabled/visible by default). */
    static click_indicator_id = void 0;
    /** Singleton instance. */
    static instance;
    loaded = false;
    /**
     * Retrieves singleton instance.
     */
    static get() {
      if (!_Client.instance) {
        _Client.instance = new _Client();
      }
      return _Client.instance;
    }
    /**
     * Hidden singleton constructor.
     */
    constructor() {
    }
    /**
     * Initializations to be called before main startup calls.
     */
    init() {
      if (this.initialized) {
        console.warn("tried to re-initialize client");
        return;
      }
      this.initialized = true;
      stendhal.config = singletons.getConfigManager();
      stendhal.session = singletons.getSessionManager();
      stendhal.actions = singletons.getSlashActionRepo();
      new EntityRegistry().init();
      this.initData();
      this.initSound();
      this.initUI();
      document.documentElement.setAttribute("data-build-version", stendhal.data.build?.version);
      document.documentElement.setAttribute("data-build-build", stendhal.data.build?.build);
    }
    /**
     * Initializes sprite resources & other data management.
     */
    initData() {
      stendhal.data = stendhal.data || {};
      stendhal.data.cache = singletons.getCacheManager();
      stendhal.data.cache.init();
      stendhal.data.cstatus = singletons.getCStatus();
      stendhal.data.cstatus.init();
      stendhal.data.group = singletons.getGroupManager();
      stendhal.players = [];
    }
    /**
     * Initializes sound manager.
     *
     * Should be called after config is initialized.
     */
    initSound() {
      stendhal.sound = singletons.getSoundManager();
      stendhal.sound.onConfigUpdate();
    }
    /**
     * Initializes GUI elements, input management, sound management, & other interface tools.
     */
    initUI() {
      stendhal.ui = stendhal.ui || {};
      stendhal.ui.equip = singletons.getInventory();
      stendhal.ui.getMenuStyle = Globals.getMenuStyle;
    }
    /**
     * Main startup routines.
     */
    startup() {
      this.devWarning();
      const sparams = new URL(document.URL).searchParams;
      stendhal.config.init(sparams);
      stendhal.session.init(sparams);
      stendhal.config.refreshTheme();
      document.getElementById("body").style.setProperty("font-family", stendhal.config.get("font.body"));
      singletons.getEventRegistry().init();
      TileStore.get().init();
      singletons.getEmojiStore().init();
      OutfitStore.get().init();
      new DesktopUserInterfaceFactory().create();
      Chat.log("client", "Client loaded. Connecting...");
      this.registerMarauroaEventHandlers();
      this.registerBrowserEventHandlers();
      let ws = Paths.ws.substring(1);
      if (stendhal.session.isTestClient() && !stendhal.session.isServerDefault()) {
        ws = ws.replace(/t/, "s");
        Chat.log("warning", "WARNING: You are connecting to the production server with a development build of the test client which may contain bugs or not function as intented. Proceeed with caution.");
      }
      import_marauroa86.marauroa.clientFramework.connect(null, null, ws);
      stendhal.ui.actionContextMenu = new DialogHandler();
      stendhal.ui.globalInternalWindow = new DialogHandler();
      htmlImageStore.startupCache();
      stendhal.sound.startupCache();
      if (document.getElementById("viewport")) {
        ViewPort.get().draw();
      }
    }
    /**
     * Prints standard warning message to development tools console.
     */
    devWarning() {
      console.log("%c ", "padding: 30px; background: url(" + window.location.protocol + "://" + window.location.host + "/images/buttons/devtools-warning.png) no-repeat; color: #AF0");
      console.log("%cIf someone told you, to copy and paste something here, it's a scam and will give them access to your account.", "color:#A00; background-color:#FFF; font-size:150%");
      console.log("If you are a developer and curious about Stendhal, have a look at https://stendhalgame.org/development/introduction.html to get the source code. And perhaps, contribute a feature or a bugfix. ");
      console.log(" ");
      console.log(" ");
      window["eval"] = function() {
      };
    }
    /**
     * Reports errors emitted by web client to server.
     */
    onError(error) {
      this.errorCounter++;
      if (this.errorCounter > 5) {
        console.log("Too many errors, stopped reporting");
        return;
      }
      var text = error.message + "\r\n";
      text += error.filename + ":" + error.lineno;
      if (error.colno) {
        text += ":" + error.colno;
      }
      if (error.error) {
        text += "\r\n" + error.error.stack;
      }
      text += "\r\n" + window.navigator.userAgent;
      try {
        console.log(text);
        var action = {
          "type": "report_error",
          "text": text
        };
        import_marauroa86.marauroa.clientFramework.sendAction(action);
      } catch (e) {
      }
      return true;
    }
    /**
     * Registers Marauroa event handlers.
     */
    registerMarauroaEventHandlers() {
      import_marauroa86.marauroa.clientFramework.onDisconnect = function(_reason, _code, _wasClean) {
        if (!_Client.instance.unloading) {
          Chat.logH("error", "Disconnected from server.");
          if (window.location.hostname !== "localhost" && window.location.hostname !== "127.0.0.1" && window.location.hostname !== "::1") {
            window.location.href = "/account/mycharacters.html";
          }
        }
      }.bind(this);
      import_marauroa86.marauroa.clientFramework.onLoginRequired = function(config2) {
        if (config2["client_login_url"]) {
          _Client.instance.unloading = true;
          let currentUrl = encodeURI(window.location.pathname + window.location.hash);
          let url = config2["client_login_url"].replace("[url]", currentUrl);
          window.location.href = url;
          return;
        }
        let body = document.getElementById("body");
        body.style.cursor = "auto";
        document.getElementById("loginpopup").style.display = "none";
        ui.createSingletonFloatingWindow(
          "Login",
          new LoginDialog(),
          100,
          50
        ).enableCloseButton(false);
      }.bind(this);
      import_marauroa86.marauroa.clientFramework.onCreateAccountAck = function(username) {
        alert("Account succesfully created, please login.");
        window.location.reload();
      }.bind(this);
      import_marauroa86.marauroa.clientFramework.onCreateCharacterAck = function(charname, _template) {
      }.bind(this);
      import_marauroa86.marauroa.clientFramework.onLoginFailed = function(_reason, _text) {
        alert("Login failed. " + _text);
        window.location.reload();
      }.bind(this);
      import_marauroa86.marauroa.clientFramework.onAvailableCharacterDetails = (characters) => {
        SingletonFloatingWindow.closeAll();
        if (!Object.keys(characters).length && this.username) {
          import_marauroa86.marauroa.clientFramework.createCharacter(this.username, {});
          return;
        }
        if (window.location.hash) {
          let name2 = window.location.hash.substring(1);
          stendhal.session.setCharName(name2);
        }
        let name = stendhal.session.getCharName();
        if (name) {
          _Client.get().chooseCharacter(name);
          return;
        }
        let body = document.getElementById("body");
        body.style.cursor = "auto";
        document.getElementById("loginpopup").style.display = "none";
        ui.createSingletonFloatingWindow(
          "Choose Character",
          new ChooseCharacterDialog(characters),
          100,
          50
        ).enableCloseButton(false);
      };
      import_marauroa86.marauroa.clientFramework.onTransferREQ = function(items) {
        for (var i in items) {
          if (typeof items[i]["name"] == "undefined") {
            continue;
          }
          items[i]["ack"] = true;
        }
      }.bind(this);
      import_marauroa86.marauroa.clientFramework.onTransfer = (items) => {
        var data = {};
        var zoneName = "";
        for (var i in items) {
          var name = items[i]["name"];
          zoneName = name.substring(0, name.indexOf("."));
          name = name.substring(name.indexOf(".") + 1);
          data[name] = items[i]["data"];
          if (name === "data_map") {
            this.onDataMap(items[i]["data"]);
          }
        }
        TileMap.get().onTransfer(zoneName, data);
      };
      if (document.getElementById("viewport")) {
        import_marauroa86.marauroa.perceptionListener = new StendhalPerceptionListener();
      }
    }
    /**
     * Creates a character selection dialog window.
     */
    chooseCharacter(name) {
      stendhal.session.setCharName(name);
      import_marauroa86.marauroa.clientFramework.chooseCharacter(name);
      Chat.log("client", "Loading world...");
      stendhal.sound.playGlobalizedEffect("ui/login");
    }
    /**
     * Sets the clients unloading state property.
     */
    onBeforeUnload() {
      _Client.instance.unloading = true;
    }
    /**
     * Registers global browser event handlers.
     */
    registerBrowserEventHandlers() {
      const keyHandler = singletons.getKeyHandler();
      document.addEventListener("keydown", keyHandler.onKeyDown);
      document.addEventListener("keyup", keyHandler.onKeyUp);
      document.addEventListener("contextmenu", stendhal.main.preventContextMenu);
      document.addEventListener("mousedown", function(e) {
        if (stendhal.ui.actionContextMenu.isOpen()) {
          stendhal.ui.actionContextMenu.close(true);
          e.preventDefault();
          e.stopPropagation();
        }
      });
      window.addEventListener("beforeunload", () => {
        this.onBeforeUnload();
      });
      document.getElementById("body").addEventListener("mouseenter", stendhal.main.onMouseEnter);
      let gamewindow = document.getElementById("viewport");
      let viewPort = ViewPort.get();
      gamewindow.setAttribute("draggable", "true");
      gamewindow.addEventListener("mousedown", viewPort.onMouseDown);
      gamewindow.addEventListener("dblclick", viewPort.onMouseDown);
      gamewindow.addEventListener("dragstart", viewPort.onDragStart);
      gamewindow.addEventListener("mousemove", viewPort.onMouseMove);
      gamewindow.addEventListener("touchstart", viewPort.onMouseDown, { passive: true });
      gamewindow.addEventListener("touchend", viewPort.onTouchEnd);
      gamewindow.addEventListener("dragover", viewPort.onDragOver);
      gamewindow.addEventListener("drop", viewPort.onDrop);
      gamewindow.addEventListener("contextmenu", viewPort.onContentMenu);
      gamewindow.addEventListener("wheel", viewPort.onMouseWheel, { passive: true });
      singletons.getJoystickController().registerGlobalEventHandlers();
      const menubutton = document.getElementById("menubutton");
      menubutton.addEventListener("click", function(e) {
        ui.showApplicationMenu();
      });
      const soundButton = document.getElementById("soundbutton");
      soundButton.addEventListener("click", function(e) {
        stendhal.sound.toggleSound();
      });
      const click_indicator = document.getElementById("click-indicator");
      click_indicator.onload = () => {
        click_indicator.onload = null;
        document.addEventListener("click", _Client.handleClickIndicator);
        document.addEventListener("touchend", _Client.handleClickIndicator);
      };
      click_indicator.src = Paths.gui + "/click_indicator.png";
    }
    /**
     * Reads zone's map data.
     *
     * @param data {any}
     *   Information about map.
     */
    onDataMap(data) {
      let map = TileMap.get();
      let viewPort = ViewPort.get();
      var zoneinfo = {};
      var deserializer = import_marauroa86.Deserializer.fromBase64(data);
      deserializer.readAttributes(zoneinfo);
      ui.get(UIComponentEnum.ZoneInfo).zoneChange(zoneinfo);
      const musicVolume = parseFloat(zoneinfo["music_volume"]);
      stendhal.sound.playSingleGlobalizedMusic(
        zoneinfo["music"],
        !Number.isNaN(musicVolume) ? musicVolume : 1
      );
      if (stendhal.config.getBoolean("effect.parallax")) {
        map.setParallax(zoneinfo["parallax"]);
        map.setIgnoredTiles(zoneinfo["parallax_ignore_tiles"]);
      }
      if (zoneinfo["color"] && stendhal.config.getBoolean("effect.lighting")) {
        if (zoneinfo["color_method"]) {
          viewPort.setColorMethod(zoneinfo["color_method"]);
        }
        if (zoneinfo["blend_method"]) {
          viewPort.setBlendMethod(zoneinfo["blend_method"]);
        }
        const hsl = Color.numToHSL(Number(zoneinfo["color"]));
        viewPort.HSLFilter = hsl.toString();
        viewPort.filter = "hue-rotate(" + hsl.H + "deg) saturate(" + hsl.S + ") brightness(" + hsl.L + ")";
      } else {
        viewPort.HSLFilter = void 0;
        viewPort.filter = void 0;
      }
      WeatherRenderer.get().update(zoneinfo["weather"]);
    }
    /**
     * Event handler to suppress browser's default context menu.
     */
    preventContextMenu(e) {
      e.preventDefault();
    }
    /**
     * Sets the default cursor for the entire page.
     */
    onMouseEnter(e) {
      e.target.style.cursor = "url(" + Paths.sprites + "/cursor/normal.png) 1 3, auto";
    }
    /**
     * Draws indicator on screen to click/touch events when enabled.
     *
     * Experimental feature disabled & hidden from settings dialog by default.
     */
    static handleClickIndicator(e) {
      if (!stendhal.config.getBoolean("click-indicator")) {
        return;
      }
      if (_Client.click_indicator_id !== void 0) {
        window.clearTimeout(_Client.click_indicator_id);
        _Client.click_indicator_id = void 0;
      }
      const pos = stendhal.data.html.extractPosition(e);
      const click_indicator = document.getElementById("click-indicator");
      click_indicator.style["left"] = pos.pageX - click_indicator.width / 2 + "px";
      click_indicator.style["top"] = pos.pageY - click_indicator.height / 2 + "px";
      click_indicator.style["display"] = "inline";
      _Client.click_indicator_id = window.setTimeout(function() {
        click_indicator.style["display"] = "none";
      }, 300);
    }
  };

  // main.ts
  window["Zlib"] = require_inflate().Zlib;
  var build = (init_build(), __toCommonJS(build_exports));
  require_sha3();
  function initGlobals() {
    build.version(import_marauroa87.marauroa, stendhal);
    stendhal.main = Client.get();
  }
  (function() {
    initGlobals();
    stendhal.main.init();
    document.addEventListener("DOMContentLoaded", () => stendhal.main.startup());
    window.addEventListener("error", (error) => stendhal.main.onError(error));
  })();
})();
/*! Bundled license information:

marauroa/index.js:
  (**
   * @license
   ***************************************************************************
   *                   (C) Copyright 2011-2026 - Marauroa                    *
   ***************************************************************************
   ***************************************************************************
   *                                                                         *
   *   This program is free software; you can redistribute it and/or modify  *
   *   it under the terms of the GNU General Public License as published by  *
   *   the Free Software Foundation; either version 2 of the License, or     *
   *   (at your option) any later version.                                   *
   *                                                                         *
   ***************************************************************************)
  (**
   * @license
   **************************************************************************
   *                   (C) Copyright 2011-2026 - Marauroa                    *
   ***************************************************************************
   ***************************************************************************
   *                                                                         *
   *   This program is free software; you can redistribute it and/or modify  *
   *   it under the terms of the GNU General Public License as published by  *
   *   the Free Software Foundation; either version 2 of the License, or     *
   *   (at your option) any later version.                                   *
   *                                                                         *
   ***************************************************************************)

marauroa/inflate.js:
  (**
   * @license zlib.js 2012 - imaya [ https://github.com/imaya/zlib.js ]
   * The MIT License
   *)
*/
