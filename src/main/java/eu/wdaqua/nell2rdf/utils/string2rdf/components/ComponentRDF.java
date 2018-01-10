package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.Header;
import eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class ComponentRDF {
	
	//private final Logger		log						= Logger.getLogger(this.getClass().getName());
	
	Header componentNell;
	Resource belief;
	Resource componentExecution;
	
	String	executionName = null,
			tokenName = null;
	
	public ComponentRDF(final Header componentNell, final Resource belief) {
		this.componentNell = componentNell;
		this.belief = belief;
	}
	
	public void addTriples () {
        componentExecution = createComponentExecution(belief);  
        addComponentName();
        addTime();
        addIteration();
        addProbability();
        addSource();
        addToken();
	}
	
	Resource createComponentExecution(final Resource resource) {
		Property predicate = resource.getModel().getProperty(UriNell.PROPERTY_GENERATED_BY);
        RDFNode object = resource.getModel().createResource(getExecutionName(), resource.getModel().getResource(getExecutionType()));
        resource.addProperty(predicate,object);
        return object.asResource();
	}
	
	void addComponentName() {
		if (getComponentName() != null) {
			Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_ASSOCIATED_WITH);
	        RDFNode object = componentExecution.getModel().getResource(getComponentName());
	        componentExecution.addProperty(predicate, object);
		}
	}
	
	void addTime() {
		Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_AT_TIME);
        RDFNode object = componentExecution.getModel().createTypedLiteral(getDateTime(),XSDDatatype.XSDdateTime);
        componentExecution.addProperty(predicate, object);
	}
	
	void addIteration () {
            Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_ITERATION);
            RDFNode object = componentExecution.getModel().createTypedLiteral(getIteration(), XSDDatatype.XSDnonNegativeInteger);
            componentExecution.addProperty(predicate, object);
	}
	
	void addProbability () {
		if (getProbability() != null) { // promoted beliefs have no probability for the components
            Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_PROBABILITY);
            RDFNode object = componentExecution.getModel().createTypedLiteral(getProbability(), XSDDatatype.XSDdecimal);
            componentExecution.addProperty(predicate,object);
        }
	}
	
	void addSource() {
		if (getStringSource() != null) {
			Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_SOURCE);
			RDFNode object = componentExecution.getModel().createTypedLiteral(getStringSource(), XSDDatatype.XSDstring);
            componentExecution.addProperty(predicate, object);
        }
	}
	
	void addToken() {
		if (getTokenRelation() != null) {
			RDFNode token = componentExecution.getModel().createResource(getTokenName(), componentExecution.getModel().getResource(getTokenClass()));
			
			Property predicate = token.getModel().getProperty(UriNell.PROPERTY_TOKE_ENTITY);
	        RDFNode object = token.getModel().createTypedLiteral(getTokenEntity(), XSDDatatype.XSDstring);
	        token.asResource().addProperty(predicate, object);
	        
	        predicate = token.getModel().getProperty(getTokenRelation());
	        object = token.getModel().createTypedLiteral(getTokenValue(), XSDDatatype.XSDstring);
	        token.asResource().addProperty(predicate, object);
	        
	        predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_TOKEN);
	        componentExecution.addProperty(predicate, token);
		}
    }
	
	String getExecutionType() {
		return UriNell.CLASS_COMPONENT_EXECUTION;
	}
	
	String getComponentType() {
		return UriNell.CLASS_COMPONENT;
	}

	String getExecutionName() {
		if (executionName == null) {
			executionName = UriNell.PREFIX + UriNell.NAMESPACE_END_METADATA + "Execution" + getCommonString();
		}
		return executionName;
	}
	
	String getComponentName() {
		return null;
	}
	
	String getDateTime() {
		return componentNell.getDateTime();
	}
	
	int getIteration() {
		return componentNell.getIteration();
	}
	
	Double getProbability() {
		return componentNell.getProbability();
	}
	
	String getStringSource() {
		return componentNell.getStringSource() != null ? componentNell.getStringSource().trim() : null;
	}
	
	String getTypeToken() {
		return componentNell.getFormatHeader().getTypeKB();
	}
	
	String getTokenName() {
		if (tokenName == null) {
			if (componentNell.getFormatHeader().getTypeKB() == ConstantList.RELATION) {
				tokenName = UriNell.RESOURCE_TOKEN_RELATION + getCommonString();
			} else if (componentNell.getFormatHeader().getTypeKB() == ConstantList.CATEGORY) {
				tokenName = UriNell.RESOURCE_TOKEN_GENERALIZATION + getCommonString();
			}
		}
		return tokenName;
	}
	
	String getTokenClass() {
		String tokenClass = null;
			if (componentNell.getFormatHeader().getTypeKB() == ConstantList.RELATION) {
				tokenClass = UriNell.CLASS_TOKEN_RELATION;
			} else if (componentNell.getFormatHeader().getTypeKB() == ConstantList.CATEGORY) {
				tokenClass = UriNell.CLASS_TOKEN_GENERALIZATION;
			}
		return tokenClass;
	}
	
	String getTokenRelation() {
		String tokenRelation = null;
		if (componentNell.getFormatHeader().getTypeKB() == ConstantList.RELATION) {
			tokenRelation = UriNell.PROPERTY_RELATION_VALUE;
		} else if (componentNell.getFormatHeader().getTypeKB() == ConstantList.CATEGORY) {
			tokenRelation = UriNell.PROPERTY_GENERALIZATION_VALUE;
		}
		return tokenRelation;
	}
	
	String getTokenEntity() {
		return componentNell.getFormatHeader().getTokenElement1();
	}
	
	String getTokenValue() {
		return componentNell.getFormatHeader().getTokenElement2();
	}
	
	String getCommonString() {
		return  "_" + componentNell.getComponentName() + "_"  + belief.getLocalName() + "_" + getIteration();
	}
}
