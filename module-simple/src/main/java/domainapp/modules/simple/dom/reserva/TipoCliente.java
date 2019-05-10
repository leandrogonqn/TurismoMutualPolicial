package domainapp.modules.simple.dom.reserva;

public enum TipoCliente {
	Afiliado("Afiliado"), No_Afiliado("NoAfiliado"), Empresa("Empresa");

	private final String nombre;

	public String getNombre() {
		return nombre;
	}

	private TipoCliente(String nom) {
		nombre = nom;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
}
