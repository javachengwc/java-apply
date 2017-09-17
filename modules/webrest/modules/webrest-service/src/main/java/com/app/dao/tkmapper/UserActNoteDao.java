package com.app.dao.tkmapper;

import com.app.dao.IDao;
import com.app.entity.UserActNote;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserActNoteDao extends IDao<UserActNote> {

    @Select("select id,name,name_ch from user_act_note where name = #{name}")
    UserActNote queryByName(@Param("name") String name);

    @Update({ "update user_act_note set name_ch=#{act.nameCh} ",
            " where name=#{act.name}" })
    void uptByName(@Param("act") UserActNote userActNote);
}
