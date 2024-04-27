package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.util.Log;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

public class Movement extends KiboRpcService {
    public Movement(KiboRpcApi _api){
        api = _api;
    }

    public void MoveToPoint(double px, double py, double pz, float qx, float qy, float qz, float qw) {
        int tries = 0;
        int maxTries = 3;

        Point point = new Point(px, py, pz);
        Quaternion rotation = new Quaternion(qx, qy, qz, qw);

        Log.i("Movement", "Trial " + Integer.toString(tries) + " out of " + Integer.toString(maxTries) + ". Trying to move...");

        try {
            api.moveTo(point, rotation, false);
            Log.i("Movement", "MoveToPoint is Successful!");
        } catch (Exception e) {
            if(tries < maxTries){
                Log.i("Movement", "MoveToPoint is Unsuccessful. Trying again...");
                Log.e("Movement", "[Error]: " + e.getMessage());
                MoveToPoint(px, py, pz, qx, qy, qz, qw);
            } else {
                Log.i("Movement", "Reached max no. of tries.");
            }
        }
    }
}
