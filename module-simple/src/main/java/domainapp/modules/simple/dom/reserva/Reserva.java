package domainapp.modules.simple.dom.reserva;

import java.util.Date;
import java.util.List;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.Where;
import domainapp.modules.simple.dom.voucher.Voucher;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Reserva")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "reservaId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.reserva.Reserva " + "WHERE reservaActivo == :reservaActivo ")})
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
	
	@Element(column="reservaId")
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
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Activo", hidden=Where.ALL_TABLES)
	private boolean reservaActivo;

	public boolean getReservaActivo() {
		return reservaActivo;
	}

	public void setReservaActivo(boolean reservaActivo) {
		this.reservaActivo = reservaActivo;
	}
}
