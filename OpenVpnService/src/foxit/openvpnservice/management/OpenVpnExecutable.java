package foxit.openvpnservice.management;

import java.io.IOException;

public interface OpenVpnExecutable {
    Process execute() throws IOException;
    void install() throws IOException;
    
    Boolean isInstalled();
    
    String getSocketPath();
}
