package com.openklaster.app.model.entities.user;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.crypto.Data;

@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Setter
    @Getter
    @Id
    String id;

    @Getter
    @Setter
    long seq;

    public DatabaseSequence() {
    }


}
