package com.guiatrabajos.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Carga de herramientas.txt y preferencias del usuario (favoritos y avance de fases). */
final class Datos {
    private static List<Herramienta> cache;

    private Datos() {}

    static synchronized List<Herramienta> todas(Context c) {
        if (cache == null) {
            List<Herramienta> lista = new ArrayList<>();
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(c.getAssets().open("herramientas.txt"), "UTF-8"))) {
                String linea;
                while ((linea = r.readLine()) != null) {
                    linea = linea.replace("\uFEFF", "").trim();
                    if (linea.isEmpty() || linea.startsWith("#")) continue;
                    Herramienta h = Herramienta.parse(linea);
                    if (h != null) lista.add(h);
                }
            } catch (IOException e) {
                // Si falla la lectura, la lista queda vacía.
            }
            cache = lista;
        }
        return cache;
    }

    static Herramienta buscar(Context c, String nombre) {
        for (Herramienta h : todas(c)) if (h.nombre.equals(nombre)) return h;
        return null;
    }

    /** Minúsculas y sin acentos, para búsquedas tolerantes. */
    static String norm(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT);
    }

    // ---- Preferencias ----
    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences("guiatrabajos", Context.MODE_PRIVATE);
    }

    private static Set<String> conjunto(Context c, String clave) {
        return new HashSet<>(prefs(c).getStringSet(clave, new HashSet<String>()));
    }

    static boolean esFav(Context c, String nombre) {
        return conjunto(c, "favs").contains(nombre);
    }

    /** Devuelve el nuevo estado (true = guardado). */
    static boolean alternarFav(Context c, String nombre) {
        Set<String> s = conjunto(c, "favs");
        boolean ahora = !s.remove(nombre);
        if (ahora) s.add(nombre);
        prefs(c).edit().putStringSet("favs", s).apply();
        return ahora;
    }

    static boolean pasoHecho(Context c, String clave) {
        return conjunto(c, "pasos").contains(clave);
    }

    static void marcarPaso(Context c, String clave, boolean hecho) {
        Set<String> s = conjunto(c, "pasos");
        if (hecho) s.add(clave); else s.remove(clave);
        prefs(c).edit().putStringSet("pasos", s).apply();
    }

    static int pasosHechos(Context c) {
        return conjunto(c, "pasos").size();
    }
}
