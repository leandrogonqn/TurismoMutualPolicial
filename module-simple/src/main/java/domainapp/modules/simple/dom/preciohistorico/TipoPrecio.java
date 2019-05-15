package domainapp.modules.simple.dom.preciohistorico;

public enum TipoPrecio {
	Activo("Activo"), Retirado("Retirado"), No_Afiliado("NoAfiliado");

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
