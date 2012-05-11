package foxit.openvpnservice.management;

public interface OpenVpnProcess {
    void startMonitor(Process process);
    void stop();
    
    void setOnProcessExited(EventHandler<Integer> value);
    
    Boolean isRunning();
}
