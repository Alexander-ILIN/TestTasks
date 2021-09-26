import app.Application;

public class DepartmentsCatalogue
{
    //Путь к файлу с исходными данными
    private static final String INPUT_FILE_PATH = "src/main/resources/inputText.txt";

    //Символ разделителя между кодами департаментов
    private static final String SUB_DEPARTMENTS_SPLITTER = "\\";


    public static void main(String[] args)
    {
        try
        {
            Application.createDepartmentsHierarchy(INPUT_FILE_PATH, SUB_DEPARTMENTS_SPLITTER);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            System.out.println("\nРабота приложения завершена...");
        }
    }
}
