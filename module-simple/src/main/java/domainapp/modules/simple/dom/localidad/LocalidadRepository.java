package domainapp.modules.simple.dom.localidad;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.provincia.Provincia;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Localidad.class)
public class LocalidadRepository {

	public List<Localidad> listar() {
		return repositoryService.allInstances(Localidad.class);
	}

	public List<Localidad> buscarPorNombre(final String localidadNombre) {

		return repositoryService.allMatches(new QueryDefault<>(Localidad.class, "buscarPorNombre", "localidadesNombre",
				localidadNombre.toLowerCase()));

	}

	public List<Localidad> listarHabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(Localidad.class, "listarHabilitados"));
	}

	public List<Localidad> listarInhabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(Localidad.class, "listarInhabilitados"));
	}

	public Localidad crear(final String localidadNombre, Provincia localidadProvincia) {
		final Localidad object = new Localidad(localidadNombre, localidadProvincia);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
