package com.example.ac2_pw;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Database {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Filme> filmes = new ArrayList<>();

    public Database() {
        List<Filme> seeds = List.of(
                new Filme(
                        "id1",
                        "Dune: Part Two",
                        "Denis Villeneuve",
                        2024,
                        4.8,
                        "Science Fiction/Adventure",
                        null,
                        true
                ),
                new Filme(
                        "id2",
                        "The Shawshank Redemption",
                        "Frank Darabont",
                        1994,
                        5.0,
                        "Drama",
                        null,
                        false
                ),
                new Filme(
                        "id3",
                        "Toy Story",
                        "John Lasseter",
                        1995,
                        4.2,
                        "Animation/Comedy",
                        null,
                        true
                ),
                new Filme(
                        "id4",
                        "Talk to Me",
                        "Danny Philippou",
                        2022,
                        3.9,
                        "Horror/Thriller",
                        null,
                        true
                ),
                new Filme(
                        "1d5",
                        "Amélie",
                        "Jean-Pierre Jeunet",
                        2001,
                        4.6,
                        "Romance/Comedy",
                        null,
                        false
                )
        );
        for (Filme filme : filmes) {
            filme.id = null;
            salvarFilme(filme);
        }
    }
    public Task<Boolean> salvarFilme(Filme filme) {
        FilmeWrapper filmeSalvo = new FilmeWrapper();
        BooleanWrapper success = new BooleanWrapper();
        filmeSalvo.value = filme;

        if (filme.id == null) {
            return db.collection("filmes")
                .add(filme)
                .continueWith(v -> {
                    if (v.isSuccessful())
                        filmeSalvo.value.id = v.getResult().getId();
                    return v.isSuccessful();
                });
        } else {
            return db.collection("filmes")
                    .document(filme.id)
                    .set(filme)
                    .continueWith(Task::isSuccessful);
        }
    }

    public Task<List<Filme>> carregarFilmes() {
        return db.collection("filmes").get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        filmes.clear();
                        for(QueryDocumentSnapshot snapshot : task.getResult()) {
                            Filme filme = snapshot.toObject(Filme.class);
                            filme.id = snapshot.getId();
                            filmes.add(filme);
                        }
                    }
                    return filmes;
                });
    }

    public Task<Boolean> removerFilme(String id) {

        final BooleanWrapper success = new BooleanWrapper();
        final FilmeWrapper filmeResult = new FilmeWrapper();

        return db.collection("produtos")
                .document(id)
                .get()
                .continueWith(v -> {
                    if (v.isSuccessful()) {
                        Filme filme = v.getResult().toObject(Filme.class);
                        if (filme != null) {
                            filmes.remove(filme);
                            return true;
                        }
                    }
                    return v.isSuccessful();
                });
    }

    public class FilmeWrapper {
        public Filme value;
    }
    public class BooleanWrapper {
        public boolean value;
    }
}
