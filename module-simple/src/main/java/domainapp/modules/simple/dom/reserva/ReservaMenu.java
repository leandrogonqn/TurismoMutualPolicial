package domainapp.modules.simple.dom.reserva;

import java.util.List;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Reserva.class, objectType="simple.ReservaMenu")
@DomainServiceLayout(named = "Reserva", menuOrder = "60")
public class ReservaMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todas las Reservas")
	@MemberOrder(sequence = "2")
	public List<Reserva> listar() {
		return reservaRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Reservas Activas")
	@MemberOrder(sequence = "3")
	public List<Reserva> listarActivos(){
		return reservaRepository.listarActivos(true);
	}
	
	@javax.inject.Inject
	ReservaRepository reservaRepository;
	
}
