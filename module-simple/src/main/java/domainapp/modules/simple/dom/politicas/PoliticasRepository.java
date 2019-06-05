package domainapp.modules.simple.dom.politicas;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Politicas.class)
public class PoliticasRepository {

	public List<Politicas> listar() {
		return repositoryService.allInstances(Politicas.class);
	}

	public Politicas crear(final String politicasNombre, final String politicasTexto) {
		final Politicas object = new Politicas(politicasNombre, politicasTexto);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
