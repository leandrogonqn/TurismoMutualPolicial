package domainapp.modules.simple.dom.proveedor;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import domainapp.modules.simple.dom.localidad.Localidad;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Proveedor.class)
public class ProveedorRepository {

	public List<Proveedor> listar() {
		return repositoryService.allInstances(Proveedor.class);
	}

	public List<Proveedor> buscarPorRazonSocial(final String proveedorRazonSocial) {

		return repositoryService.allMatches(new QueryDefault<>(Proveedor.class, "buscarPorRazonSocial", "proveedorRazonSocial",
				proveedorRazonSocial.toLowerCase()));

	}
	
	public List<Proveedor> buscarPorCodigo(final int proveedorCodigo) {
		return repositoryService
				.allMatches(new QueryDefault<>(Proveedor.class, "buscarPorCodigo", "proveedorCodigo", proveedorCodigo));
	}

	public List<Proveedor> buscarProveedorPorLocalidad(final Localidad proveedorLocalidad) {
		return repositoryService
				.allMatches(new QueryDefault<>(Proveedor.class, "buscarProveedorPorLocalidad", "proveedorLocalidad", proveedorLocalidad));
	}
	
	public List<Proveedor> listarHabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(Proveedor.class, "listarHabilitados"));
	}

	public List<Proveedor> listarInhabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(Proveedor.class, "listarInhabilitados"));
	}

	public Proveedor crear(final int proveedorCodigo, final String proveedorCuit, final String proveedorRazonSocial, 
			final String proveedorNombreComercial, final String proveedorDireccion, final Localidad proveedorLocalidad, 
			final String proveedorTelefono, final String proveedorMail, String proveedorWeb, final String proveedorContacto) {
		final Proveedor object = new Proveedor(proveedorCodigo, proveedorCuit, proveedorRazonSocial, proveedorNombreComercial, 
				proveedorDireccion, proveedorLocalidad, proveedorTelefono, proveedorMail, proveedorWeb, proveedorContacto);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
