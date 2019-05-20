package domainapp.modules.simple.dom.localidad;

import java.util.List;
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

import domainapp.modules.simple.dom.proveedor.Proveedor;
import domainapp.modules.simple.dom.proveedor.ProveedorRepository;
import domainapp.modules.simple.dom.provincia.Provincia;
import domainapp.modules.simple.dom.provincia.ProvinciaRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Localidades")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "localidadId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.localidad.Localidad "
				+ "WHERE localidadesNombre.toLowerCase().indexOf(:localidadesNombre) >= 0 "),
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.localidad.Localidad " + "WHERE localidadHabilitado == true "),
		@javax.jdo.annotations.Query(name = "listarInhabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.localidad.Localidad " + "WHERE localidadHabilitado == false ") })
@javax.jdo.annotations.Unique(name = "Localidades_localidadesNombre_UNQ", members = { "localidadesNombre" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Localidad implements Comparable<Localidad> {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name",
				getLocalidadesNombre() + " - " + this.getLocalidadProvincia().getProvinciasNombre());
	}
	// endregion

	public String cssClass() {
		return (getLocalidadHabilitado() == true) ? "habilitado" : "inhabilitado";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Localidad(String localidadNombre, Provincia localidadProvincia) {
		setLocalidadesNombre(localidadNombre);
		setLocalidadProvincia(localidadProvincia);
		this.localidadHabilitado = true;
	}

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String localidadesNombre;

	public String getLocalidadesNombre() {
		return localidadesNombre;
	}

	public void setLocalidadesNombre(String localidadesNombre) {
		this.localidadesNombre = localidadesNombre;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", name = "provinciaId")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Provincia")
	private Provincia localidadProvincia;

	public Provincia getLocalidadProvincia() {
		return localidadProvincia;
	}

	public void setLocalidadProvincia(Provincia localidadProvincia) {
		this.localidadProvincia = localidadProvincia;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Habilitado", hidden=Where.ALL_TABLES)
	private boolean localidadHabilitado;

	public boolean getLocalidadHabilitado() {
		return localidadHabilitado;
	}

	public void setLocalidadHabilitado(boolean localidadHabilitado) {
		this.localidadHabilitado = localidadHabilitado;
	}

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarLocalidad() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setLocalidadHabilitado(false);
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "localidadNombre")
	public Localidad actualizarNombre(@ParameterLayout(named = "Nombre") final String localidadNombre) {
		setLocalidadesNombre(localidadNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getLocalidadesNombre();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "LocalidadProvincia")
	public Localidad actualizarProvincia(@ParameterLayout(named = "Provincia") final Provincia LocalidadProvincia) {
		setLocalidadProvincia(LocalidadProvincia);
		return this;
	}

	public List<Provincia> choices0ActualizarProvincia() {
		return provinciaRepository.listarHabilitados();
	}

	public Provincia default0ActualizarProvincia() {
		return getLocalidadProvincia();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "localidadHabilitado")
	public Localidad actualizarHabilitado(@ParameterLayout(named = "Habilitado") final boolean localidadHabilitado) {
		setLocalidadHabilitado(localidadHabilitado);
		return this;
	}

	public boolean default0ActualizarHabilitado() {
		return getLocalidadHabilitado();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		return getLocalidadesNombre();
	}

	@Override
	public int compareTo(final Localidad localidad) {
		return this.localidadesNombre.compareTo(localidad.localidadesNombre);
	}
	// endregion

	// acciones
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todas las Localidades")
	@MemberOrder(sequence = "2")
	public List<Localidad> listar() {
		return localidadesRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar las Localidades Habilitadas")
	@MemberOrder(sequence = "3")
	public List<Localidad> listarHabilitados() {
		return localidadesRepository.listarHabilitados();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar las Localidades Inhabilitadas")
	@MemberOrder(sequence = "4")
	public List<Localidad> listarInhabilitados() {
		return localidadesRepository.listarInhabilitados();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar prestadores en esta Localidad")
	public List<Proveedor> buscarPrestadorPorLocalidad() {
		return proveedorRepository.buscarProveedorPorLocalidad(this);
	}
	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	ProvinciaRepository provinciaRepository;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	LocalidadRepository localidadesRepository;
	
	@Inject
	ProveedorRepository proveedorRepository;

	// endregion
}
