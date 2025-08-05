package NetworkApi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NetworkService extends Service {
    public NetworkService() {
    }
    public void getUrl(int choice){
        String url="";
        switch (choice) {
            case 0:
                url="https://api.tianapi.com/";
                break;
            case 1:
                url="https://aip.baidubce.com\"";
                break;
            default:
                break;
        }

    }

    //天行api

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}