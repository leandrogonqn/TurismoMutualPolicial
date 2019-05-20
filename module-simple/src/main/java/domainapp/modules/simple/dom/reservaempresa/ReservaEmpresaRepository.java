package domainapp.modules.simple.dom.reservaempresa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import domainapp.modules.simple.dom.empresa.Empresa;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.voucher.EstadoVoucher;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = ReservaEmpresa.class)
public class ReservaEmpresaRepository {

	public List<ReservaEmpresa> listar() {
		return repositoryService.allInstances(ReservaEmpresa.class);
	}

	public List<ReservaEmpresa> listarActivos(final boolean reservaActivo) {
		return repositoryService.allMatches(new QueryDefault<>(ReservaEmpresa.class, "listarActivos", "reservaActivo", reservaActivo));
	}

	public ReservaEmpresa crear(final int reservaCodigo, final Date reservaFecha, final Empresa reservaCliente,  final Producto voucherProducto,
			final Date voucherFechaEntrada, final Date voucherFechaSalida, final int voucherCantidadPasajeros, TipoPrecio precioHistoricoTipoPrecio,
			final String voucherObservaciones, final String reservaMemo) {
		List<Voucher> reservaListaVoucher = new ArrayList<>();
		Voucher v = voucherRepository.crear(voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadPasajeros, precioHistoricoTipoPrecio, voucherObservaciones);
		reservaListaVoucher.add(v);
		final ReservaEmpresa object = new ReservaEmpresa(reservaCodigo, reservaFecha, reservaCliente, reservaListaVoucher, reservaMemo);
		v.setVoucherReserva(object);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	VoucherRepository voucherRepository;
}
