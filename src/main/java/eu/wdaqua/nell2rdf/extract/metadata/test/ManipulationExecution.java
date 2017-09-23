/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.test;

import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import eu.wdaqua.nell2rdf.extract.metadata.models.AliasMatcher;
import eu.wdaqua.nell2rdf.extract.metadata.models.CMC;
import eu.wdaqua.nell2rdf.extract.metadata.models.CPL;
import eu.wdaqua.nell2rdf.extract.metadata.models.KbManipulation;
import eu.wdaqua.nell2rdf.extract.metadata.models.LE;
import eu.wdaqua.nell2rdf.extract.metadata.models.LatLong;
import eu.wdaqua.nell2rdf.extract.metadata.models.LineInstanceJOIN;
import eu.wdaqua.nell2rdf.extract.metadata.models.MBL;
import eu.wdaqua.nell2rdf.extract.metadata.models.OE;
import eu.wdaqua.nell2rdf.extract.metadata.models.OntologyModifier;
import eu.wdaqua.nell2rdf.extract.metadata.models.PRA;
import eu.wdaqua.nell2rdf.extract.metadata.models.RuleInference;
import eu.wdaqua.nell2rdf.extract.metadata.models.SEAL;
import eu.wdaqua.nell2rdf.extract.metadata.models.Semparse;
import eu.wdaqua.nell2rdf.extract.metadata.models.SpreadsheetEdits;
import eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList;
import eu.wdaqua.nell2rdf.extract.metadata.models.Header;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maisa
 */
public class ManipulationExecution {

    private LineInstanceJOIN LI;

    public void setFeatures(String line, boolean theresCandidate) {

        String[] split = line.split("\t");
        LI = new LineInstanceJOIN(split[0], split[1], split[2], split[3], split[4],
                Utility.DecodeURL(split[5]), split[6], split[7],
                split[8], split[9], split[10],
                split[11], Utility.DecodeURL(split[12]), line, theresCandidate);
    }

    public void readNELLcsv(String pathIN) throws FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader(pathIN));
        String line = reader.readLine();
        System.out.println(line);
        System.out.println("Iniciando processamento");

        StringBuffer temp = new StringBuffer();

        boolean theresCandidate = false;

        while ((line = reader.readLine()) != null) {

            //System.out.println(line);
            setFeatures(line, theresCandidate);
            Map<String, Object> p = LI.getListComponents();

            temp.append("START: \t");
            p.entrySet().forEach((pair) -> {
                int index = 0;
                String key = pair.getKey();
                temp.append("COMPONENT: ").append(key).append("\t");
               // System.out.println(((Header) pair.getValue()).getDateTime());
                switch (key) {
                    case ConstantList.ONTOLOGYMODIFIER:
                        temp.append(((OntologyModifier) pair.getValue()).getMetadata_From());
                        break;
                    case ConstantList.CPL:
                        temp.append(((CPL) pair.getValue()).getMetadata_MapTPOccurence().toString());
                        /*JSON_CPL jsonCPL = new JSON_CPL(pair.getValue());
                        jsonCPL.setJsonObject();

                         {
                            try {
                                Utility.writeJsonFile(jsonCPL.getJsonObjectMain(), Main.fileOutToString + "teste", true);
                            } catch (IOException ex) {
                                System.out.println("Erro para escrever um anta duma linha" + ex);
                            }
                        }*/
                        break;
                    case ConstantList.SEAL:
                        temp.append(((SEAL) pair.getValue()).getMedatadata_URLList().toString());
                        break;
                    case ConstantList.OE:
                        temp.append(((OE) pair.getValue()).getMetadata_mapTextURL().toString());
                        break;
                    case ConstantList.CMC:
                        index = ((CMC) pair.getValue()).getMetadata_CmcList().size();
                        while (index > 0) {
                            index--;
                            temp.append(((CMC) pair.getValue()).getMetaData_CMCObjectFieldName(index))
                                    .append("\t").append(((CMC) pair.getValue()).getMetaData_CMCObjectFieldValue(index))
                                    .append("\t").append(((CMC) pair.getValue()).getMetaData_CMCObjectScore(index));
                        }
                        break;
                    case ConstantList.ALIASMATCHER:
                        temp.append(((AliasMatcher) pair.getValue()).getMetadata_FreebaseDate());
                        break;
                    case ConstantList.MBL:
                        temp.append(((MBL) pair.getValue()).getMetadata_Entity()).append(",")
                                .append(((MBL) pair.getValue()).getMetadata_EntityCategory()).append(",")
                                 .append(((MBL) pair.getValue()).getMetadata_Relation()).append(",")
                                 .append(((MBL) pair.getValue()).getMetadata_Value()).append(",")
                                 .append(((MBL) pair.getValue()).getMetadata_ValueCategory());
                        break;
                    case ConstantList.PRA:
                        while (index > 0) {
                            index--;
                            temp.append(((PRA) pair.getValue()).getLRules());
                        }
                        break;
                    case ConstantList.RULEINFERENCE:
                        temp.append(((RuleInference) pair.getValue()).getMetaData_dAccuracy())
                                .append("\t").append(((RuleInference) pair.getValue()).getMetaData_dAccuracy())
                                .append("\t").append(((RuleInference) pair.getValue()).getMetaData_iNrCorrectionEstimation())
                                .append("\t").append(((RuleInference) pair.getValue()).getMetaData_iNrIncorrectEstimation())
                                .append("\t").append(((RuleInference) pair.getValue()).getMetaData_iNrTrainingAssertation())
                                .append("\t").append(((RuleInference) pair.getValue()).getMetaData_lRefRule().toString())
                                .append("\t").append(((RuleInference) pair.getValue()).getMetaData_lValuesRule().toString());

                        break;
                    case ConstantList.KBMANIPULATION:
                        temp.append(((KbManipulation) pair.getValue()).getMetadata_oldBug());
                        break;
                    case ConstantList.SEMPARSE:
                        temp.append(((Semparse) pair.getValue()).getMetadata_SentenceList().toString());
                        break;
                    case ConstantList.LE:
                        temp.append(((LE) pair.getValue()).getMetadata_StringSource());
                        break;
                    case ConstantList.SPREADSHEETEDITS:
                        temp.append(((SpreadsheetEdits) pair.getValue()).getMetadata_Action())
                                .append(((SpreadsheetEdits) pair.getValue()).getMetadata_Entity())
                                .append(((SpreadsheetEdits) pair.getValue()).getMetadata_From())
                                .append(((SpreadsheetEdits) pair.getValue()).getMetadata_Relation())
                                .append(((SpreadsheetEdits) pair.getValue()).getMetadata_UserFeedback())
                                .append(((SpreadsheetEdits) pair.getValue()).getMetadata_Value());
                        break;
                    case ConstantList.LATLONG:
                    case ConstantList.LATLONGTT:

                        index = ((LatLong) pair.getValue()).getMetadata_Lrules().size();
                        while (index > 0) {
                            index--;
                            temp.append(((LatLong) pair.getValue()).getMetadata_Phrase(index))
                                    .append("\t").append(((LatLong) pair.getValue()).getMetadata_Phrase(index))
                                    .append("\t").append(((LatLong) pair.getValue()).getMetadata_Value1(index))
                                    .append("\t").append(((LatLong) pair.getValue()).getMetadata_Value2(index));
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid Component Name: " + key);
                }

                temp.append("\t");

            }
            );
            temp.append("END").append("\n");
            if (temp.length() > 10000000) {
                try {
                    Utility.writeStringBuffer(temp, Main.fileOutToString, true);
                } catch (IOException ex) {
                    Logger.getLogger(LineInstanceJOIN.class.getName()).log(Level.SEVERE, null, ex);
                }
                temp.delete(0, temp.length());
            }
        }

        try {
            Utility.writeStringBuffer(temp, Main.fileOutToString, true);
        } catch (IOException ex) {
            Logger.getLogger(LineInstanceJOIN.class.getName()).log(Level.SEVERE, null, ex);
        }
        temp.delete(0, temp.length());

        System.out.println("Finalizado");

    }
/*
    public void readNELLcsvTESTE(String pathIN, String pathOUT) throws FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader(pathIN));
        String line = reader.readLine();
        System.out.println(line);
        System.out.println("Iniciando processamento");

        StringBuffer temp = new StringBuffer();

        JSONArray jsonArrayIntern = null;

        temp.append("{\"line\":[");

        boolean theresCandidate = false;

        while ((line = reader.readLine()) != null) {

            //System.out.println(line);
            setFeatures(line, theresCandidate);
            Map<String, Object> p = LI.getListComponents();

            jsonArrayIntern = new JSONArray();
            jsonArrayIntern.add(LI.setColumnsJSON());
            for (Map.Entry<String, Object> pair : p.entrySet()) {

                String key = pair.getKey();
                System.out.println(((Header) pair.getValue()).getComponentName());
                switch (key) {
                    case ConstantList.CPL:
                        //System.out.println(LineInstanceJOIN.completeLine);

                        JSON_CPL jsonCPL = new JSON_CPL(pair.getValue());
                        jsonCPL.setJsonObject();
                        jsonArrayIntern.add(jsonCPL.getJsonObjectMain());
                        break;
                    default:
                    // throw new IllegalArgumentException("Invalid Component Name: " + key);
                }
            }

            temp.append(jsonArrayIntern.toString()).append(",");

            if (temp.length() > 2) {
                jsonArrayIntern = new JSONArray();
                try {
                    Utility.writeStringBuffer(temp, Main.fileOutToString, true);
                } catch (IOException ex) {
                    Logger.getLogger(LineInstanceJOIN.class.getName()).log(Level.SEVERE, null, ex);
                }
                temp.delete(0, temp.length());
            }
        }
        temp.append(jsonArrayIntern.toString()).append("]}");

        try {
            Utility.writeStringBuffer(temp, Main.fileOutToString, true);
        } catch (IOException ex) {
            Logger.getLogger(LineInstanceJOIN.class.getName()).log(Level.SEVERE, null, ex);
        }
        temp.delete(0, temp.length());

        System.out.println("Finalizado");

    }
*/
}
