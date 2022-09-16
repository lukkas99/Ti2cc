package dao;

import model.Anime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class AnimeDAO extends DAO {	
	public AnimeDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Anime anime) {
		boolean status = false;
		try {
			String sql = "INSERT INTO anime (titulo, dataAtual, nota) "
		               + "VALUES ('" + anime.getTitulo() + 
		               ", " + anime.getNota() + ", ?, ?,);";
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(anime.getDataAtual()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Anime get(int id) {
		Anime produto = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM Animes WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 produto = new Anime(rs.getInt("id"), rs.getString("titulo"),
	        			 			   rs.getTimestamp("dataAtual").toLocalDateTime(),
	                				   rs.getInt("nota"));
	        			              
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produto;
	}
	
	
	public List<Anime> get() {
		return get("");
	}

	
	public List<Anime> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Anime> getOrderByTitulo() {
		return get("titulo");		
	}
	
	
	public List<Anime> getOrderByNota() {
		return get("nota");		
	}
	
	
	private List<Anime> get(String orderBy) {
		List<Anime> animes = new ArrayList<Anime>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM Animes" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Anime p = new Anime(rs.getInt("id"), rs.getString("titulo"),
	        							rs.getTimestamp("dataAtual").toLocalDateTime(),
	        			                rs.getInt("nota"));
	            animes.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return animes;
	}
	
	
	public boolean update(Anime anime) {
		boolean status = false;
		try {
            String sql = "UPDATE Animes SET titulo = ?, dataAtual = ?, nota = ? WHERE id = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, anime.getTitulo());
            pst.setTimestamp(2, Timestamp.valueOf(anime.getDataAtual()));
            pst.setInt(3, anime.getNota());
            pst.setInt(4, anime.getID());
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
		return status;
	}
	
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM produto WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}