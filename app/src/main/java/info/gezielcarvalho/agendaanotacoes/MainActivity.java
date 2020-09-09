package info.gezielcarvalho.agendaanotacoes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private String FileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.FileName = getApplicationContext().getFilesDir().getPath() + "/";

        final EditText contentBox = findViewById(R.id.contentbox);
        final EditText fileNameToOpen = findViewById(R.id.txtNomeArquivo);
        final EditText stringtoSearch = findViewById(R.id.txt_palavra);
        Button btLimpar = findViewById(R.id.btlimpar);
        Button btSalvar = findViewById(R.id.btsalvar);
        Button btProcurar = findViewById(R.id.btprocurar);

        btLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentBox.setText("");
            }
        });

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileNameToOpen.getText().length() == 0){
                    Toast.makeText(MainActivity.this, "Nome do arquivo vazio", Toast.LENGTH_SHORT).show();
                }
                else{
                    String completeFN = FileName + fileNameToOpen.getText().toString();
                    Log.i("entrei-salvar", completeFN);
                    String content = contentBox.getText().toString();
                    MainActivity.this.gravaDadosArquivo(completeFN,content);
                }
            }
        });

        btProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String path = FileName;
                Log.d("Files", "Path: " + path);
                File directory = new File(path);
                File[] files = directory.listFiles();
                Log.d("Files", "Size: "+ files.length);

                Boolean encontrei = false;
                for (int i = 0; i < files.length; i++) {
                    String completeFN = FileName + files[i].getName();
                    if (procurarDadosArquivos(completeFN, stringtoSearch.getText().toString())) {
                        encontrei = true;
                        Log.d("Files", "FileName:" + files[i].getName());
                        String content = recuperaDadosArquivos(completeFN);
                        contentBox.setText(content);
                    }
                }
                if(!encontrei){
                    contentBox.setText("");
                    Toast.makeText(MainActivity.this, "Palavra-chave não encontrada!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void gravaDadosArquivo(String fileName, String data){

        try{

            OutputStreamWriter bufferSaida = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
            bufferSaida.write(data);
            bufferSaida.close();
            Toast.makeText(this, "Suas anotações foram salvas com sucesso", Toast.LENGTH_SHORT).show();
        }
        catch(FileNotFoundException e){
                Toast.makeText(this, "Falha na abertura do arquivo", Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e){
            Toast.makeText(this, "Falha na codificação", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            Toast.makeText(this, "Falha na escrita", Toast.LENGTH_SHORT).show();
        }
    }

    public String recuperaDadosArquivos(String fileName){

        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

            StringBuilder sb = new StringBuilder();

            String linha = bufferedReader.readLine();

            while(linha != null){
                sb.append(linha);
                sb.append("\n");
                linha = bufferedReader.readLine();
            }

            Toast.makeText(this, "Recuperado com sucesso!", Toast.LENGTH_SHORT).show();
            return sb.toString();
        }
        catch(FileNotFoundException e){
            Toast.makeText(this, "Falha na abertura do arquivo", Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e){
            Toast.makeText(this, "Falha na codificação", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            Toast.makeText(this, "Falha na leitura", Toast.LENGTH_SHORT).show();
        }

        return "";
    }

    public Boolean procurarDadosArquivos(String fileName, String texto){

        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

            StringBuilder sb = new StringBuilder();

            String linha = bufferedReader.readLine();

            while(linha != null){
                sb.append(linha);
                sb.append("\n");
                linha = bufferedReader.readLine();
            }

            //Toast.makeText(this, "Recuperado com sucesso!", Toast.LENGTH_SHORT).show();
            Log.i("entrei-aqui", sb.toString());
            int first_in = sb.toString().indexOf(texto);
            if(first_in >= 0){
                return true;
            }
            else{
                return false;
            }
        }
        catch(FileNotFoundException e){
            Toast.makeText(this, "Falha na abertura do arquivo", Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e){
            Toast.makeText(this, "Falha na codificação", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            Toast.makeText(this, "Falha na leitura", Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}