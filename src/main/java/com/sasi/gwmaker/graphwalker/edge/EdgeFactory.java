package com.sasi.gwmaker.graphwalker.edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Factory used to create edges.
 *
 * As each edge has an ID, creation of new edges needs to be unique
 * and each edge must be given a unique ID.
 *
 * Hence, the purpose of using Factory pattern for this object.
 */
public class EdgeFactory {

    /**
     * Maintain a count of edges as we have to assign ID's to the edges based
     * on this count.
     */
    private int edgeCount;

    /**
     * Maintain the list of edges that were already created.
     *
     * This helps in identifying and creating edges with unique ID's.
     *
     * The Key is the name of the edge.
     * Edge is associated with a unique name.
     */
    private HashMap<String, XEdge> edges = new HashMap<>();

    /**
     * Get all the edges added into the edges list.
     *
     * The edges list isn't actually a list, but it's a HashMap.
     *
     * So, we have to convert HashMap into a list before returning.
     *
     * @return
     *
     * return edgeList containing all edges created till now.
     */
    public List<XEdge> getEdges(){
        List edgeList = new ArrayList<>(edges.values());
        return edgeList;
    }

    /**
     * Check if the edge already exists in the list of edges.
     *
     * @param edgeName
     *
     * Unique name of the edge.
     *
     * @return
     *
     * Boolean value, True means the edge already exists and False means otherwise.
     *
     */
    public boolean edgeExists(String edgeName){
        return edges.containsKey(edgeName);
    }

    /**
     * getEdge will create the edge associated with the edgeName.
     *
     * The edgeId must be unique as this will be used to identify an edge.
     *
     * @param edgeName
     *
     * Pass the name of the edge to be fetched.
     *
     * @return
     *
     * The edge associated with the edge name.
     */
    public XEdge getEdge(String edgeName){
        XEdge edge = (XEdge) new XEdge()
                .setName(edgeName)
                .setId("e" + edgeCount);
        edges.put(edge.getId(), edge);
        edgeCount++;
        return edge;
    }

    /**
     * This method allows the updated edge to be inserted into the list of edges.
     *
     * The updates include updated source and target vertex.
     *
     * @param edge
     *
     * Edge object.
     *
     */
    public void setEdge(XEdge edge){
        /**
         * Insert edge object into the hash map.
         */
        edges.put(edge.getName(), edge);
    }

}
