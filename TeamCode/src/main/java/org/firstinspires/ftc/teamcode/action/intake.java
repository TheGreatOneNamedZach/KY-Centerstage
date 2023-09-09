package org.firstinspires.ftc.teamcode.action;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class intake {
    // CONSTRUCT
    // DECLARE NULL
    DcMotor intakeRight;
    DcMotor intakeLeft;
    Telemetry telemetry;
    // DECLARE CUSTOM
    // METHODS
    public void init(@NonNull OpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        telemetry = opMode.telemetry;
        intakeRight = hardwareMap.get(DcMotor.class, "Intake Right");
        intakeLeft = hardwareMap.get(DcMotor.class, "Intake Left");

        intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void intake(float in, boolean out) {
        if(in != 0) {
            intakeLeft.setPower(in);
            intakeRight.setPower(in);
        } else if (out) {
            intakeLeft.setPower(-0.75);
            intakeRight.setPower(-0.75);
        } else {
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }
    }
}
