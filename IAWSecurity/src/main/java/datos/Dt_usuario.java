package datos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import entidades.Tbl_user;

public class Dt_usuario {

	poolConexion pc = poolConexion.getInstance();
	Connection c = null;
	private ResultSet rsUsuario = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;
	
	public void llenarRsUsuario(Connection c)
	{
		try
		{
			ps = c.prepareStatement("SELECT * FROM seguridad.tbl_user", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
			rsUsuario = ps.executeQuery();
		}catch(Exception e)
		{
			System.out.println("Datos: ERROR EN LISTAR USUARIOS" + e.getMessage());
			e.printStackTrace();
			
		}
	}
	
	public ArrayList<Tbl_user> listarUserActivos()
	{
		ArrayList<Tbl_user> listUser = new ArrayList<Tbl_user>();
		try
		{
			c = poolConexion.getConnection();
			ps = c.prepareStatement("SELECT * tbl_user WHERE estado <> 3", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			while(rs.next()) 
			{
				Tbl_user user = new Tbl_user();
				user.setId_user(rs.getInt("idUser"));
				user.setUser(rs.getString("user"));
				user.setPwd(rs.getString("pwd"));
				user.setNombres(rs.getString("nombres"));
				user.setApellidos(rs.getString("apellidos"));
				user.setEmail(rs.getString("email"));
				user.setPwd_temp(rs.getString("!pwd_temp"));
				user.setEstado(rs.getInt("estado"));
				
				listUser.add(user);
				
			}
		}
		catch(Exception e)
		{
			System.out.println("DATOS: ERROR EN LISTAR USUARIOS" + e.getMessage());
			e.printStackTrace();
			
		}
		finally
		{
			try
			{
				if(rs != null)
					rs.close();
				if(ps != null)
					ps.close();
				if(c != null)
					poolConexion.closeConnection(c);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
