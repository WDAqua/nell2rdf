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
    //public static String fileNELLcsv = "D:\\NELL.08m.990.cesv.csv";
    //public static String fileNELLcsv = "C:\\Users\\Maisa\\Desktop\\Temp para NELL\\NELL.08m.1040.cesv.csv";
    public static String fileNELLcsv = "C:\\Users\\Maisa\\Desktop\\Temp para NELL\\NELL.08m.1075.esv.csv";
    //public static String fileOut = "C:\\Users\\Maisa\\Desktop\\Temp para NELL\\teste_2407";
    public static String fileOutToString = "D:\\fileOutToString_2407";
    public static void main(String[] args) throws IOException, ParseException {

        ManipulationExecution KBManip = new ManipulationExecution();
        KBManip.readNELLcsv(fileNELLcsv);
    }
}
