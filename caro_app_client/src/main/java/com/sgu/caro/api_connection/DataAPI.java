
package com.sgu.caro.api_connection;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class DataAPI {
    private static AESEncryption encryption = new AESEncryption();
    
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
        return jo.toString();
    }
    
    /**
     * 	# data format
	{
            "encrypted_username": sad321eas, # email người dùng được mã hóa
            "aes_key": asd12edsa # AES key
	}
     * @param username
     * @param password
     * @return String
     */
    public String exportAESKeyAPI(String username, String aes_key){
        JSONObject jo = new JSONObject();      
        String encrypted_username = encryption.RSAEcryptAPI(username);
        
        jo.put("encrypted_username", encrypted_username);
        jo.put("aes_key", aes_key);
        return jo.toString();
    }
    
    /**
     * 	# data format
	{
            "data": "askjh132edh31", # request được mã hóa
	}
     * @param encrytedData
     * @return String
     */
    public String exportAPIData(String encrytedData){
        JSONObject jo = new JSONObject();      
        
        jo.put("data", encrytedData);
        return jo.toString();
        
    }
    
    public JSONObject importData(String rawData){
        return new JSONObject(rawData);
    }
    
    public static void main(String[] args) {}
}
