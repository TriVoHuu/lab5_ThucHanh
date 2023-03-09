package Objects;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name="Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private int id;

    @Column(name = "name", nullable = false, length = 128)
    @Getter
    @Setter
    private String name;

    @Column(name = "price")
    @Getter
    @Setter
    private double price;

    @ManyToOne
    @JoinColumn(name = "account_email")
    private Account account;

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return "["+id+", "+name+", "+price+"]";
    }
}
