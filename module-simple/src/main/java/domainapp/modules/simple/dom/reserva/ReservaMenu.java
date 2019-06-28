package domainapp.modules.simple.dom.reserva;

import java.text.SimpleDateFormat;
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
	@MemberOrder(sequence = "60")
	public List<Reserva> listar() {
		return reservaRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar reserva por codigo")
	@MemberOrder(sequence = "65")
	public List<Reserva> buscarPorCodigo(@ParameterLayout(named = "Codigo") final int reservaCodigo) {
		return reservaRepository.buscarPorCodigo(reservaCodigo);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Vouchers Por Localidad")
	@MemberOrder(sequence = "70")
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
		return localidadRepository.listar();
	}
	
	public String validateListarVoucherPorLocalidad(final Localidad productoLocalidad, final Date fechaDesde, final Date fechaHasta) {
			if (fechaDesde.after(fechaHasta)/*||fechaDesde.equals(fechaHasta)*/)
				return "La fecha desde no puede ser anterior o igual a la fecha hasta";
		return "";
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Vouchers Por Productos")
	@MemberOrder(sequence = "80")
	public List<Voucher> listarVoucherPorProducto(@ParameterLayout(named="Producto") final Producto voucherProducto,
			@ParameterLayout(named="Desde") final Date fechaDesde,
			@ParameterLayout(named="Hasta") final Date fechaHasta){
		List<Voucher> listaVoucher = new ArrayList<>();
		listaVoucher.addAll(voucherRepository.listarVoucherPorProducto(voucherProducto, EstadoVoucher.reservado, fechaDesde, fechaHasta));
		listaVoucher.addAll(voucherRepository.listarVoucherPorProducto(voucherProducto, EstadoVoucher.prereserva, fechaDesde, fechaHasta));
		return listaVoucher;
	}
	
	public List<Producto> choices0ListarVoucherPorProducto(){
		return productoRepository.listarHabilitados();
	}
	
	public String validateListarVoucherPorProducto(final Producto voucherProducto, final Date fechaDesde,
			final Date fechaHasta) {
		if (fechaDesde.after(fechaHasta)/*||fechaDesde.equals(fechaHasta)*/)
			return "La fecha desde no puede ser anterior o igual a la fecha hasta";
		return "";
	}
	
	@SuppressWarnings("deprecation")
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-calendar", named = "Listar Disponibilidad Por Productos")
	@MemberOrder(sequence = "50")
	public List<FechasDisponibles> listarDisponibilidadPorProducto(@ParameterLayout(named="Producto") final Producto voucherProducto,
			@ParameterLayout(named="Desde") final Date fechaDesde,
			@ParameterLayout(named="Hasta") final Date fechaHasta){
		List<FechasDisponibles> listaFechasDisponibles = new ArrayList<>();
		if(voucherProducto.getProductoAlojamientoPropio()==true) {
			fechaDesde.setHours(12);
			fechaHasta.setHours(12);
			Date f = fechaDesde;
			while (f.before(fechaHasta)) {
				if (voucherRepository.corroborarDisponibilidadCrear(voucherProducto, f, f) == true) {
					FechasDisponibles fechaDisponible = new FechasDisponibles();
					fechaDisponible.setProducto(voucherProducto);
					fechaDisponible.setFechaDesde(f);
					while (voucherRepository.corroborarDisponibilidadCrear(voucherProducto, f, f) == true) {
						f = voucherRepository.sumarUnDiaAFecha(f);
						if (f.after(fechaHasta)) {
							fechaDisponible.setMemo("");
							break;
						}
						fechaDisponible.setFechaHasta(f);
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						fechaDisponible.setMemo("Disponibilidad hasta las 10:00 a.m.  del día "+sdf.format(f));
					}
					listaFechasDisponibles.add(fechaDisponible);
				}
				f = voucherRepository.sumarUnDiaAFecha(f);
			}
		} else {
			FechasDisponibles fechaDisponible = new FechasDisponibles();
			fechaDisponible.setProducto(voucherProducto);
			fechaDisponible.setMemo("Consultar Disponibilidad al proveedor");
			listaFechasDisponibles.add(fechaDisponible);
		}

		return listaFechasDisponibles;
	}
	
	public List<Producto> choices0ListarDisponibilidadPorProducto(){
		return productoRepository.listarHabilitados();
	}
	
	public String validateListarDisponibilidadPorProducto(final Producto voucherProducto, final Date fechaDesde,
			final Date fechaHasta) {
		if (fechaDesde.after(fechaHasta)||fechaDesde.equals(fechaHasta))
			return "La fecha desde no puede ser anterior o igual a la fecha hasta";
		return "";
	}
	
	@SuppressWarnings("deprecation")
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-calendar", named = "Listar Disponibilidad Por Localidad")
	@MemberOrder(sequence = "40")						
	public List<FechasDisponibles> listarDisponibilidadPorLocalidad(@ParameterLayout(named="Localidad") final Localidad productoLocalidad,
			@ParameterLayout(named="Desde") final Date fechaDesde,
			@ParameterLayout(named="Hasta") final Date fechaHasta){
		fechaDesde.setHours(12);
		fechaHasta.setHours(12);
		List<Producto> listaProducto = productoRepository.buscarProductoPorLocalidad(productoLocalidad);
		List<FechasDisponibles> listaFechasDisponibles = new ArrayList<>();
		for (int i = 0; listaProducto.size()>i; i++) {
			if(listaProducto.get(i).getProductoAlojamientoPropio()==true) {
				Date f = fechaDesde;
				while(f.before(fechaHasta)) {
					if(voucherRepository.corroborarDisponibilidadCrear(listaProducto.get(i), f, f)==true) {
						FechasDisponibles fechaDisponible = new FechasDisponibles();
						fechaDisponible.setProducto(listaProducto.get(i));
						fechaDisponible.setFechaDesde(f);
						while(voucherRepository.corroborarDisponibilidadCrear(listaProducto.get(i), f, f)==true) {
							f = voucherRepository.sumarUnDiaAFecha(f);
							if (f.after(fechaHasta)) {
								fechaDisponible.setMemo("");
								break;
							}
							fechaDisponible.setFechaHasta(f);
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
							fechaDisponible.setMemo("Disponibilidad hasta las 10:00 a.m.  del día "+sdf.format(f));
						}
						listaFechasDisponibles.add(fechaDisponible);
					}
					f = voucherRepository.sumarUnDiaAFecha(f);
				}
			} else {
				FechasDisponibles fechaDisponible = new FechasDisponibles();
				fechaDisponible.setProducto(listaProducto.get(i));
				fechaDisponible.setMemo("Consultar Disponibilidad al proveedor");
				listaFechasDisponibles.add(fechaDisponible);
			}
		}
	return listaFechasDisponibles;
	}
	
	public List<Localidad> choices0ListarDisponibilidadPorLocalidad(){
		return localidadRepository.listar();
	}
	
	public String validateListarDisponibilidadPorLocalidad(final Localidad productoLocalidad, final Date fechaDesde, final Date fechaHasta) {
			if (fechaDesde.after(fechaHasta)||fechaDesde.equals(fechaHasta))
				return "La fecha desde no puede ser anterior o igual a la fecha hasta";
		return "";
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
