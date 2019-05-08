/*******************************************************************************
 * Copyright 2017 SiGeSe
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package domainapp.modules.simple.dom.voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import domainapp.modules.simple.dom.preciohistorico.PrecioHistorico;
import domainapp.modules.simple.dom.preciohistorico.PrecioHistoricoRepository;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Voucher.class)
public class VoucherRepository {

	public List<Voucher> listar() {
		return repositoryService.allInstances(Voucher.class);
	}

	public List<Voucher> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(Voucher.class, "listarActivos"));
	}

	public List<Voucher> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(Voucher.class, "listarInactivos"));
	}

	public Voucher crear(final Producto voucherProducto, final Date voucherFechaEntrada, final Date voucherFechaSalida, 
			final int voucherCantidadPasajeros, final TipoPrecio precioHistoricoTipoPrecio, final String voucherObservaciones, final String voucherMemo) {
		int voucherCantidadNoches = calcularCantidadDeNoches(voucherFechaEntrada, voucherFechaSalida);
		Double voucherPrecioTotal = calcularPrecioTotal(voucherFechaEntrada, voucherFechaSalida, voucherProducto, precioHistoricoTipoPrecio);
		final Voucher object = new Voucher(voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadNoches, 
				voucherCantidadPasajeros, voucherPrecioTotal, voucherObservaciones, voucherMemo);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	public int calcularCantidadDeNoches(Date voucherFechaEntrada, Date voucherFechaSalida) {
		int noches=(int) ((voucherFechaSalida.getTime()-voucherFechaEntrada.getTime())/86400000);
		return noches;
	}
	
	public Double calcularPrecioTotal(Date voucherFechaEntrada, Date voucherFechaSalida, Producto voucherProducto, TipoPrecio precioHistoricoTipoPrecio) {
		Double precioTotal = 0.0;
		Date fecha = voucherFechaEntrada;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		while (fecha.getTime() < voucherFechaSalida.getTime() ) {
			if (precioHistoricoRepository.mostrarPrecioPorFecha(voucherProducto, fecha, precioHistoricoTipoPrecio)==0) {
				precioTotal = precioTotal + traerUltimoPrecioCargado(fecha, precioHistoricoTipoPrecio, voucherProducto);
				messageService.warnUser("PRECAUCION: Precio del dia "+ sdf.format(fecha)+ " no esta cargado, se tomo el ultimo precio cargado");
			}else {
				precioTotal = precioTotal + precioHistoricoRepository.mostrarPrecioPorFecha(voucherProducto, fecha, precioHistoricoTipoPrecio); 
			}
			
			fecha = sumarUnDiaAFecha(fecha);
		}
		return precioTotal;
	}
	
	public static Date sumarUnDiaAFecha(Date fecha){
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, 1);  
	      return calendar.getTime(); 
	}
	
	public Double traerUltimoPrecioCargado(Date fecha, TipoPrecio precioHistoricoTipoPrecio, Producto precioHistoricoProducto){
		Calendar myCal = Calendar.getInstance();
		myCal.set(Calendar.YEAR, 2000);
		myCal.set(Calendar.MONTH, 01);
		myCal.set(Calendar.DAY_OF_MONTH, 01);
		Date fechaAux = myCal.getTime();
		Double precioTotal = 0.0;
		List<PrecioHistorico> lista = precioHistoricoRepository.listarPreciosPorProductoPorTipoDeAfiliadoActivo(precioHistoricoProducto, precioHistoricoTipoPrecio, true);
		Iterator<PrecioHistorico> iterar = lista.iterator();
		while(iterar.hasNext()) {
			PrecioHistorico list = iterar.next();
			if(list.getPrecioHistoricoFechaHasta().before(fecha)&&list.getPrecioHistoricoFechaHasta().after(fechaAux)){
				fechaAux = list.getPrecioHistoricoFechaHasta();
				precioTotal =list.getPrecioHistoricoPrecio(); 
				}
			}
		return precioTotal; 
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	PrecioHistoricoRepository precioHistoricoRepository;
	@Inject
	MessageService messageService;
}
