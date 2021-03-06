package com.example.opencvcameraapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "MainActivity";
    private JavaCameraView cameraBridgeViewBase;
    private ImageView btnCanny;
    private ImageView take_picture;
    private ImageView gallery;
    private boolean runCanny = false;
    private Mat mRGBA;
    private Mat mRGBAT;
    private int take_image = 0;
    private MediaRecorder recorder;
    private ImageView btnRecording;
    private int vide_or_photo=0;
    private int take_video_or_not=0;



    private static final int MY_CAMERA_REQUEST_CODE = 100;
    int activeCamera = CameraBridgeViewBase.CAMERA_ID_ANY;

    protected MediaRecorder mRecorder;
    protected Surface mSurface;
    //I will pass this openCV frame mSurface
    //That surface is then recorded
    public void setRecorder(MediaRecorder mRec){
        mRecorder = mRec;
        if(mRecorder != null){
            mSurface=mRecorder.getSurface();


        }
    }

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:{
                    Log.i(TAG, "onManagerConnected: Opencv loaded");
                    cameraBridgeViewBase.enableView();
                }
                default:{
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraBridgeViewBase = findViewById(R.id.myCameraId);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        btnCanny = findViewById(R.id.btnCanny);
        take_picture = findViewById(R.id.take_picture);
        gallery = findViewById(R.id.go_to_gallery);
        btnRecording = findViewById(R.id.btnRecorder);
        recorder = new MediaRecorder();

        btnCanny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(runCanny == false)
                {
                    runCanny = true;
                }else
                {
                    runCanny = false;
                }
            }
        });

        btnRecording.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==motionEvent.ACTION_DOWN)
                {
                    btnRecording.setColorFilter(Color.DKGRAY);
                    return true;
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    btnRecording.setColorFilter(Color.BLACK);
                    if(vide_or_photo == 0)
                    {
                        //video mode
                        take_picture.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24);
                        take_picture.setColorFilter(Color.BLACK);
                        vide_or_photo=1;
                    }
                    else{
                        //photo mode
                        take_picture.setImageResource(R.drawable.ic_baseline_camera_24);
                        vide_or_photo=0;
                    }


                    return true;
                }
                return false;
            }
        });

        take_picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction()==motionEvent.ACTION_DOWN)
                {
                    if(vide_or_photo==0){
                        //photo mode
                        if(take_image==0){
                            take_picture.setColorFilter(Color.DKGRAY);
                        }
                    }
                    return true;
                }
                if(motionEvent.getAction()==motionEvent.ACTION_UP){
                    if(vide_or_photo==1){
                        //video mode
                        if(take_video_or_not==0){
                            try {
                                    //start recording
                                    //create folder ImagePro
                                    File folder = new File(Environment.getExternalStorageDirectory().getPath()+"/ImagePro");
                                    boolean success = true;

                                    if(!folder.exists())
                                    {
                                        success = folder.mkdirs();
                                    }

                                    take_picture.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24);
                                    take_picture.setColorFilter(Color.RED);
                                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                    recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
                                    CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                                    recorder.setProfile(camcorderProfile);
                                    //saving video file
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd_HH-mm-ss");
                                    String currentDateAndTime = sdf.format(new Date());
                                    String fileName = Environment.getExternalStorageDirectory().getPath() + "/ImagePro/" + currentDateAndTime + ".mp4";
                                    recorder.setOutputFile(fileName);
                                    recorder.setVideoSize(480,720);
                                    //default size
                                    //start recorder
                                    recorder.prepare();
                        }catch (IOException e){
                                e.printStackTrace();
                            }

                    }

                    }else{
                        take_picture.setColorFilter(Color.BLACK);
                        if(take_image == 0)
                        {
                            take_image=1;
                        }else
                        {
                            take_image=0;
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });

        // checking if the permission has already been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissions granted");
            initializeCamera(cameraBridgeViewBase, activeCamera);
        } else {
            // prompt system dialog
            Log.d(TAG, "Permission prompt");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        } if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        }
    }

    // callback to be executed after the user has givenapproval or rejection via system prompt
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // camera can be turned on
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                initializeCamera(cameraBridgeViewBase, activeCamera);
            } else {
                // camera will stay off
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeCamera(JavaCameraView javaCameraView, int activeCamera){
        javaCameraView.setCameraPermissionGranted();
        javaCameraView.setCameraIndex(activeCamera);
        javaCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat();
        mRGBAT = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraBridgeViewBase !=null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase !=null){
            cameraBridgeViewBase.disableView();
        }
    }


    @Override
    public void onCameraViewStopped() {
        mRGBA.release();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()){
//            if load success
            Log.d(TAG, "onResume: Opencv initialized");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            Log.d(TAG, "onResume: Opencv not initialized");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        }
    };

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        mRGBAT = mRGBA.t();
        Core.flip(mRGBA.t(), mRGBAT, 1);
        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());

        if(runCanny)
        {
            Mat CannyImg = new Mat();
            Imgproc.Canny(mRGBAT, CannyImg, 80,200);
            return CannyImg;
        }

        take_image=take_picture_function_rgb(take_image, mRGBA);

        return mRGBAT;
    }

    private int take_picture_function_rgb(int take_image, Mat mRGBAT) {
        if(take_image == 1)
        {
            Mat saveMat = new Mat();
            Core.flip(mRGBA.t(), saveMat, 1);
            Imgproc.cvtColor(saveMat, saveMat, Imgproc.COLOR_RGBA2BGRA);
            //creating new folder where all images will be saved
            File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/ImagePro");

            boolean success = true;

            if(!folder.exists())
            {
                success = folder.mkdirs();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd_HH-mm-ss");
            String currentDateAndTime = sdf.format(new Date());
            String fileName = Environment.getExternalStorageDirectory().getPath() + "/ImagePro/" + currentDateAndTime + ".jpg";

            Imgcodecs.imwrite(fileName, saveMat);
            take_image=0;

        }
        return  take_image;
    }

}