/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;



/**
 *
 * @author Maisa
 */
public class MBL extends Header {

    private String promotionOfConcept;
    private String entity;
    private String entityCategory;
    private String relation;
    private String value;
    private String valueCategory;

    public String getMetadata_Entity() {
        return entity;
    }

    public String getMetadata_EntityCategory() {
        return entityCategory;
    }

    public String getMetadata_Relation() {
        return relation;
    }

    public String getMetadata_Value() {
        return value;
    }

    public String getMetadata_ValueCategory() {
        return valueCategory;
    }

    public String getPromotionOfConcept() {
        return promotionOfConcept;
    }

    public MBL(String str, double Probability) {
        super(str, MBL, Probability);
        System.out.println(LineInstanceJOIN.completeLine);
    }

    @Override
    public void processStringText(String str) {
        this.promotionOfConcept = Utility.getMBLCandidateSource(str);
        String temp[] = this.promotionOfConcept.substring(this.promotionOfConcept.indexOf(":") + 1)
                .replace("concept", "").replace("\"", "").replace(" ", "").split(":");
        this.entityCategory = temp[0];
        this.entity = temp[1];
        this.relation = temp[2];
        this.valueCategory = temp[3];
        this.value = temp[4];

    }

    @Override
    public String toString() {
        return super.toString() + " " + getPromotionOfConcept() + "]";
    }

    @Override
    public String getStringSource() {
        return getPromotionOfConcept();
    }

}
