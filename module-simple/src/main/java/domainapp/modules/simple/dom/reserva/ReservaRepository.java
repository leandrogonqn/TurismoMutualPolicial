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
package domainapp.modules.simple.dom.reserva;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Reserva.class)
public class ReservaRepository {
//
//	public List<Reserva> listar() {
//		return repositoryService.allInstances(Reserva.class);
//	}
//
//	public List<Reserva> buscarPorNombre(final String provinciasNombre) {
//
//		return repositoryService.allMatches(new QueryDefault<>(Reserva.class, "buscarPorNombre", "provinciasNombre",
//				provinciasNombre.toLowerCase()));
//
//	}
//
//	public List<Reserva> listarActivos() {
//		return repositoryService.allMatches(new QueryDefault<>(Reserva.class, "listarActivos"));
//	}
//
//	public List<Reserva> listarInactivos() {
//		return repositoryService.allMatches(new QueryDefault<>(Reserva.class, "listarInactivos"));
//	}
//
//	public Reserva crear(final String provinciaNombre) {
//		final Reserva object = new Reserva(provinciaNombre);
//		serviceRegistry.injectServicesInto(object);
//		repositoryService.persist(object);
//		return object;
//	}
//
//	@javax.inject.Inject
//	RepositoryService repositoryService;
//	@javax.inject.Inject
//	ServiceRegistry2 serviceRegistry;
}
