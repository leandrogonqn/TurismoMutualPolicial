package domainapp.modules.simple.dom.preciohistorico;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import domainapp.modules.simple.dom.producto.Producto;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "PrecioHistorico")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "precioHistoricoId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarPreciosPorProducto", language = "JDOQL", value = "SELECT "
			+ "FROM domainapp.modules.simple.dom.preciohistorico.PrecioHistorico " + "WHERE precioHistoricoProducto == :precioHistoricoProducto"),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.preciohistorico.PrecioHistorico " + "WHERE precioHistoricoActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.preciohistorico.PrecioHistorico " + "WHERE precioHistoricoActivo == false ") })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class PrecioHistorico implements Comparable<PrecioHistorico>{

	public PrecioHistorico(Producto precioHistoricoProducto, Date precioHistoricoFechaDesde, Date precioHistoricoFechaHasta,
			Double precioHistoricoPrecio) {
		super();
		setPrecioHistoricoProducto(precioHistoricoProducto);
		setPrecioHistoricoFechaDesde(precioHistoricoFechaDesde);
		setPrecioHistoricoFechaHasta(precioHistoricoFechaHasta);
		setPrecioHistoricoPrecio(precioHistoricoPrecio);
		setPrecioHistoricoActivo(true);
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
	@PropertyLayout(named = "Activo")
	private boolean precioHistoricoActivo;
	
	public boolean getPrecioHistoricoActivo() {
		return precioHistoricoActivo;
	}
	
	public void setPrecioHistoricoActivo(boolean precioHistoricoActivo) {
		this.precioHistoricoActivo = precioHistoricoActivo;
	}

	// region > toString, compareTo
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return "Precio: " + getPrecioHistoricoPrecio().toString() + " Desde: " + 
		sdf.format(getPrecioHistoricoFechaDesde());
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

	@ActionLayout(named = "Listar Precios Historicos Activos")
	@MemberOrder(sequence = "3")
	public List<PrecioHistorico> listarActivos() {
		return precioHistoricoRepository.listarActivos();
	}

	@ActionLayout(named = "Listar Precios Historicos Inactivos")
	@MemberOrder(sequence = "4")
	public List<PrecioHistorico> listarInactivos() {
		return precioHistoricoRepository.listarInactivos();
	}

	@Inject
	PrecioHistoricoRepository precioHistoricoRepository;
}
