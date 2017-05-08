package com.yang.sharelogin.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

public final class ImageUtils {
    
    /**
     * Check the SD card 
     * @return
     */
    public static boolean checkSDCardAvailable(){
        return android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
    }
    
    /**
     * Save image to the SD card 
     * @param photoBitmap
     * @param path
     * @param photoName
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }
            
            File photoFile = new File(path , photoName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
//                        fileOutputStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally{
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } 
    }
}
