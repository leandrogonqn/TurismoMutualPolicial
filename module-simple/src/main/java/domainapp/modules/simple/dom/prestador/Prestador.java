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

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
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

import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Prestador")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "prestadorId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.prestador.Prestador "
				+ "WHERE prestadorNombre.toLowerCase().indexOf(:prestadorNombre) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPrestadorPorLocalidad", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.prestador.Prestador " + "WHERE prestadorLocalidad == :prestadorLocalidad"),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.prestador.Prestador " + "WHERE prestadorActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.prestador.Prestador " + "WHERE prestadorActivo == false ") })
@javax.jdo.annotations.Unique(name = "Prestador_prestadorCodigo_UNQ", members = { "prestadorCodigo" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Prestador implements Comparable<Prestador> {
	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("{name}", "name", "Codigo: " + getPrestadorCodigo() + " - " + getPrestadorNombre());
	}
	// endregion

	public String cssClass() {
		return (getPrestadorActivo() == true) ? "activo" : "inactivo";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Prestador(int prestadorCodigo, String prestadorNombre, String prestadorDireccion, Localidad prestadorLocalidad, 
			String prestadorTelefono, String prestadorEncargado) {
		setPrestadorCodigo(prestadorCodigo);
		setPrestadorNombre(prestadorNombre);
		setPrestadorDireccion(prestadorDireccion);
		setPrestadorLocalidad(prestadorLocalidad);
		setPrestadorTelefono(prestadorTelefono);
		setPrestadorEncargado(prestadorEncargado);
		this.prestadorActivo = true;
	}
	
	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named = "Codigo")
	private int prestadorCodigo;
	
	public int getPrestadorCodigo() {
		return prestadorCodigo;
	}
	
	public void setPrestadorCodigo(int prestadorCodigo) {
		this.prestadorCodigo = prestadorCodigo;
	}

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String prestadorNombre;

	public String getPrestadorNombre() {
		return prestadorNombre;
	}

	public void setPrestadorNombre(String prestadorNombre) {
		this.prestadorNombre = prestadorNombre;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Direccion")
    private String prestadorDireccion;

    public String getPrestadorDireccion() {
		return prestadorDireccion;
	}
	public void setPrestadorDireccion(String prestadorDireccion) {
		this.prestadorDireccion = prestadorDireccion;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "true", name="localidadId")
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Localidad")
    private Localidad prestadorLocalidad;

	public Localidad getPrestadorLocalidad() {
		return prestadorLocalidad;
	}

	public void setPrestadorLocalidad(Localidad prestadorLocalidad) {
		this.prestadorLocalidad = prestadorLocalidad;
	}

    @javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Telefono")
    private String prestadorTelefono;

    public String getPrestadorTelefono() {
		return prestadorTelefono;
	}
	public void setPrestadorTelefono(String prestadorTelefono) {
		this.prestadorTelefono = prestadorTelefono;
	}	
	
    @javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Nombre del Encargado")
    private String prestadorEncargado;

    public String getPrestadorEncargado() {
		return prestadorEncargado;
	}
	public void setPrestadorEncargado(String prestadorEncargado) {
		this.prestadorEncargado = prestadorEncargado;
	}	

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Activo", hidden=Where.ALL_TABLES)
	private boolean prestadorActivo;

	public boolean getPrestadorActivo() {
		return prestadorActivo;
	}

	public void setPrestadorActivo(boolean prestadorActivo) {
		this.prestadorActivo = prestadorActivo;
	}

	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarPrestador() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setPrestadorActivo(false);
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "prestadorCodigo")
	public Prestador actualizarCodigo(@ParameterLayout(named = "Codigo") final int prestadorCodigo) {
		setPrestadorCodigo(prestadorCodigo);
		return this;
	}

	public int default0ActualizarCodigo() {
		return getPrestadorCodigo();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "prestadorNombre")
	public Prestador actualizarNombre(@ParameterLayout(named = "Nombre") final String prestadorNombre) {
		setPrestadorNombre(prestadorNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getPrestadorNombre();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "prestadorDireccion")
	public Prestador actualizarDireccion(@Nullable @Parameter(optionality=Optionality.OPTIONAL) @ParameterLayout(named = "Direccion") final String prestadorDireccion) {
		setPrestadorDireccion(prestadorDireccion);
		return this;
	}

	public String default0ActualizarDireccion() {
		return getPrestadorDireccion();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "prestadorLocalidad")
	public Prestador actualizarLocalidad(@Nullable @ParameterLayout(named = "Localidad") @Parameter(optionality=Optionality.OPTIONAL) final Localidad prestadorLocalidad) {
		setPrestadorLocalidad(prestadorLocalidad);
		return this;
	}

	public Localidad default0ActualizarLocalidad() {
		return getPrestadorLocalidad();
	}
	
	public List<Localidad> choices0ActualizarLocalidad() {
		return localidadRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "prestadorTelefono")
	public Prestador actualizarTelefono(@Nullable @ParameterLayout(named = "Telefono") @Parameter(optionality=Optionality.OPTIONAL) final String prestadorTelefono) {
		setPrestadorTelefono(prestadorTelefono);
		return this;
	}

	public String default0ActualizarTelefono() {
		return getPrestadorTelefono();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "prestadorEncargado")
	public Prestador actualizarEncargado(@Nullable @ParameterLayout(named = "Nombre del Encargado") final String prestadorEncargado) {
		setPrestadorEncargado(prestadorEncargado);
		return this;
	}

	public String default0ActualizarEncargado() {
		return getPrestadorEncargado();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "prestadorActivo")
	public Prestador actualizarActivo(@ParameterLayout(named = "Activo") final boolean prestadorActivo) {
		setPrestadorActivo(prestadorActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getPrestadorActivo();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		return getPrestadorNombre();
	}

	@Override
	public int compareTo(final Prestador prestador) {
		return this.prestadorNombre.compareTo(prestador.prestadorNombre);
	}

	// endregion

	// acciones
	@ActionLayout(named = "Listar todas los Prestadores")
	@MemberOrder(sequence = "2")
	public List<Prestador> listar() {
		return prestadorRepository.listar();
	}

	@ActionLayout(named = "Listar Prestadores Activas")
	@MemberOrder(sequence = "3")
	public List<Prestador> listarActivos() {
		return prestadorRepository.listarActivos();
	}

	@ActionLayout(named = "Listar Prestadores Inactivas")
	@MemberOrder(sequence = "4")
	public List<Prestador> listarInactivos() {
		return prestadorRepository.listarInactivos();
	}

	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	PrestadorRepository prestadorRepository;
	
	@Inject
	LocalidadRepository localidadRepository;

	// endregion
}
