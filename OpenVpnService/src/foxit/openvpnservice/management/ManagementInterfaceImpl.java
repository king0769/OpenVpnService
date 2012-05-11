package foxit.openvpnservice.management;

import java.io.IOException;

/**
 * @author Mendelt Siebenga
 */
public class ManagementInterfaceImpl implements ManagementInterface {
    public final static String TAG = "ManagementInterface";

    public final static int CONNECTIONATTEMPTS = 10;

    private DomainSocket socket;
    
    public ManagementInterfaceImpl(DomainSocket socket) {
        this.socket = socket;
    }
    
    /**
     * Connect to the management interface socket
     * 
     * @param socketPath The path on the filesystem where the socket can be found.
     * @return True if connection succeeded, false if not.
     */
    synchronized public Boolean connect(String socketPath) {
        for(int attemptsRemaining = CONNECTIONATTEMPTS - 1; attemptsRemaining >= 0; attemptsRemaining-- ) {
            if(socket.connect(socketPath, attemptsRemaining)) return true;
            
            socket.pause();
        }
        
        return false;
    }
    
    /**
     * Execute a command on the managementinterface.
     * 
     * @param command
     *            The command that will be executed on the management interface
     * @param lines
     *            The maximum amount of lines the command will return
     * @return The output from the command.
     */
    synchronized public String executeCommand(String command, int lines) throws IOException {
        socket.clear();
        
        socket.write(command + "\r\n");

        StringBuilder output = new StringBuilder();
        String outputLine;

        // Read lines until the max amount of lines has been read or the string 'END' has been found
        for (int counter = 0; counter < lines; counter++) {
            outputLine = socket.read();

            if (outputLine == null || outputLine.equals("END")) break;

            output.append(outputLine);
        }

        return output.toString();
    }
    
    synchronized public void disconnect() {
        socket.disconnect();
    }
    
    public Boolean isConnected() {
        return socket.isConnected();
    }
}
