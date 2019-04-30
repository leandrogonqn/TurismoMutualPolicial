package domainapp.modules.simple.dom.preciohistorico;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.producto.ProductoRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, objectType = "simple.PrecioHistoricoMenu", repositoryFor = PrecioHistorico.class)
@DomainServiceLayout(named = "Productos", menuOrder = "30.3")
public class PrecioHistoricoMenu {
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar historial completo")
	@MemberOrder(sequence = "2")
	public List<PrecioHistorico> listar() {
		return precioHistoricoRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Mostrar precio de producto por fecha")
	@MemberOrder(sequence = "1")
	public Double mostrarPrecioPorFecha(@ParameterLayout(named = "Producto") final Producto precioHistoricoProducto,
			@ParameterLayout(named = "fecha") final Date fecha) {
		return precioHistoricoRepository.mostrarPrecioPorFecha(precioHistoricoProducto, fecha);
	}
	
	public List<Producto> choices0MostrarPrecioPorFecha() {
		return productoRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Mostrar precio de producto por fecha")
	@MemberOrder(sequence = "1")
	public List<PrecioHistorico> listarPreciosPorRangoDeFecha(@ParameterLayout(named = "Producto") final Producto precioHistoricoProducto,
			@ParameterLayout(named = "Fecha Desde") final Date precioHistoricoFechaDesde,
			@ParameterLayout(named = "Fecha Hasta") final Date precioHistoricoFechaHasta) {
		return precioHistoricoRepository.listarPreciosPorRangoDeFecha(precioHistoricoProducto, precioHistoricoFechaDesde, precioHistoricoFechaHasta);
	}
	
	public List<Producto> choices0ListarPreciosPorRangoDeFecha() {
		return productoRepository.listarActivos();
	}

	@Inject
	PrecioHistoricoRepository precioHistoricoRepository;
	
	@javax.inject.Inject
	ProductoRepository productoRepository;
	
}
