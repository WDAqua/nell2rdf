/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;

/**
 *
 * @author Maisa
 */
public class LE extends Header {

    public LE(String str, double Probability) {
        super(str, LE, Probability);
    }

    @Override
    public void processStringText(String str) {

    }

    @Override
    public String getStringSource() {
        return null;
    }

}
