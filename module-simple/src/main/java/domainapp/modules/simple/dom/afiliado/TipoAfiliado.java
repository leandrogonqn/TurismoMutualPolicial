package domainapp.modules.simple.dom.afiliado;

public enum TipoAfiliado {
	Activo("Activo"), Retirado("Retirado");

	private final String nombre;

	public String getNombre() {
		return nombre;
	}

	private TipoAfiliado(String nom) {
		nombre = nom;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
}
