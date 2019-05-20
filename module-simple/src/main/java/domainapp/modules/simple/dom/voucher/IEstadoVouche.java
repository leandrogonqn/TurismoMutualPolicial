package domainapp.modules.simple.dom.voucher;


public interface IEstadoVouche {

	public void autorizar(Voucher voucher);
	public void confirmar (Voucher voucher);
	public void desautorizar(Voucher voucher);
	public void anular(Voucher voucher);
}
