package com.mslji.mybluetooth.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


// सबसे पहले Entity notation देगे।
// table के सभी column को यहा define करेगे।
@Entity
public class FormClass {

    @PrimaryKey (autoGenerate = true)
    int formid;


    String hospital_patient_id;
    String scan_date;
    String scan_time;


    String record_id;
    String date_of_accident;
    String time_of_accident;


    String date_of_admission;
    String time_of_admission;
    String patient_name;



    String sex;
    String age;
    String skinColor;



    String hairdensity;
    String hairCoor;
    String method_of_reaching_hospitalrg;








    String type_of_accidentrbtn;

    String geographical_location_of_accident;





    String part_of_head_that_was_affected;
    String hospital_after_injury;






    String if_no_where_did_you_go_first;
    String date_of_symptoms;
    String symptoms;







    String gcs_score;
    String ct_scan_uploaded;
    String any_other_remarks;







    String blood_pressure;
    String is_a_high_blood_pressure_patient;
    String any_othere;







    String if_yes;
    String patient_Hair_Photo;
    String patient_Hair_Photo_Two;


    public FormClass(String hospital_patient_id, String scan_date, String scan_time, String record_id, String date_of_accident, String time_of_accident, String date_of_admission, String time_of_admission, String patient_name, String sex, String age, String skinColor, String hairdensity, String hairCoor, String method_of_reaching_hospitalrg, String type_of_accidentrbtn, String geographical_location_of_accident, String part_of_head_that_was_affected, String hospital_after_injury, String if_no_where_did_you_go_first, String date_of_symptoms, String symptoms, String gcs_score, String ct_scan_uploaded, String any_other_remarks, String blood_pressure, String is_a_high_blood_pressure_patient, String any_othere, String if_yes, String patient_Hair_Photo, String patient_Hair_Photo_Two) {
        this.hospital_patient_id = hospital_patient_id;
        this.scan_date = scan_date;
        this.scan_time = scan_time;
        this.record_id = record_id;
        this.date_of_accident = date_of_accident;
        this.time_of_accident = time_of_accident;
        this.date_of_admission = date_of_admission;
        this.time_of_admission = time_of_admission;
        this.patient_name = patient_name;
        this.sex = sex;
        this.age = age;
        this.skinColor = skinColor;
        this.hairdensity = hairdensity;
        this.hairCoor = hairCoor;
        this.method_of_reaching_hospitalrg = method_of_reaching_hospitalrg;
        this.type_of_accidentrbtn = type_of_accidentrbtn;
        this.geographical_location_of_accident = geographical_location_of_accident;
        this.part_of_head_that_was_affected = part_of_head_that_was_affected;
        this.hospital_after_injury = hospital_after_injury;
        this.if_no_where_did_you_go_first = if_no_where_did_you_go_first;
        this.date_of_symptoms = date_of_symptoms;
        this.symptoms = symptoms;
        this.gcs_score = gcs_score;
        this.ct_scan_uploaded = ct_scan_uploaded;
        this.any_other_remarks = any_other_remarks;
        this.blood_pressure = blood_pressure;
        this.is_a_high_blood_pressure_patient = is_a_high_blood_pressure_patient;
        this.any_othere = any_othere;
        this.if_yes = if_yes;
        this.patient_Hair_Photo = patient_Hair_Photo;
        this.patient_Hair_Photo_Two = patient_Hair_Photo_Two;
    }


    public String getHospital_patient_id() {
        return hospital_patient_id;
    }

    public void setHospital_patient_id(String hospital_patient_id) {
        this.hospital_patient_id = hospital_patient_id;
    }

    public String getScan_date() {
        return scan_date;
    }

    public void setScan_date(String scan_date) {
        this.scan_date = scan_date;
    }

    public String getScan_time() {
        return scan_time;
    }

    public void setScan_time(String scan_time) {
        this.scan_time = scan_time;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getDate_of_accident() {
        return date_of_accident;
    }

    public void setDate_of_accident(String date_of_accident) {
        this.date_of_accident = date_of_accident;
    }

    public String getTime_of_accident() {
        return time_of_accident;
    }

    public void setTime_of_accident(String time_of_accident) {
        this.time_of_accident = time_of_accident;
    }

    public String getDate_of_admission() {
        return date_of_admission;
    }

    public void setDate_of_admission(String date_of_admission) {
        this.date_of_admission = date_of_admission;
    }

    public String getTime_of_admission() {
        return time_of_admission;
    }

    public void setTime_of_admission(String time_of_admission) {
        this.time_of_admission = time_of_admission;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    public String getHairdensity() {
        return hairdensity;
    }

    public void setHairdensity(String hairdensity) {
        this.hairdensity = hairdensity;
    }

    public String getHairCoor() {
        return hairCoor;
    }

    public void setHairCoor(String hairCoor) {
        this.hairCoor = hairCoor;
    }

    public String getMethod_of_reaching_hospitalrg() {
        return method_of_reaching_hospitalrg;
    }

    public void setMethod_of_reaching_hospitalrg(String method_of_reaching_hospitalrg) {
        this.method_of_reaching_hospitalrg = method_of_reaching_hospitalrg;
    }

    public String getType_of_accidentrbtn() {
        return type_of_accidentrbtn;
    }

    public void setType_of_accidentrbtn(String type_of_accidentrbtn) {
        this.type_of_accidentrbtn = type_of_accidentrbtn;
    }

    public String getGeographical_location_of_accident() {
        return geographical_location_of_accident;
    }

    public void setGeographical_location_of_accident(String geographical_location_of_accident) {
        this.geographical_location_of_accident = geographical_location_of_accident;
    }

    public String getPart_of_head_that_was_affected() {
        return part_of_head_that_was_affected;
    }

    public void setPart_of_head_that_was_affected(String part_of_head_that_was_affected) {
        this.part_of_head_that_was_affected = part_of_head_that_was_affected;
    }

    public String getHospital_after_injury() {
        return hospital_after_injury;
    }

    public void setHospital_after_injury(String hospital_after_injury) {
        this.hospital_after_injury = hospital_after_injury;
    }

    public String getIf_no_where_did_you_go_first() {
        return if_no_where_did_you_go_first;
    }

    public void setIf_no_where_did_you_go_first(String if_no_where_did_you_go_first) {
        this.if_no_where_did_you_go_first = if_no_where_did_you_go_first;
    }

    public String getDate_of_symptoms() {
        return date_of_symptoms;
    }

    public void setDate_of_symptoms(String date_of_symptoms) {
        this.date_of_symptoms = date_of_symptoms;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getGcs_score() {
        return gcs_score;
    }

    public void setGcs_score(String gcs_score) {
        this.gcs_score = gcs_score;
    }

    public String getCt_scan_uploaded() {
        return ct_scan_uploaded;
    }

    public void setCt_scan_uploaded(String ct_scan_uploaded) {
        this.ct_scan_uploaded = ct_scan_uploaded;
    }

    public String getAny_other_remarks() {
        return any_other_remarks;
    }

    public void setAny_other_remarks(String any_other_remarks) {
        this.any_other_remarks = any_other_remarks;
    }

    public String getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(String blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public String getIs_a_high_blood_pressure_patient() {
        return is_a_high_blood_pressure_patient;
    }

    public void setIs_a_high_blood_pressure_patient(String is_a_high_blood_pressure_patient) {
        this.is_a_high_blood_pressure_patient = is_a_high_blood_pressure_patient;
    }

    public String getAny_othere() {
        return any_othere;
    }

    public void setAny_othere(String any_othere) {
        this.any_othere = any_othere;
    }

    public String getIf_yes() {
        return if_yes;
    }

    public void setIf_yes(String if_yes) {
        this.if_yes = if_yes;
    }

    public String getPatient_Hair_Photo() {
        return patient_Hair_Photo;
    }

    public void setPatient_Hair_Photo(String patient_Hair_Photo) {
        this.patient_Hair_Photo = patient_Hair_Photo;
    }

    public String getPatient_Hair_Photo_Two() {
        return patient_Hair_Photo_Two;
    }

    public void setPatient_Hair_Photo_Two(String patient_Hair_Photo_Two) {
        this.patient_Hair_Photo_Two = patient_Hair_Photo_Two;
    }
}
