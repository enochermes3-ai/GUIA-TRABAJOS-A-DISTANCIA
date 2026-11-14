package com.guiatrabajos.app;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Utilidades de interfaz (todo se construye por código, sin layouts XML). */
final class UI {
    static final int NAVY = 0xFF1F3A5F, TEAL = 0xFF1F7A8C, BG = 0xFFF3F7FA, TEXT = 0xFF1B2733,
            MUTED = 0xFF5B6B7A, BORDER = 0xFFBFC9D4, CALLOUT = 0xFFE8F4F6, WHITE = 0xFFFFFFFF;
    static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

    private UI() {}

    static int dp(Context c, int v) {
        return Math.round(v * c.getResources().getDisplayMetrics().density);
    }

    static GradientDrawable caja(Context c, int relleno, int borde, int radioDp) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(relleno);
        g.setCornerRadius(dp(c, radioDp));
        if (borde != 0) g.setStroke(dp(c, 1), borde);
        return g;
    }

    static TextView texto(Context c, String s, int sp, int color, boolean negrita) {
        TextView t = new TextView(c);
        t.setText(s);
        t.setTextSize(sp);
        t.setTextColor(color);
        if (negrita) t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    static LinearLayout.LayoutParams params(int w, int h, int izq, int arr, int der, int aba, Context c) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(w, h);
        p.setMargins(dp(c, izq), dp(c, arr), dp(c, der), dp(c, aba));
        return p;
    }

    static TextView chip(Context c, String etiqueta, boolean activo) {
        TextView t = texto(c, etiqueta, 13, activo ? WHITE : NAVY, activo);
        t.setBackground(caja(c, activo ? TEAL : WHITE, activo ? TEAL : BORDER, 16));
        t.setPadding(dp(c, 12), dp(c, 6), dp(c, 12), dp(c, 6));
        t.setGravity(Gravity.CENTER);
        return t;
    }

    static LinearLayout tarjeta(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackground(caja(c, WHITE, BORDER, 10));
        l.setPadding(dp(c, 14), dp(c, 12), dp(c, 14), dp(c, 12));
        return l;
    }

    /** Recuadro tipo "RECOMENDACIÓN / ANÁLISIS CRÍTICO" de la guía. */
    static View aviso(Context c, String etiqueta, String cuerpo) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackground(caja(c, CALLOUT, 0, 8));
        l.setPadding(dp(c, 14), dp(c, 10), dp(c, 14), dp(c, 10));
        l.addView(texto(c, etiqueta, 12, TEAL, true));
        TextView t = texto(c, cuerpo, 14, TEXT, false);
        t.setLineSpacing(0, 1.15f);
        l.addView(t);
        return l;
    }

    static View separador(Context c) {
        View v = new View(c);
        v.setBackgroundColor(BORDER);
        v.setLayoutParams(new LinearLayout.LayoutParams(MATCH, dp(c, 1)));
        return v;
    }
}
