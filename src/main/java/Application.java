import java.io.IOException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {
        System.out.println("Введите название города:");

        Scanner scanner = new Scanner(System.in);
        String city = scanner.nextLine();
        String cityCode = RequestHandler.detectCityId(city);
        System.out.println("В городе " + city + ": ");
        String foreast = RequestHandler.getForecast(cityCode);


//        System.out.println(foreast);


    }
}
