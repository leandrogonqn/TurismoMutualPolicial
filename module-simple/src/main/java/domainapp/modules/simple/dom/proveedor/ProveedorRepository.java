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
package domainapp.modules.simple.dom.proveedor;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import domainapp.modules.simple.dom.localidad.Localidad;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Proveedor.class)
public class ProveedorRepository {

	public List<Proveedor> listar() {
		return repositoryService.allInstances(Proveedor.class);
	}

	public List<Proveedor> buscarPorNombre(final String proveedorNombre) {

		return repositoryService.allMatches(new QueryDefault<>(Proveedor.class, "buscarPorNombre", "proveedorNombre",
				proveedorNombre.toLowerCase()));

	}
	
	public List<Proveedor> buscarPorCodigo(final int proveedorCodigo) {
		return repositoryService
				.allMatches(new QueryDefault<>(Proveedor.class, "buscarPorCodigo", "proveedorCodigo", proveedorCodigo));
	}

	public List<Proveedor> buscarProveedorPorLocalidad(final Localidad proveedorLocalidad) {
		return repositoryService
				.allMatches(new QueryDefault<>(Proveedor.class, "buscarProveedorPorLocalidad", "proveedorLocalidad", proveedorLocalidad));
	}
	
	public List<Proveedor> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(Proveedor.class, "listarActivos"));
	}

	public List<Proveedor> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(Proveedor.class, "listarInactivos"));
	}

	public Proveedor crear(final int proveedorCodigo, final String proveedorNombre, final String proveedorDireccion,
			final Localidad proveedorLocalidad, final String proveedorTelefono, final String proveedorEncargado) {
		final Proveedor object = new Proveedor(proveedorCodigo, proveedorNombre, proveedorDireccion, proveedorLocalidad,
				proveedorTelefono, proveedorEncargado);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
