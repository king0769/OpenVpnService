package foxit.openvpnservice;

import java.io.IOException;

import android.app.Notification;
import android.content.Intent;
import android.net.VpnService;
import android.util.Log;
import foxit.openvpnservice.management.DomainSocketImpl;
import foxit.openvpnservice.management.EventHandler;
import foxit.openvpnservice.management.ManagementInterfaceImpl;
import foxit.openvpnservice.management.OpenVpnController;
import foxit.openvpnservice.management.OpenVpnExecutableImpl;
import foxit.openvpnservice.management.OpenVpnProcessImpl;

public class OpenVpnService extends VpnService {
    private static final String TAG = "OpenVpnService";

    private OpenVpnController controller = 
        new OpenVpnController(
            new OpenVpnExecutableImpl(this), 
            new OpenVpnProcessImpl(),
            new ManagementInterfaceImpl(new DomainSocketImpl()));
    
    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.i(TAG, "OpenVpnService starting");
            
            controller.setProcessStoppedHandler(onOpenVpnExit);
            
            if(controller.start()) {
                Log.i(TAG, "OpenVpnService started");
                
                // TODO: Make controller.start do this to make it testable;
                showNotification();
            }
            else {
                Log.i(TAG, "OpenVpnService was already started, not starting again");
            }
        }
        catch(IOException exception) {
            Log.e(TAG, "Error starting OpenVpn", exception);
            
            stopSelf();
        }
        
        return START_STICKY;
    }

    private void showNotification() {
        Notification.Builder builder = new Notification.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentInfo("Running")
            .setContentTitle("OpenVPN Service");
      
        startForeground(1, builder.getNotification());
    }
    
    private void hideNotification() {
        stopForeground(true);
    }
    
    @Override
    public void onDestroy() {
        Log.i(TAG, "OpenVpnService stopping");

        controller.stop();
        
        // TODO: Make controller.stop handle this to make it testable.
        hideNotification();
        
        Log.i(TAG, "OpenVpnService stopped");
        super.onDestroy();
    }
    
    private EventHandler<Integer> onOpenVpnExit = new EventHandler<Integer>() {
        @Override
        public void Raise(Object sender, Integer data) {
            Log.i(TAG, "OpenVpn process stopped, stopping OpenVpnService");

            stopSelf();
        }
    };
}
