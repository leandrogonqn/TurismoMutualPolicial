package domainapp.modules.simple.dom.proveedor;

import java.util.List;

import javax.annotation.Nullable;
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

import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Proveedor")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "proveedorId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.proveedor.Proveedor "
				+ "WHERE proveedorNombre.toLowerCase().indexOf(:proveedorNombre) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.proveedor.Proveedor " + "WHERE proveedorCodigo == :proveedorCodigo"),
		@javax.jdo.annotations.Query(name = "buscarProveedorPorLocalidad", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.proveedor.Proveedor " + "WHERE proveedorLocalidad == :proveedorLocalidad"),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.proveedor.Proveedor " + "WHERE proveedorActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.proveedor.Proveedor " + "WHERE proveedorActivo == false ") })
@javax.jdo.annotations.Unique(name = "Proveedor_proveedorCodigo_UNQ", members = { "proveedorCodigo" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Proveedor implements Comparable<Proveedor> {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name", "Codigo: " + getProveedorCodigo() + " - " + getProveedorNombre());
	}
	// endregion

	public String cssClass() {
		return (getProveedorActivo() == true) ? "activo" : "inactivo";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Proveedor(int proveedorCodigo, String proveedorNombre, String proveedorDireccion, Localidad proveedorLocalidad, 
			String proveedorTelefono, String proveedorEncargado) {
		setProveedorCodigo(proveedorCodigo);
		setProveedorNombre(proveedorNombre);
		setProveedorDireccion(proveedorDireccion);
		setProveedorLocalidad(proveedorLocalidad);
		setProveedorTelefono(proveedorTelefono);
		setProveedorEncargado(proveedorEncargado);
		this.proveedorActivo = true;
	}
	
	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named = "Codigo")
	private int proveedorCodigo;
	
	public int getProveedorCodigo() {
		return proveedorCodigo;
	}
	
	public void setProveedorCodigo(int proveedorCodigo) {
		this.proveedorCodigo = proveedorCodigo;
	}

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String proveedorNombre;

	public String getProveedorNombre() {
		return proveedorNombre;
	}

	public void setProveedorNombre(String proveedorNombre) {
		this.proveedorNombre = proveedorNombre;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Direccion")
    private String proveedorDireccion;

    public String getProveedorDireccion() {
		return proveedorDireccion;
	}
	public void setProveedorDireccion(String proveedorDireccion) {
		this.proveedorDireccion = proveedorDireccion;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "true", name="localidadId")
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Localidad")
    private Localidad proveedorLocalidad;

	public Localidad getProveedorLocalidad() {
		return proveedorLocalidad;
	}

	public void setProveedorLocalidad(Localidad proveedorLocalidad) {
		this.proveedorLocalidad = proveedorLocalidad;
	}

    @javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Telefono")
    private String proveedorTelefono;

    public String getProveedorTelefono() {
		return proveedorTelefono;
	}
	public void setProveedorTelefono(String proveedorTelefono) {
		this.proveedorTelefono = proveedorTelefono;
	}	
	
    @javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Nombre del Encargado")
    private String proveedorEncargado;

    public String getProveedorEncargado() {
		return proveedorEncargado;
	}
	public void setProveedorEncargado(String proveedorEncargado) {
		this.proveedorEncargado = proveedorEncargado;
	}	

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Activo", hidden=Where.ALL_TABLES)
	private boolean proveedorActivo;

	public boolean getProveedorActivo() {
		return proveedorActivo;
	}

	public void setProveedorActivo(boolean proveedorActivo) {
		this.proveedorActivo = proveedorActivo;
	}

	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarProveedor() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setProveedorActivo(false);
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorCodigo")
	public Proveedor actualizarCodigo(@ParameterLayout(named = "Codigo") final int proveedorCodigo) {
		setProveedorCodigo(proveedorCodigo);
		return this;
	}

	public int default0ActualizarCodigo() {
		return getProveedorCodigo();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorNombre")
	public Proveedor actualizarNombre(@ParameterLayout(named = "Nombre") final String proveedorNombre) {
		setProveedorNombre(proveedorNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getProveedorNombre();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorDireccion")
	public Proveedor actualizarDireccion(@Nullable @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named = "Direccion") final String proveedorDireccion) {
		setProveedorDireccion(proveedorDireccion);
		return this;
	}

	public String default0ActualizarDireccion() {
		return getProveedorDireccion();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorLocalidad")
	public Proveedor actualizarLocalidad(@Nullable @ParameterLayout(named = "Localidad") @Parameter(optionality=Optionality.OPTIONAL) final Localidad proveedorLocalidad) {
		setProveedorLocalidad(proveedorLocalidad);
		return this;
	}

	public Localidad default0ActualizarLocalidad() {
		return getProveedorLocalidad();
	}
	
	public List<Localidad> choices0ActualizarLocalidad() {
		return localidadRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorTelefono")
	public Proveedor actualizarTelefono(@Nullable @ParameterLayout(named = "Telefono") @Parameter(optionality=Optionality.OPTIONAL) final String proveedorTelefono) {
		setProveedorTelefono(proveedorTelefono);
		return this;
	}

	public String default0ActualizarTelefono() {
		return getProveedorTelefono();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorEncargado")
	public Proveedor actualizarEncargado(@Nullable @ParameterLayout(named = "Nombre del Encargado") final String proveedorEncargado) {
		setProveedorEncargado(proveedorEncargado);
		return this;
	}

	public String default0ActualizarEncargado() {
		return getProveedorEncargado();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorActivo")
	public Proveedor actualizarActivo(@ParameterLayout(named = "Activo") final boolean proveedorActivo) {
		setProveedorActivo(proveedorActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getProveedorActivo();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		return getProveedorNombre();
	}

	@Override
	public int compareTo(final Proveedor proveedor) {
		return this.proveedorNombre.compareTo(proveedor.proveedorNombre);
	}

	// endregion

	// acciones
	@ActionLayout(named = "Listar todas los Proveedores")
	@MemberOrder(sequence = "2")
	public List<Proveedor> listar() {
		return proveedorRepository.listar();
	}

	@ActionLayout(named = "Listar Proveedores Activas")
	@MemberOrder(sequence = "3")
	public List<Proveedor> listarActivos() {
		return proveedorRepository.listarActivos();
	}

	@ActionLayout(named = "Listar Proveedores Inactivas")
	@MemberOrder(sequence = "4")
	public List<Proveedor> listarInactivos() {
		return proveedorRepository.listarInactivos();
	}

	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	ProveedorRepository proveedorRepository;
	
	@Inject
	LocalidadRepository localidadRepository;

	// endregion
}
