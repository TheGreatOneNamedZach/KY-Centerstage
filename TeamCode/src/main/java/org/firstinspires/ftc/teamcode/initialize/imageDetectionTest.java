package org.firstinspires.ftc.teamcode.initialize;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.other.customSensors.imageDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous
public class imageDetectionTest extends LinearOpMode {

    org.firstinspires.ftc.teamcode.other.customSensors.imageDetection imageDetection = new imageDetection();

    @Override
    public void runOpMode() throws InterruptedException {
        imageDetection.init(this);

        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("Image", imageDetection.imageReturn());
            AprilTagDetection detection = imageDetection.aprilTagSearch(1);
            telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
            telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
            telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
            telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            telemetry.update();
        }
    }
}
