package domainapp.modules.simple.dom.reservaempresa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import domainapp.modules.simple.dom.empresa.Empresa;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.producto.ProductoRepository;
import domainapp.modules.simple.dom.reserva.Reserva;
import domainapp.modules.simple.dom.voucher.EstadoVoucher;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "ReservaEmpresa")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "reservaId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.reservaempresa.ReservaEmpresa " + "WHERE reservaActivo == :reservaActivo ")})
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
public class ReservaEmpresa extends Reserva implements Comparable<Reserva>{

	// region > title
	public TranslatableString title() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return TranslatableString.tr("{name}", "name", "Codigo: " + getReservaCodigo()+ " Cliente: "+getReservaCliente()+" Entrada: "
				+ ""+sdf.format(getReservaListaVoucher().get(0).getVoucherFechaEntrada())+"Salida: "+sdf.format(getReservaListaVoucher().get(0).getVoucherFechaSalida()));
	}
	// endregion

//	public String cssClass() {
//		return (getReservaActivo() == true) ? "activo" : "inactivo";
//	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public ReservaEmpresa(int reservaCodigo, Date reservaFecha, Empresa reservaCliente, final List<Voucher> reservaListaVoucher,
			String reservaMemo) {
		setReservaCodigo(reservaCodigo);
		setReservaFecha(reservaFecha);
		setReservaCliente(reservaCliente);
		setReservaListaVoucher(reservaListaVoucher);
		setReservaMemo(reservaMemo);
		setReservaListaVoucher(reservaListaVoucher);
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Cliente")
	private Empresa reservaCliente;

	public Empresa getReservaCliente() {
		return reservaCliente;
	}

	public void setReservaCliente(Empresa reservaCliente) {
		this.reservaCliente = reservaCliente;
	}
	
	// endregion

	// region > delete (action)
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaCodigo")
	public ReservaEmpresa actualizarReservaCodigo(@ParameterLayout(named = "Codigo") final int reservaCodigo) {
		setReservaCodigo(reservaCodigo);
		return this;
	}

	public int default0ActualizarReservaCodigo() {
		return getReservaCodigo();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaFecha")
	public ReservaEmpresa actualizarReservaFecha(@ParameterLayout(named = "Fecha") final Date reservaFecha) {
		setReservaFecha(reservaFecha);
		return this;
	}

	public Date default0ActualizarReservaFecha() {
		return getReservaFecha();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaCliente")
	public ReservaEmpresa actualizarReservaCliente(@ParameterLayout(named = "Cliente") final Empresa reservaCliente) {
		setReservaCliente(reservaCliente);
		return this;
	}

	public Empresa default0ActualizarReservaCliente() {
		return getReservaCliente();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaMemo")
	public ReservaEmpresa actualizarReservaMemo(@ParameterLayout(named = "Memo") final String reservaMemo) {
		setReservaMemo(reservaMemo);
		return this;
	}

	public String default0ActualizarReservaMemo() {
		return getReservaMemo();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		
		return "";
	}

	@Override
	public int compareTo(final Reserva reserva) {
        if (getReservaCodigo()<reserva.getReservaCodigo()) {
            return -1;
        }
        if (getReservaCodigo()>reserva.getReservaCodigo()) {
            return 1;
        }	
        return 0;
	}

	// endregion

	// acciones
//	@Action(semantics = SemanticsOf.SAFE)
//	@ActionLayout(named = "Listar todas las Reservas")
//	@MemberOrder(sequence = "2")
//	public List<Reserva> listar() {
//		return reservaRepository.listar();
//	}
//
//	@Action(semantics = SemanticsOf.SAFE)
//	@ActionLayout(named = "Listar Reservas Activas")
//	@MemberOrder(sequence = "3")
//	public List<Reserva> listarActivos() {
//		return reservaRepository.listarActivos();
//	}
//
//	@Action(semantics = SemanticsOf.SAFE)
//	@ActionLayout(named = "Listar Reservas Inactivas")
//	@MemberOrder(sequence = "4")
//	public List<Reserva> listarInactivos() {
//		return reservaRepository.listarInactivos();
//	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Voucher", cssClass="crear")
	@MemberOrder(sequence = "1")
	public ReservaEmpresa crearVoucher(@ParameterLayout(named = "Producto") final Producto voucherProducto,
			@ParameterLayout(named = "Fecha de entrada") final Date voucherFechaEntrada,
			@ParameterLayout(named = "Fecha de salida") final Date voucherFechaSalida,
			@ParameterLayout(named = "Cantidad de pasajeros") final int voucherCantidadPasajeros,
			@Nullable @ParameterLayout(named = "Observaciones", multiLine=6) @Parameter(optionality=Optionality.OPTIONAL) final String voucherObservaciones) {
		Voucher v = voucherRepository.crear(voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadPasajeros, TipoPrecio.No_Afiliado, voucherObservaciones);
		this.getReservaListaVoucher().add(v);
		this.setReservaListaVoucher(this.getReservaListaVoucher());
		v.setVoucherReserva(this);
		List<Voucher> listaVoucher = new ArrayList<>();
		for (int i = 0; i < getReservaListaVoucher().size(); i++) {
			if (getReservaListaVoucher().get(i).getVoucherEstado()==EstadoVoucher.reservado||
					getReservaListaVoucher().get(i).getVoucherEstado()==EstadoVoucher.prereserva)
				listaVoucher.add(getReservaListaVoucher().get(i));
		}
		if (!listaVoucher.isEmpty())
			confirmarReserva();
		return this;
	}
	
	public List<Producto> choices0CrearVoucher(){
		return productoRepository.listarActivos();
	}
	
	public String validateCrearVoucher(final Producto voucherProducto, final Date voucherFechaEntrada,
			final Date voucherFechaSalida, final int voucherCantidadPasajeros, final String voucherObservaciones) {
			if (voucherFechaEntrada.after(voucherFechaSalida))
				return "La fecha de salida no puede ser anterior a la de entrada";
			if(voucherRepository.corroborarDisponibilidadCrear(voucherProducto, voucherFechaEntrada, voucherFechaSalida)==false)
				return "El producto ya se encuentra reservado en las fechas seleccionadas";
		return "";
	}
	
	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	ReservaEmpresaRepository reservaEmpresaRepository;

	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	ProductoRepository productoRepository;
	
//	@Inject
//	Reserva reserva;

}
