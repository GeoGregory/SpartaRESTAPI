package com.sparta.api.spartarestapi.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("SpartaDatabase")
public class APIKeyEntity {

    @Id
    private String id;
    @Field("API_KEY")
    private String APIKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String API_KEY) {
        this.APIKey = API_KEY;
    }

    public APIKeyEntity() {
    }

    public APIKeyEntity(String id, String APIKey) {
        this.id = id;
        this.APIKey = APIKey;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
