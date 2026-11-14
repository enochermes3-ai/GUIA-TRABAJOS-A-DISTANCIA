package com.guiatrabajos.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleActivity extends Activity {
    private Herramienta h;
    private Button botonFav;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        h = Datos.buscar(this, getIntent().getStringExtra("nombre"));
        if (h == null) { finish(); return; }

        LinearLayout raiz = new LinearLayout(this);
        raiz.setOrientation(LinearLayout.VERTICAL);
        raiz.setBackgroundColor(UI.BG);

        TextView volver = UI.texto(this, "‹  Volver", 16, UI.WHITE, true);
        volver.setBackgroundColor(UI.NAVY);
        volver.setGravity(Gravity.CENTER_VERTICAL);
        volver.setPadding(UI.dp(this, 16), UI.dp(this, 14), UI.dp(this, 16), UI.dp(this, 14));
        volver.setOnClickListener(v -> finish());
        raiz.addView(volver, new LinearLayout.LayoutParams(UI.MATCH, UI.WRAP));

        ScrollView sv = new ScrollView(this);
        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(UI.dp(this, 16), UI.dp(this, 16), UI.dp(this, 16), UI.dp(this, 20));

        col.addView(UI.texto(this, h.nombre, 22, UI.NAVY, true));
        col.addView(UI.texto(this, h.categoria + " · " + h.perfil, 13, UI.TEAL, true),
                UI.params(UI.WRAP, UI.WRAP, 0, 2, 0, 12, this));

        if (h.tiene(h.precio)) campo(col, "Pago referencial", h.precio);
        if (h.tiene(h.plan)) campo(col, "Cómo acceder", h.plan);
        if (h.tiene(h.ideal)) campo(col, "Ideal para", h.ideal);

        if (h.puntos.length > 0) {
            col.addView(UI.texto(this, "Puntos clave", 12, UI.MUTED, true),
                    UI.params(UI.WRAP, UI.WRAP, 0, 4, 0, 4, this));
            LinearLayout caja = UI.tarjeta(this);
            for (String p : h.puntos) {
                TextView t = UI.texto(this, "•  " + p, 14, UI.TEXT, false);
                t.setPadding(0, UI.dp(this, 3), 0, UI.dp(this, 3));
                caja.addView(t);
            }
            col.addView(caja, UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 12, this));
        }

        if (h.categoria.startsWith("A.")) {
            col.addView(UI.aviso(this, "REQUISITOS REALES", Contenido.AVISO_REQUISITOS),
                    UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 12, this));
        } else if (h.categoria.startsWith("B.")) {
            col.addView(UI.aviso(this, "ANÁLISIS CRÍTICO", Contenido.AVISO_AFILIADOS),
                    UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 12, this));
        } else {
            col.addView(UI.aviso(this, "RECOMENDACIÓN", Contenido.AVISO_CIFRAS),
                    UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 12, this));
        }

        boolean sinEnlace = h.enlace.isEmpty();
        boton(col, sinEnlace ? "Buscar en Google" : "Abrir sitio", UI.TEAL, UI.WHITE, v -> abrir());
        botonFav = boton(col, "", UI.WHITE, UI.NAVY, v -> {
            Datos.alternarFav(this, h.nombre);
            actualizarFav();
        });
        boton(col, "Compartir", UI.WHITE, UI.NAVY, v -> compartir());
        actualizarFav();

        sv.addView(col);
        raiz.addView(sv, new LinearLayout.LayoutParams(UI.MATCH, 0, 1f));
        setContentView(raiz);
    }

    private void campo(LinearLayout col, String etiqueta, String valor) {
        col.addView(UI.texto(this, etiqueta, 12, UI.MUTED, true), UI.params(UI.WRAP, UI.WRAP, 0, 4, 0, 0, this));
        TextView t = UI.texto(this, valor, 15, UI.TEXT, false);
        t.setPadding(0, UI.dp(this, 2), 0, UI.dp(this, 8));
        col.addView(t);
    }

    private Button boton(LinearLayout col, String texto, int fondo, int colorTexto, View.OnClickListener al) {
        Button bt = new Button(this);
        bt.setText(texto);
        bt.setAllCaps(false);
        bt.setTextColor(colorTexto);
        bt.setBackground(UI.caja(this, fondo, fondo == UI.WHITE ? UI.BORDER : 0, 8));
        bt.setOnClickListener(al);
        col.addView(bt, UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 8, this));
        return bt;
    }

    private void actualizarFav() {
        botonFav.setText(Datos.esFav(this, h.nombre) ? "★  Quitar de favoritos" : "☆  Guardar en favoritos");
    }

    private String direccion() {
        if (h.enlace.isEmpty()) {
            return "https://www.google.com/search?q=" + Uri.encode(h.nombre + " trabajo remoto");
        }
        return h.enlace.startsWith("http") ? h.enlace : "https://" + h.enlace;
    }

    private void abrir() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(direccion())));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No hay navegador disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void compartir() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, h.nombre + (h.enlace.isEmpty() ? "" : " - " + direccion()));
        startActivity(Intent.createChooser(i, "Compartir"));
    }
}
