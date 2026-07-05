package chen_zi_hao;

public class User {
    private int id;
    private String email;
    private String displayName;
    private String role;
    private String skills;
    private double rating;

    public User() {}

    public User(int id, String displayName, String role) {
        this.id = id;
        this.displayName = displayName;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
