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
import domainapp.modules.simple.dom.persona.Persona;
import domainapp.modules.simple.dom.persona.PersonaRepository;
import domainapp.modules.simple.dom.personajuridica.PersonaJuridica;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Afiliado")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "afiliadoId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorLP", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado "
				+ "WHERE afiliadoLP.toLowerCase().indexOf(:afiliadoLP) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado "
				+ "WHERE personaNombre.toLowerCase().indexOf(:personaNombre) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorDNI", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado " + "WHERE personaDni == :personaDni"),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado " + "WHERE personaActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.afiliado.Afiliado " + "WHERE personaActivo == false ") })
@javax.jdo.annotations.Unique(name = "DNI_Apellido_UNQ1", members = { "personaJuridicaDni", "personaJuridicaApellido" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Afiliado extends PersonaJuridica implements Comparable<Afiliado> {

	// region > title
	public TranslatableString title() {
		return TranslatableString.tr(getAfiliadoEstado().getNombre() + ": {afiliadoNombre}", "afiliadoNombre",
				getPersonaJuridicaNombre() + " " + getPersonaJuridicaApellido() + " Cuit/Cuil: " + getPersonaCuitCuil());
	}
	// endregion
	
	public String cssClass() {
		return (getPersonaActivo() == true) ? "activo" : "inactivo";
	}
	
	public String iconName() {
		return (getAfiliadoEstado() == Estado.Activo) ? "A" : "R";
	}

	// region > constructor
	public Afiliado() {
	}

	public Afiliado(final String afiliadoNombre) {
		setPersonaJuridicaNombre(afiliadoNombre);
	}

	public Afiliado(Estado afiliadoEstado, String afiliadoLP, int afiliadoDni, String afiliadoApellido, String afiliadoNombre,
			String afiliadoCuitCuil, String afiliadoDireccion, Localidad afiliadoLocalidad, Long afiliadoTelefonoFijo, 
			Long afiliadoTelefonoCelular, String afiliadoMail, String afiliadoCBU) {
		super();
		setAfiliadoEstado(afiliadoEstado);
		setAfiliadoLP(afiliadoLP);
		setPersonaJuridicaDni(afiliadoDni);
		setPersonaJuridicaApellido(afiliadoApellido);
		setPersonaJuridicaNombre(afiliadoNombre);
		setPersonaCuitCuil(afiliadoCuitCuil);
		setPersonaDireccion(afiliadoDireccion);
		setPersonaLocalidad(afiliadoLocalidad);
		setPersonaLocalidad(afiliadoLocalidad);
		setPersonaTelefonoFijo(afiliadoTelefonoFijo);
		setPersonaTelefonoCelular(afiliadoTelefonoCelular);
		setPersonaMail(afiliadoMail);
		setAfiliadoCBU(afiliadoCBU);
		setPersonaActivo(true);
	}
	// endregion

	// region > name (read-only property)
	public static final int NAME_LENGTH = 40;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Estado")
	private Estado afiliadoEstado;

	public Estado getAfiliadoEstado() {
		return afiliadoEstado;
	}

	public void setAfiliadoEstado(Estado afiliadoEstado) {
		this.afiliadoEstado = afiliadoEstado;
	}

	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "LP")
	private String afiliadoLP;

	public String getAfiliadoLP() {
		return afiliadoLP;
	}

	public void setAfiliadoLP(String afiliadoLP) {
		this.afiliadoLP = afiliadoLP;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED, hidden=Where.EVERYWHERE)
	@PropertyLayout(named = "CBU")
	private String afiliadoCBU;

	public String getAfiliadoCBU() {
		return afiliadoCBU;
	}

	public void setAfiliadoCBU(String afiliadoCBU) {
		this.afiliadoCBU = afiliadoCBU;
	}
	
	// endregion
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoEstado")
	public Afiliado actualizarEstado(@ParameterLayout(named = "Estado") final Estado afiliadoEstado) {
		setAfiliadoEstado(afiliadoEstado);
		return this;
	}
	
	public Estado default0ActualizarEstado() {
		return getAfiliadoEstado();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoLP")
	public Afiliado actualizarLP(@Nullable @ParameterLayout(named = "LP", typicalLength=6) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 6) final String afiliadoLP) {
		setAfiliadoLP(afiliadoLP);
		return this;
	}

	public String default0ActualizarLP() {
		return getAfiliadoLP();
	}
	
	public String validateActualizarLP(final String numeroLP) {
        if (isNumeric(numeroLP) == false) {
            return "Todos los caracteres del LP deben ser numericos";
        }
		if (numeroLP.length()<=5) {
			return "LP debe contener 6 digitos";
		}
		return "";
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoNombre")
	public Afiliado actualizarNombre(@ParameterLayout(named = "Nombre") final String afiliadoNombre) {
		setPersonaJuridicaNombre(afiliadoNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getPersonaJuridicaNombre();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoApellido")
	public Afiliado actualizarApellido(@ParameterLayout(named = "Apellido") final String afiliadoApellido) {
		setPersonaJuridicaApellido(afiliadoApellido);
		return this;
	}

	public String default0ActualizarApellido() {
		return getPersonaJuridicaApellido();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoDni")
	public Afiliado actualizarDni(@ParameterLayout(named = "Numero de Documento") final int afiliadoDni) {
		setPersonaJuridicaDni(afiliadoDni);
		return this;
	}

	public int default0ActualizarDni() {
		return getPersonaJuridicaDni();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoCuitCuil")
	public Afiliado actualizarCuitCuil(@ParameterLayout(named = "Cuit/Cuil") final String afiliadoCuitCuil) {
		setPersonaCuitCuil(afiliadoCuitCuil);
		return this;
	}

	public String default0ActualizarCuitCuil() {
		return getPersonaCuitCuil();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoDireccion")
	public Afiliado actualizarDireccion(@ParameterLayout(named = "Direccion") final String afiliadoDireccion) {
		setPersonaDireccion(afiliadoDireccion);
		return this;
	}

	public String default0ActualizarDireccion() {
		return getPersonaDireccion();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoLocalidad")
	public Afiliado actualizarLocalidad(@ParameterLayout(named = "Localidades") final Localidad afiliadoLocalidad) {
		setPersonaLocalidad(afiliadoLocalidad);
		return this;
	}

	public List<Localidad> choices0ActualizarLocalidad() {
		return localidadRepository.listarActivos();
	}

	public Localidad default0ActualizarLocalidad() {
		return getPersonaLocalidad();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "personaTelefonoFijo")
	public Afiliado actualizarTelefonos(@Nullable @ParameterLayout(named = "Teléfono Fijo") @Parameter(optionality = Optionality.OPTIONAL, maxLength=10) final Long personaTelefonoFijo,
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

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoMail")
	public Afiliado actualizarMail(@ParameterLayout(named = "Mail") final String afiliadoMail) {
		setPersonaMail(afiliadoMail);
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

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "afiliadoActivo")
	public Afiliado actualizarActivo(@ParameterLayout(named = "Activo") final boolean afiliadoActivo) {
		setPersonaActivo(afiliadoActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getPersonaActivo();
	}

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarAfiliado() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setPersonaActivo(false);
	}
	// endregion

	@Override
	public String toString() {
		String afiliado = getPersonaJuridicaNombre() + " " + getPersonaJuridicaApellido();
		return afiliado;
	}

	@Override
	public int compareTo(Afiliado o) {
		return this.getPersonaJuridicaApellido().compareTo(o.getPersonaJuridicaApellido());  
	}

	// endregion

	// accion
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todos los afiliados")
	@MemberOrder(sequence = "2")
	public List<Afiliado> listarAfiliado() {
		return afiliadoRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar afiliados activos")
	@MemberOrder(sequence = "2")
	public List<Afiliado> listarAfiliadoActivos() {
		return afiliadoRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar afiliados inactivos")
	@MemberOrder(sequence = "2")
	public List<Afiliado> listarAfiliadoInactivos() {
		return afiliadoRepository.listarInactivos();
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
	AfiliadoRepository afiliadoRepository;
	
	// endregion

}
