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
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.message.MessageService;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.persona.PersonaRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = ClienteNoAfiliado.class, objectType="simple.ClienteNoAfiliadoMenu")
@DomainServiceLayout(named = "Clientes", menuOrder = "10.1")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ClienteNoAfiliadoMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Cliente No Afiliado")
	@MemberOrder(sequence = "1")
	public ClienteNoAfiliado crear(@ParameterLayout(named = "DNI") final int personaDni,
			@ParameterLayout(named = "Apellido") final String personaApellido,
			@ParameterLayout(named = "Nombre") final String personaNombre,
			@Nullable @ParameterLayout(named = "Cuit/Cuil") @Parameter(optionality = Optionality.OPTIONAL) final String personaCuitCuil,
			@Nullable @ParameterLayout(named = "Domicilio") @Parameter(optionality = Optionality.OPTIONAL) final String personaDireccion,
			@Nullable @ParameterLayout(named = "Localidad") @Parameter(optionality = Optionality.OPTIONAL) final Localidad personaLocalidad,
			@Nullable @ParameterLayout(named = "Teléfono Fijo (con caracteristica sin el 0)") @Parameter(optionality=Optionality.OPTIONAL, maxLength=10) final Long personaTelefonoFijo,
			@Nullable @ParameterLayout(named = "Teléfono Celular (con caracteristica sin el 0)") @Parameter(optionality=Optionality.OPTIONAL, maxLength=12) final Long personaTelefonoCelular,
			@Nullable @ParameterLayout(named = "E-Mail") @Parameter(optionality = Optionality.OPTIONAL) final String personaMail) {
		return clienteNoAfiliadoRepository.crear(personaDni, personaApellido, personaNombre, personaCuitCuil, personaDireccion, 
				 personaLocalidad, personaTelefonoFijo, personaTelefonoCelular, personaMail);
	}
	
	public List<Localidad> choices5Crear() {
		return localidadesRepository.listarActivos();
	}
	
	//este es el validador para que lp no tenga menos de 6, cbu no tenga menos de 22
	public String validateCrear(final int personaDni, final String personaApellido, final String personaNombre, final String personaCuitCuil,
			final String personaDireccion, final Localidad personaLocalidad,  final Long personaTelefonoFijo, final Long personaTelefonoCelular,
			final String personaMail) {
		
		if (personaTelefonoFijo == null & personaTelefonoCelular == null) {
			return "Se tiene que cargar un numero de telefono, no puede quedar telefono fijo y telefono celular vacio";
		}
		
		if(personaTelefonoFijo!=null)
		if (personaTelefonoFijo.toString().length()<10) {
			return "Error: para el numero fijo se tiene que ingresar 10 digitos";
		}
		
		if (personaTelefonoCelular!=null)
		if (personaTelefonoCelular.toString().length()<12) {
			return "Error: para el numero celular se tiene que ingresar 12 digitos";
		}
		
		if (personaMail != null){
			// Patrón para validar el email
	        Pattern pattern = Pattern
	                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); 
	        
	        // El email a validar
	        String email = personaMail;
	 
	        Matcher mather = pattern.matcher(email);
	 
	        if (mather.find() == false) {
	            return "El mail ingresado es invalido";
	        }
		}
        return "";
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Todos los no afiliados")
	@MemberOrder(sequence = "2")
	public List<ClienteNoAfiliado> listar() {
		return clienteNoAfiliadoRepository.listar();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar no afiliado Por DNI")
	@MemberOrder(sequence = "3")
	public List<ClienteNoAfiliado> buscarPorDni(@ParameterLayout(named = "DNI") final int personaDni) {
		return clienteNoAfiliadoRepository.buscarPorDNI(personaDni);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar no afiliado 	Por Nombre")
	@MemberOrder(sequence = "4")
	public List<ClienteNoAfiliado> buscarPorNombre(@ParameterLayout(named = "Nombre") final String clienteNombre) {
		return clienteNoAfiliadoRepository.buscarPorNombre(clienteNombre);
	}
	
	@javax.inject.Inject
	ClienteNoAfiliadoRepository clienteNoAfiliadoRepository;

	@javax.inject.Inject
	LocalidadRepository localidadesRepository;

	@Inject
	MessageService messageService;
	
	@Inject
	PersonaRepository personaRepository;
	
}
