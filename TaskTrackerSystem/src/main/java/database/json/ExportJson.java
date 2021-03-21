package database.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.action.ListItems;
import database.entity.Project;
import database.entity.Task;
import database.entity.Teammate;
import database.entity.User;

import java.io.*;
import java.util.List;


public class ExportJson
{
    // Export database to json file
    public  static void createDatabaseFile (String filePath) throws Exception
    {
        File jsonFile;
        ArrayNode jsonProjectList;
        ArrayNode jsonUserList;
        ArrayNode jsonTeammateList;
        ArrayNode jsonTaskList;

        try
        {
            jsonFile = createNewJsonFile(filePath);

            jsonProjectList = createJsonProjectList();
            jsonUserList = createJsonUserList();
            jsonTeammateList = createJsonTeammateList();
            jsonTaskList = createJsonTaskList();

            putInfoIntoFileJackson(jsonFile, jsonProjectList, jsonUserList, jsonTeammateList, jsonTaskList);
        }
        catch (Exception e)
        {
            throw new Exception("\nExport to file failed!\n" + e.getMessage());
        }
    }

    // Create new file
    private static File createNewJsonFile (String filePath) throws Exception
    {
        File jsonFile;

        jsonFile = new File(filePath);

        if(jsonFile.exists())
        {
            jsonFile.delete();
        }
        try
        {
            jsonFile.createNewFile();
        }
        catch (IOException e)
        {
            throw new Exception(e.getMessage());
        }

        return jsonFile;
    }

    // Create json array node for project list
    private static ArrayNode createJsonProjectList ()
    {
        ArrayNode jsonProjectList;
        ObjectMapper objectMapper = new ObjectMapper();

        List<Project> projectList = (List<Project>) ListItems.listObjects(Project.class);

        jsonProjectList = objectMapper.createArrayNode();

        for (Project project : projectList)
        {
            ObjectNode projectObject = objectMapper.createObjectNode();
            projectObject.put("id", project.getId());
            projectObject.put("name", project.getName());
            jsonProjectList.add(projectObject);
        }

        return jsonProjectList;
    }

    // Create json array node for user list
    private static ArrayNode createJsonUserList ()
    {
        ArrayNode jsonUserList;
        ObjectMapper objectMapper = new ObjectMapper();

        List<User> userList = (List<User>) ListItems.listObjects(User.class);

        jsonUserList = objectMapper.createArrayNode();

        for (User user : userList)
        {
            ObjectNode userObject = objectMapper.createObjectNode();
            userObject.put("id", user.getId());
            userObject.put("name", user.getName());
            jsonUserList.add(userObject);
        }

        return jsonUserList;
    }

    // Create json array node for teammate list
    private static ArrayNode createJsonTeammateList ()
    {
        ArrayNode jsonTeammateList;
        ObjectMapper objectMapper = new ObjectMapper();

        List<Teammate> teammateList = (List<Teammate>) ListItems.listObjects(Teammate.class);

        jsonTeammateList = objectMapper.createArrayNode();

        for (Teammate teammate : teammateList)
        {
            ObjectNode teammateObject = objectMapper.createObjectNode();
            teammateObject.put("id", teammate.getId());
            teammateObject.put("projectId", teammate.getProject().getId());
            teammateObject.put("userId", teammate.getUser().getId());
            jsonTeammateList.add(teammateObject);
        }

        return jsonTeammateList;
    }

    // Create json array node for task list
    private static ArrayNode createJsonTaskList ()
    {
        ArrayNode jsonTaskList;
        ObjectMapper objectMapper = new ObjectMapper();

        List<Task> taskList = (List<Task>) ListItems.listObjects(Task.class);

        jsonTaskList = objectMapper.createArrayNode();

        for (Task task : taskList)
        {
            ObjectNode taskObject = objectMapper.createObjectNode();
            taskObject.put("id", task.getId());
            taskObject.put("name", task.getName());
            Teammate teammate = task.getTeammate();
            if (teammate == null)
            {
                taskObject.put("teammateId", 0);
            }
            else
            {
                taskObject.put("teammateId", task.getTeammate().getId());
            }
            jsonTaskList.add(taskObject);
        }

        return jsonTaskList;
    }

    // Create POJO and put them to file
    private static void putInfoIntoFileJackson (File jsonFile, ArrayNode jsonProjectList, ArrayNode jsonUserList, ArrayNode jsonTeammateList, ArrayNode jsonTaskList) throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObjectToPut = objectMapper.createObjectNode();

        jsonObjectToPut.putPOJO("projects", jsonProjectList);
        jsonObjectToPut.putPOJO("users", jsonUserList);
        jsonObjectToPut.putPOJO("teammates", jsonTeammateList);
        jsonObjectToPut.putPOJO("tasks", jsonTaskList);

        try
        {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, jsonObjectToPut);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

}
