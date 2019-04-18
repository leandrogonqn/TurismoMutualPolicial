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
package domainapp.modules.simple.dom.cliente;

import java.util.Date;
import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.dom.localidad.Localidad;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Cliente.class)
public class ClienteRepository {

	public List<Cliente> listar() {
		return repositoryService.allInstances(Cliente.class);
	}

	public List<Cliente> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(Cliente.class, "listarActivos"));
	}

	public List<Cliente> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(Cliente.class, "listarInactivos"));
	}

	public List<Cliente> buscarPorNombre(final String clienteNombre) {
		return repositoryService.allMatches(
				new QueryDefault<>(Cliente.class, "buscarPorNombre", "clienteNombre", clienteNombre.toLowerCase()));
	}

	public List<Cliente> buscarPorDNI(final int clienteDni) {
		return repositoryService
				.allMatches(new QueryDefault<>(Cliente.class, "buscarPorDNI", "clienteDni", clienteDni));
	}

	public Cliente crear(final String clienteLP, final String clienteNombre, final String clienteApellido, Sexo clienteSexo,
			final int clienteDni, final String clienteCuitCuil, final String clienteDireccion, Localidad clienteLocalidad, 
			 final String clienteTelefono, final String clienteMail, final Date clienteFechaNacimiento,
			 final Estado clienteEstado, final String clienteCBU) {
		final Cliente object = new Cliente(clienteLP, clienteNombre, clienteApellido, clienteSexo,
				clienteDni, clienteCuitCuil, clienteDireccion, clienteLocalidad, clienteTelefono, clienteMail, clienteFechaNacimiento,
				clienteEstado, clienteCBU);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
}