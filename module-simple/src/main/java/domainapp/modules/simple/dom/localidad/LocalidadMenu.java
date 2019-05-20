package domainapp.modules.simple.dom.localidad;

import java.util.List;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.simple.dom.provincia.Provincia;
import domainapp.modules.simple.dom.provincia.ProvinciaRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Localidad.class, objectType="simple.LocalidadMenu")
@DomainServiceLayout(named = "Datos", menuOrder = "60")
public class LocalidadMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todas las Localidades")
	@MemberOrder(sequence = "2")
	public List<Localidad> listar() {
		return localidadesRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar Localidad Por Nombre")
	@MemberOrder(sequence = "5")
	public List<Localidad> buscarPorNombre(@ParameterLayout(named = "Nombre") final String localidadNombre) {
		return localidadesRepository.buscarPorNombre(localidadNombre);

	}

	public List<Provincia> choices1Crear() {
		return provinciasRepository.listarHabilitados();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@MemberOrder(sequence = "1.2")
	@ActionLayout(named = "Crear Localidad")
	public Localidad crear(@ParameterLayout(named = "Nombre") final String localidadNombre,
			@ParameterLayout(named = "Provincia") final Provincia localidadProvincia) {
		return localidadesRepository.crear(localidadNombre, localidadProvincia);
	}

	@javax.inject.Inject
	LocalidadRepository localidadesRepository;

	@javax.inject.Inject
	ProvinciaRepository provinciasRepository;

}
