package foxit.openvpnservice.jvmtest.managementinterfacecontext;

import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class WhenExecutingCommand extends ManagementInterfaceContext {
    private final static String DUMMY_COMMAND = "Dummy Command";
    private final static String DUMMY_OUTPUT = "Dummy Output";
    
    @Test
    public void shouldWriteCommandPlusNewlineToSocket() throws IOException {
        managementInterface.executeCommand(DUMMY_COMMAND, 10);
        
        verify(mockSocket).write(eq(DUMMY_COMMAND + "\r\n"));
    }
    
    @Test
    public void shouldClearInputBeforeRunningCommand() throws IOException {
        managementInterface.executeCommand(DUMMY_COMMAND, 10);
        
        InOrder inOrder = inOrder(mockSocket);
        
        inOrder.verify(mockSocket).clear();
        inOrder.verify(mockSocket).write(anyString());
    }
    
    @Test
    public void shouldReadOutputLine() throws IOException {
        when(mockSocket.read()).thenReturn(DUMMY_OUTPUT);
        
        String output = managementInterface.executeCommand(DUMMY_COMMAND, 1);
        
        assertEquals(DUMMY_OUTPUT, output);
    }
    
    @Test
    public void shouldReadMultipleLines() throws IOException {
        when(mockSocket.read()).thenReturn(DUMMY_OUTPUT);
        
        String output = managementInterface.executeCommand(DUMMY_COMMAND, 2);
        
        assertEquals(DUMMY_OUTPUT + DUMMY_OUTPUT, output);
    }
    
    @Test
    public void shouldReadUntilEND() throws IOException {
        when(mockSocket.read())
            .thenReturn(DUMMY_OUTPUT)
            .thenReturn(DUMMY_OUTPUT)
            .thenReturn("END");
        
        String output = managementInterface.executeCommand(DUMMY_COMMAND, 5);
        
        assertEquals(DUMMY_OUTPUT + DUMMY_OUTPUT, output);
    }
}
