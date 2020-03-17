//import Models.Customer;
//import Models.User;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;

public class TestSleepMethod1 extends Thread{
    public void run() {
        for (int i = 1; i < 5; i++) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(i);
        }
    }

        public static void main(String[] args){
            TestSleepMethod1 t1=new TestSleepMethod1();
            TestSleepMethod1 t2=new TestSleepMethod1();

            t1.start();

        }

}