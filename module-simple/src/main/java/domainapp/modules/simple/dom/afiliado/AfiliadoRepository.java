package domainapp.modules.simple.dom.afiliado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Afiliado.class)
public class AfiliadoRepository {
	private Connection conexion = null;
	private String url = "jdbc:mysql://localhost:3306/afiliacion";
	private String user = "root";
	private String password = "Lean3366";
	
	public List<Afiliado> listar() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		List<Afiliado> afiliados = new LinkedList<Afiliado>();
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "SELECT * FROM afiliacion.titulares_turismo";

				PreparedStatement stmt = conexion.prepareStatement(consulta);
				
				ResultSet rs = stmt.executeQuery(); 

				while(rs.next()){
					Afiliado afiliado = new Afiliado();
					afiliado.setAfiliadoId(rs.getInt(1));
					afiliado.setAfiliadoActivo(rs.getBoolean(2));
					afiliado.setAfiliadoCBU(rs.getString(3));
					afiliado.setAfiliadoLP(rs.getString(4));
					afiliado.setPersonaCuitCuil(rs.getString(5));
					afiliado.setPersonaDireccion(rs.getString(6));
					afiliado.setPersonaJuridicaApellido(rs.getString(7));
					afiliado.setPersonaJuridicaDni(rs.getInt(8));
					afiliado.setPersonaJuridicaNombre(rs.getString(9));
					afiliado.setPersonaLocalidadId(rs.getInt(10));
					afiliado.setPersonaMail(rs.getString(11));
					afiliado.setPersonaTelefono(rs.getString(12));	
					afiliados.add(afiliado);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conexion != null) {
					conexion.close();
				}
			}catch(SQLException ex) {
				messageService.warnUser(ex.getMessage());
			}
		}
		return afiliados;
	}
	
	public Afiliado buscarPorId(final int afiliadoId) {
		Afiliado afiliado = new Afiliado();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "SELECT * FROM afiliacion.titulares_turismo where id = ?";
				PreparedStatement stmt = conexion.prepareStatement(consulta);
				stmt.setInt(1, afiliadoId);
				
				ResultSet rs = stmt.executeQuery(); 
				
				if(rs.next()) {
					afiliado.setAfiliadoId(rs.getInt(1));
					afiliado.setAfiliadoActivo(rs.getBoolean(2));
					afiliado.setAfiliadoCBU(rs.getString(3));
					afiliado.setAfiliadoLP(rs.getString(4));
					afiliado.setPersonaCuitCuil(rs.getString(5));
					afiliado.setPersonaDireccion(rs.getString(6));
					afiliado.setPersonaJuridicaApellido(rs.getString(7));
					afiliado.setPersonaJuridicaDni(rs.getInt(8));
					afiliado.setPersonaJuridicaNombre(rs.getString(9));
					afiliado.setPersonaLocalidadId(rs.getInt(10));
					afiliado.setPersonaMail(rs.getString(11));
					afiliado.setPersonaTelefono(rs.getString(12));
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conexion != null) {
					conexion.close();
				}
			}catch(SQLException ex) {
				messageService.warnUser(ex.getMessage());
			}
		}
		return afiliado;
	}
	

	public List<Afiliado> buscarPorNombre(final String personaJuridicaNombre) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		List<Afiliado> afiliados = new LinkedList<Afiliado>();
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "SELECT * FROM afiliacion.titulares_turismo WHERE lower(nombre) LIKE '%"+personaJuridicaNombre.toLowerCase()+"%'";

				PreparedStatement stmt = conexion.prepareStatement(consulta);
				
				ResultSet rs = stmt.executeQuery(); 

				while(rs.next()){
					Afiliado afiliado = new Afiliado();
					afiliado.setAfiliadoId(rs.getInt(1));
					afiliado.setAfiliadoActivo(rs.getBoolean(2));
					afiliado.setAfiliadoCBU(rs.getString(3));
					afiliado.setAfiliadoLP(rs.getString(4));
					afiliado.setPersonaCuitCuil(rs.getString(5));
					afiliado.setPersonaDireccion(rs.getString(6));
					afiliado.setPersonaJuridicaApellido(rs.getString(7));
					afiliado.setPersonaJuridicaDni(rs.getInt(8));
					afiliado.setPersonaJuridicaNombre(rs.getString(9));
					afiliado.setPersonaLocalidadId(rs.getInt(10));
					afiliado.setPersonaMail(rs.getString(11));
					afiliado.setPersonaTelefono(rs.getString(12));
					afiliados.add(afiliado);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conexion != null) {
					conexion.close();
				}
			}catch(SQLException ex) {
				messageService.warnUser(ex.getMessage());
			}
		}
		return afiliados;
	}
	
	public List<Afiliado> buscarPorDNI(final int personaJuridicaDni) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		List<Afiliado> afiliados = new LinkedList<Afiliado>();
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "SELECT * FROM afiliacion.titulares_turismo WHERE numero_documento = ?";

				PreparedStatement stmt = conexion.prepareStatement(consulta);
				stmt.setInt(1, personaJuridicaDni);
				
				ResultSet rs = stmt.executeQuery(); 

				while(rs.next()){
					Afiliado afiliado = new Afiliado();
					afiliado.setAfiliadoId(rs.getInt(1));
					afiliado.setAfiliadoActivo(rs.getBoolean(2));
					afiliado.setAfiliadoCBU(rs.getString(3));
					afiliado.setAfiliadoLP(rs.getString(4));
					afiliado.setPersonaCuitCuil(rs.getString(5));
					afiliado.setPersonaDireccion(rs.getString(6));
					afiliado.setPersonaJuridicaApellido(rs.getString(7));
					afiliado.setPersonaJuridicaDni(rs.getInt(8));
					afiliado.setPersonaJuridicaNombre(rs.getString(9));
					afiliado.setPersonaLocalidadId(rs.getInt(10));
					afiliado.setPersonaMail(rs.getString(11));
					afiliado.setPersonaTelefono(rs.getString(12));
					afiliados.add(afiliado);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conexion != null) {
					conexion.close();
				}
			}catch(SQLException ex) {
				messageService.warnUser(ex.getMessage());
			}
		}
		return afiliados;
	}
	
	public List<Afiliado> buscarPorLP(final String afiliadoLP) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		List<Afiliado> afiliados = new LinkedList<Afiliado>();
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "SELECT * FROM afiliacion.titulares_turismo WHERE lower(legajo) LIKE '%"+afiliadoLP+"%'";

				PreparedStatement stmt = conexion.prepareStatement(consulta);
				
				ResultSet rs = stmt.executeQuery(); 

				while(rs.next()){
					Afiliado afiliado = new Afiliado();
					afiliado.setAfiliadoId(rs.getInt(1));
					afiliado.setAfiliadoActivo(rs.getBoolean(2));
					afiliado.setAfiliadoCBU(rs.getString(3));
					afiliado.setAfiliadoLP(rs.getString(4));
					afiliado.setPersonaCuitCuil(rs.getString(5));
					afiliado.setPersonaDireccion(rs.getString(6));
					afiliado.setPersonaJuridicaApellido(rs.getString(7));
					afiliado.setPersonaJuridicaDni(rs.getInt(8));
					afiliado.setPersonaJuridicaNombre(rs.getString(9));
					afiliado.setPersonaLocalidadId(rs.getInt(10));
					afiliado.setPersonaMail(rs.getString(11));
					afiliado.setPersonaTelefono(rs.getString(12));
					afiliados.add(afiliado);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conexion != null) {
					conexion.close();
				}
			}catch(SQLException ex) {
				messageService.warnUser(ex.getMessage());
			}
		}
		return afiliados;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	MessageService messageService;

}
