package com.sgu.caro.api_connection;

import org.json.JSONObject;

public class DataAPI {
    public DataAPI() {}
    
    public String encryptData(String rawData){
        return rawData;
    }
    
    /**
     * 	# data format
	{
            "user_1": 3,
            "user_2": 2,
            "result": 3,
            "result_type": 1,
            "start_date": "2021/12/12 20:16:00",
            "end_date": "2021/12/12 20:46:00"
        }
     * @param username
     * @param password
     * @return String
     */
    public String exportMatchAPI(int user_1, int user_2, int result, int result_type, String start_date, String end_date){
        JSONObject jo = new JSONObject();      
        
        jo.put("user_1", user_1);
        jo.put("user_2", user_2);
        jo.put("result", result);
        jo.put("result_type", result_type);
        jo.put("start_date", start_date);
        jo.put("end_date", end_date);
        return encryptData(jo.toString());
    }
    
    public JSONObject importData(String rawData){
        return new JSONObject(rawData);
    }
    
    public static void main(String[] args) {}
}
