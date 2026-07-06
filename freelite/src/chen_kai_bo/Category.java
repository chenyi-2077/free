package chen_kai_bo;
import chen_xi_rui.BidDao;
import chen_xi_rui.Bid;
import chen_yi_an.EscrowService;
import chen_zi_hao.Order;
import chen_zi_hao.OrderDao;

public class Category {
    private int id;
    private String name;
    public Category() {}
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
