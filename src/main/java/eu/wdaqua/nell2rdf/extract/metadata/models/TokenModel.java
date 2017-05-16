/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

/**
 *
 * @author Maisa
 */
public class TokenModel {
     private String typeKB;
    private String element1;
    private String element2;
    private double[] valueElement2;

    public String getTypeKB() {
        return typeKB;
    }

    public String getElement1() {
        return element1;
    }

    public String getElement2() {
        return element2;
    }

    public double[] getValueElement2() {
        return valueElement2;
    }

    public TokenModel(String typeKB, String element1, double first, double second) {
        this.typeKB = typeKB;
        this.element1 = element1;
        valueElement2 = new double[2];
        this.valueElement2[0] = first;
        this.valueElement2[1] = second;
    }

    public TokenModel(String typeKB, String element1, String element2) {
        this.typeKB = typeKB;
        this.element1 = element1;
        this.element2 = element2;
    }
}
