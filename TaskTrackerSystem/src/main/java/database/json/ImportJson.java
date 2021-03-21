package database.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import database.action.CreateSessionFactory;
import database.entity.Project;
import database.entity.Task;
import database.entity.Teammate;
import database.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.List;

public class ImportJson {

    // Imports database from JSON file
    public static void loadDatabaseFromFile (String filePath) throws Exception
    {
        try
        {
            String fileContent = getJsonFile(filePath);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode allJsonContent = mapper.readTree(fileContent);

            ArrayNode jsonProjectList = (ArrayNode) allJsonContent.get("projects");
            ArrayNode jsonUserList = (ArrayNode) allJsonContent.get("users");
            ArrayNode jsonTeammateList = (ArrayNode) allJsonContent.get("teammates");
            ArrayNode jsonTaskList = (ArrayNode) allJsonContent.get("tasks");

            putProjects(jsonProjectList);
            putUsers(jsonUserList);
            putTeammates(jsonTeammateList);
            putTasks(jsonTaskList);

            System.out.println("\nDatabase imported!\n");

        }
        catch (Exception e)
        {
            throw new Exception("\nImport from file failed!\n" + e.getMessage());
        }
    }

    // Get json file content
    private static String getJsonFile(String filePath) throws Exception
    {
        StringBuilder fileContent;

        File jsonFile = new File(filePath);

        if(!jsonFile.exists())
        {
            throw new FileNotFoundException("File not found!\n");
        }

        fileContent = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(jsonFile.toPath());
            lines.forEach(line -> fileContent.append(line));
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
        return fileContent.toString();
    }

    // Put projects into database
    private static void putProjects (ArrayNode jsonProjectList)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        for (JsonNode node : jsonProjectList)
        {
            Transaction transaction = databaseSession.beginTransaction();
            Project project = new Project();
            int id = Integer.parseInt(node.get("id").toString());
            String name = node.get("name").toString();
            project.setId(id);
            project.setName(name.replaceAll("\\\"", ""));
            databaseSession.save(project);
            transaction.commit();
        }
        databaseSession.close();
    }

    // Put users into database
    private static void putUsers (ArrayNode jsonUserList)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        for (JsonNode node : jsonUserList)
        {
            Transaction transaction = databaseSession.beginTransaction();
            User user = new User();
            int id = Integer.parseInt(node.get("id").toString());
            String name = node.get("name").toString();
            user.setId(id);
            user.setName(name.replaceAll("\\\"",""));
            databaseSession.save(user);
            transaction.commit();
        }
        databaseSession.close();
    }

    // Put teammates into database
    private static void putTeammates (ArrayNode jsonTeammateList)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        for (JsonNode node : jsonTeammateList)
        {
            Transaction transaction = databaseSession.beginTransaction();
            Teammate teammate = new Teammate();
            int id = Integer.parseInt(node.get("id").toString());
            int projectId = Integer.parseInt(node.get("projectId").toString());
            int userId = Integer.parseInt(node.get("userId").toString());
            Project project = databaseSession.get(Project.class, projectId);
            User user = databaseSession.get(User.class, userId);
            teammate.setId(id);
            teammate.setProject(project);
            teammate.setUser(user);
            databaseSession.save(teammate);
            transaction.commit();
        }
        databaseSession.close();
    }

    // Put tasks into database
    private static void putTasks (ArrayNode jsonTaskList)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        for (JsonNode node : jsonTaskList)
        {
            Transaction transaction = databaseSession.beginTransaction();
            Task task = new Task();
            int id = Integer.parseInt(node.get("id").toString());
            String name = node.get("name").toString();
            int teammateId = Integer.parseInt(node.get("teammateId").toString());
            task.setId(id);
            task.setName(name.replaceAll("\\\"",""));
            Teammate teammate = databaseSession.get(Teammate.class, teammateId);
            if (teammate != null)
            {
                task.setTeammate(teammate);
            }
            databaseSession.save(task);
            transaction.commit();
        }
        databaseSession.close();
    }
}
