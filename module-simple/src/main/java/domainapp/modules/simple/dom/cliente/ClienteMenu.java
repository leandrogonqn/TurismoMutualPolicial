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
import javax.annotation.Nullable;
import javax.inject.Inject;

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

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Cliente.class, objectType="simple.ClienteMenu")
@DomainServiceLayout(named = "Clientes", menuOrder = "10.1")
public class ClienteMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Todos los Clientes")
	@MemberOrder(sequence = "2")
	public List<Cliente> listar() {
		return clientesRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar Por Nombre")
	@MemberOrder(sequence = "5")
	public List<Cliente> buscarPorNombre(@ParameterLayout(named = "Nombre") final String clienteNombre) {
		return clientesRepository.buscarPorNombre(clienteNombre);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar Por Nombre")
	@MemberOrder(sequence = "6")
	public List<Cliente> buscarPorLP(@ParameterLayout(named = "LP") @Parameter(maxLength=6) final String clienteLP) {
		return clientesRepository.buscarPorLP(clienteLP);
	}
	
	public String validateBuscarPorLP(final String clienteLP) {
		if (clienteLP.length()<=5) {
			return "LP debe contener 6 digitos";
		}
		return "";
	}
	

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, cssClassFa = "fa-search", named = "Buscar Por DNI")
	@MemberOrder(sequence = "7")
	public List<Cliente> buscarPorDNI(@ParameterLayout(named = "DNI") final int clienteDni) {
		return clientesRepository.buscarPorDNI(clienteDni);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Cliente")
	@MemberOrder(sequence = "1")
	public Cliente crear(@ParameterLayout(named = "Estado") final Estado clienteEstado,
			@Nullable @ParameterLayout(named = "LP", typicalLength=6) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 6) final String clienteLP,
			@ParameterLayout(named = "Nombre") final String clienteNombre,
			@ParameterLayout(named = "Apellido") final String clienteApellido,
			@ParameterLayout(named = "Numero de Documento") final int clienteDni,
			@Nullable @ParameterLayout(named = "Cuit/Cuil") @Parameter(optionality = Optionality.OPTIONAL) final String clienteCuitCuil,
			@ParameterLayout(named = "Sexo") final Sexo clienteSexo,
			@Nullable @ParameterLayout(named = "Dirección") @Parameter(optionality = Optionality.OPTIONAL) final String clienteDireccion,
			@Nullable @ParameterLayout(named = "Localidad") @Parameter(optionality = Optionality.OPTIONAL) final Localidad clienteLocalidad,
			@ParameterLayout(named = "Teléfono") final String clienteTelefono,
			@Nullable @ParameterLayout(named = "E-Mail") @Parameter(optionality = Optionality.OPTIONAL) final String clienteMail,
			@Nullable @ParameterLayout(named = "Fecha de Nacimiento") @Parameter(optionality = Optionality.OPTIONAL) final Date clienteFechaNacimiento,
			@Nullable @ParameterLayout(named = "CBU", typicalLength=22) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 22) final String clienteCBU) {
		return clientesRepository.crear(clienteLP, clienteNombre, clienteApellido, clienteSexo, clienteDni, clienteCuitCuil, 
				 clienteDireccion, clienteLocalidad, clienteTelefono, clienteMail, clienteFechaNacimiento, clienteEstado, clienteCBU);
	}
	
//	public int choices2Crear(final Estado clienteEstado, final int clienteLP, final String clienteNombre, 
//			final String clienteApellido, final int clienteDni, final String clienteCuitCuil, final Sexo clienteSexo,
//			final String clienteDireccion, final Localidad clienteLocalidad, final String clienteTelefono, final String clienteMail,
//			final Date clienteFechaNacimiento, final int clienteCBU) {
//		if (clienteEstado==Estado.No_Socio) {
//			return 0;
//		}
//		return 1	;
//	}
	
	public List<Localidad> choices8Crear() {
		return localidadesRepository.listarActivos();
	}
	
	//este es el validador para que lp no tenga menos de 6, cbu no tenga menos de 22
	public String validateCrear(final Estado clienteEstado, final String numeroLP, final String clienteNombre, 
			final String clienteApellido, final int clienteDni, final String clienteCuitCuil, final Sexo clienteSexo,
			final String clienteDireccion, final Localidad clienteLocalidad, final String clienteTelefono, final String clienteMail,
			final Date clienteFechaNacimiento, final String clienteCBU) {

		if (numeroLP!=null) {
	        if (isNumeric(numeroLP) == false) {
	            return "Todos los caracteres del LP deben ser numericos";
	        }
			if (clienteEstado==Estado.No_Socio & numeroLP!="0") {
				return "Si el cliente no es socio LP tiene que ser 0 o nulo";
			} 
			if (clienteEstado==Estado.Activo||clienteEstado==Estado.Retirado) {
				if (numeroLP.length()<=5) {
					return "LP debe contener 6 digitos";
				}
			}
		} else {
			if (clienteEstado==Estado.Activo||clienteEstado==Estado.Retirado) {
				return "Si el socio es activo o retirado debe ingresar el LP";
			}
		}
		if (clienteCBU!=null) {
			if (clienteCBU.length()<22) {
				return "CBU debe contener 6 digitos";
			}
			String primeraParte = clienteCBU.substring(0, 7);
			String segundaParte = clienteCBU.substring(8, 14);
			String terceraParte = clienteCBU.substring(15, 22);
	        if (isNumeric(primeraParte) == false) {
	            return "Todos los caracteres del CBU deben ser numericos";
	        }
	        if (isNumeric(segundaParte) == false) {
	            return "Todos los caracteres del CBU deben ser numericos";
	        }
	        if (isNumeric(terceraParte) == false) {
	            return "Todos los caracteres del CBU deben ser numericos";
	        }
		}
		return "";
	}
	
        public static boolean isNumeric(String cadena) {

            boolean resultado;

            try {
                Integer.parseInt(cadena);
                resultado = true;
            } catch (NumberFormatException excepcion) {
                resultado = false;
            }

            return resultado;
        }
        

	@javax.inject.Inject
	ClienteRepository clientesRepository;

	@javax.inject.Inject
	LocalidadRepository localidadesRepository;

	@Inject
	MessageService messageService;
	
}
