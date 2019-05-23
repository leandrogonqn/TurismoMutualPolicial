package domainapp.modules.simple.dom.politicas;

import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Politicas")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "politicasId")
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Politicas {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name", getPoliticasTitulo());
	}
	// endregion


	public static final int NAME_LENGTH = 200;

	// Constructor
	public Politicas(String politicasTitulo, String politicasTexto) {
		setPoliticasTitulo(politicasTitulo);
		setPoliticasTexto(politicasTexto);
	}

	public Politicas() {
		// TODO Auto-generated constructor stub
	}


	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Titulo")
	private String politicasTitulo;

	public String getPoliticasTitulo() {
		return politicasTitulo;
	}

	public void setPoliticasTitulo(String politicasTitulo) {
		this.politicasTitulo = politicasTitulo;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", length = 10000)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Texto")
	private String politicasTexto;

	public String getPoliticasTexto() {
		return politicasTexto;
	}

	public void setPoliticasTexto(String politicasTexto) {
		this.politicasTexto = politicasTexto;
	}

	// endregion

	// region > delete (action)

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "politicasTitulo")
	public Politicas actualizarTitulo(@ParameterLayout(named = "Titulo") final String politicasTitulo) {
		setPoliticasTitulo(politicasTitulo);
		return this;
	}

	public String default0ActualizarTitulo() {
		return getPoliticasTitulo();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "politicasTexto")
	public Politicas actualizarTexto(@ParameterLayout(named = "Texto", multiLine=20) final String politicasTexto) {
		setPoliticasTexto(politicasTexto);
		return this;
	}

	public String default0ActualizarTexto() {
		return getPoliticasTexto();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		return getPoliticasTitulo();
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
	PoliticasRepository politicasRepository;

	// endregion
}
