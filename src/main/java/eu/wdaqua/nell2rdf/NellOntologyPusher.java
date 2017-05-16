package eu.wdaqua.nell2rdf;

import java.io.File;
import java.io.IOException;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.config.RepositoryImplConfig;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.sail.config.SailRepositoryConfig;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.config.SailImplConfig;
import org.openrdf.sail.nativerdf.config.NativeStoreConfig;

public class NellOntologyPusher {
	public static void main(String[] args) {
		RemoteRepositoryManager repositoryManager = new RemoteRepositoryManager(
				"http://161.3.199.17:8080/openrdf-workbench/home/overview.view");

		try {

			repositoryManager.initialize();
			SailImplConfig backendConfig = new NativeStoreConfig("spoc,posc");

			RepositoryImplConfig repositoryTypeSpec = new SailRepositoryConfig(
					backendConfig);

			RepositoryConfig repConfig = new RepositoryConfig("nell",
					repositoryTypeSpec);
			repositoryManager.addRepositoryConfig(repConfig);

			Repository myRepository = new HTTPRepository(
					"http://161.3.199.17:8080/openrdf-sesame/home/overview.view", "nell");

			myRepository.initialize();

			RepositoryConnection con = myRepository.getConnection();

			con.add(new File(
					"/Users/cgravier/Downloads/NELL.08m.690.instances.ttl"),
					"http://localhost:8080/openrdf-sesame", RDFFormat.TURTLE);

			con.close();

		}

		catch (RDFParseException | RepositoryException | IOException
				| RepositoryConfigException e) {
			e.printStackTrace();
		}

	}
}
