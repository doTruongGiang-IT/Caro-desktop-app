package com.sgu.caro.socket_connection;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class DataSocket {
    public DataSocket() {}
    
    public String encryptData(String rawData){
        return rawData;
    }
    
    /**
     * 	# data format
	{
            "type": "result_match",
            "data": {
                "user": 1101, #id người thắng
                "res_pos": [[12, 14], [12, 15], [12, 16], [12, 17], [12, 18]] # các tọa độ của bước thắng
            }
	}
     * @param userID
     * @param posX
     * @param posY
     * @return String
     */
    public String exportResultMatch(int userID, ArrayList<Integer> posX, ArrayList<Integer> posY){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        List<JSONArray> posMatch = new ArrayList<>();
        
        jo.put("type", "result_match");
        data.put("user", userID);
        for (int i=0; i<5; i++){
            List<Integer> pos = new ArrayList<>();
            pos.add(posX.get(i));
            pos.add(posY.get(i));
            JSONArray posData = new JSONArray(pos);
            posMatch.add(posData);
        }
        JSONArray posDataMatch = new JSONArray(posMatch);;
        data.put("res_pos", posDataMatch);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     *  # data format
	{
            "type": "send_invitation",
            "data": {
                "user": 1101, #id người dùng hệ thống muốn ghép cặp
            }
	}
     * @param userID
     * @return 
     */
    public String exportDataSendInvitation(int userID){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "accept_pariring");
        data.put("user", userID);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    
    public JSONObject importData(String rawData){
        return new JSONObject(rawData);
    }
    
    public static void main(String[] args) {
        System.out.println(
            new DataSocket().exportResultMatch(
                1101, 
                new ArrayList<Integer>( Arrays.asList(12, 5, 15, 20)), 
                new ArrayList<Integer>( Arrays.asList(12, 5, 15, 16, 20))
            )
        );
        System.out.println(new DataSocket().exportDataSendInvitation(1101));
    }
}