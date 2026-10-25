package org.firstinspires.ftc.teamcode.Match.TeleOp; 
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode; 
import com.qualcomm.hardware.limelightvision.LLResult; 
import com.qualcomm.robotcore.eventloop.opmode.TeleOp; 
import com.qualcomm.robotcore.hardware.DcMotorEx; 
import com.qualcomm.robotcore.hardware.DcMotorSimple; 
import com.qualcomm.robotcore.hardware.Servo; 
import com.qualcomm.robotcore.util.ElapsedTime; 
import com.qualcomm.robotcore.hardware.ControlSystem; 
import com.qualcomm.robotcore.hardware.VoltageSensor; 

// Use ControlHub for easy access since SDK 8.0+ // IMU specific imports // AprilTag specific imports /** * {@link TeleOpPreviewEvent} is a basic TeleOp OpMode for the DECODE 2025-2026 season. * This OpMode implements Field-Centric Mecanum drive and auxiliary controls, including a * quadratic function for fly-by launch velocity calculation. * Adapted from SWYFT-Itkan Ri3d demonstration code */ 
@TeleOp(name = "Jan 31st", group = "Match") 
//@Disabled 
public class newCODEE extends LinearOpMode { private ElapsedTime runtime = new ElapsedTime(); 
 private DcMotorEx leftFront = null; 
 private DcMotorEx rightFront = null; 
 private DcMotorEx leftBack = null; 
 private DcMotorEx rightBack = null; 
 // Mechanism Motors 
 private DcMotorEx intake = null; 
 private DcMotorEx shooterL= null; 
 // Using DcMotorEx to get velocity 
 private DcMotorEx shooterR = null; 
 //private DcMotorEx motorLift = null; 
 private DcMotorEx transfer= null; 
 private Servo blocker = null;
 private Servo targetFlag = null;

 boolean runTransfer = false;
 private com.qualcomm.hardware.limelightvision.Limelight3A limelight;
 // Servos 
 //private Servo servoHold = null;
 double limelightta = 0, limelighttx = 0, limelightty = 0 ; 
            
 boolean velocityTargetReached = false;
 boolean foundTarget = false;
 boolean targetAligned = false;
 boolean alignTarget = true;
 double turnspeed = 0.01;
                   
 final double UNBLOCK = 0.32;
 final double BLOCK = 0.15; 
 final double TARGETON = 0.3;
 final double TARGETOFF = 0.5;
 final double TARGETFOUND = 0;
 final int velocityError = 10;
 final int unblockTimeinMilliseconds = 200;
 double shooterSlowdownPower = 0.5;
 // static final double CLIMB_POWER = 1.0; 
 @Override 
 public void runOpMode() { 
     telemetry.addData("Status", "Initializing..."); 
     telemetry.update(); 
     // --- HARDWARE MAPPING --- 
     ini(); 
     waitForStart(); 
     runtime.reset(); 
     while (opModeIsActive()) { 
         //----------------// // MECANUM DRIVE // //----------------//
         double rx = gamepad1.right_stick_x; 
         double x = gamepad1.left_stick_x; 
         double y = -gamepad1.left_stick_y; 
         //----------------------------// // INTAKE AND SHOOTING LOGIC // //----------------------------// 
         if (gamepad1.right_trigger > 0.1){ 
             intake.setPower(0.75); transfer.setPower(0.75); 
              blocker.setPosition(BLOCK);
             
         } 
         else if (gamepad1.left_trigger > 0.1){ 
             intake.setPower(-0.75); transfer.setPower(-0.75); 
             
         } 
         else{ 
             intake.setPower(0); transfer.setPower(0); 
             
         } 
          int targetV = 0; 
         boolean shootingPressed = false; 
         if(gamepad1.x || gamepad2.x){ 
             targetV = 1250; shootingPressed = true; 
             turnspeed = 0.02;
             shooterSlowdownPower = 0.32;
                   
         } 
         if(gamepad1.a || gamepad2.a){ 
             targetV = 1400; shootingPressed = true; 
             turnspeed = 0.02;
             shooterSlowdownPower = 0.32;
         } 
         if(gamepad1.b || gamepad2.b){
             targetV = 1500; shootingPressed = true; 
             turnspeed = 0.02;
             shooterSlowdownPower = 0.31;
         } 
         if(gamepad1.y || gamepad2.y){ 
             targetV = 1850; shootingPressed = true; 
             turnspeed = 0.01;
             shooterSlowdownPower = 0.60;
         } 
         
         
         if(gamepad1.back || gamepad2.back) { 
              alignTarget = false;
             } 
             
          if(gamepad1.start || gamepad2.start){ 
          
              alignTarget = true;
              targetFlag.setPosition(TARGETON);
             } 
        
         if (alignTarget){
             targetFlag.setPosition(TARGETON);
             
            }
            else {
             targetFlag.setPosition(TARGETOFF);
            }
           
        double currentVelocity = shooterL.getVelocity();
        if (shootingPressed) {
            intake.setPower(1);
            transfer.setPower(1);
            if(runTransfer){
                
            }
            if (currentVelocity < (targetV + velocityError)) { // deadband prevents jitter
                velocityTargetReached = false;
                shooterR.setPower(1);
                shooterL.setPower(1);
               
            } else {
                velocityTargetReached = true;
                shooterR.setPower(shooterSlowdownPower);
                shooterL.setPower(shooterSlowdownPower);
                
            }
            if (alignTarget){
              targetAligned = false;
              limelight.start(); 
              limelight.pipelineSwitch(0); 
              LLResult result = limelight.getLatestResult(); 
              limelightta = 0;
              limelighttx = 0;
              limelightty = 0; 
              foundTarget = result != null && result.isValid(); 
              targetAligned = false; 
              if (foundTarget) { 
                   targetFlag.setPosition(TARGETFOUND);
                   limelightta = result.getTa(); 
                   limelighttx = result.getTx();
                   limelightty = result.getTy();
                   double headingErrorThreshold = 1;
                   double headingError = limelighttx;
                   if (Math.abs(headingError) > headingErrorThreshold) {
                    //rx = headingError * 0.01 + 0.1 * Math.signum(headingError);
                    
                    rx = headingError * turnspeed + 0.1 * Math.signum(headingError);
                          
                    
                   } 
                   else { 
                           rx = 0;
                           // within tolerance, stop turning 
                           targetAligned = true; 
                           limelight.stop(); 
                       } 
                   
               } 
             } else {
              targetAligned = true;
              
                   
             }
             if(targetAligned && velocityTargetReached){
                    runTransfer = true;
                 blocker.setPosition(UNBLOCK);
                 ElapsedTime timer = new ElapsedTime();
                 while(timer.milliseconds() <= unblockTimeinMilliseconds){
                    currentVelocity = shooterL.getVelocity();
                    if (currentVelocity > targetV ) { 
                        shooterR.setPower(shooterSlowdownPower);
                        shooterL.setPower(shooterSlowdownPower);
                       
                    } else {
                        shooterR.setPower(1);
                        shooterL.setPower(1);
                        
                    }
                   }
             
                 
             }
             
        } else {
            shooterR.setPower(0);
            shooterL.setPower(0);
            runTransfer = false;
            blocker.setPosition(BLOCK); //Added this code
            targetAligned = false;
            limelight.stop(); 
            limelightta = 0;
            limelighttx = 0;
            limelightty = 0 ; 
            foundTarget = false;
             
        }
         
         double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1); 
         double frontLeftPower = (y + x + rx) / denominator; 
         double backLeftPower = (y - x + rx) / denominator; 
         double frontRightPower = (-y + x +rx) / denominator; 
         double backRightPower = (-y - x + rx) / denominator; 
         
         leftFront.setPower(frontLeftPower); 
         leftBack.setPower(backLeftPower); 
         rightFront.setPower(frontRightPower); 
         rightBack.setPower(backRightPower); 
 
         
        telemetry.addData("Actual Velocity", "%.2f", shooterL.getVelocity());
        telemetry.addData("Target Velocity", targetV);
        telemetry.addData("Limelight sees Target?", foundTarget);
        telemetry.addData("Limelight Tx", limelighttx); 
        telemetry.addData("Limelight Ty", limelightty);
        telemetry.addData("Limelight Ta", limelightta); 
        telemetry.addData("rx", rx);
        telemetry.addData("Limelight Aligned?", targetAligned);
        
        // BATTERY VOLTAGE 
        telemetry.update();     
     
         
     }
 } 
 public void ini(){ 
     leftFront = hardwareMap.get(DcMotorEx.class, "motorLeftFront"); 
     rightFront = hardwareMap.get(DcMotorEx.class, "motorRightFront"); 
     leftBack = hardwareMap.get(DcMotorEx.class, "motorLeftBack"); 
     rightBack = hardwareMap.get(DcMotorEx.class, "motorRightBack"); 
     transfer = hardwareMap.get(DcMotorEx.class, "Transfermotor");
     intake = hardwareMap.get(DcMotorEx.class, "motorIntake");
     shooterL = hardwareMap.get(DcMotorEx.class, "motorTurretLeft"); 
     // Mapped as DcMotorEx to read encoder
     shooterR = hardwareMap.get(DcMotorEx.class, "motorTurretRight");
     //motorLift = hardwareMap.get(DcMotorEx.class, "motorLift");
     //servoHold = hardwareMap.get(Servo.class, "servoHold");
     // --- MOTOR DIRECTION --- 
     leftFront.setDirection(DcMotorEx.Direction.FORWARD);
     leftBack.setDirection(DcMotorEx.Direction.FORWARD);
     rightFront.setDirection(DcMotorEx.Direction.FORWARD);
     rightBack.setDirection(DcMotorEx.Direction.FORWARD);
     shooterR.setDirection(DcMotorEx.Direction.REVERSE);
     shooterL.setDirection(DcMotorEx.Direction.FORWARD);
     intake.setDirection(DcMotorEx.Direction.FORWARD);
     transfer.setDirection(DcMotorEx.Direction.FORWARD); 
     //motorLift.setDirection(DcMotorEx.Direction.FORWARD); 
     blocker = hardwareMap.get(Servo.class, "servoHold"); 
     targetFlag = hardwareMap.get(Servo.class, "servoTargetFlag");
     limelight = hardwareMap.get(com.qualcomm.hardware.limelightvision.Limelight3A.class, "limelight"); 
     //limelight.setPollRateHz(10); 
     limelight.pipelineSwitch(0); 
     // --- MOTOR BEHAVIOR ---
     // Drivetrain and Climber set to BRAKE
     leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
     rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
     leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
     rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
     //motorLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
     // Intake and Shooters set to FLOAT (Coast) 
     intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT); 
     shooterL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT); 
     shooterR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT); 
     transfer.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT); 
     // --- ENCODER SETUP FOR SHOOTER --- 
     shooterL.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER); 
     shooterR.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER); 
     // Set other motors to run without encoders 
     leftFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); 
     rightFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); 
     leftBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); 
     rightBack.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); 
     intake.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); 
     shooterR.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER); 
     shooterL.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
     //motorLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); 
     blocker.setDirection(Servo.Direction.FORWARD); 
     blocker.setPosition(BLOCK);
     targetFlag.setDirection(Servo.Direction.FORWARD); 
     targetFlag.setPosition(TARGETON);
      
 } 
    
}