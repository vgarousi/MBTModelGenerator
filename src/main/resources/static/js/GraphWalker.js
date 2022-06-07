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

var STARTSESS_ENDPOINT = "http://localhost:8496/startrec";
var KEEPALIVE_ENDPOINT = "http://localhost:8496/keepalive";
var VERTEX_ENDPOINT = "http://localhost:8496/vertex";
var EDGE_ENDPOINT = "http://localhost:8496/edge";

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
    } else if (document.attachEvent) {
        document.attachEvent('onclick', intercept);
    }
}

/**
 * Intercept clicks
 * @param e
 */
function intercept(e) {
    var target = e.target;
    var href;
    if(target.tagName === 'BUTTON') {
        submitButtonEdge(target);
        return;
    }
    if(target.tagName === 'A') submitButtonEdge(target);
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
