package practica.pln;

import java.util.Map;

public class BDReputacionFuente {
    private static final Map<String, Double> PUNTUACIONES = Map.of(
        "bbc.com", 0.95,
        "bbc news", 0.95,
        "bbc", 0.95,
        "reuters.com", 0.95,
        "reuters", 0.95,
        "elpais.com", 0.88,
        "elpais", 0.88,
        "elmundo.es", 0.85,
        "rtve.es", 0.90,
        "rtve", 0.90);

    // Como Map.of solo admite 10 pares clave-valor, usamos un inicializador estático o Map.ofEntries para los demás
    // Pero para simplificar, he usado Map.of para los principales y un Map estático clásico
    private static final Map<String, Double> TODAS_PUNTUACIONES = new java.util.HashMap<>(PUNTUACIONES);
    static {
        TODAS_PUNTUACIONES.put("elconfidencial.com", 0.72);
        TODAS_PUNTUACIONES.put("okdiario.com", 0.35);
        TODAS_PUNTUACIONES.put("okdiario", 0.35);
        TODAS_PUNTUACIONES.put("elplural.com", 0.40);
        TODAS_PUNTUACIONES.put("el mundo today", 0.20); // Satira
    }

    public double getPuntuacion(String urlFuente) {
        if (urlFuente == null) return 0.5;
        
        String fuenteLower = urlFuente.toLowerCase();
        
        return TODAS_PUNTUACIONES.entrySet().stream()
            .filter(entrada -> fuenteLower.contains(entrada.getKey()))
            .mapToDouble(Map.Entry::getValue)
            .findFirst()
            .orElse(0.5); // puntuacion neutra si no se conoce la fuente
    }
}
