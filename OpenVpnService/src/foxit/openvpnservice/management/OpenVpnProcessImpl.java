package foxit.openvpnservice.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import android.util.Log;

public class OpenVpnProcessImpl implements OpenVpnProcess {

    private static final String TAG = "OpenVpnProcess";

    private Process openVpnProcess;
    private Thread monitorThread;
    
    public void startMonitor(Process process) {
        openVpnProcess = process;
        
        monitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                monitorProcess(openVpnProcess);
            }
        });
        
        monitorThread.start();
        Log.i(TAG, "Started monitor thread");
    }

    private int getPid() {
        // Use nasty reflection hack to get at the pid
        Field pidField;
        try {
            pidField = openVpnProcess.getClass().getDeclaredField("pid");
            pidField.setAccessible(true);
            return (Integer) pidField.get(openVpnProcess);

        } catch (NoSuchFieldException e) {
            return 0;
        } catch (IllegalArgumentException e) {
            return 0;
        } catch (IllegalAccessException e) {
            return 0;
        }
    }
    
    public void stop() {
        if (openVpnProcess != null) {
            killProcess();
            Log.i(TAG, "Kill signal sent");
        }
    }
    
    private void killProcess() {
        android.os.Process.sendSignal(getPid(), android.os.Process.SIGNAL_KILL);
        openVpnProcess = null;
    }
    
    public Boolean isRunning() {
        return openVpnProcess != null;
    }
    
    private void monitorProcess(Process process) {
        openVpnProcess = process;

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                openVpnProcess.getInputStream()));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                Log.i(TAG, line);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            process.waitFor();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        Log.i(TAG, "OpenVPN process has exited, stopping monitoring thread");
        openVpnProcess = null;

        notifyProcessExited(process.exitValue());
    }

    private EventHandler<Integer> onProcessExited;

    public void setOnProcessExited(EventHandler<Integer> value) {
        onProcessExited = value;
    }

    private void notifyProcessExited(int exitCode) {
        if (onProcessExited != null) {
            onProcessExited.Raise(this, exitCode);
        }
    }
    
    protected void finalize() throws Throwable {
        // TODO: Move to controller finalize and test
        try {
            if(openVpnProcess != null) {
                killProcess();
            }
        }
        finally {
            super.finalize();
        }
    }
}
