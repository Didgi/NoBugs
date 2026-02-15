package practice7_exception_generics.exceptions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FirstTaskMain {
    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("data.txt"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден " + e.getMessage());
        }
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при закрытии файла " + e.getMessage());
        }
    }
}
