package domainapp.modules.simple.dom.voucher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
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

import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.reserva.Reserva;
import domainapp.modules.simple.dom.reserva.ReservaRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Voucher")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "voucherId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.voucher.Voucher " + "WHERE voucherActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.voucher.Voucher " + "WHERE voucherActivo == false "),
		@javax.jdo.annotations.Query(name = "listarVoucherPorProducto", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.voucher.Voucher " + "WHERE voucherActivo == :voucherActivo && voucherProducto == :voucherProducto ")})
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Voucher implements Comparable<Voucher> {
	// region > title
	public TranslatableString title() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return TranslatableString.tr("{name}", "name", getVoucherProducto().toString() + ". Fecha de entrada: "+sdf.format(getVoucherFechaEntrada())+
				". Fecha de salida: "+sdf.format(getVoucherFechaSalida()));
	}
	// endregion

	public String cssClass() {
		return (getVoucherActivo() == true) ? "activo" : "inactivo";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Voucher(final Producto voucherProducto, final Date voucherFechaEntrada, final Date voucherFechaSalida, final int voucherCantidadNoches,
			final int voucherCantidadPasajeros, final Double voucherPrecioTotal, final String voucherObservaciones, final String voucherMemo) {
		setVoucherProducto(voucherProducto);
		setVoucherFechaEntrada(voucherFechaEntrada);
		setVoucherFechaSalida(voucherFechaSalida);
		setVoucherCantidadNoches(voucherCantidadNoches);
		setVoucherCantidadPasajeros(voucherCantidadPasajeros);
		setVoucherPrecioTotal(voucherPrecioTotal);
		setVoucherObservaciones(voucherObservaciones);
		setVoucherMemo(voucherMemo);
		this.voucherActivo = true;
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
	@PropertyLayout(named="Memo", hidden=Where.ALL_TABLES)
	private String voucherMemo;
	
	public String getVoucherMemo() {
		return voucherMemo;
	}
	
	public void setVoucherMemo(String voucherMemo) {
		this.voucherMemo = voucherMemo;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Activo", hidden=Where.ALL_TABLES)
	private boolean voucherActivo;

	public boolean getVoucherActivo() {
		return voucherActivo;
	}

	public void setVoucherActivo(boolean voucherActivo) {
		this.voucherActivo = voucherActivo;
	}

	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarVoucher() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setVoucherActivo(false);
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherFechaEntrada")
	public Voucher actualizarFechaEntrada(@ParameterLayout(named = "Fecha de Entrada") final Date voucherFechaEntrada) {
		setVoucherFechaEntrada(voucherFechaEntrada);
		return this;
	}

	public Date default0ActualizarFechaEntrada() {
		return getVoucherFechaEntrada();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherFechaSalida")
	public Voucher actualizarFechaSalida(@ParameterLayout(named = "Fecha de Salida") final Date voucherFechaSalida) {
		setVoucherFechaSalida(voucherFechaSalida);
		return this;
	}

	public Date default0ActualizarFechaSalida() {
		return getVoucherFechaSalida();
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
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherMemo")
	public Voucher actualizarMemo(@ParameterLayout(named = "Memo", multiLine=6) final String voucherMemo) {
		setVoucherMemo(voucherMemo);
		return this;
	}

	public String default0ActualizarMemo() {
		return getVoucherMemo();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherActivo")
	public Voucher actualizarActivo(@ParameterLayout(named = "Activo") final boolean voucherActivo) {
		setVoucherActivo(voucherActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getVoucherActivo();
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
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todas las Vouchers")
	@MemberOrder(sequence = "2")
	public List<Voucher> listar() {
		return voucherRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Voucher Activas")
	@MemberOrder(sequence = "3")
	public List<Voucher> listarActivos() {
		return voucherRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Vouchers Inactivas")
	@MemberOrder(sequence = "4")
	public List<Voucher> listarInactivos() {
		return voucherRepository.listarInactivos();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Volver a la reserva")
	@MemberOrder(sequence = "5")
	public Reserva volverAReserva() {
		return reservaRepository.buscarReservaPorVoucher(this);
	}

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

	// endregion
}
