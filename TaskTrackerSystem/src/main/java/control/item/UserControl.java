package control.item;

import control.ApplicationControl;
import database.action.CreateItem;
import database.action.DeleteItem;
import database.action.ListItems;
import database.entity.Teammate;
import database.entity.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserControl
{
    // Menu messages
    private static final String CREATE_USER_MESSAGE =
            "\nPlease, enter user name (at least 3 characters).\n" +
                    "0 - RETURN to creation menu";

    private static final String USER_CREATED_MESSAGE =
            "\nNew user created id = ";

    private static final String USER_CREATION_FAIL_MESSAGE =
            "\n\"Attempt to create new user failed!\"";

    private static final String EMPTY_USER_LIST =
            "\nNo users exist!";

    private static final String DELETE_USER_MESSAGE =
            "\nPlease, enter user id.\n" +
                    "0 - RETURN to creation menu";

    private static final String REMOVAL_SUCCESS_MESSAGE =
            "\nUser successfully deleted";

    private static final String CHILD_ELEMENTS_MESSAGE =
            "\nUser can't be deleted.\n" +
            "Please, delete child elements at first.";

    private static final String INVALID_USER_ID_MESSAGE =
            "\nEntered not existing user id!";

    // Enter user parameters and launch creation
    public static void selectCreateUser()
    {
        System.out.println(CREATE_USER_MESSAGE);
        String userName = ApplicationControl.validateInput(ApplicationControl.getNamesValidation());

        switch (userName)
        {
            case "0":
                ApplicationControl.selectCreate();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidName());
                selectCreateUser();
                break;
            default:
                int userId = CreateItem.createUser(userName);
                if (userId > 0)
                {
                    System.out.println(USER_CREATED_MESSAGE + userId);
                }
                else
                {
                    System.out.println(USER_CREATION_FAIL_MESSAGE);
                }
                selectCreateUser();
                break;
        }
    }

    // Launch list users
    public static void selectListUsers()
    {
        List<User> userList = (List<User>) ListItems.listObjects(User.class);

        if (userList.size() ==0)
        {
            System.out.println(EMPTY_USER_LIST);
        }
        else
        {
            Collections.sort(userList, Comparator.comparing(User::getName));

            System.out.printf("%n%-10s%-30s%n", "USER ID", "USER NAME (" + userList.size() + " USERS)");
            for(User user : userList)
            {
                System.out.printf("%-10d%-30s%n", user.getId(), user.getName());
            }
        }
        ApplicationControl.selectList();
    }

    // Enter user id and launch deletion
    public static void selectDeleteUser()
    {
        System.out.println(DELETE_USER_MESSAGE);

        String userId = ApplicationControl.validateInput(ApplicationControl.getIdValidation());

        switch (userId)
        {
            case "0":
                ApplicationControl.selectDelete();
                break;
            case "":
                System.out.println(ApplicationControl.getNotValidId());
                selectDeleteUser();
                break;
            default:
                userDeletionResult(Integer.parseInt(userId));
                selectDeleteUser();
                break;
        }
    }

    // Result of user deletion
    private static void userDeletionResult (int userId)
    {
        int result = DeleteItem.deleteUser(userId);

        switch (result)
        {
            case -1:
                System.out.println(INVALID_USER_ID_MESSAGE);
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
                    if(teammate.getUser().getId() == userId)
                    {
                        System.out.printf("%-15d%-35s%-30s%n", teammate.getId(), teammate.getProject().getName(), teammate.getUser().getName());
                    }
                }
                break;
        }
    }
}
