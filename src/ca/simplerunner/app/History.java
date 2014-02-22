package ca.simplerunner.app;

import java.util.ArrayList;
import java.util.List;

import ca.simplerunner.R;
import ca.simplerunner.database.Database;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Class that handles showing the history of past runs
 * to the user.
 * 
 * @author Abe Friesen
 */
public class History extends ListActivity {
	
	Database db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        db = new Database(this);
        getPastRuns();
	}
	
	/*
	 * Retrieve past runs from database
	 */
	private void getPastRuns() {
		new Thread(new Runnable() {
			public void run() {
				final ArrayList<RunStat> stats = db.getRunStats();
				runOnUiThread(new Runnable() {
					public void run() {
						populateListView(stats);
					}
				});
			}
		}).start();
	}
	
	/*
	 * Populate the list view with past runs
	 */
	private void populateListView(ArrayList<RunStat> stats) {
		ListView listview = (ListView) findViewById(android.R.id.list);
		StatAdapter adapter = new StatAdapter(this, R.layout.listview, stats);
        listview.setAdapter(adapter);
        listview.setSelector(R.drawable.listselector);
	}
	
	/*
	 * Populate Action bar with menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.back_action, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	/*
	 * Handle menu item chosen
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_back:
			db.closeDB();
			finish();
			return true;
		default:
			return false;
		}
	}
	
	@Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
		StatAdapter adp = (StatAdapter) l.getAdapter();
		RunStat stat = adp.getItem(position);
        long runID = stat.getID();
//        v.setBackground(getResources().getDrawable(R.drawable.selected));
//        v.setBackgroundResource(R.drawable.selected);
        loadViewRunActivity(runID);
    }
	
	public void loadViewRunActivity(long runID) {
		db.closeDB();
		Intent i = new Intent(History.this, RunView.class);
		i.putExtra("runID", runID);
		startActivity(i);
	}
	
	private class StatAdapter extends ArrayAdapter<RunStat> {

		private LayoutInflater inflater;
		public StatAdapter(Context context, int textViewResourceId,
				List<RunStat> objects) {
			super(context, textViewResourceId, objects);
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			
			View layout = convertView;
			RunStat stat = getItem(position);
			
	        //Inflate the view
	        if(convertView==null)
	        {
	        	layout = inflater.inflate(R.layout.listview, null);
	        }
	        
	        if(position % 2 == 0) {
	        	layout.setBackgroundResource(R.drawable.listselector);
	        }
	        else {
	        	layout.setBackgroundResource(R.drawable.listselector2);
	        }

	        TextView dateText = (TextView) layout.findViewById(R.id.dateText);
	        TextView distanceText = (TextView) layout.findViewById(R.id.distanceText);

	        dateText.setText(stat.getDate());
	        distanceText.setText(Main.formatDistance(stat.getDistance()));

	        return layout;
	    }
	}
}
