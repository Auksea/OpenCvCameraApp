package com.example.opencvcameraapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    ViewPager mViewPager;
    ArrayList<String> filePath = new ArrayList<>();
    ViewPageAdapter viewPageAdapter;
    ImageView goBack;
    ImageView goDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        goBack = (ImageView) findViewById(R.id.goBack);
        goDelete = (ImageView) findViewById(R.id.deletePhoto);

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImagePro");
        createFileArray(folder);
        mViewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        viewPageAdapter = new ViewPageAdapter(GalleryActivity.this, filePath);
        mViewPager.setAdapter(viewPageAdapter);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, MainActivity.class));
            }
        });

        goDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPageAdapter.removeView(1);
           }
        });

    }

    private void createFileArray(File folder) {
        File listFile[] = folder.listFiles();

        if(listFile != null)
        {
            for(int i = 0; i<listFile.length; i++)
            {
                filePath.add(listFile[i].getAbsolutePath());
            }
        }
    }
}
