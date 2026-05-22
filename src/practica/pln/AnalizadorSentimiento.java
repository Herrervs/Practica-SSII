package practica.pln;

import java.util.Set;

public class AnalizadorSentimiento {
    private static final Set<String> PALABRAS_NEGATIVAS = Set.of("alarma", "crisis", "catástrofe", "escándalo",
            "fraude", "peligro", "terrible", "desastre", "conspiración", "urgente");
    private static final Set<String> PATRONES_DE_CLICBAIT = Set.of("no te lo creerás", "te sorprenderá",
            "todo el mundo habla", "lo que los medios ocultan", "la verdad que no quieren");

    public double analizarSentimiento(String texto) {
        String textoMinusculas = texto.toLowerCase();
        long palNegativas = PALABRAS_NEGATIVAS.stream().filter(textoMinusculas::contains).count();
        long patronesClickbait = PATRONES_DE_CLICBAIT.stream().filter(textoMinusculas::contains).count();
        // Score 0.0 (muy sospechoso) y 1.0 (neutro)
        // palNegativas penaliza el score por cada palabra negativa encontrada (0.15 permite que con 1-2 palabras el score no se penalice tanto. hacen falta unas 7 para que baje a 0)
        // patronesClickbait penaliza el score por cada patrón de clickbait encontrado (0.3 permite que con 2 patrones de clickbait el score sea 0.4. hacen falta unas 4 para que baje a 0)
        // math.max evita que el resultado sea negativo
        return Math.max(0.0, 1.0 - (palNegativas * 0.15) - (patronesClickbait * 0.3));
    }
}
