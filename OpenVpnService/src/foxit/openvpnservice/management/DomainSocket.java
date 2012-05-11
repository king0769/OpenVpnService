package foxit.openvpnservice.management;

import java.io.IOException;

public interface DomainSocket {
    Boolean connect(String path, int attemptsRemaining);
    void disconnect();
    
    Boolean isConnected();
    
    void clear() throws IOException;
    String read() throws IOException;
    void write(String value) throws IOException;
    
    void pause();
}
