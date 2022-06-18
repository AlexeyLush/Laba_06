package interactive;

public enum InputType {

    REGISTER("Register"),
    LOGIN("Login"),
    EXIT("Exit"),
    REGISTER_ACCOUNT("Register_Account"),
    LOGIN_ACCOUNT("Login_Account"),
    GET_SALT("Get_Salt");

    private final String type;

    public String getType(){
        return this.type;
    }

    InputType(String type) {
        this.type = type;
    }
}
