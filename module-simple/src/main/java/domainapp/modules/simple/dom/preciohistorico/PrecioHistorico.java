package domainapp.modules.simple.dom.preciohistorico;

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
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import domainapp.modules.simple.dom.producto.Producto;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "PrecioHistorico")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "precioHistoricoId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarPreciosPorProducto", language = "JDOQL", value = "SELECT "
			+ "FROM domainapp.modules.simple.dom.preciohistorico.PrecioHistorico " + "WHERE precioHistoricoProducto == :precioHistoricoProducto &&  precioHistoricoHabilitado == :precioHistoricoHabilitado "),
		@javax.jdo.annotations.Query(name = "listarPreciosPorProductoPorTipoDeAfiliado", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.preciohistorico.PrecioHistorico " + "WHERE precioHistoricoProducto == :precioHistoricoProducto && precioHistoricoTipoPrecio == :precioHistoricoTipoPrecio &&  precioHistoricoHabilitado == :precioHistoricoHabilitado "),
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.preciohistorico.PrecioHistorico " + "WHERE precioHistoricoHabilitado == true "),
		@javax.jdo.annotations.Query(name = "listarInhabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.preciohistorico.PrecioHistorico " + "WHERE precioHistoricoHabilitado == false "),
		@javax.jdo.annotations.Query(name = "listarHabilitadosAfiliados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.preciohistorico.PrecioHistorico " + "WHERE precioHistoricoHabilitado == :precioHistoricoHabilitado && precioHistoricoTipoPrecio == :precioHistoricoTipoPrecio ")})
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class PrecioHistorico implements Comparable<PrecioHistorico>{
	
	public TranslatableString title() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return TranslatableString.tr("{name}", "name",getPrecioHistoricoProducto().toString() + " Precio: " + getPrecioHistoricoPrecio().toString() + " Desde: " + 
		sdf.format(getPrecioHistoricoFechaDesde()+ " Hasta: "+sdf.format(getPrecioHistoricoFechaHasta())));
	}
	
	public String cssClass() {
		String a;
		if (getPrecioHistoricoHabilitado() == true) {
			a = getPrecioHistoricoTipoPrecio().getNombre();
		}else {
			a = "inhabilitado";
		}
		return a;
	}

	public PrecioHistorico(Producto precioHistoricoProducto, Date precioHistoricoFechaDesde, Date precioHistoricoFechaHasta,
			TipoPrecio precioHistoricoTipoPrecio, Double precioHistoricoPrecio) {
		super();
		setPrecioHistoricoProducto(precioHistoricoProducto);
		setPrecioHistoricoFechaDesde(precioHistoricoFechaDesde);
		setPrecioHistoricoFechaHasta(precioHistoricoFechaHasta);
		setPrecioHistoricoTipoPrecio(precioHistoricoTipoPrecio);
		setPrecioHistoricoPrecio(precioHistoricoPrecio);
		setPrecioHistoricoHabilitado(true);
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Producto")
	private Producto precioHistoricoProducto;
	
	public Producto getPrecioHistoricoProducto() {
		return precioHistoricoProducto;
	}

	public void setPrecioHistoricoProducto(Producto precioHistoricoProducto) {
		this.precioHistoricoProducto = precioHistoricoProducto;
	}

	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named = "Fecha Desde")
	private Date precioHistoricoFechaDesde;
	
	public Date getPrecioHistoricoFechaDesde() {
		return precioHistoricoFechaDesde;
	}
	
	public void setPrecioHistoricoFechaDesde(Date precioHistoricoFechaDesde) {
		this.precioHistoricoFechaDesde = precioHistoricoFechaDesde;
	}
	
	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named = "Fecha Hasta")
	private Date precioHistoricoFechaHasta;
	
	public Date getPrecioHistoricoFechaHasta() {
		return precioHistoricoFechaHasta;
	}
	
	public void setPrecioHistoricoFechaHasta(Date precioHistoricoFechaHasta) {
		this.precioHistoricoFechaHasta = precioHistoricoFechaHasta;
	}

	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named = "Tipo de precio")
	private TipoPrecio precioHistoricoTipoPrecio;
	
	public TipoPrecio getPrecioHistoricoTipoPrecio() {
		return precioHistoricoTipoPrecio;
	}
	
	public void setPrecioHistoricoTipoPrecio(TipoPrecio precioHistoricoTipoPrecio) {
		this.precioHistoricoTipoPrecio = precioHistoricoTipoPrecio;
	}
	
	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named = "Precio")
	private Double precioHistoricoPrecio;
	
	public Double getPrecioHistoricoPrecio() {
		return precioHistoricoPrecio;
	}
	
	public void setPrecioHistoricoPrecio(Double precioHistoricoPrecio) {
		this.precioHistoricoPrecio = precioHistoricoPrecio;
	}

	@Column(allowsNull="false")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named = "Habilitado")
	private boolean precioHistoricoHabilitado;
	
	public boolean getPrecioHistoricoHabilitado() {
		return precioHistoricoHabilitado;
	}
	
	public void setPrecioHistoricoHabilitado(boolean precioHistoricoHabilitado) {
		this.precioHistoricoHabilitado = precioHistoricoHabilitado;
	}
	
	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarPrecioHistorico() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setPrecioHistoricoHabilitado(false);
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "precioHistoricoProducto")
	public PrecioHistorico actualizarPrecioHistoricoProducto(@ParameterLayout(named = "Producto") final Producto precioHistoricoProducto) {
		setPrecioHistoricoProducto(precioHistoricoProducto);
		return this;
	}
	
	public Producto default0ActualizarPrecioHistoricoProducto() {
		return getPrecioHistoricoProducto();
	}
	
	public String validateActualizarPrecioHistoricoProducto(final Producto precioHistoricoProducto) {
		if (precioHistoricoRepository.verificarActualizarPrecio(this, precioHistoricoProducto, getPrecioHistoricoTipoPrecio(), getPrecioHistoricoFechaDesde(), getPrecioHistoricoFechaHasta())==false)
			return "ERROR: Superposicion de otra fechas";
		return "";
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "precioHistoricoFechaDesde")
	public PrecioHistorico actualizarPrecioHistoricoFecha(@ParameterLayout(named = "Fecha Desde") final Date precioHistoricoFechaDesde,
			@ParameterLayout(named = "Fecha Hasta") final Date precioHistoricoFechaHasta) {
		setPrecioHistoricoFechaDesde(precioHistoricoFechaDesde);
		setPrecioHistoricoFechaHasta(precioHistoricoFechaHasta);
		return this;
	}

	public Date default0ActualizarPrecioHistoricoFecha() {
		return getPrecioHistoricoFechaDesde();
	}

	public Date default1ActualizarPrecioHistoricoFecha() {
		return getPrecioHistoricoFechaHasta();
	}
	
	public String validateActualizarPrecioHistoricoFecha(final Date precioHistoricoFechaDesde, final Date precioHistoricoFechaHasta) {
		if(precioHistoricoFechaDesde.after(precioHistoricoFechaHasta))
			return "ERROR: la fecha desde no puede ser posterior";
		if (precioHistoricoRepository.verificarActualizarPrecio(this, getPrecioHistoricoProducto(), getPrecioHistoricoTipoPrecio(), precioHistoricoFechaDesde, precioHistoricoFechaHasta)==false)
			return "ERROR: Superposicion de otra fechas";
		return "";
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "precioHistoricoTipoPrecio")
	public PrecioHistorico actualizarPrecioHistoricoTipoPrecio(@ParameterLayout(named = "Tipo Precio") final TipoPrecio precioHistoricoTipoPrecio) {
		setPrecioHistoricoTipoPrecio(precioHistoricoTipoPrecio);
		return this;
	}

	public TipoPrecio default0ActualizarPrecioHistoricoTipoPrecio() {
		return getPrecioHistoricoTipoPrecio();
	}
	
	public String validateActualizarPrecioHistoricoTipoPrecio(final TipoPrecio precioHistoricoTipoPrecio) {
		if (precioHistoricoRepository.verificarActualizarPrecio(this, getPrecioHistoricoProducto(), precioHistoricoTipoPrecio, getPrecioHistoricoFechaDesde(), getPrecioHistoricoFechaHasta())==false)
			return "ERROR: Superposicion de otra fechas";
		return "";
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "precioHistoricoPrecio")
	public PrecioHistorico actualizarPrecioHistoricoPrecio(@ParameterLayout(named = "Precio") final Double precioHistoricoPrecio) {
		setPrecioHistoricoPrecio(precioHistoricoPrecio);
		return this;
	}

	public Double default0ActualizarPrecioHistoricoPrecio() {
		return getPrecioHistoricoPrecio();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "precioHistoricoHabilitado")
	public PrecioHistorico actualizarPrecioHistoricoHabilitado(@ParameterLayout(named = "Habilitado") final boolean precioHistoricoHabilitado) {
		setPrecioHistoricoHabilitado(precioHistoricoHabilitado);
		return this;
	}

	public boolean default0ActualizarPrecioHistoricoHabilitado() {
		return getPrecioHistoricoHabilitado();
	}
	
	public String validateActualizarPrecioHistoricoHabilitado(final boolean precioHistoricoHabilitado) {
		if (precioHistoricoHabilitado == true) {
			if (precioHistoricoRepository.verificarActualizarPrecio(this, getPrecioHistoricoProducto(), getPrecioHistoricoTipoPrecio(), getPrecioHistoricoFechaDesde(), getPrecioHistoricoFechaHasta())==false)
				return "ERROR: Superposicion de otra fechas";
		}
		return "";
	}
	
	// region > toString, compareTo
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return getPrecioHistoricoProducto().toString() + " Precio: " + getPrecioHistoricoPrecio().toString() + " Desde: " + 
		sdf.format(getPrecioHistoricoFechaDesde()+ " Hasta: "+sdf.format(getPrecioHistoricoFechaHasta()));
	}

	@Override
	public int compareTo(final PrecioHistorico precioHistoricoServicio) {
		return this.precioHistoricoFechaDesde.compareTo(precioHistoricoServicio.precioHistoricoFechaDesde);
	}

	// endregion
	
	// acciones
	@ActionLayout(named = "Listar todos los Precios Historicos")
	@MemberOrder(sequence = "2")
	public List<PrecioHistorico> listar() {
		return precioHistoricoRepository.listar();
	}

	@ActionLayout(named = "Listar Precios Historicos Habilitados")
	@MemberOrder(sequence = "3")
	public List<PrecioHistorico> listarHabilitados() {
		return precioHistoricoRepository.listarHabilitados();
	}

	@ActionLayout(named = "Listar Precios Historicos Inhabilitados")
	@MemberOrder(sequence = "4")
	public List<PrecioHistorico> listarInhabilitados() {
		return precioHistoricoRepository.listarInhabilitados();
	}

	@Inject
	PrecioHistoricoRepository precioHistoricoRepository;
	
	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;
}
