package domainapp.modules.simple.dom.empresa;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
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
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.persona.Persona;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Empresa")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "empresaId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorRazonSocial", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.empresa.Empresa "
				+ "WHERE empresaRazonSocial.toLowerCase().indexOf(:empresaRazonSocial) >= 0 "),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.empresa.Empresa " + "WHERE personaActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.empresa.Empresa " + "WHERE personaActivo == false ") })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Empresa extends Persona implements Comparable<Empresa> {

	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("Empresa: {empresaRazonSocial}", "empresaRazonSocial",
				getEmpresaRazonSocial() + " Cuit/Cuil: " + getPersonaCuitCuil());
	}
	// endregion

	// region > constructor
	public Empresa() {
	}

	public Empresa(final String empresaRazonSocial) {
		setEmpresaRazonSocial(empresaRazonSocial);
	}

	public Empresa(String empresaRazonSocial, String personaCuitCuil, String personaDireccion, 
			Localidad personaLocalidad,	Long personaTelefonoFijo, Long personaTelefonoCelular, String personaMail) {
		setEmpresaRazonSocial(empresaRazonSocial);
		setPersonaCuitCuil(personaCuitCuil);
		setPersonaDireccion(personaDireccion);
		setPersonaLocalidad(personaLocalidad);
		setPersonaTelefonoFijo(personaTelefonoFijo);
		setPersonaTelefonoCelular(personaTelefonoCelular);
		setPersonaMail(personaMail);
		setPersonaActivo(true);
	}
	// endregion

	// region > name (read-only property)
	public static final int NAME_LENGTH = 40;

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Razon Social")
	private String empresaRazonSocial;

	public String getEmpresaRazonSocial() {
		return empresaRazonSocial;
	}

	public void setEmpresaRazonSocial(final String empresaRazonSocial) {
		this.empresaRazonSocial = empresaRazonSocial;
	}

	// endregion

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoLocalidad")
	public Empresa actualizarLocalidad(@ParameterLayout(named = "Localidades") final Localidad afiliadoLocalidad) {
		setPersonaLocalidad(afiliadoLocalidad);
		return this;
	}

	public List<Localidad> choices0ActualizarLocalidad() {
		return localidadRepository.listarActivos();
	}

	public Localidad default0ActualizarLocalidad() {
		return getPersonaLocalidad();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "empresaRazonSocial")
	public Empresa actualizarRazonSocial(@ParameterLayout(named = "Razon Social") final String empresaRazonSocial) {
		setEmpresaRazonSocial(empresaRazonSocial);
		return this;
	}

	public String default0ActualizarRazonSocial() {
		return getEmpresaRazonSocial();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoCuitCuil")
	public Empresa actualizarCuitCuil(@ParameterLayout(named = "Cuit/Cuil") final String afiliadoCuitCuil) {
		setPersonaCuitCuil(afiliadoCuitCuil);
		return this;
	}

	public String default0ActualizarCuitCuil() {
		return getPersonaCuitCuil();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoDireccion")
	public Empresa actualizarDireccion(@ParameterLayout(named = "Direccion") final String afiliadoDireccion) {
		setPersonaDireccion(afiliadoDireccion);
		return this;
	}

	public String default0ActualizarDireccion() {
		return getPersonaDireccion();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaTelefonoFijo")
	public Empresa actualizarTelefonos(@Nullable @ParameterLayout(named = "Teléfono Fijo") @Parameter(optionality = Optionality.OPTIONAL, maxLength=10) final Long personaTelefonoFijo,
			@Nullable @ParameterLayout(named = "Teléfono Celular") @Parameter(optionality = Optionality.OPTIONAL, maxLength=12) final Long personaTelefonoCelular) {
		setPersonaTelefonoFijo(personaTelefonoFijo);
		setPersonaTelefonoCelular(personaTelefonoCelular);
		return this;
	}

	public Long default0ActualizarTelefonos() {
		return getPersonaTelefonoFijo();
	}
	
	public Long default1ActualizarTelefonos() {
		return getPersonaTelefonoCelular();
	}
	
	public String validateActualizarTelefonos(final Long afiliadoTelefonoFijo, final Long afiliadoTelefonoCelular) {
		if (afiliadoTelefonoFijo == null & afiliadoTelefonoCelular == null) {
			return "Se tiene que cargar un numero de telefono, no puede quedar telefono fijo y telefono celular vacio";
		}
		
		if(afiliadoTelefonoFijo!=null)
		if (afiliadoTelefonoFijo.toString().length()<10) {
			return "Error: para el numero fijo se tiene que ingresar 10 digitos";
		}
		
		if (afiliadoTelefonoCelular!=null)
		if (afiliadoTelefonoCelular.toString().length()<12) {
			return "Error: para el numero celular se tiene que ingresar 12 digitos";
		}
		return "";
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoMail")
	public Empresa actualizarMail(@ParameterLayout(named = "Mail") final String afiliadoMail) {
		setPersonaMail(afiliadoMail);
		return this;
	}

	public String default0ActualizarMail() {
		return getPersonaMail();
	}

	public String validateActualizarMail(final String personaMail) {
		if (personaMail != null){
			// Patrón para validar el email
	        Pattern pattern = Pattern
	                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); 
	        
	        // El email a validar
	        String email = personaMail;
	 
	        Matcher mather = pattern.matcher(email);
	 
	        if (mather.find() == false) {
	            return "El mail ingresado es invalido";
	        }
		}
		return "";
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoActivo")
	public Empresa actualizarActivo(@ParameterLayout(named = "Activo") final boolean afiliadoActivo) {
		setPersonaActivo(afiliadoActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getPersonaActivo();
	}

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarEmpresa() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setPersonaActivo(false);
	}

	// endregion

	@Override
	public String toString() {
		return getEmpresaRazonSocial();
	}

	@Override
	public int compareTo(final Empresa empresa) {
		return this.empresaRazonSocial.compareTo(empresa.empresaRazonSocial);
	}

	// accion
	@ActionLayout(named = "Listar todos los empresas")
	@MemberOrder(sequence = "2")
	public List<Empresa> listar() {
		return empresaRepository.listar();
	}

	@ActionLayout(named = "Listar Clientes Activos")
	@MemberOrder(sequence = "3")
	public List<Empresa> listarEmpresaActivos() {
		return empresaRepository.listarActivos();
	}

	@ActionLayout(named = "Listar Clientes Inactivos")
	@MemberOrder(sequence = "4")
	public List<Empresa> listarEmpresaInactivos() {
		return empresaRepository.listarInactivos();
	}

	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	LocalidadRepository localidadRepository;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	EmpresaRepository empresaRepository;

	// endregion

}
