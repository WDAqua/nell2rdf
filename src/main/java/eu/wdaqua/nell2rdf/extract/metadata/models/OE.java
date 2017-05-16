/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;


import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;
/**
 *
 * @author Maisa
 */
public class OE extends Header {

  private Map<String, URL> mapTextURL;

    public OE(String str, double Probability) {
        super(str, OE, Probability);
    }

    public Map<String, URL> getMapTextURL() {
        return mapTextURL;
    }

    @Override
    public String toString() {

        StringBuffer temp = new StringBuffer();
        temp.append(" {");
        this.mapTextURL.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            URL value = entry.getValue();
            temp.append(key).append('\t').append(value);
        });
        temp.append("}");

        return super.toString() + temp.toString() + "]";
    }

    @Override
    public void processStringText(String str) {
        this.mapTextURL = new HashMap<>();

        Pattern pattern = Pattern.compile(Utility.REGEX_OE_SOURCE);
        Matcher matcher = pattern.matcher(str);
        matcher.find();

        String temp[] = matcher.group().trim().split("\t");
        int i = 0;
        while (i < temp.length) {
            if (((i + 1) >= temp.length) || ("".equals(temp[i + 1]))) {
                mapTextURL.put(temp[i], null);
            } else {
                try {
                    mapTextURL.put(temp[i], new URL(temp[i + 1]));
                } catch (MalformedURLException ex) {
                    System.out.println("Problema com URL");
                }
            }
            i += 2;
        }
    }

    @Override
    public String getStringSource() {
        StringBuffer temp = new StringBuffer();
        temp.append(" {");
        this.mapTextURL.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            URL value = entry.getValue();
            temp.append(key).append('\t').append(value);
        });
        temp.append("}");

        return temp.toString();
    }
}
