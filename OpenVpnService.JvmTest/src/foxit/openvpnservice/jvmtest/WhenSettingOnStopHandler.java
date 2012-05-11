package foxit.openvpnservice.jvmtest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import foxit.openvpnservice.management.EventHandler;

public class WhenSettingOnStopHandler extends OpenVpnControllerContext {

    private EventHandler<Integer> dummyHandler;
    
    @SuppressWarnings("unchecked")
    public WhenSettingOnStopHandler() {
        dummyHandler = mock(EventHandler.class);
    }

    @Test
    public void shouldSetProcessStoppedHandler() {
        
        controller.setProcessStoppedHandler(dummyHandler);
        
        verify(mockProcess).setOnProcessExited(dummyHandler);
    }
    
    
}
