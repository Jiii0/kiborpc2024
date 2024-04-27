package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.odml.image.MlImage;

import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;

import java.util.List;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcApi;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

public class ItemRecognition extends KiboRpcService {
    private String modelFile;
    public ItemRecognition(KiboRpcApi _api, String modelFile) {
        api = _api;
        this.modelFile = modelFile;
    }
}
