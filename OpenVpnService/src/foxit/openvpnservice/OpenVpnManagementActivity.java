package foxit.openvpnservice;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author Mendelt Siebenga
 */
public class OpenVpnManagementActivity extends Activity {
    
	private Button startButton;
	private Button stopButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        
        startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start();
			}
		});
        
        stopButton.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
				stop();
			}
        });
    }
	
	private void start() {
		// Prepare to call the VpnService
		Intent prepareIntent = VpnService.prepare(this);
		
		if (prepareIntent != null) {
			// Ask the user if he is ok with setting up a VPN.
			startActivityForResult(prepareIntent, 0);
        } else {
        	// The vpnservice was already prepared, start it
        	startService();
        }
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (result == RESULT_OK) {
        	startService();
        }
    }
    
    private void startService() {
        Intent intent = new Intent(this, OpenVpnService.class);
        
        startService(intent);
    }
    
	private void stop() {
		Intent intent = new Intent(this, OpenVpnService.class);
		
		stopService(intent);
	}
}