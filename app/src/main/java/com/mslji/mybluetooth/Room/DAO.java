package com.mslji.mybluetooth.Room;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface DAO {

    // insert
    // इसमें class का object को pass करेगो तो ये automaticaly insert कर देगा।
    @Insert
    public void formInsertation(FormClass formClass);



    @Query("SELECT EXISTS(SELECT * FROM FormClass WHERE hospital_patient_id = :id)")
    Boolean isRowIsExist(int id);

    @Query("SELECT * FROM FormClass WHERE hospital_patient_id = :id")
    List<FormClass> getFormClass(int id);




    @Query("Delete from FormClass WHERE hospital_patient_id = :id")
    void deleteform(int id);
}

