package ddi.hackru.edu.drugtodrugapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class QuerriesListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querries_list_view);

        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("transfer");

        List<String> adversityList = (List<String>) bundle.get("adversityList");

        // add...
        ListView lv = (ListView) findViewById(R.id.lv);
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adversityList);
        lv.setAdapter(adapter);

        setTitle(bundle.getString("medication"));
    }
}
