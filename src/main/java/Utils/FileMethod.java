package Utils;

import Models.Survey.Question;
import Models.Temp.CheckRequireQuestion;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileMethod {
    public static void saveFile(String directoryPath, String fileName, Object data){
        try{
            String filePath = directoryPath + fileName;
            File directory = new File(directoryPath);
            File file = new File(filePath);
            if(!directory.exists()&&!file.exists()){
                directory.mkdirs();
                file.createNewFile();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static void createDirectory(String directoryPath){
        File directory = new File(directoryPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
    }
    public void saveFileFromURL(String fileName, String fileUrl) throws MalformedURLException, IOException{
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;

        try { bufferedInputStream = new BufferedInputStream(new URL(fileUrl).openStream());
            fileOutputStream = new FileOutputStream(fileName);
            byte[] data = new byte[1024];
            int count;
            while ((count = bufferedInputStream .read(data, 0, 1024)) != -1) {
                fileOutputStream.write(data, 0, count);
                showInfoProcess();
            }
        }  finally {
            if ( bufferedInputStream != null)
                bufferedInputStream .close();
            if (fileOutputStream != null)
                fileOutputStream.close();
        }
    }
    public abstract void showInfoProcess();
    public void writeCheckData(CheckRequireQuestion checkRequireQuestion, Question question){

    }
}
