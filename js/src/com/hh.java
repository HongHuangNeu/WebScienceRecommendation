package com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.jayway.jsonpath.JsonPath;
import user.User;;
public class hh {
  public static ArrayList<User> user_list=new ArrayList<User>();
	/**
	 * @param args
	 */
  public static boolean isMember(long d)
  {
	  for(User u:user_list)
	  {
		  if(u.user_id==d)
			  return true;
	  }
	  return false;
  }
  public static User getUser(long d)
  {
	  for(User u:user_list)
	  {
		  if(u.user_id==d)
			  return u;
	  }
	  return null;  
  }
  public static void delUser(long d)
  {
	  for(User u:user_list)
	  {
		  if(u.user_id==d)
			  user_list.remove(u);
	  }
	  
  }
	public static void main(String[] args)throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		int count=0;
		int num=0;
		
		File fr = new File("D:\\OutputWeb\\testData.jason");
		File fw1 = new File("D:\\OutputWeb\\w1.txt");
		File fw2 = new File("D:\\OutputWeb\\w2.txt");
		if(!fw1.exists()){
			fw1.createNewFile();
		}
		if(!fw2.exists()){
			fw2.createNewFile();
		}
int record=0;
		Scanner sc = new Scanner(fr);
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(fw1, true));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(fw2, true));
       ArrayList<String> users=new ArrayList<String>();
		
		//String s="hh";
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
		  num++;
		  record++;
		 
		  System.out.println("No "+num);
		try{
			String text=JsonPath.read(line,"$.text");
			String tid=JsonPath.read(line,"$.id_str");
			String j=JsonPath.read(line,"$.user.id_str");
			long uid=Long.valueOf(j);
			long twid=Long.valueOf(tid);
			if(!hh.isMember(uid))
			{User u=new User(twid-3,uid);
			u.add(text,twid);
				hh.user_list.add(u);
			}else{
			 User u=hh.getUser(uid);
			 u.add(text, twid);
		//System.out.println(u);
			}
						
		    //created_at
			
		}catch(Exception e){
			if(!line.endsWith("}"))continue;
			System.out.println("The line is ");
			System.out.println(line);
			int l=line.length();
			String result=line.substring("StatusJSONImpl".length(),l);
			System.out.println("The result is");
			System.out.println(result);
			count++;
			//String j=JsonPath.read(result,"$.text");
			//System.out.println("The result result is\n"+j);
		}
			
		}
		sc.close();
		bw1.close();
		bw2.close();
		System.out.println(count);
		System.out.println(users.size());
		for(User u:user_list)
		{
			u.show();
			
		}
		
	        
			}

}
