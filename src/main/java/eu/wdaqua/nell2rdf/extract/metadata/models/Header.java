/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maisa
 */
public abstract class Header {

    public FormatHeader formatHeader;
    private String source;
    protected String componentName;
    private int iteration;

    private Double probability;

    private String dateTime;

    protected Map<String, String[]> mapToken;

    abstract public void processStringText(String str);

    abstract public String getStringSource();

    public Header(String str, String ComponentName, Double Probability) {
        this.headerTreatment(str, ComponentName);
        this.probability = Probability;
    }

    public Header(String str, String ComponentName) {
        this.headerTreatment(str, ComponentName);
    }

    private void headerTreatment(String str, String ComponentName) {
        this.mapToken = new HashMap<>();
        this.componentName = ComponentName;
        this.source = str;
        processStringText(this.source);
        try {
            this.dateTime = Utility.getDateTime(Utility.getComponentsHeader(str));
        } catch (ParseException ex) {
            Logger.getLogger(Header.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setIteration(str);

        setToken(str);

        //POG
        formatHeader = new FormatHeader();
        formatHeader.formattingHeaderToken(mapToken, componentName);
        formatHeader.setTempDateTime(dateTime);
        formatHeader.setTempIteration(iteration);
        formatHeader.setTempProbability(probability);
        //END POG

    }

    public String getSource() {
        return source;
    }

    public int getIteration() {
        return iteration;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getComponentName() {
        return componentName;
    }

    public double getProbability() {
        return probability;
    }

    public Map<String, String[]> getMapToken() {
        return mapToken;
    }

    public void setToken(String str) {
        // str = "SpreadsheetEdits-Iter:924-2015/05/13-13:58:04-<token=angelina_county_airport,//en.wikipedia.org/wiki/angelina%20county%20airport>-bkisiel: \"angelina_county_airport haswikipediaurl //en.wikipedia.org/wiki/angelina%20county%20airport\", Action=(+haswikipediaurl) (from NELL.08m.924.SSFeedback.csv)";
        String temp = Utility.getToken(str);
        if (!temp.isEmpty()) {
            String tempSlip[] = temp.split(",");
            if (tempSlip.length == 2) {
                try {
                    mapToken.put("token", new String[]{tempSlip[0], tempSlip[1]});
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Pera " + e + "\n" + str + "\nCompleteLine: " + LineInstanceJOIN.completeLine);
                }
            } else {
                mapToken.put("token", new String[]{tempSlip[0], ""});
            }
        } else {
            mapToken.put("token", new String[]{"", ""});
        }
    }

    public void setIteration(String str) {
        this.iteration = Integer.valueOf(Utility.getIterationComponent(str));
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        //Component Name
        output.append("[ComponentName: ").append(this.componentName).append("\t");
        //Iterations
        output.append("Iteration: ");
        int i = 0;
        output.append(this.iteration);
        output.append("\t");
        //Datetime
        output.append("Datetime: ").append(this.dateTime).append("\t");
        //Tokens
        String[] tempKey = mapToken.get("token");
        try {
            output.append("Tokens: <").
                    append(tempKey[0]).append(",").
                    append(tempKey[1]);
        } catch (NullPointerException e) {
            System.out.println("OutO");
        }
        if (this.componentName.contains("Lat")) {
            output.append(",").append(tempKey[2]);
        }
        output.append(">");
        output.append(" probability [").append(this.probability).append("]");
        return output.toString();

    }

    public FormatHeader getFormatHeader() {
        return formatHeader;
    }
}
