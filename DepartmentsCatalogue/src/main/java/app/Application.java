package app;

import java.util.List;
import java.util.Set;

//Класс используется для управления приложением
public class Application
{
    //Запуск чтения и валидации входных данных, затем создания иерархии и сортировки имён департаментов, затем вывода информации в консоль.
    public static void createDepartmentsHierarchy (String inputFilePath, String subDepartmentsSplitter) throws Exception
    {
        //Получение исходного списка строк
        List<String> inputStringList = FileReaderAndValidator.readFile(inputFilePath);

        Set<String> validInputStrings = FileReaderAndValidator.validateInputStrings(inputStringList, subDepartmentsSplitter);

        //Получение отсортированного списка департаментов
        List<String> sortedList = DepartmentsHierarchySorter.createSortedList(validInputStrings, subDepartmentsSplitter);

        //Печать исходного массива
        System.out.println("\nИсходный массив:");
        inputStringList.forEach(System.out::println);

        //Печать отсортированного массива
        System.out.println("\nОтсортированный массив:");
        sortedList.forEach(System.out::println);
    }

}
