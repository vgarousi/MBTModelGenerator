package com.sasi.gwmaker.controllers;

import com.sasi.gwmaker.enums.WalkerStatus;
import com.sasi.gwmaker.graphwalker.GraphWalker;
import com.sasi.gwmaker.graphwalker.GraphWalkerFactory;
import com.sasi.gwmaker.timer.ITaskTimer;
import com.sasi.gwmaker.timer.TaskTimer;
import com.sasi.gwmaker.util.StringUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InvalidAttributeValueException;
import javax.servlet.http.HttpServletResponse;

@RestController
public class WalkerController implements ITaskTimer {

    private String OK = "OK";

    private String FAIL = "FAIL";

    private boolean verboseLogging = false;

    /**
     * Wait for specified no. of Seconds for the keepAlive request to be received.
     *
     * If a keepAlive request is not received in the KEEP_ALIVE_DURATION,
     * the record session is terminated.
     */
    private long KEEP_ALIVE_DURATION = 10000L;

    /**
     * Last epoch time the KeepAlive request was received.
     *
     * Will be used to calculate the gap between the requests for logging puposes.
     */
    private long lastKeepAliveMs;

    /**
     * GraphWalker object handles all MBT file generation tasks.
     */
    private GraphWalker graphWalker;

    /**
     * Keep Alive Task Timer.
     *
     * After the start of the session this timer needs to be reset before the
     * keep alive duration for the session to be running.
     *
     * If the timer runs out, it indicates that the website has been closed
     * and the session is stopped and the MBT .json file is generated.
     *
     */
    private TaskTimer kaTimer;

    private WalkerStatus status = WalkerStatus.STOPPED;

    /**
     * This Endpoint must be called before the start of a ClickStream session.
     *
     * This will set up all the variables required.
     *
     * If the startrec endpoint was not called at the start of session, all the other
     * endpoints will fail to work.
     *
     * @param title
     *
     * Title of the Website SUT. Will be used as the name of the model.
     */
    @PostMapping("/startrec")
    public String onStart(@RequestParam("title") String title){
        switch(status) {
            case STARTED:
                // Walker is already started. Ignore this API Call.
                return "STARTED";
            case STOPPED:
                // Get a new graph walker object at the start of the new session
                graphWalker = GraphWalkerFactory.newGraphWalker();
                graphWalker.setModelName(title);
                kaTimer = new TaskTimer(KEEP_ALIVE_DURATION);
                kaTimer.setTimerInterface(this);
                try {
                    kaTimer.startTimer();
                    lastKeepAliveMs = System.currentTimeMillis();
                } catch (InvalidAttributeValueException e) {
                    e.printStackTrace();
                    return "FAIL";
                }

                System.out.println("Start Received.");
                System.out.println("Setting Model Name: " + title);
                status = WalkerStatus.STARTED;
                return "STARTED";
            default:
                // Unknown state.
                return "UNKNOWN";
        }
    }

    /**
     * Call this endpoint to add a new vertex.
     *
     * @param name
     *
     * Name of the vertex.
     *
     * @return
     *
     * OK if the vertex has been successfully added.
     *
     * FAIL if the graphwalker has not been initialized yet.
     */
    @PostMapping("/vertex")
    public String onVertex(@RequestParam("name") String name){
        name = StringUtil.processVertexName(name);
        if(graphWalker != null){
            System.out.println("Vertex Received: " + name);
            graphWalker.onVertex(name);
            return OK;
        }else {
            return FAIL;
        }
    }

    /**
     * Call this endpoint to add a new edge.
     *
     * @param name
     *
     * Name of the edge.
     *
     * @return
     *
     * OK if the edge has been successfully added.
     *
     * FAIL if the graphwalker has not been initialized yet.
     */
    @PostMapping("/edge")
    public String onEdge(@RequestParam("name") String name){
        name = StringUtil.processEdgeName(name);
        if(graphWalker != null){
            System.out.println("Edge Received: " + name);
            graphWalker.onEdge(name);
            return OK;
        }else {
            return FAIL;
        }
    }

    /**
     * This endpoint must be called for every
     */
    @PostMapping("/keepalive")
    public String onKeepAlive(HttpServletResponse response){
        if(graphWalker != null) {
            long kaGap = (System.currentTimeMillis() - lastKeepAliveMs);
            System.out.println("KEEPALIVE Received. Resetting timer... DUR: "  + (kaGap / 1000) + " sec");
            lastKeepAliveMs = System.currentTimeMillis();
            boolean restartSuccess = kaTimer.resetTimer();
            if (restartSuccess) {
                if(verboseLogging) System.out.println("-Timer restart success!");
            } else {
                if(verboseLogging) System.out.println("-Timer restart failed! Did you start the timer in the first place?");
            }
            return OK;
        }else{
            response.setStatus(409);
            return FAIL + ": Session not started!";
        }
    }

    @PostMapping("/stoprec")
    public void onStop(){
        finalizeGraphs();
    }

    @Override
    public void onTimeUp() {
        System.out.println(
                "Looks like the client is offline as i have not received any keepalive requests" +
                        " in the last 10 Seconds!\n" +
                        "-Halting the record session and finalizing the graphs..."
        );
        finalizeGraphs();
    }

    private void finalizeGraphs(){
        status = WalkerStatus.STOPPED;
        graphWalker.finalizeGraph();

        graphWalker = null;
    }

}
