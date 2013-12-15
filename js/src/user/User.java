package user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import mx.bigdata.jcalais.CalaisClient;
import mx.bigdata.jcalais.CalaisConfig;
import mx.bigdata.jcalais.CalaisObject;
import mx.bigdata.jcalais.CalaisResponse;
import mx.bigdata.jcalais.rest.CalaisRestClient;
public class User {
public long basic;
public long user_id;
public double tag_total=0;
public Map<String,Double> tag_weight=new HashMap<String,Double>();
public double topic_total=0;
public Map<String,Double> topic_weight=new HashMap<String,Double>();
public double entity_total=0;
public Map<String,Double> entity_weight=new HashMap<String,Double>();
	/**
	 * @param args
	 */
	public User(long basic,long user_id)
	{
		this.basic=basic;
		this.user_id=user_id;
	}
    public void show() throws ClassNotFoundException, SQLException
    {  System.out.println("usr "+this.user_id);
    Class.forName("com.mysql.jdbc.Driver");
    
    
    String url = "jdbc:mysql://localhost:3306/recommand";        
    String username = "root";
    String password = "root";
    Connection con = DriverManager.getConnection(url, username, password);
    Statement sql_statement = con.createStatement();
   
    
    
    	 Iterator it = tag_weight.entrySet().iterator();
    	 System.out.println("tags:");
    	 while(it.hasNext())
    	 {  Map.Entry entry = (Map.Entry) it.next();
    	   Object key = entry.getKey();
    	   Double value = (Double)entry.getValue();
    	   Double d=value/tag_total;
    	   
    	   System.out.println("key=" + key + " value=" + value/tag_total);
    	   String l=null;
    	   try{
    		   
    	   String query = "INSERT INTO result VALUES ("+this.user_id+", 'tag', '"+key.toString().replace('\'', '"')+"', '"+d+"')";            
    	   l=query;  
    	   sql_statement.executeUpdate(query);
    	     
    	   }
    	     catch(Exception e)
    	     {
    	    	 System.out.println("The incorrect line is");
    	    	 System.out.println(l);
    	    	 System.exit(0);
    	     }
    	 }
    	 System.out.println("topic:");
    	 Iterator itt = topic_weight.entrySet().iterator();
    	 while(itt.hasNext())
    	 {  Map.Entry entry = (Map.Entry) itt.next();
    	   Object key = entry.getKey();
    	   Double value = (Double)entry.getValue();
    	   Double d=value/topic_total;
    	   
    	   System.out.println("key=" + key + " value=" + value/topic_total);
       String l=null;
    	   try{
    	   String query = "INSERT INTO result VALUES ("+this.user_id+", 'topic', '"+key.toString().replace('\'', '"')+"', '"+d+"')";            
   	    sql_statement.executeUpdate(query);
   	    l=query;
    	   }catch(Exception e)
    	   {
    		   
    		   System.out.println("The incorrect sql is " );
    		   System.out.println(l);
    		   System.exit(0);
    	   }
    	 }
    	 System.out.println("entities:");
    	 Iterator ittt = topic_weight.entrySet().iterator();
    	 while(ittt.hasNext())
    	 {  Map.Entry entry = (Map.Entry) ittt.next();
    	   Object key = entry.getKey();
    	   Double value = (Double)entry.getValue();
    	   Double d=value/entity_total;
    	  
    	   System.out.println("key=" + key + " value=" + value/entity_total);
    	   String query = "INSERT INTO result VALUES ("+this.user_id+", 'entity', '"+key.toString()+"', '"+d+"')";            
   	     sql_statement.executeUpdate(query);		
    	 }
    	 sql_statement.close();
         con.close();
    }
    public void add(String s,long id) throws IOException
    {
    	CalaisClient client = new CalaisRestClient("8qk7hqenee8uyhdn36cq8a7d");
       
		CalaisConfig config = new CalaisConfig();
		config.set(CalaisConfig.UserParam.EXTERNAL_ID, "vincentgong7test");
		config.set(CalaisConfig.ProcessingParam.CALCULATE_RELEVANCE_SCORE,
				"true");
		CalaisResponse response = client.analyze(s, config);
		for (CalaisObject tags : response.getSocialTags()) {
			String n= tags.getField("name");
			if(tag_weight.containsKey(n))
			{
				Double d=tag_weight.get(n);
				d+=(1-1/(id-basic))*1;
				tag_total+=(1-1/(id-basic))*1;
				tag_weight.remove(n);
				tag_weight.put(n, d);
			}
			else
			{
				tag_weight.put(n,(double)(1-1/(id-basic)) );
				tag_total+=(1-1/(id-basic));
			}
		}
		for (CalaisObject topic : response.getTopics()) {
			//System.out.println(topic.getField("categoryName"));
			String n= topic.getField("categoryName");
			if(topic_weight.containsKey(n))
			{
				Double d=topic_weight.get(n);
				d+=(1-1/(id-basic))*1;
				topic_total+=(1-1/(id-basic))*1;
				topic_weight.remove(n);
				topic_weight.put(n, d);
			}
			else
			{
				topic_weight.put(n,(double)(1-1/(id-basic)) );
				topic_total+=(1-1/(id-basic));
			}
		}
		
		for (CalaisObject entity : response.getEntities()) {
			
			/*System.out.println(entity.getField("_type") + ":"
					+ entity.getField("name") + ":"
					+ entity.getField("relevance"));*/
		      String type=entity.getField("_type");
			if(entity_weight.containsKey(type))
			{
				Double d=entity_weight.get(type);
				d+=(1-1/(id-basic))*1*Double.valueOf(entity.getField("relevance"));
				entity_total+=(1-1/(id-basic))*1*Double.valueOf(entity.getField("relevance"));
				entity_weight.remove(type);
				entity_weight.put(type, d);
			}
			else
			{     double value=Double.valueOf(entity.getField("relevance"));
				entity_weight.put(type,(double)(1-1/(id-basic)*Double.valueOf(entity.getField("relevance"))) );
				entity_total+=(1-1/(id-basic)*value);
			}
		
		}

    }
 public   String toString()
    {
    	return "haha";
    }
}
