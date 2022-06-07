package com.sasi.gwmaker.storage;

import com.sasi.gwmaker.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

public class StorageHandler {

    private String path = "F:\\output";

    private String EXTENSION = ".json";

    /**
     * Static method to save the contents of mbt model json into a json file.
     *
     * @param json
     *
     * The content of the mbt json model.
     *
     * @param filename
     *
     * Name of the json, this is typically the model name.
     *
     * @return
     *
     * Status of the file saving. False indicates a failure and vice versa.
     *
     */
    public boolean saveMbtJson(String json, String filename){
        /**
         * Check if the folder exists.
         */
        File storagePath = new File(path);
        System.out.println("Folder: " + storagePath.getAbsolutePath());
        if(!storagePath.exists()){
            /**
             * If the folder does not exist, create a folder.
             */
            storagePath.mkdir();
        }
        /**
         * Create the json file if it does not exist.
         */
        File mbtFile = new File(storagePath + "\\" + StringUtil.processFileName(filename) + EXTENSION);
        if(!mbtFile.exists()){
            try {
                mbtFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        /**
         * Write the contents of the mbt model to json file.
         */
        try (PrintStream out = new PrintStream(new FileOutputStream(mbtFile.getAbsolutePath()))) {
            out.print(json);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
