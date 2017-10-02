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

    public List<CMCObjects> getMetadata_CmcList() {
        return cmcList;
    }

    public CMC(String str, Double Probability) {
        super(str, CMC, Probability);
    }

    private void setCmcList(String fieldName, String fieldValue, double score) {
        cmcList.add(new CMCObjects(fieldName, fieldValue, score));
    }

    private void setCmcList(String fieldName, String fieldValue) {
        //fake value - This format does not give a number
        cmcList.add(new CMCObjects(fieldName, fieldValue, -0000001));
    }

    public String getMetaData_CMCObjectFieldName(int index) {
        return cmcList.get(index).getFieldName();
    }

    public String getMetaData_CMCObjectFieldValue(int index) {
        return cmcList.get(index).getFieldValue();
    }

    public Double getMetaData_CMCObjectScore(int index) {
        return cmcList.get(index).getScore();
    }

    public String getCMCObjetcFieldComplete(int index) {
        return this.cmcList.get(index).getFieldName()
                + '=' + this.cmcList.get(index).getFieldValue()
                + '\t' + this.cmcList.get(index).getScore();
    }

    @Override
    public void processStringText(String str) {
        this.cmcList = new ArrayList<>();

        Pattern pattern = Pattern.compile(Utility.REGEX_CMC_SOURCE_FLOAT);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            String temp = matcher.group();
            String tempDouble[] = temp.split("\t");
            String tempString[] = new String[2];

            if (temp.contains("=")) {
                tempString = tempDouble[0].split("=");
            } else {
                tempString[0] = tempDouble[0];
                tempString[1] = "";
            }
            // try {
            setCmcList(tempString[0].trim(), tempString[1].trim(), Double.valueOf(tempDouble[1].trim()));
            //} catch (ArrayIndexOutOfBoundsException e) {
            //     System.out.println(temp + "\t" + str + "\n" + Utility.REGEX_CMC_SOURCE_FLOAT);
            // }
        }

        /*
        pattern = Pattern.compile(Utility.REGEX_CMC_SOURCE_STRING);
        matcher = pattern.matcher(str);

        while (matcher.find()) {

            String temp = matcher.group();
            String tempDouble[] = temp.split("\t");
            String tempString[] = tempDouble[0].split("=");

            //Add -0000001 as a fake float number
            setCmcList(tempString[0].trim(), tempString[1].trim());
        }*/
    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer();
        temp.append("{");
        for (CMCObjects cmcListTemp : this.cmcList) {
            temp.append(cmcListTemp.getFieldName()).append("=").
                    append(cmcListTemp.getFieldValue()).append(" ").
                    append(cmcListTemp.getScore()).append(" ");
        }
        return super.toString() + " " + temp.toString().trim() + "}]";
    }

    @Override
    public String getStringSource() {
        StringBuffer temp = new StringBuffer();
        temp.append("{");
        for (CMCObjects cmcListTemp : this.cmcList) {
            temp.append(cmcListTemp.getFieldName()).append("=").
                    append(cmcListTemp.getFieldValue()).append(" ").
                    append(cmcListTemp.getScore()).append(" ");
        }
        return temp.append("}").toString();
    }

    //Fiels found in CMC
    private class CMCObjects {

        private String fieldName;
        private String fieldValue;
        private double score;

        public CMCObjects(String fieldName, String fieldValue, double score) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
            this.score = score;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public double getScore() {
            return score;
        }

    }
}
