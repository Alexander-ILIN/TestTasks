package database.action;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class CreateSessionFactory
{
    private static final String CONFIGURATION_FILE = "hibernate.cfg.xml";
    private static SessionFactory sessionFactory;

    public static void create() throws Exception
    {
        StandardServiceRegistry registry;
        Metadata metadata;

        registry = new StandardServiceRegistryBuilder().configure(CONFIGURATION_FILE).build();
        metadata = new MetadataSources(registry).getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();

        if(sessionFactory == null)
        {
            throw new Exception("\nAttempt to connect to database failed!");
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public static void close()
    {
        sessionFactory.close();
    }


}
