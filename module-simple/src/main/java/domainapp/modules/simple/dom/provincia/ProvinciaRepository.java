package domainapp.modules.simple.dom.provincia;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Provincia.class)
public class ProvinciaRepository {

	public List<Provincia> listar() {
		return repositoryService.allInstances(Provincia.class);
	}

	public List<Provincia> buscarPorNombre(final String provinciasNombre) {

		return repositoryService.allMatches(new QueryDefault<>(Provincia.class, "buscarPorNombre", "provinciasNombre",
				provinciasNombre.toLowerCase()));

	}

	public List<Provincia> listarHabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(Provincia.class, "listarHabilitados"));
	}

	public List<Provincia> listarInhabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(Provincia.class, "listarInhabilitados"));
	}

	public Provincia crear(final String provinciaNombre) {
		final Provincia object = new Provincia(provinciaNombre);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
