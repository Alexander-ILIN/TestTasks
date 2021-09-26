package app;

import java.util.*;

//Класс используется для построения иерархии департаментов и сортировки их названий
public class DepartmentsHierarchySorter
{
    //Map, содержащий имена департаментов верхнего уровня в качестве ключей и списки входящих департаментов в качестве значений
    private static Map<String, Set<Department>> topLevelDepartments = new HashMap<>();

    //Получение кодов подразделений организации с последующей сортировкой
    public static List<String> createSortedList(Set<String> inputData, String splitter)
    {
        //Отсортированный список подразделений. Будет возвращён, как результат работы данного метода
        List<String> sortedDepartments = new ArrayList<>();

        //Регулярное выражение для разбивки строк входящего массива
        String splitRegex = String.valueOf("\\") + splitter;

        //Перебор элементов входящего массива строк
        for(String inputLine : inputData)
        {
            //Уровень текущего департамента
            int currentLevel = 0;

            StringJoiner fullDepartmentName = new StringJoiner(splitter);

            //Разбивка строк для получения кодов подразделений
            String[] departmentCodes = inputLine.split(splitRegex);

            //Имя верхнеуровневого департамента
            String topLevelDepartmentName = departmentCodes[0];

            //Генерация кодов входящих подразделений и их добаление в Map
            for(String departmentCode : departmentCodes)
            {
                ++currentLevel;
                fullDepartmentName.add(departmentCode);
                addDepartment(topLevelDepartmentName, fullDepartmentName.toString(), currentLevel);
            }
        }

        //Comparator для сравнения имён департаментов
        Comparator<Department> subDepartmentsComparator = (department, t1) -> {
            int levelComparison = Integer.compare(department.getDepartmentLevel(), t1.getDepartmentLevel());

            if (levelComparison != 0) {
                return levelComparison;
            } else {
                return t1.getDepartmentName().compareTo(department.getDepartmentName());
            }
        };

        //Сортировка имён департаментов и их добавление в arrayList
        topLevelDepartments.keySet().stream().sorted(Comparator.reverseOrder()).iterator().
                forEachRemaining(topDep -> topLevelDepartments.get(topDep).stream().sorted(subDepartmentsComparator).
                        map(dep -> dep.getDepartmentName()).forEachOrdered(name -> sortedDepartments.add(name)));

        return sortedDepartments;
    }

    //Проверка ниличия / добавления департамента в Map
    private static void addDepartment (String topLevelDepartmentName, String currentDepartmentName, int currentDepartmentLevel)
    {
        //Если департамент нет в общем Map
        if (!Department.doesDepartmentExist(currentDepartmentName))
        {
            //Создание и добавление департамента в общий Map
            Department departmentToAdd = new Department(currentDepartmentName, currentDepartmentLevel);
            Department.addDepartment(departmentToAdd);

            Set<Department> subDepartments;

            //Если департамент есть в Map со списком верхнеуровневых департаментов
            if(topLevelDepartments.containsKey(topLevelDepartmentName))
            {
                subDepartments = topLevelDepartments.get(topLevelDepartmentName);
            }
            else
            {
                subDepartments = new HashSet<>();
            }
            subDepartments.add(departmentToAdd);

            //Добавление / обновление информации в Map, содержащем верхнеуровневые департаменты
            topLevelDepartments.put(topLevelDepartmentName, subDepartments);
        }
    }
}
