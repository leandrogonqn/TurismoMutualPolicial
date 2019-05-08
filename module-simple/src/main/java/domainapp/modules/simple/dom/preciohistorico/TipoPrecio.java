package domainapp.modules.simple.dom.preciohistorico;

public enum TipoPrecio {
	Afiliado("Afiliado"), NoAfiliado("No afiliado");

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
