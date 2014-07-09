package io.github.spoiq_auth_module.util;

import flow.Flow;
import mortar.Blueprint;

public interface CanShowScreen<S extends Blueprint> {
    void showScreen(S screen, Flow.Direction direction);
}
