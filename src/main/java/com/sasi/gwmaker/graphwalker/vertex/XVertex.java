package com.sasi.gwmaker.graphwalker.vertex;

import org.graphwalker.core.model.Vertex;
import org.json.JSONObject;

/**
 * This class extends Vertex class to provide additional functionality.
 *
 * @TODO Implement vertex to json conversion.
 */
public class XVertex extends Vertex {

    public int edgeCount;

    public double x;

    public double y;

    /**
     * Return a JSONObject object with all the following properties:
     *
     * ID
     * Name
     * X Co-ordinate
     * Y Co-ordinate
     * Description
     *
     */
    public JSONObject getVertexJson(){
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("name", getName());

        JSONObject properties = new JSONObject();
        properties.put("x", x);
        properties.put("y", y);
        properties.put("description", "");

        json.put("properties", properties);

        return json;
    }

}
