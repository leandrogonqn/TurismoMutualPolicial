package domainapp.modules.simple.dom.producto;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.categoria.Categoria;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.politicas.Politicas;
import domainapp.modules.simple.dom.politicas.PoliticasRepository;
import domainapp.modules.simple.dom.proveedor.Proveedor;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Producto.class)
public class ProductoRepository {
	
	public Producto crear(final int productoCodigo, final boolean productoAlojamientoPropio, final Integer productoProveedorId,
			final Categoria productoCategoria, final String productoDireccion, final int productoLocalidadId, 
			final String productoTelefono) {
		List<Politicas> listaPoliticas = politicasRepository.listar(); 
		final Producto object = new Producto(productoCodigo, productoAlojamientoPropio, productoProveedorId, productoCategoria, 
				productoDireccion, productoLocalidadId, productoTelefono, listaPoliticas);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	public List<Producto> buscarPorCodigo(final int productoCodigo) {
		return repositoryService
				.allMatches(new QueryDefault<>(Producto.class, "buscarPorCodigo", "productoCodigo", productoCodigo));
	}
	
	public List<Producto> buscarProductoPorLocalidad(final Localidad productoLocalidad) {
		int productoLocalidadId = productoLocalidad.getLocalidadId();
		return repositoryService
				.allMatches(new QueryDefault<>(Producto.class, "buscarProductoPorLocalidad", "productoLocalidadId", productoLocalidadId));
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

	public List<Producto> listarHabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(Producto.class, "listarHabilitados"));
	}

	public List<Producto> listarInhabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(Producto.class, "listarInhabilitados"));
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	PoliticasRepository politicasRepository;
}
