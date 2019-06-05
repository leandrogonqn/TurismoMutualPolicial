package domainapp.modules.simple.dom.afiliado;

import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
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

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Afiliado.class, objectType="simple.AfiliadoMenu")
@DomainServiceLayout(named = "Clientes", menuOrder = "10.1")
public class AfiliadoMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Crear Afiliado")
	@MemberOrder(sequence = "1")
	public Afiliado crear(@ParameterLayout(named = "Activo") final boolean afiliadoActivo,
			@Nullable @ParameterLayout(named = "LP", typicalLength=6) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 6) final String afiliadoLP,
			@ParameterLayout(named = "DNI") @Parameter(maxLength=8) final int afiliadoDni,
			@ParameterLayout(named = "Apellido") final String afiliadoApellido,
			@ParameterLayout(named = "Nombre") final String afiliadoNombre,
			@Nullable @ParameterLayout(named = "Cuit/Cuil") @Parameter(optionality = Optionality.OPTIONAL) final String afiliadoCuitCuil,
			@Nullable @ParameterLayout(named = "Dirección") @Parameter(optionality = Optionality.OPTIONAL) final String afiliadoDireccion,
			@Nullable @ParameterLayout(named = "Localidad") @Parameter(optionality = Optionality.OPTIONAL) final Localidad afiliadoLocalidad,
			@ParameterLayout(named = "Teléfono") final String afiliadoTelefono,
			@Nullable @ParameterLayout(named = "E-Mail") @Parameter(optionality = Optionality.OPTIONAL) final String afiliadoMail,
			@Nullable @ParameterLayout(named = "CBU") @Parameter(optionality = Optionality.OPTIONAL) final String afiliadoCBU) {
		return afiliadoRepository.crearCompleto(afiliadoActivo, afiliadoLP, afiliadoNombre, afiliadoApellido, afiliadoDni, afiliadoCuitCuil, 
				 afiliadoDireccion, afiliadoLocalidad, afiliadoTelefono, afiliadoMail, afiliadoCBU);
	}
	
	public List<Localidad> choices7Crear() {
		return localidadesRepository.listarHabilitados();
	}
	
	//este es el validador para que lp no tenga menos de 6, cbu no tenga menos de 22
	public String validateCrear(final boolean afiliadoActivo, final String afiliadoLP, final int afiliadoDni, final String afiliadoApellido, 
			final String afiliadoNombre, final String afiliadoCuitCuil,	final String afiliadoDireccion, final Localidad afiliadoLocalidad,  
			final String afiliadoTelefono, final String afiliadoMail, final String afiliadoCBU) {
        if (isNumeric(afiliadoLP) == false) {
            return "Todos los caracteres del LP deben ser numericos";
        }
        
		if (afiliadoLP.length()<=5) {
			return "LP debe contener 6 digitos";
		}
		
		if (Integer.toString(afiliadoDni).length()<6)
			return "Largo del dni incorrecto";
		
		if (afiliadoMail != null){
			// Patrón para validar el email
	        Pattern pattern = Pattern
	                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); 
	        
	        // El email a validar
	        String email = afiliadoMail;
	 
	        Matcher mather = pattern.matcher(email);
	 
	        if (mather.find() == false) {
	            return "El mail ingresado es invalido";
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
	AfiliadoRepository afiliadoRepository;

	@javax.inject.Inject
	LocalidadRepository localidadesRepository;

	@Inject
	MessageService messageService;
	
}
