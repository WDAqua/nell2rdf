package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.Header;
import eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class ComponentRDF {

	// private final Logger log = Logger.getLogger(this.getClass().getName());

	Header		componentNell;
	Resource	belief;
	Resource	componentExecution;

	String		executionName	= null,
			tokenName = null;

	public ComponentRDF(final Header componentNell, final Resource belief) {
		this.componentNell = componentNell;
		this.belief = belief;
	}

	public void addTriples() {
		this.componentExecution = createComponentExecution(this.belief);
		addComponentName();
		addTime();
		addIteration();
		addProbability();
		addSource();
		addToken();
	}

	Resource createComponentExecution(final Resource resource) {
		final Property predicate = resource.getModel().getProperty(getMetadataUri(PROPERTY_GENERATED_BY));
		final RDFNode object = resource.getModel().createResource(getExecutionName(), resource.getModel().getResource(getMetadataUri(getExecutionType())));
		resource.addProperty(predicate, object);
		return object.asResource();
	}

	void addComponentName() {
		if (getComponentName() != null) {
			final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_ASSOCIATED_WITH));
			final RDFNode object = this.componentExecution.getModel().getResource(getMetadataUri(getComponentName()));
			this.componentExecution.addProperty(predicate, object);
		}
	}

	void addTime() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_AT_TIME));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getDateTime(), XSDDatatype.XSDdateTime);
		this.componentExecution.addProperty(predicate, object);
	}

	void addIteration() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_ITERATION));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getIteration(), XSDDatatype.XSDnonNegativeInteger);
		this.componentExecution.addProperty(predicate, object);
	}

	void addProbability() {
		if (getProbability() != null) { // promoted beliefs have no probability for the components
			final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_PROBABILITY));
			final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getProbability(), XSDDatatype.XSDdecimal);
			this.componentExecution.addProperty(predicate, object);
		}
	}

	void addSource() {
		if (getStringSource() != null) {
			final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_SOURCE));
			final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getStringSource(), XSDDatatype.XSDstring);
			this.componentExecution.addProperty(predicate, object);
		}
	}

	void addToken() {
		if (getTokenRelation() != null) {
			final RDFNode token = this.componentExecution.getModel().createResource(getTokenName(), this.componentExecution.getModel().getResource(getMetadataUri(getTokenClass())));

			Property predicate = token.getModel().getProperty(getMetadataUri(PROPERTY_TOKE_ENTITY));
			RDFNode object = token.getModel().createTypedLiteral(getTokenEntity(), XSDDatatype.XSDstring);
			token.asResource().addProperty(predicate, object);

			predicate = token.getModel().getProperty(getTokenRelation());
			object = token.getModel().createTypedLiteral(getTokenValue(), XSDDatatype.XSDstring);
			token.asResource().addProperty(predicate, object);

			predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_TOKEN));
			this.componentExecution.addProperty(predicate, token);
		}
	}

	String getExecutionType() {
		return getMetadataUri(CLASS_COMPONENT_EXECUTION);
	}

	String getComponentType() {
		return getMetadataUri(CLASS_COMPONENT);
	}

	String getExecutionName() {
		if (this.executionName == null) {
			this.executionName = getMetadataUri("Execution" + getCommonString());
		}
		return this.executionName;
	}

	String getComponentName() {
		return null;
	}

	String getDateTime() {
		return this.componentNell.getDateTime().replace('/', '-').replace(' ', 'T');
	}

	int getIteration() {
		return this.componentNell.getIteration();
	}

	Double getProbability() {
		return this.componentNell.getProbability();
	}

	String getStringSource() {
		return this.componentNell.getStringSource() != null ? this.componentNell.getStringSource().trim() : null;
	}

	String getTypeToken() {
		return this.componentNell.getFormatHeader().getTypeKB();
	}

	String getTokenName() {
		if (this.tokenName == null) {
			if (this.componentNell.getFormatHeader().getTypeKB() == ConstantList.RELATION) {
				this.tokenName = getMetadataUri(RESOURCE_TOKEN_RELATION + getCommonString());
			} else if (this.componentNell.getFormatHeader().getTypeKB() == ConstantList.CATEGORY) {
				this.tokenName = getMetadataUri(RESOURCE_TOKEN_GENERALIZATION + getCommonString());
			}
		}
		return this.tokenName;
	}

	String getTokenClass() {
		String tokenClass = null;
		if (this.componentNell.getFormatHeader().getTypeKB() == ConstantList.RELATION) {
			tokenClass = getMetadataUri(CLASS_TOKEN_RELATION);
		} else if (this.componentNell.getFormatHeader().getTypeKB() == ConstantList.CATEGORY) {
			tokenClass = getMetadataUri(CLASS_TOKEN_GENERALIZATION);
		}
		return tokenClass;
	}

	String getTokenRelation() {
		String tokenRelation = null;
		if (this.componentNell.getFormatHeader().getTypeKB() == ConstantList.RELATION) {
			tokenRelation = getMetadataUri(PROPERTY_RELATION_VALUE);
		} else if (this.componentNell.getFormatHeader().getTypeKB() == ConstantList.CATEGORY) {
			tokenRelation = getMetadataUri(PROPERTY_GENERALIZATION_VALUE);
		}
		return tokenRelation;
	}

	String getTokenEntity() {
		return this.componentNell.getFormatHeader().getTokenElement1();
	}

	String getTokenValue() {
		return this.componentNell.getFormatHeader().getTokenElement2();
	}

	String getCommonString() {
		return "_" + this.componentNell.getComponentName() + "_" + this.belief.getLocalName() + "_" + getIteration();
	}
}
