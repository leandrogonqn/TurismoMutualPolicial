package domainapp.modules.simple.dom.reportes;

import java.util.Date;

import domainapp.modules.simple.dom.afiliado.Afiliado;
import domainapp.modules.simple.dom.producto.Producto;

public class VoucherAfiliadoReporte {
	
	private int reservaCodigo;

	public int getReservaCodigo() {
		return reservaCodigo;
	}

	public void setReservaCodigo(int reservaCodigo) {
		this.reservaCodigo = reservaCodigo;
	}

	private Date reservaFecha;

	public Date getReservaFecha() {
		return reservaFecha;
	}

	public void setReservaFecha(Date reservaFecha) {
		this.reservaFecha = reservaFecha;
	}
	
	private Afiliado reservaCliente;

	public Afiliado getReservaCliente() {
		return reservaCliente;
	}

	public void setReservaCliente(Afiliado reservaCliente) {
		this.reservaCliente = reservaCliente;
	}
	
	private int personaJuridicaDni;

	public int getPersonaJuridicaDni() {
		return personaJuridicaDni;
	}

	public void setPersonaJuridicaDni(int personaJuridicaDni) {
		this.personaJuridicaDni = personaJuridicaDni;
	}
	
	private String personaTelefono;

	public String getPersonaTelefono() {
		return personaTelefono;
	}
	public void setPersonaTelefono(String personaTelefono) {
		this.personaTelefono = personaTelefono;
	}	
		
	private String personaMail;
	
	public String getPersonaMail() {
		return personaMail;
	}
	public void setPersonaMail(String personaMail) {
		this.personaMail = personaMail;
	}	
	
	private Producto voucherProducto;

	public Producto getVoucherProducto() {
		return voucherProducto;
	}

	public void setVoucherProducto(Producto voucherProducto) {
		this.voucherProducto = voucherProducto;
	}	
	
	private Date voucherFechaEntrada;

	public Date getVoucherFechaEntrada() {
		return voucherFechaEntrada;
	}

	public void setVoucherFechaEntrada(Date voucherFechaEntrada) {
		this.voucherFechaEntrada = voucherFechaEntrada;
	}	

	private Date voucherFechaSalida;

	public Date getVoucherFechaSalida() {
		return voucherFechaSalida;
	}

	public void setVoucherFechaSalida(Date voucherFechaSalida) {
		this.voucherFechaSalida = voucherFechaSalida;
	}	
	
	private int voucherCantidadNoches;

	public int getVoucherCantidadNoches() {
		return voucherCantidadNoches;
	}

	public void setVoucherCantidadNoches(int voucherCantidadNoches) {
		this.voucherCantidadNoches = voucherCantidadNoches;
	}	
	
	private int voucherCantidadPasajeros;

	public int getVoucherCantidadPasajeros() {
		return voucherCantidadPasajeros;
	}

	public void setVoucherCantidadPasajeros(int voucherCantidadPasajeros) {
		this.voucherCantidadPasajeros = voucherCantidadPasajeros;
	}	
	
	private Double voucherPrecioTotal;

	public Double getVoucherPrecioTotal() {
		return voucherPrecioTotal;
	}

	public void setVoucherPrecioTotal(Double voucherPrecioTotal) {
		this.voucherPrecioTotal = voucherPrecioTotal;
	}	
	
	private String voucherObservaciones;
	
	public String getVoucherObservaciones() {
		return voucherObservaciones;
	}
	
	public void setVoucherObservaciones(String voucherObservaciones) {
		this.voucherObservaciones=voucherObservaciones;
	}
	
	private String politicasTexto;

	public String getPoliticasTexto() {
		return politicasTexto;
	}

	public void setPoliticasTexto(String politicasTexto) {
		this.politicasTexto = politicasTexto;
	}
	
}
