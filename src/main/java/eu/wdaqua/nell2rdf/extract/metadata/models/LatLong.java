/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.LATLONG;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.LATLONGTT;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Maisa
 */
public class LatLong extends Header {

    private String rules;
    private List<Rule> lrules;

    public List<Rule> getMetadata_Lrules() {
        return lrules;
    }

    public LatLong(String str, String ComponentName, double Probability) {
        super(str, ComponentName, Probability);
    }

    public String getRules() {
        return rules;
    }

    public String getMetadata_Phrase(int index) {
        return this.lrules.get(index).getsPhrase();
    }

    public Double getMetadata_Value1(int index) {
        return this.lrules.get(index).getValue1();
    }

    public Double getMetadata_Value2(int index) {
        return this.lrules.get(index).getValue2();
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

        setRules(str);
    }

    public void setRules(String str) {

        List<Rule> lrules = new ArrayList<>();

        Pattern pattern = Pattern.compile(Utility.REGEX_LATLONG_RULES);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            String temp = matcher.group();

            String tempDouble[] = temp.split("@");
            Double value1 = Double.parseDouble(tempDouble[1].trim().split(",")[0]);
            Double value2 = Double.parseDouble(tempDouble[1].trim().split(",")[1]);
            String sPhrase = tempDouble[0].trim();

            lrules.add(new Rule(sPhrase, value1, value2));
        }
        this.lrules = lrules;
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
                .append(mapToken.get("token")[2]).append("}")
                .append("} [").append(rules).append("]");
        return super.toString() + temp.toString();
    }

    @Override
    public String getStringSource() {
        StringBuffer temp = new StringBuffer();
        temp.append("[").append(rules).append("]");
        return temp.toString();
    }

    private class Rule {

        String sPhrase;
        Double value1;
        Double value2;

        public String getsPhrase() {
            return sPhrase;
        }

        public Double getValue1() {
            return value1;
        }

        public Double getValue2() {
            return value2;
        }

        public Rule(String sPhrase, Double value1, Double value2) {
            this.sPhrase = sPhrase;
            this.value1 = value1;
            this.value2 = value2;
        }

    }

}
