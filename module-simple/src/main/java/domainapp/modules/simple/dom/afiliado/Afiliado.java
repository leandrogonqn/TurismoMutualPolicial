package domainapp.modules.simple.dom.afiliado;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
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

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Afiliado")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "afiliadoId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorLP", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado "
				+ "WHERE afiliadoLP.toLowerCase().indexOf(:afiliadoLP) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado "
				+ "WHERE personaNombre.toLowerCase().indexOf(:personaNombre) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorDNI", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado " + "WHERE personaDni == :personaDni"),
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado " + "WHERE personaHabilitado == true "),
		@javax.jdo.annotations.Query(name = "listarInhabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado " + "WHERE personaHabilitado == false ") })
@javax.jdo.annotations.Unique(name = "DNI_Apellido_UNQ1", members = { "personaJuridicaDni", "personaJuridicaApellido" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Afiliado implements Comparable<Afiliado> {

	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("Afiliado " + getAfiliadoEstado().getNombre() + 
				" - LP "+getAfiliadoLP()+ " - "+
				getPersonaJuridicaNombre() + " " + getPersonaJuridicaApellido());
	}
	// endregion
	
	public String cssClass() {
		return (getPersonaHabilitado() == true) ? "habilitado" : "inhabilitado";
	}
	
	public String iconName() {
		return (getAfiliadoEstado() == TipoAfiliado.Activo) ? "A" : "R";
	}

	// region > constructor
	public Afiliado() {
	}

	public Afiliado(final String afiliadoNombre) {
		setPersonaJuridicaNombre(afiliadoNombre);
	}

	public Afiliado(TipoAfiliado afiliadoEstado, String afiliadoLP, int afiliadoDni, String afiliadoApellido, String afiliadoNombre,
			String afiliadoCuitCuil, String afiliadoDireccion, Localidad afiliadoLocalidad, String afiliadoTelefono, 
			String afiliadoMail, String afiliadoCBU) {
		super();
		setAfiliadoEstado(afiliadoEstado);
		setAfiliadoLP(afiliadoLP);
		setPersonaJuridicaDni(afiliadoDni);
		setPersonaJuridicaApellido(afiliadoApellido);
		setPersonaJuridicaNombre(afiliadoNombre);
		setPersonaCuitCuil(afiliadoCuitCuil);
		setPersonaDireccion(afiliadoDireccion);
		setPersonaLocalidad(afiliadoLocalidad);
		setPersonaLocalidad(afiliadoLocalidad);
		setPersonaTelefono(afiliadoTelefono);
		setPersonaMail(afiliadoMail);
		setAfiliadoCBU(afiliadoCBU);
		setPersonaHabilitado(true);
	}
	// endregion

	// region > name (read-only property)
	public static final int NAME_LENGTH = 40;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Estado")
	private TipoAfiliado afiliadoEstado;

	public TipoAfiliado getAfiliadoEstado() {
		return afiliadoEstado;
	}

	public void setAfiliadoEstado(TipoAfiliado afiliadoEstado) {
		this.afiliadoEstado = afiliadoEstado;
	}

	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "LP")
	private String afiliadoLP;

	public String getAfiliadoLP() {
		return afiliadoLP;
	}

	public void setAfiliadoLP(String afiliadoLP) {
		this.afiliadoLP = afiliadoLP;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "DNI")
	private int personaJuridicaDni;

	public int getPersonaJuridicaDni() {
		return personaJuridicaDni;
	}

	public void setPersonaJuridicaDni(int personaJuridicaDni) {
		this.personaJuridicaDni = personaJuridicaDni;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Apellido")
	private String personaJuridicaApellido;

	public String getPersonaJuridicaApellido() {
		return personaJuridicaApellido;
	}

	public void setPersonaJuridicaApellido(String personaJuridicaApellido) {
		this.personaJuridicaApellido = personaJuridicaApellido;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String personaJuridicaNombre;

	public String getPersonaJuridicaNombre() {
		return personaJuridicaNombre;
	}

	public void setPersonaJuridicaNombre(final String personaJuridicaNombre) {
		this.personaJuridicaNombre = personaJuridicaNombre;
	}
	
	 @javax.jdo.annotations.Column(allowsNull = "true")
	 @Property(
	           editing = Editing.DISABLED
	 )
	 @PropertyLayout(named="Cuit/Cuil", hidden=Where.ALL_TABLES)
	 private String personaCuitCuil;
	 public String getPersonaCuitCuil() {
		return personaCuitCuil;
	 }
	 public void setPersonaCuitCuil(String personaCuitCuil) {
		this.personaCuitCuil = personaCuitCuil;
	 }	
	 
	@javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
  @Property(
          editing = Editing.DISABLED
  )
  @PropertyLayout(named="Direccion", hidden=Where.ALL_TABLES)
  private String personaDireccion;

  public String getPersonaDireccion() {
		return personaDireccion;
	}
	public void setPersonaDireccion(String personaDireccion) {
		this.personaDireccion = personaDireccion;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "true", name="localidadId")
  @Property(
          editing = Editing.DISABLED
  )
  @PropertyLayout(named="Localidad", hidden=Where.ALL_TABLES)
  private Localidad personaLocalidad;

	public Localidad getPersonaLocalidad() {
		return personaLocalidad;
	}

	public void setPersonaLocalidad(Localidad personaLocalidad) {
		this.personaLocalidad = personaLocalidad;
	}

  @javax.jdo.annotations.Column(allowsNull = "false")
  @Property(
          editing = Editing.DISABLED
  )
  @PropertyLayout(named="Telefono")
  private String personaTelefono;

  public String getPersonaTelefono() {
		return personaTelefono;
	}
	public void setPersonaTelefono(String personaTelefono) {
		this.personaTelefono = personaTelefono;
	}	
	
  @javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
  @Property(
          editing = Editing.DISABLED
  )
  @PropertyLayout(named="Mail", hidden=Where.ALL_TABLES)
  private String personaMail;

  public String getPersonaMail() {
		return personaMail;
	}
	public void setPersonaMail(String personaMail) {
		this.personaMail = personaMail;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED, hidden=Where.EVERYWHERE)
	@PropertyLayout(named = "CBU")
	private String afiliadoCBU;

	public String getAfiliadoCBU() {
		return afiliadoCBU;
	}

	public void setAfiliadoCBU(String afiliadoCBU) {
		this.afiliadoCBU = afiliadoCBU;
	}
	
   @javax.jdo.annotations.Column(allowsNull = "false")
   @Property(
           editing = Editing.DISABLED
   )
   @PropertyLayout(named="Habilitado", hidden=Where.ALL_TABLES)
   private boolean personaHabilitado;

   public boolean getPersonaHabilitado() {
		return personaHabilitado;
	}
	public void setPersonaHabilitado(boolean personaHabilitado) {
		this.personaHabilitado = personaHabilitado;
	}	
	
	// endregion
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoEstado")
	public Afiliado actualizarEstado(@ParameterLayout(named = "Estado") final TipoAfiliado afiliadoEstado) {
		setAfiliadoEstado(afiliadoEstado);
		return this;
	}
	
	public TipoAfiliado default0ActualizarEstado() {
		return getAfiliadoEstado();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoLP")
	public Afiliado actualizarLP(@Nullable @ParameterLayout(named = "LP", typicalLength=6) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 6) final String afiliadoLP) {
		setAfiliadoLP(afiliadoLP);
		return this;
	}

	public String default0ActualizarLP() {
		return getAfiliadoLP();
	}
	
	public String validateActualizarLP(final String numeroLP) {
        if (isNumeric(numeroLP) == false) {
            return "Todos los caracteres del LP deben ser numericos";
        }
		if (numeroLP.length()<=5) {
			return "LP debe contener 6 digitos";
		}
		return "";
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoNombre")
	public Afiliado actualizarNombre(@ParameterLayout(named = "Nombre") final String afiliadoNombre) {
		setPersonaJuridicaNombre(afiliadoNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getPersonaJuridicaNombre();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoApellido")
	public Afiliado actualizarApellido(@ParameterLayout(named = "Apellido") final String afiliadoApellido) {
		setPersonaJuridicaApellido(afiliadoApellido);
		return this;
	}

	public String default0ActualizarApellido() {
		return getPersonaJuridicaApellido();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoDni")
	public Afiliado actualizarDni(@ParameterLayout(named = "Numero de Documento") @Parameter(maxLength=8) final int afiliadoDni) {
		setPersonaJuridicaDni(afiliadoDni);
		return this;
	}

	public int default0ActualizarDni() {
		return getPersonaJuridicaDni();
	}
	
	public String validateActualizarDni(final int afiliadoDni) {
		if (Integer.toString(afiliadoDni).length()<6)
			return "Largo del dni incorrecto";
		return "";
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoCuitCuil")
	public Afiliado actualizarCuitCuil(@ParameterLayout(named = "Cuit/Cuil") final String afiliadoCuitCuil) {
		setPersonaCuitCuil(afiliadoCuitCuil);
		return this;
	}

	public String default0ActualizarCuitCuil() {
		return getPersonaCuitCuil();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoDireccion")
	public Afiliado actualizarDireccion(@ParameterLayout(named = "Direccion") final String afiliadoDireccion) {
		setPersonaDireccion(afiliadoDireccion);
		return this;
	}

	public String default0ActualizarDireccion() {
		return getPersonaDireccion();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoLocalidad")
	public Afiliado actualizarLocalidad(@ParameterLayout(named = "Localidades") final Localidad afiliadoLocalidad) {
		setPersonaLocalidad(afiliadoLocalidad);
		return this;
	}

	public List<Localidad> choices0ActualizarLocalidad() {
		return localidadRepository.listarHabilitados();
	}

	public Localidad default0ActualizarLocalidad() {
		return getPersonaLocalidad();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoTelefono")
	public Afiliado actualizarTelefono(@ParameterLayout(named = "Telefono") final String afiliadoTelefono) {
		setPersonaTelefono(afiliadoTelefono);
		return this;
	}

	public String default0ActualizarTelefono() {
		return getPersonaTelefono();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoMail")
	public Afiliado actualizarMail(@ParameterLayout(named = "Mail") final String afiliadoMail) {
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

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoHabilitado")
	public Afiliado actualizarHabilitado(@ParameterLayout(named = "Habilitado") final boolean afiliadoHabilitado) {
		setPersonaHabilitado(afiliadoHabilitado);
		return this;
	}

	public boolean default0ActualizarHabilitado() {
		return getPersonaHabilitado();
	}

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarAfiliado() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setPersonaHabilitado(false);
	}
	// endregion

	@Override
	public String toString() {
		String afiliado = "Afiliado " + getAfiliadoEstado().getNombre() + 
				" - LP "+getAfiliadoLP()+ " - "+
				getPersonaJuridicaNombre() + " " + getPersonaJuridicaApellido();
		return afiliado;
	}

	@Override
	public int compareTo(Afiliado o) {
		return this.getPersonaJuridicaApellido().compareTo(o.getPersonaJuridicaApellido());  
	}

	// endregion

	// accion
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todos los afiliados")
	@MemberOrder(sequence = "2")
	public List<Afiliado> listarAfiliado() {
		return afiliadoRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar afiliados habilitados")
	@MemberOrder(sequence = "2")
	public List<Afiliado> listarAfiliadoHabilitados() {
		return afiliadoRepository.listarHabilitados();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar afiliados inhabilitados")
	@MemberOrder(sequence = "2")
	public List<Afiliado> listarAfiliadoInhabilitados() {
		return afiliadoRepository.listarInhabilitados();
	}
	
	@ActionLayout(hidden=Where.EVERYWHERE)
    public static boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
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
	AfiliadoRepository afiliadoRepository;
	
	// endregion

}
