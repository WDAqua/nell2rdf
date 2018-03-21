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

	public PRArdf(final PRA pra, final Resource belief) {
		super(pra, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addPaths();
	}

	void addPaths() {
		getPaths().forEach(walk -> {
			Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_RELATION_PATH));
			RDFNode object = this.componentExecution.getModel().createResource(getMetadataUri(createSequentialName(RESOURCE_RELATION_PATH) + getCommonString()),
					this.componentExecution.getModel().getResource(getMetadataUri(CLASS_RELATION_PATH)));
			this.componentExecution.addProperty(predicate, object);

			final Resource path = object.asResource();

			predicate = path.getModel().getProperty(getMetadataUri(PROPERTY_DIRECTION_OF_PATH));
			object = path.getModel().createResource(getMetadataUri(walk.getsPathDirection()));
			path.addProperty(predicate, object);

			predicate = path.getModel().getProperty(getMetadataUri(PROPERTY_SCORE_OF_PATH));
			object = path.getModel().createTypedLiteral(walk.getdScore(), XSDDatatype.XSDdecimal);
			path.addProperty(predicate, object);

			final RDFList list = path.getModel().createList();
			walk.getlPath().forEach(step -> {
				final RDFNode element = list.getModel().createTypedLiteral(step);
				if (list.isEmpty()) {
					list.cons(element);
				} else {
					list.add(element);
				}
			});
			predicate = path.getModel().getProperty(getMetadataUri(PROPERTY_LIST_OF_RELATIONS));
			path.addProperty(predicate, list);
		});
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_PRA);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_PRA_EXECUTION);
	}

	List<Rule> getPaths() {
		return ((PRA) this.componentNell).getLRules();
	}

}
