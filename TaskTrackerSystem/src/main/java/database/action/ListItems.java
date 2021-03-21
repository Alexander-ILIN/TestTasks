package database.action;

import database.entity.Project;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class ListItems
{
    // Return objects list
    public static List<?> listObjects (Class objectClass)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        String command = "FROM " + objectClass.getSimpleName();
        List<?> list = databaseSession.createQuery(command, objectClass).getResultList();
        databaseSession.close();

        return list;
    }


    // Check if all project ids from list exist in database
    public static List<Integer> checkProjectId (int[] projectId)
    {
        List<Integer> notValidId = new ArrayList<>();

        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        for (int i = 0; i < projectId.length; i++)
        {
            Project project = databaseSession.get(Project.class, projectId[i]);
            if(project == null)
            {
                notValidId.add(projectId[i]);
            }
        }
        databaseSession.close();
        return notValidId;
    }




}
