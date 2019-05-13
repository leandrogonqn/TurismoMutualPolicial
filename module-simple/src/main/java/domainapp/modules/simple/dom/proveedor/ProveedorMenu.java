package domainapp.modules.simple.dom.proveedor;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Proveedor.class, objectType="simple.ProveedorMenu")
@DomainServiceLayout(named = "Proveedor", menuOrder = "20")
public class ProveedorMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todos los Proveedores")
	@MemberOrder(sequence = "2")
	public List<Proveedor> listar() {
		return proveedorRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar proveedor Por Codigo")
	@MemberOrder(sequence = "7")
	public List<Proveedor> buscarPorCodigo(@ParameterLayout(named = "Codigo") final int proveedorCodigo) {
		return proveedorRepository.buscarPorCodigo(proveedorCodigo);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar proveedor por Localidad")
	@MemberOrder(sequence = "6")
	public List<Proveedor> buscarProveedorPorLocalidad(@ParameterLayout(named = "Localidad") final Localidad proveedorLocalidad) {
		return proveedorRepository.buscarProveedorPorLocalidad(proveedorLocalidad);
	}
	
	public List<Localidad> choices0BuscarProveedorPorLocalidad() {
		return localidadRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar Proveedor Por Razon Social")
	@MemberOrder(sequence = "5")
	public List<Proveedor> buscarPorRazonSocial(@ParameterLayout(named = "Razon Social") final String proveedorRazonSocial) {
		return proveedorRepository.buscarPorRazonSocial(proveedorRazonSocial);

	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Proveedor")
	@MemberOrder(sequence = "1.2")
	public Proveedor crear(@ParameterLayout(named="Codigo") final int proveedorCodigo,
			@ParameterLayout(named="CUIT") final String proveedorCuit,
			@ParameterLayout(named = "Razon Social") final String proveedorRazonSocial,
			@ParameterLayout(named = "Nombre Comercial") final String proveedorNombreComercial,
			@Nullable @ParameterLayout(named="Direccion") @Parameter(optionality=Optionality.OPTIONAL) final String proveedorDireccion,
			@Nullable @ParameterLayout(named="Localidad") @Parameter(optionality=Optionality.OPTIONAL) final Localidad proveedorLocalidad,
			@Nullable @ParameterLayout(named="Telefono") @Parameter(optionality=Optionality.OPTIONAL) final String proveedorTelefono,
			@Nullable @ParameterLayout(named="Mail") @Parameter(optionality=Optionality.OPTIONAL) final String proveedorMail,
			@Nullable @ParameterLayout(named="Web") @Parameter(optionality=Optionality.OPTIONAL) final String proveedorWeb,
			@Nullable @ParameterLayout(named="Contacto") @Parameter(optionality=Optionality.OPTIONAL) final String proveedorContacto) {
		return proveedorRepository.crear(proveedorCodigo, proveedorCuit, proveedorRazonSocial, proveedorNombreComercial, proveedorDireccion, 
				proveedorLocalidad, proveedorTelefono, proveedorMail, proveedorWeb, proveedorContacto);
	}
	
	public List<Localidad> choices5Crear() {
		return localidadRepository.listarActivos();
	}

	@javax.inject.Inject
	ProveedorRepository proveedorRepository;
	
	@Inject
	LocalidadRepository localidadRepository;

}
