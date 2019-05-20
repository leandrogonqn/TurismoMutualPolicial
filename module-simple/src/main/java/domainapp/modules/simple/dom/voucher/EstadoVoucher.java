package domainapp.modules.simple.dom.voucher;

import javax.inject.Inject;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import domainapp.modules.simple.dom.voucher.Voucher;
import domainapp.modules.simple.dom.voucher.VoucherRepository;

public enum EstadoVoucher implements IEstadoVouche{
	
	presupuestado("presupuestado"){
		public void confirmar (Voucher voucher) {
			if(voucher.getVoucherProducto().getProductoAutorizacion()==true) {
				voucher.setVoucherEstado(prereserva);
			} else {
				voucher.setVoucherEstado(reservado);
			}
		}
	},
	reservado("reservado"){
	},
	prereserva("prereserva"){

		@Override
		public void desautorizar(Voucher voucher) {
			// TODO Auto-generated method stub
			voucher.setVoucherEstado(no_autorizado);
		}

		@Override
		public void autorizar(Voucher voucher) {
			// TODO Auto-generated method stub
			voucher.setVoucherEstado(reservado);
		}
		
	},
	no_autorizado("no autorizado"){
	},
	anulado("anulado"){
		@Override
		public void anular(Voucher voucher) {
			// TODO Auto-generated method stub
		}
		
	};
	
	@Override
	public void anular(Voucher voucher) {
		voucher.setVoucherEstado(anulado);
	}
	
	@Override
	public void autorizar(Voucher voucher) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void desautorizar(Voucher voucher) {
	}
	
	@Override
	public void confirmar(Voucher voucher) {
	}
	
	private final String nombre;

	public String getNombre() {
		return nombre;
	}

	private EstadoVoucher(String nom) {
		nombre = nom;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
	
	@Inject
	MessageService messageService;
	
	@Inject
	VoucherRepository voucherRepository;
	
	@javax.inject.Inject
	RepositoryService repositoryService;

}
