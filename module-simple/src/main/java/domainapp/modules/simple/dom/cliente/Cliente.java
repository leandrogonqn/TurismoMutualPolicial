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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
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
import domainapp.modules.simple.dom.prestador.Prestador;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Clientes")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "clienteId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorNombre", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.cliente.Cliente "
				+ "WHERE clienteNombre.toLowerCase().indexOf(:clienteNombre) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorLP", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.cliente.Cliente "
				+ "WHERE clienteLP.toLowerCase().indexOf(:clienteLP) >= 0 "),
		@javax.jdo.annotations.Query(name = "buscarPorDNI", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.cliente.Cliente " + "WHERE clienteDni == :clienteDni"),
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.cliente.Cliente " + "WHERE clienteActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.cliente.Cliente " + "WHERE clienteActivo == false ") })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Cliente implements Comparable<Cliente> {

	// region > title
	public TranslatableString title() {
		return TranslatableString.tr("Cliente: {clienteNombre}", "clienteNombre",
				getClienteNombre() + " " + getClienteApellido() + " Cuit/Cuil: " + getClienteCuitCuil());
	}
	// endregion
	
	public String cssClass() {
		return (getClienteActivo() == true) ? "activo" : "inactivo";
	}

	public String iconName() {
		return (getClienteSexo() == Sexo.Femenino) ? "Femenino" : "Masculino";
	}

	// region > constructor
	public Cliente() {
	}

	public Cliente(final String clienteNombre) {
		setClienteNombre(clienteNombre);
	}

	public Cliente(String clienteLP, String clienteNombre, String clienteApellido, Sexo clienteSexo, int clienteDni, String clienteCuitCuil, 
			String clienteDireccion, Localidad clienteLocalidad,  String clienteTelefono, String clienteMail, 
			Date clienteFechaNacimiento, Estado clienteEstado, String clienteCBU) {
		super();
		setClienteLP(clienteLP);
		setClienteNombre(clienteNombre);
		setClienteApellido(clienteApellido);
		setClienteSexo(clienteSexo);
		setClienteDni(clienteDni);
		setClienteCuitCuil(clienteCuitCuil);
		setClienteDireccion(clienteDireccion);
		setClienteLocalidad(clienteLocalidad);
		setClienteTelefono(clienteTelefono);
		setClienteMail(clienteMail);
		setClienteFechaNacimiento(clienteFechaNacimiento);
		setClienteEstado(clienteEstado);
		setClienteCBU(clienteCBU);
		setClienteActivo(true);
	}
	// endregion

	// region > name (read-only property)
	public static final int NAME_LENGTH = 40;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Estado")
	private Estado clienteEstado;

	public Estado getClienteEstado() {
		return clienteEstado;
	}

	public void setClienteEstado(Estado clienteEstado) {
		this.clienteEstado = clienteEstado;
	}

	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "LP")
	private String clienteLP;

	public String getClienteLP() {
		return clienteLP;
	}

	public void setClienteLP(String clienteLP) {
		this.clienteLP = clienteLP;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String clienteNombre;

	public String getClienteNombre() {
		return clienteNombre;
	}

	public void setClienteNombre(final String clienteNombre) {
		this.clienteNombre = clienteNombre;
	}

	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Apellido")
	private String clienteApellido;

	public String getClienteApellido() {
		return clienteApellido;
	}

	public void setClienteApellido(String clienteApellido) {
		this.clienteApellido = clienteApellido;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "DNI")
	private int clienteDni;

	public int getClienteDni() {
		return clienteDni;
	}

	public void setClienteDni(int clienteDni) {
		this.clienteDni = clienteDni;
	}
	
	 @javax.jdo.annotations.Column(allowsNull = "true")
	 @Property(
	           editing = Editing.DISABLED
	 )
	 @PropertyLayout(named="Cuit/Cuil", hidden=Where.ALL_TABLES)
	 private String clienteCuitCuil;
	 public String getClienteCuitCuil() {
		return clienteCuitCuil;
	 }
	 public void setClienteCuitCuil(String clienteCuitCuil) {
		this.clienteCuitCuil = clienteCuitCuil;
	 }	
	 
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED, hidden=Where.ALL_TABLES)
	@PropertyLayout(named = "Sexo")
	private Sexo clienteSexo;

	public Sexo getClienteSexo() {
		return clienteSexo;
	}

	public void setClienteSexo(Sexo clienteSexo) {
		this.clienteSexo = clienteSexo;
	}

	@javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Direccion", hidden=Where.ALL_TABLES)
    private String clienteDireccion;

    public String getClienteDireccion() {
		return clienteDireccion;
	}
	public void setClienteDireccion(String clienteDireccion) {
		this.clienteDireccion = clienteDireccion;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "true", name="localidadId")
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Localidad", hidden=Where.ALL_TABLES)
    private Localidad clienteLocalidad;

	public Localidad getClienteLocalidad() {
		return clienteLocalidad;
	}

	public void setClienteLocalidad(Localidad clienteLocalidad) {
		this.clienteLocalidad = clienteLocalidad;
	}

    @javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Telefono")
    private String clienteTelefono;

    public String getClienteTelefono() {
		return clienteTelefono;
	}
	public void setClienteTelefono(String clienteTelefono) {
		this.clienteTelefono = clienteTelefono;
	}	

    @javax.jdo.annotations.Column(allowsNull = "true", length = NAME_LENGTH)
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Mail", hidden=Where.ALL_TABLES)
    private String clienteMail;

    public String getClienteMail() {
		return clienteMail;
	}
	public void setClienteMail(String clienteMail) {
		this.clienteMail = clienteMail;
	}	

	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Fecha de Nacimiento")
	private Date clienteFechaNacimiento;

	public Date getClienteFechaNacimiento() {
		return clienteFechaNacimiento;
	}

	public void setClienteFechaNacimiento(Date clienteFechaNacimiento) {
		this.clienteFechaNacimiento = clienteFechaNacimiento;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED, hidden=Where.ALL_TABLES)
	@PropertyLayout(named = "CBU")
	private String clienteCBU;

	public String getClienteCBU() {
		return clienteCBU;
	}

	public void setClienteCBU(String clienteCBU) {
		this.clienteCBU = clienteCBU;
	}

    @javax.jdo.annotations.Column(allowsNull = "false")
    @Property(
            editing = Editing.DISABLED
    )
    @PropertyLayout(named="Activo", hidden=Where.ALL_TABLES)
    private boolean clienteActivo;
 
    public boolean getClienteActivo() {
		return clienteActivo;
	}
	public void setClienteActivo(boolean clienteActivo) {
		this.clienteActivo = clienteActivo;
	}	
	// endregion
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteEstado")
	public Cliente actualizarEstado(@ParameterLayout(named = "Estado") final Estado clienteEstado,
			@Nullable @ParameterLayout(named = "LP", typicalLength=6) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 6) final String clienteLP) {
		setClienteEstado(clienteEstado);
		setClienteLP(clienteLP);
		return this;
	}
	
	public Estado default0ActualizarEstado() {
		return getClienteEstado();
	}

	public String default1ActualizarEstado() {
		return getClienteLP();
	}
	
	public String validateActualizarEstado(final Estado clienteEstado, final String numeroLP) {
		if (numeroLP!=null) {
	        if (isNumeric(numeroLP) == false) {
	            return "Todos los caracteres del LP deben ser numericos";
	        }
			if (clienteEstado==Estado.No_Socio & numeroLP!="0") {
				return "Si el cliente no es socio, el LP tiene que ser 0 o nulo";
			} 
			if (clienteEstado==Estado.Activo||clienteEstado==Estado.Retirado) {
				if (numeroLP.length()<=5) {
					return "LP debe contener 6 digitos";
				}
			}
		} else {
			if (clienteEstado==Estado.Activo||clienteEstado==Estado.Retirado) {
				return "Si el cliente es activo o retirado, el LP no puede ser nulo";
			}
		}
		return "";
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteLP")
	public Cliente actualizarLP(@Nullable @ParameterLayout(named = "LP", typicalLength=6) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 6) final String clienteLP) {
		setClienteLP(clienteLP);
		return this;
	}

	public String default0ActualizarLP() {
		return getClienteLP();
	}
	
	public String validateActualizarLP(final String numeroLP) {
		if (numeroLP!=null) {
	        if (isNumeric(numeroLP) == false) {
	            return "Todos los caracteres del LP deben ser numericos";
	        }
			if (this.clienteEstado==Estado.No_Socio & numeroLP!="0") {
				return "Si el cliente no es socio, el LP tiene que ser 0 o nulo (primero modifique el estado si asi lo desea)";
			} 
			if (this.clienteEstado==Estado.Activo||this.clienteEstado==Estado.Retirado) {
				if (numeroLP.length()<=5) {
					return "LP debe contener 6 digitos";
				}
			}
		} else {
			if (this.clienteEstado==Estado.Activo||this.clienteEstado==Estado.Retirado) {
				return "Si el cliente es activo o retirado, el LP no puede ser nulo (primero modifique el estado si asi lo desea)";
			}
		}
		return "";
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteNombre")
	public Cliente actualizarNombre(@ParameterLayout(named = "Nombre") final String clienteNombre) {
		setClienteNombre(clienteNombre);
		return this;
	}

	public String default0ActualizarNombre() {
		return getClienteNombre();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteApellido")
	public Cliente actualizarApellido(@ParameterLayout(named = "Apellido") final String clienteApellido) {
		setClienteApellido(clienteApellido);
		return this;
	}

	public String default0ActualizarApellido() {
		return getClienteApellido();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteDni")
	public Cliente actualizarDni(@ParameterLayout(named = "Numero de Documento") final int clienteDni) {
		setClienteDni(clienteDni);
		return this;
	}

	public int default0ActualizarDni() {
		return getClienteDni();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteCuitCuil")
	public Cliente actualizarCuitCuil(@ParameterLayout(named = "Cuit/Cuil") final String clienteCuitCuil) {
		setClienteCuitCuil(clienteCuitCuil);
		return this;
	}

	public String default0ActualizarCuitCuil() {
		return getClienteCuitCuil();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteSexo")
	public Cliente actualizarSexo(@ParameterLayout(named = "Sexo") final Sexo clienteSexo) {
		setClienteSexo(clienteSexo);
		return this;
	}
	
	public Sexo default0ActualizarSexo() {
		return getClienteSexo();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteDireccion")
	public Cliente actualizarDireccion(@ParameterLayout(named = "Direccion") final String clienteDireccion) {
		setClienteDireccion(clienteDireccion);
		return this;
	}

	public String default0ActualizarDireccion() {
		return getClienteDireccion();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteLocalidad")
	public Cliente actualizarLocalidad(@ParameterLayout(named = "Localidades") final Localidad clienteLocalidad) {
		setClienteLocalidad(clienteLocalidad);
		return this;
	}

	public List<Localidad> choices0ActualizarLocalidad() {
		return localidadRepository.listarActivos();
	}

	public Localidad default0ActualizarLocalidad() {
		return getClienteLocalidad();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteTelefono")
	public Cliente actualizarTelefono(@ParameterLayout(named = "Telefono") final String clienteTelefono) {
		setClienteTelefono(clienteTelefono);
		return this;
	}

	public String default0ActualizarTelefono() {
		return getClienteTelefono();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteMail")
	public Cliente actualizarMail(@ParameterLayout(named = "Mail") final String clienteMail) {
		setClienteMail(clienteMail);
		return this;
	}

	public String default0ActualizarMail() {
		return getClienteMail();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteFechaNacimiento")
	public Cliente actualizarFechaNacimiento(
			@Nullable @ParameterLayout(named = "Fecha de Nacimiento") @Parameter(optionality = Optionality.OPTIONAL) final Date clienteFechaNacimiento) {
		setClienteFechaNacimiento(clienteFechaNacimiento);
		return this;
	}

	public Date default0ActualizarFechaNacimiento() {
		return getClienteFechaNacimiento();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteCBU")
	public Cliente actualizarCBU(@Nullable @ParameterLayout(named = "CBU", typicalLength=22) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 22) final String clienteCBU) {
		setClienteCBU(clienteCBU);
		return this;
	}

	public String default0ActualizarCBU() {
		return getClienteCBU();
	}
	
	public String validateActualizarCBU(final String clienteCBU) {
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
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "clienteActivo")
	public Cliente actualizarActivo(@ParameterLayout(named = "Activo") final boolean clienteActivo) {
		setClienteActivo(clienteActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getClienteActivo();
	}

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarCliente() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setClienteActivo(false);
	}
	// endregion

	@Override
	public String toString() {
		String cliente = getClienteNombre() + " " + getClienteApellido();
		return cliente;
	}

	@Override
	public int compareTo(Cliente o) {
		if (clienteFechaNacimiento.before(o.getClienteFechaNacimiento())) {
			return -1;
		}
		if (clienteFechaNacimiento.after(o.getClienteFechaNacimiento())) {
			return 1;
		}
		return 0;
	}

	// endregion

	// accion
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todos los clientes")
	@MemberOrder(sequence = "2")
	public List<Cliente> listarClientes() {
		return clientesRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar clientes activos")
	@MemberOrder(sequence = "2")
	public List<Cliente> listarClientesActivos() {
		return clientesRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar clientes inactivos")
	@MemberOrder(sequence = "2")
	public List<Cliente> listarClienteInactivos() {
		return clientesRepository.listarInactivos();
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
	ClienteRepository clientesRepository;

	// endregion

}
