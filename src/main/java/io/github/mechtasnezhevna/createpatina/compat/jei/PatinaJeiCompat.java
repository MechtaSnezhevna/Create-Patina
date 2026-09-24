package io.github.mechtasnezhevna.createpatina.compat.jei;

import java.lang.reflect.Field;

public final class PatinaJeiCompat {

    private PatinaJeiCompat() {
    }

    private static final Field CONTENTS = findContents();

    private static Field findContents() {
        try {
            Class<?> clazz = Class.forName("mezz.jei.gui.overlay.IngredientListOverlay");
            Field field = clazz.getDeclaredField("contents");
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getContents(Object overlay) {
        try {
            return CONTENTS.get(overlay);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to access JEI IngredientListOverlay.contents ", e);
        }
    }
}
