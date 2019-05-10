package domainapp.modules.simple.dom.categoria;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Categoria.class)
public class CategoriaRepository {

	public List<Categoria> listar() {
		return repositoryService.allInstances(Categoria.class);
	}

	public List<Categoria> buscarPorNombre(final String categoriaNombre) {

		return repositoryService.allMatches(new QueryDefault<>(Categoria.class, "buscarPorNombre", "categoriaNombre",
				categoriaNombre.toLowerCase()));

	}

	public List<Categoria> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(Categoria.class, "listarActivos"));
	}

	public List<Categoria> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(Categoria.class, "listarInactivos"));
	}
	
	public List<Categoria> buscarPorCodigo(final int categoriaCodigo) {
		return repositoryService
				.allMatches(new QueryDefault<>(Categoria.class, "buscarPorCodigo", "categoriaCodigo", categoriaCodigo));
	}


	public Categoria crear(final int categoriaCodigo, final String categoriaNombre) {
		final Categoria object = new Categoria(categoriaCodigo, categoriaNombre);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
