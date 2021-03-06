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
package domainapp.modules.simple.dom.localidad;

import java.util.List;
import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
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

import domainapp.modules.simple.dom.proveedor.Proveedor;
import domainapp.modules.simple.dom.proveedor.ProveedorRepository;
import domainapp.modules.simple.dom.provincia.Provincia;
import domainapp.modules.simple.dom.provincia.ProvinciaRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Localidades")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "localidadId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.localidad.Localidad "
				+ "WHERE localidadesNombre.toLowerCase().indexOf(:localidadesNombre) >= 0 "),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.localidad.Localidad " + "WHERE localidadActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.localidad.Localidad " + "WHERE localidadActivo == false ") })
@javax.jdo.annotations.Unique(name = "Localidades_localidadesNombre_UNQ", members = { "localidadesNombre" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Localidad implements Comparable<Localidad> {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name",
				getLocalidadesNombre() + " - " + this.getLocalidadProvincia().getProvinciasNombre());
	}
	// endregion

	public String cssClass() {
		return (getLocalidadActivo() == true) ? "activo" : "inactivo";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Localidad(String localidadNombre, Provincia localidadProvincia) {
		setLocalidadesNombre(localidadNombre);
		setLocalidadProvincia(localidadProvincia);
		this.localidadActivo = true;
	}

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String localidadesNombre;

	public String getLocalidadesNombre() {
		return localidadesNombre;
	}

	public void setLocalidadesNombre(String localidadesNombre) {
		this.localidadesNombre = localidadesNombre;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", name = "provinciaId")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Provincia")
	private Provincia localidadProvincia;

	public Provincia getLocalidadProvincia() {
		return localidadProvincia;
	}

	public void setLocalidadProvincia(Provincia localidadProvincia) {
		this.localidadProvincia = localidadProvincia;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Activo", hidden=Where.ALL_TABLES)
	private boolean localidadActivo;

	public boolean getLocalidadActivo() {
		return localidadActivo;
	}

	public void setLocalidadActivo(boolean localidadActivo) {
		this.localidadActivo = localidadActivo;
	}

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarLocalidad() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setLocalidadActivo(false);
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "localidadNombre")
	public Localidad actualizarNombre(@ParameterLayout(named = "Nombre") final String localidadNombre) {
		setLocalidadesNombre(localidadNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getLocalidadesNombre();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "LocalidadProvincia")
	public Localidad actualizarProvincia(@ParameterLayout(named = "Provincia") final Provincia LocalidadProvincia) {
		setLocalidadProvincia(LocalidadProvincia);
		return this;
	}

	public List<Provincia> choices0ActualizarProvincia() {
		return provinciaRepository.listarActivos();
	}

	public Provincia default0ActualizarProvincia() {
		return getLocalidadProvincia();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "localidadActivo")
	public Localidad actualizarActivo(@ParameterLayout(named = "Activo") final boolean localidadActivo) {
		setLocalidadActivo(localidadActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getLocalidadActivo();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		return getLocalidadesNombre();
	}

	@Override
	public int compareTo(final Localidad localidad) {
		return this.localidadesNombre.compareTo(localidad.localidadesNombre);
	}
	// endregion

	// acciones
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todas las Localidades")
	@MemberOrder(sequence = "2")
	public List<Localidad> listar() {
		return localidadesRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar las Localidades Activas")
	@MemberOrder(sequence = "3")
	public List<Localidad> listarActivos() {
		return localidadesRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar las Localidades Inactivas")
	@MemberOrder(sequence = "4")
	public List<Localidad> listarInactivos() {
		return localidadesRepository.listarInactivos();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar prestadores en esta Localidad")
	public List<Proveedor> buscarPrestadorPorLocalidad() {
		return proveedorRepository.buscarProveedorPorLocalidad(this);
	}
	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	ProvinciaRepository provinciaRepository;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	LocalidadRepository localidadesRepository;
	
	@Inject
	ProveedorRepository proveedorRepository;

	// endregion
}
