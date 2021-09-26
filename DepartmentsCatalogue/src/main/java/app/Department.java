package app;

import java.util.HashMap;
import java.util.Map;

//Класс используется для хранения информации о департаменте: имя и уровень
public class Department
{
    //Общий Map, содержащий все созданные департаменты в качестве значений и их имена в качестве ключей
    private static Map<String, Department> allDepartments = new HashMap<>();
    //Имя департамента
    private final String departmentName;
    //Уровень департамента
    private final int departmentLevel;

    public Department(String departmentName, int departmentLevel)
    {
        this.departmentName = departmentName;
        this.departmentLevel = departmentLevel;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public int getDepartmentLevel()
    {
        return departmentLevel;
    }

    //Добавление департамента в общий Map
    public static void addDepartment (Department department)
    {
        allDepartments.put(department.getDepartmentName(), department);
    }

    //Проверка существования департамента в общем Map
    public static boolean doesDepartmentExist (String DepartmentName)
    {
        return allDepartments.containsKey(DepartmentName);
    }
}
