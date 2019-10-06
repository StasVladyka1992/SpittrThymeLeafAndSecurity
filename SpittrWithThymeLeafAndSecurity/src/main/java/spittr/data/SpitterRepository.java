package spittr.data;

import spittr.Spitter;

public interface SpitterRepository {
    Spitter save (Spitter spitter);
    Spitter findByUserName(String name);
}
