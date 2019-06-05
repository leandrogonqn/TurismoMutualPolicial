package domainapp.modules.simple.dom.provincia;

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

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Provincia.class, objectType="simple.ProvinciaMenu")
@DomainServiceLayout(named = "Datos", menuOrder = "60.1")
public class ProvinciaMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todas las Provincias")
	@MemberOrder(sequence = "2")
	public List<Provincia> listar() {
		return provinciasRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar Provincia Por Nombre")
	@MemberOrder(sequence = "5")
	public List<Provincia> buscarPorNombre(@ParameterLayout(named = "Nombre") final String provinciaNombre) {
		return provinciasRepository.buscarPorNombre(provinciaNombre);

	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Provincia")
	@MemberOrder(sequence = "1.2")
	public Provincia crear(@ParameterLayout(named = "Nombre") final String provinciaNombre) {
		return provinciasRepository.crear(provinciaNombre);
	}

	@javax.inject.Inject
	ProvinciaRepository provinciasRepository;

}
