package com.jangam.schoolmgt.userservice.JSON.deseralize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.jangam.schoolmgt.userservice.model.Role;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Role> {
    @Override
    public Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String role = p.getText().toUpperCase();
        return Role.valueOf(role);
    }
}
