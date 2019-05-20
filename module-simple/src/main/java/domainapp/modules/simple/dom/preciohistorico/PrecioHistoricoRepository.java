package domainapp.modules.simple.dom.preciohistorico;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import domainapp.modules.simple.dom.producto.Producto;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = PrecioHistorico.class)
public class PrecioHistoricoRepository {

	public List<PrecioHistorico> listar() {
		return repositoryService.allInstances(PrecioHistorico.class);
	}

	public List<PrecioHistorico> listarHabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(PrecioHistorico.class, "listarHabilitados"));
	}

	public List<PrecioHistorico> listarInhabilitados() {
		return repositoryService.allMatches(new QueryDefault<>(PrecioHistorico.class, "listarInhabilitados"));
	}
	
	public List<PrecioHistorico> listarHabilitadosAfiliados(final TipoPrecio precioHistoricoTipoPrecio, final boolean precioHistoricoHabilitado) {
		return repositoryService.allMatches(new QueryDefault<>(PrecioHistorico.class, "listarHabilitadosAfiliados", "precioHistoricoTipoPrecio", 
				precioHistoricoTipoPrecio, "precioHistoricoHabilitado", precioHistoricoHabilitado));
	}
	public List<PrecioHistorico> listarPreciosPorProductoPorTipoDeAfiliado(final Producto precioHistoricoProducto, 
			final TipoPrecio precioHistoricoTipoPrecio, final boolean precioHistoricoHabilitado) {
		return repositoryService.allMatches(new QueryDefault<>(PrecioHistorico.class, "listarPreciosPorProductoPorTipoDeAfiliado", 
				"precioHistoricoProducto", precioHistoricoProducto, "precioHistoricoTipoPrecio", precioHistoricoTipoPrecio, "precioHistoricoHabilitado", precioHistoricoHabilitado));
	}

	public List<PrecioHistorico> listarPreciosPorProducto (final Producto precioHistoricoProducto, final boolean precioHistoricoHabilitado) {
		return repositoryService.allMatches(new QueryDefault<>(PrecioHistorico.class, "listarPreciosPorProducto", 
						"precioHistoricoProducto", precioHistoricoProducto, "precioHistoricoHabilitado", precioHistoricoHabilitado));
	}
	
	public Double mostrarPrecioPorFecha (final Producto precioHistoricoProducto, final Date fecha, final TipoPrecio precioHistoricoTipoPrecio) {
		Double a = 0.0;
		List<PrecioHistorico> listaPrecios = listarPreciosPorProductoPorTipoDeAfiliado(precioHistoricoProducto, precioHistoricoTipoPrecio, true);
		Iterator<PrecioHistorico> iterar = listaPrecios.iterator();
		while(iterar.hasNext()) {
			PrecioHistorico listServicio = iterar.next();
			if((listServicio.getPrecioHistoricoFechaDesde().before(fecha)||listServicio.getPrecioHistoricoFechaDesde().equals(fecha))
					&&(listServicio.getPrecioHistoricoFechaHasta().after(fecha)||listServicio.getPrecioHistoricoFechaHasta().equals(fecha))){
				a=listServicio.getPrecioHistoricoPrecio(); 
				break;
				}
			}
		return a;
	}
	
	public List<PrecioHistorico> listarPreciosPorRangoDeFecha(Producto precioHistoricoProducto, Date precioHistoricoFechaDesde, Date precioHistoricoFechaHasta){
		List<PrecioHistorico> lista = listarPreciosPorProducto(precioHistoricoProducto, true);
		Iterator<PrecioHistorico> it = lista.iterator();
		while (it.hasNext()) {
			PrecioHistorico item = it.next();
			if (precioHistoricoFechaDesde.after(item.getPrecioHistoricoFechaHasta()))
				it.remove();
			if (precioHistoricoFechaHasta.before(item.getPrecioHistoricoFechaDesde()))
				it.remove();
		}
		Collections.sort(lista);
		return lista;
	}

	public PrecioHistorico crear(Producto precioHistoricoProducto, Date precioHistoricoFechaDesde, Date precioHistoricoFechaHasta,
			TipoPrecio precioHistoricoTipoPrecio, Double precioHistoricoPrecio) {
		final PrecioHistorico object = new PrecioHistorico(precioHistoricoProducto, precioHistoricoFechaDesde, precioHistoricoFechaHasta,
				precioHistoricoTipoPrecio, precioHistoricoPrecio);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	public boolean verificarCrearPrecio(final Producto precioHistoricoProducto, final TipoPrecio precioHistoricoTipoPrecio, final Date precioHistoricoFechaDesde, final Date precioHistoricoFechaHasta) {
		List<PrecioHistorico> listaPrecioHabilitado = listarPreciosPorProductoPorTipoDeAfiliado(precioHistoricoProducto, precioHistoricoTipoPrecio, true);
		for(int indice = 0;indice<listaPrecioHabilitado.size();indice++) {
			if ((precioHistoricoFechaDesde.before(listaPrecioHabilitado.get(indice).getPrecioHistoricoFechaHasta())||precioHistoricoFechaDesde.equals(listaPrecioHabilitado.get(indice).getPrecioHistoricoFechaHasta()))
					& (precioHistoricoFechaHasta.after(listaPrecioHabilitado.get(indice).getPrecioHistoricoFechaDesde())||precioHistoricoFechaHasta.equals(listaPrecioHabilitado.get(indice).getPrecioHistoricoFechaDesde())))
				return false; 
		}
		return true;
	}
	
	public boolean verificarActualizarPrecio(PrecioHistorico precioHistorico, Producto precioHistoricoProducto,
			TipoPrecio precioHistoricoTipoPrecio, Date precioHistoricoFechaDesde, Date precioHistoricoFechaHasta) {
		List<PrecioHistorico> listaPrecioHabilitado = listarPreciosPorProductoPorTipoDeAfiliado(precioHistoricoProducto, precioHistoricoTipoPrecio, true);
		Iterator<PrecioHistorico> it = listaPrecioHabilitado.iterator();
		while (it.hasNext()) {
			PrecioHistorico item = it.next();
			if (item == precioHistorico)
				it.remove();
		}
		for(int indice = 0;indice<listaPrecioHabilitado.size();indice++) {
			if ((precioHistoricoFechaDesde.before(listaPrecioHabilitado.get(indice).getPrecioHistoricoFechaHasta())||precioHistoricoFechaDesde.equals(listaPrecioHabilitado.get(indice).getPrecioHistoricoFechaHasta()))
					& (precioHistoricoFechaHasta.after(listaPrecioHabilitado.get(indice).getPrecioHistoricoFechaDesde())||precioHistoricoFechaHasta.equals(listaPrecioHabilitado.get(indice).getPrecioHistoricoFechaDesde())))
				return false; 
		}
		return true;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	MessageService messageService;
	
}
