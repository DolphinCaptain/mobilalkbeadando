package com.example.shop;


  import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
  import android.widget.Filter;
  import android.widget.Filterable;
  import android.widget.ImageView;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import com.bumptech.glide.Glide;

  import java.util.ArrayList;
  import java.util.List;

public class ShoppingRateTypeAdapter extends ArrayAdapter<ShoppingItem> implements Filterable
{

    private ArrayList<ShoppingItem> mShoppingData;
    private ArrayList<ShoppingItem> mSoppingDataAll;
    private Context mContext;
    private int lastPosition = -1;


    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ShoppingItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mSoppingDataAll.size();
                results.values = mSoppingDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(ShoppingItem item : mSoppingDataAll) {
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mShoppingData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };


    public ShoppingRateTypeAdapter(Context context, int resource, List<ShoppingItem> shoppingItemList)
    {

        super(context,resource,shoppingItemList);

        this.mShoppingData= (ArrayList<ShoppingItem>) shoppingItemList;
        this.mContext=context;
        this.mSoppingDataAll= (ArrayList<ShoppingItem>) shoppingItemList;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ShoppingItem shoppingItem = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.itemTitle);
        TextView tv1 = (TextView) convertView.findViewById(R.id.subTitle);
        TextView tv2 = (TextView) convertView.findViewById(R.id.ratingBar);
        TextView tv3 = (TextView) convertView.findViewById(R.id.price);
        ImageView iv = (ImageView) convertView.findViewById(R.id.itemImage);

        tv.setText(shoppingItem.getName());
        tv1.setText(shoppingItem.getInfo());
        tv2.setText(shoppingItem.getPrice());
        tv3.setText(shoppingItem.getRatedInfo());
        iv.setImageResource(shoppingItem.getImageResource());




        convertView.findViewById(R.id.add_to_cart).setOnClickListener(view -> ((ShopListActivity)mContext).updateAlertIcon(shoppingItem));

        return convertView;
    }
}