/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.test;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 * @author Maisa
 */
public class Main {

   //public static String fileNELLcsv = "D:\\Google-Drive\\PG\\RDF_NELL\\Teste_NELL.08m.1045.esv.csv";
    //public static String fileNELLcsv = "D:\\Google-Drive\\PG\\NELL2RDF\\NELL.08m.1050.esv.csv";
    //public static String fileNELLcsv = "D:\\NELL.08m.1050.cesv.csv";
    public static String fileNELLcsv = "D:\\NELL.08m.990.cesv.csv";
    //public static String fileNELLcsv = "D:\\teste.txt";
    public static String fileOut = "D:\\Google-Drive\\PG\\NELL2RDF\\";
    public static String fileOutToString = "D:\\fileOutToString";

    public static void main(String[] args) throws IOException, ParseException {

        ManipulationExecution KBManip = new ManipulationExecution();
        KBManip.readNELLcsv(fileNELLcsv, fileOut);
    }
}
