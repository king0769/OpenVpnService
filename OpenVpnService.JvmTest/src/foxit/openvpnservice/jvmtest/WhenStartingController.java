package foxit.openvpnservice.jvmtest;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

public class WhenStartingController extends OpenVpnControllerContext {
    
    private static final String DUMMY_SOCKETPATH = "Socketpath";
    
    @Test
    public void shouldReturnFalseWhenAlreadyRunning() throws IOException {
        
        when(mockProcess.isRunning()).thenReturn(true);
        
        assertFalse(controller.start());
    }
    
    @Test
    public void shouldNotStartExecutableWhenAlreadyRunning() throws IOException {
        
        when(mockProcess.isRunning()).thenReturn(true);
        
        controller.start();
        
        verify(mockExecutable, never()).execute();
    }
    
    @Test 
    public void shouldReturnTrueWhenNotAlreadyRunning() throws IOException {
        
        when(mockProcess.isRunning()).thenReturn(false);
        
        assertTrue(controller.start());
    }
    
    @Test
    public void shouldStartExecutableWhenNotAlreadyRunning() throws IOException {
        
        when(mockProcess.isRunning()).thenReturn(false);
        
        controller.start();
        
        verify(mockExecutable).execute();
    }
    
    @Test 
    public void shouldInstallExcecutableWhenNotInstalled() throws IOException {
        
        when(mockProcess.isRunning()).thenReturn(false);
        when(mockExecutable.isInstalled()).thenReturn(false);
        
        controller.start();
        
        verify(mockExecutable).install();
    }
    
    @Test
    public void shouldNotInstallExcecutableWhenAlreadyInstalled() throws IOException {
        
        when(mockProcess.isRunning()).thenReturn(false);
        when(mockExecutable.isInstalled()).thenReturn(true);
        
        controller.start();
        
        verify(mockExecutable, never()).install();
    }
    
    @Test
    public void shouldStartMonitoringProcess() throws IOException {
        when(mockProcess.isRunning()).thenReturn(false);
        when(mockExecutable.isInstalled()).thenReturn(true);
        
        controller.start();
        
        verify(mockProcess).startMonitor(any(Process.class));
    }
    
    @Test
    public void shouldStartManagementInterfaceOnTheRightPath() throws IOException {
        
        when(mockProcess.isRunning()).thenReturn(false);
        when(mockExecutable.getSocketPath()).thenReturn(DUMMY_SOCKETPATH);
        when(mockManagementInterface.isConnected()).thenReturn(false);
        
        controller.start();
        
        verify(mockManagementInterface).connect(DUMMY_SOCKETPATH);
    }
    
    @Test
    public void shouldNotStartManagementInterfaceWhenAlreadyStarted() throws IOException {
        
        when(mockProcess.isRunning()).thenReturn(false);
        when(mockExecutable.getSocketPath()).thenReturn(DUMMY_SOCKETPATH);
        when(mockManagementInterface.isConnected()).thenReturn(true);
        
        controller.start();
        
        verify(mockManagementInterface, never()).connect(DUMMY_SOCKETPATH);
    }
}
