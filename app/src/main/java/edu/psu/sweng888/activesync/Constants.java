package edu.psu.sweng888.activesync;

public final class Constants {

    public static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

    public static final double KG_TO_LBS_FACTOR = 2.204623;

    public static final double LBS_TO_KG_FACTOR = 1 / KG_TO_LBS_FACTOR;

    private Constants() { }

    public static final String EXTRAS_KEY_WORKOUT_TO_EDIT = "workout_to_edit";

    public static final String EXTRAS_KEY_USER_DISPLAY_NAME = "user_display_name";

    public static final String EXTRAS_KEY_USER_EMAIL_ADDRESS = "user_email_address";

    public static final String USER_INFO_PLACEHOLDER = "<Unknown>";

    public static final String EXTRAS_KEY_DEBUG_USE_TEST_USER = "debug_use_test_user";
}
