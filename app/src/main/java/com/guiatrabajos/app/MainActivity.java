package com.guiatrabajos.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String[] NOMBRES_TAB = {"Plataformas", "Fases", "Prompts", "Info"};

    private FrameLayout cuerpo;
    private final TextView[] pestanas = new TextView[4];
    private final View[] vistas = new View[4];
    private int tabActual = -1;

    // Estado de la pestaña Plataformas
    private String fCat = null, fPerfil = null, consulta = "";
    private boolean fFav = false;
    private final List<Herramienta> visibles = new ArrayList<>();
    private Adaptador adaptador;
    private TextView contador;
    private LinearLayout filaCategorias, filaPerfiles;

    // Estado de la pestaña Fases
    private TextView progreso;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout raiz = new LinearLayout(this);
        raiz.setOrientation(LinearLayout.VERTICAL);
        raiz.setBackgroundColor(UI.BG);
        raiz.addView(cabecera());
        cuerpo = new FrameLayout(this);
        raiz.addView(cuerpo, new LinearLayout.LayoutParams(UI.MATCH, 0, 1f));
        raiz.addView(barraInferior());
        setContentView(raiz);
        mostrarTab(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tabActual == 0 && adaptador != null) filtrar();
    }

    // ------------------------------------------------------------------ marco
    private View cabecera() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackgroundColor(UI.NAVY);
        l.setPadding(UI.dp(this, 16), UI.dp(this, 12), UI.dp(this, 16), UI.dp(this, 12));
        l.addView(UI.texto(this, "Guía de Trabajos a Distancia", 20, UI.WHITE, true));
        int n = Datos.todas(this).size();
        l.addView(UI.texto(this, n + " plataformas · 6 categorías · 5 fases", 12, 0xFFBFD4E6, false));
        return l;
    }

    private View barraInferior() {
        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setBackgroundColor(UI.WHITE);
        col.addView(UI.separador(this));
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            TextView t = UI.texto(this, NOMBRES_TAB[i], 14, UI.MUTED, false);
            t.setGravity(Gravity.CENTER);
            t.setPadding(0, UI.dp(this, 14), 0, UI.dp(this, 14));
            t.setOnClickListener(v -> mostrarTab(idx));
            pestanas[i] = t;
            fila.addView(t, new LinearLayout.LayoutParams(0, UI.WRAP, 1f));
        }
        col.addView(fila);
        return col;
    }

    private void mostrarTab(int i) {
        if (vistas[i] == null) {
            switch (i) {
                case 0: vistas[i] = vistaPlataformas(); break;
                case 1: vistas[i] = vistaFases(); break;
                case 2: vistas[i] = vistaPrompts(); break;
                default: vistas[i] = vistaInfo(); break;
            }
        }
        cuerpo.removeAllViews();
        cuerpo.addView(vistas[i], new FrameLayout.LayoutParams(UI.MATCH, UI.MATCH));
        tabActual = i;
        for (int k = 0; k < 4; k++) {
            boolean sel = k == i;
            pestanas[k].setTextColor(sel ? UI.TEAL : UI.MUTED);
            pestanas[k].setTypeface(sel ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        }
        if (i == 0) filtrar();
    }

    // ------------------------------------------------------------ Plataformas
    private View vistaPlataformas() {
        LinearLayout v = new LinearLayout(this);
        v.setOrientation(LinearLayout.VERTICAL);

        EditText q = new EditText(this);
        q.setHint("Buscar plataforma, tema o pago…");
        q.setSingleLine(true);
        q.setTextSize(15);
        q.setInputType(InputType.TYPE_CLASS_TEXT);
        q.setBackground(UI.caja(this, UI.WHITE, UI.BORDER, 10));
        q.setPadding(UI.dp(this, 12), UI.dp(this, 10), UI.dp(this, 12), UI.dp(this, 10));
        q.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable e) {
                consulta = Datos.norm(e.toString().trim());
                filtrar();
            }
        });
        v.addView(q, UI.params(UI.MATCH, UI.WRAP, 12, 10, 12, 6, this));

        filaCategorias = new LinearLayout(this);
        filaCategorias.setOrientation(LinearLayout.HORIZONTAL);
        v.addView(desplazable(filaCategorias), UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 4, this));

        filaPerfiles = new LinearLayout(this);
        filaPerfiles.setOrientation(LinearLayout.HORIZONTAL);
        v.addView(desplazable(filaPerfiles), UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 2, this));

        contador = UI.texto(this, "", 12, UI.MUTED, false);
        v.addView(contador, UI.params(UI.WRAP, UI.WRAP, 16, 2, 16, 2, this));

        FrameLayout zona = new FrameLayout(this);
        ListView lista = new ListView(this);
        lista.setDivider(null);
        lista.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lista.setVerticalScrollBarEnabled(false);
        adaptador = new Adaptador();
        lista.setAdapter(adaptador);
        TextView vacio = UI.texto(this, "Sin resultados con esos filtros.", 14, UI.MUTED, false);
        vacio.setGravity(Gravity.CENTER);
        vacio.setPadding(UI.dp(this, 24), UI.dp(this, 40), UI.dp(this, 24), 0);
        zona.addView(lista, new FrameLayout.LayoutParams(UI.MATCH, UI.MATCH));
        zona.addView(vacio, new FrameLayout.LayoutParams(UI.MATCH, UI.MATCH));
        lista.setEmptyView(vacio);
        v.addView(zona, new LinearLayout.LayoutParams(UI.MATCH, 0, 1f));

        reconstruirChips();
        return v;
    }

    private HorizontalScrollView desplazable(LinearLayout fila) {
        HorizontalScrollView h = new HorizontalScrollView(this);
        h.setHorizontalScrollBarEnabled(false);
        fila.setPadding(UI.dp(this, 12), UI.dp(this, 2), UI.dp(this, 12), UI.dp(this, 2));
        h.addView(fila, new FrameLayout.LayoutParams(UI.WRAP, UI.WRAP));
        return h;
    }

    private void agregarChip(LinearLayout fila, String etiqueta, boolean activo, View.OnClickListener al) {
        TextView c = UI.chip(this, etiqueta, activo);
        c.setOnClickListener(al);
        fila.addView(c, UI.params(UI.WRAP, UI.WRAP, 0, 0, 8, 0, this));
    }

    private void reconstruirChips() {
        filaCategorias.removeAllViews();
        agregarChip(filaCategorias, "Todas", fCat == null && !fFav, v -> {
            fCat = null; fFav = false; reconstruirChips(); filtrar();
        });
        agregarChip(filaCategorias, "★ Favoritos", fFav, v -> {
            fFav = !fFav; if (fFav) fCat = null; reconstruirChips(); filtrar();
        });
        for (final String c : Contenido.CATEGORIAS) {
            agregarChip(filaCategorias, c, c.equals(fCat), v -> {
                fFav = false; fCat = c.equals(fCat) ? null : c; reconstruirChips(); filtrar();
            });
        }
        filaPerfiles.removeAllViews();
        agregarChip(filaPerfiles, "Todos los perfiles", fPerfil == null, v -> {
            fPerfil = null; reconstruirChips(); filtrar();
        });
        for (final String p : Contenido.PERFILES) {
            agregarChip(filaPerfiles, p, p.equals(fPerfil), v -> {
                fPerfil = p.equals(fPerfil) ? null : p; reconstruirChips(); filtrar();
            });
        }
    }

    private void filtrar() {
        visibles.clear();
        for (Herramienta h : Datos.todas(this)) {
            if (fFav && !Datos.esFav(this, h.nombre)) continue;
            if (fCat != null && !h.categoria.equals(fCat)) continue;
            if (fPerfil != null && !h.perfil.equals(fPerfil)) continue;
            if (!consulta.isEmpty() && !h.coincide(consulta)) continue;
            visibles.add(h);
        }
        adaptador.notifyDataSetChanged();
        int n = visibles.size();
        contador.setText(n + (n == 1 ? " resultado" : " resultados"));
    }

    private class Adaptador extends BaseAdapter {
        @Override public int getCount() { return visibles.size(); }
        @Override public Object getItem(int i) { return visibles.get(i); }
        @Override public long getItemId(int i) { return i; }

        @Override
        public View getView(int pos, View reuso, ViewGroup padre) {
            final Herramienta h = visibles.get(pos);
            final Context c = MainActivity.this;
            FrameLayout envoltorio = new FrameLayout(c);
            envoltorio.setPadding(UI.dp(c, 12), UI.dp(c, 5), UI.dp(c, 12), UI.dp(c, 5));
            LinearLayout tarjeta = UI.tarjeta(c);

            LinearLayout fila = new LinearLayout(c);
            fila.setOrientation(LinearLayout.HORIZONTAL);
            fila.setGravity(Gravity.CENTER_VERTICAL);
            TextView nombre = UI.texto(c, h.nombre, 16, UI.NAVY, true);
            fila.addView(nombre, new LinearLayout.LayoutParams(0, UI.WRAP, 1f));
            final TextView estrella = UI.texto(c, Datos.esFav(c, h.nombre) ? "★" : "☆", 24, UI.TEAL, false);
            estrella.setPadding(UI.dp(c, 10), 0, 0, 0);
            estrella.setOnClickListener(v -> {
                boolean on = Datos.alternarFav(c, h.nombre);
                estrella.setText(on ? "★" : "☆");
                if (fFav) filtrar();
            });
            fila.addView(estrella);
            tarjeta.addView(fila);

            tarjeta.addView(UI.texto(c, h.categoria + " · " + h.perfil, 12, UI.TEAL, false));
            if (h.tiene(h.precio)) {
                TextView t = UI.texto(c, "Pago: " + h.precio, 13, UI.TEXT, false);
                t.setPadding(0, UI.dp(c, 6), 0, 0);
                tarjeta.addView(t);
            }
            if (h.tiene(h.plan)) {
                TextView t = UI.texto(c, "Acceso: " + h.plan, 13, UI.MUTED, false);
                t.setPadding(0, UI.dp(c, 2), 0, 0);
                tarjeta.addView(t);
            }
            tarjeta.setOnClickListener(v -> {
                Intent i = new Intent(c, DetalleActivity.class);
                i.putExtra("nombre", h.nombre);
                startActivity(i);
            });
            envoltorio.addView(tarjeta, new FrameLayout.LayoutParams(UI.MATCH, UI.WRAP));
            return envoltorio;
        }
    }

    // ------------------------------------------------------------------ Fases
    private View vistaFases() {
        ScrollView sv = new ScrollView(this);
        LinearLayout col = columna();
        col.addView(UI.aviso(this, "PLAN DE ACCIÓN",
                "Sigue estas fases en orden y marca lo que vayas completando. Tu avance se guarda en el teléfono."),
                UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 10, this));
        progreso = UI.texto(this, "", 13, UI.NAVY, true);
        col.addView(progreso, UI.params(UI.WRAP, UI.WRAP, 2, 0, 0, 6, this));

        for (int f = 0; f < Contenido.FASES.length; f++) {
            String[] fase = Contenido.FASES[f];
            LinearLayout t = UI.tarjeta(this);
            t.addView(UI.texto(this, fase[0], 16, UI.NAVY, true));
            for (int s = 1; s < fase.length; s++) {
                final String clave = "f" + f + "_" + s;
                CheckBox cb = new CheckBox(this);
                cb.setText(fase[s]);
                cb.setTextSize(14);
                cb.setTextColor(UI.TEXT);
                cb.setButtonTintList(ColorStateList.valueOf(UI.TEAL));
                cb.setChecked(Datos.pasoHecho(this, clave));
                cb.setOnCheckedChangeListener((b, marcado) -> {
                    Datos.marcarPaso(this, clave, marcado);
                    actualizarProgreso();
                });
                t.addView(cb, UI.params(UI.MATCH, UI.WRAP, 0, 6, 0, 0, this));
            }
            col.addView(t, UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 10, this));
        }
        actualizarProgreso();
        sv.addView(col);
        return sv;
    }

    private void actualizarProgreso() {
        int total = 0;
        for (String[] f : Contenido.FASES) total += f.length - 1;
        progreso.setText("Avance: " + Datos.pasosHechos(this) + " de " + total + " pasos completados");
    }

    // ---------------------------------------------------------------- Prompts
    private View vistaPrompts() {
        ScrollView sv = new ScrollView(this);
        LinearLayout col = columna();
        col.addView(UI.aviso(this, "CATEGORÍA F · PROMPTS DE IA",
                "Plantillas para validar y desarrollar una idea antes de invertir. Cambia lo que está entre [corchetes] por tus datos y pégalo en ChatGPT u otro asistente."),
                UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 10, this));
        for (final String[] p : Contenido.PROMPTS) {
            LinearLayout t = UI.tarjeta(this);
            t.addView(UI.texto(this, p[0], 16, UI.NAVY, true));
            TextView desc = UI.texto(this, p[1], 13, UI.MUTED, false);
            desc.setPadding(0, UI.dp(this, 4), 0, UI.dp(this, 8));
            t.addView(desc);
            TextView cuerpoPrompt = UI.texto(this, p[2], 13, UI.TEXT, false);
            cuerpoPrompt.setBackground(UI.caja(this, UI.BG, UI.BORDER, 8));
            cuerpoPrompt.setPadding(UI.dp(this, 10), UI.dp(this, 8), UI.dp(this, 10), UI.dp(this, 8));
            t.addView(cuerpoPrompt);
            Button copiar = new Button(this);
            copiar.setText("Copiar prompt");
            copiar.setAllCaps(false);
            copiar.setTextColor(UI.WHITE);
            copiar.setBackground(UI.caja(this, UI.TEAL, 0, 8));
            copiar.setOnClickListener(v -> {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(p[0], p[2]));
                Toast.makeText(this, "Prompt copiado", Toast.LENGTH_SHORT).show();
            });
            t.addView(copiar, UI.params(UI.MATCH, UI.WRAP, 0, 10, 0, 0, this));
            col.addView(t, UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 10, this));
        }
        col.addView(UI.aviso(this, "ANÁLISIS CRÍTICO", Contenido.AVISO_PROMPTS));
        sv.addView(col);
        return sv;
    }

    // ------------------------------------------------------------------- Info
    private View vistaInfo() {
        ScrollView sv = new ScrollView(this);
        LinearLayout col = columna();
        col.addView(UI.texto(this, "Comparativa de categorías", 18, UI.NAVY, true),
                UI.params(UI.WRAP, UI.WRAP, 2, 0, 0, 6, this));
        for (String[] c : Contenido.COMPARATIVA) {
            LinearLayout t = UI.tarjeta(this);
            t.addView(UI.texto(this, c[0], 15, UI.NAVY, true));
            TextView enfoque = UI.texto(this, c[1], 13, UI.TEXT, false);
            enfoque.setPadding(0, UI.dp(this, 4), 0, 0);
            t.addView(enfoque);
            TextView ej = UI.texto(this, "Destacados: " + c[2], 12, UI.MUTED, false);
            ej.setPadding(0, UI.dp(this, 4), 0, 0);
            t.addView(ej);
            t.addView(UI.texto(this, "Tipo de ingreso: " + c[3], 12, UI.TEAL, true));
            col.addView(t, UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 8, this));
        }
        col.addView(UI.texto(this, "Avisos importantes", 18, UI.NAVY, true),
                UI.params(UI.WRAP, UI.WRAP, 2, 8, 0, 6, this));
        col.addView(UI.aviso(this, "REQUISITOS REALES", Contenido.AVISO_REQUISITOS),
                UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 8, this));
        col.addView(UI.aviso(this, "ANÁLISIS CRÍTICO", Contenido.AVISO_AFILIADOS),
                UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 8, this));
        col.addView(UI.aviso(this, "RECOMENDACIÓN", Contenido.AVISO_CIFRAS),
                UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 8, this));
        col.addView(UI.aviso(this, "RECOMENDACIÓN", Contenido.AVISO_TABLEROS),
                UI.params(UI.MATCH, UI.WRAP, 0, 0, 0, 8, this));
        col.addView(UI.texto(this, "Leyenda", 18, UI.NAVY, true),
                UI.params(UI.WRAP, UI.WRAP, 2, 8, 0, 6, this));
        TextView ley = UI.texto(this, Contenido.LEYENDA, 13, UI.MUTED, false);
        col.addView(ley);
        sv.addView(col);
        return sv;
    }

    private LinearLayout columna() {
        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(UI.dp(this, 12), UI.dp(this, 12), UI.dp(this, 12), UI.dp(this, 16));
        return col;
    }
}
