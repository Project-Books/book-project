package com.karankumar.bookproject.backend.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@JsonIgnoreProperties(value = {"id", "books"})
@EqualsAndHashCode(callSuper = true, exclude= "books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Publisher extends BaseEntity{
    
    @NotNull
    @NotEmpty
    private String name;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "publisher")
    @Setter
    @Getter
    private Set<Book> books = new HashSet<>();
    
    public Publisher(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    

}
