package database.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "users")
public class User
{
    @Id ()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column (name = "name", length = 30)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Teammate> teammateList;

    public User(String name)
    {
        this.name = name;
    }

    public User() {
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

    public List<Teammate> getTeammateList()
    {
        return teammateList;
    }

    public void setTeammateList(List<Teammate> teammateList)
    {
        this.teammateList = teammateList;
    }

}
