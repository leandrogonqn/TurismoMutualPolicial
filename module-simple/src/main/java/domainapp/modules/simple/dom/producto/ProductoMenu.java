package domainapp.modules.simple.dom.producto;

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
import domainapp.modules.simple.dom.categoria.Categoria;
import domainapp.modules.simple.dom.categoria.CategoriaRepository;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.proveedor.Proveedor;
import domainapp.modules.simple.dom.proveedor.ProveedorRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Producto.class, objectType="simple.ProductoMenu")
@DomainServiceLayout(named = "Productos", menuOrder = "30.2")
public class ProductoMenu {
	

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Producto")
	@MemberOrder(sequence = "1")
	public Producto crear(@ParameterLayout(named = "Codigo") final int productoCodigo,
			@ParameterLayout(named = "Es alojamiento propio?") final boolean productoAlojamientoPropio,
			@Nullable @ParameterLayout(named = "Proveedor") @Parameter(optionality=Optionality.OPTIONAL) final Proveedor productoProveedor,
			@ParameterLayout(named = "Categoria") final Categoria productoCategoria,
			@ParameterLayout(named = "Localidad") final Localidad productoLocalidad) {
		return productoRepository.crear(productoCodigo, productoAlojamientoPropio, productoProveedor, productoCategoria, productoLocalidad);
	}

	public List<Proveedor> choices2Crear(final int productoCodigo, final boolean productoAlojamientoPropio, final Proveedor productoProveedor,
			final Categoria productoCategoria, final Localidad productoLocalidad) {
		if (productoAlojamientoPropio==true)
			return null;
		return proveedorRepository.listarActivos();
	}
	
	public List<Categoria> choices3Crear(){
		return categoriaRepository.listarActivos();
	}
	
	public List<Localidad> choices4Crear(){
		return localidadRepository.listarActivos();
	}
	
	public String validateCrear(final int productoCodigo, final boolean productoAlojamientoPropio, final Proveedor productoProveedor,
			final Categoria productoCategoria, final Localidad productoLocalidad) {
		if(productoAlojamientoPropio==false & productoProveedor == null)
			return "Si el alojamiento no es propio el proveedor no puede ser nulo";
		return "";
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todos los Productos")
	@MemberOrder(sequence = "2")
	public List<Producto> listar() {
		return productoRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar producto Por codigo")
	@MemberOrder(sequence = "3")
	public List<Producto> buscarPorCodigo(@ParameterLayout(named = "Codigo") final int categoriaCodigo) {
		return productoRepository.buscarPorCodigo(categoriaCodigo);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar producto por Localidad")
	@MemberOrder(sequence = "4")
	public List<Producto> buscarProductoPorLocalidad(@ParameterLayout(named = "Localidad") final Localidad productoLocalidad) {
		return productoRepository.buscarProductoPorLocalidad(productoLocalidad);
	}
	
	public List<Localidad> choices0BuscarProductoPorLocalidad(){
		return localidadRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar producto por Categoria")
	@MemberOrder(sequence = "5")
	public List<Producto> buscarProductoPorCategoria(@ParameterLayout(named = "Categoria") final Categoria productoCategoria) {
		return productoRepository.buscarProductoPorCategoria(productoCategoria);
	}
	
	public List<Categoria> choices0BuscarProductoPorCategoria(){
		return categoriaRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar producto por Proveedor")
	@MemberOrder(sequence = "5")
	public List<Producto> buscarProductoPorProveedor(@ParameterLayout(named = "Proveedor") final Proveedor productoProveedor) {
		return productoRepository.buscarProductoPorProveedor(productoProveedor);
	}
	
	public List<Proveedor> choices0BuscarProductoPorProveedor(){
		return proveedorRepository.listarActivos();
	}

	@javax.inject.Inject
	ProductoRepository productoRepository;
	
	@Inject
	ProveedorRepository proveedorRepository;
	
	@Inject
	CategoriaRepository categoriaRepository;
	
	@Inject
	LocalidadRepository localidadRepository;

}
