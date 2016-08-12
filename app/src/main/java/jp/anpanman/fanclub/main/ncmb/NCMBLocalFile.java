package jp.anpanman.fanclub.main.ncmb;

import com.nifty.cloud.mb.core.NCMB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

class NCMBLocalFile {
    static final String FOLDER_NAME = "NCMB";

    NCMBLocalFile() {
    }

    static File create(String fileName) {
        return new File(NCMB.getCurrentContext().context.getDir("NCMB", 0), fileName);
    }

    static void writeFile(File writeFile, JSONObject fileData) {
        checkNCMBContext();

        try {
            FileOutputStream e = new FileOutputStream(writeFile);
            e.write(fileData.toString().getBytes("UTF-8"));
            e.close();
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    static JSONObject readFile(File readFile) {
        checkNCMBContext();
        new JSONObject();

        try {
            BufferedReader e = new BufferedReader(new FileReader(readFile));
            String information = e.readLine();
            e.close();
            JSONObject json = new JSONObject(information);
            return json;
        } catch (JSONException | IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    static void deleteFile(File deleteFile) {
        checkNCMBContext();
        deleteFile.delete();
    }

    static void checkNCMBContext() {
        if(NCMB.getCurrentContext() == null) {
            throw new RuntimeException("Please run the　NCMB.initialize.");
        } else if(NCMB.getCurrentContext().context == null) {
            throw new RuntimeException("NCMB.initialize context may not be null.");
        }
    }
}
