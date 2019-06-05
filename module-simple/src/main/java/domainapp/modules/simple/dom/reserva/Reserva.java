package domainapp.modules.simple.dom.reserva;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Persistent;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.value.Blob;
import domainapp.modules.simple.dom.reportes.ReporteRepository;
import domainapp.modules.simple.dom.reportes.VoucherAfiliadoReporte;
import domainapp.modules.simple.dom.reportes.VoucherEmpresaReporte;
import domainapp.modules.simple.dom.reportes.VoucherNoAfiliadoReporte;
import domainapp.modules.simple.dom.reservaafiliado.ReservaAfiliado;
import domainapp.modules.simple.dom.reservaempresa.ReservaEmpresa;
import domainapp.modules.simple.dom.reservanoafiliado.ReservaNoAfiliado;
import domainapp.modules.simple.dom.voucher.EstadoVoucher;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;
import net.sf.jasperreports.engine.JRException;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Reserva")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "reservaId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "buscarPorCodigo", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.reserva.Reserva " + "WHERE reservaCodigo == :reservaCodigo"),
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.reserva.Reserva "
				+ "WHERE reservaHabilitado == :reservaHabilitado ") })
@javax.jdo.annotations.Unique(name = "Reserva_reservaCodigo_UNQ", members = { "reservaCodigo" })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
public abstract class Reserva {

	public static final int NAME_LENGTH = 200;
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Codigo")
	private int reservaCodigo;

	public int getReservaCodigo() {
		return reservaCodigo;
	}

	public void setReservaCodigo(int reservaCodigo) {
		this.reservaCodigo = reservaCodigo;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Fecha")
	private Date reservaFecha;

	public Date getReservaFecha() {
		return reservaFecha;
	}

	public void setReservaFecha(Date reservaFecha) {
		this.reservaFecha = reservaFecha;
	}
	
	@Persistent(mappedBy="voucherReserva")
	@Property(editing = Editing.ENABLED)
	@CollectionLayout(named = "Vouchers")
	private List<Voucher> reservaListaVoucher;

	public List<Voucher> getReservaListaVoucher() {
		return reservaListaVoucher;
	}

	public void setReservaListaVoucher(List<Voucher> reservaListaVoucher) {
		this.reservaListaVoucher = reservaListaVoucher;
	}
	
	@ActionLayout(named="Precio Total", hidden=Where.ALL_TABLES)	 
	public Double getPrecioTotal() {
		Double a = 0.0;
		for(int indice = 0;indice<getReservaListaVoucher().size();indice++)
		{
			if((getReservaListaVoucher().get(indice).getVoucherEstado()==EstadoVoucher.reservado)||
					(getReservaListaVoucher().get(indice).getVoucherEstado()==EstadoVoucher.prereserva))
			a = a + getReservaListaVoucher().get(indice).getVoucherPrecioTotal();
		}
		return a;
	}
	
	@Column(allowsNull = "true")
	@Property(editing=Editing.DISABLED)
	@PropertyLayout(named="Memo", hidden=Where.ALL_TABLES)
	private String reservaMemo;
	
	public String getReservaMemo() {
		return reservaMemo;
	}
	
	public void setReservaMemo(String reservaMemo) {
		this.reservaMemo = reservaMemo;
	}
	
	@Action(publishing=Publishing.ENABLED)
	@ActionLayout(named="Confirmar Presupuesto")
	public Reserva confirmarReserva() {
		for (int i = 0; i < getReservaListaVoucher().size(); i++) {
			if(getReservaListaVoucher().get(i).getVoucherProducto().getProductoAlojamientoPropio()==true) {
				if(voucherRepository.corroborarDisponibilidadActualizar(getReservaListaVoucher().get(i).getVoucherProducto(), 
						getReservaListaVoucher().get(i).getVoucherFechaEntrada(), getReservaListaVoucher().get(i).getVoucherFechaSalida(),
						getReservaListaVoucher().get(i))
						==true) {
					getReservaListaVoucher().get(i).getVoucherEstado().confirmar(getReservaListaVoucher().get(i));
				} else {
					messageService.warnUser("NO HAY DISPONIBILIDAD");
				}
			} else {
				getReservaListaVoucher().get(i).getVoucherEstado().confirmar(getReservaListaVoucher().get(i));
			}
			if(getReservaListaVoucher().get(i).getVoucherEstado()==EstadoVoucher.prereserva)
				messageService.warnUser("PRERESERVA PENDIENTE DE AUTORIZACION");
		}
		return this;
	}
	
	@Action(publishing=Publishing.ENABLED)
	@ActionLayout(named="Anular Reserva")
	public Reserva anularReserva() {
		for (int i = 0; i < getReservaListaVoucher().size(); i++) {
			getReservaListaVoucher().get(i).getVoucherEstado().anular(getReservaListaVoucher().get(i));
		}
		return this;
	}
	
    @Action(semantics = SemanticsOf.SAFE, publishing=Publishing.ENABLED)
	public Blob imprimirVoucher(
            @ParameterLayout(named="Voucher: ") final Voucher voucher) throws JRException, IOException {
    	
    	List<Object> objectsReport = new ArrayList<Object>();
    	
    	String jrxml = "";
    	
    	if(voucher.getVoucherReserva().getClass()==ReservaAfiliado.class) {
        	ReservaAfiliado r = (ReservaAfiliado) voucher.getVoucherReserva();
        	
    		VoucherAfiliadoReporte voucherAfiliadoReporte = new VoucherAfiliadoReporte();
    			
    		voucherAfiliadoReporte.setReservaCodigo(voucher.getVoucherReserva().getReservaCodigo());
    		voucherAfiliadoReporte.setReservaFecha(voucher.getVoucherReserva().getReservaFecha());
    		voucherAfiliadoReporte.setReservaCliente(r.getReservaCliente());
    		voucherAfiliadoReporte.setPersonaJuridicaDni(r.getReservaCliente().getPersonaJuridicaDni());
    		voucherAfiliadoReporte.setPersonaTelefono(r.getReservaCliente().getPersonaTelefono());
    		voucherAfiliadoReporte.setPersonaMail(r.getReservaCliente().getPersonaMail());
    		voucherAfiliadoReporte.setVoucherProducto(voucher.getVoucherProducto());
    		voucherAfiliadoReporte.setVoucherFechaEntrada(voucher.getVoucherFechaEntrada());
    		voucherAfiliadoReporte.setVoucherFechaSalida(voucher.getVoucherFechaSalida());
    		voucherAfiliadoReporte.setVoucherCantidadNoches(voucher.getVoucherCantidadNoches());
    		voucherAfiliadoReporte.setVoucherCantidadPasajeros(voucher.getVoucherCantidadPasajeros());
    		voucherAfiliadoReporte.setVoucherPrecioTotal(voucher.getVoucherPrecioTotal());
    		voucherAfiliadoReporte.setVoucherObservaciones(voucher.getVoucherObservaciones());
    		if(voucher.getVoucherProducto().getProductoPoliticas()==null) {
    			voucherAfiliadoReporte.setPoliticasTexto(" ");
    		}else {
    			voucherAfiliadoReporte.setPoliticasTexto(voucher.getVoucherProducto().getProductoPoliticas().getPoliticasTexto());
    		}
    		objectsReport.add(voucherAfiliadoReporte);
    		jrxml = "VoucherAfiliado.jrxml";
    	}
    	
    	if(voucher.getVoucherReserva().getClass()==ReservaNoAfiliado.class) {
        	ReservaNoAfiliado r = (ReservaNoAfiliado) voucher.getVoucherReserva();
        	
    		VoucherNoAfiliadoReporte voucherNoAfiliadoReporte = new VoucherNoAfiliadoReporte();
    			
    		voucherNoAfiliadoReporte.setReservaCodigo(voucher.getVoucherReserva().getReservaCodigo());
    		voucherNoAfiliadoReporte.setReservaFecha(voucher.getVoucherReserva().getReservaFecha());
    		voucherNoAfiliadoReporte.setReservaCliente(r.getReservaCliente());
    		voucherNoAfiliadoReporte.setPersonaJuridicaDni(r.getReservaCliente().getPersonaJuridicaDni());
    		voucherNoAfiliadoReporte.setPersonaTelefono(r.getReservaCliente().getPersonaTelefono());
    		voucherNoAfiliadoReporte.setPersonaMail(r.getReservaCliente().getPersonaMail());
    		voucherNoAfiliadoReporte.setVoucherProducto(voucher.getVoucherProducto());
    		voucherNoAfiliadoReporte.setVoucherFechaEntrada(voucher.getVoucherFechaEntrada());
    		voucherNoAfiliadoReporte.setVoucherFechaSalida(voucher.getVoucherFechaSalida());
    		voucherNoAfiliadoReporte.setVoucherCantidadNoches(voucher.getVoucherCantidadNoches());
    		voucherNoAfiliadoReporte.setVoucherCantidadPasajeros(voucher.getVoucherCantidadPasajeros());
    		voucherNoAfiliadoReporte.setVoucherPrecioTotal(voucher.getVoucherPrecioTotal());
    		voucherNoAfiliadoReporte.setVoucherObservaciones(voucher.getVoucherObservaciones());
    		if(voucher.getVoucherProducto().getProductoPoliticas()==null) {
    			voucherNoAfiliadoReporte.setPoliticasTexto(" ");
    		}else {
    			voucherNoAfiliadoReporte.setPoliticasTexto(voucher.getVoucherProducto().getProductoPoliticas().getPoliticasTexto());
    		}
    		objectsReport.add(voucherNoAfiliadoReporte);
    		jrxml = "VoucherNoAfiliado.jrxml";
    	}

    	if(voucher.getVoucherReserva().getClass()==ReservaEmpresa.class) {
        	ReservaEmpresa r = (ReservaEmpresa) voucher.getVoucherReserva();
        	
    		VoucherEmpresaReporte voucherEmpresaReporte = new VoucherEmpresaReporte();
    			
    		voucherEmpresaReporte.setReservaCodigo(voucher.getVoucherReserva().getReservaCodigo());
    		voucherEmpresaReporte.setReservaFecha(voucher.getVoucherReserva().getReservaFecha());
    		voucherEmpresaReporte.setReservaCliente(r.getReservaCliente());
    		voucherEmpresaReporte.setPersonaCuitCuil(r.getReservaCliente().getPersonaCuitCuil());
    		voucherEmpresaReporte.setPersonaTelefono(r.getReservaCliente().getPersonaTelefono());
    		voucherEmpresaReporte.setPersonaMail(r.getReservaCliente().getPersonaMail());
    		voucherEmpresaReporte.setVoucherProducto(voucher.getVoucherProducto());
    		voucherEmpresaReporte.setVoucherFechaEntrada(voucher.getVoucherFechaEntrada());
    		voucherEmpresaReporte.setVoucherFechaSalida(voucher.getVoucherFechaSalida());
    		voucherEmpresaReporte.setVoucherCantidadNoches(voucher.getVoucherCantidadNoches());
    		voucherEmpresaReporte.setVoucherCantidadPasajeros(voucher.getVoucherCantidadPasajeros());
    		voucherEmpresaReporte.setVoucherPrecioTotal(voucher.getVoucherPrecioTotal());
    		voucherEmpresaReporte.setVoucherObservaciones(voucher.getVoucherObservaciones());
    		if(voucher.getVoucherProducto().getProductoPoliticas()==null) {
    			voucherEmpresaReporte.setPoliticasTexto(" ");
    		}else {
    			voucherEmpresaReporte.setPoliticasTexto(voucher.getVoucherProducto().getProductoPoliticas().getPoliticasTexto());
    		}
    		objectsReport.add(voucherEmpresaReporte);
    		jrxml = "VoucherEmpresa.jrxml";
    	}
		
		//String nombreArchivo = "VoucherAfiliado_"+this.getVoucherReserva().getReservaCodigo()+"_"+new SimpleDateFormat("dd/MM/yyyy").format(this.getVoucherFechaEntrada());
		String nombreArchivo = "Voucher_"+voucher.getVoucherReserva().getReservaCodigo()+"_"+new SimpleDateFormat("dd/MM/yyyy").format(voucher.getVoucherFechaEntrada());
		return reporteRepository.imprimirReporteIndividual(objectsReport,jrxml, nombreArchivo);
    }
    
	public String validateImprimirVoucher(Voucher voucher) {
		if (voucher.getVoucherEstado()!=EstadoVoucher.reservado)
			return "NO SE PUEDE IMPRIMIR EL VOUCHER PORQUE SU ESTADO NO ES RESERVADO";
		return "";
	}
	
	public List<Voucher> choices0ImprimirVoucher(){
		return this.getReservaListaVoucher();
	}
	
	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	MessageService messageService;
	
	@Inject
	ReporteRepository reporteRepository;
}
