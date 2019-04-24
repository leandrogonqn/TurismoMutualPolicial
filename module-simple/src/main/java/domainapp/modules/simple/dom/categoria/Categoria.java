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
import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Categoria")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "categoriaId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.categoria.Categoria "
				+ "WHERE categoriaNombre.toLowerCase().indexOf(:categoriaNombre) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.categoria.Categoria " + "WHERE categoriaCodigo == :categoriaCodigo"),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.categoria.Categoria " + "WHERE categoriaActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.categoria.Categoria " + "WHERE categoriaActivo == false ") })
@javax.jdo.annotations.Unique(name = "Categoria_categoriaNombre_UNQ", members = { "categoriaNombre" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Categoria implements Comparable<Categoria> {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name", getCategoriaNombre());
	}
	// endregion

	public String cssClass() {
		return (getCategoriaActivo() == true) ? "activo" : "inactivo";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Categoria(int categoriaCodigo, String categoriaNombre) {
		setCategoriaCodigo(categoriaCodigo);
		setCategoriaNombre(categoriaNombre);
		this.categoriaActivo = true;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Codigo")
	private int categoriaCodigo;

	public int getCategoriaCodigo() {
		return categoriaCodigo;
	}

	public void setCategoriaCodigo(int categoriaCodigo) {
		this.categoriaCodigo = categoriaCodigo;
	}

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String categoriaNombre;

	public String getCategoriaNombre() {
		return categoriaNombre;
	}

	public void setCategoriaNombre(String categoriaNombre) {
		this.categoriaNombre = categoriaNombre;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Activo", hidden=Where.ALL_TABLES)
	private boolean categoriaActivo;

	public boolean getCategoriaActivo() {
		return categoriaActivo;
	}

	public void setCategoriaActivo(boolean categoriaActivo) {
		this.categoriaActivo = categoriaActivo;
	}

	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarCategoria() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setCategoriaActivo(false);
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "categoriaCodigo")
	public Categoria actualizarCodigo(@ParameterLayout(named = "Codigo") final int categoriaCodigo) {
		setCategoriaCodigo(categoriaCodigo);
		return this;
	}

	public int default0ActualizarCodigo() {
		return getCategoriaCodigo();
	}


	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "categoriaNombre")
	public Categoria actualizarNombre(@ParameterLayout(named = "Nombre") final String categoriaNombre) {
		setCategoriaNombre(categoriaNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getCategoriaNombre();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "categoriaActivo")
	public Categoria actualizarActivo(@ParameterLayout(named = "Activo") final boolean categoriaActivo) {
		setCategoriaActivo(categoriaActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getCategoriaActivo();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		return getCategoriaNombre();
	}

	@Override
	public int compareTo(final Categoria categoria) {
		return this.categoriaNombre.compareTo(categoria.categoriaNombre);
	}

	// endregion

	// acciones
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todas las Categorias")
	@MemberOrder(sequence = "2")
	public List<Categoria> listar() {
		return categoriaRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Categorias Activas")
	@MemberOrder(sequence = "3")
	public List<Categoria> listarActivos() {
		return categoriaRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Categorias Inactivas")
	@MemberOrder(sequence = "4")
	public List<Categoria> listarInactivos() {
		return categoriaRepository.listarInactivos();
	}

	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	CategoriaRepository categoriaRepository;

	// endregion
}
