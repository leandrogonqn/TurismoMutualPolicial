package domainapp.modules.simple.dom.cliente;

public enum Estado {
	Activo("Activo"), Retirado("Retirado"), No_Socio("No Socio");

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
