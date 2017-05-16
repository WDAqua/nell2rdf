/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.LATLONG;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.LATLONGTT;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;

/**
 *
 * @author Maisa
 */
public class LatLong extends Header {

    private String rules;

    public LatLong(String str, String ComponentName, double Probability) {
        super(str, ComponentName, Probability);
    }

    public String getRules() {
        return rules;
    }

    @Override
    public void processStringText(String str) {
        String temp = "";
        if (this.componentName.equalsIgnoreCase(LATLONG)) {
            temp = Utility.getLatog(str);
        } else if (this.componentName.equalsIgnoreCase(LATLONGTT)) {
            temp = Utility.getLatogTT(str);
        }
        this.rules = temp;
    }

    @Override
    public void setToken(String str) {
        String temp = Utility.getTokenLatLong(str);
        if (!temp.isEmpty()) {
            String tempSlip[] = temp.split(",");
            mapToken.put("token", new String[]{tempSlip[0], tempSlip[1], tempSlip[2]});
        }
    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer();
        temp.append("[").append("[ComponentName: ").append(this.componentName).append(" {");
        temp.append(mapToken.get("token")[0]).append(",")
                .append(mapToken.get("token")[1]).append(",")
                .append(mapToken.get("token")[2]).append("}").
                append(mapToken.get("token")[2]).append("} [").append(rules).append("]");;
        return super.toString() + temp.toString();
    }

    @Override
    public String getStringSource() {
        StringBuffer temp = new StringBuffer();
        temp.append("[").append(rules).append("]");
        return temp.toString();
    }

}
