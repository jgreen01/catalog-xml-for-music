package edu.umich.umd.engin.greenbj.xmlMusicCatalog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class XmlMusicCatalogActivity extends Activity 
		implements OnClickListener {
	
	private static String TAG = "xmlMusicCalalog";
	
    private Spinner		titSpinner; 
    private TextView	artTextView;
    private TextView 	couTextView;
    private TextView 	comTextView;
    private TextView 	priTextView;
    private TextView 	yeaTextView;
    private Button		delButton;
    private Button		addButton;
    
    private int	itemSelected;
    private ArrayAdapter <CharSequence> adapter;
    private String xmlFile = Environment.getExternalStorageDirectory()+ "/musicCatalog.xml";
    private xmlManager xmlData = new xmlManager(xmlFile);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        adapter =  new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        titSpinner = (Spinner) this.findViewById(R.id.albumSpinner);
        titSpinner.setAdapter(adapter);
        
        delButton = (Button) this.findViewById(R.id.deleteButton);
        delButton.setOnClickListener(this);
        
        addButton = (Button) this.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        
        artTextView = (TextView) this.findViewById(R.id.textViewArtist);
        couTextView = (TextView) this.findViewById(R.id.textViewCountry);
        comTextView = (TextView) this.findViewById(R.id.textViewCompany);
        priTextView = (TextView) this.findViewById(R.id.textViewPrice);
        yeaTextView = (TextView) this.findViewById(R.id.textViewYear);
        
        for(int i = 0; i < xmlData.getCount(); i++)
        	adapter.add(xmlData.getTitList().get(i));
        
        titSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                itemSelected = titSpinner.getSelectedItemPosition();
                updateValues(itemSelected);
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        
    }
    
	private void updateValues(int index){
		if(xmlData.getCount() != 0){
			artTextView.setText(xmlData.getArtList().get(index));
	        couTextView.setText(xmlData.getCouList().get(index));
	        comTextView.setText(xmlData.getComList().get(index));
	        priTextView.setText(String.format("$%.2f", xmlData.getPriList().get(index)));
	        yeaTextView.setText(xmlData.getYeaList().get(index).toString());
		} else {
			artTextView.setText("Artist");
			couTextView.setText("Coutry");
			comTextView.setText("Company");
			priTextView.setText("$0");
			yeaTextView.setText("2012");
		}
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick: " + v);
		
		if(v.getId() == R.id.addButton){
			addAlbumDialog();
		}
			
		if(v.getId() == R.id.deleteButton){
			
			// remove item from list
			if(xmlData.getCount() != 0) // you can't remove nothing
				adapter.remove(xmlData.getTitList().get(itemSelected));
			
			// to delete item from xml
			xmlData.remove(itemSelected); // itemSelected hasn't changed yet
			
			// set TextViews to new item
			itemSelected = titSpinner.getSelectedItemPosition(); // now it's changed
			
			// if itemSelected if beyond the size of the array 
			// when at the end of the list
			if(itemSelected >= xmlData.getCount())
				itemSelected--;
			
			updateValues(itemSelected);
			
		}	
	}
	
	private void addAlbumDialog(){
		
		LayoutInflater factory = LayoutInflater.from(this);
		View newAlbumView = factory.inflate(R.layout.adddialog, null);
		
		final EditText titEditText = (EditText) newAlbumView.findViewById(R.id.titleEdit);
		final EditText artEditText = (EditText) newAlbumView.findViewById(R.id.artistEdit);
		final EditText couEditText = (EditText) newAlbumView.findViewById(R.id.countryEdit);
		final EditText comEditText = (EditText) newAlbumView.findViewById(R.id.companyEdit);
		final EditText priEditText = (EditText) newAlbumView.findViewById(R.id.priceEdit);
		final EditText yeaEditText = (EditText) newAlbumView.findViewById(R.id.yearEdit);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Add Album");
		alert.setView(newAlbumView);
		
		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton){
				
				String titVal = titEditText.getText().toString();
				String artVal = artEditText.getText().toString();
				String couVal = couEditText.getText().toString();
				String comVal = comEditText.getText().toString();
				
				double priVal = 0.0;
				if(!priEditText.getText().toString().equals(""))
					priVal = Double.valueOf(priEditText.getText().toString().trim()).doubleValue();
				
				int yeaVal = 0;
				if(!yeaEditText.getText().toString().equals(""))
					yeaVal = Integer.parseInt(yeaEditText.getText().toString());

				xmlData.add(titVal, artVal, couVal, comVal, priVal, yeaVal);
				
				// add new value to list
				if(!titVal.equals(""))
					adapter.insert(titVal, 0);
				else
					adapter.insert("unknown", 0);
				
				updateValues(itemSelected);
				
    		}
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton){
				// do nothing
			}
		});
		
		alert.show();
		
	}
	
}