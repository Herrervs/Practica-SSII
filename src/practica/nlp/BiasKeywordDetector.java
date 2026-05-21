package practica.nlp;

import java.util.List;

public class BiasKeywordDetector {
    private static final List<String> BIAS_INDICATORS = List.of(
        "dicen que", "supuestamente", "según fuentes anónimas",
        "como todos saben", "está demostrado que", "los expertos dicen",
        "el gobierno oculta", "la élite", "adoctrinamiento"
    );

    public double detectBias(String texto) {
        String textoMinusculas = texto.toLowerCase();
        long coincidencias = BIAS_INDICATORS.stream().filter(textoMinusculas::contains).count();
        return Math.max(0.0, 1.0 - (coincidencias * 0.2));
        // 1.0 = sin sesgo; 0.0 = muy sesgado
        // a menor sesgo mayor reputacion
    }
}