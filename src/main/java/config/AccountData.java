package config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountData {
    ACCOUNT_NUMBER_PREFIX("ACC");

    private final String value;
}
