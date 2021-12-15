
package com.sgu.caro.api_connection;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class DataAPI {
    public DataAPI() {}
    
    public String encryptData(String rawData){
        return rawData;
    }
    
    /**
     * 	# data format
	{
            "username": email@email.com, # email người dùng
            "password": mypassword # mật khẩu người dùng
	}
     * @param username
     * @param password
     * @return String
     */
    public String exportLoginAPI(String username, String password){
        JSONObject jo = new JSONObject();      
        
        jo.put("username", username);
        jo.put("password", password);
        return encryptData(jo.toString());
    }
    
    public JSONObject importData(String rawData){
        return new JSONObject(rawData);
    }
//    
//    public Map<String, String> convertListBeforeJava8(List<Object> list) {
//        Map<String, String> map = new HashMap<>();
//        for (Object user : list) {
//            map.put(user., animal);
//        }
//        return map;
//    }
    
    public static void main(String[] args) {}
}
