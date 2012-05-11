package foxit.openvpnservice.androidtest;

import android.content.Intent;
import android.test.AndroidTestCase;
import foxit.openvpnservice.OpenVpnService;

public class Tests extends AndroidTestCase {

//    public Tests(String name) {
//        super(name);
//    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testStop() {
        Intent intent = new Intent(mContext, OpenVpnService.class);
        
        this.mContext.startService(intent);
    }
}
