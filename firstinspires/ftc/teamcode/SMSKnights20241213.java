// import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
// import com.qualcomm.robotcore.hardware.DcMotorEx;
// import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
// import com.qualcomm.robotcore.hardware.DcMotor;
// import com.qualcomm.robotcore.hardware.Servo;

// @Autonomous
// public class SMSKnights20241213 extends LinearOpMode {

//     DcMotorEx fl, fr, bl, br, arm;
//     Servo claw, wrist;

//     // Constants for servo positions and movement times
//     final double CLAW_OPEN_POSITION = 0.3;
//     final double CLAW_CLOSED_POSITION = 0.05;
//         final double WRIST_POSITION_DPAD_START = 0.1;
//     final double WRIST_POSITION_DPAD_LEFT = 0.3;
//         final double WRIST_POSITION_DPAD_UP = 0.95;
//     final double DRIVE_POWER = 0.5;
//     final int FORWARD_TIME_MS = 600;
//     final int FORWARD1_TIME_MS = 1000; 
//     final int FORWARD2_TIME_MS = 800;
//     final int FORWARD3_TIME_MS = 900;
//     final int ARM_RAISE_POSITION = -9850;
//      final int ARM_START_POSITION = -8000;
//     final int BACKUP_TIME_MS = 350;
//     final int BACKUP1_TIME_MS = 1150;
//     final int STRAFE_TIME_MS = 900;
//     final int STRAFE1_TIME_MS = 380; 
//     final int STRAFE2_TIME_MS = 250;
//     final int TURN_TIME_MS = 445;

//     @Override
//     public void runOpMode() {
//         // Initialize motors and servos
//         fl = hardwareMap.get(DcMotorEx.class, "fl");
//         fr = hardwareMap.get(DcMotorEx.class, "fr");
//         bl = hardwareMap.get(DcMotorEx.class, "bl");
//         br = hardwareMap.get(DcMotorEx.class, "br");
//         arm = hardwareMap.get(DcMotorEx.class, "arm");
//         claw = hardwareMap.get(Servo.class, "claw");
//         wrist = hardwareMap.get(Servo.class, "wrist");

//         // Set motor directions
//         fl.setDirection(DcMotorEx.Direction.REVERSE);
//         bl.setDirection(DcMotorEx.Direction.REVERSE);
//         br.setDirection(DcMotorEx.Direction.FORWARD);
//         fr.setDirection(DcMotorEx.Direction.FORWARD);
//         arm.setDirection(DcMotorEx.Direction.REVERSE);

//         // Set zero power behavior
//         fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//         fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//         bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//         br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//         arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        

//     // Set up encoder mode for arm
//         arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//         arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//         claw.setPosition(CLAW_CLOSED_POSITION);

//         waitForStart();

//         if (opModeIsActive()) {
//             // Step 1: Drive forward
//             wrist.setPosition(WRIST_POSITION_DPAD_START);
//             driveBackward(DRIVE_POWER, FORWARD_TIME_MS);
//             sleep(500);

//             // Step 2: Raise the arm
//             wrist.setPosition(WRIST_POSITION_DPAD_LEFT);
//             sleep(500);
//             moveArmToPosition(ARM_RAISE_POSITION);
//             sleep(600);
//             claw.setPosition(CLAW_OPEN_POSITION);
//             // Step 3: Initial backward movement
//             driveForward(DRIVE_POWER, BACKUP_TIME_MS);
//             sleep(500);
// moveArmToPosition(ARM_START_POSITION);
      
//             // Step 4: Strafe right
//             strafeRight(DRIVE_POWER, STRAFE_TIME_MS);
//             sleep(500);
//   //turnRight(DRIVE_POWER * 0.3, 200); // Reduced power and short duration for a slight left turn
         
//             // Step 5: Move forward
//             driveBackward(DRIVE_POWER, FORWARD1_TIME_MS);
//             sleep(500);

//             // Step 5.1: Adjust alignment by turning left slightly
      

//             // Step 6: Strafe right again
//             strafeRight(DRIVE_POWER, STRAFE1_TIME_MS);
//             sleep(500);
//   //turnRight(DRIVE_POWER * 0.3, 360); // Reduced power and short duration for a slight left turn
        
//             // Step 7: Move forward
//             driveForward(DRIVE_POWER, BACKUP1_TIME_MS);
// //turnRight(DRIVE_POWER * 0.3, 360); // Reduced power and short duration for a slight left turn
//             sleep(500);
//             // Step 8: Move backward
//             driveBackward(DRIVE_POWER, FORWARD1_TIME_MS);
//             sleep(500);

//             // Step 9: Strafe right
//          strafeRight(DRIVE_POWER, STRAFE2_TIME_MS);
//             sleep(500);
// //turnRight(DRIVE_POWER * 0.3, 360); // Reduced power and short duration for a slight left turn
       
//             // Step 10: Move forward
//             driveForward(DRIVE_POWER, BACKUP1_TIME_MS);
//             sleep(500);

//             // Step 11: Move backward
//             driveBackward(DRIVE_POWER, FORWARD1_TIME_MS);
//             sleep(500);

//             // Step 12: Strafe right
//             strafeRight(DRIVE_POWER, STRAFE1_TIME_MS);
//             sleep(500);
// //turnRight(DRIVE_POWER * 0.3, 360); // Reduced power and short duration for a slight left turn
        
//             // Step 13: Move forward
//             driveForward(DRIVE_POWER, BACKUP1_TIME_MS);
//             sleep(500);

//             // Step 14: Move backward
//             driveBackward(DRIVE_POWER, FORWARD3_TIME_MS);
//             sleep(500);

//             // Step 15: Turn right
//             turnRight(DRIVE_POWER, TURN_TIME_MS);
//             sleep(500);
//  wrist.setPosition(WRIST_POSITION_DPAD_LEFT);
//             sleep(500);
//             // Step 16: Raise arm
//             moveArmToPosition(ARM_RAISE_POSITION); // Adjust as needed
//             sleep(500);

//             // Step 17: Final forward movement
//             driveBackward(DRIVE_POWER, FORWARD2_TIME_MS);

//             // Stop all motors at the end
//             stopMotors();
//         }
//     }

//     // Method to move the arm to a specific position using encoder
//     private void moveArmToPosition(int position) {
//         arm.setTargetPosition(position);
//         arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//         arm.setPower(1); // Power to move the arm
//         while (arm.isBusy() && opModeIsActive()) {
//             telemetry.addData("Arm Position", arm.getCurrentPosition());
//             telemetry.update();
//         }
//         arm.setPower(0); // Stop the arm motor
//     }

//     // Method to drive forward for a specified time
//     private void driveForward(double power, int timeMs) {
//         fl.setPower(power);
//         fr.setPower(power);
//         bl.setPower(power);
//         br.setPower(power);
//         sleep(timeMs); // Run motors for the specified time
//         stopMotors();
//     }

//     // Method to drive backward for a specified time
//     private void driveBackward(double power, int timeMs) {
//         fl.setPower(-power);
//         fr.setPower(-power);
//         bl.setPower(-power);
//         br.setPower(-power);
//         sleep(timeMs); // Run motors for the specified time
//         stopMotors();
//     }

//     // Method to strafe right for a specified time
//     private void strafeRight(double power, int timeMs) {
//         fl.setPower(power);
//         fr.setPower(-power);
//         bl.setPower(-power);
//         br.setPower(power);
//         sleep(timeMs); // Run motors for the specified time
//         stopMotors();
//     }

//     // Method to turn right for a specified time
//     private void turnRight(double power, int timeMs) {
//         fl.setPower(power);
//         fr.setPower(-power);
//         bl.setPower(power);
//         br.setPower(-power);
//         sleep(timeMs); // Run motors for the specified time
//         stopMotors();
//     }

//     // Method to turn left for a specified time
//     private void turnLeft(double power, int timeMs) {
//         fl.setPower(-power);
//         fr.setPower(power);
//         bl.setPower(-power);
//         br.setPower(power);
//         sleep(timeMs); // Run motors for the specified time
//         stopMotors();
//     }

//     // Method to stop all motors
//     private void stopMotors() {
//         fl.setPower(0);
//         fr.setPower(0);
//         bl.setPower(0);
//         br.setPower(0);
//     }
// }

