package foxit.openvpnservice.management;

import java.io.IOException;

public class OpenVpnController {
    private OpenVpnExecutable executable;
    private OpenVpnProcess process;
    private ManagementInterface managementInterface;
    
    public OpenVpnController(OpenVpnExecutable executable, OpenVpnProcess process, ManagementInterface mangagementInterface) {
        this.executable = executable;
        this.process = process;
        this.managementInterface = mangagementInterface;
    }
    
    /**
     * 
     * Does all the work starting an OpenVPN connection. 
     * 
     * @return True if the connection was started successfully, false if OpenVPN was already running
     * @throws IOException When something goes wrong starting OpenVPN
     */
    public Boolean start() throws IOException {
        if(process.isRunning()) return false;

        if(!executable.isInstalled()) {
            executable.install();
        }
        
        process.startMonitor(executable.execute());
        
        if(!managementInterface.isConnected()) {
            managementInterface.connect(executable.getSocketPath());
        }
        
        managementInterface.executeCommand("log on", 1);
        managementInterface.executeCommand("state on", 1);
        managementInterface.executeCommand("hold release", 100);
        
        return true;
    }
    
    /**
     * Stops the OpenVPN process
     */
    public void stop() {
        if(process.isRunning()) {
            process.stop();
        }
        
        if(managementInterface.isConnected()) {
            managementInterface.disconnect();
        }
    }
    
    /**
     * Sets the event handler that gets called when the process stops.
     * 
     * @param handler The event handler to use, null for no handler.
     */
    public void setProcessStoppedHandler(EventHandler<Integer> handler) {
        process.setOnProcessExited(handler);
    }
}
