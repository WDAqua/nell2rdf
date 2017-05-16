/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;

/**
 *
 * @author Maisa
 */
public class Semparse extends Header {

     private List<String> listSentence;

    public List<String> getListSentence() {
        return listSentence;
    }

    public Semparse(String str, double Probability) {
        super(str, SEMPARSE, Probability);
    }

    @Override
    public void processStringText(String str) {
        listSentence = new ArrayList<>();
        String tempSplit[] = Utility.getSEMPARSE(str).split(",");
        listSentence.addAll(Arrays.asList(tempSplit));
    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer();
        temp.append("{");
        for (String sentence : this.listSentence) {
            temp.append(sentence).append("\t");
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
        temp.append("{");
        for (String sentence : this.listSentence) {
            temp.append(sentence).append("\t");
        }
        if (temp.lastIndexOf("\t") > -1) {
            return temp.toString().substring(0, temp.lastIndexOf("\t")) + "}";
        } else {
            return temp.toString() + "}";
        }
    }
}
