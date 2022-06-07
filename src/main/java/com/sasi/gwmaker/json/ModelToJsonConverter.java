package com.sasi.gwmaker.json;

import com.sasi.gwmaker.graphwalker.edge.XEdge;
import com.sasi.gwmaker.graphwalker.vertex.XVertex;
import org.graphwalker.core.model.Edge;
import org.graphwalker.core.model.Model;
import org.graphwalker.core.model.Vertex;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Converts the model object into JSON format.
 *
 * This will later be saved into a json file in storage.
 */
public class ModelToJsonConverter {

    /**
     * Extract all variables from Model object and write them into JSON object.
     *
     * @param model
     *
     * Model object provided by GraphWalker.
     *
     * @return
     *
     * JSON object to be saved.
     *
     */
    public static JSONObject parseModel(Model model){
        JSONObject json = new JSONObject();
        json.put("name", model.getName());
        json.put("generator", model.getProperty("generator"));

        JSONArray edgeArray = new JSONArray();
        for(Edge edge : model.getEdges()){
            edgeArray.put(
                    ((XEdge) edge).getEdgeJson()
            );
        }

        JSONArray vertexArray = new JSONArray();
        for(Vertex vertex: model.getVertices()){
            vertexArray.put(
                    ((XVertex) vertex).getVertexJson()
            );
        }

        json.put("edges", edgeArray);
        json.put("vertices", vertexArray);

        JSONArray models = new JSONArray();
        models.put(json);

        JSONObject finalJson = new JSONObject();
        finalJson.put("models", models);

        return finalJson;
    }

}
