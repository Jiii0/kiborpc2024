package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.util.Log;

import org.opencv.aruco.Aruco;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.utils.Converters;

import java.util.Dictionary;
import java.util.List;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        // write your plan 1 here
        api.startMission();

        Movement move = new Movement(api);
        AREvent arEvent = new AREvent(api);
        ARMarker arMarker = new ARMarker(api);

        move.MoveToPoint(10.9d, -9.92284d, 5.195d, 0f, 0f, -0.707f,0.707f);

        Mat navImage = arEvent.GetNavCam();
        Mat undistortedNavImage = arEvent.UndistortedImage(navImage);

        api.saveMatImage(navImage, "Original_NavCam.png");
        arEvent.SaveMarkers(undistortedNavImage, arMarker);
        List<Point> corners =  arMarker.GetMarkerPos();
        Point size = arMarker.GetMarkerSize(corners);


        // Scan All Objects then Go to the Astronaut

        // Scan target object and find in the list

        // Go to the nearest matched object
    }

    @Override
    protected void runPlan2(){
        // write your plan 2 here
    }

    @Override
    protected void runPlan3(){
        // write your plan 3 here
    }
}

