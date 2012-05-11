package foxit.openvpnservice.jvmtest.managementinterfacecontext;

import static org.mockito.Mockito.mock;
import foxit.openvpnservice.management.DomainSocket;
import foxit.openvpnservice.management.ManagementInterfaceImpl;

public class ManagementInterfaceContext {
    protected DomainSocket mockSocket;
    protected ManagementInterfaceImpl managementInterface;
    
    public ManagementInterfaceContext() {
        mockSocket = mock(DomainSocket.class);
        managementInterface = new ManagementInterfaceImpl(mockSocket);
    }
}
