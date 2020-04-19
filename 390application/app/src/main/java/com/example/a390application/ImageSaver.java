package com.example.a390application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {
    private String directoryName = "images";
    private String fileName = "image.png";
    private Context context;
    private boolean external = false;

    public ImageSaver(Context context) {
        this.context = context;
    }

    // initializes filename
    public ImageSaver setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    // initializes external
    public ImageSaver setExternal(boolean external) {
        this.external = external;
        return this;
    }
    // sets directory name
    public ImageSaver setDirectory(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    // here a bitmap is saved in an external directory of the app
    public void save(Bitmap bitmapImage) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile()); //file is created where the image would be saved
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream); // given bitmap is compressed and saved in fileOutputStream
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close(); // file is closed
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    // file is created in the external drive in the directory given
    private File createFile() {
        File directory;
        if (external) {
            directory = getAlbumStorageDir(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }
        } else {
            directory = new File(context.getFilesDir() + "/" + directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }
        }

        return new File(directory, fileName);
    }

    // here the file stored in the external storage is fetched
    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("ImageSaver", "Directory not created");
        }
        return file;
    }

    // the external storage is made writable
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // the external storage is made readable only
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    // all bitmap saved in file is loaded and decoded
    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}