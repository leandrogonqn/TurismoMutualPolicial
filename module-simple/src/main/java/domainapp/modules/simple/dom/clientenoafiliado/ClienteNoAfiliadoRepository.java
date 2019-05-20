package domainapp.modules.simple.dom.clientenoafiliado;

import java.util.List;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.localidad.Localidad;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = ClienteNoAfiliado.class)
public class ClienteNoAfiliadoRepository {

	public ClienteNoAfiliado crear(final int personaDni, final String personaApellido, final String personaNombre, 
			final String personaCuitCuil, final String personaDireccion, Localidad personaLocalidad, 
			 final Long personaTelefonoFijo, final Long personaTelefonoCelular, final String personaMail) {
		final ClienteNoAfiliado object = new ClienteNoAfiliado(personaDni, personaApellido, personaNombre, 
				personaCuitCuil, personaDireccion, personaLocalidad, personaTelefonoFijo, personaTelefonoCelular, personaMail);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	public List<ClienteNoAfiliado> listar() {
		return repositoryService.allInstances(ClienteNoAfiliado.class);
	}

	public List<ClienteNoAfiliado> listarHabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(ClienteNoAfiliado.class, "listarHabilitados"));
	}

	public List<ClienteNoAfiliado> listarInhabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(ClienteNoAfiliado.class, "listarInhabilitados"));
	}

	public List<ClienteNoAfiliado> buscarPorNombre(final String personaNombre) {
		return repositoryService.allMatches(
				new QueryDefault<>(ClienteNoAfiliado.class, "buscarPorNombre", "personaNombre", personaNombre.toLowerCase()));
	}
	
	public List<ClienteNoAfiliado> buscarPorDNI(final int personaDni) {
		return repositoryService
				.allMatches(new QueryDefault<>(ClienteNoAfiliado.class, "buscarPorDNI", "personaDni", personaDni));
	}


	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
