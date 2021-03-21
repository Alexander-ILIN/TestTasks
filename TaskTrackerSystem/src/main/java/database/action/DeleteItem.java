package database.action;

import database.entity.Project;
import database.entity.Task;
import database.entity.Teammate;
import database.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

// Delete projects, users, tasks and assignments of users on projects
public class DeleteItem
{
    // Delete teammate by id
    public static int deleteProject(int projectId)
    {
        int result;

        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        Project project = databaseSession.get(Project.class, projectId);

        if (project == null)
        {
            result = -1;
        }
        else if (project.getTeammateList().size() == 0)
        {
            Transaction transaction = databaseSession.beginTransaction();
            databaseSession.delete(project);
            transaction.commit();
            result = 1;
        }
        else
        {
            result = 0;
        }

        databaseSession.close();
        return result;
    }

    // Delete user by id
    public static int deleteUser(int userId)
    {
        int result;

        Session databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        User user = databaseSession.get(User.class, userId);

        if (user == null)
        {
            result = -1;
        }
        else if (user.getTeammateList().size() == 0)
        {
            Transaction transaction = databaseSession.beginTransaction();
            databaseSession.delete(user);
            transaction.commit();
            result = 1;
        }
        else
        {
            result = 0;
        }

        databaseSession.close();
        return result;

    }

    // Delete teammate by id
    public static int deleteTeammate(int teammateId)
    {
        int result;
        Transaction transaction;
        Session databaseSession;
        Teammate teammate;

        databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        teammate = databaseSession.get(Teammate.class, teammateId);

        if(teammate == null)
        {
            result = -1;
        }
        else if (teammate.getTaskList().size() == 0)
        {
            transaction = databaseSession.beginTransaction();
            teammate.setProject(null);
            teammate.setUser(null);
            databaseSession.save(teammate);
            transaction.commit();
            databaseSession.close();

            databaseSession = CreateSessionFactory.getSessionFactory().openSession();
            transaction = databaseSession.beginTransaction();
            databaseSession.delete(teammate);
            transaction.commit();
            result = 1;
        }
        else
        {
            result = 0;
        }

        databaseSession.close();
        return result;
    }

    // Delete task by id return true if success, false if not
    public static boolean deleteTask(int taskId)
    {
        boolean isRemoved;
        Session databaseSession;
        Task task;
        Transaction transaction;

        databaseSession = CreateSessionFactory.getSessionFactory().openSession();
        task = databaseSession.get(Task.class, taskId);

        if (task == null)
        {
            isRemoved = false;
        }
        else
        {
            if (task.getTeammate() != null)
            {
                transaction = databaseSession.beginTransaction();
                task.setTeammate(null);
                databaseSession.save(task);
                transaction.commit();
                databaseSession.close();

                databaseSession = CreateSessionFactory.getSessionFactory().openSession();
                task = databaseSession.get(Task.class, taskId);
            }
            transaction = databaseSession.beginTransaction();
            databaseSession.delete(task);
            transaction.commit();

            isRemoved = true;
        }
        databaseSession.close();

        return isRemoved;
    }
}
