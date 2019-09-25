package com.example.testdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
   BarGraphView graph;
   Button stopRepeatPlay, normalPlay,reversePlay, repeatPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        graph = findViewById ( R.id.graph );
        float[] data = {19,78,30,100,150,45,180,55};
        graph.setData ( data );

        stopRepeatPlay = findViewById ( R.id.stopRepeatPlay );
        normalPlay = findViewById ( R.id.normalPlay );
        reversePlay = findViewById ( R.id.reversePlay );
        repeatPlay = findViewById ( R.id.repeatPlay );

        stopRepeatPlay.setOnClickListener ( (view) -> graph.stop () );
        normalPlay.setOnClickListener ( (view) -> graph.playGraph() );
        reversePlay.setOnClickListener ( (view) -> graph.playReverse () );
        repeatPlay.setOnClickListener ( (view) -> graph.playRepeat () );
    }
}
