package com.example.isuview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store the animal images
    private final ArrayList<Integer> imageIDarray;

    //to store the list of countries
    private final ArrayList<String> nameArray;

    //to store the list of countries
    private final ArrayList<String> infoArray;
    public CustomListAdapter(Activity context, ArrayList<String> nameArrayParam, ArrayList<String> infoArrayParam, ArrayList<Integer> imageIDArrayParam){

        super(context,R.layout.listview_row , nameArrayParam);

        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.txtviewName);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.txtviewComment);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.avaIMG);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray.get(position));
        infoTextField.setText(infoArray.get(position));
        imageView.setImageResource(imageIDarray.get(position));

        return rowView;

    };

}
