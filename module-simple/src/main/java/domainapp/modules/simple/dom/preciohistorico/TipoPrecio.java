package domainapp.modules.simple.dom.preciohistorico;

public enum TipoPrecio {
	Afiliado("Afiliado"), No_Afiliado("NoAfiliado");

	private final String nombre;

	public String getNombre() {
		return nombre;
	}

	private TipoPrecio(String nom) {
		nombre = nom;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
}
