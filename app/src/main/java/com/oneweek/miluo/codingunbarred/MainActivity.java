package com.oneweek.miluo.codingunbarred;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Courses button
    public void showCourseSelection(View view) {
        Intent intent = new Intent(this, CourseSelectionActivity.class);
        startActivity(intent);
    }

    // Resources button
    public void showResourceSelection(View view) {
        Intent intent = new Intent(this, ResourceSelectionActivity.class);
        startActivity(intent);
    }

    // Sandbox button
    public void showSandbox(View view) {
        Intent intent = new Intent(this, DevModeActivity.class);
        startActivity(intent);
    }
}
