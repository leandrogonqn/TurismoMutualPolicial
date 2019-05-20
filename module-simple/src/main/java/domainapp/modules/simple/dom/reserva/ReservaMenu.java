package domainapp.modules.simple.dom.reserva;

import java.util.ArrayList;
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

import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.producto.ProductoRepository;
import domainapp.modules.simple.dom.voucher.EstadoVoucher;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Reserva.class, objectType="simple.ReservaMenu")
@DomainServiceLayout(named = "Reserva", menuOrder = "60")
public class ReservaMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todas las Reservas")
	@MemberOrder(sequence = "2")
	public List<Reserva> listar() {
		return reservaRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Vouchers Por Localidad")
	@MemberOrder(sequence = "3")
	public List<Voucher> listarVoucherPorLocalidad(@ParameterLayout(named="Localidad") final Localidad productoLocalidad,
			@ParameterLayout(named="Desde") final Date fechaDesde,
			@ParameterLayout(named="Hasta") final Date fechaHasta){
		List<Producto> listaProducto = productoRepository.buscarProductoPorLocalidad(productoLocalidad);
		List<Voucher> listaVoucher = new ArrayList<>();
		for(int indice = 0;indice<listaProducto.size();indice++) {
			listaVoucher.addAll(voucherRepository.listarVoucherPorProducto(listaProducto.get(indice), EstadoVoucher.reservado, fechaDesde, fechaHasta));
			listaVoucher.addAll(voucherRepository.listarVoucherPorProducto(listaProducto.get(indice), EstadoVoucher.prereserva, fechaDesde, fechaHasta));
		}
		return listaVoucher;
	}
	
	public List<Localidad> choices0ListarVoucherPorLocalidad(){
		return localidadRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Vouchers Por Productos")
	@MemberOrder(sequence = "4")
	public List<Voucher> listarVoucherPorProducto(@ParameterLayout(named="Producto") final Producto voucherProducto,
			@ParameterLayout(named="Desde") final Date fechaDesde,
			@ParameterLayout(named="Hasta") final Date fechaHasta){
		List<Voucher> listaVoucher = new ArrayList<>();
		listaVoucher.addAll(voucherRepository.listarVoucherPorProducto(voucherProducto, EstadoVoucher.reservado, fechaDesde, fechaHasta));
		listaVoucher.addAll(voucherRepository.listarVoucherPorProducto(voucherProducto, EstadoVoucher.prereserva, fechaDesde, fechaHasta));
		return listaVoucher;
	}
	
	public List<Producto> choices0ListarVoucherPorProducto(){
		return productoRepository.listarActivos();
	}
	
	@javax.inject.Inject
	ReservaRepository reservaRepository;
	
	@Inject
	ProductoRepository productoRepository;
	
	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	LocalidadRepository localidadRepository;
	
}
