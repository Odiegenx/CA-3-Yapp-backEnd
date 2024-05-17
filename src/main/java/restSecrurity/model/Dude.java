package restSecrurity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dude implements iModel<Integer>{
    private Integer id;
    private String name;

    public Dude(String name) {
        this.name = name;
    }
    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Dude{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
