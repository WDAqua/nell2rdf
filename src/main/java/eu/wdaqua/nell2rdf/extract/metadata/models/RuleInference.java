/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.RULEINFERENCE;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;

/**
 *
 * @author Maisa
 */
public class RuleInference extends Header {

   private String rules;

    public RuleInference(String str, double Probability) {
        super(str, RULEINFERENCE, Probability);
    }

    @Override
    public void processStringText(String str) {
        this.rules = Utility.getRuleInference(str);
    }

    public String getRules() {
        return rules;
    }

    @Override
    public String toString() {
        return super.toString() + "{" + getRules() + "}]";
    }

    @Override
    public String getStringSource() {
        return "{" + getRules() + "}";
    }
}
