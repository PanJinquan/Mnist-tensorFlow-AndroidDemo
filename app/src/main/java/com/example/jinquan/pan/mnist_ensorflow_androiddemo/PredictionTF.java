package com.example.jinquan.pan.mnist_ensorflow_androiddemo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class PredictionTF {
    private static final String TAG = "PredictionTF";
    //设置模型输入/输出节点的数据维度
    private static final int IN_COL = 1;
    private static final int IN_ROW = 28*28;
    private static final int OUT_COL = 1;
    private static final int OUT_ROW = 1;
    //模型中输入变量的名称
    private static final String inputName = "input/x_input";
    //模型中输出变量的名称
    private static final String outputName = "output";

    TensorFlowInferenceInterface inferenceInterface;
    static {
        //加载libtensorflow_inference.so库文件
        System.loadLibrary("tensorflow_inference");
        Log.e(TAG,"libtensorflow_inference.so库加载成功");
    }

    PredictionTF(AssetManager assetManager, String modePath) {
        //初始化TensorFlowInferenceInterface对象
        inferenceInterface = new TensorFlowInferenceInterface(assetManager,modePath);
        Log.e(TAG,"TensoFlow模型文件加载成功");
    }

    /**
     *  利用训练好的TensoFlow模型预测结果
     * @param bitmap 输入被测试的bitmap图
     * @return 返回预测结果，int数组
     */
    public int[] getPredict(Bitmap bitmap) {
        float[] inputdata = bitmapToFloatArray(bitmap,28,28);//需要将图片缩放带28*28
        //将数据feed给tensorflow的输入节点
        inferenceInterface.feed(inputName, inputdata, IN_COL, IN_ROW);
        //运行tensorflow
        String[] outputNames = new String[] {outputName};
        inferenceInterface.run(outputNames);
        ///获取输出节点的输出信息
        int[] outputs = new int[OUT_COL*OUT_ROW]; //用于存储模型的输出数据
        inferenceInterface.fetch(outputName, outputs);
        return outputs;
    }

    /**
     * 将bitmap转为（按行优先）一个float数组，并且每个像素点都归一化到0~1之间。
     * @param bitmap 输入被测试的bitmap图片
     * @param rx 将图片缩放到指定的大小（列）->28
     * @param ry 将图片缩放到指定的大小（行）->28
     * @return   返回归一化后的一维float数组 ->28*28
     */
    public static float[] bitmapToFloatArray(Bitmap bitmap, int rx, int ry){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        // 计算缩放比例
        float scaleWidth = ((float) rx) / width;
        float scaleHeight = ((float) ry) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        Log.i(TAG,"bitmap width:"+bitmap.getWidth()+",height:"+bitmap.getHeight());
        Log.i(TAG,"bitmap.getConfig():"+bitmap.getConfig());
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        float[] result = new float[height*width];
        int k = 0;
        //行优先
        for(int j = 0;j < height;j++){
            for (int i = 0;i < width;i++){
                int argb = bitmap.getPixel(i,j);
                int r = Color.red(argb);
                int g = Color.green(argb);
                int b = Color.blue(argb);
                int a = Color.alpha(argb);
                //由于是灰度图，所以r,g,b分量是相等的。
                assert(r==g && g==b);
//                Log.i(TAG,i+","+j+" : argb = "+argb+", a="+a+", r="+r+", g="+g+", b="+b);
                result[k++] = r / 255.0f;
            }
        }
        return result;
    }
}
