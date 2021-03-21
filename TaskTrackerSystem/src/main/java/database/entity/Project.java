package database.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column (name = "name", length = 30)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Teammate> teammateList;

    public Project(String name)
    {
        this.name = name;
    }

    public Project()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Teammate> getTeammateList()
    {
        return teammateList;
    }

    public void setTeammateList(List<Teammate> teammateList)
    {
        this.teammateList = teammateList;
    }
}
