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
public class SpreadsheetEdits extends Header {

 private String userFeedback;
    private String entity;
    private String relation;
    private String value;
    private String action;
    private String from;

    public SpreadsheetEdits(String str, double Probability) {
        super(str, SPREADSHEETEDITS, Probability);
    }

    @Override
    public void processStringText(String str) {
        this.userFeedback = Utility.getSpreadSheetUserFeedback(str);
        this.action = Utility.getSpreadSheetAction(str);
        this.from = Utility.getSpreadSheetFrom(str);

        String tempSplit[] = Utility.getSpreadSheetERV(str, this.userFeedback).split(" ");
        try {
            this.entity = tempSplit[0];
            this.relation = tempSplit[1];
            this.value = tempSplit[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(str);
        }
    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer();
        temp.append(" (").append(this.entity).append(",").append(this.relation).append(",").append(this.value).append(")");
        temp.append("-").append(this.userFeedback).append("-");
        temp.append(" FROM: ").append(this.from);
        temp.append(" ACTION: ").append(this.action);

        return super.toString() + temp.toString() + "}";
    }

    @Override
    public String getStringSource() {
        StringBuffer temp = new StringBuffer();
        temp.append(" (").append(this.entity).append(",").append(this.relation).append(",").append(this.value).append(")");
        temp.append("-").append(this.userFeedback).append("-");
        temp.append(" FROM: ").append(this.from);
        temp.append(" ACTION: ").append(this.action);
        return temp.toString() + "}";
    }
}
