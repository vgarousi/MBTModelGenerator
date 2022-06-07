package com.sasi.gwmaker.plane;

import com.sasi.gwmaker.graphwalker.vertex.XVertex;
import org.graphwalker.core.model.Edge;
import org.graphwalker.core.model.Model;
import org.graphwalker.core.model.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class handles the placement of the vertices on the 2D X-Y Axis graph.
 *
 * The placement is done on the basis of the number of edges a vertex is connected to.
 *
 * The number of edges is calculated for every vertex and the vertices are then
 * sorted in ascending order based on the number of vertices into a list.
 *
 * Then, the vertices are places on a circular path equidistantly in a zig
 */
public class PlaneGenerator {

    private static int MINIMUM_SEPARATION = 400;

    private static int ROTATION = 90;

    public static Model generatePlaneData(Model model){

        int vertexCount = model.getVertices().size();

        double angularSeparation = (360.0 / vertexCount);
        double radius = (((double) MINIMUM_SEPARATION) / 2.0) / sin( angularSeparation/2.0 );

        System.out.println("Radius: " + radius);
        System.out.println("Angular Separation: " + angularSeparation);

        List<XVertex> vertices = new ArrayList<>();

        // Calculate number of edges attached to each vertex.
        for(Vertex vertex : model.getVertices()){
            vertices.add(
                    populateEdgeCount(model, (XVertex) vertex)
            );
        }

        // Sort vertices based on number of edges connected in ascending order.
        Collections.sort(
                vertices,
                Comparator.comparingInt(o -> o.edgeCount)
        );

        boolean flipper = true;
        int downCount = (vertexCount - 1);
        int upCount = 0;
        int totalCount = 0;
        int currentIndex;

        for(; totalCount < vertexCount; totalCount++){
            if(flipper){
                currentIndex = downCount--;
            }else{
                currentIndex = upCount++;
            }

            ((XVertex) vertices.get( currentIndex ))
                    .x = cos( ((totalCount) * angularSeparation) - ROTATION ) * radius;
            ((XVertex) vertices.get( currentIndex ))
                    .y = sin( ((totalCount) * angularSeparation) - ROTATION ) * radius;

            // Flip the flipper.
            flipper = !flipper;
        }

        Model newModel = new Model();
        newModel.setName( model.getName() );
        newModel.setProperties( model.getProperties() );

        /**
         * Add vertices first and edges later as adding edges before
         * vertices will cause duplicate vertices!
         */
        for(Vertex vertex : model.getVertices()){
            newModel.addVertex(vertex);
        }

        for(Edge edge : model.getEdges()){
            newModel.addEdge(edge);
        }

        return newModel;
    }

    private static XVertex populateEdgeCount(Model model, XVertex vertex){
        for(Edge edge : model.getEdges()){
            if(edge.getSourceVertex() != null) if(edge.getSourceVertex().getId() == vertex.getId()) vertex.edgeCount++;
            if(edge.getTargetVertex() != null) if(edge.getTargetVertex().getId() == vertex.getId()) vertex.edgeCount++;
        }

        return vertex;
    }

    private static double sin(double degrees){
        return Math.sin(
                Math.toRadians(degrees)
        );
    }

    private static double cos(double degrees){
        return Math.cos(
                Math.toRadians(degrees)
        );
    }

}
