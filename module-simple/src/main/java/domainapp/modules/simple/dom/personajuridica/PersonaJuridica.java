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
package domainapp.modules.simple.dom.personajuridica;

import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import domainapp.modules.simple.dom.persona.Persona;

@javax.jdo.annotations.Queries({
	@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
			+ "FROM domainapp.modules.simple.dom.persona.Persona " + "WHERE personaActivo == true "),
	@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
			+ "FROM domainapp.modules.simple.dom.persona.Persona " + "WHERE personaActivo == false ") })
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="personaJuridicaId")
@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple"
)
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
public abstract class PersonaJuridica extends Persona{
	
    public String cssClass(){
    	return (getPersonaActivo()==true)? "activo":"inactivo";
    }
	
	public static final int NAME_LENGTH = 40;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "DNI")
	private int personaJuridicaDni;

	public int getPersonaJuridicaDni() {
		return personaJuridicaDni;
	}

	public void setPersonaJuridicaDni(int personaJuridicaDni) {
		this.personaJuridicaDni = personaJuridicaDni;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Apellido")
	private String personaJuridicaApellido;

	public String getPersonaJuridicaApellido() {
		return personaJuridicaApellido;
	}

	public void setPersonaJuridicaApellido(String personaJuridicaApellido) {
		this.personaJuridicaApellido = personaJuridicaApellido;
	}
	
	@javax.jdo.annotations.Column(allowsNull = "false", length = NAME_LENGTH)
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Nombre")
	private String personaJuridicaNombre;

	public String getPersonaJuridicaNombre() {
		return personaJuridicaNombre;
	}

	public void setPersonaJuridicaNombre(final String personaJuridicaNombre) {
		this.personaJuridicaNombre = personaJuridicaNombre;
	}

	@Inject
	PersonaJuridicaRepository personaJuridicaRepository;

}
