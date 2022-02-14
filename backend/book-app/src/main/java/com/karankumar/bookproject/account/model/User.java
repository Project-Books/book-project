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

package com.karankumar.bookproject.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import com.karankumar.bookproject.account.constraint.PasswordStrength;
import com.karankumar.bookproject.account.constraint.PasswordStrengthCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Represents a single User */
@Entity(name = "bp_user")
@Table(name = "bp_user")
@Builder
@Getter
@Setter
@JsonIgnoreProperties(value = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@ExcludeFromJacocoGeneratedReport
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
      message = "The email must conform to OWASP Validation Regex for email address")
  @NotBlank
  private String email;

  @NotBlank
  @PasswordStrengthCheck(PasswordStrength.STRONG)
  private String password;

  @NotNull private boolean active;

  private boolean locked;

  private int failedAttempts;

  private LocalDateTime lockTime;

  // Fetch type can be eager as there are not many roles
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinTable(
      name = "user_role",
      joinColumns =
          @JoinColumn(
              name = "user_id",
              referencedColumnName = "id",
              foreignKey = @ForeignKey(name = "user_role_user_id_fk")),
      inverseJoinColumns =
          @JoinColumn(
              name = "role_id",
              referencedColumnName = "id",
              foreignKey = @ForeignKey(name = "user_role_role_id_fk")))
  private Set<Role> roles = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return email.equals(user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}
