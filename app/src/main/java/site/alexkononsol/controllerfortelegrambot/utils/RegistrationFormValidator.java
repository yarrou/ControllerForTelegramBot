package site.alexkononsol.controllerfortelegrambot.utils;

import android.content.Context;
import android.content.res.Resources;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.entity.RegistrationForm;
import site.alexkononsol.controllerfortelegrambot.entity.result.AuthResult;

public class RegistrationFormValidator {
    public RegistrationFormValidator(Context context) {
        this.context = context;
    }

    private Context context;

    public  AuthResult regFormValidate(RegistrationForm form){
        AuthResult result = new AuthResult();
        if(!form.getPassword().equals(form.getRepeatPassword())){
            result.setStatus(400);
            result.setMessage(context.getString(R.string.warning_passwords_don_t_match));
        }else if(form.getPassword().trim().length()<5){
            result.setStatus(400);
            result.setMessage(context.getString(R.string.warning_password_is_too_short));
        }
        else {
            result.setStatus(100);
            result.setMessage("");
        }
        return result;
    }
}
