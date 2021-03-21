import control.ApplicationControl;
import database.action.CreateSessionFactory;
import database.json.ExportJson;
import database.json.ImportJson;

public class TaskTrackerSystem
{
    private static String jsonFilePath = "src/main/resources/database.json";

    public static void main(String[] args)
    {
        try
        {
            // Create session factory
            CreateSessionFactory.create();

            // Import data from json file
            ImportJson.loadDatabaseFromFile(jsonFilePath);

            // Task tracker running
            ApplicationControl.runApplication();

            // Export data to json file
//            ExportJson.createDatabaseFile(jsonFilePath);

            CreateSessionFactory.close();
        }
        catch (Exception e)
        {
            CreateSessionFactory.close();
            System.out.println(e.getMessage());
        }
    }
}
