package com.Model;

import static com.Model.Initializer.inConn1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Regression
{
	
    Statement stmt;
	static Initializer in=new Initializer();
	public void regression(int Id) throws SQLException, ClassNotFoundException
	{
		        ResultSet rs = null;
		        String SQL;
			    PreparedStatement pstmt;
			    Double PolarityRatio,Hype,Actor,Actress,Genre,Sequel,Error;
		        int HolidayEffect;															//take coeefficients
			
			     in.connection_open();
			
				 SQL = "select * from regression";						
				 pstmt=inConn1.prepareStatement(SQL);
				 rs = pstmt.executeQuery();
				 rs.next();
				 PolarityRatio = rs.getDouble("PolarityRatio");
				 Hype = rs.getDouble("Hype");
				 Actor = rs.getDouble("Actor");
				 Actress = rs.getDouble("Actress");
				 HolidayEffect = rs.getInt("HolidayEffect");
				 Genre = rs.getDouble("Genre");
				 Sequel = rs.getDouble("Sequel");
				 Error = rs.getDouble("Error");
			
				 Double PolarityValue,HypeValue,ActorValue,ActressValue,HolidayEffectValue,GenreValue,SequelValue;
				 Double Revenue;
			
			
				 SQL = "select * from movie where MOVIE_ID= ?";							//take values
				 pstmt=inConn1.prepareStatement(SQL);
				 pstmt.setInt(1,Id);
				 rs = pstmt.executeQuery();
				 rs.next();
				 PolarityValue = rs.getDouble("PolarityRatio");
				 HypeValue = rs.getDouble("Hype");
				 ActorValue= rs.getDouble("Actor");
				 ActressValue = rs.getDouble("Actress");
				 HolidayEffectValue = rs.getDouble("Holiday");
				 GenreValue = rs.getDouble("Genre");
				 SequelValue = rs.getDouble("Sequel");
			
			
			Double p,hy,a1,a2,h,g,s;														//calculate
			p=PolarityRatio*PolarityValue;
			hy=Hype*HypeValue;
			a1=Actor*ActorValue;
			a2=Actress*ActressValue;
			h=HolidayEffect*HolidayEffectValue;
			g=Genre*GenreValue;
			s=Sequel*SequelValue;
			Revenue=p+hy+a1+a2+h+g+s+Error;
			
			
			SQL="Update movie set PolarityRatioPart=?,HypePart=?,ActorPart=?,ActressPart=?,HolidayPart=?,GenrePart=?,SequelPart=?, Revenue=? where MOVIE_ID=?";
			pstmt=inConn1.prepareStatement(SQL);
			pstmt.setDouble(1,p);
			pstmt.setDouble(2,hy);
			pstmt.setDouble(3,a1);
			pstmt.setDouble(4,a2);
			pstmt.setDouble(5,h);
			pstmt.setDouble(6,g);
			pstmt.setDouble(7,s);
			pstmt.setDouble(8,Revenue);
			pstmt.setInt(9,Id);
			pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "Revenue successfully updated. Thankyou!");
			
			in.connection_close();
			
			}
	

}

