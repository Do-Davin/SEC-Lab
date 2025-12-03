package com.example;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User model class representing a user entity.
 * This is the Model in the MVVM pattern.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "%s (%s), born on %s".formatted(getFullName(), email, birthDate);
    }
}