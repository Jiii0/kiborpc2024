package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.odml.image.ByteBufferMlImageBuilder;
import com.google.android.odml.image.MlImage;

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

    public void RunObjectDetection(Bitmap inputImage) {
        TensorImage tensorImage = TensorImage.fromBitmap(inputImage);
        ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(5)
                .setScoreThreshold(0.3f)
                .build();
        try {
            Log.i("RunObjectDetection", "[Start] Trying to detect objects...");
            ObjectDetector detector = ObjectDetector.createFromFileAndOptions(modelFile, options);
            List<Detection> result = detector.detect(tensorImage);
            Log.i("RunObjectDetection", "[Finish] Success!!");
        } catch (Exception e) {
            Log.i("RunObjectDetection", "[Error] No file found...");
        }

    }
}
