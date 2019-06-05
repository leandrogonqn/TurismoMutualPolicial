package domainapp.modules.simple.dom.proveedor;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
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
		@javax.jdo.annotations.Query(name = "buscarPorRazonSocial", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.proveedor.Proveedor "
				+ "WHERE proveedorRazonSocial.toLowerCase().indexOf(:proveedorRazonSocial) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.proveedor.Proveedor " + "WHERE proveedorCodigo == :proveedorCodigo"),
		@javax.jdo.annotations.Query(name = "buscarProveedorPorLocalidad", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.proveedor.Proveedor " + "WHERE proveedorLocalidad == :proveedorLocalidad")})
@javax.jdo.annotations.Unique(name = "Proveedor_proveedorCodigo_UNQ", members = { "proveedorCodigo" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Proveedor implements Comparable<Proveedor> {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name", "Codigo: " + getProveedorCodigo() + " - " + getProveedorRazonSocial());
	}
	// endregion

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Proveedor(int proveedorCodigo, String proveedorCuit, String proveedorRazonSocial, String proveedorNombreComercial, String proveedorDireccion, 
			Localidad proveedorLocalidad, String proveedorTelefono, String proveedorMail, String proveedorWeb, String proveedorContacto) {
		setProveedorCodigo(proveedorCodigo);
		setProveedorCuit(proveedorCuit);
		setProveedorRazonSocial(proveedorRazonSocial);
		setProveedorNombreComercial(proveedorNombreComercial);
		setProveedorDireccion(proveedorDireccion);
		setProveedorLocalidad(proveedorLocalidad);
		setProveedorTelefono(proveedorTelefono);
		setProveedorMail(proveedorMail);
		setProveedorWeb(proveedorWeb);
		setProveedorContacto(proveedorContacto);
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
	
	 @javax.jdo.annotations.Column(allowsNull = "true")
	 @Property(
	           editing = Editing.DISABLED
	 )
	 @PropertyLayout(named="Cuit", hidden=Where.ALL_TABLES)
	 private String proveedorCuit;
	 public String getProveedorCuit() {
		return proveedorCuit;
	 }
	 public void setProveedorCuit(String proveedorCuit) {
		this.proveedorCuit = proveedorCuit;
	 }	

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Razon Social")
	private String proveedorRazonSocial;

	public String getProveedorRazonSocial() {
		return proveedorRazonSocial;
	}

	public void setProveedorRazonSocial(String proveedorRazonSocial) {
		this.proveedorRazonSocial = proveedorRazonSocial;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre Comercial")
	private String proveedorNombreComercial;

	public String getProveedorNombreComercial() {
		return proveedorNombreComercial;
	}

	public void setProveedorNombreComercial(String proveedorNombreComercial) {
		this.proveedorNombreComercial = proveedorNombreComercial;
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
    @PropertyLayout(named="Mail", hidden=Where.ALL_TABLES)
    private String proveedorMail;

    public String getProveedorMail() {
		return proveedorMail;
	}
	public void setProveedorMail(String proveedorMail) {
		this.proveedorMail = proveedorMail;
	}	
	
    @javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Web", hidden=Where.ALL_TABLES)
    private String proveedorWeb;

    public String getProveedorWeb() {
		return proveedorWeb;
	}
	public void setProveedorWeb(String proveedorWeb) {
		this.proveedorWeb = proveedorWeb;
	}	
	
    @javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Contacto")
    private String proveedorContacto;

    public String getProveedorContacto() {
		return proveedorContacto;
	}
	public void setProveedorContacto(String proveedorContacto) {
		this.proveedorContacto = proveedorContacto;
	}	

	// endregion

	// region > delete (action)
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorCodigo")
	public Proveedor actualizarCodigo(@ParameterLayout(named = "Codigo") final int proveedorCodigo) {
		setProveedorCodigo(proveedorCodigo);
		return this;
	}

	public int default0ActualizarCodigo() {
		return getProveedorCodigo();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorRazonSocial")
	public Proveedor actualizarRazonSocial(@ParameterLayout(named = "Razon Social") final String proveedorRazonSocial) {
		setProveedorRazonSocial(proveedorRazonSocial);
		return this;
	}

	public String default0ActualizarRazonSocial() {
		return getProveedorRazonSocial();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "proveedorNombreComercial")
	public Proveedor actualizarNombreComercial(@ParameterLayout(named = "Nombre Comercial") final String proveedorNombreComercial) {
		setProveedorNombreComercial(proveedorNombreComercial);
		return this;
	}

	public String default0ActualizarNombreComercial() {
		return getProveedorNombreComercial();
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
		return localidadRepository.listarHabilitados();
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
	public Proveedor actualizarContacto(@Nullable @ParameterLayout(named = "Contacto") final String proveedorContacto) {
		setProveedorContacto(proveedorContacto);
		return this;
	}

	public String default0ActualizarContacto() {
		return getProveedorContacto();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		return getProveedorRazonSocial();
	}

	@Override
	public int compareTo(final Proveedor proveedor) {
		return this.proveedorRazonSocial.compareTo(proveedor.proveedorRazonSocial);
	}

	// endregion

	// acciones

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
