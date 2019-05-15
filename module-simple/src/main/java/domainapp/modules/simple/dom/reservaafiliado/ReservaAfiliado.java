package domainapp.modules.simple.dom.reservaafiliado;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Join;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CollectionLayout;
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
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import domainapp.modules.simple.dom.afiliado.Afiliado;
import domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliado;
import domainapp.modules.simple.dom.empresa.Empresa;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.producto.ProductoRepository;
import domainapp.modules.simple.dom.reserva.Reserva;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "ReservaAfiliado")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "reservaId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.reservaafiliado.ReservaAfiliado " + "WHERE reservaActivo == :reservaActivo ")})
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
public class ReservaAfiliado extends Reserva implements Comparable<Reserva>{

	// region > title
	public TranslatableString title() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return TranslatableString.tr("{name}", "name", "Codigo: " + getReservaCodigo()+ " Cliente: "+getReservaCliente()+" Entrada: "
				+ ""+sdf.format(getReservaListaVoucher().get(0).getVoucherFechaEntrada())+"Salida: "+sdf.format(getReservaListaVoucher().get(0).getVoucherFechaSalida()));
	}
	// endregion

	public String cssClass() {
		return (getReservaActivo() == true) ? "activo" : "inactivo";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public ReservaAfiliado(int reservaCodigo, Date reservaFecha, Afiliado reservaCliente, final List<Voucher> reservaListaVoucher,
			CanalDePago reservaCanalDePago, String reservaMemo) {
		setReservaCodigo(reservaCodigo);
		setReservaFecha(reservaFecha);
		setReservaCliente(reservaCliente);
		setReservaListaVoucher(reservaListaVoucher);
		setReservaCanalDePago(reservaCanalDePago);
		setReservaMemo(reservaMemo);
		setReservaListaVoucher(reservaListaVoucher);
		setReservaActivo(true);
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Cliente")
	private Afiliado reservaCliente;

	public Afiliado getReservaCliente() {
		return reservaCliente;
	}

	public void setReservaCliente(Afiliado reservaCliente) {
		this.reservaCliente = reservaCliente;
	}
	
	@Column(allowsNull = "true")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named="Canal de Pago", hidden=Where.ALL_TABLES)
	private CanalDePago reservaCanalDePago;
	
	public CanalDePago getReservaCanalDePago() {
		return reservaCanalDePago;
	}
	
	public void setReservaCanalDePago(CanalDePago reservaCanalDePago) {
		this.reservaCanalDePago=reservaCanalDePago;
	}
	
	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarReserva() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setReservaActivo(false);
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaCodigo")
	public ReservaAfiliado actualizarReservaCodigo(@ParameterLayout(named = "Codigo") final int reservaCodigo) {
		setReservaCodigo(reservaCodigo);
		return this;
	}

	public int default0ActualizarReservaCodigo() {
		return getReservaCodigo();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaFecha")
	public ReservaAfiliado actualizarReservaFecha(@ParameterLayout(named = "Fecha") final Date reservaFecha) {
		setReservaFecha(reservaFecha);
		return this;
	}

	public Date default0ActualizarReservaFecha() {
		return getReservaFecha();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaCliente")
	public ReservaAfiliado actualizarReservaCliente(@ParameterLayout(named = "Cliente") final Afiliado reservaCliente) {
		setReservaCliente(reservaCliente);
		return this;
	}

	public Afiliado default0ActualizarReservaCliente() {
		return getReservaCliente();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaCanalDePago")
	public ReservaAfiliado actualizarReservaCanalDePago(@ParameterLayout(named = "Canal De Pago") final CanalDePago reservaCanalDePago) {
		setReservaCanalDePago(reservaCanalDePago);
		return this;
	}

	public CanalDePago default0ActualizarReservaCanalDePago() {
		return getReservaCanalDePago();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaMemo")
	public ReservaAfiliado actualizarReservaMemo(@ParameterLayout(named = "Memo") final String reservaMemo) {
		setReservaMemo(reservaMemo);
		return this;
	}

	public String default0ActualizarReservaMemo() {
		return getReservaMemo();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "reservaActivo")
	public ReservaAfiliado actualizarActivo(@ParameterLayout(named = "Activo") final boolean reservaActivo) {
		setReservaActivo(reservaActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getReservaActivo();
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
	public ReservaAfiliado crearVoucher(@ParameterLayout(named = "Producto") final Producto voucherProducto,
			@ParameterLayout(named = "Fecha de entrada") final Date voucherFechaEntrada,
			@ParameterLayout(named = "Fecha de salida") final Date voucherFechaSalida,
			@ParameterLayout(named = "Cantidad de pasajeros") final int voucherCantidadPasajeros,
			@Nullable @ParameterLayout(named = "Observaciones", multiLine=6) @Parameter(optionality=Optionality.OPTIONAL) final String voucherObservaciones) {
		TipoPrecio precioHistoricoTipoPrecio;
		if(this.getReservaCliente().getClass()==Afiliado.class)
			precioHistoricoTipoPrecio = TipoPrecio.Afiliado;
		else
			precioHistoricoTipoPrecio = TipoPrecio.No_Afiliado;
		Voucher v = voucherRepository.crear(voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadPasajeros, precioHistoricoTipoPrecio, voucherObservaciones);
		this.getReservaListaVoucher().add(v);
		this.setReservaListaVoucher(this.getReservaListaVoucher());
		return this;
	}
	
	public List<Producto> choices0CrearVoucher(){
		return productoRepository.listarActivos();
	}
	
	public String validateCrearVoucher(final Producto voucherProducto, final Date voucherFechaEntrada,
			final Date voucherFechaSalida, final int voucherCantidadPasajeros, final String voucherObservaciones) {
			List<Voucher> listaVoucher = voucherRepository.listarVoucherPorProducto(voucherProducto, true);
			if (voucherFechaEntrada.after(voucherFechaSalida))
				return "La fecha de salida no puede ser anterior a la de entrada";
			for(int indice = 0;indice<listaVoucher.size();indice++) {
				if (voucherFechaEntrada.before(listaVoucher.get(indice).getVoucherFechaSalida())
						& voucherFechaSalida.after(listaVoucher.get(indice).getVoucherFechaEntrada()))
					return "El producto ya se encuentra reservado en las fechas seleccionadas"; 
			}
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
	ReservaAfiliadoRepository reservaRepository;

	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	ProductoRepository productoRepository;

}
