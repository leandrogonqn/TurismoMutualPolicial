package domainapp.modules.simple.dom.producto;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.categoria.Categoria;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.proveedor.Proveedor;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Producto.class)
public class ProductoRepository {
	
	public Producto crear(final int productoCodigo, final boolean productoAlojamientoPropio, final Proveedor productoProveedor,
			final Categoria productoCategoria, final Localidad productoLocalidad) {
		final Producto object = new Producto(productoCodigo, productoAlojamientoPropio, productoProveedor, productoCategoria, productoLocalidad);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	public List<Producto> buscarPorCodigo(final int productoCodigo) {
		return repositoryService
				.allMatches(new QueryDefault<>(Producto.class, "buscarPorCodigo", "productoCodigo", productoCodigo));
	}
	
	public List<Producto> buscarProductoPorLocalidad(final Localidad productoLocalidad) {
		return repositoryService
				.allMatches(new QueryDefault<>(Producto.class, "buscarProductoPorLocalidad", "productoLocalidad", productoLocalidad));
	}
	
	public List<Producto> buscarProductoPorCategoria(final Categoria productoCategoria) {
		return repositoryService
				.allMatches(new QueryDefault<>(Producto.class, "buscarProductoPorCategoria", "productoCategoria", productoCategoria));
	}
	
	public List<Producto> buscarProductoPorProveedor(final Proveedor productoProveedor) {
		return repositoryService
				.allMatches(new QueryDefault<>(Producto.class, "buscarProductoPorProveedor", "productoProveedor", productoProveedor));
	}
	
	public List<Producto> listar() {
		return repositoryService.allInstances(Producto.class);
	}

	public List<Producto> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(Producto.class, "listarActivos"));
	}

	public List<Producto> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(Producto.class, "listarInactivos"));
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
