package com.example.ac2_pw;

public enum Genero {
    ACAO("Ação"),
    DRAMA("Drama"),
    COMEDIA("Comédia"),
    FICCAO("Ficção");

    public final String displayName;

    Genero(String displayName) {
        this.displayName = displayName;
    }
}
