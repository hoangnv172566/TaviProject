import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadFile {
    public static void main(String[] args) throws IOException {

//        String path = "https://znews-photo.zadn.vn/w1024/Uploaded/qhj_yvobvhfwbv/2018_07_18/Nguyen_Huy_Binh1.jpg";
//
//        try{
//            URL source = new URL(path);
//            System.out.println(source.getHost());
//            System.out.println(source.getFile());
//            HttpURLConnection httpURLConnection = (HttpURLConnection) source.openConnection();
//            httpURLConnection.setRequestMethod("HEAD");
//            InputStream in = source.openStream();
//
//
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String path = "F:\\Project\\TaviSRSProject\\src\\test\\java\\BossesAndEmployees.java";
        File file = new File("src/test/java/BossesAndEmployees.java");
        Desktop Des = Desktop.getDesktop();
        Des.open(file);

    }
}
