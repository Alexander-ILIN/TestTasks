package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Класс осуществляет чтение строк из файла и их валидацию перед сортировкой
public class FileReaderAndValidator
{
    //Получение списка строк из файла
    public static List<String> readFile(String filePath) throws Exception
    {
        File inputFile = new File(filePath);

        if(!inputFile.exists())
        {
            throw new FileNotFoundException("File not found!\n");
        }

        return Files.readAllLines(inputFile.toPath());
    }

    //Валидация списка строк, полученного из файла
    public static Set<String> validateInputStrings (List<String> inputStrings, String subDepartmentsSplitter) throws Exception
    {
        //Set с уникальными валидными строками
        Set<String> validStrings = new HashSet<>();

        String splitterForRegex = String.valueOf("\\") + subDepartmentsSplitter;

        //Регулярное выражение для валидации строк
        String validationRegex = "((.[^" + splitterForRegex + "])+)((" + splitterForRegex + "(.[^" + splitterForRegex + "])+)*)";

        //Перебор списка строк, получнных из файла
        for(String inputString : inputStrings)
        {
            //Проверка на пустую строку
            if (!inputString.isBlank())
            {
                //Проверка строки с помощью регулярного выражения
                if(inputString.matches(validationRegex))
                {
                    validStrings.add(inputString);
                }
                else
                {
                    throw new Exception("Файл содержит строки в невалидном формате.");
                }
            }
        }

        if (validStrings.isEmpty())
        {
            throw new Exception("Файл не содержит валидных строк.");
        }

        return validStrings;
    }
}
