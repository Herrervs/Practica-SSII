package practica.pln;

import java.util.Map;

public class BDReputacionFuente {
    private static final Map<String, Double> PUNTUACIONES = Map.of(
        "bbc.com", 0.95,
        "reuters.com", 0.95,
        "elpais.com", 0.88,
        "elmundo.es", 0.85,
        "rtve.es", 0.90,
        "elconfidencial.com", 0.72,
        "okdiario.com", 0.35,
        "elplural.com", 0.40);

    public double getPuntuacion(String urlFuente) {
        return PUNTUACIONES.entrySet().stream()
            .filter(entrada -> urlFuente.contains(entrada.getKey()))
            .mapToDouble(Map.Entry::getValue)
            .findFirst()
            .orElse(0.5); // puntuacion neutra si no se conoce la fuente
    }
}
