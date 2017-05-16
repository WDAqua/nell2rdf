/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;


import static eu.wdaqua.nell2rdf.extract.metadata.util.Utility.REGEX_COMPONENTS_NAME;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Maisa
 */
public class MBL_OR_ERC extends Header {

    public MBL_OR_ERC(String str, String ComponentName) {
        super(str, ComponentName);
    }

    private void setMapToken(Map<String, String[]> mapToken) {
        this.mapToken = mapToken;
    }

    @Override
    public void processStringText(String str) {
        setMapToken(getComponentsMBL(str));
    }

    //Extract the information from MBL_SOURCE - the column SOURCE
    //Source: A summary of the provenance for the belief indicating the set 
    //of learning subcomponents (CPL, SEAL, etc.) that had submitted this belief as being potentially true.
    private Map<String, String[]> getComponentsMBL(String str) {

        Pattern pattern = Pattern.compile(REGEX_COMPONENTS_NAME);
        Matcher matcher = null;

        matcher = pattern.matcher(str);

        while (matcher.find()) {
            String temp = matcher.group();
            String tempComponentName = temp.substring(0, temp.indexOf("("));
            temp = temp.substring(temp.indexOf("(")).replace("(", "").replace(")", "");
            String token1 = "";
            String token2 = "";

            if (temp.split(",").length == 2) {
                token1 = temp.split(",")[0];
                token2 = temp.split(",")[1];
            }
            mapToken.put(tempComponentName, new String[]{token1, token2});
        }
        return mapToken;
    }

    @Override
    public String getStringSource() {
       return null;
    }
}
