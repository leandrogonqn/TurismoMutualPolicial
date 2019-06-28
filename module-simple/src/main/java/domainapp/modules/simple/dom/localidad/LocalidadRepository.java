package domainapp.modules.simple.dom.localidad;

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

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Localidad.class)
public class LocalidadRepository {
	private Connection conexion = null;
	private String url = "jdbc:mysql://localhost:3306/afiliacion";
	private String user = "root";
	private String password = "Lean3366";

	public List<Localidad> listar() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		List<Localidad> localidades = new LinkedList<Localidad>();
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "select * from localidades";

				PreparedStatement stmt = conexion.prepareStatement(consulta);
				
				ResultSet rs = stmt.executeQuery(); 

				while(rs.next()){
					Localidad localidad = new Localidad();
					localidad.setLocalidadId(rs.getInt(1));
					localidad.setLocalidadProvinciaId(rs.getInt(2));
					localidad.setLocalidadesNombre(rs.getString(3));
					localidades.add(localidad);
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
		return localidades;
	}

	public List<Localidad> buscarPorNombre(final String localidadNombre) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		List<Localidad> localidades = new LinkedList<Localidad>();
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "select * from localidades WHERE lower(nombre) LIKE '%"+localidadNombre.toLowerCase()+"%'";

				PreparedStatement stmt = conexion.prepareStatement(consulta);
				
				ResultSet rs = stmt.executeQuery(); 

				while(rs.next()){
					Localidad localidad = new Localidad();
					localidad.setLocalidadId(rs.getInt(1));
					localidad.setLocalidadProvinciaId(rs.getInt(2));
					localidad.setLocalidadesNombre(rs.getString(3));
					localidades.add(localidad);
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
		return localidades;
	}
	
	public Localidad buscarPorId(final int localidadId) {
		Localidad localidad = new Localidad();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "select * from localidades where id = ?";
				PreparedStatement stmt = conexion.prepareStatement(consulta);
				stmt.setInt(1, localidadId);
				
				ResultSet rs = stmt.executeQuery(); 
				
				if(rs.next()) {
					localidad.setLocalidadId(rs.getInt(1));
					localidad.setLocalidadProvinciaId(rs.getInt(2));
					localidad.setLocalidadesNombre(rs.getString(3));
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
		return localidad;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	MessageService messageService;
}
