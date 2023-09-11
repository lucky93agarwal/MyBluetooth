package com.mslji.mybluetooth.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mslji.mybluetooth.Model.GetAllData;

import java.util.ArrayList;

public class MyDbHandler extends SQLiteOpenHelper {
    int total = 0;
    // 1. आप को jetne bhi culom bnane hai Unke variable declear krke chalte hai
    private static String colId = "id";
    private static String pasentid = "pasent_id";
    private static String pasent_num = "pasent_num";
    private static String pasent_date = "pasent_date";
    private static String alldata = "alldata";


    private static String firstTable = "first_table";

    private static String secTable = "sec_table";




    // 1. इसमें हमें context pass करना होगा
    // 2. database का नाम।
    // 3. Cursorfactory में null pass करना होगा।
    // 4. database का version देना होगा।
    // जैसे आप ये सब information pass करेगे data base create कर देगा।
    public MyDbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 1. onCreate method के आन्दर database create करने का code लिखना है।
    // 2. SQLiteDatabase एक class है।
    // 3. SQLiteDatabase class create database को reprjent करती है।

    @Override
    public void onCreate(SQLiteDatabase db) {
        // जैसे आप का oncreate method चलेगा तो हम table को create करेगे।

        String create_user = "CREATE TABLE "+firstTable+"("+colId+" INTEGER PRIMARY KEY,"+ pasentid+" TEXT NOT NULL,"+pasent_num+" TEXT NOT NULL,"+pasent_date+" TEXT NOT NULL,"+alldata+" TEXT NOT NULL)";


        String create_data = "CREATE TABLE "+secTable+"("+colId+" INTEGER PRIMARY KEY,"+ alldata+" TEXT)";
        db.execSQL(create_data);
        db.execSQL(create_user);
    }
    // 1. onUpgrade method के अन्दर अगर आप को database को upgrade करने का code लिखना होगा।
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }







    public boolean checkrow(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM first_table WHERE productid=?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {id});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found


            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    //// check row present or not



    public int firstqtyprice(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(qty) as Total FROM first_table",null);

        if (cursor.moveToFirst()){
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }


        return total;
    }



    public int firsttotalprice(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(totalprice) as Total FROM first_table",null);

        if (cursor.moveToFirst()){
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }


        return total;
    }

    public int insertUser(FirstTableData user){
        int i=0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(pasentid, user.getPasentid());
        contentValues.put(pasent_num,user.getPasentnum());
        contentValues.put(pasent_date,user.getPasentdate());
        contentValues.put(alldata, user.getAlldata());

        sqLiteDatabase.insert(firstTable,null,contentValues);
        i = 1;
        return i;
    }



    public int insertAllData(GetAllData user){
        int i=0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(alldata,user.getData());


        sqLiteDatabase.insert(secTable,null,contentValues);
        i = 1;
        return i;
    }






    //// data को delete करने के लिए
    /// आप को जिस user का data delete करना है उसकी id pass करेगे।
    public int deleteAll(){
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();



        sqLiteDatabase.execSQL("delete from "+ firstTable);
        sqLiteDatabase.close();




        i =1;
        return i;
    }

    //// data को delete करने के लिए
    /// आप को जिस user का data delete करना है उसकी id pass करेगे।
    public void deleteAlldata(){



        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.execSQL("delete from "+ secTable);



    }




    //// data को delete करने के लिए
    /// आप को जिस user का data delete करना है उसकी id pass करेगे।
    public int deleteUser(String id){
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();



        sqLiteDatabase.delete(firstTable, pasentid+"=?", new String[]{id});
        sqLiteDatabase.close();




        i =1;
        return i;
    }




    /// इसमें जिस user की details देखनी है उसकी id को pass ना करा कर एक Collection return करा सकते है।
    // तो हम एक Array List बनायेगे
    // और Array List को Genric बनादेते है।
    public ArrayList<FirstTableData> viewUser(){

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM "+firstTable;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);

        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<FirstTableData> arrayList = new ArrayList<FirstTableData>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if(cursor.moveToFirst()){

            do {
                FirstTableData user = new FirstTableData();
                user.setId(cursor.getString(0));
                user.setPasentid(cursor.getString(1));
                user.setPasentnum(cursor.getString(2));
                user.setPasentdate(cursor.getString(3));
                user.setAlldata(cursor.getString(4));
                arrayList.add(user);
            }while (cursor.moveToNext());


        }





        return arrayList;
    }








    public ArrayList<GetAllData> viewAllDataUser(){

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM "+secTable+" ORDER BY alldata DESC LIMIT 1";

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);

        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetAllData> arrayList = new ArrayList<GetAllData>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if(cursor.moveToFirst()){

            do {
                GetAllData user = new GetAllData();
                user.setId(cursor.getString(0));
                user.setData(cursor.getString(1));
                arrayList.add(user);
            }while (cursor.moveToNext());


        }





        return arrayList;
    }

    public boolean updateUser(FirstTableData user){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(pasentid,user.getPasentid());
        contentValues.put(pasent_num,user.getPasentnum());
        contentValues.put(pasent_date,user.getPasentnum());
        contentValues.put(alldata,user.getAlldata());
        sqLiteDatabase.update(firstTable,contentValues,"productid=?",new String[]{user.getId()});
        return true;
    }

}
