package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.PRA;
import eu.wdaqua.nell2rdf.extract.metadata.models.PRA.Rule;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class PRArdf extends ComponentRDF {

	public PRArdf(final PRA pra, Resource belief) {
		super(pra, belief);
	}

	public void addTriples () {
		super.addTriples();
		addPaths();
	}
	
	void addPaths() {
		getPaths().forEach(walk -> {
			Property predicate = componentExecution.getModel().getProperty(PROPERTY_RELATION_PATH);
			RDFNode object = componentExecution.getModel().createResource(createSequentialUri(RESOURCE_RELATION_PATH + getCommonString()), componentExecution.getModel().getResource(CLASS_RELATION_PATH));
			componentExecution.addProperty(predicate, object);
			
			Resource path = object.asResource();
			
			predicate = path.getModel().getProperty(PROPERTY_DIRECTION_OF_PATH);
			object = path.getModel().createResource(NAMESPACE_PREFIX + NAMESPACE_END_METADATA + walk.getsPathDirection());
			path.addProperty(predicate, object);
			
			predicate = path.getModel().getProperty(PROPERTY_SCORE_OF_PATH);
			object = path.getModel().createTypedLiteral(walk.getdScore(), XSDDatatype.XSDdecimal);
			path.addProperty(predicate, object);
			
			RDFList list = path.getModel().createList();
			walk.getlPath().forEach(step -> {
				RDFNode element = list.getModel().createTypedLiteral(step);
				if (list.isEmpty()) {
					list.cons(element);
				} else {
					list.add(element);
				}
			});
			predicate = path.getModel().getProperty(PROPERTY_LIST_OF_RELATIONS);
			path.addProperty(predicate, list);
		});
	}
	
	String getComponentName() {
		return RESOURCE_PRA;
	}
	
	String getExecutionType() {
		return CLASS_PRA_EXECUTION;
	}
	
	List<Rule> getPaths() {
		return ((PRA) componentNell).getLRules();
	}
	
}
