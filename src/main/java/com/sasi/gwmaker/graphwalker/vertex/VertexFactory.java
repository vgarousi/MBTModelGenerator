package com.sasi.gwmaker.graphwalker.vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Factory used to create vertices.
 *
 * As each vertex has an ID, creation of new vertices needs to be unique
 * and each vertex must be given a unique ID.
 *
 * Hence, the purpose of using Factory pattern for this object.
 */
public class VertexFactory {

    /**
     * Maintain a count of vertices as we have to assign ID's to the vertices based
     * on this count.
     */
    private int vertexCount;

    /**
     * Maintain the list of vertices that were already created.
     *
     * This helps in identifying and creating vertices with unique ID's.
     *
     * The Key is the name of the vertex.
     * Vertex is associated with a unique name.
     */
    private HashMap<String, XVertex> vertices = new HashMap<>();

    /**
     * Get all the vertices added into the vertices list.
     *
     * The vertices list isn't actually a list, but it's a HashMap.
     *
     * So, we have to convert HashMap into a list before returning.
     *
     * @return
     *
     * return vertexList containing all vertices created till now.
     */
    public List<XVertex> getVertices(){
        List vertexList = new ArrayList<>(vertices.values());
        return vertexList;
    }

    /**
     * Check if the vertex already exists in the list of vertices.
     *
     * @param vertexName
     *
     * Unique name of the vertex.
     *
     * @return
     *
     * Boolean value, True means the vertex already exists and False means otherwise.
     *
     */
    public boolean vertexExists(String vertexName){
        return vertices.containsKey(vertexName);
    }

    /**
     * getVertex will create or fetch the vertex associated with the vertexName.
     *
     * The vertexName must be unique as this will be used to identify a vertex.
     *
     * @param vertexName
     *
     * Pass the name of the vertex to be fetched.
     *
     * @return
     *
     * The vertex associated with the vertex name.
     */
    public XVertex getVertex(String vertexName){
        if(vertices.containsKey(vertexName)){
            /**
             * Return a vertex if it already exists.
             */
            return vertices.get(vertexName);
        }else{
            /**
             * If a vertex doesn't exist, create a new vertex,
             * assign a new ID based on the vertex count and return it.
             *
             * The format of the ID is "n$" where $ is the incrementing vertex count.
             */
            XVertex vertex = (XVertex) new XVertex()
                    .setName(vertexName)
                    .setId("n" + (vertexCount));
            vertices.put(vertexName, vertex);
            // Increment the vertex ID after creating a new vertex.
            vertexCount++;
            // Return the created vertex.
            return vertex;
        }
    }


}
