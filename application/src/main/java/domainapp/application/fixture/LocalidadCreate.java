package domainapp.application.fixture;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadMenu;
import domainapp.modules.simple.dom.provincia.Provincia;
import domainapp.modules.simple.dom.provincia.ProvinciaRepository;

public class LocalidadCreate extends FixtureScript {

    //region > name (input)
    private String name;
    /**
     * Name of the object (required)
     */
    public String getName() {
        return name;
    }

    public LocalidadCreate setName(final String name) {
        this.name = name;
        return this;
    }
    
    private Provincia provincia;
    //endregion

    public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	//region > simpleObject (output)
    private Localidad simpleObject;

    /**
     * The created simple object (output).
     * @return
     */
    public Localidad getSimpleObject() {
        return simpleObject;
    }
    //endregion

    @Override
    protected void execute(final ExecutionContext ec) {

        String name = checkParam("name", ec, String.class);
        
        this.simpleObject = wrap(simpleObjectMenu).crear(name, provinciasRepository.listarActivos().get(0));

        // also make available to UI
        ec.addResult(this, simpleObject);
    }

    @javax.inject.Inject
    private LocalidadMenu simpleObjectMenu;

    @javax.inject.Inject
    ProvinciaRepository provinciasRepository;

}
