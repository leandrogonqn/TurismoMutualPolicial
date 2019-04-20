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

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.simple.dom.cliente.Cliente;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Prestador.class, objectType="simple.PrestadorMenu")
@DomainServiceLayout(named = "Prestador", menuOrder = "20")
public class PrestadorMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar todos los Prestadores")
	@MemberOrder(sequence = "2")
	public List<Prestador> listar() {
		return prestadorRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar Por Codigo")
	@MemberOrder(sequence = "7")
	public List<Prestador> buscarPorCodigo(@ParameterLayout(named = "Codigo") final int prestadorCodigo) {
		return prestadorRepository.buscarPorCodigo(prestadorCodigo);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar prestador por Localidad")
	@MemberOrder(sequence = "6")
	public List<Prestador> buscarPrestadorPorLocalidad(@ParameterLayout(named = "Localidad") final Localidad prestadorLocalidad) {
		return prestadorRepository.buscarPrestadorPorLocalidad(prestadorLocalidad);
	}
	
	public List<Localidad> choices0BuscarPrestadorPorLocalidad() {
		return localidadRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Buscar Prestador Por Nombre")
	@MemberOrder(sequence = "5")
	public List<Prestador> buscarPorNombre(@ParameterLayout(named = "Nombre") final String prestadorNombre) {
		return prestadorRepository.buscarPorNombre(prestadorNombre);

	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Prestador")
	@MemberOrder(sequence = "1.2")
	public Prestador crear(@ParameterLayout(named="Codigo") final int prestadorCodigo,
			@ParameterLayout(named = "Nombre") final String prestadorNombre,
			@Nullable @ParameterLayout(named="Direccion") @Parameter(optionality=Optionality.OPTIONAL) final String prestadorDireccion,
			@Nullable @ParameterLayout(named="Localidad") @Parameter(optionality=Optionality.OPTIONAL) final Localidad prestadorLocalidad,
			@Nullable @ParameterLayout(named="Telefono") @Parameter(optionality=Optionality.OPTIONAL) final String prestadorTelefono,
			@Nullable @ParameterLayout(named="Nombre del Encargado") @Parameter(optionality=Optionality.OPTIONAL) final String prestadorEncargado) {
		return prestadorRepository.crear(prestadorCodigo, prestadorNombre, prestadorDireccion, prestadorLocalidad, prestadorTelefono, prestadorEncargado);
	}
	
	public List<Localidad> choices3Crear() {
		return localidadRepository.listarActivos();
	}

	@javax.inject.Inject
	PrestadorRepository prestadorRepository;
	
	@Inject
	LocalidadRepository localidadRepository;

}
