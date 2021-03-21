package control.item;

import control.ApplicationControl;
import database.action.CreateItem;
import database.action.CreateSessionFactory;
import database.action.DeleteItem;
import database.action.ListItems;
import database.entity.Project;
import database.entity.Task;
import database.entity.Teammate;
import database.entity.User;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

public class TeammateControl
{
    // Menu messages
    private static final String CREATE_TEAMMATE_MESSAGE_1 =
            "\nPlease, enter project id.\n" +
                    "0 - RETURN to creation menu";

    private static final String CREATE_TEAMMATE_MESSAGE_2 =
            "\nPlease, enter user id.\n" +
                    "0 - RETURN to creation menu";

    private static final String DELETE_TEAMMATE_MESSAGE =
            "\nPlease, enter teammate id.\n" +
                    "0 - RETURN to creation menu";

    private static final String INVALID_TEAMMATE_ID_MESSAGE =
            "\nEntered not existing teammate id!";

    private static final String INVALID_BOTH_ID_MESSAGE =
            "\nEntered not existing project id and user id!";

    private static final String INVALID_PROJECT_ID_MESSAGE =
            "\nEntered not existing project id!";

    private static final String INVALID_USER_ID_MESSAGE =
            "\nEntered not existing user id!";

    private static final String INVALID_ID_COMBINATION_MESSAGE =
            "\nEntered combination of project id and user id already exists!";

    private static final String TEAMMATE_CREATED_MESSAGE =
            "\nNew teammate created id = ";

    private static final String EMPTY_TEAMMATE_LIST =
            "\nNo teammates exist!";

    private static final String REMOVAL_SUCCESS_MESSAGE =
            "\nTeammate successfully deleted";

    private static final String CHILD_ELEMENTS_MESSAGE =
            "\nTeammate can't be deleted.\n" +
            "Please, delete child elements at first.";

    // Enter teammate parameters and launch creation
    public static void selectCreateTeammate()
    {
        String userId;

        System.out.println(CREATE_TEAMMATE_MESSAGE_1);
        String projectId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());

        switch (projectId)
        {
            case "0":
                ApplicationControl.selectCreate();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidId());
                selectCreateTeammate();
                break;
            default:
                System.out.println(CREATE_TEAMMATE_MESSAGE_2);

                // Get and validate user id
                userId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());
                switch (userId)
                {
                    case "0":
                        ApplicationControl.selectCreate();
                        break;
                    case "":
                        System.out.println(ApplicationControl.getNotValidId());
                        selectCreateTeammate();
                        break;
                    default:
                        teammateCreationResult(projectId, userId);
                        selectCreateTeammate();
                        break;
                }
                break;
        }
    }

    // Print messages depending on teammate creation result
    private static void teammateCreationResult (String projectId, String userId)
    {
        int teammateId = CreateItem.createTeammate(Integer.parseInt(projectId), Integer.parseInt(userId));
        switch (teammateId)
        {
            case 0:
                System.out.println(INVALID_BOTH_ID_MESSAGE);
                break;
            case -1:
                System.out.println(INVALID_PROJECT_ID_MESSAGE);
                break;
            case -2:
                System.out.println(INVALID_USER_ID_MESSAGE);
                break;
            case -3:
                System.out.println(INVALID_ID_COMBINATION_MESSAGE);
                break;
            default:
                System.out.println(TEAMMATE_CREATED_MESSAGE + teammateId);
                break;
        }
    }

    // Launch list teammates
    public static void selectListTeammates()
    {
        List<Teammate> teammateList = (List<Teammate>) ListItems.listObjects(Teammate.class);
        List<Project> projectList = (List<Project>) ListItems.listObjects(Project.class);
        List<User> userList = (List<User>) ListItems.listObjects(User.class);

        if (teammateList.size() ==0)
        {
            System.out.println(EMPTY_TEAMMATE_LIST);
        }
        else
        {
            Collections.sort(teammateList, (teammate, t1) ->
            {
                int firstLevel = (teammate.getUser().getName()).compareTo(t1.getUser().getName());
                if(firstLevel == 0)
                {
                    return (teammate.getProject().getName()).compareTo(t1.getProject().getName());
                }
                else
                {
                    return firstLevel;
                }
            });

            System.out.printf("%n%-15s%-25s%-25s%-15s%-15s%n", "TEAMMATE ID", "USER NAME (" + teammateList.size() + " TEAMMATES)", "PROJECT NAME", "USER ID", "PROJECT ID");
            for(Teammate teammate : teammateList)
            {
                System.out.printf("%-15d%-25s%-25s%-15d%-15d%n", teammate.getId(), teammate.getUser().getName(), teammate.getProject().getName(), teammate.getUser().getId(), teammate.getProject().getId());
            }
            for (Project project : projectList)
            {
                if(project.getTeammateList().size() ==0)
                {
                    System.out.printf("%-15s%-25s%-25s%-15s%-15d%n", "n/a", "***not assigned***", project.getName(), "n/a", project.getId());
                }
            }
            for (User user : userList)
            {
                if(user.getTeammateList().size() == 0)
                {
                    System.out.printf("%-15s%-25s%-25s%-15d%-15s%n", "n/a", user.getName(), "***not assigned***", user.getId(), "n/a");
                }
            }
        }
        ApplicationControl.selectList();
    }

    // Enter teammate id and launch deletion
    public static void selectDeleteTeammate()
    {
        System.out.println(DELETE_TEAMMATE_MESSAGE);

        String teammateId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());

        switch (teammateId)
        {
            case "0":
                ApplicationControl.selectDelete();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidId());
                selectDeleteTeammate();
                break;
            default:
                teammateDeletionResult(Integer.parseInt(teammateId));
                selectDeleteTeammate();
                break;
        }
    }

    private static void teammateDeletionResult (int teammateId)
    {
        int result = DeleteItem.deleteTeammate(teammateId);

        switch (result)
        {
            case -1:
                System.out.println(INVALID_TEAMMATE_ID_MESSAGE);
                break;
            case 1:
                System.out.println(REMOVAL_SUCCESS_MESSAGE);
                break;
            case 0:
                Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
                Teammate teammate = databaseSession.get(Teammate.class, teammateId);
                List<Task> taskList = teammate.getTaskList();
                databaseSession.close();

                System.out.println(CHILD_ELEMENTS_MESSAGE);
                System.out.printf("%-10s%-30s%n", "TASK ID", "TASK NAME");

                for(Task task : taskList)
                {
                    System.out.printf("%-10d%-30s%n", task.getId(), task.getName());
                }
                break;
        }
    }
}
