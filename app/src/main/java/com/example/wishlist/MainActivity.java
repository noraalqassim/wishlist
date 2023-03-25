package com.example.wishlist;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends FragmentActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment_Show_All_Category()).commit();
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId( R.id.itemHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.itemHome:
                    fragment = new Fragment_Show_All_Category();
                    break;
                case R.id.itemCategory:
                    fragment = new FragmentAddProduct();
                    break;
                case R.id.itemMenuShop:
                    fragment = new Fragment_Add_Category();
                    break;
                case R.id.itemAccount:
                    fragment = new UserFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
