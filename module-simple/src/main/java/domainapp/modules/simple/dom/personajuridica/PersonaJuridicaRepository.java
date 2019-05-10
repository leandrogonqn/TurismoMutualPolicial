package domainapp.modules.simple.dom.personajuridica;

import java.util.List;
import javax.inject.Inject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = PersonaJuridica.class)
public class PersonaJuridicaRepository {

	public List<PersonaJuridica> listar() {
		return repositoryService.allInstances(PersonaJuridica.class);
	}

	public List<PersonaJuridica> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(PersonaJuridica.class, "listarActivos"));
	}

	public List<PersonaJuridica> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(PersonaJuridica.class, "listarInactivos"));
	}

	@Inject
	RepositoryService repositoryService;

}
