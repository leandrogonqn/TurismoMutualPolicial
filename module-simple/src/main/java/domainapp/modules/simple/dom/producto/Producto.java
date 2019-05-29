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
import domainapp.modules.simple.dom.categoria.Categoria;
import domainapp.modules.simple.dom.categoria.CategoriaRepository;
import domainapp.modules.simple.dom.empresa.Empresa;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.politicas.Politicas;
import domainapp.modules.simple.dom.politicas.PoliticasRepository;
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
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.producto.Producto " + "WHERE productoHabilitado == true "),
		@javax.jdo.annotations.Query(name = "listarInhabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.producto.Producto " + "WHERE productoHabilitado == false ") })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Producto implements Comparable<Producto> {
	// region > title
	public TranslatableString title() {
		TranslatableString s;
		if (getProductoAlojamientoPropio()==true) {
			s = TranslatableString.tr("Codigo: "+getProductoCodigo()+" Alojamiento propio - "+getProductoCategoria().toString()+" - "+getProductoLocalidad().toString());
		}else {
			s = TranslatableString.tr("Codigo: "+getProductoCodigo()+ " "+ getProductoProveedor().toString()+" - "+getProductoCategoria().toString()+" - "+getProductoLocalidad().toString());
		}
		return s;
	}
	// endregion

	public String cssClass() {
		return (getProductoHabilitado() == true) ? "habilitado" : "inhabilitado";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Producto(int productoCodigo, boolean productoAlojamientoPropio, Proveedor productoProveedor, Categoria productoCategoria,
			String productoDireccion, Localidad productoLocalidad, String productoTelefono, List<Politicas> listaPoliticas) {
		setProductoCodigo(productoCodigo);
		setProductoAlojamientoPropio(productoAlojamientoPropio);
		setProductoProveedor(productoProveedor);
		setProductoCategoria(productoCategoria);
		setPersonaDireccion(productoTelefono);
		setProductoLocalidad(productoLocalidad);
		setPersonaTelefono(productoTelefono);
		if (productoAlojamientoPropio==true & !listaPoliticas.isEmpty()) {
			setProductoPoliticas(listaPoliticas.get(0));
		}
		this.productoHabilitado = true;
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
	
	@javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Direccion", hidden = Where.ALL_TABLES)
	private String personaDireccion;

	public String getPersonaDireccion() {
		return personaDireccion;
	}

	public void setPersonaDireccion(String personaDireccion) {
		this.personaDireccion = personaDireccion;
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

	@javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Telefono")
	private String personaTelefono;

	public String getPersonaTelefono() {
		return personaTelefono;
	}

	public void setPersonaTelefono(String personaTelefono) {
		this.personaTelefono = personaTelefono;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Necesita Autorizacion?")
	private boolean productoAutorizacion;

	public boolean getProductoAutorizacion() {
		return productoAutorizacion;
	}

	public void setProductoAutorizacion(boolean productoAutorizacion) {
		this.productoAutorizacion = productoAutorizacion;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Politicas")
	private Politicas productoPoliticas;

	public Politicas getProductoPoliticas() {
		return productoPoliticas;
	}

	public void setProductoPoliticas(Politicas productoPoliticas) {
		this.productoPoliticas = productoPoliticas;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Habilitado", hidden=Where.ALL_TABLES)
	private boolean productoHabilitado;

	public boolean getProductoHabilitado() {
		return productoHabilitado;
	}

	public void setProductoHabilitado(boolean productoHabilitado) {
		this.productoHabilitado = productoHabilitado;
	}

	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarProducto() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setProductoHabilitado(false);
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
		return proveedorRepository.listarHabilitados();
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
		return categoriaRepository.listarHabilitados();
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
		return localidadRepository.listarHabilitados();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoDireccion")
	public Producto actualizarDireccion(@ParameterLayout(named = "Direccion") final String afiliadoDireccion) {
		setPersonaDireccion(afiliadoDireccion);
		return this;
	}

	public String default0ActualizarDireccion() {
		return getPersonaDireccion();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoTelefono")
	public Producto actualizarTelefono(@ParameterLayout(named = "Telefono") final String afiliadoTelefono) {
		setPersonaTelefono(afiliadoTelefono);
		return this;
	}

	public String default0ActualizarTelefono() {
		return getPersonaTelefono();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoAutorizacion")
	public Producto actualizarAutorizacion(@ParameterLayout(named = "Necesita autorizacion?") final boolean productoAutorizacion) {
		setProductoAutorizacion(productoAutorizacion);
		return this;
	}

	public boolean default0ActualizarAutorizacion() {
		return getProductoAutorizacion();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoPoliticas")
	public Producto actualizarPoliticas(@ParameterLayout(named = "Politicas") final Politicas productoPoliticas) {
		setProductoPoliticas(productoPoliticas);
		return this;
	}

	public Politicas default0ActualizarPoliticas() {
		return getProductoPoliticas();
	}
	
	public List<Politicas> choices0ActualizarPoliticas() {
		return politicasRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "productoHabilitado")
	public Producto actualizarHabilitado(@ParameterLayout(named = "Habilitado") final boolean productoHabilitado) {
		setProductoHabilitado(productoHabilitado);
		return this;
	}

	public boolean default0ActualizarHabilitado() {
		return getProductoHabilitado();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		String s;
		if (getProductoAlojamientoPropio()==true) {
			s = "Codigo: "+getProductoCodigo()+" Alojamiento propio - "+getProductoCategoria().toString()+" - "+getProductoLocalidad().toString();
		}else {
			s= "Codigo: "+getProductoCodigo()+" "+getProductoProveedor().toString()+" - "+getProductoCategoria().toString()+" - "+getProductoLocalidad().toString();
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
	public List<Producto> listarHabilitados() {
		return productoRepository.listarHabilitados();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Productos Inactivas")
	@MemberOrder(sequence = "4")
	public List<Producto> listarInhabilitados() {
		return productoRepository.listarInhabilitados();
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
	
	@Inject
	PoliticasRepository politicasRepository;

	// endregion
}
