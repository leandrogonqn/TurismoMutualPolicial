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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.preciohistorico.PrecioHistoricoRepository;
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

	public Voucher crear(final Producto voucherProducto, final Date voucherFechaEntrada, final Date voucherFechaSalida, final int voucherCantidadNoches, 
			final int voucherCantidadPasajeros, final Double voucherPrecioTotal) {
		final Voucher object = new Voucher(voucherProducto, voucherFechaEntrada, voucherFechaSalida, voucherCantidadNoches, voucherCantidadPasajeros, voucherPrecioTotal);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	public int calcularCantidadDeNoches(Date voucherFechaEntrada, Date voucherFechaSalida) {
		int noches=(int) ((voucherFechaSalida.getTime()-voucherFechaEntrada.getTime())/86400000);
		return noches;
	}
	
	public Double calcularPrecioTotal(Date voucherFechaEntrada, Date voucherFechaSalida, Producto voucherProducto) {
		Double precioTotal = 0.0;
		Date fecha = voucherFechaEntrada;
		while (fecha.getTime() < voucherFechaSalida.getTime() ) {
			precioTotal = precioTotal + precioHistoricoRepository.mostrarPrecioPorFecha(voucherProducto, fecha); 
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

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	PrecioHistoricoRepository precioHistoricoRepository;
}
