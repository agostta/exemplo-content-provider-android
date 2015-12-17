package br.edu.ifspsaocarlos.agenda.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class ContatoDataHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "agenda.db";
    public static final String DATABASE_TABLE = "contatos";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "nome";
    public static final String KEY_FONE = "fone";
    public static final String KEY_FONE_2 = "fone2";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DATA_NASCIMENTO = "data";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_FONE_2 + " TEXT, "  +
            KEY_DATA_NASCIMENTO + " TEXT, "  +
            KEY_EMAIL + " TEXT);";

    public ContatoDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int     newVersion) {
    }
}