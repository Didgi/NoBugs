package common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AdminSession {

    //количество создаваемых пользователей
    int amountUsers() default 1;
    //номер пользователя чей токен будет помещён в localStorage
    int mainUserNumberToPutInStorage() default 1;
}
