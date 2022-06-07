package com.sasi.gwmaker.graphwalker.edge;

import com.sasi.gwmaker.graphwalker.vertex.XVertex;
import org.graphwalker.core.model.Edge;
import org.graphwalker.core.model.Vertex;
import org.json.JSONObject;

/**
 * This class extends Edge class to provide additional functionality.
 *
 * @TODO Implement edge to json conversion.
 */
public class XEdge extends Edge {

    @Override
    public Edge setName(String name) {
        return super.setName(name);
    }

    /**
     * Return a JSONObject object with all the following properties:
     *
     * ID
     * Name
     * Source Vertex's ID
     * Target Vertex's ID
     * Description
     *
     */
    public JSONObject getEdgeJson(){
        Vertex source = getSourceVertex();
        Vertex target = getTargetVertex();

        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("name", getName());
        json.put("sourceVertexId", (source != null) ? source.getId() : "");
        json.put("targetVertexId", (target != null) ? target.getId() : "");

        JSONObject properties = new JSONObject();
        properties.put("description", "");

        json.put("properties", properties);

        return json;
    }

}
