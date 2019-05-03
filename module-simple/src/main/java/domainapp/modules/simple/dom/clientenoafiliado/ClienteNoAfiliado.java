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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.personajuridica.PersonaJuridica;

@javax.jdo.annotations.Queries({
	@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
			+ "FROM domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliado "
			+ "WHERE personaNombre.toLowerCase().indexOf(:personaNombre) >= 0 "),
	@javax.jdo.annotations.Query(name = "buscarPorDNI", language = "JDOQL", value = "SELECT "
			+ "FROM domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliado " + "WHERE personaDni == :personaDni"),
	@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
			+ "FROM domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliado " + "WHERE personaActivo == true "),
	@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
			+ "FROM domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliado " + "WHERE personaActivo == false ") })
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "ClienteNoAfiliado")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "clienteNoAfiliadoId")
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ClienteNoAfiliado extends PersonaJuridica implements Comparable<ClienteNoAfiliado> {

	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("Cliente No Afiliado: {personaNombre}", "personaNombre",
				getPersonaJuridicaNombre() + " " + getPersonaJuridicaApellido() + " Cuit/Cuil: " + getPersonaCuitCuil());
	}
	// endregion
	
	public String cssClass() {
		return (getPersonaActivo() == true) ? "activo" : "inactivo";
	}

	// region > constructor
	public ClienteNoAfiliado() {
	}

	public ClienteNoAfiliado(final String personaNombre) {
		setPersonaJuridicaNombre(personaNombre);
	}

	public ClienteNoAfiliado(int personaDni, String personaApellido, String personaNombre, String personaCuitCuil, 
			String personaDireccion, Localidad personaLocalidad,  Long personaTelefonoFijo, Long personaTelefonoCelular,
			String personaMail) {
		setPersonaJuridicaDni(personaDni);
		setPersonaJuridicaApellido(personaApellido);
		setPersonaJuridicaNombre(personaNombre);
		setPersonaCuitCuil(personaCuitCuil);
		setPersonaDireccion(personaDireccion);
		setPersonaLocalidad(personaLocalidad);
		setPersonaTelefonoFijo(personaTelefonoFijo);
		setPersonaTelefonoCelular(personaTelefonoCelular);
		setPersonaMail(personaMail);
		setPersonaActivo(true);
	}
	// endregion

	// region > name (read-only property)
	public static final int NAME_LENGTH = 40;
	
	
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaNombre")
	public ClienteNoAfiliado actualizarNombre(@ParameterLayout(named = "Nombre") final String personaNombre) {
		setPersonaJuridicaNombre(personaNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getPersonaJuridicaNombre();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaApellido")
	public ClienteNoAfiliado actualizarApellido(@ParameterLayout(named = "Apellido") final String personaApellido) {
		setPersonaJuridicaApellido(personaApellido);
		return this;
	}

	public String default0ActualizarApellido() {
		return getPersonaJuridicaApellido();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaDni")
	public ClienteNoAfiliado actualizarDni(@ParameterLayout(named = "Numero de Documento") final int personaDni) {
		setPersonaJuridicaDni(personaDni);
		return this;
	}

	public int default0ActualizarDni() {
		return getPersonaJuridicaDni();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaCuitCuil")
	public ClienteNoAfiliado actualizarCuitCuil(@ParameterLayout(named = "Cuit/Cuil") final String personaCuitCuil) {
		setPersonaCuitCuil(personaCuitCuil);
		return this;
	}

	public String default0ActualizarCuitCuil() {
		return getPersonaCuitCuil();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaDireccion")
	public ClienteNoAfiliado actualizarDireccion(@ParameterLayout(named = "Direccion") final String personaDireccion) {
		setPersonaDireccion(personaDireccion);
		return this;
	}

	public String default0ActualizarDireccion() {
		return getPersonaDireccion();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaLocalidad")
	public ClienteNoAfiliado actualizarLocalidad(@ParameterLayout(named = "Localidades") final Localidad personaLocalidad) {
		setPersonaLocalidad(personaLocalidad);
		return this;
	}

	public List<Localidad> choices0ActualizarLocalidad() {
		return localidadRepository.listarActivos();
	}

	public Localidad default0ActualizarLocalidad() {
		return getPersonaLocalidad();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaTelefonoFijo")
	public ClienteNoAfiliado actualizarTelefonos(@Nullable @ParameterLayout(named = "Teléfono Fijo") @Parameter(optionality = Optionality.OPTIONAL, maxLength=10) final Long personaTelefonoFijo,
			@Nullable @ParameterLayout(named = "Teléfono Celular") @Parameter(optionality = Optionality.OPTIONAL, maxLength=12) final Long personaTelefonoCelular) {
		setPersonaTelefonoFijo(personaTelefonoFijo);
		setPersonaTelefonoCelular(personaTelefonoCelular);
		return this;
	}

	public Long default0ActualizarTelefonos() {
		return getPersonaTelefonoFijo();
	}
	
	public Long default1ActualizarTelefonos() {
		return getPersonaTelefonoCelular();
	}
	
	public String validateActualizarTelefonos(final Long afiliadoTelefonoFijo, final Long afiliadoTelefonoCelular) {
		if (afiliadoTelefonoFijo == null & afiliadoTelefonoCelular == null) {
			return "Se tiene que cargar un numero de telefono, no puede quedar telefono fijo y telefono celular vacio";
		}
		
		if(afiliadoTelefonoFijo!=null)
		if (afiliadoTelefonoFijo.toString().length()<10) {
			return "Error: para el numero fijo se tiene que ingresar 10 digitos";
		}
		
		if (afiliadoTelefonoCelular!=null)
		if (afiliadoTelefonoCelular.toString().length()<12) {
			return "Error: para el numero celular se tiene que ingresar 12 digitos";
		}
		return "";
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaMail")
	public ClienteNoAfiliado actualizarMail(@ParameterLayout(named = "Mail") final String personaMail) {
		setPersonaMail(personaMail);
		return this;
	}

	public String default0ActualizarMail() {
		return getPersonaMail();
	}
	
	public String validateActualizarMail(final String personaMail) {
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

	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaActivo")
	public ClienteNoAfiliado actualizarActivo(@ParameterLayout(named = "Activo") final boolean personaActivo) {
		setPersonaActivo(personaActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getPersonaActivo();
	}

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarClienteNoAfiliado() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setPersonaActivo(false);
	}
	// endregion

	@Override
	public String toString() {
		String clienteNoAfiliado = getPersonaJuridicaNombre() + " " + getPersonaJuridicaApellido();
		return clienteNoAfiliado;
	}

	@Override
	public int compareTo(ClienteNoAfiliado o) {
		return this.getPersonaJuridicaApellido().compareTo(o.getPersonaJuridicaApellido());  
	}

	// endregion

	// accion
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todos los clientes no afiliados")
	@MemberOrder(sequence = "2")
	public List<ClienteNoAfiliado> listarClienteNoAfiliado() {
		return clienteNoAfiliadoRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar clientes no socios activos")
	@MemberOrder(sequence = "2")
	public List<ClienteNoAfiliado> listarClienteActivos() {
		return clienteNoAfiliadoRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar clientes no afiliados inactivos")
	@MemberOrder(sequence = "2")
	public List<ClienteNoAfiliado> listarClienteNoAfiliadoInactivos() {
		return clienteNoAfiliadoRepository.listarInactivos();
	}
	
	@ActionLayout(hidden=Where.EVERYWHERE)
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
    

	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	LocalidadRepository localidadRepository;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	ClienteNoAfiliadoRepository clienteNoAfiliadoRepository;
	
//	@Inject
//	PersonaRepository personaRepository;

	// endregion

}
