package control.item;

import control.ApplicationControl;
import database.action.CreateItem;
import database.action.DeleteItem;
import database.action.ListItems;
import database.entity.Project;
import database.entity.Teammate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectControl
{
    // Menu messages
    private static final String CREATE_PROJECT_MESSAGE =
            "\nPlease, enter project name (at least 3 characters).\n" +
                    "0 - RETURN to creation menu";

    private static final String PROJECT_CREATED_MESSAGE =
            "\nNew project created id = ";

    private static final String PROJECT_CREATION_FAIL_MESSAGE =
            "\nAttempt to create new project failed!";

    private static final String EMPTY_PROJECT_LIST =
            "\nNo projects exist!";

    private static final String DELETE_PROJECT_MESSAGE =
            "\nPlease, enter project id.\n" +
                    "0 - RETURN to creation menu";

    private static final String REMOVAL_SUCCESS_MESSAGE =
            "\nProject successfully deleted";

    private static final String CHILD_ELEMENTS_MESSAGE =
            "\nProject can't be deleted.\n" +
            "Please, delete child elements at first.";

    private static final String INVALID_PROJECT_ID_MESSAGE =
            "\nEntered not existing project id!";

    // Enter project parameters and launch creation
    public static void selectCreateProject()
    {
        System.out.println(CREATE_PROJECT_MESSAGE);
        String projectName = ApplicationControl.validateInput(ApplicationControl.getNamesValidation());

        switch (projectName)
        {
            case "0":
                ApplicationControl.selectCreate();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidName());
                selectCreateProject();
                break;
            default:
                int projectId = CreateItem.createProject(projectName);
                if (projectId > 0)
                {
                    System.out.println(PROJECT_CREATED_MESSAGE + projectId);
                }
                else
                {
                    System.out.println(PROJECT_CREATION_FAIL_MESSAGE);
                }
                selectCreateProject();
                break;
        }

    }

    // Launch list projects
    public static void selectListProjects()
    {
        List<Project> projectList = (List<Project>) ListItems.listObjects(Project.class);

        if (projectList.size() ==0)
        {
            System.out.println(EMPTY_PROJECT_LIST);
        }
        else
        {
            Collections.sort(projectList, Comparator.comparing(Project::getName));

            System.out.printf("%n%-15s%-25s%n", "PROJECT ID", "PROJECT NAME (" + projectList.size() + " PROJECTS)");
            for(Project project : projectList)
            {
                System.out.printf("%-15d%-25s%n", project.getId(), project.getName());
            }
        }
        ApplicationControl.selectList();
    }

    // Enter project id and launch deletion
    public static void selectDeleteProject()
    {
        System.out.println(DELETE_PROJECT_MESSAGE);

        String projectId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());

        switch (projectId)
        {
            case "0":
                ApplicationControl.selectDelete();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidId());
                selectDeleteProject();
                break;
            default:
                projectDeletionResult(Integer.parseInt(projectId));
                selectDeleteProject();
                break;
        }
    }

    // Result of project deletion
    private static void projectDeletionResult (int projectId)
    {
        int result = DeleteItem.deleteProject(projectId);

        switch (result)
        {
            case -1:
                System.out.println(INVALID_PROJECT_ID_MESSAGE);
                break;
            case 1:
                System.out.println(REMOVAL_SUCCESS_MESSAGE);
                break;
            case 0:
                List<Teammate> teammateList = (List<Teammate>) ListItems.listObjects(Teammate.class);
                System.out.println(CHILD_ELEMENTS_MESSAGE);
                System.out.printf("%-15s%-35s%-30s%n", "TEAMMATE ID", "PROJECT NAME", "USER NAME");
                for(Teammate teammate : teammateList)
                {
                    if(teammate.getProject().getId() == projectId)
                    {
                        System.out.printf("%-15d%-35s%-30s%n", teammate.getId(), teammate.getProject().getName(), teammate.getUser().getName());
                    }
                }
                break;
        }
    }
}
