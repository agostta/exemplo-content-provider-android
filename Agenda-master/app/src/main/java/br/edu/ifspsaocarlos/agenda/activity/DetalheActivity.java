package br.edu.ifspsaocarlos.agenda.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.contentProvider.ContatoProvider;
import br.edu.ifspsaocarlos.agenda.data.ContatoDataHelper;
import br.edu.ifspsaocarlos.agenda.model.Contato;

public class DetalheActivity extends AppCompatActivity {

    private Contato c;
    private ContatoProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("contact"))
        {

            this.c = (Contato) getIntent().getSerializableExtra("contact");
            EditText nameText = (EditText)findViewById(R.id.editText1);
            nameText.setText(c.getNome());

            EditText foneText = (EditText)findViewById(R.id.editText2);
            foneText.setText(c.getFone());

            EditText fone2Text = (EditText)findViewById(R.id.foneComercial);
            fone2Text.setText(c.getFone2());

            EditText emailText = (EditText)findViewById(R.id.editText3);
            emailText.setText(c.getEmail());

            EditText dataText = (EditText)findViewById(R.id.dataNascimento);
            dataText.setText(c.getDataNascimento());

            int pos =c.getNome().indexOf(" ");
            if (pos==-1)
                pos=c.getNome().length();

            setTitle(c.getNome().substring(0,pos));
        }

        provider = new ContatoProvider();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("contact"))
        {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                getContentResolver().delete(ContentUris.withAppendedId(ContatoProvider.CONTENT_URI, c.getId()), null, null);
                Toast.makeText(getApplicationContext(), "Removido com sucesso", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void salvar()
    {
        if (c==null) {
            getContentResolver().insert(ContatoProvider.CONTENT_URI, getContatoValues());
            Toast.makeText(this, "Incluído com sucesso", Toast.LENGTH_SHORT).show();
        }
        else {
            getContentResolver().update(ContentUris.withAppendedId(ContatoProvider.CONTENT_URI, c.getId()), getContatoValues(), null, null);
            Toast.makeText(this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
        }

        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    /**
     *
     * Retorna um ContentValues com os valores dos campos do contato na view
     *
     * @return
     */
    private ContentValues getContatoValues(){
        String name = ((EditText) findViewById(R.id.editText1)).getText().toString();
        String fone = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String fone2 = ((EditText) findViewById(R.id.foneComercial)).getText().toString();
        String email = ((EditText) findViewById(R.id.editText3)).getText().toString();
        String dtNascimento = ((EditText) findViewById(R.id.dataNascimento)).getText().toString();

        ContentValues values = new ContentValues();
        values.put(ContatoDataHelper.KEY_NAME, name);
        values.put(ContatoDataHelper.KEY_EMAIL, email);
        values.put(ContatoDataHelper.KEY_FONE, fone);
        values.put(ContatoDataHelper.KEY_FONE_2, fone2);
        values.put(ContatoDataHelper.KEY_DATA_NASCIMENTO, dtNascimento);

        return values;
    }

}
