package mx.uaemex.fi.ico.linc21.consumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.enterprise.context.ApplicationScoped;
import mx.uaemex.fi.ico.linc21.entities.Cliente;
import static mx.uaemex.fi.ico.linc21.databaseconnectivity.ConnectionParameters.*;

@ApplicationScoped
public class ConsumerService {
	
	public static final String SUCCESS = "Todo salio bien";
	public static final String BAD_LOGIN = "El usuario y/o la contraseña son incorrectos";
	public static final String NOT_FOUND = "Cliente no encontrado";
	
	private String status;

	public void login(Cliente cliente) {
		
		status = SUCCESS;
		String contraseña = "";
		
		try(Connection conn = DriverManager.getConnection(CONNECTION_URI,CONNECTION_PROPERTIES);
			PreparedStatement stm = conn.prepareStatement("SELECT contraseña from AQC.CLIENTE WHERE usuario = ?")){
			
				stm.setString(1, cliente.usuario);
				stm.execute();
				ResultSet rs = stm.getResultSet();
				
				while(rs.next()) {contraseña = rs.getString("contraseña");}
		
			
		} catch (SQLException e) {
			status = e.getMessage();
		}
		
		if(!contraseña.equals(cliente.contraseña)){
			status = BAD_LOGIN;
		}
		
	}
	
	public void registerNewClient(Cliente cliente) {
		
		status = SUCCESS;
		
		try(Connection conn = DriverManager.getConnection(CONNECTION_URI,CONNECTION_PROPERTIES);
			PreparedStatement stm = conn.prepareStatement("INSERT INTO AQC.Cliente (rfc,nombre,apellido,numerodetelefono,correo,fechadenacimiento,genero,direccion_id,usuario,contraseña) VALUES(?,?,?,?,?,?,?,?,?,?)")){
				
			stm.setString(1, cliente.rfc);
			stm.setString(2, cliente.nombre);
			stm.setString(3, cliente.apellido);
			stm.setLong(4, cliente.numerodetelefono);
			stm.setString(5, cliente.correo);
			stm.setDate(6, cliente.fechadenacimiento);
			stm.setString(7, String.valueOf(cliente.genero));
			stm.setInt(8, cliente.direccion_id);
			stm.setString(9, cliente.usuario);
			stm.setString(10, cliente.contraseña);
			
			stm.execute();
							
			} catch (SQLException e) {
				status = e.getMessage();
			}
	}
	
public Cliente buscarClientePorRFC(Cliente clienteb) {
		
		status = NOT_FOUND;
		Cliente cliente = new Cliente();
		
		try(Connection conn = DriverManager.getConnection(CONNECTION_URI,CONNECTION_PROPERTIES);
			PreparedStatement stm = conn.prepareStatement("SELECT * FROM aqc.CLIENTE WHERE rfc = ?")){
				
			stm.setString(1, clienteb.rfc);
			stm.execute();
			ResultSet rs = stm.getResultSet();
			
			while (rs.next()) {
				status = SUCCESS;
				
				cliente.rfc = rs.getString("rfc");

				cliente.nombre = rs.getString("nombre"); 

				cliente.apellido = rs.getString("apellido"); 

				cliente.numerodetelefono = rs.getLong("numerodetelefono");

				cliente.correo = rs.getString("correo"); 

				cliente.fechadenacimiento = rs.getDate("fechadenacimiento"); 

				cliente.genero = rs.getString("genero").charAt(0); 

				cliente.direccion_id = rs.getInt("direccion_id"); 

				cliente.usuario = rs.getString("usuario"); 

				cliente.contraseña = rs.getString("contraseña");	
			
			}
				
			} catch (SQLException e) {
				status = e.getMessage();
			}
		
		return cliente;
	}
	
	public String getStatus() {
		return status;
	}
}
