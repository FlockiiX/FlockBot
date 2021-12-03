package de.flockiix.flockbot.core.button;

import de.flockiix.flockbot.core.exception.ButtonAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ButtonHandler.class);
    private final List<Button> buttons = new ArrayList<>();

    /**
     * Registers the given buttons.
     *
     * @param buttonsToRegister the buttons to be registered
     */
    public void registerButtons(Button... buttonsToRegister) {
        List<Button> toRegisterButtons = new ArrayList<>(Arrays.asList(buttonsToRegister));
        for (Button button : toRegisterButtons) {
            boolean idFound = this.buttons.stream().anyMatch((it) -> it.getButtonId().equalsIgnoreCase(button.getButtonId()));
            if (idFound)
                throw new ButtonAlreadyExistsException("A button with this id is already present: " + button.getButtonId());

            buttons.add(button);
        }
        LOGGER.info("Button registration finished");
    }

    /**
     * Searches for a button with the given id.
     *
     * @param buttonId the button id of the button
     * @return the button if found by its id. Otherwise null
     */
    public Button getButton(String buttonId) {
        String searchLower = buttonId.toLowerCase();
        return this.buttons.stream()
                .filter(button -> button.getButtonId().equals(searchLower))
                .findFirst().orElse(null);
    }
}
