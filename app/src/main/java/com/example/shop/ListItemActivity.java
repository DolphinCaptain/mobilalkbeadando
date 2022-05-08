package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItemActivity extends AppCompatActivity
{
    ShoppingItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);
        getSelectedShape();
        setValues();

    }

    private void getSelectedShape()
    {
        Intent previousIntent = getIntent();
        String parsedStringID = previousIntent.getStringExtra("id");
        selectedItem = ShopListActivity.itemList.get(Integer.valueOf(parsedStringID));
    }

    private void setValues()
    {
        TextView tv = (TextView) findViewById(R.id.itemTitle);
        TextView tv1 = (TextView) findViewById(R.id.subTitle);
        TextView tv2 = (TextView) findViewById(R.id.ratingBar);
        TextView tv3 = (TextView) findViewById(R.id.price);
        ImageView iv = (ImageView) findViewById(R.id.itemImage);

        tv.setText(selectedItem.getName());
        tv1.setText(selectedItem.getInfo());
        tv2.setText(selectedItem.getPrice());
        tv3.setText(selectedItem.getRatedInfo());
        iv.setImageResource(selectedItem.getImageResource());
    }
}
