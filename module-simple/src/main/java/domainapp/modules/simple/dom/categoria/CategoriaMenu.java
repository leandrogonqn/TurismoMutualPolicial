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
package domainapp.modules.simple.dom.categoria;

import java.util.List;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Categoria.class, objectType="simple.CategoriaMenu")
@DomainServiceLayout(named = "Productos", menuOrder = "30.1")
public class CategoriaMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todas las Categorias")
	@MemberOrder(sequence = "2")
	public List<Categoria> listar() {
		return categoriaRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar categoria Por codigo")
	@MemberOrder(sequence = "3")
	public List<Categoria> buscarPorCodigo(@ParameterLayout(named = "Codigo") final int categoriaCodigo) {
		return categoriaRepository.buscarPorCodigo(categoriaCodigo);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar Categoria Por Nombre")
	@MemberOrder(sequence = "4")
	public List<Categoria> buscarPorNombre(@ParameterLayout(named = "Nombre") final String categoriaNombre) {
		return categoriaRepository.buscarPorNombre(categoriaNombre);

	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Categoria")
	@MemberOrder(sequence = "1")
	public Categoria crear(@ParameterLayout(named="Codigo") final int categoriaCodigo,
			@ParameterLayout(named = "Nombre") final String categoriaNombre) {
		return categoriaRepository.crear(categoriaCodigo, categoriaNombre);
	}

	@javax.inject.Inject
	CategoriaRepository categoriaRepository;

}
