package com.guiatrabajos.app;

/** Una plataforma o recurso. Formato de línea: categoria|nombre|acceso|pago|puntos;;|ideal para|enlace|perfil */
final class Herramienta {
    final String categoria, nombre, plan, precio, ideal, enlace, perfil;
    final String[] puntos;
    private final String indice;

    private Herramienta(String[] f) {
        categoria = f[0];
        nombre = f[1];
        plan = f[2];
        precio = f[3];
        puntos = f[4].isEmpty() ? new String[0] : f[4].split(";;");
        ideal = f[5];
        enlace = f[6];
        perfil = f[7];
        StringBuilder sb = new StringBuilder();
        sb.append(nombre).append(' ').append(categoria).append(' ').append(plan).append(' ')
          .append(precio).append(' ').append(ideal).append(' ').append(perfil);
        for (String p : puntos) sb.append(' ').append(p);
        indice = Datos.norm(sb.toString());
    }

    static Herramienta parse(String linea) {
        String[] raw = linea.split("\\|", -1);
        if (raw.length < 2) return null;
        String[] f = new String[8];
        for (int i = 0; i < 8; i++) f[i] = i < raw.length ? raw[i].trim() : "";
        return new Herramienta(f);
    }

    boolean coincide(String consultaNormalizada) {
        return indice.contains(consultaNormalizada);
    }

    boolean tiene(String s) {
        return s != null && !s.isEmpty() && !s.equals("—");
    }
}
