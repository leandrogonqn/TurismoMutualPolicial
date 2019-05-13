package domainapp.modules.simple.dom.reserva;

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

import domainapp.modules.simple.dom.persona.Persona;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Reserva.class)
public class ReservaRepository {

	public List<Reserva> listar() {
		return repositoryService.allInstances(Reserva.class);
	}

	public List<Reserva> listarActivos(final boolean reservaActivo) {
		return repositoryService.allMatches(new QueryDefault<>(Reserva.class, "listarActivos", "reservaActivo", reservaActivo));
	}

	public Reserva crear(final int reservaCodigo, final Date reservaFecha, final Persona reservaCliente,  final Producto voucherProducto,
			final Date voucherFechaEntrada, final Date voucherFechaSalida, final int voucherCantidadPasajeros, TipoPrecio precioHistoricoTipoPrecio,
			final String voucherObservaciones, final CanalDePago reservaCanalDePago, final String reservaMemo) {
		List<Voucher> reservaListaVoucher = new ArrayList<>();
		Voucher v = voucherRepository.crear(voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadPasajeros, precioHistoricoTipoPrecio, voucherObservaciones);
		reservaListaVoucher.add(v);
		final Reserva object = new Reserva(reservaCodigo, reservaFecha, reservaCliente, reservaListaVoucher, reservaCanalDePago, reservaMemo);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	public Reserva buscarReservaPorVoucher(Voucher voucher){
		Reserva r = null;
		List<Reserva> lista = listarActivos(true);
		for(int indice = 0;indice<lista.size();indice++) {
			List<Voucher> v = lista.get(indice).getReservaListaVoucher();
			for(int ind = 0;ind<v.size();ind++)
				if (v.get(ind)==voucher)
					r = lista.get(indice); 
		}
		return r;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	VoucherRepository voucherRepository;
}
