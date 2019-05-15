package domainapp.modules.simple.dom.voucher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.simple.dom.afiliado.Afiliado;
import domainapp.modules.simple.dom.afiliado.AfiliadoRepository;
import domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliado;
import domainapp.modules.simple.dom.clientenoafiliado.ClienteNoAfiliadoRepository;
import domainapp.modules.simple.dom.empresa.Empresa;
import domainapp.modules.simple.dom.empresa.EmpresaRepository;
import domainapp.modules.simple.dom.localidad.Localidad;
import domainapp.modules.simple.dom.localidad.LocalidadRepository;
import domainapp.modules.simple.dom.preciohistorico.TipoPrecio;
import domainapp.modules.simple.dom.producto.Producto;
import domainapp.modules.simple.dom.producto.ProductoRepository;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Voucher.class, objectType="simple.VoucherMenu")
@DomainServiceLayout(named = "Reserva", menuOrder = "70")
public class VoucherMenu {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Vouchers Por Localidad")
	@MemberOrder(sequence = "3")
	public List<Voucher> listarVoucherPorLocalidad(@ParameterLayout(named="Localidad") final Localidad productoLocalidad,
			@ParameterLayout(named="Desde") final Date fechaDesde,
			@ParameterLayout(named="Hasta") final Date fechaHasta){
		List<Producto> listaProducto = productoRepository.buscarProductoPorLocalidad(productoLocalidad);
		List<Voucher> listaVoucher = new ArrayList<>();
		for(int indice = 0;indice<listaProducto.size();indice++) {
			listaVoucher.addAll(voucherRepository.listarVoucherPorProducto(listaProducto.get(indice), true, fechaDesde, fechaHasta));
		}
		return listaVoucher;
	}
	
	public List<Localidad> choices0ListarVoucherPorLocalidad(){
		return localidadRepository.listarActivos();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Listar Vouchers Por Productos")
	@MemberOrder(sequence = "3")
	public List<Voucher> listarVoucherPorProducto(@ParameterLayout(named="Producto") final Producto voucherProducto,
			@ParameterLayout(named="Desde") final Date fechaDesde,
			@ParameterLayout(named="Hasta") final Date fechaHasta){
		return voucherRepository.listarVoucherPorProducto(voucherProducto, true, fechaDesde, fechaHasta);
	}
	
	public List<Producto> choices0ListarVoucherPorProducto(){
		return productoRepository.listarActivos();
	}
	
	@Inject
	VoucherRepository voucherRepository;
	
	@Inject
	ProductoRepository productoRepository;

	@Inject
	LocalidadRepository localidadRepository;
	
}
