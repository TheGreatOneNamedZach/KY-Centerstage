package org.firstinspires.ftc.teamcode.action;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class launcher {
    // CONSTRUCT
    // DECLARE NULL
    Servo launcher;
    // DECLARE CUSTOM
    // METHODS
    public void init(@NonNull OpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        launcher = hardwareMap.get(Servo.class, "Launcher");
    }
    public void launch(Boolean launch) {
        if(launch) {
            launcher.setPosition(0.0);
        } else {
            launcher.setPosition(45.0);
        }
    }
}