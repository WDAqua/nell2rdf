package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import eu.wdaqua.nell2rdf.extract.metadata.models.AliasMatcher;
import eu.wdaqua.nell2rdf.extract.metadata.models.CMC;
import eu.wdaqua.nell2rdf.extract.metadata.models.CPL;
import eu.wdaqua.nell2rdf.extract.metadata.models.Header;
import eu.wdaqua.nell2rdf.extract.metadata.models.KbManipulation;
import eu.wdaqua.nell2rdf.extract.metadata.models.LE;
import eu.wdaqua.nell2rdf.extract.metadata.models.LatLong;
import eu.wdaqua.nell2rdf.extract.metadata.models.MBL;
import eu.wdaqua.nell2rdf.extract.metadata.models.OE;
import eu.wdaqua.nell2rdf.extract.metadata.models.OntologyModifier;
import eu.wdaqua.nell2rdf.extract.metadata.models.PRA;
import eu.wdaqua.nell2rdf.extract.metadata.models.RuleInference;
import eu.wdaqua.nell2rdf.extract.metadata.models.SEAL;
import eu.wdaqua.nell2rdf.extract.metadata.models.Semparse;
import eu.wdaqua.nell2rdf.extract.metadata.models.SpreadsheetEdits;
import eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList;

public class ComponentRDFBuilder {
	
	private static Logger		log						= Logger.getLogger(ComponentRDFBuilder.class);
	
	private ComponentRDFBuilder() {
		// Static class
	}
	
	public static ComponentRDF build(Header componentNell, Resource resource) {
		final ComponentRDF componentRdf;
		switch (componentNell.getComponentName()) {
		case ConstantList.ALIASMATCHER:
			componentRdf = new AliasMatcherRDF((AliasMatcher) componentNell, resource);
			break;
		case ConstantList.CMC:
			componentRdf = new CMCrdf((CMC) componentNell, resource);
			break;
		case ConstantList.CPL:
			componentRdf = new CPLrdf((CPL) componentNell, resource);
			break;
		case ConstantList.KBMANIPULATION:
			componentRdf = new KbManipulationRDF((KbManipulation) componentNell, resource);
			break;
		case ConstantList.LATLONG:
		case ConstantList.LATLONGTT:
			componentRdf = new LatLongRDF((LatLong) componentNell, resource);
			break;
		case ConstantList.LE:
			componentRdf = new LErdf((LE) componentNell, resource);
			break;
		case ConstantList.MBL:
			componentRdf = new MBLrdf((MBL) componentNell, resource);
			break;
		case ConstantList.OE:
			componentRdf = new OErdf((OE) componentNell, resource);
			break;
		case ConstantList.ONTOLOGYMODIFIER:
			componentRdf = new OntologyModifierRDF((OntologyModifier) componentNell, resource);
			break;
		case ConstantList.PRA:
			componentRdf = new PRArdf((PRA) componentNell, resource);
			break;
		case ConstantList.RULEINFERENCE:
			componentRdf = new RLrdf((RuleInference) componentNell, resource);
			break;
		case ConstantList.SEAL:
			componentRdf = new SEALrdf((SEAL) componentNell, resource);
			break;
		case ConstantList.SEMPARSE:
			componentRdf = new SemparseRDF((Semparse) componentNell, resource);
			break;
		case ConstantList.SPREADSHEETEDITS:
			componentRdf = new SpreadsheetEditsRDF((SpreadsheetEdits) componentNell, resource);
			break;
		default:
			log.warn("It was not possible to identify the type of component " + componentNell.getComponentName() + ". Extracting only generic data.");
			componentRdf = new ComponentRDF(componentNell, resource);
		}
		return componentRdf;
	}

}
