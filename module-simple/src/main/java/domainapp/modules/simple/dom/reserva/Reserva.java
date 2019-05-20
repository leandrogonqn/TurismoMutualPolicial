package domainapp.modules.simple.dom.reserva;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Element;
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
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.message.MessageService;

import domainapp.modules.simple.dom.reservaafiliado.ReservaAfiliado;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Reserva")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "reservaId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarHabilitados", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.reserva.Reserva " + "WHERE reservaHabilitado == :reservaHabilitado ")})
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
	public Double getReservaPrecioTotal() {
		Double a = 0.0;
		for(int indice = 0;indice<getReservaListaVoucher().size();indice++)
		{
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
	
	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	MessageService messageService;
}
