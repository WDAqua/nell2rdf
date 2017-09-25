/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Maisa
 */
public final class LineInstanceJOIN {

    private boolean candidate;
    public static String completeLine;
    static String CAT_OR_REL;

    private final String entity;
    private final String relation;
    private final String value;

    private List<Integer> nrIterations = new ArrayList<>();
    private List<Double> probability;

    private int nrIterationsInt;
    private double probabilityDouble;

    //Object Responsable for the Source Column
    private final String source;
    private MBL_OR_ERC MBL_source;

    //Object Responsable for the Source Column
    private List<String> entityLiteralStrings;
    private List<String> valueLiteralStrings;
    private List<String> bestEntityLiteralString;
    private List<String> bestValueLiteralString;
    private List<String> categoriesForEntity;
    private List<String> categoriesForValue;

    private final String candidateSource;
    private Map<String, Header> listComponents;

    public void inicilizeObjets() {
        this.entityLiteralStrings = new ArrayList<>();
        this.valueLiteralStrings = new ArrayList<>();
        this.bestEntityLiteralString = new ArrayList<>();
        this.bestValueLiteralString = new ArrayList<>();
        this.categoriesForEntity = new ArrayList<>();
        this.categoriesForValue = new ArrayList<>();
        this.nrIterations = new ArrayList<>();
        this.probability = new ArrayList<>();
        this.listComponents = new HashMap<>();
    }

    public String organizeStringsExtraction(String str) {
        str = str.trim();
        if (!str.isEmpty()) {
            try {
                str = str.replace("\" ", "\", ");
                str = str.replace("\"", "").trim();
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("fa");
            }
        }
        return str;
    }

    public LineInstanceJOIN(String Entity, String Relation, String Value, String Iteration,
            String probabilityPROMOTION, String Source, String EntityLiteralStrings,
            String ValueLiteralStrings, String BestEntityLiteralString, String BestValueLiteralString,
            String CategoriesForEntity, String CategoriesForValue, String CandidatSource, String CompleteLine) {

        this.inicilizeObjets();

        String tempMBLorERC = "";
        this.source = Source;

        this.entity = Entity;
        this.relation = Relation;
        this.value = Value;

        if (relation.contains(ConstantList.LOOK_GENERALIZATIONS)) {
            CAT_OR_REL = ConstantList.CATEGORY;
        } else {
            CAT_OR_REL = ConstantList.RELATION;
        }

        if (!(this.source.contains("null"))) {
            if (Source.contains("EntityResolverCleanup-Iter:")) {
                tempMBLorERC = "EntityResolverCleanup";
            } else if (Source.contains("MBL-Iter:")) {
                tempMBLorERC = "MBL";
            } else {
                System.out.println(Source);
            }
            this.MBL_source = new MBL_OR_ERC(Source, tempMBLorERC);
        }
        
        IsCandidate(probabilityPROMOTION);

        if (candidate) {
            this.setProbability(probabilityPROMOTION);
            this.setIterations(Iteration);
        } else {
            this.probabilityDouble = Double.valueOf(probabilityPROMOTION.trim());
            this.nrIterationsInt = Integer.valueOf(Iteration.trim());
            this.probability = null;
        }
        this.entityLiteralStrings.addAll(Arrays.asList(organizeStringsExtraction(EntityLiteralStrings).split(",")));
        this.valueLiteralStrings.addAll(Arrays.asList(organizeStringsExtraction(ValueLiteralStrings).split(",")));
        this.bestEntityLiteralString.addAll(Arrays.asList(organizeStringsExtraction(BestEntityLiteralString).split(",")));
        this.bestValueLiteralString.addAll(Arrays.asList(organizeStringsExtraction(BestValueLiteralString).split(",")));
        this.categoriesForEntity.addAll(Arrays.asList(organizeStringsExtraction(CategoriesForEntity).split(",")));
        this.categoriesForValue.addAll(Arrays.asList(organizeStringsExtraction(CategoriesForValue).split(",")));

        LineInstanceJOIN.completeLine = CompleteLine;

        this.candidateSource = CandidatSource;

        this.setListComponents(Utility.getSTRperComponents(Utility.getCandidateSource(this.candidateSource)), this.probability);

    }

    public Map<String, Header> getListComponents() {
        return listComponents;
    }

    public List<Double> getProbability() {
        return probability;
    }

    public String getEntity() {
        return entity;
    }

    public String getRelation() {
        return relation;
    }

    public String getValue() {
        return value;
    }

    public List<Integer> getNrIterations() {
        return nrIterations;
    }

    public void IsCandidate(String temp) {
        candidate = temp.contains("[");
    }

    public int getNrIterationsInt() {
        return nrIterationsInt;
    }

    public double getProbabilityDouble() {
        return probabilityDouble;
    }

    public void setProbability(String str) {
        String temp = str.replace("]", "").replace("[", "");
        if (temp.contains(",")) {
            String strSplit[];
            strSplit = temp.split(", ");
            for (String srtTemp : strSplit) {
                this.probability.add(Double.valueOf(srtTemp));
            }
        } else {
            this.probability.add(Double.valueOf(temp));
        }
    }

    public void setIterations(String str) {

        String temp = str.replace("]", "").replace("[", "");
        if (temp.contains(",")) {
            String strSplit[];
            strSplit = temp.split(", ");
            for (String srtTemp : strSplit) {
                this.nrIterations.add(Integer.valueOf(srtTemp));
            }
        } else {
            this.nrIterations.add(Integer.valueOf(temp));
        }
    }

    //Macarronada Italiana
    //Here is where the componentes are created; [ aqui
    public void setListComponents(List<String> stringListComponents, List<Double> probList) {
        double tempProbility = 0;

        for (int i = 0; i < stringListComponents.size(); i++) {

            if (probList == null) {
                tempProbility = 0.0;
            } else {
                try {
                    tempProbility = probList.get(i);
                } catch (IndexOutOfBoundsException e) {
                    System.out.print(e);
                }
            }

            String line = stringListComponents.get(i);

            //NAMESPACE_ONTOLOGY MODIFIER
            if (line.startsWith(ConstantList.TEXT_ONTOLOGYMODIFIER)) {
                this.listComponents.put(ConstantList.ONTOLOGYMODIFIER, new OntologyModifier(line, tempProbility));
                //NAMESPACE_ONTOLOGY CPL
            } else if (line.startsWith(ConstantList.TEXT_CPL)) {
                this.listComponents.put(ConstantList.CPL, new CPL(line, tempProbility));
                //SEAL
            } else if (line.startsWith(ConstantList.TEXT_SEAL)) {
                this.listComponents.put(ConstantList.SEAL, new SEAL(line, tempProbility));
                //OPEN EVAL
            } else if (line.startsWith(ConstantList.TEXT_OE)) {
                this.listComponents.put(ConstantList.OE, new OE(line, tempProbility));
                //CMU
            } else if (line.startsWith(ConstantList.TEXT_CMC)) {
                this.listComponents.put(ConstantList.CMC, new CMC(line, tempProbility));
                //ALIAS MATCHER
            } else if (line.startsWith(ConstantList.TEXT_ALIASMATCHER)) {
                this.listComponents.put(ConstantList.ALIASMATCHER, new AliasMatcher(line, tempProbility));
                //MBL
            } else if (line.startsWith(ConstantList.TEXT_MBL)) {
                this.listComponents.put(ConstantList.MBL, new MBL(line, tempProbility));
                //PRA
            } else if (line.startsWith(ConstantList.TEXT_PRA)) {
                this.listComponents.put(ConstantList.PRA, new PRA(line, tempProbility));
                //RULE INFERENCE
            } else if (line.startsWith(ConstantList.TEXT_RULEINFERENCE)) {
                this.listComponents.put(ConstantList.RULEINFERENCE, new RuleInference(line, tempProbility));
                //KB MANIPULATION
            } else if (line.startsWith(ConstantList.TEXT_KBMANIPULATION)) {
                this.listComponents.put(ConstantList.KBMANIPULATION, new KbManipulation(line, tempProbility));
                //SEMPARSE
            } else if (line.startsWith(ConstantList.TEXT_SEMPARSE)) {
                this.listComponents.put(ConstantList.SEMPARSE, new Semparse(line, tempProbility));
                //LE
            } else if (line.startsWith(ConstantList.TEXT_LE)) {
                this.listComponents.put(ConstantList.LE, new LE(line, tempProbility));
                //SPREADSHEET EDITS
            } else if (line.startsWith(ConstantList.TEXT_SPREADSHEETEDITS)) {
                this.listComponents.put(ConstantList.SPREADSHEETEDITS, new SpreadsheetEdits(line, tempProbility));
                //LATLONG & LATLONGTT
            } else if (line.startsWith(ConstantList.TEXT_LATLONG)) {
                this.listComponents.put(ConstantList.LATLONG, new LatLong(line, ConstantList.LATLONG, tempProbility));
            } else if (line.startsWith(ConstantList.TEXT_LATLONGTT)) {
                this.listComponents.put(ConstantList.LATLONGTT, new LatLong(line, ConstantList.LATLONGTT, tempProbility));
            }
        }
    }
    /*   
     public JSONObject setColumnsJSON() {
        JSONObject joColumns = new JSONObject();
        joColumns.put(ConstantList.ENTITY_CSV, this.entity);
        joColumns.put(ConstantList.RELATION_CSV, this.relation);
        joColumns.put(ConstantList.VALUE_CSV, this.value);
        if (!candidate) {
            joColumns.put(ConstantList.ITERATION_CSV, this.nrIterationsInt);
            joColumns.put(ConstantList.PROBABILITY_CSV, this.probabilityDouble);
        } else {
            JSONArray jArrayTempIteration = new JSONArray();

            for (int i = 0; i < this.nrIterations.size(); i++) {
                jArrayTempIteration.add(this.nrIterations.get(i));
            }
            joColumns.put(ConstantList.ITERATION_CSV, jArrayTempIteration);

            JSONArray jArrayTempProbabilty = new JSONArray();
            for (int i = 0; i < this.probability.size(); i++) {
                jArrayTempProbabilty.add(this.probability.get(i));
            }
            joColumns.put(ConstantList.PROBABILITY_CSV, jArrayTempProbabilty);
        }

        joColumns.put(ConstantList.SOURCE_CSV, this.source);
        joColumns.put(ConstantList.ENTITY_LS_CSV, this.entityLiteralStrings);
        joColumns.put(ConstantList.VALUE_LS_CSV, this.valueLiteralStrings);
        joColumns.put(ConstantList.BEST_ENTITY_CSV, this.bestEntityLiteralString);
        joColumns.put(ConstantList.BEST_VALUE_CVS, this.bestValueLiteralString);
        joColumns.put(ConstantList.CATEGORIES_ENTITY_CSV, this.categoriesForEntity);
        joColumns.put(ConstantList.CATEGORIES_VALUE_CSV, this.categoriesForValue);

        return joColumns;
    }*/
}
