package domainapp.modules.simple.dom.afiliado;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import domainapp.modules.simple.dom.localidad.Localidad;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Afiliado.class)
public class AfiliadoRepository {

	public List<Afiliado> buscarPorLP(final String afiliadoLP) {
		return repositoryService.allMatches(
				new QueryDefault<>(Afiliado.class, "buscarPorLP", "afiliadoLP", afiliadoLP.toLowerCase()));
	}

	public Afiliado crearCompleto(boolean afiliadoActivo, String afiliadoLP, String afiliadoNombre, String afiliadoApellido,
			int afiliadoDni, String afiliadoCuitCuil, String afiliadoDireccion, Localidad afiliadoLocalidad,
			String afiliadoTelefonoFijo,  String afiliadoMail, String afiliadoCBU) {
		final Afiliado object = new Afiliado(afiliadoActivo, afiliadoLP, afiliadoDni, afiliadoApellido, afiliadoNombre, 
				afiliadoCuitCuil, afiliadoDireccion, afiliadoLocalidad, afiliadoTelefonoFijo, 
				afiliadoMail, afiliadoCBU);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	public List<Afiliado> listar() {
		return repositoryService.allInstances(Afiliado.class);
	}

	public List<Afiliado> buscarPorNombre(final String personaJuridicaNombre) {
		return repositoryService.allMatches(
				new QueryDefault<>(Afiliado.class, "buscarPorNombre", "personaJuridicaNombre", personaJuridicaNombre.toLowerCase()));
	}
	
	public List<Afiliado> buscarPorDNI(final int personaJuridicaDni) {
		return repositoryService
				.allMatches(new QueryDefault<>(Afiliado.class, "buscarPorDNI", "personaJuridicaDni", personaJuridicaDni));
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;

}
