package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
@TeleOp(name = "testingFullCode", group = "Linear Opmode")
public class FullCode extends LinearOpMode {

    // Drivetrain
    private DcMotorEx frontLeft, frontRight, backLeft, backRight;

    // Subsystems
    private DcMotorEx intake, transfer, shooter, shooterb;
    private Servo blocker;

    // Limelight
    private com.qualcomm.hardware.limelightvision.Limelight3A limelight;

    // Shooter control
    private double kp = 0.002, kf = 0.045;
    private double MIN_SHOOTER_VELOCITY = 1200;
    private double MAX_SHOOTER_VELOCITY = 2200;
    private double currentShooterVelocity = 0;
    private double error = 0;
    double power;
    private boolean SHOOTING = false;

    // Auto-align constants
    private final double ALIGN_TOLERANCE_DEG = 1;;
    private final double TURN_KP = 0.01;     // tune this
    private final double MIN_TURN_POWER = 0.1;
    // Blocker positions
    private static final double BLOCKER_BLOCK_POSITION   = 0.1;
    private static final double BLOCKER_UNBLOCK_POSITION = 0.4;

    @Override
    public void runOpMode() {
        initHardware();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
             double drive  = gamepad1.left_stick_y;
            double strafe = -gamepad1.left_stick_x;
            double rotate = -gamepad1.right_stick_x;
            
            
            LLResult result = limelight.getLatestResult();
            double ta = 0, tx = 0, ty = 0 ;
            boolean hasTarget = result != null && result.isValid();
            if (hasTarget) {
                ta = result.getTa();
                tx = result.getTx();
                ty = result.getTy();
            }
           
            double targetVelo = calcVelo(ta,ty);
             boolean shootButton = gamepad1.left_bumper;
             boolean aligned = false;
            if (shootButton) {
                if(hasTarget){
                double headingError = tx+1; 
                // want tx -> 0
                if (Math.abs(headingError) > ALIGN_TOLERANCE_DEG) {
                    rotate = headingError * TURN_KP + MIN_TURN_POWER * Math.signum(headingError);
                } else {
                    rotate = 0; // within tolerance, stop turning
                    aligned = true;
                }
                }
                currentShooterVelocity = -shooter.getVelocity();
                error = targetVelo - Math.abs(currentShooterVelocity);
                if (error < -800) {
                    kf = 0.9;
                } else if (error < 0) {
                    kf = 0.6;
                } else {
                    kf = 0.7;
                    if(targetVelo == 2000) kf = 0.9;
                }
                 power = error * kp + kf;
                shooter.setPower(power);
                shooterb.setPower(power);
            } else {
                shooter.setPower(0);
                shooterb.setPower(0);
            }
             /// left bumper and x -> 1500 or 2000 depending on limelight and doesn't shoot if it's gonna miss
            /// left bumper only -> 1500 or 2000 depending on limelight 
            boolean shootingPressed = (!gamepad1.x && gamepad1.left_bumper) || (gamepad1.x &&
            gamepad1.left_bumper && willScoreIA(ta,ty));
            boolean shootingSafe = (Math.abs(error) < 30);
            if (shootingSafe && shootingPressed && aligned) {
                blocker.setPosition(BLOCKER_UNBLOCK_POSITION);
                intake.setPower(-1.0);
                transfer.setPower(-1.0);
            } else {
                blocker.setPosition(BLOCKER_BLOCK_POSITION);
                intake.setPower(0.0);
                transfer.setPower(0.0);
            }
            double fl = drive + strafe + rotate;
            double fr = drive - strafe - rotate;
            double bl = drive - strafe + rotate;
            double br = drive + strafe - rotate;

            double max = Math.max(Math.abs(fl),
                    Math.max(Math.abs(fr),
                            Math.max(Math.abs(bl), Math.abs(br))));
            if (max > 1.0) {
                fl /= max; fr /= max; bl /= max; br /= max;
            }

            frontLeft.setPower(fl);
            frontRight.setPower(fr);
            backLeft.setPower(bl);
            backRight.setPower(br);

            // ===== INTAKE CONTROL (manual, independent of shooter) =====
            if (gamepad1.right_trigger > 0.1) {
                intake.setPower(-1.0);
                transfer.setPower(-1.0);
            } else if (gamepad1.left_trigger > 0.1) {
                intake.setPower(1.0);
                transfer.setPower(1.0);
            } else if (!SHOOTING) { // avoid fighting auto-feed when shooting
                intake.setPower(0.0);
                transfer.setPower(0.0);
            }

            // Telemetry
            telemetry.addData("power", power);
            telemetry.addData("error", error);
            telemetry.addData("rotate", rotate);
            telemetry.addData("aligned", aligned);
             telemetry.addData("shootingpressed", shootingPressed);
              telemetry.addData("shootingsafe", shootingSafe);
            telemetry.addData("ta", ta);
            telemetry.addData("tx", tx);
             telemetry.addData("ty", ty);
             
            telemetry.addData("Target Velo", targetVelo);
            telemetry.addData("Current Velo", currentShooterVelocity);
            telemetry.addData("Error", error);
            telemetry.addData("Shooter Power", shooter.getPower());
            telemetry.addData("AutoAlign Active", SHOOTING && hasTarget);
            telemetry.update();
        }
    }

    private void initHardware() {
        // Drivetrain
        frontLeft  = hardwareMap.get(DcMotorEx.class, "fl");
        frontRight = hardwareMap.get(DcMotorEx.class, "fr");
        backLeft   = hardwareMap.get(DcMotorEx.class, "bl");
        backRight  = hardwareMap.get(DcMotorEx.class, "br");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Subsystems
        intake   = hardwareMap.get(DcMotorEx.class, "tra");
        transfer = hardwareMap.get(DcMotorEx.class, "trb");
        shooter  = hardwareMap.get(DcMotorEx.class, "sha");
        shooterb = hardwareMap.get(DcMotorEx.class, "shb");

        intake.setDirection(DcMotor.Direction.REVERSE);
        transfer.setDirection(DcMotor.Direction.REVERSE);
        shooter.setDirection(DcMotor.Direction.FORWARD);
        shooterb.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Servo
        blocker = hardwareMap.get(Servo.class, "blo");
        blocker.setPosition(BLOCKER_BLOCK_POSITION);

        // Limelight
        limelight = hardwareMap.get(com.qualcomm.hardware.limelightvision.Limelight3A.class, "limelight");
        limelight.setPollRateHz(10);
        limelight.start();
        limelight.pipelineSwitch(0);
    }
    int calcVelo(double ta, double ty){
        if(ty == 0)
            return 1500;
        if(ty < 1)
            return 2000;
        if(ty < 5.5)
         {
             return 1700;
         }
        if(ty < 10)
            return 1600;
        return 1500;
    }
    boolean willScoreIA(double ta, double ty){
        return true;
    }
    private double calculateTargetVelocity(double ta) {
        double min_ta = 0.285;
        double max_ta = 2.75;
        double range  = max_ta - min_ta;
        double normalized_ta;

        ta = Math.max(min_ta, Math.min(max_ta, ta));

        if (ta < 0.5) {
            normalized_ta = (ta - min_ta) / range + 0.35;
        } else if (ta < 1.35 && ta > 0.7) {
            normalized_ta = (ta - min_ta) / range + 0.25;
        } else {
            normalized_ta = (ta - min_ta) / range;
        }
        double inverted = 1.0 - normalized_ta;
        inverted = Math.max(0.0, Math.min(inverted, 1.0));

        return MIN_SHOOTER_VELOCITY +
                inverted * (MAX_SHOOTER_VELOCITY - MIN_SHOOTER_VELOCITY);
    }
}
