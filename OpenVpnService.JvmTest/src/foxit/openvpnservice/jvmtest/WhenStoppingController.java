package foxit.openvpnservice.jvmtest;

import static org.mockito.Mockito.*;
import java.io.IOException;

import org.junit.Test;

public class WhenStoppingController extends OpenVpnControllerContext {

    @Test
    public void shouldStopProcessWhenRunning() throws IOException {
        when(mockProcess.isRunning()).thenReturn(true);
        
        controller.stop();
        
        verify(mockProcess).stop();
    }
    
    @Test
    public void shouldNotStopProcessWhenNotRunning() {
        when(mockProcess.isRunning()).thenReturn(false);
        
        controller.stop();
        
        verify(mockProcess, never()).stop();
    }
    
    @Test
    public void shouldStopManagementInterfaceWhenRunning() {
        when(mockManagementInterface.isConnected()).thenReturn(true);
        
        controller.stop();
        
        verify(mockManagementInterface).disconnect();
    }
    
    @Test
    public void shouldNotStopMangementInterfaceWhenNotRunning() {
        when(mockManagementInterface.isConnected()).thenReturn(false);
        
        controller.stop();
        
        verify(mockManagementInterface, never()).disconnect();
    }
}
