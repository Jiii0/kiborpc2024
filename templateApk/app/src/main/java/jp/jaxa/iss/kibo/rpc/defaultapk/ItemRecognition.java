package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.odml.image.ByteBufferMlImageBuilder;
import com.google.android.odml.image.MlImage;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.InterpreterApi;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

public class ItemRecognition extends KiboRpcService {
    private File modelFile;

    public ItemRecognition(KiboRpcApi _api, String modelFile) {
        api = _api;
        this.modelFile = new File(modelFile);
    }

    public Bitmap MatToBitmap(Mat inputImage) {
        Log.i("MatToBitmap", "[Start]");
        Bitmap bitmapImage = null;
        Mat tmp = new Mat(inputImage.height(), inputImage.width(), inputImage.type(), new Scalar(4));
        try {
            Log.i("MatToBitmap", "[Start] Creating Bitmap...");
            Imgproc.cvtColor(inputImage, tmp, Imgproc.COLOR_GRAY2RGBA, 4);
            bitmapImage = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bitmapImage);
            Log.i("MatToBitmap", "[Finish] Bitmap Created.");
            return bitmapImage;
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }

        return null;
    }

    public void RunObjectDetection(Mat inputImage) {
        Bitmap bitmapImage = MatToBitmap(inputImage);

        TensorImage tensorImage = TensorImage.fromBitmap(bitmapImage);
        ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(5)
                .setScoreThreshold(0.3f)
                .build();

        try {
            Log.i("RunObjectDetection", "[Start] Loading model...");
            ObjectDetector detector = ObjectDetector.createFromFileAndOptions(modelFile, options);
            Log.i("RunObjectDetection", "[Model] Detector initialized...");
            List<Detection> result = detector.detect(tensorImage);
            Log.i("RunObjectDetection", "[Finish] Success!!");
        } catch (Exception e) {
            Log.i("RunObjectDetection", "[Error] No file found...");
            Log.e("RunObjectDetection", e.getMessage());
        }

    }
}
