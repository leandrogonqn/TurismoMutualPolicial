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
/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.modules.simple.dom.clientenoafiliado;

import java.util.List;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.localidad.Localidad;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = ClienteNoAfiliado.class)
public class ClienteNoAfiliadoRepository {

	public ClienteNoAfiliado crear(final int personaDni, final String personaApellido, final String personaNombre, 
			final String personaCuitCuil, final String personaDireccion, Localidad personaLocalidad, 
			 final Long personaTelefonoFijo, final Long personaTelefonoCelular, final String personaMail) {
		final ClienteNoAfiliado object = new ClienteNoAfiliado(personaDni, personaApellido, personaNombre, 
				personaCuitCuil, personaDireccion, personaLocalidad, personaTelefonoFijo, personaTelefonoCelular, personaMail);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	public List<ClienteNoAfiliado> listar() {
		return repositoryService.allInstances(ClienteNoAfiliado.class);
	}

	public List<ClienteNoAfiliado> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(ClienteNoAfiliado.class, "listarActivos"));
	}

	public List<ClienteNoAfiliado> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(ClienteNoAfiliado.class, "listarInactivos"));
	}

	public List<ClienteNoAfiliado> buscarPorNombre(final String personaNombre) {
		return repositoryService.allMatches(
				new QueryDefault<>(ClienteNoAfiliado.class, "buscarPorNombre", "personaNombre", personaNombre.toLowerCase()));
	}
	
	public List<ClienteNoAfiliado> buscarPorDNI(final int personaDni) {
		return repositoryService
				.allMatches(new QueryDefault<>(ClienteNoAfiliado.class, "buscarPorDNI", "personaDni", personaDni));
	}


	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}
