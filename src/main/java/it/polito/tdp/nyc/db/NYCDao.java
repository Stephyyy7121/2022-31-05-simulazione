package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.nyc.model.City;
import it.polito.tdp.nyc.model.Hotspot;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	
	//metood che permette di estrarre dal DB tutti provider --> da inserire nella tendina 
	
	public List<String> getAllProvider() {
		
		String sql = "SELECT DISTINCT Provider "
				+ "FROM nyc_wifi_hotspot_locations ";
		
		List<String> result = new ArrayList<String>();
		
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("Provider"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	
	//metodo per estrarre i quartieri che hanno un determianto provider
	
	public List<City> getAllQuartieri(String provider) {
		
		String sql = "SELECT Distinct city, AVG(Latitude) as Lat, AVG(Longitude) AS Lng "
				+ "FROM nyc_wifi_hotspot_locations "
				+ "WHERE provider= ? "
				+ "GROUP BY city "
				+ "ORDER BY city";
		
		List<City> result = new ArrayList<City>();
		
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, provider);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				result.add(new City(rs.getString("City"), new LatLng(rs.getDouble("Lat"), rs.getDouble("Lng"))));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return result;
		
	}
	
}
