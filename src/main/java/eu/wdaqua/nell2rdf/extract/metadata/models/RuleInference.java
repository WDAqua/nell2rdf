/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.models;

import static eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList.RULEINFERENCE;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Maisa
 */
public class RuleInference extends Header {

    private String rules;
    private Rule cRules;

    public RuleInference(String str, Double Probability) {
        super(str, RULEINFERENCE, Probability);
        cRules = new Rule(str);
    }

    public List<String[]> getMetaData_lRule() {
        return cRules.lRule;
    }

    public int getMetaData_iNrCorrectionEstimation() {
        return this.cRules.iNrCorrectionEstimation;
    }

    public int getMetaData_iNrIncorrectEstimation() {
        return this.cRules.iNrIncorrectEstimation;
    }

    public int getMetaData_iNrTrainingAssertation() {
        return this.cRules.iNrTrainingAssertation;
    }

    public double getMetaData_dAccuracy() {
        return this.cRules.dAccuracy;
    }

    public List<String> getMetaData_lRefRule() {
        return this.cRules.lRefRule;
    }

    public List<String> getMetaData_lValuesRule() {
        return this.cRules.lValuesRule;
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

    private class Rule {

        //private List<subRule> lRule;
        private String[] subRule;
        private List<String[]> lRule = new ArrayList<>();

        private int iNrCorrectionEstimation;
        private int iNrIncorrectEstimation;
        private int iNrTrainingAssertation;
        private double dAccuracy;

        private List<String> lRefRule;

        private List<String> lValuesRule;

        public Rule(String str) {
            setRules(str);
            setValuesNumber(str);
            setRefRule(str);
            setValuesRule(str);
        }

        private void setRules(String str) {

            String sRules = Utility.getRuleInference_RULES_1(str);

            Pattern pattern = Pattern.compile(Utility.REGEX_RULES_2);
            Matcher matcher = pattern.matcher(sRules);

            while (matcher.find()) {
                String sTemp = matcher.group();
                String sTempSplit[] = sTemp.replace(" ", "").split(",");

                subRule = new String[3];

                subRule[0] = sTempSplit[0].trim();
                subRule[1] = sTempSplit[1].trim();
                subRule[2] = sTempSplit[2].trim();

                lRule.add(subRule);
            }
        }

        private void setValuesNumber(String str) {

            Pattern pattern = Pattern.compile(Utility.REGEX_RULES_3);
            Matcher matcher = pattern.matcher(str);

            if (matcher.find()) {
                String sTemp = matcher.group();
                String sTempSplit[] = sTemp.replace(" ", "").split(",");
                this.dAccuracy = Double.valueOf(sTempSplit[0].trim());
                this.iNrCorrectionEstimation = Integer.valueOf(sTempSplit[1].trim());
                this.iNrIncorrectEstimation = Integer.valueOf(sTempSplit[2].trim());
                this.iNrTrainingAssertation = Integer.valueOf(sTempSplit[3].trim());
            }
        }

        private void setRefRule(String str) {

            Pattern pattern = Pattern.compile(Utility.REGEX_RULES_4);
            Matcher matcher = pattern.matcher(str);

            if (matcher.find()) {
                String sTemp = matcher.group();
                String sTempSplit[] = sTemp.replace(" ", "").split(",");
                lRefRule = new ArrayList<>();
                lRefRule.addAll(Arrays.asList(sTempSplit));
            }
        }

        private void setValuesRule(String str) {

            String sTemp = Utility.getRuleInference_RULES_5(str).replace(" ", "");

            String sTempSplit[] = sTemp.split(",");
            lValuesRule = new ArrayList<>();
            lValuesRule.addAll(Arrays.asList(sTempSplit));

        }
    }
}
