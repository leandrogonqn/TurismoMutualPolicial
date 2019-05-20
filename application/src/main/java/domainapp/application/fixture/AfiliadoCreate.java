package domainapp.application.fixture;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.modules.simple.dom.afiliado.Afiliado;
import domainapp.modules.simple.dom.afiliado.AfiliadoMenu;
import domainapp.modules.simple.dom.afiliado.TipoAfiliado;
import domainapp.modules.simple.dom.localidad.Localidad;

public class AfiliadoCreate extends FixtureScript {

    //region > name (input)
	
    private TipoAfiliado estado;
    
    public TipoAfiliado getEstado(){
    	return estado;
    }
    
    public AfiliadoCreate setEstado(final TipoAfiliado estado){
    	this.estado = estado;
    	return this;
    }
    
    private String LP;
    
    public String getLP(){
    	return LP;
    }
    
    public AfiliadoCreate setLP(final String LP){
    	this.LP = LP;
    	return this;
    }
	
    private String name;

    public String getName() {
        return name;
    }

    public AfiliadoCreate setName(final String name) {
        this.name = name;
        return this;
    }
    
    private String apellido;
    
    public String getApellido(){
    	return apellido;
    }
    
    public AfiliadoCreate setApellido(final String apellido){
    	this.apellido = apellido;
    	return this;
    }
    
    private int dni;
    
    public int getDni() {
		return dni;
	}
    
    public AfiliadoCreate setDni(final int dni){
    	this.dni = dni;
    	return this;
    }
    
    private String cuitCuil;
    
    public String cuitCuil() {
    	return cuitCuil;
    }
    
    public AfiliadoCreate setCuitCuil(final String cuitCuil) {
    	this.cuitCuil = cuitCuil;
    	return this;
    }
    
    private String direccion;

   	public String getDireccion() {
   		return direccion;
   	}

   	public AfiliadoCreate setDireccion(String direccion) {
   		this.direccion = direccion;
   		return this;
   	}
    
    private Localidad localidad;
    
    public Localidad getLocalidad(){
    	return localidad;
    }
    
    public AfiliadoCreate setLocalidad(final Localidad localidad){
    	this.localidad = localidad;
    	return this;
    }
    
   	private Long telefonoFijo;
   	public Long getTelefonoFijo() {
   		return telefonoFijo;
   	}

   	public void setTelefonoFijo(Long telefonoFijo) {
   		this.telefonoFijo = telefonoFijo;
   	}
   	
   	private Long telefonoCelular;
   	public Long getTelefonoCelular() {
   		return telefonoCelular;
   	}

   	public void setTelefonoCelular(Long telefonoCelular) {
   		this.telefonoCelular = telefonoCelular;
   	}
   	
   	private String email;

   	public String getEmail() {
   		return email;
   	}

   	public AfiliadoCreate setEmail(String email) {
   		this.email = email;
   		return this;
   	}
   	
   	private String CBU;

   	public String getCBU() {
   		return CBU;
   	}

   	public AfiliadoCreate setCBU(String CBU) {
   		this.CBU = CBU;
   		return this;
   	}
   	
    //endregion
    

	//region > simpleObject (output)
    private Afiliado simpleObject;

    /**
     * The created simple object (output).
     * @return
     */
    public Afiliado getSimpleObject() {
        return simpleObject;
    }
    //endregion

    @Override
    protected void execute(final ExecutionContext ec) {

        String name = checkParam("name", ec, String.class);
        String apellido = checkParam("apellido", ec, String.class);
        int dni = checkParam("dni", ec, Integer.class);

        this.simpleObject = wrap(simpleObjectMenu).crear(estado, LP, dni, apellido, name, cuitCuil, direccion, localidad, telefonoFijo, telefonoCelular, email, CBU);

        // also make available to UI
        ec.addResult(this, simpleObject);
    }

    @javax.inject.Inject
    private AfiliadoMenu simpleObjectMenu;

}
