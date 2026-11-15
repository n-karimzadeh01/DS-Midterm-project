package models;

public class Account {

    private final String username; //unique
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String DEFAULT_ROLE = "User";

    public Account(String username, String password,String firstName,String lastName,String email){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = DEFAULT_ROLE;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {          //does not show the password cause its important information
        return "Account{" + "username:'" + username + '\'' + ", firstName:'" + firstName + '\'' + ", lastName:'" + lastName + '\'' + ", email:'" + email + '\'' + ", role:'" + role + '\'' + '}';
    }

}
