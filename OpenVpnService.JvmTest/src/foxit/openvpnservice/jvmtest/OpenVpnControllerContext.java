package foxit.openvpnservice.jvmtest;

import static org.mockito.Mockito.mock;
import foxit.openvpnservice.management.*;

public class OpenVpnControllerContext {
    protected OpenVpnExecutable mockExecutable;
    protected OpenVpnProcess mockProcess;
    protected ManagementInterface mockManagementInterface;
    
    protected OpenVpnController controller;
    
    public OpenVpnControllerContext() {
        mockExecutable = mock(OpenVpnExecutable.class);
        mockProcess = mock(OpenVpnProcess.class);
        mockManagementInterface = mock(ManagementInterface.class);
        
        controller = new OpenVpnController(mockExecutable, mockProcess, mockManagementInterface);
    }
}
