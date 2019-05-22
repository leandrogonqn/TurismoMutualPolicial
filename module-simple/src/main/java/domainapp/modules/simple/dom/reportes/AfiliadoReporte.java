package domainapp.modules.simple.dom.reportes;

import domainapp.modules.simple.dom.afiliado.TipoAfiliado;
import domainapp.modules.simple.dom.localidad.Localidad;

public class AfiliadoReporte {
	
	public TipoAfiliado afiliadoEstado;
	
	public TipoAfiliado getAfiliadoEstado() {
		return afiliadoEstado;
	}
	
	public void setAfiliadoEstado(TipoAfiliado afiliadoEstado) {
		this.afiliadoEstado = afiliadoEstado;
	}
	
	private String afiliadoLP;

	public String getAfiliadoLP() {
		return afiliadoLP;
	}
	
	public void setAfiliadoLP(String afiliadoLP) {
		this.afiliadoLP = afiliadoLP;
	}
	
	private int personaJuridicaDni;

	public int getPersonaJuridicaDni() {
		return personaJuridicaDni;
	}

	public void setPersonaJuridicaDni(int personaJuridicaDni) {
		this.personaJuridicaDni = personaJuridicaDni;
	}
	
	private String personaJuridicaApellido;

	public String getPersonaJuridicaApellido() {
		return personaJuridicaApellido;
	}

	public void setPersonaJuridicaApellido(String personaJuridicaApellido) {
		this.personaJuridicaApellido = personaJuridicaApellido;
	}
	
	private String personaJuridicaNombre;

	public String getPersonaJuridicaNombre() {
		return personaJuridicaNombre;
	}
	
	public void setPersonaJuridicaNombre(final String personaJuridicaNombre) {
		this.personaJuridicaNombre = personaJuridicaNombre;
	}

	 private String personaCuitCuil;
	 
	 public String getPersonaCuitCuil() {
		return personaCuitCuil;
	 }
	 
	 public void setPersonaCuitCuil(String personaCuitCuil) {
		this.personaCuitCuil = personaCuitCuil;
	 }	

	 private String personaDireccion;

	 public String getPersonaDireccion() {
		return personaDireccion;
	}
	 
		public void setPersonaDireccion(String personaDireccion) {
			this.personaDireccion = personaDireccion;
		}	 

	private Localidad personaLocalidad;

	public Localidad getPersonaLocalidad() {
		return personaLocalidad;
	}

	public void setPersonaLocalidad(Localidad personaLocalidad) {
		this.personaLocalidad = personaLocalidad;
	}
	
  private Long personaTelefonoFijo;

  public Long getPersonaTelefonoFijo() {
		return personaTelefonoFijo;
	}
  
	public void setPersonaTelefonoFijo(Long personaTelefonoFijo) {
		this.personaTelefonoFijo = personaTelefonoFijo;
	}	

  private Long personaTelefonoCelular;

  public Long getPersonaTelefonoCelular() {
		return personaTelefonoCelular;
  }
  
	public void setPersonaTelefonoCelular(Long personaTelefonoCelular) {
		this.personaTelefonoCelular = personaTelefonoCelular;
	}	
  
		private String personaMail;

  public String getPersonaMail() {
		return personaMail;
	}
  
	public void setPersonaMail(String personaMail) {
		this.personaMail = personaMail;
	}

  private String afiliadoCBU;

	public String getAfiliadoCBU() {
		return afiliadoCBU;
	}
	
	public void setAfiliadoCBU(String afiliadoCBU) {
		this.afiliadoCBU = afiliadoCBU;
	}

	private boolean personaHabilitado;

   public boolean getPersonaHabilitado() {
		return personaHabilitado;
	}
   
	public void setPersonaHabilitado(boolean personaHabilitado) {
		this.personaHabilitado = personaHabilitado;
	}	

}
