package domainapp.modules.simple.dom.proveedor;

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

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Proveedor.class)
public class ProveedorRepository {
	private Connection conexion = null;
	private String url = "jdbc:mysql://localhost:3306/afiliacion";
	private String user = "root";
	private String password = "Lean3366";

	public List<Proveedor> listar() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		List<Proveedor> proveedores = new LinkedList<Proveedor>();
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "select * from proveedores";

				PreparedStatement stmt = conexion.prepareStatement(consulta);
				
				ResultSet rs = stmt.executeQuery(); 

				while(rs.next()){
					Proveedor proveedor = new Proveedor();
					proveedor.setProveedorId(rs.getInt(1));
					proveedor.setProveedorRazonSocial(rs.getString(2));
					proveedor.setProveedorCuit(rs.getString(3));
					proveedores.add(proveedor);
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
		return proveedores;
	}

	public List<Proveedor> buscarPorRazonSocial(final String proveedorRazonSocial) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		List<Proveedor> proveedores = new LinkedList<Proveedor>();
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "select * from proveedores WHERE lower(nombre) LIKE '%"+proveedorRazonSocial.toLowerCase()+"%'";

				PreparedStatement stmt = conexion.prepareStatement(consulta);
				
				ResultSet rs = stmt.executeQuery(); 

				while(rs.next()){
					Proveedor proveedor = new Proveedor();
					proveedor.setProveedorId(rs.getInt(1));
					proveedor.setProveedorRazonSocial(rs.getString(2));
					proveedor.setProveedorCuit(rs.getString(3));
					proveedores.add(proveedor);
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
		return proveedores;
	}
	
	public Proveedor buscarPorId(final int proveedorId) {
		Proveedor proveedor = new Proveedor();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			messageService.warnUser(ex.getMessage());
		}
		try {
			conexion = DriverManager.getConnection(url, user, password);
			if(!conexion.isClosed()) {
				String consulta = "select * from proveedores where id = ?";
				PreparedStatement stmt = conexion.prepareStatement(consulta);
				stmt.setInt(1, proveedorId);
				
				ResultSet rs = stmt.executeQuery(); 
				
				if(rs.next()) {
					proveedor.setProveedorId(rs.getInt(1));
					proveedor.setProveedorRazonSocial(rs.getString(2));
					proveedor.setProveedorCuit(rs.getString(3));
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
		return proveedor;
	}

	@javax.inject.Inject
	RepositoryService repositoryService;
	@javax.inject.Inject
	ServiceRegistry2 serviceRegistry;
	@Inject
	MessageService messageService;
}
