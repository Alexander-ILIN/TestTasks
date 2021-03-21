package database.entity;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private int id;

    @Column (name = "name", length = 30)
    private String name;

    @ManyToOne (targetEntity = Teammate.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "teammateId", referencedColumnName = "id")
    Teammate teammate;

    public Task()
    {
    }

    public Task(String name)
    {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teammate getTeammate() {
        return teammate;
    }

    public void setTeammate(Teammate teammate)
    {
        this.teammate = teammate;
    }

}
