package database.action;

import database.entity.Project;
import database.entity.Task;
import database.entity.Teammate;
import database.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;

// Create projects, users, tasks. Assign users on projects (create teammates) and tasks on users (teammates)
public class CreateItem
{
    // Create project return id
    public static int createProject(String projectName)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();

        Transaction transaction = databaseSession.beginTransaction();
        Project project = new Project(projectName);

        databaseSession.save(project);
        transaction.commit();
        databaseSession.close();

        return project.getId();
    }

    // Create user return id
    public static int createUser(String userName)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = databaseSession.beginTransaction();
        User user = new User(userName);

        databaseSession.save(user);
        transaction.commit();
        databaseSession.close();

        return user.getId();
    }

    // Create user return id
    public static int createTask (String taskName)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = databaseSession.beginTransaction();
        Task task = new Task(taskName);

        databaseSession.save(task);
        transaction.commit();
        databaseSession.close();

        return task.getId();
    }

    // Create teammate return teammate id in case of success or 0, -1, -2, -3 if entered not existing project id and user id
    public static int createTeammate (int projectId, int userId)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        Project project = databaseSession.get(Project.class, projectId);
        User user = databaseSession.get(User.class, userId);

        if (project == null && user == null)
        {
            databaseSession.close();
            return 0;
        }
        if (project == null)
        {
            databaseSession.close();
            return -1;
        }
        else if (user == null)
        {
            databaseSession.close();
            return -2;
        }
        else if (!isNewTeammateUnique(projectId, userId))
        {
            databaseSession.close();
            return -3;
        }
        else
        {
            Transaction transaction = databaseSession.beginTransaction();
            Teammate teammate = new Teammate(project, user);

            databaseSession.save(teammate);
            transaction.commit();
            databaseSession.close();
            return teammate.getId();
        }
    }
    // Check if new teammate is unique
    private static boolean isNewTeammateUnique (int projectId, int userId)
    {
        List<Teammate> teammateList = (List<Teammate>) ListItems.listObjects(Teammate.class);
        List<Teammate> projectTeammateList = teammateList.stream().
                filter(teammate -> teammate.getProject().getId() == projectId).collect(Collectors.toList());

        if(projectTeammateList.size() !=0)
        {
            Teammate foundTeammate = projectTeammateList.stream().
                    filter(teammate -> teammate.getUser().getId() == userId).findFirst().orElse(null);
            if(foundTeammate == null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    // Assign task to teammate return 1 in case of success 0, -1, -2, -3 in case of fail
    public static int assignTask (int taskId, int teammateId)
    {
        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();

        Task task = databaseSession.get(Task.class, taskId);
        Teammate teammate = databaseSession.get(Teammate.class, teammateId);

        if(task == null && teammate == null)
        {
            databaseSession.close();
            return 0;
        }
        else if (task == null)
        {
            databaseSession.close();
            return -1;
        }
        else if (teammate == null)
        {
            databaseSession.close();
            return -2;
        }
        else if (task.getTeammate() != null)
        {
            databaseSession.close();
            return -3;
        }
        else
        {
            Transaction transaction = databaseSession.beginTransaction();
            task.setTeammate(teammate);
            databaseSession.update(task);
            transaction.commit();
            databaseSession.close();

            return 1;
        }

    }
}
