package filetest.casualdrive.com.filetest;

import android.app.Application;
import com.smrtbeat.SmartBeat;


public class MainApplication extends Application
{ 
    @Override 
    public void onCreate ()
    {
        super.onCreate();

        SmartBeat.initAndStartSession(this, "e8c58ce4-24fd-4cb7-8812-e5253130a9ce");
        SmartBeat.enableAutoScreenCapture();
        SmartBeat.enableLogCat("*:W");
        SmartBeat.enableDebugLog("smartbeat");
        SmartBeat.setUserId("test_user");
    }
    
} 
 