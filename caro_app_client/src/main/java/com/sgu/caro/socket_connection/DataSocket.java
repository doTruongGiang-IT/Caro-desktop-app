package com.sgu.caro.socket_connection;

import com.sgu.caro.api_connection.AESEncryption;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONException;

public class DataSocket {
    AESEncryption encryption = new AESEncryption();
    
    public DataSocket() {}
    
    public String encryptData(String rawData){
        return encryption.encrypt(rawData);
    }
    
    public String decryptData(String encryptedData){
        return encryption.decrypt(encryptedData);
    }
    
    /**
     * 	# data format
	{
            "type": "go_step",
            "data": {
                "user": 1101, #id người dùng
                "pos": [20, 12] # tọa độ của bước đi
            }
	}
     * @param userID
     * @param posX
     * @param posY
     * @return String
     */
    public String exportDataGoStep(int userID, int posX, int posY){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        List<Integer> pos = new ArrayList<>();
        
        jo.put("type", "go_step");
        data.put("user", userID);
        pos.add(posX);
        pos.add(posY);
        JSONArray posData = new JSONArray(pos);
        data.put("pos", posData);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     * 	# data format
	{
            "type": "send_message",
            "data": {
                "user": 1101, #id người dùng
                "message": "this is a message" # nội dung tin nhắn
            }
	}
     * @param userID
     * @param message
     * @return 
     */
    public String exportDataSendMessage(int userID, String message){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "send_message");
        data.put("user", userID);
        data.put("message", message);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     *  # data format
	{
            "type": "accept_pariring",
            "data": {
                "user": 1101, #id người dùng
                "is_accepted": true # đồng ý ghép cặp hay không
            }
	}
     * @param userID
     * @param isAccepted
     * @return 
     */
    public String exportDataAcceptPairing(int userID, boolean isAccepted){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "accept_pariring");
        data.put("user", userID);
        data.put("is_accepted", isAccepted);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     *  # data format
	{
            "type": "go_match",
            "data": {
                "user": 1101, #id người dùng
            }
	}
     * @param userID
     * @return 
     */
    public String exportDataGoMatch(int userID){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "go_match");
        data.put("user", userID);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     *  # data format
	{
            "type": "end_match",
            "data": {
                "user_1": 1101, #id người dùng 1
                "user_2": 1101, #id người dùng 2 
            }
	}
     * @param userID1
     * @param userID2
     * @return 
     */
    public String exportDataEndMatch(int userID1, int userID2) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();

        jo.put("type", "end_match");
        data.put("user_1", userID1);
        data.put("user_2", userID2);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     *  # data format
	{
            "type": "out_match",
            "data": {
                "user": 1101, #id người dùng 1
            }
	}
     * @param userID1
     * @return 
     */
    public String exportDataOutMatch(int userID1) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();

        jo.put("type", "out_match");
        data.put("user", userID1);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     *  # data format
	{
            "type": "public_key",
            "data": "",
	}
     * @param userID1
     * @param userID2
     * @return 
     */
    public String exportDataPublicKey() {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();

        jo.put("type", "public_key");
        jo.put("data", "");
        return jo.toString();
    }
    
    /**
     *  # data format
	{
            "type": "aes_key",
            "data": {
                "aes_key": "129321sdh91821"
            },
	}
     * @param userID1
     * @param userID2
     * @return 
     */
    public String exportDataAESKey(String AESKey) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();

        jo.put("type", "aes_key");
        data.put("aes_key", AESKey);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    public JSONObject importData(String rawData){
        try {
            return new JSONObject(decryptData(rawData));
        }
        catch (JSONException e){
            return new JSONObject(decryptData(rawData));
        }
    }
    
    public static void main(String[] args) {
        System.out.println(new DataSocket().exportDataGoStep(1101, 20, 12));
        System.out.println(new DataSocket().exportDataSendMessage(1101, "this is a message asd"));
        System.out.println(new DataSocket().exportDataAcceptPairing(1101, true));
    }
}
