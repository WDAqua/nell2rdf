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
public class OntologyModifier extends Header {

    private String from;

    public OntologyModifier(String str, Double Probability) {
        super(str, ONTOLOGYMODIFIER, Probability);
    }

    public String getMetadata_From() {
        return this.from;
    }

    private void setFrom(String from) {
        this.from = from;
    }

    @Override
    public void processStringText(String str) {
        setFrom(Utility.getOntologyModifier(str));
    }

    @Override
    public String toString() {
        return super.toString() + " FROM: " + this.from + ']';
    }

    @Override
    public String getStringSource() {
        return this.from;
    }
}
