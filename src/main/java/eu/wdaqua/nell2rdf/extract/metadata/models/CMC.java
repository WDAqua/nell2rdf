/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;


import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;

/**
 *
 * @author Maisa
 */
public class CMC extends Header {

    //List of CMC fields
    private List<CMCObjects> cmcList;
    
    public CMC(String str, double Probability) {
        super(str, CMC, Probability);
    }
    
    private void setCmcList(String field, float score) {
        cmcList.add(new CMCObjects(field, score));
    }
    
    private void setCmcList(String field) {
        //fake value - This format does not give a number
        cmcList.add(new CMCObjects(field, -0000001));
    }
    
    public List<CMCObjects> getCmcList() {
        return cmcList;
    }
    
    @Override
    public void processStringText(String str) {
        this.cmcList = new ArrayList<>();
        
        Pattern pattern = Pattern.compile(Utility.REGEX_CMC_SOURCE_FLOAT);
        Matcher matcher = pattern.matcher(str);
        
        while (matcher.find()) {
            String tempFloat[] = matcher.group().split("\t");
            setCmcList(tempFloat[0].trim(), Float.valueOf(tempFloat[1].trim()));
        }
        
        pattern = Pattern.compile(Utility.REGEX_CMC_SOURCE_STRING);
        matcher = pattern.matcher(str);
        
        while (matcher.find()) {
            String tempFloat[] = matcher.group().split("\t");
            //Add -0000001 as a fake float number
            setCmcList(tempFloat[0].trim());
        }
        
    }
    
    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer();
        temp.append("{");
        for (CMCObjects cmcListTemp : getCmcList()) {
            temp.append(cmcListTemp.getField()).append(" ").append(cmcListTemp.getScore()).append(" ");
        }
        return super.toString() + " " + temp.toString().trim() + "}]";
    }
    
    @Override
    public String getStringSource() {
        StringBuffer temp = new StringBuffer();
        temp.append("{");
        for (CMCObjects cmcListTemp : getCmcList()) {
            temp.append(cmcListTemp.getField()).append(" ").append(cmcListTemp.getScore()).append(" ");
        }
        return temp.append("}").toString();
    }

    //Fiels found in CMC
    class CMCObjects {
        
        private String field;
        private double score;
        
        public CMCObjects(String field, double score) {
            this.field = field;
            this.score = score;
        }
        
        public String getField() {
            return field;
        }
        
        public double getScore() {
            return score;
        }
        
    }
}
