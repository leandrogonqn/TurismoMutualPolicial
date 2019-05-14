package domainapp.modules.simple.dom.reserva;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import domainapp.modules.simple.dom.afiliado.Afiliado;
import domainapp.modules.simple.dom.afiliado.AfiliadoRepository;
import domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliado;
import domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliadoRepository;
import domainapp.modules.simple.dom.empresa.Empresa;
import domainapp.modules.simple.dom.empresa.EmpresaRepository;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.persona.Persona;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.producto.ProductoRepository;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Reserva.class, objectType="simple.ReservaMenu")
@DomainServiceLayout(named = "Reserva", menuOrder = "60")
public class ReservaMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Reserva")
	@MemberOrder(sequence = "1.2")
	public Reserva crear(@ParameterLayout(named = "Codigo") final int reservaCodigo,
			@ParameterLayout(named = "Fecha de Reserva") final Date reservaFecha,
			@ParameterLayout(named = "Tipo de cliente") final TipoCliente reservaTipoCliente,
			@ParameterLayout(named = "Cliente") final Persona reservaCliente,
			@ParameterLayout(named = "Producto") final Producto voucherProducto,
			@ParameterLayout(named = "Fecha de entrada") final Date voucherFechaEntrada,
			@ParameterLayout(named = "Fecha de salida") final Date voucherFechaSalida,
			@ParameterLayout(named = "Cantidad de pasajeros") final int voucherCantidadPasajeros,
			@Nullable @ParameterLayout(named = "Observaciones del voucher", multiLine=6) @Parameter(optionality=Optionality.OPTIONAL) final String voucherObservaciones,
			@ParameterLayout(named = "Canal de pago") final CanalDePago reservaCanalDePago,
			@Nullable @ParameterLayout(named = "Memo de la reserva", multiLine=6) @Parameter(optionality=Optionality.OPTIONAL) final String reservaMemo) {
		TipoPrecio precioHistoricoTipoPrecio;
		if(reservaTipoCliente==TipoCliente.Afiliado)
			precioHistoricoTipoPrecio = TipoPrecio.Afiliado;
		else
			precioHistoricoTipoPrecio = TipoPrecio.No_Afiliado;

		return reservaRepository.crear(reservaCodigo, reservaFecha, reservaCliente, voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadPasajeros,
				precioHistoricoTipoPrecio, voucherObservaciones, reservaCanalDePago, reservaMemo);
	}
	
	public List<Persona> choices3Crear(final int reservaCodigo, final Date reservaFecha, final TipoCliente reservaTipoCliente,
			final Persona reservaCliente, final Producto voucherProducto, final Date voucherFechaEntrada,
			final Date voucherFechaSalida, final int voucherCantidadPasajeros, final String voucherObservaciones,
			final CanalDePago reservaCanalDePago, final String reservaMemo) {
		List lista = new ArrayList<>();
		if (reservaTipoCliente==TipoCliente.Afiliado) {
			lista = afiliadoRepository.listarActivos();
		} else if (reservaTipoCliente==TipoCliente.No_Afiliado){
			lista = clienteNoAfiliadoRepository.listarActivos();
		} else if (reservaTipoCliente==TipoCliente.Empresa) {
			lista = empresaRepository.listarActivos();
		}
		return lista;
	}
	
	public List<Producto> choices4Crear(){
		return productoRepository.listarActivos();
	}
	
	public List<CanalDePago> choices9Crear(final int reservaCodigo, final Date reservaFecha, final TipoCliente reservaTipoCliente,
			final Persona reservaCliente, final Producto voucherProducto, final Date voucherFechaEntrada,
			final Date voucherFechaSalida, final int voucherCantidadPasajeros, final String voucherObservaciones,
			final CanalDePago reservaCanalDePago, final String reservaMemo) {
		List<CanalDePago> lista = new ArrayList<>();
		if (reservaTipoCliente!=TipoCliente.Afiliado) {
			lista.add(CanalDePago.Efectivo);
		} else {
			lista.add(CanalDePago.Debito_Automatico);
			lista.add(CanalDePago.Efectivo);
			lista.add(CanalDePago.Planilla);
		}
		return lista;
	}
	
	public String validateCrear(final int reservaCodigo, final Date reservaFecha, final TipoCliente reservaTipoCliente,
			final Persona reservaCliente, final Producto voucherProducto, final Date voucherFechaEntrada,
			final Date voucherFechaSalida, final int voucherCantidadPasajeros, final String voucherObservaciones,
			final CanalDePago reservaCanalDePago, final String reservaMemo) {
			List<Voucher> listaVoucher = voucherRepository.listarVoucherPorProducto(voucherProducto, true);
			if (voucherFechaEntrada.after(voucherFechaSalida))
				return "La fecha de salida no puede ser anterior a la de entrada";
			if (reservaTipoCliente==TipoCliente.Afiliado & reservaCliente.getClass()!=Afiliado.class) 
				return "ERROR: el tipo de cliente elegido no concuerda con el cliente seleccionado";
			if (reservaTipoCliente==TipoCliente.Empresa & reservaCliente.getClass()!=Empresa.class) 
				return "ERROR: el tipo de cliente elegido no concuerda con el cliente seleccionado";
			if (reservaTipoCliente==TipoCliente.No_Afiliado & reservaCliente.getClass()!=ClienteNoAfiliado.class) 
				return "ERROR: el tipo de cliente elegido no concuerda con el cliente seleccionado";
			if (reservaTipoCliente!=TipoCliente.Afiliado & reservaCanalDePago!=CanalDePago.Efectivo)
				return "ERROR: el tipo de cliente elegido no puede seleccionar el canal de pago seleccionado";
			if (reservaCanalDePago==CanalDePago.Debito_Automatico && reservaCliente.getAfiliadoCBU()==null) 
				return "ERROR: CBU no cargado, elija otro canal de pago";
			if (voucherProducto.getProductoAlojamientoPropio()==true) {
				for(int indice = 0;indice<listaVoucher.size();indice++) {
					if (voucherFechaEntrada.before(listaVoucher.get(indice).getVoucherFechaSalida())
							& voucherFechaSalida.after(listaVoucher.get(indice).getVoucherFechaEntrada()))
						return "El producto ya se encuentra reservado en las fechas seleccionadas"; 
				}
			}
		return "";
	}
	
	public Date default1Crear() {
		Date hoyDate = new Date();
		return hoyDate;
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todas las Reservas")
	@MemberOrder(sequence = "2")
	public List<Reserva> listar() {
		return reservaRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Reservas Activas")
	@MemberOrder(sequence = "3")
	public List<Reserva> listarActivos(){
		return reservaRepository.listarActivos(true);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Vouchers Por Productos")
	@MemberOrder(sequence = "3")
	public List<Voucher> listarVoucherPorProducto(@ParameterLayout(named="Producto") final Producto voucherProducto,
			@ParameterLayout(named="Desde") final Date fechaDesde,
			@ParameterLayout(named="Hasta") final Date fechaHasta){
		return voucherRepository.listarVoucherPorProducto(voucherProducto, true, fechaDesde, fechaHasta);
	}
	
	public List<Producto> choices0ListarVoucherPorProducto(){
		return productoRepository.listarActivos();
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
			listaVoucher.addAll(voucherRepository.listarVoucherPorProducto(listaProducto.get(indice), true, fechaDesde, fechaHasta));
		}
		return listaVoucher;
	}
	
	public List<Localidad> choices0ListarVoucherPorLocalidad(){
		return localidadRepository.listarActivos();
	}
	
	@javax.inject.Inject
	ReservaRepository reservaRepository;
	
	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	AfiliadoRepository afiliadoRepository;
	
	@Inject
	ClienteNoAfiliadoRepository clienteNoAfiliadoRepository;
	
	@Inject
	EmpresaRepository empresaRepository;
	
	@Inject
	ProductoRepository productoRepository;

	@Inject
	LocalidadRepository localidadRepository;
}
