package com.sgu.caro.cli_tool;

import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.socket_connection.SocketConnection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class CLITool {

    public static APIConnection apiConnection = new APIConnection();
    public static Scanner input = new Scanner(System.in);

    public void run() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                main();
            }
        });
        thread.start();
    }

    public void main() {
        String banner = "\n\n"
                + "== MENU =================================\n"
                + "| [1] Thống kê tổng số user             |\n"
                + "| [2] Block user                        |\n"
                + "| [3] Unblock user                      |\n"
                + "| [4] Xếp hạng user theo điểm           |\n"
                + "| [5] Xếp hạng user theo tỉ lệ thắng    |\n"
                + "| [6] Xếp hạng user theo chuỗi thắng    |\n"
                + "| [7] Danh sách các trận dài nhất       |\n"
                + "| [8] Danh sách các trận ngắn nhất      |\n"
                + "=======-=================================\n\n";

        while (true) {
            System.out.println(banner);
            System.out.print("Option: ");
            String raw_option = input.nextLine();
            int option = -1;
            try {
                option = Integer.parseInt(raw_option);
            } catch (NumberFormatException e) {
                System.err.println("Invalid option");
                continue;
            }

            switch (option) {
                case 1:
                    run_option_1();
                    break;
                case 2:
                    run_option_2();
                    break;
                case 3:
                    run_option_3();
                    break;
                case 4:
                    run_option_4();
                    break;
                case 5:
                    run_option_5();
                    break;
                case 6:
                    run_option_6();
                    break;
                case 7:
                    run_option_7();
                    break;
                case 8:
                    run_option_8();
                    break;
                default:
                    System.err.println("Invalid option");
                    break;
            }
        }
    }

    public void run_option_1() {
        JSONArray response = apiConnection.callGetListAPI(apiConnection.getUserListAPIURL);

        int number_of_user = response.length();
        int number_of_online_users = SocketConnection.socketClients.size();
        TableTool st = new TableTool();
        st.setShowVerticalLines(true);

        System.out.println("\n\n");
        System.out.println("============= [OPTION 1] =============");
        System.out.println("Total: " + number_of_user + "\t" + "|" + "\t Online users: " + number_of_online_users);
        System.out.println("============= User List ==============");

        st.setHeaders("#", "id", "username", "name", "score", "is_active", "is_online");
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int id = user.getInt("id");
            String username = user.getString("username");
            String name = user.getString("lastName") + user.getString("firstName");
            int score = user.getInt("score");
            boolean is_active = user.getBoolean("active");
            boolean is_online = SocketConnection.socketClients.containsKey(Integer.toString(id));

            String is_active_string;
            String is_online_string;
            if (is_active) {
                is_active_string = "[+]";
            } else {
                is_active_string = "[ ]";
            }
            if (is_online) {
                is_online_string = "[+]";
            } else {
                is_online_string = "[ ]";
            }

            st.addRow(Integer.toString(i + 1), Integer.toString(id), username, name, Integer.toString(score), is_active_string, is_online_string);
        }

        st.print();
        System.err.println("\n\n");
    }

    public void run_option_2() {
        JSONArray response = apiConnection.callGetListAPI(apiConnection.getUserListAPIURL);

        int number_of_user = 0;
        int t = 0;
        TableTool st = new TableTool();
        st.setShowVerticalLines(true);
        Set<Integer> ids = new HashSet<Integer>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int id = user.getInt("id");
            boolean is_active = user.getBoolean("active");
            ids.add(id);
            if (is_active) {
                number_of_user++;
            }
        }

        System.out.println("\n\n");
        System.out.println("============= [OPTION 2] =============");
        System.out.println("Total: " + number_of_user);
        System.out.println("============= User List ==============");

        st.setHeaders("#", "id", "username", "name", "score", "is_active");
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int id = user.getInt("id");
            String username = user.getString("username");
            String name = user.getString("lastName") + user.getString("firstName");
            int score = user.getInt("score");
            boolean is_active = user.getBoolean("active");

            if (is_active) {
                t++;
                String is_active_string;
                if (is_active) {
                    is_active_string = "[+]";
                } else {
                    is_active_string = "[ ]";
                }

                st.addRow(Integer.toString(t), Integer.toString(id), username, name, Integer.toString(score), is_active_string);
            }
        }

        st.print();
        System.out.println("");

        System.out.println("User id: ");
        String userIDString = input.nextLine();
        int userID = -1;
        try {
            userID = Integer.parseInt(userIDString);
        } catch (NumberFormatException e) {
            System.err.println("Invalid option");
        }

        if (ids.contains(userID)) {
            apiConnection.callPatchAPI(apiConnection.patchBlockUserAPIURL + Integer.toString(userID));
            System.out.println("Block user successfully");
        } else {
            System.err.println("Invalid user id");
        }

        System.err.println("\n\n");
    }
    
    public void run_option_3() {
        JSONArray response = apiConnection.callGetListAPI(apiConnection.getUserListAPIURL);

        int number_of_user = 0;
        int t = 0;
        TableTool st = new TableTool();
        st.setShowVerticalLines(true);
        Set<Integer> ids = new HashSet<Integer>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int id = user.getInt("id");
            boolean is_active = user.getBoolean("active");
            ids.add(id);
            if (!is_active) {
                number_of_user++;
            }
        }

        System.out.println("\n\n");
        System.out.println("============= [OPTION 3] =============");
        System.out.println("Total: " + number_of_user);
        System.out.println("============= User List ==============");

        st.setHeaders("#", "id", "username", "name", "score", "is_active");
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int id = user.getInt("id");
            String username = user.getString("username");
            String name = user.getString("lastName") + user.getString("firstName");
            int score = user.getInt("score");
            boolean is_active = user.getBoolean("active");

            if (!is_active) {
                t++;
                String is_active_string;
                if (is_active) {
                    is_active_string = "[+]";
                } else {
                    is_active_string = "[ ]";
                }

                st.addRow(Integer.toString(t), Integer.toString(id), username, name, Integer.toString(score), is_active_string);
            }
        }

        st.print();
        System.out.println("");

        System.out.println("User id: ");
        String userIDString = input.nextLine();
        int userID = -1;
        try {
            userID = Integer.parseInt(userIDString);
        } catch (NumberFormatException e) {
            System.err.println("Invalid option");
        }

        if (ids.contains(userID)) {
            apiConnection.callPatchAPI(apiConnection.patchUnblockUserAPIURL + Integer.toString(userID));
            System.out.println("Unblock user successfully");
        } else {
            System.err.println("Invalid user id");
        }

        System.err.println("\n\n");
    }
    
    public void run_option_4() {
        JSONArray response = apiConnection.callGetListAPI(apiConnection.getRatingScoreAPIURL);

        int number_of_user = response.length();
        TableTool st = new TableTool();
        st.setShowVerticalLines(true);

        System.out.println("\n\n");
        System.out.println("============= [OPTION 4] =============");
        System.out.println("Total: " + number_of_user);
        System.out.println("============= Ranking List by Score ==============");

        st.setHeaders("#", "id", "username", "name", "score", "win_rate", "win_length");
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int id = user.getInt("id");
            String username = user.getString("username");
            String name = user.getString("name");
            int score = user.getInt("score");
            double win_rate = user.getDouble("win_rate");
            int win_length = user.getInt("win_length");

            st.addRow(Integer.toString(i + 1), Integer.toString(id), username, name, Integer.toString(score), Double.toString(win_rate), Integer.toString(win_length));
        }

        st.print();
        System.err.println("\n\n");
    }

    public void run_option_5() {
        JSONArray response = apiConnection.callGetListAPI(apiConnection.getRatingWinRateAPIURL);

        int number_of_user = response.length();
        TableTool st = new TableTool();
        st.setShowVerticalLines(true);

        System.out.println("\n\n");
        System.out.println("============= [OPTION 5] =============");
        System.out.println("Total: " + number_of_user);
        System.out.println("============= Ranking List by win rate ==============");

        st.setHeaders("#", "id", "username", "name", "score", "win_rate", "win_length");
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int id = user.getInt("id");
            String username = user.getString("username");
            String name = user.getString("name");
            int score = user.getInt("score");
            double win_rate = user.getDouble("win_rate");
            int win_length = user.getInt("win_length");

            st.addRow(Integer.toString(i + 1), Integer.toString(id), username, name, Integer.toString(score), Double.toString(win_rate), Integer.toString(win_length));
        }

        st.print();
        System.err.println("\n\n");
    }

    public void run_option_6() {
        JSONArray response = apiConnection.callGetListAPI(apiConnection.getRatingWinLengthAPIURL);

        int number_of_user = response.length();
        TableTool st = new TableTool();
        st.setShowVerticalLines(true);

        System.out.println("\n\n");
        System.out.println("============= [OPTION 6] =============");
        System.out.println("Total: " + number_of_user);
        System.out.println("============= Ranking List by win length ==============");

        st.setHeaders("#", "id", "username", "name", "score", "win_rate", "win_length");
        for (int i = 0; i < response.length(); i++) {
            JSONObject user = response.getJSONObject(i);
            int id = user.getInt("id");
            String username = user.getString("username");
            String name = user.getString("name");
            int score = user.getInt("score");
            double win_rate = user.getDouble("win_rate");
            int win_length = user.getInt("win_length");

            st.addRow(Integer.toString(i + 1), Integer.toString(id), username, name, Integer.toString(score), Double.toString(win_rate), Integer.toString(win_length));
        }

        st.print();
        System.err.println("\n\n");
    }
    
    public void run_option_7() {
        JSONArray response = apiConnection.callGetListAPI(apiConnection.getRatingShortestMatchAPIURL);

        int number_of_user = response.length();
        TableTool st = new TableTool();
        st.setShowVerticalLines(true);

        System.out.println("\n\n");
        System.out.println("============= [OPTION 7] =============");
        System.out.println("Total: " + number_of_user);
        System.out.println("============= Match List (Top 10 Shortest) ==============");

        st.setHeaders("#", "id", "user 1", "user 2", "user win", "time play", "start date", "end date");
        for (int i = 0; i < response.length(); i++) {
            JSONObject match = response.getJSONObject(i);
            int id = match.getInt("id");
            String user_1 = match.getString("user_1");
            String user_2 = match.getString("user_2");
            String user_win = match.getString("user_win");
            String time_play = match.getString("time_play");
            String start_date = match.getString("start_date");
            String end_date = match.getString("end_date");

            st.addRow(Integer.toString(i + 1), Integer.toString(id), user_1, user_2, user_win, time_play, start_date, end_date);
        }

        st.print();
        System.err.println("\n\n");
    }
    
    public void run_option_8() {
        JSONArray response = apiConnection.callGetListAPI(apiConnection.getRatingLongestMatchAPIURL);

        int number_of_user = response.length();
        TableTool st = new TableTool();
        st.setShowVerticalLines(true);

        System.out.println("\n\n");
        System.out.println("============= [OPTION 8] =============");
        System.out.println("Total: " + number_of_user);
        System.out.println("============= Match List (Top 10 Longest) ==============");

        st.setHeaders("#", "id", "user 1", "user 2", "user win", "time play", "start date", "end date");
        for (int i = 0; i < response.length(); i++) {
            JSONObject match = response.getJSONObject(i);
            int id = match.getInt("id");
            String user_1 = match.getString("user_1");
            String user_2 = match.getString("user_2");
            String user_win = match.getString("user_win");
            String time_play = match.getString("time_play");
            String start_date = match.getString("start_date");
            String end_date = match.getString("end_date");

            st.addRow(Integer.toString(i + 1), Integer.toString(id), user_1, user_2, user_win, time_play, start_date, end_date);
        }

        st.print();
        System.err.println("\n\n");
    }
    
}
