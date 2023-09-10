package org.firstinspires.ftc.teamcode.initialize;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.action.arm;
import org.firstinspires.ftc.teamcode.action.hook;
import org.firstinspires.ftc.teamcode.action.intake;
import org.firstinspires.ftc.teamcode.action.launcher;
import org.firstinspires.ftc.teamcode.action.mecanumDrive;
import org.firstinspires.ftc.teamcode.other.customSensors.GamepadBonus;

/** This OpMode is a TeleOp.
 * It uses mecanum wheels to move around.
 * In addition, it can pick up cones with a claw and lift them with a linear slide.
 * The robot is driver controlled. It requires two gamepads. */
@TeleOp(name = "Mechanum Drive", group = "Main")
public class teleOp extends OpMode {
    // CONSTRUCT
    org.firstinspires.ftc.teamcode.action.mecanumDrive mecanumDrive = new mecanumDrive();
    org.firstinspires.ftc.teamcode.action.intake intake = new intake();
    org.firstinspires.ftc.teamcode.action.arm arm = new arm();
    org.firstinspires.ftc.teamcode.other.customSensors.GamepadBonus gamepadBonus = new GamepadBonus();
    org.firstinspires.ftc.teamcode.action.launcher launcher = new launcher();
    org.firstinspires.ftc.teamcode.action.hook hook = new hook();
    // DECLARE NULL
    // DECLARE CUSTOM
    // METHODS
    /** Initializes the teleop. */
    @Override
    public void init() {
        mecanumDrive.init(this);
        intake.init(this);
        arm.init(this);
        //launcher.init(this);
        gamepadBonus.init();
        gamepadBonus.resetLED(gamepad1);
        gamepadBonus.resetLED(gamepad2);
    }

    /** Runs one time when the teleop starts. */
    public void start() {
        mecanumDrive.runWithoutEncoder();
    }

    /** Loops until the stop button is pressed. */
    @Override
    public void loop() {
        mecanumDrive.setPower(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        mecanumDrive.slowMode(gamepad1.left_bumper);
        intake.intake(gamepad1.right_trigger, gamepad1.right_bumper);
        arm.setPower(-gamepad2.right_stick_y);
        //launcher.launch(gamepad2.dpad_left);
        hook.setPower(gamepad2.left_stick_x);

        mecanumDrive.telemetryOutput();
    }
}