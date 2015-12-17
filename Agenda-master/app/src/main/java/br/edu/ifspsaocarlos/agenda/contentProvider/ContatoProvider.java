package br.edu.ifspsaocarlos.agenda.contentProvider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import br.edu.ifspsaocarlos.agenda.data.ContatoDataHelper;

/**
 * Created by agostta on 16/12/2015.
 */
public class ContatoProvider extends android.content.ContentProvider {

    public static final String AUTHORITY = "br.edu.ifspsaocarlos.sdm.agenda.contentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/contatos");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.br.edu.ifspsaocarlos.sdm.agenda.contato";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.br.edu.ifspsaocarlos.sdm.agenda.contatos";

    //URIs
    public static final int CONTATOS = 1;
    public static final int CONTATOS_BY_ID = 2;

    private static UriMatcher matcher;

    private SQLiteDatabase database;
    private ContatoDataHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ContatoDataHelper(getContext());

        if(matcher == null){
            matcher = new UriMatcher(UriMatcher.NO_MATCH);
            matcher.addURI(AUTHORITY, "contatos", CONTATOS);
            matcher.addURI(AUTHORITY, "contatos/#", CONTATOS_BY_ID);
        }

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor;

        switch (matcher.match(uri)){
            case CONTATOS:
                cursor = database.query(ContatoDataHelper.DATABASE_TABLE, projection, selection, selectionArgs,null, null, sortOrder);
                break;
            case CONTATOS_BY_ID:
                cursor = database.query(ContatoDataHelper.DATABASE_TABLE, projection, ContatoDataHelper.KEY_ID + "=" + uri.getLastPathSegment(), null, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("URI Not found!");
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case CONTATOS:
                return CONTENT_TYPE;
            case CONTATOS_BY_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("URI Not found!");
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        database = dbHelper.getWritableDatabase();
        int uriType = matcher.match(uri);
        long id;
        switch (uriType){
            case CONTATOS:
                id = database.insert(ContatoDataHelper.DATABASE_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("URI Not found!");
        }
        uri = ContentUris.withAppendedId(uri, id);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        database = dbHelper.getWritableDatabase();
        int uriType = matcher.match(uri);
        int count;
        switch (uriType) {
            case CONTATOS:
                count = database.delete(ContatoDataHelper.DATABASE_TABLE, selection, selectionArgs);
                break;
            case CONTATOS_BY_ID:
                count = database.delete(ContatoDataHelper.DATABASE_TABLE, ContatoDataHelper.KEY_ID + "=" + uri.getPathSegments().get(1), null);
                break;
            default:
                throw new IllegalArgumentException("URI Not found!");
        }
        database.close();
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        database = dbHelper.getWritableDatabase();
        int uriType = matcher.match(uri);
        int count;
        switch (uriType){
            case CONTATOS:
                count = database.update(ContatoDataHelper.DATABASE_TABLE, values, selection, selectionArgs);
                break;
            case CONTATOS_BY_ID:
                count = database.update(ContatoDataHelper.DATABASE_TABLE, values, ContatoDataHelper.KEY_ID + "=" +  uri.getPathSegments().get(1), null);
                break;
            default:
                throw new IllegalArgumentException("URI Not found!");
        }

        database.close();
        return count;
    }
}
