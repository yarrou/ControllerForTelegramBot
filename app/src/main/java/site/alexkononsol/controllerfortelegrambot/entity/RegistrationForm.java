package site.alexkononsol.controllerfortelegrambot.entity;

public class RegistrationForm {
    private final String userLogin;
    private final String password;
    private final String repeatPassword;

    public RegistrationForm(String userLogin, String password, String repeatPassword) {
        this.userLogin = userLogin;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

}