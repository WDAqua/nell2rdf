package eu.wdaqua.nell2rdf.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.jena.rdf.model.Model;

public class Write {
	private Model model;
	private String outputFile;

	public Write(Model model, String outputFile) {
		this.model = model;
		this.outputFile = outputFile;
	}

	public void write() {
		File fileTurtle = new File(this.outputFile);
		try {
			OutputStream outTurtle = new FileOutputStream(fileTurtle);
			this.model.write(outTurtle, "Turtle");
			outTurtle.flush();
			outTurtle.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
