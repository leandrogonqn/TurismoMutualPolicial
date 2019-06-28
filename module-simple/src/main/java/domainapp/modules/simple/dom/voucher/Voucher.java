package domainapp.modules.simple.dom.voucher;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.services.user.UserService;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.reportes.ReporteRepository;
import domainapp.modules.simple.dom.reserva.Reserva;
import domainapp.modules.simple.dom.reserva.ReservaRepository;
import domainapp.modules.simple.dom.reservaafiliado.ReservaAfiliado;

@SuppressWarnings("deprecation")
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Voucher")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "voucherId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarVoucherPorEstado", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.voucher.Voucher " + "WHERE voucherEstado == :voucherEstado "),
		@javax.jdo.annotations.Query(name = "listarVoucherPorProducto", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.voucher.Voucher " + "WHERE voucherEstado == :voucherEstado && voucherProducto == :voucherProducto ")})
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Voucher implements Comparable<Voucher> {
	// region > title
	public TranslatableString title() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return TranslatableString.tr("{name}", "name", getVoucherProducto().toString() + ". Fecha de entrada: "+sdf.format(getVoucherFechaEntrada())+
				". Fecha de salida: "+sdf.format(getVoucherFechaSalida()));
	}
	// endregion

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Voucher(final Producto voucherProducto, final Date voucherFechaEntrada, final Date voucherFechaSalida, final int voucherCantidadNoches,
			final int voucherCantidadPasajeros, final Double voucherPrecioTotal, final String voucherObservaciones, final String voucherUsuario) {
		setVoucherProducto(voucherProducto);
		setVoucherFechaEntrada(voucherFechaEntrada);
		setVoucherFechaSalida(voucherFechaSalida);
		setVoucherCantidadNoches(voucherCantidadNoches);
		setVoucherCantidadPasajeros(voucherCantidadPasajeros);
		setVoucherPrecioTotal(voucherPrecioTotal);
		setVoucherObservaciones(voucherObservaciones);
		setVoucherEstado(EstadoVoucher.presupuestado);
		setVoucherUsuario(voucherUsuario);
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Producto", cssClass="largo")
	private Producto voucherProducto;

	public Producto getVoucherProducto() {
		return voucherProducto;
	}

	public void setVoucherProducto(Producto voucherProducto) {
		this.voucherProducto = voucherProducto;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Fecha Entrada")
	private Date voucherFechaEntrada;

	public Date getVoucherFechaEntrada() {
		return voucherFechaEntrada;
	}

	public void setVoucherFechaEntrada(Date voucherFechaEntrada) {
		this.voucherFechaEntrada = voucherFechaEntrada;
	}	

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Fecha Salida")
	private Date voucherFechaSalida;

	public Date getVoucherFechaSalida() {
		return voucherFechaSalida;
	}

	public void setVoucherFechaSalida(Date voucherFechaSalida) {
		this.voucherFechaSalida = voucherFechaSalida;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Noches")
	private int voucherCantidadNoches;

	public int getVoucherCantidadNoches() {
		return voucherCantidadNoches;
	}

	public void setVoucherCantidadNoches(int voucherCantidadNoches) {
		this.voucherCantidadNoches = voucherCantidadNoches;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Pasajeros")
	private int voucherCantidadPasajeros;

	public int getVoucherCantidadPasajeros() {
		return voucherCantidadPasajeros;
	}

	public void setVoucherCantidadPasajeros(int voucherCantidadPasajeros) {
		this.voucherCantidadPasajeros = voucherCantidadPasajeros;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Precio Total")
	private Double voucherPrecioTotal;

	public Double getVoucherPrecioTotal() {
		return voucherPrecioTotal;
	}

	public void setVoucherPrecioTotal(Double voucherPrecioTotal) {
		this.voucherPrecioTotal = voucherPrecioTotal;
	}	
	
	@Column(allowsNull = "true")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named="Observaciones", hidden=Where.ALL_TABLES)
	private String voucherObservaciones;
	
	public String getVoucherObservaciones() {
		return voucherObservaciones;
	}
	
	public void setVoucherObservaciones(String voucherObservaciones) {
		this.voucherObservaciones=voucherObservaciones;
	}
	
	@Column(allowsNull = "true")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named="Estado")
	private EstadoVoucher voucherEstado;
	
	public EstadoVoucher getVoucherEstado() {
		return voucherEstado;
	}
	
	public void setVoucherEstado(EstadoVoucher voucherEstado) {
		this.voucherEstado = voucherEstado;
	}
	
	@Column(name="reservaId", allowsNull="true")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named="Reserva", hidden=Where.ALL_TABLES)
	private Reserva voucherReserva;
	
	public Reserva getVoucherReserva() {
		return voucherReserva;
	}
	
	public void setVoucherReserva(Reserva voucherReserva) {
		this.voucherReserva = voucherReserva;
	}
	
	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named="Usuario")
	private String voucherUsuario;
	
	public String getVoucherUsuario() {
		return voucherUsuario;
	}
	
	public void setVoucherUsuario(String voucherUsuario) {
		this.voucherUsuario=voucherUsuario;
	}
	
	// endregion

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherFechaEntrada")
	public Voucher actualizarFecha(@ParameterLayout(named = "Fecha de Entrada") final Date voucherFechaEntrada,
			@ParameterLayout(named = "Fecha de Salida") final Date voucherFechaSalida) {
		setVoucherFechaEntrada(voucherFechaEntrada);
		setVoucherFechaSalida(voucherFechaSalida);
		TipoPrecio p = TipoPrecio.Activo;
		if (getVoucherReserva().getClass()==ReservaAfiliado.class) {
			ReservaAfiliado ra = (ReservaAfiliado) getVoucherReserva();
			if (ra.getReservaCliente().getAfiliadoActivo()==true) {
				p = TipoPrecio.Activo;
			} else {
				p = TipoPrecio.Retirado;
			}
		} else {
			p = TipoPrecio.No_Afiliado;
		}
		setVoucherPrecioTotal(voucherRepository.calcularPrecioTotal(voucherFechaEntrada, voucherFechaSalida, this.getVoucherProducto(), p));
		setVoucherCantidadNoches(voucherRepository.calcularCantidadDeNoches(voucherFechaEntrada, voucherFechaSalida));
		return this;
	}

	public Date default0ActualizarFecha() {
		return getVoucherFechaEntrada();
	}
	
	public Date default1ActualizarFecha() {
		return getVoucherFechaSalida();
	} 
	
	public String validateActualizarFecha(final Date voucherFechaEntrada, final Date voucherFechaSalida) {
			if (voucherFechaEntrada.after(voucherFechaSalida))
				return "La fecha de salida no puede ser anterior a la de entrada";
			if (voucherRepository.corroborarDisponibilidadActualizar(this.getVoucherProducto(), voucherFechaEntrada, 
					voucherFechaSalida, this)==false)
				return "El producto ya se encuentra reservado en las fechas seleccionadas";
		return "";
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherCantidadPasajeros")
	public Voucher actualizarCantidadPasajeros(@ParameterLayout(named = "CantidadPasajeros") final int voucherCantidadPasajeros) {
		setVoucherCantidadPasajeros(voucherCantidadPasajeros);
		return this;
	}

	public int default0ActualizarCantidadPasajeros() {
		return getVoucherCantidadPasajeros();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherPrecioTotal")
	public Voucher actualizarPrecioTotal(@ParameterLayout(named = "Precio Total") final Double voucherPrecioTotal) {
		setVoucherPrecioTotal(voucherPrecioTotal);
		return this;
	}

	public Double default0ActualizarPrecioTotal() {
		return getVoucherPrecioTotal();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherObservaciones")
	public Voucher actualizarObservaciones(@ParameterLayout(named = "Observaciones", multiLine=6) final String voucherObservaciones) {
		setVoucherObservaciones(voucherObservaciones);
		return this;
	}

	public String default0ActualizarObservaciones() {
		return getVoucherObservaciones();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherEstado")
	public Voucher actualizarEstado(@ParameterLayout(named = "Estado", multiLine=6) final EstadoVoucher voucherEstado) {
		setVoucherEstado(voucherEstado);
		return this;
	}

	public EstadoVoucher default0ActualizarEstado() {
		return getVoucherEstado();
	}
	
	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return getVoucherProducto().toString() + " Entrada: " + sdf.format(getVoucherFechaEntrada()) + " Salida : " + sdf.format(getVoucherFechaSalida());
	}

	@Override
	public int compareTo(final Voucher voucher) {
		return this.voucherFechaEntrada.compareTo(voucher.voucherFechaEntrada);
	}

	// endregion

	// acciones
	@Action(publishing=Publishing.ENABLED, invokeOn=InvokeOn.OBJECT_AND_COLLECTION)
	public Voucher autorizarVoucher() {
		voucherEstado.autorizar(this);
		return this;
	}
	
	@Action(publishing=Publishing.ENABLED, invokeOn=InvokeOn.OBJECT_AND_COLLECTION)
	public Voucher desautorizarVoucher() {
		this.getVoucherEstado().desautorizar(this);
		return this;
	}
	
//    public String obtenerUsuario() {          
//    	String usuario = userService.getUser().getName();
//    	String rol = userService.getUser().getRoles().get(0).getName();
//        return "Usuario: "+usuario+" -- Rol: "+rol;
//    }
	
	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	ReservaRepository reservaRepository;
	
	@Inject
	ReporteRepository reporteRepository;
	
	@Inject
	UserService userService;

	// endregion
}
