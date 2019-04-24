package domainapp.modules.simple.dom.afiliado;

public enum Estado {
	Activo("Activo"), Retirado("Retirado");

	private final String nombre;

	public String getNombre() {
		return nombre;
	}

	private Estado(String nom) {
		nombre = nom;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
}
