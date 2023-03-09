package Objects;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity
@Table(name="Account")
public class Account {
    @Id
    @Column(name = "email")
    @Getter
    @Setter
    private String email;

    @Column(name = "name", nullable = false, length = 128)
    @Getter
    @Setter
    private String name;

    @Column(name="password",nullable = false, length = 128)
    @Getter
    @Setter
    private String password;

    @OneToMany(mappedBy = "account")
    private List<Product> products;

    public Account() {
        super();
    }

    public Account(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String toString() {
        return "["+name+", "+email+"]";
    }

    public List<Product> getProducts() {
        return products;
    }
}
