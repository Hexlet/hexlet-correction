package io.hexlet.hexletcorrection.persistence.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
public class CorrectionMessage {
    
    @Id
    @GeneratedValue(strategy = AUTO)
    @EqualsAndHashCode.Exclude
    private Long id;
    
    @Size(max = 255)
    @EqualsAndHashCode.Exclude
    private String comment;
    
    @NotNull
    @Size(max = 1000)
    @Column(nullable = false, length = 1000)
    private String highlightText;
    
    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String userName;
    
    @NotBlank
    @Size(max = 1000)
    @Column(nullable = false, length = 1000)
    private String pageURL;
}
