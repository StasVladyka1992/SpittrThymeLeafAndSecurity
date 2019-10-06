package spittr.data;

import org.springframework.stereotype.Component;
import spittr.Spitter;

@Component
public class SpitterRepositoryImpl implements SpitterRepository {
    @Override
    public Spitter save(Spitter spitter) {
        Spitter saved = new Spitter(24L, "jbauer", "24hours", "Jack",
                "Bauer", "stavladyk@gmail.com");
        return saved;
    }

    @Override
    public Spitter findByUserName(String name) {
        Spitter saved = new Spitter(24L, "jbauer", "24hours", "Jack",
                "Bauer", "stavladyk@gmail.com");
        return saved;
    }
}
