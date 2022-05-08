package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.MenuItemCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ShopListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopListActivity.class.getName();
    private FirebaseUser user;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;
    private int gridNumber = 1;
    private Integer itemLimit = 100;
    Button notifyBtn;

    public String selectedFilter = "all";
    public static ArrayList<ShoppingItem> itemList = new ArrayList<ShoppingItem>();

    // Member variables.
    private ListView listView;
    private RecyclerView mRecyclerView;
    private ArrayList<ShoppingItem> mItemsData;
    private ShoppingRateTypeAdapter mAdapter;


    private NotificationHelper mNotificationHelper;


    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private SharedPreferences preferences;

    private boolean viewRow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        notifyBtn = findViewById(R.id.add_to_cart);
        listView = findViewById(R.id.itemListView);
        // Set the Layout Manager.



        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();
        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ShoppingRateTypeAdapter(this, 0, mItemsData);
        listView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        queryData();



        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver, filter);



        // Intent intent = new Intent("CUSTOM_MOBALKFEJL_BROADCAST");
        // LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();

            if (intentAction == null)
                return;

            switch (intentAction) {
                case Intent.ACTION_POWER_CONNECTED:
                    itemLimit = 10;
                    queryData();
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    itemLimit = 5;
                    queryData();
                    break;
            }
        }
    };

  //  private void setNotifyBtn(){
//
  //      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
  //          NotificationChannel channel = new NotificationChannel("My notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
  //          NotificationManager manager = getSystemService(NotificationManager.class);
  //          manager.createNotificationChannel(channel);
  //      }
//
  //      notifyBtn.setOnClickListener(v -> {
  //          NotificationCompat.Builder builder = new NotificationCompat.Builder(ShopListActivity.this, "My notification");
  //          builder.setContentTitle("Title");
  //          builder.setContentText("valamiszoveg");
  //          builder.setSmallIcon(R.drawable.icshopping_cart);
  //          builder.setAutoCancel(true);
//
  //          NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ShopListActivity.this);
  //          managerCompat.notify(1, builder.build());
  //          startActivity(notifyBtn);
  //      });
//
  //  }

    private void startActivity(Button notifyBtn) {
    }


    private void initializeData() {

        // Get the resources from the XML file.
        String[] itemsList = getResources()
                .getStringArray(R.array.shopping_item_names);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.shopping_item_desc);
        String[] itemsPrice = getResources()
                .getStringArray(R.array.shopping_item_price);
        TypedArray itemsImageResources =
                getResources().obtainTypedArray(R.array.shopping_item_images);
        String[] itemRate = getResources().getStringArray(R.array.shopping_item_rates);

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < itemsList.length; i++) {
            mItems.add(new ShoppingItem(
                    itemsList[i],
                    itemsInfo[i],
                    itemsPrice[i],
                    itemRate[i],
                    itemsImageResources.getResourceId(i, 0),
                    0));
        }

        // Recycle the typed array.
        itemsImageResources.recycle();
    }


    private void setUpList() {


        Log.d(LOG_TAG, "Itjjooooooooo: ");
        listView = (ListView) findViewById(R.id.itemListView);

        ShoppingRateTypeAdapter adapter = new ShoppingRateTypeAdapter(getApplicationContext(), 0, mItemsData);
        listView.setAdapter(adapter);
    }


    private void setUpOnclickListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ShoppingItem selectItem = (ShoppingItem) (listView.getItemAtPosition(position));
                Intent showDetail = new Intent(getApplicationContext(), ShoppingRateTypeAdapter.class);
                showDetail.putExtra("id", selectItem._getId());
                startActivity(showDetail);
            }
        });
    }


    private void queryData() {
        mItemsData.clear();
        mItems.orderBy("cartedCount", Query.Direction.DESCENDING).limit(itemLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ShoppingItem item = document.toObject(ShoppingItem.class);
                item.setId(document.getId());
                mItemsData.add(item);
            }

            if (mItemsData.size() == 0) {
                initializeData();setUpList();
                queryData();
            }

            // Notify the adapter of the change.
            mAdapter.notifyDataSetChanged();
        });
    }


    public void deleteItem(ShoppingItem item) {
        DocumentReference ref = mItems.document(item._getId());

        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is succesfully deleted: " + item._getId());
                })
                .addOnFailureListener(failure -> {
                    Toast.makeText(this, "Item " + item._getId() + "cannot deleted.", Toast.LENGTH_LONG).show();
                });

        queryData();

    }

    private void updateItem(ShoppingItem item) {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearchView);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_button:
                Log.d(LOG_TAG, "Setting clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.cart:
                Log.d(LOG_TAG, "Cart clicked!");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(ShoppingItem item) {



        cartItems = (cartItems + 1);
        if (0 < cartItems) {
            countTextView.setText(String.valueOf(cartItems));
        } else {
            countTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);

        mItems.document(item._getId()).update("cartedCount", item.getCartedCount() + 1).addOnFailureListener(failure -> {
            Toast.makeText(this, "Item " + item._getId() + "cannot be changed.", Toast.LENGTH_LONG).show();
        });

        //mNotificationHelper.send(item.getName());
        queryData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }


    private void filterList(String status) {

        selectedFilter = status;
        ArrayList<ShoppingItem> filteredTypes = new ArrayList<ShoppingItem>();

        for (ShoppingItem shoppingItem : mItemsData) {
            if (shoppingItem.getRatedInfo().toLowerCase().contains(status)) {

                filteredTypes.add(shoppingItem);

            }

            ShoppingRateTypeAdapter adapter = new ShoppingRateTypeAdapter(getApplicationContext(), 0, filteredTypes);
            listView.setAdapter(adapter);
        }
    }


    public void allButton(View view) {
        selectedFilter = "all";
        ShoppingRateTypeAdapter adapter = new ShoppingRateTypeAdapter(getApplicationContext(), 0, mItemsData);
        listView.setAdapter(adapter);
        Log.d(LOG_TAG, "all clicked!");
    }

    public void ferfi_button(View view) {

        filterList("ferfi");
        Log.d(LOG_TAG, "ferfi clicked!");
    }

    public void noi_button(View view) {
        filterList("noi");
        Log.d(LOG_TAG, "noi clicked!");
    }

    public void fiu_button(View view) {
        filterList("fiu");
        Log.d(LOG_TAG, "fiu clicked!");
    }

    public void lany_button(View view) {
        filterList("lany");
        Log.d(LOG_TAG, "lany clicked!");
    }

    // @Override
    // protected void onPause() {
    //     super.onPause();

    //     SharedPreferences.Editor editor = preferences.edit();
    //     editor.putInt("cartItems", cartItems);
    //     editor.putInt("gridNum", gridNumber);
    //     editor.apply();

    //     Log.i(LOG_TAG, "onPause");
    // }
}