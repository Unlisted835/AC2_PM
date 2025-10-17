package com.example.ac2_pw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText txtTitulo, txtDiretor, txtGenero;
    private RatingBar rtbNota;
    private Button btnSave;
    private CheckBox chkJaViuNoCinema;
    private EditText numAnoDeLancamento;
    private RecyclerView rcvListaFilmes;
    private Filme filmeAtual = new Filme();
    private List<Filme> filmes = new ArrayList<>();
    private FilmeAdapter adapter;
    private Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new Database();
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDiretor = findViewById(R.id.txtDiretor);
        txtGenero = findViewById(R.id.txtGenero);
        rtbNota = findViewById(R.id.rtbNota);
        btnSave = findViewById(R.id.btnSave);
        chkJaViuNoCinema = findViewById(R.id.chkJaViuNoCinema);
        numAnoDeLancamento = findViewById(R.id.numAnoDeLancamento);
        rcvListaFilmes = findViewById(R.id.rcvListaFilmes);

        btnSave.setOnClickListener(this::salvarFilme);

        adapter = new FilmeAdapter(filmes);
        adapter.setOnItemShortClickListener(this::carregarFilme);
        adapter.setOnItemLongClickListener(this::removerFilme);

        rcvListaFilmes.setLayoutManager(new LinearLayoutManager(this));
        rcvListaFilmes.setAdapter(adapter);

        carregarFilmes();
    }

    public void carregarFilme(Filme filme, View v) {
        filmeAtual = filme;

        txtTitulo.setText(filme.titulo);
        txtDiretor.setText(filme.diretor);
        txtGenero.setText(filme.generoString);
        rtbNota.setRating((float) filme.nota);
        chkJaViuNoCinema.setActivated(filme.jaViuNoCinema);
        numAnoDeLancamento.setText(String.valueOf(filme.anoDeLancamento));
        btnSave.setText("Atualizar filme");
    }
    public void salvarFilme(View v) {
        db.salvarFilme(filmeAtual)
                .addOnSuccessListener(task -> Toast.makeText(this, "Filme salvo com sucesso!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(task -> Toast.makeText(this, "Houve um erro ao salvar o filme", Toast.LENGTH_LONG).show());
        carregarFilme(new Filme(), v);
        btnSave.setText("Salvar novo");
    }

    public void carregarFilmes() {
        db.carregarFilmes()
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Houve um problema ao carregar os filmes", Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(list -> {
                    filmes.clear();

                    for (Filme filme : list) {
                        list.add(filme);
                        adapter.setOnItemShortClickListener((f, c) -> {
                            txtTitulo.setText(f.titulo);
                            txtDiretor.setText(f.diretor);
                            txtGenero.setText(f.generoString);
                            rtbNota.setRating((float) f.nota);
                            chkJaViuNoCinema.setActivated(f.jaViuNoCinema);
                            numAnoDeLancamento.setText(String.valueOf(f.anoDeLancamento));
                            filmeAtual = f;
                        });

                    }

                    filmes.sort(Comparator.comparing(f -> f.generoString));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Filmes carregados com sucesso!", Toast.LENGTH_SHORT).show();
                });
    }

    private void removerFilme(Filme filme, View view) {

        db.removerFilme(filme.id)
                .addOnSuccessListener(v ->
                        Toast.makeText(this, "Filme removido com sucesso!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(v ->
                        Toast.makeText(this, "Houve um erro ao remover filme", Toast.LENGTH_LONG).show());
    }
}