package com.example.ac2_pw;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder> {

    private List<Filme> filmes;
    public FilmeAdapter(List<Filme> filmesList) {
        this.filmes = filmesList;
    }

    @FunctionalInterface
    public interface OnItemShortClickListener {
        void onItemClick(Filme filme, View view);
    }

    @FunctionalInterface
    public interface OnItemLongClickListener {
        void onItemClick(Filme filme, View view);
    }
    private OnItemShortClickListener shortClickListener;
    private OnItemLongClickListener longClickListener;

    public void setOnItemShortClickListener(OnItemShortClickListener listener) {
        this.shortClickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public static class FilmeViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitulo;
        public TextView txtInfo;
        public RatingBar rtbNota;

        public FilmeViewHolder(View itemView) {
            super(itemView);

            txtTitulo = new TextView(itemView.getContext());
            txtTitulo.setTextSize(18);
            txtTitulo.getPaint().setFakeBoldText(true);

            txtInfo = new TextView(itemView.getContext());

            rtbNota = new RatingBar(itemView.getContext(), null, android.R.attr.ratingBarStyleSmall);
            rtbNota.setNumStars(5);
            rtbNota.setStepSize(0.5f);
            rtbNota.setIsIndicator(true);
        }
    }
    @Override
    public FilmeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new FilmeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FilmeViewHolder holder, int position) {
        Filme filme = filmes.get(position);

        holder.txtTitulo.setText(filme.titulo);

        String info = String.format("Data de Lançamento: %s \n Gênero: %s", filme.anoDeLancamento, filme.generoString);

        holder.txtInfo.setText(info);

        holder.rtbNota.setRating((float) filme.nota);

        holder.itemView.setOnClickListener(v -> {
            if (shortClickListener != null) {
                shortClickListener.onItemClick(filme, v);
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            private long lastClickTime = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime < 300) {
                        longClickListener.onItemClick(filme, v);
                    }
                    lastClickTime = currentTime;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmes.size();
    }
}
