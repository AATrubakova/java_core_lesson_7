import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Введите название города:");

        Scanner scanner = new Scanner(System.in);
        String city = scanner.nextLine();
        String cityCode = RequestHandler.detectCityId(city);
        String foreast = RequestHandler.getForecast(cityCode, city);

    }
}
