/*
 * Class model of CML and CPL components
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;


import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;

/**
 *
 * @author Maisa
 */
public class CPL extends Header {

       private Map<String, Integer> mapMD;
    //Category or Relation?
    private String from;

    public CPL(String str, double Probability) {
        super(str, CPL, Probability);
    }

    public String getFrom() {
        return from;
    }

    public Map<String, Integer> getMapTPOccurence() {
        return mapMD;
    }

    private void setMapTPOccurence(String TP, int occurence) {
        this.mapMD.put(TP, occurence);
    }

    @Override
    public void processStringText(String str) {
        this.mapMD = new HashMap<>();
        String tempREGEX = "";
        if (str.contains("arg1") && (str.contains("arg2"))) {
            this.from = "Relation";
            tempREGEX = Utility.REGEX_CPL_SOURCE;
        } else {
            this.from = "Category";
            tempREGEX = Utility.REGEX_CML_SOURCE;
        }
        Pattern pattern = Pattern.compile(tempREGEX);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String temp[] = matcher.group().split("\t");
            setMapTPOccurence(temp[0], Integer.valueOf(temp[1]));
        }

    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer();
        temp.append(" FROM: ").append(" ").append(getFrom());
        temp.append(" {");
        for (Map.Entry<String, Integer> pair : this.mapMD.entrySet()) {
            temp.append(pair.getKey()).append("\t").append(pair.getValue()).append("\t");
        }

        if (temp.lastIndexOf("\t") > -1) {
            return super.toString() + temp.toString().substring(0, temp.lastIndexOf("\t")) + "}]";
        } else {
            return super.toString() + temp.toString() + "}]";
        }

    }

    @Override
    public String getStringSource() {
        StringBuffer temp = new StringBuffer();
        temp.append(" FROM: ").append(" ").append(getFrom());
        temp.append(" {");
        for (Map.Entry<String, Integer> pair : this.mapMD.entrySet()) {
            temp.append(pair.getKey()).append("\t").append(pair.getValue()).append("\t");
        }

        if (temp.lastIndexOf("\t") > -1) {
            return temp.toString().substring(0, temp.lastIndexOf("\t")) + "}";
        } else {
            return temp.toString() + "}";
        }
    }

    
}
