package domainapp.modules.simple.dom.reservaafiliado;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.afiliado.Afiliado;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = ReservaAfiliado.class)
public class ReservaAfiliadoRepository {

	public List<ReservaAfiliado> listar() {
		return repositoryService.allInstances(ReservaAfiliado.class);
	}

	public List<ReservaAfiliado> listarActivos(final boolean reservaActivo) {
		return repositoryService.allMatches(new QueryDefault<>(ReservaAfiliado.class, "listarActivos", "reservaActivo", reservaActivo));
	}

	public ReservaAfiliado crear(final int reservaCodigo, final Date reservaFecha, final Afiliado reservaCliente,  final Producto voucherProducto,
			final Date voucherFechaEntrada, final Date voucherFechaSalida, final int voucherCantidadPasajeros, TipoPrecio precioHistoricoTipoPrecio,
			final String voucherObservaciones, final CanalDePago reservaCanalDePago, final String reservaMemo) {
		List<Voucher> reservaListaVoucher = new ArrayList<>();
		Voucher v = voucherRepository.crear(voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadPasajeros, precioHistoricoTipoPrecio, voucherObservaciones);
		reservaListaVoucher.add(v);
		final ReservaAfiliado object = new ReservaAfiliado(reservaCodigo, reservaFecha, reservaCliente, reservaListaVoucher, reservaCanalDePago, reservaMemo);
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
