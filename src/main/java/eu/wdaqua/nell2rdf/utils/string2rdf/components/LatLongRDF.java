package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import eu.wdaqua.nell2rdf.extract.metadata.models.LatLong;
import eu.wdaqua.nell2rdf.extract.metadata.models.LatLong.Rule;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class LatLongRDF extends ComponentRDF {
	
	public LatLongRDF(final LatLong latLong, Resource belief) {
		super(latLong, belief);
	}

	public void addTriples () {
		super.addTriples();
		addNameLatLongTriples();
	}
	
	void addToken() {
			RDFNode token = componentExecution.getModel().createResource(getTokenName(), componentExecution.getModel().getResource(createUri(getTokenClass())));
			
			Property predicate = token.getModel().getProperty(PROPERTY_TOKE_ENTITY);
	        RDFNode object = token.getModel().createTypedLiteral(getTokenEntity(), XSDDatatype.XSDstring);
	        token.asResource().addProperty(predicate, object);
	        
	        predicate = token.getModel().getProperty(PROPERTY_LATITUDE_VALUE);
	        object = token.getModel().createTypedLiteral(getLatitudeValue(), XSDDatatype.XSDdecimal);
	        token.asResource().addProperty(predicate, object);
	        
	        predicate = token.getModel().getProperty(PROPERTY_LONGITUDE_VALUE);
	        object = token.getModel().createTypedLiteral(getLongitudeValue(), XSDDatatype.XSDdecimal);
	        token.asResource().addProperty(predicate, object);
	        
	        predicate = componentExecution.getModel().getProperty(PROPERTY_TOKEN);
	        componentExecution.addProperty(predicate, token);
    }
	
	void addNameLatLongTriples() {
		getRules().forEach(rule -> {
			RDFNode nameLatLongTriple = componentExecution.getModel().createResource(createSequentialUri(RESOURCE_NAMELATLONG_TRIPLE + getCommonString()), componentExecution.getModel().getResource(createUri(CLASS_NAMELATLONG_TRIPLE))); 

			Property predicate_λ = nameLatLongTriple.getModel().getProperty(PROPERTY_PLACE_NAME);
			RDFNode object_λ = nameLatLongTriple.getModel().createTypedLiteral(ResourceFactory.createLangLiteral(rule.getsPhrase(), ENGLISH_TAG));
			nameLatLongTriple.asResource().addProperty(predicate_λ, object_λ);
			
			predicate_λ = nameLatLongTriple.getModel().getProperty(PROPERTY_LATITUDE_VALUE);
			object_λ = nameLatLongTriple.getModel().createTypedLiteral(rule.getValue1(), XSDDatatype.XSDdecimal);
			nameLatLongTriple.asResource().addProperty(predicate_λ, object_λ);
			
			predicate_λ = nameLatLongTriple.getModel().getProperty(PROPERTY_LONGITUDE_VALUE);
			object_λ = nameLatLongTriple.getModel().createTypedLiteral(rule.getValue2(), XSDDatatype.XSDdecimal);
			nameLatLongTriple.asResource().addProperty(predicate_λ, object_λ);
			
			predicate_λ = componentExecution.getModel().getProperty(PROPERTY_LOCATION);
			componentExecution.addProperty(predicate_λ, nameLatLongTriple);
		});
	}
	
	String getComponentName() {
		return RESOURCE_LATLONG;
	}
	
	String getExecutionType() {
		return CLASS_LATLONG_EXECUTION;
	}
	
	String getTokenName() {
		if (tokenName == null) {
			tokenName = RESOURCE_TOKEN_GEO + getCommonString();
		}
		return tokenName;
	}
	
	String getTokenClass() {
		return CLASS_TOKEN_GEO;
	}
	
	double getLatitudeValue() {
		return componentNell.getFormatHeader().getTokenElement2LatLong()[0];
	}
	
	double getLongitudeValue() {
		return componentNell.getFormatHeader().getTokenElement2LatLong()[1];
	}
	
	List<Rule> getRules() {
		return ((LatLong) componentNell).getMetadata_Lrules();
	}

}
