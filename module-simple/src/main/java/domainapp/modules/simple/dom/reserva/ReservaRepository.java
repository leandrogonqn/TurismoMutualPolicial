package domainapp.modules.simple.dom.reserva;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.producto.ProductoRepository;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Reserva.class)
public class ReservaRepository {


	public List<Reserva> listar() {
		return repositoryService.allInstances(Reserva.class);
	}

	public List<Reserva> listarHabilitados(final boolean reservaHabilitado) {
		return repositoryService.allMatches(new QueryDefault<>(Reserva.class, "listarHabilitados", "reservaHabilitado", reservaHabilitado));
	}
	
	public List<Reserva> buscarPorCodigo(final int reservaCodigo) {
		return repositoryService
				.allMatches(new QueryDefault<>(Reserva.class, "buscarPorCodigo", "reservaCodigo", reservaCodigo));
	}
	
	public List<FechasDisponibles> listarDisponibilidad(final Date fechaDesde, final Date fechaHasta) {
		List<Producto> listaProducto = productoRepository.listarHabilitados();
		List<FechasDisponibles> listaFechasDisponibles = new ArrayList<>();
		for (int i = 0; listaProducto.size() > i; i++) {
			if (listaProducto.get(i).getProductoAlojamientoPropio() == true) {
				Date f = fechaDesde;
				while (f.before(fechaHasta)) {
					if (voucherRepository.corroborarDisponibilidadCrear(listaProducto.get(i), f, f) == true) {
						FechasDisponibles fechaDisponible = new FechasDisponibles();
						fechaDisponible.setProducto(listaProducto.get(i));
						fechaDisponible.setFechaDesde(f);
						while (voucherRepository.corroborarDisponibilidadCrear(listaProducto.get(i), f, f) == true) {
							f = voucherRepository.sumarUnDiaAFecha(f);
							if (f.after(fechaHasta)) {
								fechaDisponible.setMemo("");
								break;
							}
							fechaDisponible.setFechaHasta(f);
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
							fechaDisponible.setMemo("Disponibilidad hasta las 10:00 a.m.  del día "+sdf.format(f));
						}
						listaFechasDisponibles.add(fechaDisponible);
					}
					f = voucherRepository.sumarUnDiaAFecha(f);
				}
			} else {
				FechasDisponibles fechaDisponible = new FechasDisponibles();
				fechaDisponible.setProducto(listaProducto.get(i));
				fechaDisponible.setMemo("Consultar Disponibilidad al proveedor");
				listaFechasDisponibles.add(fechaDisponible);
			}
		}
		return listaFechasDisponibles;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	VoucherRepository voucherRepository;
	@Inject
	ProductoRepository productoRepository;
}
