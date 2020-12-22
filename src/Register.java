public class Register {
    
    private String fullName;
    private String username;
    private String password;
    private String email;

    public boolean  loginMessage(int temp){
        if(temp == 1)
        return false;
        else return true;
    }

    public Register(String fullName, String username, String email, String password) {

        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
