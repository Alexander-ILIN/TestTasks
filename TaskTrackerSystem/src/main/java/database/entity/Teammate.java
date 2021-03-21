package database.entity;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "teammates")
public class Teammate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @ManyToOne (targetEntity = Project.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", referencedColumnName = "id")
    Project project;

    @ManyToOne (targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    User user;

    @OneToMany (mappedBy = "teammate", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Task> taskList;

    public Teammate()
    {
    }

    public Teammate(Project project, User user)
    {
        this.project = project;
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Task> getTaskList()
    {
        return taskList;
    }

    public void setTaskList(List<Task> taskList)
    {
        this.taskList = taskList;
    }

}
