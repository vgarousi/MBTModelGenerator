package com.sasi.gwmaker.graphwalker;

import com.sasi.gwmaker.graphwalker.edge.EdgeFactory;
import com.sasi.gwmaker.graphwalker.edge.XEdge;
import com.sasi.gwmaker.graphwalker.generator.XRPathGenerator;
import com.sasi.gwmaker.graphwalker.vertex.VertexFactory;
import com.sasi.gwmaker.graphwalker.vertex.XVertex;
import com.sasi.gwmaker.json.ModelToJsonConverter;
import com.sasi.gwmaker.plane.PlaneGenerator;
import com.sasi.gwmaker.storage.StorageHandler;
import org.graphwalker.core.model.Model;
import org.json.JSONObject;

import java.util.List;

/**
 * Graphwalker class will handle all the core functions of creating
 * a graphwalker compatible MBT file in .json format.
 *
 * The functions in this class will be called from the @WalkerController
 * which is a REST API Controller.
 */
public class GraphWalker {

    /**
     * Path Generator is used to traverse the MBT Model.
     */
    private XRPathGenerator pathGenerator;

    /**
     * The Model will hold all the vertices and edges.
     *
     * This will be used later to generate the MBT .json file.
     */
    private Model model;

    /**
     * A Factory pattern to create vertices based on unique vertexName.
     *
     * VertexFactory is not static as we have to create a new factory for every session.
     */
    private VertexFactory vertexFactory;

    /**
     * A Factory pattern to create edges based on unique edgeName.
     *
     * EdgeFactory is not static as we have to create a new factory for every session.
     */
    private EdgeFactory edgeFactory;

    /**
     * Maintain a count of edges as we have to assign ID's to the vertices based
     * on this count.
     */
    private int edgeCount;

    /**
     * Used to store the last used vertex.
     *
     * This is used as the source vertex of the next upcoming edge as per the flow.
     */
    private XVertex lastVertex;

    /**
     * Used to store the last used Edge.
     *
     * This object is held until the next vertex is received.
     * As the next vertex that is received will be used as the destination vertex
     * of the previous edge.
     *
     * After updating the lastEdge with the target vertex, it will be saved to the
     * EdgeFactory.
     */
    private XEdge lastEdge;

    /**
     * Constructor
     */
    public GraphWalker() {
        pathGenerator = new XRPathGenerator();
        model = new Model();
        model.setProperty("generator", pathGenerator.pathJsonName);
        vertexFactory = new VertexFactory();
        edgeFactory = new EdgeFactory();
    }

    public void setModelName(String name){
        model.setName(name);
    }

    public void onVertex(String vertexName){
        /**
         * Create a new vertex or fetch if it already exists.
         */
        lastVertex = vertexFactory.getVertex(vertexName);
        /**
         * If the new vertex is a result of a previous edge,
         * then this new vertex must be added as a target vertex to the last edge.
         *
         * Check if a previous edge exists, if so, add new vertex as target vertex
         * to the previous edge and save it to the EdgeFactory.
         */
        if(lastEdge != null){
            // set new vertex as a target to the last edge.
            lastEdge.setTargetVertex(lastVertex);
            // save the modified edge into the edge factory.
            edgeFactory.setEdge(lastEdge);
            // clear the last edge variable. just for precaution.
            // so that it won't mess up a fresh new MBT diagram.
            lastEdge = null;
        }
    }

    public void onEdge(String edgeName){
        /**
         * Create a nwe edge or fetch it from the EdgeFactory.
         */
        lastEdge = edgeFactory.getEdge(edgeName);
        /**
         * Set the previous vertex as the source vertex to the new edge.
         */
        if(lastVertex != null){
            lastEdge.setSourceVertex(lastVertex);
        }
    }

    public void finalizeGraph(){
        List<XVertex> vertexList = vertexFactory.getVertices();
        List<XEdge> edgeList = edgeFactory.getEdges();
        for(XVertex vertex: vertexList){
            model.addVertex(vertex);
        }
        for(XEdge edge : edgeList){
            model.addEdge(edge);
        }

        JSONObject mbtJson = ModelToJsonConverter.parseModel(
                PlaneGenerator.generatePlaneData(
                        model
                )
        );

        StorageHandler storageHandler = new StorageHandler();
        if(storageHandler.saveMbtJson(mbtJson.toString(), model.getName())){
            System.out.println("MBT .json file has been successfully created!");
        }else{
            System.out.println("Failed to create the MBT .json file...");
        }
    }

}
