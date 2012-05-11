package foxit.openvpnservice.jvmtest.managementinterfacecontext;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class WhenConnecting extends ManagementInterfaceContext {
    private static final String DUMMY_SOCKETPATH = "Dummy socket path";
    
    @Test
    public void shouldConnectDomainSocketToSocketPath() {
        when(mockSocket.connect(anyString(), anyInt())).thenReturn(true);
        managementInterface.connect(DUMMY_SOCKETPATH);
        
        verify(mockSocket).connect(eq(DUMMY_SOCKETPATH), anyInt());
    }
    
    @Test
    public void shouldRetryWhenConnectFails() {
        when(mockSocket.connect(DUMMY_SOCKETPATH, 9)).thenReturn(false);
        when(mockSocket.connect(DUMMY_SOCKETPATH, 8)).thenReturn(true);
        
        managementInterface.connect(DUMMY_SOCKETPATH);
        
        verify(mockSocket, times(2)).connect(anyString(), anyInt());
    }
    
    @Test
    public void shouldWaitBetweenRetries() {
        when(mockSocket.connect(DUMMY_SOCKETPATH, 9)).thenReturn(false);
        when(mockSocket.connect(DUMMY_SOCKETPATH, 8)).thenReturn(true);
        
        managementInterface.connect(DUMMY_SOCKETPATH);
        
        InOrder inOrder = inOrder(mockSocket);
        
        inOrder.verify(mockSocket).connect(anyString(), anyInt());
        inOrder.verify(mockSocket).pause();
        inOrder.verify(mockSocket).connect(anyString(), anyInt());
    }
    
    @Test
    public void shouldNotTryMoreThan10Times() {
        when(mockSocket.connect(anyString(), anyInt())).thenReturn(false);
        
        managementInterface.connect(DUMMY_SOCKETPATH);
        
        verify(mockSocket, times(10)).connect(anyString(), anyInt());
    }
}
