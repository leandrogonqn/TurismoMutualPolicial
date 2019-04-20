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
package domainapp.modules.simple.dom.prestador;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.cliente.Cliente;
import domainapp.modules.simple.dom.localidad.Localidad;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Prestador.class)
public class PrestadorRepository {

	public List<Prestador> listar() {
		return repositoryService.allInstances(Prestador.class);
	}

	public List<Prestador> buscarPorNombre(final String prestadorNombre) {

		return repositoryService.allMatches(new QueryDefault<>(Prestador.class, "buscarPorNombre", "prestadorNombre",
				prestadorNombre.toLowerCase()));

	}
	
	public List<Prestador> buscarPorCodigo(final int prestadorCodigo) {
		return repositoryService
				.allMatches(new QueryDefault<>(Prestador.class, "buscarPorCodigo", "prestadorCodigo", prestadorCodigo));
	}

	public List<Prestador> buscarPrestadorPorLocalidad(final Localidad prestadorLocalidad) {
		return repositoryService
				.allMatches(new QueryDefault<>(Prestador.class, "buscarPrestadorPorLocalidad", "prestadorLocalidad", prestadorLocalidad));
	}
	
	public List<Prestador> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(Prestador.class, "listarActivos"));
	}

	public List<Prestador> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(Prestador.class, "listarInactivos"));
	}

	public Prestador crear(final int prestadorCodigo, final String prestadorNombre, final String prestadorDireccion,
			final Localidad prestadorLocalidad, final String prestadorTelefono, final String prestadorEncargado) {
		final Prestador object = new Prestador(prestadorCodigo, prestadorNombre, prestadorDireccion, prestadorLocalidad,
				prestadorTelefono, prestadorEncargado);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
