package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import eu.wdaqua.nell2rdf.extract.metadata.models.AliasMatcher;
import eu.wdaqua.nell2rdf.extract.metadata.models.Header;
import eu.wdaqua.nell2rdf.extract.metadata.models.LineInstanceJOIN;
import eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList;

public class ComponentRDFBuilder {
	
	private ComponentRDFBuilder() {
		// Static class
	}
	
	public static ComponentRDF build(Header componentNell) {
		final ComponentRDF componentRdf;
		switch (componentNell.getComponentName()) {
		case ConstantList.ALIASMATCHER:
			componentRdf = new AliasMatcherRDF((AliasMatcher) componentNell);
			break;

		default:
			componentRdf = new ComponentRDF(componentNell);
		}
		return componentRdf;
	}

}
