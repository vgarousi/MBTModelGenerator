package com.sasi.gwmaker;

import com.sasi.gwmaker.graphwalker.edge.XEdge;
import com.sasi.gwmaker.graphwalker.generator.XRPathGenerator;
import com.sasi.gwmaker.graphwalker.vertex.XVertex;
import com.sasi.gwmaker.json.ModelToJsonConverter;
import com.sasi.gwmaker.plane.PlaneGenerator;
import com.sasi.gwmaker.storage.StorageHandler;
import org.graphwalker.core.model.Model;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PlaneGenerationTests {

    @Test
    public void generatePlaneTest(){
        Model model = new Model();
        model.setName("ShoppingCart");
        model.setProperty("generator", new XRPathGenerator().pathJsonName);
        XVertex n2 = vert("Amazon", "n2");
        XVertex n3 = vert("SearchResult", "n3");
        XVertex n4 = vert("BookInformation", "n4");
        XVertex n5 = vert("AddedToCart", "n5");
        XVertex n6 = vert("ShoppingCart", "n6");

        model.addVertex(n2);
        model.addVertex(n3);
        model.addVertex(n4);
        model.addVertex(n5);
        model.addVertex(n6);

        model.addEdge(edge("SearchBook", "e10", n4, n3));
        model.addEdge(edge("EnterBaseURL", "c2a189b6-bd93-4fa8-a32a-c5d0aafe4a0a", n2, n2));
        model.addEdge(edge("SearchBook", "e2", n2, n3));
        model.addEdge(edge("ClickBook", "e3", n3, n4));
        model.addEdge(edge("AddBookToCart", "e4", n4, n5));
        model.addEdge(edge("ShoppingCart", "e5", n5, n6));
        model.addEdge(edge("ShoppingCart", "e6", n3, n6));
        model.addEdge(edge("ShoppingCart", "e7", n4, n6));
        model.addEdge(edge("SearchBook", "e8", n6, n3));
        model.addEdge(edge("SearchBook", "e9", n5, n3));

        JSONObject mbtJson = ModelToJsonConverter.parseModel(
                PlaneGenerator.generatePlaneData(
                        model
                )
        );

        StorageHandler storageHandler = new StorageHandler();
        boolean result = storageHandler.saveMbtJson(mbtJson.toString(), model.getName());

        assertThat(
                result
        ).isEqualTo(true);

    }

    private XVertex vert(String name, String id){
        XVertex vert = new XVertex();
        vert.setName("v_" + name);
        vert.setId(id);
        return vert;
    }

    private XEdge edge(String name, String id, XVertex source, XVertex target){
        XEdge edge = new XEdge();
        edge.setName("e_" + name);
        edge.setId(id);
        edge.setSourceVertex(source);
        edge.setTargetVertex(target);
        return edge;
    }

}
