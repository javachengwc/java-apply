package com.app.service;

import com.app.entity.UserActNote;

public interface UserActNoteService {

    public UserActNote getById(Integer id);

    public UserActNote queryByName(String name);

    public void uptByName(UserActNote userActNote);
}
