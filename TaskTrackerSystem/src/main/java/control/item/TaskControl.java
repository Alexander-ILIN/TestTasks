package control.item;

import control.ApplicationControl;
import database.action.CreateItem;
import database.action.DeleteItem;
import database.action.ListItems;
import database.entity.Task;
import database.entity.Teammate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskControl
{
    //validate user input to list tasks created for specified projects by specified user
    private static final String PROJECT_ID_VALIDATION = ".+";
    // Menu messages
    private static final String CREATE_TASK_MESSAGE =
            "\nPlease, enter task name (at least 3 characters).\n" +
                    "0 - RETURN to creation menu";

    private static final String ASSIGN_DELETE_TASK_MESSAGE_1 =
            "\nPlease, enter task id.\n" +
                    "0 - RETURN to creation menu";

    private static final String ASSIGN_TASK_MESSAGE_2 =
            "\nPlease, enter teammate id.\n" +
                    "0 - RETURN to creation menu";

    private static final String TASK_CREATED_MESSAGE =
            "\nNew task created id = ";

    private static final String TASK_CREATION_FAIL_MESSAGE =
            "\nAttempt to create new task failed!";

    private static final String EMPTY_TASK_LIST =
            "\nNo tasks exist!";

    private static final String TASK_BY_PROJECT_USER_1 =
            "\nPlease, enter projects' id(s) separated by space.\n" +
            "0 - RETURN to list menu";

    private static final String TASK_BY_PROJECT_USER_2 =
            "\nPlease, enter user id.\n" +
            "0 - RETURN to list menu";

    private static final String INVALID_BOTH_ID_MESSAGE =
            "\nEntered not existing task id and teammate id!";

    private static final String INVALID_TASK_ID_MESSAGE =
            "\nEntered not existing task id!";

    private static final String INVALID_TEAMMATE_ID_MESSAGE =
            "\nEntered not existing teammate id!";

    private static final String INVALID_ID_COMBINATION_MESSAGE =
            "\nEntered task already assigned!\n" +
            "Please, enter another task / teammate combination";

    private static final String ASSIGNMENT_SUCCESS_MESSAGE =
            "\nTask successfully assigned";

    private static final String REMOVAL_SUCCESS_MESSAGE =
            "\nTask successfully deleted";

    private static final String NOT_VALID_ID =
            "\nEntered not valid project IDs\n" +
                    "Please, try again.";

    private static final String NOT_EXISTING_ID =
            "\nFollowing project id(s) are not existing:";

    private static final String TASKS_NOT_FOUND =
            "\nNo tasks found for entered combination of projects and user";

    // Enter task parameters and launch creation
    public static void selectCreateTask()
    {
        System.out.println(CREATE_TASK_MESSAGE);
        String taskName = ApplicationControl.validateInput(ApplicationControl.getNamesValidation());

        switch (taskName)
        {
            case "0":
                ApplicationControl.selectCreate();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidName());
                selectCreateTask();
                break;
            default:
                int taskId = CreateItem.createTask(taskName);
                if (taskId > 0)
                {
                    System.out.println(TASK_CREATED_MESSAGE + taskId);
                }
                else
                {
                    System.out.println(TASK_CREATION_FAIL_MESSAGE);
                }
                selectCreateTask();
                break;
        }
    }

    // Launch list tasks
    public static void selectAssignTask()
    {
        String teammateId;

        System.out.println(ASSIGN_DELETE_TASK_MESSAGE_1);
        String taskId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());

        switch (taskId)
        {
            case "0":
                ApplicationControl.selectCreate();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidId());
                selectAssignTask();
                break;
            default:
                System.out.println(ASSIGN_TASK_MESSAGE_2);

                // Get and validate teammate id
                teammateId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());
                switch (teammateId)
                {
                    case "0":
                        ApplicationControl.selectCreate();
                        break;
                    case "":
                        System.out.println(ApplicationControl.getNotValidId());
                        selectAssignTask();
                        break;
                    default:
                        assignmentResult(taskId, teammateId);
                        selectAssignTask();
                        break;
                }
                break;
        }
    }

    // Print messages depending on teammate creation result
    private static void assignmentResult (String taskId, String teammateId) {
        int result = CreateItem.assignTask(Integer.parseInt(taskId), Integer.parseInt(teammateId));
        switch (result) {
            case 0:
                System.out.println(INVALID_BOTH_ID_MESSAGE);
                break;
            case -1:
                System.out.println(INVALID_TASK_ID_MESSAGE);
                break;
            case -2:
                System.out.println(INVALID_TEAMMATE_ID_MESSAGE);
                break;
            case -3:
                System.out.println(INVALID_ID_COMBINATION_MESSAGE);
                break;
            default:
                System.out.println(ASSIGNMENT_SUCCESS_MESSAGE);
                break;
        }
    }

    // Launch list tasks
    public static void selectListTasks()
    {
        List<Task> taskList = (List<Task>) ListItems.listObjects(Task.class);
        List<Teammate> teammateList = (List<Teammate>) ListItems.listObjects(Teammate.class);

        if (taskList.size() ==0)
        {
            System.out.println(EMPTY_TASK_LIST);
        }
        else
        {
            Collections.sort(taskList, Comparator.comparing(Task::getName));

            System.out.printf("%n%-10s%-35s%-20s%-25s%-15s%n", "TASK ID", "TASK NAME (" + taskList.size() + " TASKS)", "USER NAME", "PROJECT NAME", "TEAMMATE ID");
            for(Task task : taskList)
            {
                if (task.getTeammate() != null)
                {
                    Teammate teammate = task.getTeammate();
                    System.out.printf("%-10d%-35s%-20s%-25s%-15d%n", task.getId(), task.getName(),teammate.getUser().getName(), teammate.getProject().getName(), teammate.getId());
                }
                else
                {
                    System.out.printf("%-10s%-35s%-20s%-25s%-15s%n", task.getId(), task.getName(),"***not assigned***", "***not assigned***", "n/a");
                }
            }

            for (Teammate teammate : teammateList)
            {
                if(teammate.getTaskList().size() == 0)
                {
                    System.out.printf("%-10s%-35s%-20s%-25s%-15d%n", "n/a", "***not assigned***",teammate.getUser().getName(), teammate.getProject().getName(), teammate.getId());
                }
            }

        }
        ApplicationControl.selectList();
    }


    // Enter task id and launch deletion
    public static void selectDeleteTask()
    {
        System.out.println(ASSIGN_DELETE_TASK_MESSAGE_1);
        String taskId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());

        switch (taskId)
        {
            case "0":
                ApplicationControl.selectDelete();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidId());
                selectDeleteTask();
                break;
            default:

                if(DeleteItem.deleteTask(Integer.parseInt(taskId)))
                {
                    System.out.println(REMOVAL_SUCCESS_MESSAGE);
                }
                else
                {
                    System.out.println(INVALID_TASK_ID_MESSAGE);
                }
                selectDeleteTask();
        }

    }

    public static void selectListByProjectsUser()
    {
        String userId;
        int [] projectsIdArray;

        System.out.println(TASK_BY_PROJECT_USER_1);
        String userInput = ApplicationControl.validateInput(PROJECT_ID_VALIDATION);

        if(createProjectsIdArray(userInput) != null)
        {
            projectsIdArray = createProjectsIdArray(userInput);
            if (projectsIdArray.length == 1 && projectsIdArray[0] == 0)
            {
                ApplicationControl.selectList();
            }
            else
            {
                if(isIdListValid(projectsIdArray)) // Validate project id list
                {
                    System.out.println(TASK_BY_PROJECT_USER_2);

                    // Get and validate user id
                    userId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());
                    switch (userId)
                    {
                        case "0":
                            ApplicationControl.selectList();
                            break;
                        case "":
                            System.out.println(ApplicationControl.getNotValidId());
                            selectListByProjectsUser();
                            break;
                        default:
                            listByProjectsUserResult(projectsIdArray, Integer.parseInt(userId));
                            selectListByProjectsUser();
                            break;
                    }
                }
                else
                {
                    selectListByProjectsUser();
                }
            }
        }
        else
        {
            System.out.println(NOT_VALID_ID);
            selectListByProjectsUser();
        }
    }

    private static int [] createProjectsIdArray (String projectsInput)
    {
        int [] projectsIdArray;

        String [] inputArray = (projectsInput.trim()).split("\\s+");
        projectsIdArray = new int[inputArray.length];

        boolean areNumbers = true;
        for (int i = 0; i < inputArray.length; i++)
        {
            String id = inputArray[i];
            if(id.matches(ApplicationControl.getIdValidation()))
            {
                projectsIdArray[i] = Integer.parseInt(inputArray[i]);
            }
            else
            {
                areNumbers = false;
                break;
            }
        }
        if(areNumbers)
        {
            return projectsIdArray;
        }
        else
        {
            return null;
        }
    }

    private static boolean isIdListValid (int[] projectId)
    {
        List<Integer> notValidIdList = ListItems.checkProjectId(projectId);
        if(notValidIdList.size() == 0)
        {
            return true;
        }
        else
        {
            System.out.println(NOT_EXISTING_ID);
            for (Integer id : notValidIdList)
            {
                System.out.println(id);
            }
            return false;
        }
    }

    // List all tasks created for specified projects by specified user
    private static void listByProjectsUserResult(int [] projectIdList, int userId)
    {
        List<Task> taskFiltered = new ArrayList<>();
        List<Teammate> teammateFiltered = new ArrayList<>();

        List<Teammate> teammateList = (List<Teammate>) ListItems.listObjects(Teammate.class);

        for (int i = 0; i < projectIdList.length; i++)
        {
            int id = projectIdList[i];
            List<Teammate> tempList = teammateList.stream().
                    filter(teammate -> teammate.getProject().getId() == id).
                    filter(teammate -> teammate.getUser().getId() == userId).collect(Collectors.toList());
            teammateFiltered.addAll(tempList);
        }

        for(Teammate teammate : teammateFiltered)
        {
            List<Task> taskList = teammate.getTaskList();
            for(Task task : taskList)
            {
                taskFiltered.add(task);
            }
        }

        if(taskFiltered.size() == 0)
        {
            System.out.println(TASKS_NOT_FOUND);
        }
        else
        {
            System.out.printf("%n%-15s%-25s%-15s%-20s%-15s%-30s%n", "PROJECT ID", "PROJECT NAME", "USER ID", "USER NAME", "TASK ID", "TASK NAME");
            for(Task task : taskFiltered)
            {
                System.out.printf("%-15s%-25s%-15s%-20s%-15s%-30s%n", task.getTeammate().getProject().getId(), task.getTeammate().getProject().getName(), task.getTeammate().getUser().getId(), task.getTeammate().getUser().getName(), task.getId(), task.getName());
            }
        }
    }
}
