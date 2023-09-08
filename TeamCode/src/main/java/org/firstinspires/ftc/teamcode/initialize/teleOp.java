package org.firstinspires.ftc.teamcode.initialize;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.action.mecanumDrive;

/** This OpMode is a TeleOp.
 * It uses mecanum wheels to move around.
 * In addition, it can pick up cones with a claw and lift them with a linear slide.
 * The robot is driver controlled. It requires two gamepads. */
@TeleOp(name = "Mechanum Drive", group = "Main")
public class teleOp extends OpMode {
    // CONSTRUCT
    org.firstinspires.ftc.teamcode.action.mecanumDrive mecanumDrive = new mecanumDrive();
    // DECLARE NULL
    // DECLARE CUSTOM
    // METHODS
    /** Initializes the teleop. */
    @Override
    public void init() {
        mecanumDrive.init(this);
    }

    /** Runs one time when the teleop starts. */
    public void start() {
        mecanumDrive.runWithoutEncoder();
    }

    /** Loops until the stop button is pressed. */
    @Override
    public void loop() {
        mecanumDrive.setPower(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        mecanumDrive.slowMode(gamepad1.right_bumper);

        mecanumDrive.telemetryOutput();
    }
}