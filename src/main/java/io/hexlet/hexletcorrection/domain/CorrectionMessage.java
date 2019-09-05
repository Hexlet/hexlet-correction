package io.hexlet.hexletcorrection.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CorrectionMessage {
    
    @Id
    @EqualsAndHashCode.Exclude
    private String id;
    
    @EqualsAndHashCode.Exclude
    private String comment;
    
    private String highlightText;
    
    private String username;
    
    private String pageURL;
}