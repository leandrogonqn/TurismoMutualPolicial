/*******************************************************************************
 * Copyright 2017 SiGeSe
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package domainapp.modules.simple.dom.voucher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import domainapp.modules.simple.dom.producto.Producto;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "simple", table = "Voucher")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "voucherId")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listarActivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.voucher.Voucher " + "WHERE voucherActivo == true "),
		@javax.jdo.annotations.Query(name = "listarInactivos", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.modules.simple.dom.voucher.Voucher " + "WHERE voucherActivo == false ") })
@DomainObject(publishing = Publishing.ENABLED, auditing = Auditing.ENABLED)
public class Voucher implements Comparable<Voucher> {
	// region > title
	public TranslatableString title() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return TranslatableString.tr("{name}", "name", getVoucherProducto().toString() + ". Fecha de entrada: "+sdf.format(getVoucherFechaEntrada())+
				". Fecha de salida: "+sdf.format(getVoucherFechaSalida()));
	}
	// endregion

	public String cssClass() {
		return (getVoucherActivo() == true) ? "activo" : "inactivo";
	}

	public static final int NAME_LENGTH = 200;

	// Constructor
	public Voucher(final Producto voucherProducto, final Date voucherFechaEntrada, final Date voucherFechaSalida, final int voucherCantidadNochesfinal,
			int voucherCantidadPasajeros, Double voucherPrecioTotal) {
		setVoucherProducto(voucherProducto);
		setVoucherFechaEntrada(voucherFechaEntrada);
		setVoucherFechaSalida(voucherFechaSalida);
		setVoucherCantidadNoches(voucherCantidadNochesfinal);
		setVoucherCantidadPasajeros(voucherCantidadPasajeros);
		setVoucherPrecioTotal(voucherPrecioTotal);
		this.voucherActivo = true;
	}

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Producto")
	private Producto voucherProducto;

	public Producto getVoucherProducto() {
		return voucherProducto;
	}

	public void setVoucherProducto(Producto voucherProducto) {
		this.voucherProducto = voucherProducto;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Fecha Entrada")
	private Date voucherFechaEntrada;

	public Date getVoucherFechaEntrada() {
		return voucherFechaEntrada;
	}

	public void setVoucherFechaEntrada(Date voucherFechaEntrada) {
		this.voucherFechaEntrada = voucherFechaEntrada;
	}	

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Fecha Salida")
	private Date voucherFechaSalida;

	public Date getVoucherFechaSalida() {
		return voucherFechaSalida;
	}

	public void setVoucherFechaSalida(Date voucherFechaSalida) {
		this.voucherFechaSalida = voucherFechaSalida;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Cantidad de noches")
	private int voucherCantidadNoches;

	public int getVoucherCantidadNoches() {
		return voucherCantidadNoches;
	}

	public void setVoucherCantidadNoches(int voucherCantidadNoches) {
		this.voucherCantidadNoches = voucherCantidadNoches;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Cantidad de Pasajeros")
	private int voucherCantidadPasajeros;

	public int getVoucherCantidadPasajeros() {
		return voucherCantidadPasajeros;
	}

	public void setVoucherCantidadPasajeros(int voucherCantidadPasajeros) {
		this.voucherCantidadPasajeros = voucherCantidadPasajeros;
	}	
	
	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Precio Total")
	private Double voucherPrecioTotal;

	public Double getVoucherPrecioTotal() {
		return voucherPrecioTotal;
	}

	public void setVoucherPrecioTotal(Double voucherPrecioTotal) {
		this.voucherPrecioTotal = voucherPrecioTotal;
	}	

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Activo", hidden=Where.ALL_TABLES)
	private boolean voucherActivo;

	public boolean getVoucherActivo() {
		return voucherActivo;
	}

	public void setVoucherActivo(boolean voucherActivo) {
		this.voucherActivo = voucherActivo;
	}

	// endregion

	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void borrarVoucher() {
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		setVoucherActivo(false);
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherFechaEntrada")
	public Voucher actualizarFechaEntrada(@ParameterLayout(named = "Fecha de Entrada") final Date voucherFechaEntrada) {
		setVoucherFechaEntrada(voucherFechaEntrada);
		return this;
	}

	public Date default0ActualizarFechaEntrada() {
		return getVoucherFechaEntrada();
	}
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherFechaSalida")
	public Voucher actualizarFechaSalida(@ParameterLayout(named = "Fecha de Salida") final Date voucherFechaSalida) {
		setVoucherFechaSalida(voucherFechaSalida);
		return this;
	}

	public Date default0ActualizarFechaSalida() {
		return getVoucherFechaSalida();
	} 
	
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherCantidadPasajeros")
	public Voucher actualizarCantidadPasajeros(@ParameterLayout(named = "CantidadPasajeros") final int voucherCantidadPasajeros) {
		setVoucherCantidadPasajeros(voucherCantidadPasajeros);
		return this;
	}

	public int default0ActualizarCantidadPasajeros() {
		return getVoucherCantidadPasajeros();
	}


	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "voucherActivo")
	public Voucher actualizarActivo(@ParameterLayout(named = "Activo") final boolean voucherActivo) {
		setVoucherActivo(voucherActivo);
		return this;
	}

	public boolean default0ActualizarActivo() {
		return getVoucherActivo();
	}

	// endregion

	// region > toString, compareTo
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return getVoucherProducto().toString() + " Entrada: " + sdf.format(getVoucherFechaEntrada()) + " Salida : " + sdf.format(getVoucherFechaSalida());
	}

	@Override
	public int compareTo(final Voucher voucher) {
		return this.voucherFechaEntrada.compareTo(voucher.voucherFechaEntrada);
	}

	// endregion

	// acciones
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar todas las Vouchers")
	@MemberOrder(sequence = "2")
	public List<Voucher> listar() {
		return voucherRepository.listar();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Voucher Activas")
	@MemberOrder(sequence = "3")
	public List<Voucher> listarActivos() {
		return voucherRepository.listarActivos();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(named = "Listar Vouchers Inactivas")
	@MemberOrder(sequence = "4")
	public List<Voucher> listarInactivos() {
		return voucherRepository.listarInactivos();
	}

	// region > injected dependencies

	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

	@Inject
	VoucherRepository voucherRepository;

	// endregion
}
