package domainapp.modules.simple.dom.producto;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
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

import domainapp.modules.simple.dom.afiliado.Estado;
import domainapp.modules.simple.dom.categoria.Categoria;
import domainapp.modules.simple.dom.categoria.CategoriaRepository;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.preciohistorico.PrecioHistorico;
import domainapp.modules.simple.dom.preciohistorico.PrecioHistoricoRepository;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.proveedor.Proveedor;
import domainapp.modules.simple.dom.proveedor.ProveedorRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Producto")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "productoId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.producto.Producto " + "WHERE productoCodigo == :productoCodigo"),
		@javax.jdo.annotations.Query(name = "buscarProductoPorLocalidad", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.producto.Producto " + "WHERE productoLocalidad == :productoLocalidad"),
		@javax.jdo.annotations.Query(name = "buscarProductoPorCategoria", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.producto.Producto " + "WHERE productoCategoria == :productoCategoria"),
		@javax.jdo.annotations.Query(name = "buscarProductoPorProveedor", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.producto.Producto " + "WHERE productoProveedor == :productoProveedor"),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.producto.Producto " + "WHERE productoActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.producto.Producto " + "WHERE productoActivo == false ") })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Producto implements Comparable<Producto> {
	// region > title
	public TranslatableString title() {
		TranslatableString s;
		if (getProductoAlojamientoPropio()==true) {
			s = TranslatableString.tr("Alojamiento propio - "+getProductoCategoria().toString()+" - "+getProductoLocalidad().toString());
		}else {
			s = TranslatableString.tr(getProductoProveedor().toString()+" - "+getProductoCategoria().toString()+" - "+getProductoLocalidad().toString());
		}
		return s;
	}
	// endregion

	public String cssClass() {
		return (getProductoActivo() == true) ? "activo" : "inactivo";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Producto(int productoCodigo, boolean productoAlojamientoPropio, Proveedor productoProveedor, Categoria productoCategoria,
			Localidad productoLocalidad, String productoPoliticas) {
		setProductoCodigo(productoCodigo);
		setProductoAlojamientoPropio(productoAlojamientoPropio);
		setProductoProveedor(productoProveedor);
		setProductoCategoria(productoCategoria);
		setProductoLocalidad(productoLocalidad);
		setProductoPoliticas(productoPoliticas);
		this.productoActivo = true;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Codigo")
	private int productoCodigo;

	public int getProductoCodigo() {
		return productoCodigo;
	}

	public void setProductoCodigo(int productoCodigo) {
		this.productoCodigo = productoCodigo;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Alojamiento propio")
	private boolean productoAlojamientoPropio;

	public boolean getProductoAlojamientoPropio() {
		return productoAlojamientoPropio;
	}

	public void setProductoAlojamientoPropio(boolean productoAlojamientoPropio) {
		this.productoAlojamientoPropio = productoAlojamientoPropio;
	}	

	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Proveedor")
	private Proveedor productoProveedor;

	public Proveedor getProductoProveedor() {
		return productoProveedor;
	}

	public void setProductoProveedor(Proveedor productoProveedor) {
		this.productoProveedor = productoProveedor;
	}	

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Categoria")
	private Categoria productoCategoria;

	public Categoria getProductoCategoria() {
		return productoCategoria;
	}

	public void setProductoCategoria(Categoria productoCategoria) {
		this.productoCategoria = productoCategoria;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Localidad")
	private Localidad productoLocalidad;

	public Localidad getProductoLocalidad() {
		return productoLocalidad;
	}

	public void setProductoLocalidad(Localidad productoLocalidad) {
		this.productoLocalidad = productoLocalidad;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Politicas")
	private String productoPoliticas;

	public String getProductoPoliticas() {
		return productoPoliticas;
	}

	public void setProductoPoliticas(String productoPoliticas) {
		this.productoPoliticas = productoPoliticas;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Activo", hidden=Where.ALL_TABLES)
	private boolean productoActivo;

	public boolean getProductoActivo() {
		return productoActivo;
	}

	public void setProductoActivo(boolean productoActivo) {
		this.productoActivo = productoActivo;
	}

	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarProducto() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setProductoActivo(false);
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoCodigo")
	public Producto actualizarCodigo(@ParameterLayout(named = "Codigo") final int productoCodigo) {
		setProductoCodigo(productoCodigo);
		return this;
	}

	public int default0ActualizarCodigo() {
		return getProductoCodigo();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoAlojamientoPropio")
	public Producto actualizarProveedor(@ParameterLayout(named = "AlojamientoPropio") final boolean productoAlojamientoPropio,
			@Nullable @ParameterLayout(named = "Proveedor") @Parameter(optionality=Optionality.OPTIONAL) final Proveedor productoProveedor) {
		setProductoProveedor(productoProveedor);
		return this;
	}
	
	public List<Proveedor> choices1ActualizarProveedor(final boolean productoAlojamientoPropio, final Proveedor productoProveedor) {
		if (productoAlojamientoPropio==true)
			return null;
		return proveedorRepository.listarActivos();
	} 
	
	public String validateActualizarProveedor(final boolean productoAlojamientoPropio, final Proveedor productoProveedor) {
		if(productoAlojamientoPropio==false & productoProveedor == null)
			return "Si el alojamiento no es propio el proveedor no puede ser nulo";
		return "";
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoCategoria")
	public Producto actualizarCategoria(@ParameterLayout(named = "Categoria") final Categoria productoCategoria) {
		setProductoCategoria(productoCategoria);
		return this;
	}

	public Categoria default0ActualizarCategoria() {
		return getProductoCategoria();
	}
	
	public List<Categoria> choices0ActualizarCategoria() {
		return categoriaRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoLocalidad")
	public Producto actualizarLocalidad(@ParameterLayout(named = "Localidad") final Localidad productoLocalidad) {
		setProductoLocalidad(productoLocalidad);
		return this;
	}

	public Localidad default0ActualizarLocalidad() {
		return getProductoLocalidad();
	}
	
	public List<Localidad> choices0ActualizarLocalidad() {
		return localidadRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoPoliticas")
	public Producto actualizarPoliticas(@ParameterLayout(named = "Politicas") final String productoPoliticas) {
		setProductoPoliticas(productoPoliticas);
		return this;
	}

	public String default0ActualizarPoliticas() {
		return getProductoPoliticas();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoActivo")
	public Producto actualizarActivo(@ParameterLayout(named = "Activo") final boolean productoActivo) {
		setProductoActivo(productoActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getProductoActivo();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		String s;
		if (getProductoAlojamientoPropio()==true) {
			s = "Alojamiento propio - "+getProductoCategoria().toString()+" - "+getProductoLocalidad().toString();
		}else {
			s= getProductoProveedor().toString()+" - "+getProductoCategoria().toString()+" - "+getProductoLocalidad().toString();
		}
		return s;
	}

	@Override
	public int compareTo(final Producto producto) {
        if (this.getProductoCodigo()<producto.getProductoCodigo()) {
            return -1;
        }
        if (this.getProductoCodigo()>producto.getProductoCodigo()) {
            return 1;
        }	
        return 0;
	}
	
	// endregion

	// acciones
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todos los Productos")
	@MemberOrder(sequence = "2")
	public List<Producto> listar() {
		return productoRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Productos Activas")
	@MemberOrder(sequence = "3")
	public List<Producto> listarActivos() {
		return productoRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Productos Inactivas")
	@MemberOrder(sequence = "4")
	public List<Producto> listarInactivos() {
		return productoRepository.listarInactivos();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Agregar Precio")
	public Producto agregarPrecio(@ParameterLayout(named = "Fecha desde") final Date precioHistoricoFechaDesde,
			@ParameterLayout(named = "Fecha Hasta") final Date precioHistoricoFechaHasta,
			@ParameterLayout(named = "Tipo de Precio") final TipoPrecio precioHistoricoTipoPrecio,
			@ParameterLayout(named = "Precio") final Double precioHistoricoPrecio) {
		precioHistoricoRepository.crear(this, precioHistoricoFechaDesde, precioHistoricoFechaHasta, precioHistoricoTipoPrecio, precioHistoricoPrecio);
		return this;
	}
	
	public String validateAgregarPrecio(final Date precioHistoricoFechaDesde, final Date precioHistoricoFechaHasta, 
			final TipoPrecio precioHistoricoTipoPrecio, final Double precioHistoricoPrecio) {
		if(precioHistoricoFechaDesde.after(precioHistoricoFechaHasta))
			return "ERROR: la fecha desde no puede ser posterior";
		if (precioHistoricoRepository.verificarCrearPrecio(this, precioHistoricoTipoPrecio, precioHistoricoFechaDesde, precioHistoricoFechaHasta)==false)
			return "ERROR: la fecha cargada se superpone con otra fecha";
		return "";
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Mostrar precio de producto por fecha")
	@MemberOrder(sequence = "1")
	public Double mostrarPrecioPorFecha(@ParameterLayout(named = "fecha") final Date fecha,
			@ParameterLayout(named = "Tipo Precio") final TipoPrecio precioHistoricoTipoPrecio) {
		return precioHistoricoRepository.mostrarPrecioPorFecha(this, fecha, precioHistoricoTipoPrecio);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar precios del producto")
	@MemberOrder(sequence = "1")
	public List<PrecioHistorico> listarPreciosPorProducto() {
		return precioHistoricoRepository.listarPreciosPorProducto(this, true);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Mostrar precio de producto por rango de fecha")
	@MemberOrder(sequence = "1")
	public List<PrecioHistorico> listarPreciosPorRangoDeFecha(@ParameterLayout(named = "Fecha Desde") final Date precioHistoricoFechaDesde,
			@ParameterLayout(named = "Fecha Hasta") final Date precioHistoricoFechaHasta) {
		return precioHistoricoRepository.listarPreciosPorRangoDeFecha(this, precioHistoricoFechaDesde, precioHistoricoFechaHasta);
	}
	
	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	ProductoRepository productoRepository;
	
	@Inject
	ProveedorRepository proveedorRepository;
	
	@Inject
	LocalidadRepository localidadRepository;
	
	@Inject
	CategoriaRepository categoriaRepository;
	
	@Inject
	PrecioHistoricoRepository precioHistoricoRepository;

	// endregion
}