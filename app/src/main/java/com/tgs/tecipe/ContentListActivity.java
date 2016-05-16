package com.tgs.tecipe;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tgs.tecipe.util.Item;
import com.tgs.tecipe.util.XMLHandler;

public class ContentListActivity extends Activity implements OnItemClickListener{


	private ContentListActivity _activity;
	private ListView listView=null;
	private ArrayList vegList=null;
	private ArrayList nonVegList=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		_activity=this;
		 listView=(ListView)findViewById(R.id.list_view);
		
		
		listView.setOnItemClickListener(this);
		

		new BackGroundWork().execute("");
	}
	
	class BackGroundWork extends AsyncTask<String, String, String>
	{
		ProgressDialog dialog=null;
		private XMLHandler handler=null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			dialog=new ProgressDialog(_activity);
			dialog.setTitle(R.string.loading);
			dialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			
			try {
				SAXParser parser=SAXParserFactory.newInstance().newSAXParser();
				handler=new XMLHandler();
				parser.parse(_activity.getAssets().open("recipe.xml"), handler);
				
				if(handler.getItemList().size()>0)
				{
					vegList=new  ArrayList<Item>();
					nonVegList=new  ArrayList<Item>();
					
					for (int i = 0; i < handler.getItemList().size(); i++) {
						
						if( handler.getItemList().get(i).getCategory().toString().equalsIgnoreCase("veg"))
						{
							vegList.add(handler.getItemList().get(i));
						}
						else{
							nonVegList.add(handler.getItemList().get(i));
						}
					}
				}
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			
			if(handler!=null)
			listView.setAdapter(new MyAdapter(_activity,handler));
		}
	}
	
	class MyAdapter extends BaseAdapter{

		Context context=null;
		ArrayList<Item> itemList=null;
		public MyAdapter(Context context, XMLHandler handler) {
			// TODO Auto-generated constructor stub
			itemList=handler.getItemList();
			this.context=context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return itemList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View contentView, ViewGroup arg2) {

			View view=null;
			if(contentView==null)
			{
				view=View.inflate(getApplicationContext(), R.layout.list_item_old,null);
				view.setTag(view);
				contentView=view;
				contentView.setTag(view);
			}
			else
			{
				view=(View)contentView.getTag();
			}
			
			TextView name_text=null;
			
			name_text=(TextView)view.findViewById(R.id.item_name);
			name_text.setText(""+itemList.get(arg0).getName());
			view.setTag(itemList.get(arg0));
			
			if(itemList.get(arg0).getCategory().toString().equalsIgnoreCase("veg"))
			{
				name_text.setTextColor(Color.GREEN);
			}
			else
			{
				name_text.setTextColor(Color.RED);
			}
			
			return view;
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		System.out.println("TEST "+((Item)arg1.getTag()).getName());
		Intent intent=new Intent(getApplicationContext(),PDFFileDisplay.class);
		//intent.putExtra("list", )
		/*if(((Item)arg1.getTag()).getCategory().toString().equalsIgnoreCase("veg"))
		{
		intent.putExtra("LIST", (Serializable)vegList);
		}
		else
		{
			intent.putExtra("LIST",(Serializable) nonVegList);
		}*/
		startActivity(intent);
	}
}
