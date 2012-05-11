package foxit.openvpnservice.management;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class OpenVpnExecutableImpl implements OpenVpnExecutable {
    private static final String TAG = "OpenVpnExecutable";
    
	private static final String OPENVPNFILENAME = "openvpn";
	private static final String OPENVPNOPTIONS = "--dev null --management %s unix --management-hold --tmp-dir %s";
	
	private Context context;
	
	public OpenVpnExecutableImpl(Context context) {
		this.context = context;
	}
	
	public synchronized Process execute() throws IOException {
        String commandLine = getOpenVpnFilePath() + " " + getOpenVpnOptions();
        
        Log.i(TAG, "Starting OpenVPN executable: " + commandLine);
        
		Process process = Runtime.getRuntime().exec(commandLine);

        Log.i(TAG, "OpenVPN executable started");
        
        return process;
	}
	
	public synchronized void install() throws IOException {
		String executableFilename = getOpenVpnFilePath();
		
		Log.i(TAG, "Installing OpenVPN binary to " + executableFilename);
		
		writeAssetToFile(OPENVPNFILENAME, executableFilename);
		
		Log.i(TAG, "Set OpenVPN binary permissions");
		setFilePermissions("744", executableFilename);
	}
	
	private void writeAssetToFile(String assetName, String filePath) throws IOException {
		AssetManager assets = context.getAssets();
		
		InputStream in = null;
		OutputStream out = null;
		
		try {
			in = assets.open( assetName );    
	        out = new FileOutputStream( filePath);
	        
		    byte[] buffer = new byte[1024];
		    int read;
		    while((read = in.read(buffer)) != -1){
		      out.write(buffer, 0, read);
		    }
		}
		finally {
			if(in != null) {
				in.close();
				in = null;
			}
			
			if(out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}
	}
	
	private String getOpenVpnFilePath() {
        return new File(context.getFilesDir(), OPENVPNFILENAME).getPath();
    }
    
    private String getOpenVpnOptions() {
        return String.format(OPENVPNOPTIONS, getSocketPath(), context.getCacheDir());
    }
    
    public String getSocketPath() {
        return new File(context.getCacheDir(), "managementSocket").getPath();
    }
    
	private void setFilePermissions(String permissions, String filePath) throws IOException {
		String command = "/system/bin/chmod " + permissions + " " + filePath;
		Runtime.getRuntime().exec(command);
	}
	
	public Boolean isInstalled() {
		File openVpnFile = new File(getOpenVpnFilePath());
		return openVpnFile.exists();
	}
}
