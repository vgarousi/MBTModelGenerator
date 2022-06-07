/**
 *
 * Include this script into all html files.
 *
 * <script type="text/javascript" src="/js/GraphWalker.js" ></script>
 *
 * This script will keep pinging the keepalive endpoint of the
 * local graphwalker click stream collection REST API server.
 *
 * When the user closes the website, this script will stop
 * sending keepalive requests and after the server timeout,
 * the click stream collection session will stop and finalize
 * the MBT .json file creation.
 *
 * The current delay between sending keep alive requests is
 * 5 Seconds.
 *
 */

var KEEP_ALIVE_DURATION = 3333;

var PORT = "8079";

var STARTSESS_ENDPOINT = "http://localhost:" + PORT +"/startrec";
var KEEPALIVE_ENDPOINT = "http://localhost:" + PORT +"/keepalive";
var VERTEX_ENDPOINT = "http://localhost:" + PORT +"/vertex";
var EDGE_ENDPOINT = "http://localhost:" + PORT +"/edge";

var LAST_EDGE = "last_edge";

var sessionInit = sessionStorage.getItem("sessionInit");

init()

function init(){
    if(sessionInit == null || sessionInit != "true"){
        startSession();
        sessionStorage.setItem("sessionInit", "true");
    }else{
        keepAlive()
        window.setInterval(keepAlive, KEEP_ALIVE_DURATION)
    }

    if (document.addEventListener) {
        document.addEventListener('click', intercept);
        document.addEventListener('focusin', focusin);
        document.addEventListener('focusout', focusout);
    } else if (document.attachEvent) {
        document.attachEvent('onclick', intercept);
        document.attachEvent('focusin', focusin);
        document.attachEvent('focusout', focusout);
    }
}

var targetBeforeText = "";

function focusin(e) {
    var target = e.target;
    if(target.tagName == "INPUT") {
        targetBeforeText = target.value;
    }
}

function focusout(e) {
    var target = e.target;
    if(target.tagName == "INPUT") {
        if(targetBeforeText != target.value) {
            console.log("Input Focus Out Text Changed: " + target.value);
            submitInput(target)
        }
        targetBeforeText = target.value;
    }
}

/**
 * Intercept clicks
 * @param e
 */
function intercept(e) {
    var target = e.target;
    var href;
    console.log("Intercepted: " + target.tagName + " value: " + target.value);
    switch (target.tagName){
        case "INPUT":
            submitInput(target);
            break;
        case "BUTTON":
            submitButtonEdge(target);
            break;
        case "A":
            submitButtonEdge(target);
            break;
    }
}

function submitInput(target){
    var text = target.value;
    var id = target.id;
    console.log("Input: " + id + " value: " + text);
    if(text != null && text !== "") edge("" + id + ":" + text);
}

function submitButtonEdge(target){
    var text = target.innerText;
    if(text == null){
        text = target.innerHTML;
    }
    console.log("Button: " + text);
    if(text != null && text !== "") edge(text);
}

function startSession(){
    console.log("Starting session...")
    var ssr = new XMLHttpRequest()
    var data = new FormData()
    data.append("title", document.title)
    ssr.open("POST", STARTSESS_ENDPOINT, false)
    ssr.onload = function () {
        console.log("Response: " + this.responseText)
        if(this.responseText == "STARTED"){
            window.setInterval(keepAlive, KEEP_ALIVE_DURATION)
        }
    }
    ssr.send(data)
}

function vertex(name){
    console.log("Sending vertex: " + name)
    var ssr = new XMLHttpRequest()
    var data = new FormData()
    data.append("name", name)
    ssr.open("POST", VERTEX_ENDPOINT)
    ssr.onload = function () {
        console.log("Response: " + this.responseText)
    }
    ssr.send(data)
}

function edge(name){
    console.log("Sending edge: " + name)
    var ssr = new XMLHttpRequest()
    var data = new FormData()
    data.append("name", name)
    ssr.open("POST", EDGE_ENDPOINT)
    ssr.onload = function () {
        console.log("Response: " + this.responseText)
    }
    ssr.send(data)
}

function keepAlive(){
    var kar = new XMLHttpRequest()
    kar.open("POST", KEEPALIVE_ENDPOINT)
    kar.send()
}
