package domainapp.application.fixture.scenarios;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import com.google.common.collect.Lists;

import domainapp.application.fixture.ProvinciaCreate;
import domainapp.application.fixture.ProvinciaTearDown;
import domainapp.modules.simple.dom.provincia.Provincia;

public class RecreateProvincia extends FixtureScript{
	
	public final List<String> Names = Collections.unmodifiableList(Arrays.asList(
            "Neuquén", "Rio Negro", "Córdoba", "Buenos Aires", "Santa Fe", "Mendoza", "San Juan", "Santa Cruz", "Chubut", "Tierra del Fuego"));

    public RecreateProvincia() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }
    
  //region > number (optional input)
    private Integer number;

    /**
     * The number of objects to create, up to 10; optional, defaults to 3.
     */
    public Integer getNumber() {
        return number;
    }

    public RecreateProvincia setNumber(final Integer number) {
        this.number = number;
        return this;
    }
    //endregion

    //region > simpleObjects (output)
    private final List<Provincia> simpleObjects = Lists.newArrayList();

    /**
     * The simpleobjects created by this fixture (output).
     */
    @Programmatic
    public List<Provincia> getSimpleObjects() {
        return simpleObjects;
    }
    //endregion

	
	@Override
	protected void execute(final ExecutionContext ec) {
		// TODO Auto-generated method stub
	    // defaults
        final int number = defaultParam("number", ec, 10);

        // validate
        if(number < 0 || number > Names.size()) {
            throw new IllegalArgumentException(String.format("number must be in range [0,%d)", Names.size()));
        }

        //
        // execute
        //
        ec.executeChild(this, new ProvinciaTearDown());

        for (int i = 0; i < number; i++) {
            final ProvinciaCreate fs = new ProvinciaCreate().setName(Names.get(i));
            ec.executeChild(this, fs.getName(), fs);
            simpleObjects.add(fs.getSimpleObject());
        }
	}

}
