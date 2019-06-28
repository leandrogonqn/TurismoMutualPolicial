package domainapp.application.fixture.scenarios;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import domainapp.application.fixture.AfiliadoCreate;
import domainapp.application.fixture.AfiliadoTearDown;
import domainapp.modules.simple.dom.afiliado.Afiliado;
import domainapp.modules.simple.dom.afiliado.TipoAfiliado;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;

import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScript;


public class RecreateAfiliado extends FixtureScript {

	@Override
	protected void execute(ExecutionContext executionContext) {
		// TODO Auto-generated method stub
		
	}
//
//    public final List<String> LP = Collections.unmodifiableList(Arrays.asList(
//            "001234"));
//	
//    public final List<String> NAMES = Collections.unmodifiableList(Arrays.asList(
//            "Juan"));
//    
//    public final List<String> apellidos = Collections.unmodifiableList(Arrays.asList(
//            "Perez"));
//    
//    public final List<String> cuitCuil = Collections.unmodifiableList(Arrays.asList(
//            "20313131313"));
//    
//    public final List<String> documentos = Collections.unmodifiableList(Arrays.asList(
//            "31313131"));
//    
//    public final List<String> direcciones = Collections.unmodifiableList(Arrays.asList(
//    		"Salta 123"));
//    
//    public final List<String> telefono = Collections.unmodifiableList(Arrays.asList(
//    		"2994471351"));  
//    
//    public final List<String> emails = Collections.unmodifiableList(Arrays.asList(
//    		"juan.perez@ejemplo.com"));
//    
//    public final List<String> CBU = Collections.unmodifiableList(Arrays.asList(
//    		"0123456789012345678901"));
//
//    public RecreateAfiliado() {
//        withDiscoverability(Discoverability.DISCOVERABLE);
//    }
//
//    //region > number (optional input)
//    private Integer number;
//
//    /**
//     * The number of objects to create, up to 10; optional, defaults to 3.
//     */
//    public Integer getNumber() {
//        return number;
//    }
//
//    public RecreateAfiliado setNumber(final Integer number) {
//        this.number = number;
//        return this;
//    }
//    //endregion
//
//    //region > simpleObjects (output)
//    private final List<Afiliado> simpleObjects = Lists.newArrayList();
//
//    /**
//     * The simpleobjects created by this fixture (output).
//     */
//    @Programmatic
//    public List<Afiliado> getSimpleObjects() {
//        return simpleObjects;
//    }
//    //endregion
//
//    @Override
//    protected void execute(final ExecutionContext ec) {
//
//        // defaults
//        final int number = defaultParam("number", ec, 1);
//
//        // validate
//        if(number < 0 || number > NAMES.size()) {
//            throw new IllegalArgumentException(String.format("number must be in range [0,%d)", NAMES.size()));
//        }
//
//        //
//        // execute
//        //
//        ec.executeChild(this, new AfiliadoTearDown());
//        
//        for (int i = 0; i < number; i++) {
//            final AfiliadoCreate fs = new AfiliadoCreate();
//            fs.setActivo(true);
//            fs.setLP(LP.get(i));
//            fs.setName(NAMES.get(i));
//            fs.setApellido(apellidos.get(i));
//            fs.setDni(Integer.parseInt(documentos.get(i)));
//            fs.setCuitCuil(cuitCuil.get(i));
//            fs.setDireccion(direcciones.get(i));
//            fs.setLocalidad(localidadesRepository.listarHabilitados().get(0));
//            fs.setTelefono(telefono.get(i));
//            fs.setEmail(emails.get(i));
//            fs.setCBU(CBU.get(i));
//            ec.executeChild(this, fs.getName(), fs);
//            simpleObjects.add(fs.getSimpleObject());
//        }
//    }
//    
//    @javax.inject.Inject
//    LocalidadRepository localidadesRepository;
}
