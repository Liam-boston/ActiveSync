package edu.psu.sweng888.activesync.dataAccessLayer.cannedData;

import android.util.Pair;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.dao.ExerciseTypeDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.MuscleGroupDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeMuscleGroupCrossRef;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

/**
 * A configuration-as-code list of all the default exercise types available at app startup.
 */
public class DefaultExerciseTypes {
    // =============================================================================================
    // ====== EDIT THIS AREA ONLY TO ADD NEW EXERCISE TYPES ========================================
    // =============================================================================================
    // BICEPS
    public static final ExerciseType DumbbellCurl = new ExerciseType(12l, "Dumbbell Curl");
    public static final ExerciseType InclineDumbbellCurl = new ExerciseType(13l, "Incline Dumbbell Curl");
    public static final ExerciseType BarbellCurl = new ExerciseType(14l, "Barbell Curl");
    public static final ExerciseType PreacherCurl = new ExerciseType(15l, "Preacher Curl");
    public static final ExerciseType ConcentrationCurl = new ExerciseType(16l, "Concentration Curl");
    public static final ExerciseType HammerCurl = new ExerciseType(17l, "Hammer Curl");
    public static final ExerciseType CableCurl = new ExerciseType(18l, "Cable Curl");

    // CHEST
    public static final ExerciseType PushUps = new ExerciseType(2l, "Push-ups");
    public static final ExerciseType BarbellBenchPress = new ExerciseType(3l, "Barbell Bench Press");
    public static final ExerciseType DumbbellBenchPress = new ExerciseType(4l, "Dumbbell Bench Press");
    public static final ExerciseType InclineBarbellBenchPress = new ExerciseType(5l, "Incline Barbell Bench Press");
    public static final ExerciseType InclineDumbbellBenchPress = new ExerciseType(6l, "Incline Dumbbell Bench Press");
    public static final ExerciseType DeclineBarbellBenchPress = new ExerciseType(7l, "Decline Barbell Bench Press");
    public static final ExerciseType DeclineDumbbellBenchPress = new ExerciseType(8l, "Decline Dumbbell Bench Press");
    public static final ExerciseType DumbbellChestFly = new ExerciseType(9l, "Dumbbell Chest Fly");
    public static final ExerciseType CableChestFly = new ExerciseType(10l, "Cable Chest Fly");
    public static final ExerciseType Dip = new ExerciseType(11l, "Dip");

    // BACK
    public static final ExerciseType PullUps = new ExerciseType(1l, "Pull-ups");
    public static final ExerciseType LatPulldown = new ExerciseType(19l, "Lat Pulldown");
    public static final ExerciseType Deadlift = new ExerciseType(20l, "Deadlift");
    public static final ExerciseType CableRow = new ExerciseType(21l, "Cable Row");
    public static final ExerciseType DumbbellBentOverRow = new ExerciseType(22l, "Dumbbell Bent Over Row");
    public static final ExerciseType BarbellBentOverRow = new ExerciseType(23l, "Barbell Bent Over Row");
    public static final ExerciseType SingleArmDumbbellRow = new ExerciseType(24l, "Single Arm Dumbbell Row");
    public static final ExerciseType TBarRow = new ExerciseType(25l, "T-Bar Row");

    // SHOULDERS
    public static final ExerciseType MilitaryPress = new ExerciseType(26l, "Military Press");
    public static final ExerciseType DumbbellShoulderPress = new ExerciseType(27l, "Dumbbell Shoulder Press");
    public static final ExerciseType ArnoldPress = new ExerciseType(28l, "Arnold Press");
    public static final ExerciseType LateralRaise = new ExerciseType(29l, "Lateral Raise");
    public static final ExerciseType FrontRaise = new ExerciseType(30l, "Front Raise");
    public static final ExerciseType RearDeltFly = new ExerciseType(31l, "Rear Delt Fly");
    public static final ExerciseType Shrug = new ExerciseType(32l, "Shrug");
    public static final ExerciseType FacePull = new ExerciseType(33l, "Face Pull");

    // TRICEPS
    public static final ExerciseType SkullCrusher = new ExerciseType(34l, "Skull Crusher");
    public static final ExerciseType CloseGripBenchPress = new ExerciseType(35l, "Close Grip Bench Press");
    public static final ExerciseType CableRopeExtension = new ExerciseType(36l, "Cable Rope Extension");
    public static final ExerciseType CablePushdown = new ExerciseType(37l, "Cable Pushdown");
    public static final ExerciseType OverheadDumbbellExtension = new ExerciseType(38l, "Overhead Dumbbell Extension");
    public static final ExerciseType SingleArmCableKickback = new ExerciseType(39l, "Single Arm Cable Kickback");
    public static final ExerciseType TricepMachineDip = new ExerciseType(40l, "Tricep Machine Dip");
    public static final ExerciseType BenchDip = new ExerciseType(41l, "Bench Dip");
    public static final ExerciseType CloseGripPushups = new ExerciseType(42l, "Close Grip Pushups");

    // QUADRICEPS
    public static final ExerciseType FrontSquat = new ExerciseType(43l, "Front Squat");
    public static final ExerciseType BackSquat = new ExerciseType(44l, "Back Squat");
    public static final ExerciseType GobletSquat = new ExerciseType(45l, "Goblet Squat");
    public static final ExerciseType BulgarianSplitSquat = new ExerciseType(46l, "Bulgarian Split Squat");
    public static final ExerciseType Lunge = new ExerciseType(47l, "Lunge");
    public static final ExerciseType WalkingLunge = new ExerciseType(48l, "Walking Lunge");
    public static final ExerciseType LegExtension = new ExerciseType(49l, "Leg Extension");
    public static final ExerciseType SingleLegLegExtension = new ExerciseType(50l, "Single Leg Leg Extension");
    public static final ExerciseType StepUps = new ExerciseType(51l, "Step-ups");

    // HAMSTRINGS
    public static final ExerciseType StraightLegDeadlift = new ExerciseType(52l, "Straight Leg Deadlift");
    public static final ExerciseType RomanianDeadlift = new ExerciseType(53l, "Romanian Deadlift");
    public static final ExerciseType DumbbellRomanianDeadlift = new ExerciseType(54l, "Dumbbell Romanian Deadlift");
    public static final ExerciseType LegCurl = new ExerciseType(55l, "Leg Curl");
    public static final ExerciseType GoodMornings = new ExerciseType(56l, "Good Mornings");
    public static final ExerciseType ReverseLunges = new ExerciseType(57l, "Reverse Lunges");
    public static final ExerciseType SumoSquat = new ExerciseType(58l, "Sumo Squat");
    public static final ExerciseType HyperExtensions = new ExerciseType(59l, "Hyper Extensions");

    // GLUTES
    public static final ExerciseType BarbellHipThrust = new ExerciseType(60l, "Barbell Hip Thrust");
    public static final ExerciseType GluteBridge = new ExerciseType(61l, "Glute Bridge");
    public static final ExerciseType GluteHamRaise = new ExerciseType(62l, "Glute Ham Raise");
    public static final ExerciseType Kickbacks = new ExerciseType(63l, "Kickbacks");
    public static final ExerciseType SingleLegCableKickback = new ExerciseType(64l, "Single Leg Cable Kickback");
    public static final ExerciseType KettlebellSwing = new ExerciseType(65l, "Kettlebell Swing");
    public static final ExerciseType LateralLunge = new ExerciseType(66l, "Lateral Lunge");

    // Prevent instantiation
    private DefaultExerciseTypes() {
    }

    // NOTE: When adding a new default exercise type, register it in the method call below with the
    //       muscle groups that it targets!
    public static List<Pair<ExerciseType, MuscleGroup[]>> getDefaultExerciseTypesWithAssociatedMuscleGroups() {
        return registerDefaults(
                // BICEPS
                exerciseTypeWithGroups(
                        DumbbellCurl,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        InclineDumbbellCurl,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        BarbellCurl,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        PreacherCurl,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        ConcentrationCurl,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        HammerCurl,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        CableCurl,
                        DefaultMuscleGroups.Biceps
                ),
                // CHEST
                exerciseTypeWithGroups(
                        PushUps,
                        DefaultMuscleGroups.Chest,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        BarbellBenchPress,
                        DefaultMuscleGroups.Chest,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        DumbbellBenchPress,
                        DefaultMuscleGroups.Chest,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        InclineBarbellBenchPress,
                        DefaultMuscleGroups.Chest,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        InclineDumbbellBenchPress,
                        DefaultMuscleGroups.Chest,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        DeclineBarbellBenchPress,
                        DefaultMuscleGroups.Chest,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        DeclineDumbbellBenchPress,
                        DefaultMuscleGroups.Chest,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        DumbbellChestFly,
                        DefaultMuscleGroups.Chest
                ),
                exerciseTypeWithGroups(
                        CableChestFly,
                        DefaultMuscleGroups.Chest
                ),
                exerciseTypeWithGroups(
                        Dip,
                        DefaultMuscleGroups.Chest,
                        DefaultMuscleGroups.Triceps
                ),
                // BACK
                exerciseTypeWithGroups(
                        PullUps,
                        DefaultMuscleGroups.Back,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        LatPulldown,
                        DefaultMuscleGroups.Back,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        Deadlift,
                        DefaultMuscleGroups.Back,
                        DefaultMuscleGroups.Hamstrings,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        CableRow,
                        DefaultMuscleGroups.Back,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        DumbbellBentOverRow,
                        DefaultMuscleGroups.Back,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        BarbellBentOverRow,
                        DefaultMuscleGroups.Back,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        SingleArmDumbbellRow,
                        DefaultMuscleGroups.Back,
                        DefaultMuscleGroups.Biceps
                ),
                exerciseTypeWithGroups(
                        TBarRow,
                        DefaultMuscleGroups.Back,
                        DefaultMuscleGroups.Biceps
                ),
                // SHOULDERS
                exerciseTypeWithGroups(
                        MilitaryPress,
                        DefaultMuscleGroups.Shoulders,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        DumbbellShoulderPress,
                        DefaultMuscleGroups.Shoulders,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        ArnoldPress,
                        DefaultMuscleGroups.Shoulders,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        LateralRaise,
                        DefaultMuscleGroups.Shoulders
                ),
                exerciseTypeWithGroups(
                        FrontRaise,
                        DefaultMuscleGroups.Shoulders
                ),
                exerciseTypeWithGroups(
                        RearDeltFly,
                        DefaultMuscleGroups.Shoulders
                ),
                exerciseTypeWithGroups(
                        Shrug,
                        DefaultMuscleGroups.Shoulders
                ),
                exerciseTypeWithGroups(
                        FacePull,
                        DefaultMuscleGroups.Shoulders
                ),
                // TRICEPS
                exerciseTypeWithGroups(
                        SkullCrusher,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        CloseGripBenchPress,
                        DefaultMuscleGroups.Triceps,
                        DefaultMuscleGroups.Chest
                ),
                exerciseTypeWithGroups(
                        CableRopeExtension,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        CablePushdown,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        OverheadDumbbellExtension,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        SingleArmCableKickback,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        TricepMachineDip,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        BenchDip,
                        DefaultMuscleGroups.Triceps
                ),
                exerciseTypeWithGroups(
                        CloseGripPushups,
                        DefaultMuscleGroups.Triceps,
                        DefaultMuscleGroups.Chest
                ),
                // QUADRICEPS
                exerciseTypeWithGroups(
                        FrontSquat,
                        DefaultMuscleGroups.Quadriceps,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        BackSquat,
                        DefaultMuscleGroups.Quadriceps,
                        DefaultMuscleGroups.Hamstrings,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        GobletSquat,
                        DefaultMuscleGroups.Quadriceps,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        BulgarianSplitSquat,
                        DefaultMuscleGroups.Quadriceps
                ),
                exerciseTypeWithGroups(
                        Lunge,
                        DefaultMuscleGroups.Quadriceps
                ),
                exerciseTypeWithGroups(
                        WalkingLunge,
                        DefaultMuscleGroups.Quadriceps
                ),
                exerciseTypeWithGroups(
                        LegExtension,
                        DefaultMuscleGroups.Quadriceps
                ),
                exerciseTypeWithGroups(
                        SingleLegLegExtension,
                        DefaultMuscleGroups.Quadriceps
                ), exerciseTypeWithGroups(
                        StepUps,
                        DefaultMuscleGroups.Quadriceps,
                        DefaultMuscleGroups.Glutes
                ),
                // HAMSTRINGS
                exerciseTypeWithGroups(
                        StraightLegDeadlift,
                        DefaultMuscleGroups.Hamstrings,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        RomanianDeadlift,
                        DefaultMuscleGroups.Hamstrings,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        DumbbellRomanianDeadlift,
                        DefaultMuscleGroups.Hamstrings,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        LegCurl,
                        DefaultMuscleGroups.Hamstrings
                ),
                exerciseTypeWithGroups(
                        GoodMornings,
                        DefaultMuscleGroups.Hamstrings
                ),
                exerciseTypeWithGroups(
                        ReverseLunges,
                        DefaultMuscleGroups.Hamstrings,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        SumoSquat,
                        DefaultMuscleGroups.Hamstrings,
                        DefaultMuscleGroups.Glutes,
                        DefaultMuscleGroups.Quadriceps
                ),
                exerciseTypeWithGroups(
                        HyperExtensions,
                        DefaultMuscleGroups.Hamstrings
                ),
                // GLUTES
                exerciseTypeWithGroups(
                        BarbellHipThrust,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        GluteBridge,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        GluteHamRaise,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        Kickbacks,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        SingleLegCableKickback,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        KettlebellSwing,
                        DefaultMuscleGroups.Glutes
                ),
                exerciseTypeWithGroups(
                        LateralLunge,
                        DefaultMuscleGroups.Glutes
                )
        );
    }

    // =============================================================================================
    // =============================================================================================
    // =============================================================================================

    private static List<Pair<ExerciseType, MuscleGroup[]>> registerDefaults(
            Pair<ExerciseType, MuscleGroup[]>... associations
    ) {
        List<Pair<ExerciseType, MuscleGroup[]>> defaults = new ArrayList<>();
        for (Pair<ExerciseType, MuscleGroup[]> association : associations) {
            defaults.add(association);
        }
        return defaults;
    }

    private static Pair<ExerciseType, MuscleGroup[]> exerciseTypeWithGroups(
            ExerciseType exerciseType,
            MuscleGroup... groups
    ) {
        return new Pair<>(exerciseType, groups);
    }


    public static void initialize(ActiveSyncDatabase db) {
        // TODO: Should we try to make sure this method is only called once per application run?
        //       That may be premature optimization since we're using an insert statement that
        //       ignores conflicts, but I'm not sure if there are other problems I'm unaware of.
        List<Pair<ExerciseType, MuscleGroup[]>> defaults =
                getDefaultExerciseTypesWithAssociatedMuscleGroups();

        HashSet<Long> registeredMuscleGroupIds = new HashSet<>(); // Keep track of what we've inserted so we don't double insert. Not a huge problem, but also easy to avoid.
        ExerciseTypeDao exerciseTypeDao = db.exerciseTypeDao();
        MuscleGroupDao muscleGroupDao = db.muscleGroupDao();
        for (Pair<ExerciseType, MuscleGroup[]> exerciseTypeAndGroupsPair : defaults) {
            // Pull items out of the pair
            ExerciseType exerciseType = exerciseTypeAndGroupsPair.first;
            MuscleGroup[] associatedMuscleGroups = exerciseTypeAndGroupsPair.second;

            // Ensure the exercise type exists in the database.
            exerciseTypeDao.ensureInserted(exerciseType);

            // Register entries for the muscle groups associated with the exercise first (if we
            // haven't already done so). Create a link between each muscle group and the exercise
            // type we added.
            for (MuscleGroup muscleGroup : associatedMuscleGroups) {
                muscleGroupDao.ensureInserted(muscleGroup);
                exerciseTypeDao.ensureMuscleGroupLinkedToExerciseType(
                        new ExerciseTypeMuscleGroupCrossRef(
                                exerciseType,
                                muscleGroup
                        )
                );
            }

        }
    }
}
