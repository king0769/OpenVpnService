package foxit.openvpnservice.androidtest;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import foxit.openvpnservice.OpenVpnManagementActivity;
import foxit.openvpnservice.R;

public class ManagementActivityTests extends ActivityInstrumentationTestCase2<OpenVpnManagementActivity> {
    public ManagementActivityTests() {
        this("OpenVpnManagementActivity");
    }
    
    public ManagementActivityTests(String name) {
        super(OpenVpnManagementActivity.class);
        setName(name);
    }
    
    public void setUp() {
        activity = getActivity();
        
        startButton = (Button)activity.findViewById(R.id.startButton);
        stopButton = (Button)activity.findViewById(R.id.stopButton);
    }
    
    OpenVpnManagementActivity activity;
    Button startButton;
    Button stopButton;
    
    public void testOne() {
        assertNotNull(activity);
    }
    
    public void testControlsInitialized() {
        assertNotNull(startButton);
        assertNotNull(stopButton);
    }
    
    @UiThreadTest
    public void testPushButton() {
        startButton.performClick();
    }
}
