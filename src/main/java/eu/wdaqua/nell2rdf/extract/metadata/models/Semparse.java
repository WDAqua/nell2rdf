/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;

import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;

/**
 *
 * @author Maisa
 */

public class Semparse extends Header {

    private String sSentence;

    public String getMetadata_SentenceList() {
        return sSentence;
    }

    public Semparse(String str, Double Probability) {
        super(str, SEMPARSE, Probability);
    }

    @Override
    public void processStringText(String str) {
        sSentence = Utility.getSEMPARSE(str);

    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer();
        temp.append("{");

        temp.append(sSentence).append("\t");

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
        temp.append(sSentence).append("\t");
        if (temp.lastIndexOf("\t") > -1) {
            return temp.toString().substring(0, temp.lastIndexOf("\t")) + "}";
        } else {
            return temp.toString() + "}";
        }
    }
}
