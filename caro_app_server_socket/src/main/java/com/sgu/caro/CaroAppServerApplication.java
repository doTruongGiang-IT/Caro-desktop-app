package com.sgu.caro;

import com.sgu.caro.cli_tool.CLITool;
import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.socket_connection.SocketConnection;

public class CaroAppServerApplication {
        
    public static void main(String[] args) {
        new APIConnection().getJWT();
        CLITool cliTool = new CLITool();
        cliTool.run();
        SocketConnection socket = new SocketConnection();
        socket.startConnection();
    }

}
