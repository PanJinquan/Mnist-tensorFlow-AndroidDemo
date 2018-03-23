package com.example.jinquan.pan.mnist_ensorflow_androiddemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAG = "MainActivity";
    private static final String MODEL_FILE = "file:///android_asset/mnist.pb"; //模型存放路径
    TextView txt;
    TextView tv;
    ImageView imageView;
    Bitmap bitmap;
    PredictionTF preTF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        txt=(TextView)findViewById(R.id.txt_id);
        imageView =(ImageView)findViewById(R.id.imageView1);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
        imageView.setImageBitmap(bitmap);
        preTF =new PredictionTF(getAssets(),MODEL_FILE);//输入模型存放路径，并加载TensoFlow模型
    }

    public void click01(View v){
        String res="预测结果为：";
        int[] result= preTF.getPredict(bitmap);
        for (int i=0;i<result.length;i++){
            Log.i(TAG, res+result[i] );
            res=res+String.valueOf(result[i])+" ";
        }
        txt.setText(res);
        tv.setText(stringFromJNI());
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
