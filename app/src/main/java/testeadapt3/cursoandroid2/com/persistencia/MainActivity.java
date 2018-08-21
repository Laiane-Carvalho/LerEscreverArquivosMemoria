package testeadapt3.cursoandroid2.com.persistencia;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.editTexto)
    EditText editText;

    @BindView(R.id.textTexto)
    TextView txtTexto;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.botaoSalvar)
    Button botaoSalvar;

    @BindView(R.id.botaoLer)
    Button botaoLer;

    public static final int PERMISSAO_SDCARD = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        ButterKnife.bind( this );

        botaoSalvar.setOnClickListener( this );
        botaoLer.setOnClickListener( this );

    }

    @Override
    public void onClick(View view) {
        boolean ler = false;
        if (view.getId() == R.id.botaoLer) {
            ler = true;
        }
        int tipo = radioGroup.getCheckedRadioButtonId();
        if (ler) {
            switch (tipo) {
                case R.id.rbMemoriaInterna:
                    carregarInterno();
                    break;
                case R.id.rbmemoriaExternaPrivado:
                    carregarDoSdCard( true );
                    break;
                case R.id.rbmemoriaExternaPublica:
                    carregarDoSdCard( false );
                    break;
            }
        } else {
            switch (tipo) {
                case R.id.rbMemoriaInterna:
                    salvarInterno();
                    break;
                case R.id.rbmemoriaExternaPrivado:
                    salvarNoSdCard( true );
                    break;
                case R.id.rbmemoriaExternaPublica:
                    salvarNoSdCard( false );
                    break;
            }
        }
    }

    private boolean checarPermissao(String permissao, int requestCode) {
        if (ActivityCompat.checkSelfPermission( getApplicationContext(), permissao ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale( this, permissao )) {
                toastMessage( "Você precisa habilitar essa permissão para conseguir salvar o arquivo!" );
            }
            ActivityCompat.requestPermissions( this, new String[]{permissao}, requestCode );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        switch (requestCode) {
            case PERMISSAO_SDCARD: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toastMessage( "Permissão concedida!" );
                } else {
                    toastMessage( "Permissão negada!" );
                }
            }
        }
    }

    private void salvarInterno() {
        try {
            FileOutputStream fos = openFileOutput( "arquivo.txt", Context.MODE_PRIVATE );//openFileOutPut nos retornara o FileOutPutStream para salvarmos o arquivo na memoria interna
            salvar( fos );
        } catch (IOException e) {
            e.printStackTrace();
            Log.e( "NGVL", "Erro ao salvar aquivo", e );
        }
    }

    private void carregarInterno() {
        try {
            FileInputStream fis = openFileInput( "arquivo.txt" ); //openFileInput ler arquivo da memoria interna
            carregar( fis );
        } catch (IOException e) {
            e.printStackTrace();
            Log.e( "NGVL", "Erro ao carregar arquivo", e );
        }
    }

    private void salvarNoSdCard(Boolean privado) {
        boolean terPermissao = checarPermissao(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PERMISSAO_SDCARD );
        if (!terPermissao) {
            return;
        }
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals( state )) { //verificamos se o SD esta montado e pronto para escrita
            File meuDir = getExternalDir( privado ); //estando pronto ...obtemos o diretorio
            try {
                if (!meuDir.exists()) { //diretorio  checamos se ele existe
                    meuDir.mkdir(); //se n existir criamos um novo diretorio
                }
                File arquivoTxt = new File( meuDir, "arquivo.txt" );//criamos um objeto File (arqivo),
                if (!arquivoTxt.exists()) { //checamos se o arquivo existe
                    arquivoTxt.createNewFile(); //se n existir o criaremos
                }
                FileOutputStream fos = new FileOutputStream( arquivoTxt );
                salvar( fos );
            } catch (IOException e) {
                e.printStackTrace();
                Log.e( "NGVL", "erro ao salvar aquivo" );
            }
        } else {
            Log.e( "NGVL", "Nao é possivel escrever no SD" );
        }
    }

    private File getExternalDir(Boolean privado) {
        if (privado) {
            //SDCard/Android/data/pacote.da.app/files
            return getExternalFilesDir( null );
        } else {
            //SDCard/DCIM
            return Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DCIM );
        }
    }

    private void carregarDoSdCard(Boolean privado) { //para ler o conteudo dos aquivos
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals( state ) || Environment.MEDIA_MOUNTED_READ_ONLY.equals( state )) { //verificamos se esta montado ao menos para leitura
            File meuDir = getExternalDir( privado );
            if (meuDir.exists()) { //verificamos se o diretorio e o arquivo que queremos ler existe
                File arquivoTxt = new File( meuDir, "arquivo.txt" );
                if (arquivoTxt.exists()) {
                    try {
                        arquivoTxt.createNewFile();//caso exista criamos o objeto FileInputStream.
                        FileInputStream fis = new FileInputStream( arquivoTxt );
                        carregar( fis ); //chamamos o metodo que vai carregar estes dados passando o objeto File
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e( "NGVL", "erro ao carregar arquivo " );
                    }
                }
            }
        } else {
            Log.e( "NGVL", "SD Card Indisponivel" );
        }
    }

    private void salvar(FileOutputStream fos) throws IOException {
        String[] linhas = TextUtils.split(
                editText.getText().toString(), "\n" );
        //ler o que foi escrito, e trazer o parametro de entrada
        PrintWriter writer = new PrintWriter( fos );
        for (String linha : linhas) {
            writer.println( linha );
        }
        writer.flush();//quando você invoca o flush, você diz que quer enviar todos os seu conteúdo naquele momento
        writer.close();
        fos.close();
    }

    private void carregar(FileInputStream fis) throws IOException {
        BufferedReader reader = new BufferedReader( //bufferedreader serve pra ler de entrada de dados, desde teclado, passando por arquivos e até sockets...
                new InputStreamReader( fis ) );
        StringBuilder sb = new StringBuilder(); //manipular strings,A classe StringBuilder serve para resolver quando forem adicionados varios objetos da classe String na memória. Você declara o objeto e vai fazendo append das novas strings, sem precisar alocar novos objetos.
        String linha;
        while ((linha = reader.readLine()) != null) {
            if (sb.length() != 0)
                sb.append( "\n" );
            sb.append( linha );
        }
        reader.close();
        fis.close();
        txtTexto.setText( sb.toString() );
    }

    private void toastMessage(String m) {
        Toast.makeText( getApplicationContext(), m, Toast.LENGTH_LONG ).show();
    }
}
