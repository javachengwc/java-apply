package com.boot3.model;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "uu实体类",name = "uu实体类")
public class Uu {

    @Schema(name = "id", example = "1")
    private String id;

    @Schema(name = "名称", example = "xx")
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
