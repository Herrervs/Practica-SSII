package practica.nlp;

import java.util.List;

public class DetectorPalabrasClaveSesgo {
    private static final List<String> INDICADORES_SESGO = List.of(
        "dicen que", "supuestamente", "según fuentes anónimas",
        "como todos saben", "está demostrado que", "los expertos dicen",
        "el gobierno oculta", "la élite", "adoctrinamiento"
    );

    public double detectarSesgo(String texto) {
        String textoMinusculas = texto.toLowerCase();
        long coincidencias = INDICADORES_SESGO.stream().filter(textoMinusculas::contains).count();
        return Math.max(0.0, 1.0 - (coincidencias * 0.2));
        // 1.0 = sin sesgo; 0.0 = muy sesgado
        // a menor sesgo mayor reputacion
    }
}
