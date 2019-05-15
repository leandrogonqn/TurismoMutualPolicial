package domainapp.modules.simple.dom.reservaafiliado;

import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
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
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.producto.ProductoRepository;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = ReservaAfiliado.class, objectType="simple.ReservaAfiliadoMenu")
@DomainServiceLayout(named = "Reserva", menuOrder = "40")
public class ReservaAfiliadoMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Reserva Afiliado")
	@MemberOrder(sequence = "1.2")
	public ReservaAfiliado crear(@ParameterLayout(named = "Codigo") final int reservaCodigo,
			@ParameterLayout(named = "Fecha de Reserva") final Date reservaFecha,
			@ParameterLayout(named = "Cliente") final Afiliado reservaCliente,
			@ParameterLayout(named = "Producto") final Producto voucherProducto,
			@ParameterLayout(named = "Fecha de entrada") final Date voucherFechaEntrada,
			@ParameterLayout(named = "Fecha de salida") final Date voucherFechaSalida,
			@ParameterLayout(named = "Cantidad de pasajeros") final int voucherCantidadPasajeros,
			@Nullable @ParameterLayout(named = "Observaciones del voucher", multiLine=6) @Parameter(optionality=Optionality.OPTIONAL) final String voucherObservaciones,
			@ParameterLayout(named = "Canal de pago") final CanalDePago reservaCanalDePago,
			@Nullable @ParameterLayout(named = "Memo de la reserva", multiLine=6) @Parameter(optionality=Optionality.OPTIONAL) final String reservaMemo) {
		return reservaRepository.crear(reservaCodigo, reservaFecha, reservaCliente, voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadPasajeros,
				TipoPrecio.Afiliado, voucherObservaciones, reservaCanalDePago, reservaMemo);
	}
	
	public List<Afiliado> choices2Crear() {
		return afiliadoRepository.listarActivos();
	}
	
	public List<Producto> choices3Crear(){
		return productoRepository.listarActivos();
	}
	
	public String validateCrear(final int reservaCodigo, final Date reservaFecha,
			final Afiliado reservaCliente, final Producto voucherProducto, final Date voucherFechaEntrada,
			final Date voucherFechaSalida, final int voucherCantidadPasajeros, final String voucherObservaciones,
			final CanalDePago reservaCanalDePago, final String reservaMemo) {
			List<Voucher> listaVoucher = voucherRepository.listarVoucherPorProducto(voucherProducto, true);
			if (voucherFechaEntrada.after(voucherFechaSalida))
				return "La fecha de salida no puede ser anterior a la de entrada";
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
	
	@javax.inject.Inject
	ReservaAfiliadoRepository reservaRepository;
	
	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	AfiliadoRepository afiliadoRepository;
	
	@Inject
	ProductoRepository productoRepository;
	
}