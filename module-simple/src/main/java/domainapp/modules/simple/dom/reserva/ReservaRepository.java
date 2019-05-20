package domainapp.modules.simple.dom.reserva;

import java.util.List;
import javax.inject.Inject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Reserva.class)
public class ReservaRepository {


	public List<Reserva> listar() {
		return repositoryService.allInstances(Reserva.class);
	}

	public List<Reserva> listarHabilitados(final boolean reservaHabilitado) {
		return repositoryService.allMatches(new QueryDefault<>(Reserva.class, "listarHabilitados", "reservaHabilitado", reservaHabilitado));
	}
	
	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	VoucherRepository voucherRepository;
}
