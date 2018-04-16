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

    public MBL(String str, Double Probability) {
        super(str, MBL, Probability);
    }

    @Override
    public void processStringText(String str) {
        this.promotionOfConcept = Utility.getMBLCandidateSource(str);
        if (this.promotionOfConcept.isEmpty()) {
            this.promotionOfConcept = Utility.getMBLCandidateSourceUpdat0418(str);
        }
        String temp[];
        try {
            if (this.promotionOfConcept.split("concept:").length == 4) {
                temp = this.promotionOfConcept.substring(this.promotionOfConcept.indexOf(":") + 1)
                        .replace(" concept", "").replace("\"", "").split(":");

                this.entityCategory = temp[0];
                this.entity = temp[1];
                this.relation = temp[2];
                this.valueCategory = temp[3];
                this.value = temp[4];

            } else if (this.promotionOfConcept.split("concept:").length == 3) {
                temp = this.promotionOfConcept.substring(this.promotionOfConcept.indexOf(":") + 1)
                        .replace(" concept", "").replace("\"", "").replace(" ", ":").trim().split(":");

                this.entityCategory = temp[0];
                this.entity = temp[1];
                this.relation = temp[2];
                this.value = temp[3];

            } else {
                System.out.println("Check the MBL parameters. Something is not being processed");
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(str);
            System.out.println("EC = " + this.entityCategory);
            System.out.println("E = " + this.entity);
            System.out.println("R = " + this.relation);
            System.out.println("VC = " + this.valueCategory);
            System.out.println("V = " + this.value);
        }
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
