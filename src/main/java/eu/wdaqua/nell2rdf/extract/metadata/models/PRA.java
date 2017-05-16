/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import java.util.HashMap;
import java.util.Map;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;


public class PRA extends Header {

     private Map<String, Double> mapTriple;

    public PRA(String str, double Probability) {
        super(str, PRA, Probability);
    }

    public Map<String, Double> getMapTriple() {
        return mapTriple;
    }

    @Override
    public void processStringText(String str) {
        this.componentName = "PRA";
        String temp = Utility.getPRA(str);
        String tempSplit[] = temp.split("\t");

        mapTriple = new HashMap<>();
        for (int i = 0; i < tempSplit.length;) {
            mapTriple.put(tempSplit[0], Double.valueOf(tempSplit[1]));
            i += 2;
        }
    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer();
        temp.append(" {");
        this.mapTriple.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            temp.append('\t').append(key).append('\t').append(value);
        });
        temp.append("}");

        return super.toString() + temp.toString() + "]";
    }

    @Override
    public String getStringSource() {
        StringBuffer temp = new StringBuffer();
        temp.append(" {");
        this.mapTriple.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            temp.append('\t').append(key).append('\t').append(value);
        });
        temp.append("}");
        return temp.toString();
    }

}
