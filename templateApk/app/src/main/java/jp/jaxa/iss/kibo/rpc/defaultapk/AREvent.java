package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.util.Log;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

public class AREvent extends KiboRpcService {
    public AREvent(KiboRpcApi _api){
        api = _api;
    }

    public Mat GetDockCam() {
        Mat result = new Mat();
        int tries = 0;
        int maxTries = 3;

        while(tries < maxTries) {
            Log.i("AREvent", "Trial no. " + Integer.toString(tries) + "[GetDockCam] Getting Dock Image...");
            try {
                result = api.getMatDockCam();
                Log.i("AREvent", "[GetDockCam] Success!");
                break;
            } catch (Exception e) {
                Log.i("AREvent", "[GetDockCam] Error! Trying again...");
                tries++;
            }
        }
        if(result.empty())
            Log.i("AREvent", "[GetDockCam] Error! Empty Image.");
        return result;
    }

    public Mat GetNavCam() {
        Mat result = new Mat();
        int tries = 0;
        int maxTries = 3;

        while(tries < maxTries) {
            Log.i("AREvent", "Trial no. " + Integer.toString(tries) + "[GetNavCam] Getting Nav Image...");
            try {
                result = api.getMatNavCam();
                Log.i("AREvent", "[GetNavCam] Success!");
                break;
            } catch (Exception e) {
                Log.i("AREvent", "[GetNavCam] Error! Trying again...");
                tries++;
            }
        }
        if(result.empty())
            Log.i("AREvent", "[GetNavCam] Error! Empty Image.");
        return result;
    }

    public Mat UndistortedImage(Mat inputImage) {
        Mat result = new Mat();

        Mat cameraMatrix = new Mat(3, 3, CvType.CV_64F);
        cameraMatrix.put(0, 0, api.getNavCamIntrinsics()[0]);

        Mat cameraCoefficients = new Mat(1, 5, CvType.CV_64F);
        cameraCoefficients.put(0, 0, api.getNavCamIntrinsics()[1]);
        cameraCoefficients.convertTo(cameraCoefficients, CvType.CV_64F);

        //Fix Distortion
        Calib3d.undistort(inputImage, result, cameraMatrix, cameraCoefficients);
        return result;
    }

    public void DrawMarkers(Mat inputImage, ARMarker arMarker) {
        Mat result = inputImage.clone();
        int tries = 0;
        int maxTries = 3;
        while(tries < maxTries) {
            try {
                Log.i("AREvent", "[DrawMarkers]: Drawing Markers...");
                Aruco.detectMarkers(result, arMarker.dict, arMarker.markerCorners, arMarker.markerIds);
                Aruco.drawDetectedMarkers(result, arMarker.markerCorners, arMarker.markerIds, arMarker.borderColor);
                Log.i("AREvent", "[DrawMarkers]: Saving Image...");
                api.saveMatImage(result, "ARMakerDetection_Test.png");
                Log.i("AREvent", "[DrawMarkers]: Success!");
                break;
            } catch (Exception e) {
                Log.i("AREvent", "[DrawMarkers]: Error! Trying Again...");
                Log.e("AREvent", "[DrawMarkers]: "  + e.getMessage());
                tries++;
            }
        }
    }

    public void SaveMarkers(Mat inputImage, ARMarker arMarker) {
        DrawMarkers(inputImage, arMarker);
    }
}
