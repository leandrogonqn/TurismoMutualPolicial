package domainapp.modules.simple.dom.politicas;

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

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Politicas.class, objectType="simple.PoliticasMenu")
@DomainServiceLayout(named = "Datos", menuOrder = "60.3")
public class PoliticasMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todas las Politicas")
	@MemberOrder(sequence = "2")
	public List<Politicas> listar() {
		return politicasRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Politicas")
	@MemberOrder(sequence = "1.2")
	public Politicas crear(@ParameterLayout(named = "Titulo") final String politicasTitulo,
			@ParameterLayout(named = "Texto", multiLine=20) final String politicasTexto) {
		return politicasRepository.crear(politicasTitulo, politicasTexto);
	}

	@javax.inject.Inject
	PoliticasRepository politicasRepository;

}
