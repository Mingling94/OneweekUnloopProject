package com.oneweek.miluo.codingunbarred;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DevModeActivity extends AppCompatActivity {

    public static final String COURSE_NAME = "COURSE_NAME";

    public static final String LESSON_NAME = "LESSON_NAME";

    private static final String HTML_EXTENSION = ".html";

    private static final String CSS_EXTENSION = ".css";

    private static final String JS_EXTENSION = ".js";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devmode);

        Intent intent = getIntent();
        String lessonName = intent.getStringExtra(LESSON_NAME);
        String courseName = intent.getStringExtra(COURSE_NAME);

        //code to load in the lesson
        WebView lessonBox = (WebView)findViewById(R.id.LessonBox);
        lessonBox.getSettings().setJavaScriptEnabled(true);
        lessonBox.loadUrl("file:///android_asset/" + "courses/" + courseName + "/lessons/" + lessonName + "/" + lessonName + ".html");

        String htmlSnippet = tryReadFile(getSavedSnippetFilename(lessonName, HTML_EXTENSION));
        String cssSnippet = tryReadFile(getSavedSnippetFilename(lessonName, CSS_EXTENSION));
        String jsSnippet = tryReadFile(getSavedSnippetFilename(lessonName, JS_EXTENSION));

        if (htmlSnippet != null && cssSnippet != null && jsSnippet != null) {
            this.populateEditors(htmlSnippet, cssSnippet, jsSnippet);
        } else {
            this.resetCodeSnippets();
        }

        this.runCode();
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.saveCodeSnippets();
    }

    private void runCode() {
        this.saveCodeSnippets();

        Intent intent = getIntent();
        String lessonName = intent.getStringExtra(LESSON_NAME);
        String courseName = intent.getStringExtra(COURSE_NAME);


        WebView webBox = (WebView)findViewById(R.id.WebBox);
        webBox.getSettings().setJavaScriptEnabled(true);
        //fix below code to give it a real path
        webBox.loadUrl(getSavedSnippetFilename(lessonName, HTML_EXTENSION).getAbsolutePath());
    }

    private void populateEditors(String htmlSnippet, String cssSnippet, String jsSnippet) {
        EditText htmlEditor = (EditText)findViewById(R.id.HTMLBox);
        EditText cssEditor = (EditText)findViewById(R.id.CSSBox);
        EditText jsEditor = (EditText)findViewById(R.id.JSBox1);

        htmlEditor.setText(htmlSnippet);
        cssEditor.setText(cssSnippet);
        jsEditor.setText(jsSnippet);
    }

    private void resetCodeSnippets() {
        Intent intent = getIntent();
        String lessonName = intent.getStringExtra(LESSON_NAME);
        String courseName = intent.getStringExtra(COURSE_NAME);

        //fix below code to have course name too
        String htmlSnippet = tryReadAsset(getDefaultSnippetFilename(lessonName, HTML_EXTENSION));
        String cssSnippet = tryReadAsset(getDefaultSnippetFilename(lessonName, CSS_EXTENSION));
        String jsSnippet = tryReadAsset(getDefaultSnippetFilename(lessonName, JS_EXTENSION));

        this.populateEditors(htmlSnippet, cssSnippet, jsSnippet);
        this.saveCodeSnippets();
    }

    private void saveCodeSnippets() {
        Intent intent = getIntent();
        String lessonName = intent.getStringExtra(LESSON_NAME);
        String courseName = intent.getStringExtra(COURSE_NAME);

        EditText htmlEditor = (EditText)findViewById(R.id.HTMLBox);
        EditText cssEditor = (EditText)findViewById(R.id.CSSBox);
        EditText jsEditor = (EditText)findViewById(R.id.JSBox1);

        //pass coursename in here
        saveFile(getSavedSnippetFilename(lessonName, HTML_EXTENSION), htmlEditor.getText().toString());
        saveFile(getSavedSnippetFilename(lessonName, CSS_EXTENSION), cssEditor.getText().toString());
        saveFile(getSavedSnippetFilename(lessonName, JS_EXTENSION), jsEditor.getText().toString());
    }

    private String getDefaultSnippetFilename(String lessonName, String suffix) {

        return "file:///android_asset/" + lessonName + "/preloadedCode/" + lessonName + suffix;
    }

    private File getSavedSnippetFilename(String lessonName, String suffix) {
        return new File(new File(getFilesDir(), lessonName), lessonName + suffix);
    }

    private String tryReadAsset(String filename) {
        InputStream inputStream;
        StringBuilder buffer = new StringBuilder();

        try {
            inputStream = getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();
            while (line != null) {
                buffer.append(buffer);
                line = reader.readLine();
            }

            inputStream.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return buffer.toString();
    }

    private String tryReadFile(File file) {
        if (!file.exists())
        {
            return null;
        }

        StringBuilder buffer = new StringBuilder();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            while (line != null) {
                buffer.append(buffer);
                line = reader.readLine();
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return buffer.toString();
    }

    private void saveFile(File file, String snippet) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(snippet);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
