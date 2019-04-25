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
package domainapp.modules.simple.dom.afiliado;

import java.util.List;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import domainapp.modules.simple.dom.localidad.Localidad;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Afiliado.class)
public class AfiliadoRepository {

	public List<Afiliado> buscarPorLP(final String afiliadoLP) {
		return repositoryService.allMatches(
				new QueryDefault<>(Afiliado.class, "buscarPorLP", "afiliadoLP", afiliadoLP.toLowerCase()));
	}

	public Afiliado crearCompleto(Estado afiliadoEstado, String afiliadoLP, String afiliadoNombre, String afiliadoApellido,
			int afiliadoDni, String afiliadoCuitCuil, String afiliadoDireccion, Localidad afiliadoLocalidad,
			Long afiliadoTelefonoFijo, Long afiliadoTelefonoCelular, String afiliadoMail) {
		final Afiliado object = new Afiliado(afiliadoEstado, afiliadoLP, afiliadoDni, afiliadoApellido, afiliadoNombre, 
				afiliadoCuitCuil, afiliadoDireccion, afiliadoLocalidad, afiliadoTelefonoFijo, afiliadoTelefonoCelular, afiliadoMail);
		serviceRegistry.injectServicesInto(object);
		repositoryService.persist(object);
		return object;
	}
	
	public List<Afiliado> listar() {
		return repositoryService.allInstances(Afiliado.class);
	}

	public List<Afiliado> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(Afiliado.class, "listarActivos"));
	}

	public List<Afiliado> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(Afiliado.class, "listarInactivos"));
	}

	public List<Afiliado> buscarPorNombre(final String personaNombre) {
		return repositoryService.allMatches(
				new QueryDefault<>(Afiliado.class, "buscarPorNombre", "personaNombre", personaNombre.toLowerCase()));
	}
	
	public List<Afiliado> buscarPorDNI(final int personaDni) {
		return repositoryService
				.allMatches(new QueryDefault<>(Afiliado.class, "buscarPorDNI", "personaDni", personaDni));
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;

}
