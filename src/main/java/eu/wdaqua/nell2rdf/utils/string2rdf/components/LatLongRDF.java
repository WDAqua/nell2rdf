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

	public LatLongRDF(final LatLong latLong, final Resource belief) {
		super(latLong, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addNameLatLongTriples();
	}

	@Override
	void addToken() {
		final RDFNode token = this.componentExecution.getModel().createResource(getTokenName(), this.componentExecution.getModel().getResource(getTokenClass()));

		Property predicate = token.getModel().getProperty(getMetadataUri(PROPERTY_TOKE_ENTITY));
		RDFNode object = token.getModel().createTypedLiteral(getTokenEntity(), XSDDatatype.XSDstring);
		token.asResource().addProperty(predicate, object);

		predicate = token.getModel().getProperty(getMetadataUri(PROPERTY_LATITUDE_VALUE));
		object = token.getModel().createTypedLiteral(getLatitudeValue(), XSDDatatype.XSDdecimal);
		token.asResource().addProperty(predicate, object);

		predicate = token.getModel().getProperty(getMetadataUri(PROPERTY_LONGITUDE_VALUE));
		object = token.getModel().createTypedLiteral(getLongitudeValue(), XSDDatatype.XSDdecimal);
		token.asResource().addProperty(predicate, object);

		predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_TOKEN));
		this.componentExecution.addProperty(predicate, token);
	}

	void addNameLatLongTriples() {
		getRules().forEach(rule -> {
			final RDFNode nameLatLongTriple = this.componentExecution.getModel().createResource(getMetadataUri(createSequentialName(RESOURCE_NAMELATLONG_TRIPLE + getCommonString())));
			this.componentExecution.getModel().getResource(getMetadataUri(getMetadataUri(CLASS_NAMELATLONG_TRIPLE)));

			Property predicate_λ = nameLatLongTriple.getModel().getProperty(getMetadataUri(PROPERTY_PLACE_NAME));
			RDFNode object_λ = nameLatLongTriple.getModel().createTypedLiteral(ResourceFactory.createLangLiteral(rule.getsPhrase(), ENGLISH_TAG));
			nameLatLongTriple.asResource().addProperty(predicate_λ, object_λ);

			predicate_λ = nameLatLongTriple.getModel().getProperty(getMetadataUri(PROPERTY_LATITUDE_VALUE));
			object_λ = nameLatLongTriple.getModel().createTypedLiteral(rule.getValue1(), XSDDatatype.XSDdecimal);
			nameLatLongTriple.asResource().addProperty(predicate_λ, object_λ);

			predicate_λ = nameLatLongTriple.getModel().getProperty(getMetadataUri(PROPERTY_LONGITUDE_VALUE));
			object_λ = nameLatLongTriple.getModel().createTypedLiteral(rule.getValue2(), XSDDatatype.XSDdecimal);
			nameLatLongTriple.asResource().addProperty(predicate_λ, object_λ);

			predicate_λ = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_LOCATION));
			this.componentExecution.addProperty(predicate_λ, nameLatLongTriple);
		});
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_LATLONG);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_LATLONG_EXECUTION);
	}

	@Override
	String getTokenName() {
		if (this.tokenName == null) {
			this.tokenName = getMetadataUri(RESOURCE_TOKEN_GEO + getCommonString());
		}
		return this.tokenName;
	}

	@Override
	String getTokenClass() {
		return getMetadataUri(CLASS_TOKEN_GEO);
	}

	double getLatitudeValue() {
		return this.componentNell.getFormatHeader().getTokenElement2LatLong()[0];
	}

	double getLongitudeValue() {
		return this.componentNell.getFormatHeader().getTokenElement2LatLong()[1];
	}

	List<Rule> getRules() {
		return ((LatLong) this.componentNell).getMetadata_Lrules();
	}

}
