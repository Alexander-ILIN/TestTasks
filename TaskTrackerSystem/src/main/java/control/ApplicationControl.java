package control;

import control.item.ProjectControl;
import control.item.TaskControl;
import control.item.TeammateControl;
import control.item.UserControl;

import java.util.Scanner;

public class ApplicationControl
{
    private static Scanner inputScanner;

    // User input validation
    private static final String LEVEL_0_VALIDATION = "[0-3]";
    private static final String CREATE_LIST_VALIDATION = "[0-5]";
    private static final String DELETE_VALIDATION = "[0-4]";
    // For projects, tasks and users
    private static final String NAMES_VALIDATION = "(.{3,})|[0]";
    // For teammates and tasks
    private static final String ID_VALIDATION = "\\d+";

    // Menu messages
    private static final String UNKNOWN_COMMAND =
            "\nUnknown command!\n" +
            "Please, try again.";

    // For projects, tasks and users
    private static final String NOT_VALID_NAME =
            "\nEntered not valid name!\n" +
            "Please, try again.";

    // For teammates and tasks
    private static final String NOT_VALID_ID =
            "\nEntered not valid id!\n" +
            "Please, try again.";

    private static final String EXIT_MESSAGE = "Good bye!";

    private static final String LEVEL_0_MESSAGE =
            "\nWelcome to task tracker system.\n" +
            "Please, enter command:\n" +
            "0 - EXIT\n" +
            "1 - CREATE task, user, project or project teammate\n" +
            "2 - LIST tasks, users, projects or project teammates\n" +
            "3 - DELETE task, user, project or project teammate";

    private static final String CREATE_MESSAGE =
            "\nPlease, enter command:\n" +
            "0 - RETURN to main menu\n" +
            "1 - CREATE project\n" +
            "2 - CREATE user\n" +
            "3 - CREATE teammate (assign user on project)\n" +
            "4 - CREATE task\n" +
            "5 - ASSIGN task on teammate";

    private static final String LIST_MESSAGE =
            "\nPlease, enter command:\n" +
            "0 - RETURN to main menu\n" +
            "1 - LIST projects\n" +
            "2 - LIST users\n" +
            "3 - LIST teammates\n" +
            "4 - LIST tasks\n" +
            "5 - LIST task created for specified projects by specified user";

    private static final String DELETE_MESSAGE =
            "\nPlease, enter command:\n" +
            "0 - RETURN to main menu\n" +
            "1 - DELETE project\n" +
            "2 - DELETE user\n" +
            "3 - DELETE teammate\n" +
            "4 - DELETE task";

    public static void runApplication ()
    {
        inputScanner = new Scanner(System.in);
        commandLevel0 ();
    }

    // Start menu, select action
    private static void commandLevel0 ()
    {
        System.out.println(LEVEL_0_MESSAGE);
        String inputString = validateInput(LEVEL_0_VALIDATION);

        switch (inputString)
        {
            case "0":
                System.out.println(EXIT_MESSAGE);
                break;
            case "1":
                selectCreate();
                break;
            case "2":
                selectList();
                break;
            case "3":
                selectDelete();
                break;
            default:
                System.out.println(UNKNOWN_COMMAND);
                commandLevel0();
                break;
        }

    }

    // Select which object should be created
    public static void selectCreate ()
    {
        System.out.println(CREATE_MESSAGE);
        String inputString = validateInput(CREATE_LIST_VALIDATION);

        switch (inputString)
        {
            case "0":
                commandLevel0();
                break;
            case "1":
                ProjectControl.selectCreateProject();
                break;
            case "2":
                UserControl.selectCreateUser();
                break;
            case "3":
                TeammateControl.selectCreateTeammate();
                break;
            case "4":
                TaskControl.selectCreateTask();
                break;
            case "5":
                TaskControl.selectAssignTask();
                break;
            default:
                System.out.println(UNKNOWN_COMMAND);
                selectCreate();
                break;
        }

    }

    // Select which object list should be printed
    public static void selectList ()
    {
        System.out.println(LIST_MESSAGE);
        String inputString = validateInput(CREATE_LIST_VALIDATION);

        switch (inputString)
        {
            case "0":
                commandLevel0();
                break;
            case "1":
                ProjectControl.selectListProjects();
                break;
            case "2":
                UserControl.selectListUsers();
                break;
            case "3":
                TeammateControl.selectListTeammates();
                break;
            case "4":
                TaskControl.selectListTasks();
                break;
            case "5":
                TaskControl.selectListByProjectsUser();
                break;
            default:
                System.out.println(UNKNOWN_COMMAND);
                selectList();
                break;
        }
    }

    // Select which object should be deleted
    public static void selectDelete ()
    {
        System.out.println(DELETE_MESSAGE);
        String inputString = validateInput(DELETE_VALIDATION);

        switch (inputString)
        {
            case "0":
                commandLevel0();
                break;
            case "1":
                ProjectControl.selectDeleteProject();
                break;
            case "2":
                UserControl.selectDeleteUser();
                break;
            case "3":
                TeammateControl.selectDeleteTeammate();
                break;
            case "4":
                TaskControl.selectDeleteTask();
                break;
            default:
                System.out.println(UNKNOWN_COMMAND);
                selectDelete();
                break;
        }
    }

    // User input validation
    public static String validateInput (String regex)
    {
        String inputString = inputScanner.nextLine();

        if(inputString.matches(regex))
        {
            return inputString;
        }
        else return "";
    }

    public static String getNamesValidation()
    {
        return NAMES_VALIDATION;
    }

    public static String getIdValidation()
    {
        return ID_VALIDATION;
    }

    public static String getNotValidName()
    {
        return NOT_VALID_NAME;
    }

    public static String getNotValidId()
    {
        return NOT_VALID_ID;
    }
}
