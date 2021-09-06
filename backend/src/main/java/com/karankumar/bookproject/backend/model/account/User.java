/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.karankumar.bookproject.backend.constraints.PasswordStrength;
import com.karankumar.bookproject.backend.constraints.PasswordStrengthCheck;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * Represents a single User
 */
@Entity
@Builder
@Data
@JsonIgnoreProperties(value = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uidgenerator")
    @SequenceGenerator(name = "uidgenerator", sequenceName = "uid_sequence")
    @Getter
    @Setter(AccessLevel.NONE)
    private Long id;

    // For the RegExp see https://owasp.org/www-community/OWASP_Validation_Regex_Repository
    @Pattern(
            regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "The email must conform to OWASP Validation Regex for email address"
    )
    @NotBlank
    private String email;

    @NotBlank
    @PasswordStrengthCheck(PasswordStrength.STRONG)
    private String password;

    @NotNull
    private boolean active;

    // Fetch type can be eager as there are not many roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "user_role_user_id_fk")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "user_role_role_id_fk")
            )
    )
    private Set<Role> roles;
}
