package com.example.ac2_pw;

import java.util.Comparator;
import java.util.Objects;

public class Filme {
    public String id;
    public String titulo;
    public String diretor;
    public int anoDeLancamento;
    public double nota;
    public String generoString;
    public Genero generoEnum;
    public boolean jaViuNoCinema;

    public Filme() {
    }

    public Filme(String id, String titulo, String diretor, int anoDeLancamento, double nota, String generoString, Genero generoEnum, boolean jaViuNoCinema) {
        this.id = id;
        this.titulo = titulo;
        this.diretor = diretor;
        this.anoDeLancamento = anoDeLancamento;
        this.nota = nota;
        this.generoString = generoString;
        this.generoEnum = generoEnum;
        this.jaViuNoCinema = jaViuNoCinema;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass().isAssignableFrom(Filme.class)) return false;
        Filme filme = (Filme) o;
        return Objects.equals(this.id, filme.id);
    }
}
