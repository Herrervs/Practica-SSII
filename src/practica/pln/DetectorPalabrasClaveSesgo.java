package practica.pln;

import java.util.List;

public class DetectorPalabrasClaveSesgo {
    private static final List<String> INDICADORES_SESGO = List.of(
            // Frases hechas y rumores
            "dicen que", "supuestamente", "según fuentes anónimas",
            "como todos saben", "está demostrado que", "los expertos dicen",
            "el gobierno oculta", "la élite", "adoctrinamiento",
            // Exageraciones y sensacionalismo
            "escándalo", "indignante", "ridículo", "fracaso absoluto",
            "milagro", "impresionante", "vergonzoso", "sin precedentes",
            "catástrofe", "histórico", "brutal",
            // Manipulación emocional, insultos
            "borregos", "dictadura", "basura", "injusto", "descarado",
            "censura", "régimen", "títeres", "lavado de cerebro", "desastre",
            "caos", "miseria", "vergüenza",
            // Palabras de imposición
            "evidentemente", "obviamente", "indudablemente",
            "es innegable", "la cruda realidad", "la verdad oculta",
            "nadie puede negar", "sin duda alguna");

    public double detectarSesgo(String texto) {
        String textoMinusculas = texto.toLowerCase();
        long coincidencias = INDICADORES_SESGO.stream().filter(textoMinusculas::contains).count();
        return Math.max(0.0, 1.0 - (coincidencias * 0.1));
        // 1.0 = sin sesgo; 0.0 = muy sesgado
        // a menor sesgo mayor reputacion
    }
}
