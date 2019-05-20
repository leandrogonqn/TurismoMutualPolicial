package domainapp.modules.simple.dom.provincia;

import java.util.List;
import javax.inject.Inject;
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

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Provincias")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "provinciaId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.provincia.Provincia "
				+ "WHERE provinciasNombre.toLowerCase().indexOf(:provinciasNombre) >= 0 "),

		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.provincia.Provincia " + "WHERE provinciaHabilitado == true "),
		@javax.jdo.annotations.Query(name = "listarInhabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.provincia.Provincia " + "WHERE provinciaHabilitado == false ") })
@javax.jdo.annotations.Unique(name = "Provincias_provinciasNombre_UNQ", members = { "provinciasNombre" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Provincia implements Comparable<Provincia> {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name", getProvinciasNombre());
	}
	// endregion

	public String cssClass() {
		return (getProvinciaHabilitado() == true) ? "habilitado" : "inhabilitado";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Provincia(String provinciaNombre) {
		setProvinciasNombre(provinciaNombre);
		this.provinciaHabilitado = true;
	}

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String provinciasNombre;

	public String getProvinciasNombre() {
		return provinciasNombre;
	}

	public void setProvinciasNombre(String provinciasNombre) {
		this.provinciasNombre = provinciasNombre;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Habilitado", hidden=Where.ALL_TABLES)
	private boolean provinciaHabilitado;

	public boolean getProvinciaHabilitado() {
		return provinciaHabilitado;
	}

	public void setProvinciaHabilitado(boolean provinciaHabilitado) {
		this.provinciaHabilitado = provinciaHabilitado;
	}

	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarProvincia() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setProvinciaHabilitado(false);
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "provinciaNombre")
	public Provincia actualizarNombre(@ParameterLayout(named = "Nombre") final String provinciaNombre) {
		setProvinciasNombre(provinciaNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getProvinciasNombre();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "provinciaHabilitado")
	public Provincia actualizarHabilitado(@ParameterLayout(named = "Habilitado") final boolean provinciaHabilitado) {
		setProvinciaHabilitado(provinciaHabilitado);
		return this;
	}

	public boolean default0ActualizarHabilitado() {
		return getProvinciaHabilitado();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		return getProvinciasNombre();
	}

	@Override
	public int compareTo(final Provincia provincia) {
		return this.provinciasNombre.compareTo(provincia.provinciasNombre);
	}

	// endregion

	// acciones
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todas las Provincias")
	@MemberOrder(sequence = "2")
	public List<Provincia> listar() {
		return provinciasRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Provincia Activas")
	@MemberOrder(sequence = "3")
	public List<Provincia> listarHabilitados() {
		return provinciasRepository.listarHabilitados();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Provincias Inactivas")
	@MemberOrder(sequence = "4")
	public List<Provincia> listarInhabilitados() {
		return provinciasRepository.listarInhabilitados();
	}

	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	ProvinciaRepository provinciasRepository;

	// endregion
}
