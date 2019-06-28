package domainapp.modules.simple.dom.provincia;

import javax.inject.Inject;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

@DomainObject(nature = Nature.VIEW_MODEL, objectType="Provincia")
public class Provincia implements Comparable<Provincia> {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name", getProvinciasNombre());
	}
	// endregion

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Provincia() {
		// TODO Auto-generated constructor stub
	}
	
	public Provincia(int provinciasId, String provinciaNombre) {
		setProvinciasId(provinciasId);
		setProvinciasNombre(provinciaNombre);
	}

	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Id")
	private int provinciasId;

	public int getProvinciasId() {
		return provinciasId;
	}

	public void setProvinciasId(int provinciasId) {
		this.provinciasId = provinciasId;
	}
	
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String provinciasNombre;

	public String getProvinciasNombre() {
		return provinciasNombre;
	}

	public void setProvinciasNombre(String provinciasNombre) {
		this.provinciasNombre = provinciasNombre;
	}

	// endregion

	// region > delete (action)

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
