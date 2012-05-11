package foxit.openvpnservice.management;

import java.io.IOException;

public interface ManagementInterface {
    Boolean connect(String socketPath);
    void disconnect();
    
    String executeCommand(String command, int lines) throws IOException;
    
    Boolean isConnected();
}
