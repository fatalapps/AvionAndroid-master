package com.avion.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.avion.app.action.EmailConf;
import com.avion.app.action.RegionResponse;
import com.avion.app.entity.CountryEntity;
import com.avion.app.entity.Currency;
import com.avion.app.entity.RegionEntity;
import com.avion.app.repository.UserRepository;
import com.avion.app.unit.NekConfigs;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static RegionResponse chooseRegion = new RegionResponse(280, "Москва");
    public static MutableLiveData<RegionEntity> chooseRegionLiveData = new MutableLiveData<>();
    public static MutableLiveData<CountryEntity> countryData = new MutableLiveData<>();

    private DrawerLayout drawerLayout;
    private static NavigationView navigationView;
    private NavController navController;

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private UserRepository userRepository;
    private static Context cont;
    private ImageView main_top_menu_imageview;
    public static String bbalance = "0";
    private TextView main_top_name_textView;
    private static TextView main_top_name_textView_st;
    private ImageView main_top_call_imageView;
    public static String curr_itemm = "";
    public static String last_reg = "";
    public static boolean attached = false;
    public static MutableLiveData<Boolean> attached_live = new MutableLiveData<>();
    public static Boolean trans_paid = false;
    public static Currency currency;

    public static String getCurrencySign(int id) {
        switch (id) {
            case 1:
                return "\u20BD";
            case 2:
                return "€";
            case 3:
                return "$";
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logstr("RESULT_INCOMING req:" + requestCode + " res:" + resultCode);
        if (requestCode == 11) {
            if (resultCode == -1) {
                logstr("ORDER HAS BEEN PAID");
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(new Date().getTime()));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "android_in_app_purchase");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "android_in_app_purchase");
                NekConfigs.analytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);
                trans_paid = true;
            } else {
                Snackbar.make(main_top_call_imageView, getString(R.string.paying_error), Snackbar.LENGTH_SHORT)
                        .show();
                trans_paid = false;
            }
        } else trans_paid = false;
        if (requestCode == 33) {
            if (resultCode == -1) {
                logstr("CARD HAS BEEN ATTACHED");
                attached_live.setValue(true);
            } else attached_live.setValue(false);
        } else attached_live.setValue(false);
    }

    public static int is_photo = 0;

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = "ch_n";//getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_id);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("NekFCM", "Token created " + task.getResult().getToken());
                        String token = task.getResult().getToken();
                        new UserRepository().refisterFirebase(token);

                        FirebaseFirestore.getInstance().collection("users_mails")

                                .get()
                                .addOnCompleteListener(taskk -> {
                                    for (QueryDocumentSnapshot documentSnapshot : taskk.getResult()) {

                                        EmailConf email_data = documentSnapshot.toObject(EmailConf.class);
                                        logstr("d: " + documentSnapshot.getId() + NekConfigs.getUserPgone());
                                        email_data.setPhone(documentSnapshot.getId());
                                        if (email_data.getPhone().equals(NekConfigs.getUserPgone())) {
                                            NekConfigs.mail = email_data.getEmail();
                                        } else {
                                            MainActivity.logstr(email_data.getPhone() + " m!= " + NekConfigs.getUserPgone());
                                        }
                                    }

                                });

                    } else {
                        Log.e("NekFCM", "Token not created " + task.getException().getLocalizedMessage());
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Locale locale = new Locale("ru");
        Locale.setDefault(locale);
        Bundle bundle_notif = getIntent().getExtras();
        navController = Navigation.findNavController(this, R.id.main_nav);
        if (bundle_notif != null) {
            if (bundle_notif.getString("orderID") != null) {
                String ordID = bundle_notif.getString("orderID");
                Bundle bundle_1 = new Bundle();
                bundle_1.putString("order_id", ordID);
                navController.navigate(R.id.orderInfoFragment, bundle_1);
            }
        }
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, null);
        MainActivity.cont = this;

        main_top_menu_imageview = findViewById(R.id.main_top_menu_imageview);
        main_top_name_textView = findViewById(R.id.main_top_name_textView);
        main_top_name_textView_st = findViewById(R.id.main_top_name_textView);
        main_top_call_imageView = findViewById(R.id.main_top_call_imageView);
        chooseRegionLiveData.setValue(new RegionEntity(280, "Москва"));


        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);


        Navigation.setViewNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {


                    drawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {
                        case R.id.menu_ur_trips:
                            navController.navigate(R.id.menu_orderlist);
                            break;
                        case R.id.menu_payment:
                            navController.navigate(R.id.menu_topayment);
                            break;
                        case R.id.menu_favorites_address:
                            navController.navigate(R.id.menu_favoriteaddress);
                            break;
                        case R.id.menu_promocodes:
                            navController.navigate(R.id.menu_topromocodes);
                            break;
                        case R.id.menu_helpDesc:


                            String url = "https://avion-d0512.firebaseapp.com/";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            break;

                        case R.id.menu_exit:
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(this, SplashActivity.class));
                            this.finish();
                    }


                    return true;
                });

        setUpUserHeader();
        main_top_call_imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+74996775588"));
            startActivity(intent);
        });
        main_top_menu_imageview.setOnClickListener(openMenuClick);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {

                case R.id.profileFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentProfile));
                    break;
                case R.id.makeOrderFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentMakeOrder));
                    break;
                case R.id.driverCommentFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentComment));
                    break;
                case R.id.chooseCountryFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentCode));
                    break;
                case R.id.orderInfoFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentOrder));
                    break;
                case R.id.favoriteAddressListFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentFavourite));
                    break;
                case R.id.choosePaymentFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentPayment));
                    break;
                case R.id.chooseTimeFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentTime));
                    break;
                case R.id.chooseOptionsFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentOptions));
                    break;
                case R.id.meetWTableFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentTable));
                    break;
                case R.id.chooseLanguageFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentLanguage));
                    break;
                case R.id.cheldrenSeatsFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentKreslo));
                    break;
                case R.id.pickUpAddressFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentPoint));
                    break;
                case R.id.choosePointAddressFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentPoint));
                    break;
                case R.id.orderListFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentTrips));
                    break;
                case R.id.feedBackFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentReview));
                    break;
                case R.id.createNewFavoriteAddressFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentAddAddress));
                    break;
                case R.id.promocodeFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentPromo));
                    break;
                case R.id.chooseRegionFragment:
                    main_top_name_textView.setText(getString(R.string.TopFragmentReg));
                    break;
                case R.id.lulkaFragment:
                    main_top_name_textView.setText(getString(R.string.lulka));
                    break;
                default:
                    main_top_name_textView.setText(getString(R.string.TopFragmentMakeOrder));
                    break;
            }
            if (destination.getId() == R.id.makeOrderFragment) {
                main_top_menu_imageview.setOnClickListener(openMenuClick);
                main_top_menu_imageview.setImageDrawable(getDrawable(R.drawable.ic_menu_black_24dp));
            } else {
                main_top_menu_imageview.setOnClickListener(backClick);
                main_top_menu_imageview.setImageDrawable(getDrawable(R.drawable.ic_keyboard_backspace_black_24dp));
            }
        });

        if (getIntent().getStringExtra("nek_order_id") != null) {
            Log.d("Nek", "nek_order_id is not null");
            Bundle bundle = new Bundle();
            bundle.putString("order_id", getIntent().getStringExtra("nek_order_id"));
            navController.navigate(R.id.orderInfoFragment, bundle);
        }


    }

    public static void changeTopName(String name) {
        main_top_name_textView_st.setText(name);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private View.OnClickListener openMenuClick = v -> {
        updateMenuBalance();
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            drawerLayout.openDrawer(GravityCompat.START);
    };
    private View.OnClickListener backClick = v -> {
        onBackPressed();
    };

    @Override
    public void onBackPressed() {
        navigationView.getMenu().setGroupCheckable(0, false, true);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawers();
                else
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+74996775588"));
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.top_main_menu, menu);
        return true;
    }

    public static LifecycleOwner owner;

    private void setUpUserHeader() {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }
        if (userRepository == null)
            userRepository = new UserRepository();
        final View headerView = navigationView.getHeaderView(0);
        final CircleImageView menu_user_circleImageView = headerView.findViewById(R.id.menu_user_circleImageView);
        final TextView menu_user_name_txtview = headerView.findViewById(R.id.menu_user_name_txtview);
        final TextView menu_user_bonusses_txtview = headerView.findViewById(R.id.menu_user_bonusses_txtview);
        if (currentUser.getDisplayName() != null)
            menu_user_name_txtview.setText(currentUser.getDisplayName());
        if (currentUser.getPhotoUrl() != null) {
            Logger.d("PHOTO: " + currentUser.getPhotoUrl());
            Picasso.get().load(currentUser.getPhotoUrl()).rotate(90).into(menu_user_circleImageView);
            userPhoto = currentUser.getPhotoUrl();
        }
        headerView.setOnClickListener(v -> {
            navController.navigate(R.id.menu_userProfile);
            drawerLayout.closeDrawers();
        });
        owner = this;

        userRepository.getUser(currentUser.getUid()).observe(this, user -> {
            if (user == null)
                return;

        });


    }

    public static void updateMenuBalance() {
        logstr("UPDATE_BALANCE_MENU!!! ");
        final View headerView = navigationView.getHeaderView(0);
        final TextView menu_user_bonusses_txtview = headerView.findViewById(R.id.menu_user_bonusses_txtview);
        new UserRepository().getLivePromoBalance().observe(owner, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.length() > 0) {
                    Logger.e("UPDATE_BALANCE_MENU: " + s);
                    updateBalance(s);
                }
            }
        });
    }

    public static void updateBalance(String b) {
        bbalance = String.valueOf((int) Double.parseDouble(b));
        final View headerView = navigationView.getHeaderView(0);
        final TextView menu_user_bonusses_txtview = headerView.findViewById(R.id.menu_user_bonusses_txtview);
        menu_user_bonusses_txtview.setText(getAppContext().getString(R.string.bonuses) + " " + bbalance);
    }

    public static CircleImageView getcircle() {
        final View headerView = navigationView.getHeaderView(0);
        return headerView.findViewById(R.id.menu_user_circleImageView);

    }

    public static void setNewUserPhoto(Uri userPhoto) {
        final View headerView = navigationView.getHeaderView(0);
        final CircleImageView menu_user_circleImageView = headerView.findViewById(R.id.menu_user_circleImageView);
        Logger.d("PHOTO!: " + userPhoto);
        Picasso.get().load(userPhoto).transform(new ExifTransformation(getAppContext(), userPhoto)).into(menu_user_circleImageView);

        MainActivity.userPhoto = userPhoto;
    }

    public static Context getAppContext() {
        return MainActivity.cont;
    }

    public static Uri userPhoto;

    public static void setNewUserName(String name) {
        final View headerView = navigationView.getHeaderView(0);
        final TextView menu_user_name_txtview = headerView.findViewById(R.id.menu_user_name_txtview);
        menu_user_name_txtview.setText(name);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static void logstr(String str) {
        Logger.d("MAIN_LOGGER:\r\n" + str);
    }

}
