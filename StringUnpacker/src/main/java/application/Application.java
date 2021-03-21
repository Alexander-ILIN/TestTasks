package application;

import java.util.List;
import java.util.Scanner;

public class Application
{
    //регулярное выражение для валидации символов, введённых пользователем
    private static final String ALLOWED_INPUT = "(((\\d+\\[)*)[a-zA-Z]+(\\]*))+|(0?)";
    //сообщение при возникновении исключения
    public static final String NOT_VALID_CHARS_MSG = "Введённая строка содержит недопустимые символы или недопустимое сочетание символов";

    public static void run()
    {
        //получение, валидация строки, введённой пользователем и запуск распаковки
        try
        {
            String inputString = userInput();

            //выход из приложения
            if(inputString.equals("0"))
            {
                return;
            }

            //запуск распаковки
            String unpackedString = launchUnpacking(inputString);

            //печать в консоль
            System.out.println("\n" + unpackedString);

            //повторный запуск данного метода
            run();
        }
        catch (Exception e)
        {
            //печать сообщения при возникновении исключения
            System.out.println(e.getMessage());
            //повторный запуск данного метода
            run();
        }
    }

    //получение и валидация строки, введённой пользователем
    public static String userInput() throws Exception
    {
        Scanner inputScanner = new Scanner(System.in);

        System.out.println("\nВведите строку в формате \"число[строка]\"\n" +
                "Допустимые символы: латинские буквы, числа и скобки []\n" +
                "0 - выход");

        //получение строки, введённой пользователем в консоль
        String inputString = inputScanner.nextLine();

        //валидация строки, введённой пользователем
        if(inputString.matches(ALLOWED_INPUT))
        {
            return inputString;
        }
        else
        {
            //исключение, если пользователь ввёл невалидные символы / сочетание символов
            throw new Exception(NOT_VALID_CHARS_MSG);
        }
    }

    //запуск распаковки строки
    public static String launchUnpacking (String inputString) throws Exception
    {
        //провека положения скобок и получение списка массивов целых чисел, содержащих инфомацию о подстроках, которые необходимо распаковать
        //при непройденной проверке возникает исключение
        List<Integer[][]> substringPositions = StringOperator.checkInput(inputString);
        //получение распакованной строки
        String unpackedString = StringOperator.unpackString(substringPositions, inputString);

        //результат - распакованная строка
        return unpackedString;
    }
}
