package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.util.Log;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.Kinematics;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

public class ARMarker extends KiboRpcService {
    public Dictionary dict = Aruco.getPredefinedDictionary(Aruco.DICT_5X5_250);
    public List<Mat> markerCorners = new ArrayList<>();
    public Mat markerIds = new Mat();
    public Scalar borderColor = new Scalar(0, 255, 0);

    public ARMarker(KiboRpcApi _api) {
        api = _api;
    }

    public List<Point> GetMarkerPos() {
        Mat coordinates = markerCorners.get(0);
        List<Point> corners_xy = new ArrayList<>(4);
        for (int r = 0; r < coordinates.rows(); r++) {
            for (int c = 0; c < coordinates.cols(); c++) {
                corners_xy.add(new Point((int)coordinates.get(r,c)[0], (int)coordinates.get(r,c)[1])); // x,y point
            }
        }
        Log.i("GetMarkerPos", String.format("X1: %d, Y1: %d", (int)corners_xy.get(0).x, (int)corners_xy.get(0).y));
        Log.i("GetMarkerPos", String.format("X2: %d, Y2: %d", (int)corners_xy.get(1).x, (int)corners_xy.get(1).y));
        Log.i("GetMarkerPos", String.format("X3: %d, Y3: %d", (int)corners_xy.get(2).x, (int)corners_xy.get(2).y));
        Log.i("GetMarkerPos", String.format("X4: %d, Y4: %d", (int)corners_xy.get(3).x, (int)corners_xy.get(3).y));
        return corners_xy;
    }

    public Point GetMarkerSize(List<Point> corners) {
        double width = Math.abs((corners.get(0).x - corners.get(1).x));
        double height = Math.abs((corners.get(0).y - corners.get(3).y));

        Log.i("GetMarkerSize", String.format("Width: %f, Height: %f", (float) width, (float) height));
        return new Point(width, height);
    }
}
