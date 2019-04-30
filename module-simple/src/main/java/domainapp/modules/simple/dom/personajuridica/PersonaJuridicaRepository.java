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
package domainapp.modules.simple.dom.personajuridica;

import java.util.List;
import javax.inject.Inject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = PersonaJuridica.class)
public class PersonaJuridicaRepository {

	public List<PersonaJuridica> listar() {
		return repositoryService.allInstances(PersonaJuridica.class);
	}

	public List<PersonaJuridica> listarActivos() {
		return repositoryService.allMatches(new QueryDefault<>(PersonaJuridica.class, "listarActivos"));
	}

	public List<PersonaJuridica> listarInactivos() {
		return repositoryService.allMatches(new QueryDefault<>(PersonaJuridica.class, "listarInactivos"));
	}

	@Inject
	RepositoryService repositoryService;

}
