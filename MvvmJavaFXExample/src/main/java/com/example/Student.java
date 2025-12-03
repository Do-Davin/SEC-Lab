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
        // return getFullName() + "\t\t\t" + email + "\t\t\t\t\t" + birthDate;
        return String.format("%-50s %-50s %s", getFullName(), email, birthDate.toString());
    }
}