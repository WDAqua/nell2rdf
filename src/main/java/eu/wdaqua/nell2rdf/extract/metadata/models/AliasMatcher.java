/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.*;
import java.time.LocalDate;


/**
 *
 * @author Maisa
 */
public class AliasMatcher extends Header {

    //[Freebase 7/9/2012] mudando para 9/7/2012
    private LocalDate FreebaseDate;

    public AliasMatcher(String str, Double Probability) {
        super(str, ALIASMATCHER, Probability);
    }

    @Override
    public void processStringText(String str) {
        this.FreebaseDate = Utility.setDateTimeFormatFreebase(Utility.getAliasMatcherFreebase(str));
    }

    public LocalDate getMetadata_FreebaseDate() {
        return FreebaseDate;
    }

    @Override
    public String toString() {
        return super.toString() + " " + getMetadata_FreebaseDate() + "]";
    }

    @Override
    public String getStringSource() {
        return this.FreebaseDate.toString();
    }
}
