package foxit.openvpnservice.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;

public class DomainSocketImpl implements DomainSocket {
    public final static String TAG = "DomainSocket";
    
    private LocalSocket socket;
    
    private PrintWriter socketWriter;
    private BufferedReader socketReader;
    
    public Boolean connect(String path, int attemptsRemaining) {
        try {
            Log.v(TAG, String.format("Connecting to domainsocket %s", path));
            
            socket = new LocalSocket();
            socket.connect(new LocalSocketAddress(path, LocalSocketAddress.Namespace.FILESYSTEM));
            
            socketWriter = new PrintWriter(socket.getOutputStream());
            socketReader = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            
            Log.v(TAG, "Connection succesful");
            
            return true;
        }
        catch (IOException exception) {
            Log.v(TAG, String.format( "Connection failed, wait and retry %d attempts remaining"), exception);
            socket = null;
            return false;
        }
    }
    
    public void disconnect() {
        try {
            socketWriter.close();
            socketWriter = null;
            
            socketReader.close();
            socketReader = null;
            
            socket.close();
            socket = null;
        }
        catch (IOException exception) {
            Log.e(TAG, "Error closing management interface connection", exception);
        }
    }
    
    public void clear() throws IOException {
        Log.v(TAG, "Clear input stream");
        
        while (socketReader.ready()) {
            socketReader.read();
        }
    }
    
    public String read() throws IOException {
        String line = socketReader.readLine();
        Log.v(TAG, line);
        
        return line;
    }
    
    public void write(String value) throws IOException {
        Log.v(TAG, String.format( "Write command to domain socket: %s", value));
        
        socketWriter.print(value);
        socketWriter.flush();
    }
    
    public void pause() {
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
    
    public Boolean isConnected() {
        if(socket == null) return false;
        
        return socket.isConnected();
    }
}
