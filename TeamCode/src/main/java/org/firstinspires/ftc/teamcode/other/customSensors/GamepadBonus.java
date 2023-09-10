package org.firstinspires.ftc.teamcode.other.customSensors;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadBonus {

    Gamepad.LedEffect warningLED;
    Gamepad.LedEffect holdLED;

    Gamepad.RumbleEffect warningRumble;
    Gamepad.RumbleEffect holdRumble;

    public void init() {
        warningLED = new Gamepad.LedEffect.Builder()
                .addStep(255.0, 0.0, 0.0, 250)
                .addStep(0.0, 0.0, 255.0, 100)
                .addStep(255.0, 0.0, 0.0, 250)
                .addStep(0.0, 0.0, 255.0, 100)
                .build();

        holdLED = new Gamepad.LedEffect.Builder()
                .addStep(255.0, 0.0, 0.0, 1000)
                .addStep(0.0, 0.0, 255.0, 100)
                .build();

        warningRumble = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 250)
                .addStep(0.0, 0.0, 100)
                .addStep(1.0, 1.0, 250)
                .addStep(0.0, 0.0, 100)
                .build();

        holdRumble = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 1.0, 1000)
                .addStep(0.0, 0.0, 100)
                .build();
    }

    public void resetLED(Gamepad gamepad) {
        gamepad.setLedColor(0.0, 0.0, 255.0, Gamepad.LED_DURATION_CONTINUOUS);
    }
    public void warning(Gamepad gamepad) {
        gamepad.runLedEffect(warningLED);
        gamepad.runRumbleEffect(warningRumble);
    }

    public void hold(Gamepad gamepad) {
        gamepad.runLedEffect(holdLED);
        gamepad.runRumbleEffect(holdRumble);
    }
}
