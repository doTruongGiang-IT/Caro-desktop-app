package com.sgu.caro.socket_connection;

import com.sgu.caro.entity.Group;
import com.sgu.caro.entity.User;
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
        
        if (posX != null){
            for (int i=0; i<5; i++){
                List<Integer> pos = new ArrayList<>();
                pos.add(posX.get(i));
                pos.add(posY.get(i));
                JSONArray posData = new JSONArray(pos);
                posMatch.add(posData);
            }
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
    public String exportDataSendInvitation(int userID, String displayName, int score){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "send_invitation");
        data.put("user", userID);
        data.put("display_name", displayName);
        data.put("score", score);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     * 	# data format
	{
            "type": "send_message",
            "data": {
                "user": 1101, #id người dùng
                "usernam": "asdjhsakd", # ten user
                "message": "this is a message" # nội dung tin nhắn
            }
	}
     * @param userID
     * @param message
     * @return 
     */
    public String exportDataSendMessage(int userID, String username, String message){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "send_message");
        data.put("user", userID);
        data.put("username", username);
        data.put("message", message);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     * 	# data format
	{
            "type": "start_match",
            "data": {
                "is_started": true
            }
	}
     * @param is_started
     * @param stepType
     * @return 
     */
    public String exportDataStartMatch(boolean is_started, String stepType){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "start_match");
        data.put("is_started", is_started);
        data.put("step_type", stepType);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     * 	# data format
	{
            "type": "get_group",
            "data": {
                "groups": [
                    {
                        user_1: 1001,
                        user_2: 1002,
                        number_of_watchers: 10,
                    },
                    {
                        user_1: 1001,
                        user_2: 1002,
                        number_of_watchers: 10,
                    }
                ]
            }
	}
     * @param groups
     * @return 
     */
    public String exportDataGetGroup(ArrayList<Group> groups){
        JSONObject jo = new JSONObject();      
        JSONObject data = new JSONObject();      
        List<JSONObject> grouplist = new ArrayList<>();
        
        jo.put("type", "get_group");
        for (Group group : groups){  
            if (group.isAccept_pairing_1() && group.isAccept_pairing_2()){
                JSONObject element = new JSONObject();
                element.put("user_1", group.getUser_1());
                element.put("user_2", group.getUser_2());
                if (group.getDataUser1() == null){
                    element.put("username_1", "");
                }
                else{
                    element.put("username_1", group.getDataUser1().getName());
                }
                if (group.getDataUser2() == null){
                    element.put("username_2", "");
                }
                else{
                    element.put("username_2", group.getDataUser2().getName());
                }
                element.put("number_of_watchers", group.getWatchers().size());
                grouplist.add(element);
            }
        }
        
        JSONArray groupdata = new JSONArray(grouplist);
        data.put("groups", groupdata);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     * 	# data format
	{
            "type": "get_user",
            "data": {
                "users": [
                    {
                       "id": 1100,
                       "name": "name1",
                       "score" 1200
                    },
                    {
                       "id": 1200,
                       "name": "name2",
                       "score" 1200
                   }
                ]
            }
	}
     * @param users
     * @return 
     */
    public String exportDataGetUser(ArrayList<User> users){
        JSONObject jo = new JSONObject();      
        JSONObject data = new JSONObject();
        List<JSONObject> userlist = new ArrayList<>();
        
        jo.put("type", "get_user");
        for (User user : users){      
            JSONObject element = new JSONObject();
            element.put("id", user.getId());
            element.put("name", user.getName());
            element.put("score", user.getScore());
            userlist.add(element);
        }
        
        JSONArray userdata = new JSONArray(userlist);
        data.put("users", userdata);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     * 	# data format
        {
            "type": "accept_watch",
            "data": {
                "accept": true,
                "user_1": 1231,
                "username_1": "1231",
                "score_1": "123",
                "user_2": 12312,
                "username_2": "123",
                "score_2": 123,
                "who_x": 1,
                "matrix": [[]]
            }
	}
     * @param users
     * @return 
     */
    public String exportDataAcceptWatch(boolean accept, int user_1, String username_1, int score_1, int user_2, String username_2, int score_2, int who_x, int[][] matrix){
        JSONObject jo = new JSONObject();      
        JSONObject data = new JSONObject();
        List<JSONObject> userlist = new ArrayList<>();
        
        jo.put("type", "accept_watch");
        JSONArray userdata = new JSONArray(userlist);
        data.put("accept", accept);
        data.put("user_1", user_1);
        data.put("username_1", username_1);
        data.put("score_1", score_1);
        data.put("user_2", user_2);
        data.put("username_2", username_2);
        data.put("score_2", score_2);
        data.put("who_x", who_x);
        
        List<JSONArray> posMatch = new ArrayList<>();
        
        for (int i=0; i<20; i++){
            List<Integer> pos = new ArrayList<>();
            for (int j=0; j<20; j++){
                pos.add(matrix[i][j]);
            }
            JSONArray posData = new JSONArray(pos);
            posMatch.add(posData);
        }
        JSONArray posDataMatch = new JSONArray(posMatch);;
        data.put("matrix", posDataMatch);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    /**
     * 	# data format
	{
            "type": "get_watcher",
            "data": {
                "watchers": [
                    "name 1",
                    "name 2",
                ]
            }
	}
     * @param groups
     * @return 
     */
    public String exportDataGetWatcher(ArrayList <String> watchers){
        JSONObject jo = new JSONObject();      
        JSONObject data = new JSONObject();      
        
        jo.put("type", "get_watcher");
        
        JSONArray groupdata = new JSONArray(watchers);
        data.put("watchers", groupdata);
        jo.put("data", data);
        return encryptData(jo.toString());
    }
    
    public JSONObject importData(String rawData){
        return new JSONObject(rawData);
    }
    
    public JSONArray importDataList(String rawData){
        return new JSONArray(rawData);
    }
    
    public static void main(String[] args) {
        System.out.println(
            new DataSocket().exportResultMatch(
                1101, 
                new ArrayList<Integer>( Arrays.asList(12, 5, 15, 20)), 
                new ArrayList<Integer>( Arrays.asList(12, 5, 15, 16, 20))
            )
        );
    }
}