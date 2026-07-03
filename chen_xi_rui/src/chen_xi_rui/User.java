package chen_xi_rui;

/**
 * 简化版用户实体
 */
public class User {

    private int id;
    private String displayName;
    private String role;
    private double rating;

    public User() {
    }

    public User(int id, String displayName, String role, double rating) {
        this.id = id;
        this.displayName = displayName;
        this.role = role;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
